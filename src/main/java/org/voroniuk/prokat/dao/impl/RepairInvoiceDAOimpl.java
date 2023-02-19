package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.RepairInvoice;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.SaveOrderCommand;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Repair invoice Data Access Object implementation
 * provided methods for
 * finding, creating Repair invoices
 * update return date
 *
 * @author D. Voroniuk
 */

public class RepairInvoiceDAOimpl implements RepairInvoiceDAO {

    private final DBManager dbManager;
    private final CarDAO carDAO;
    private static final Logger LOG = Logger.getLogger(SaveOrderCommand.class);

    public RepairInvoiceDAOimpl(DBManager dbManager, CarDAO carDAO) {
        this.dbManager = dbManager;
        this.carDAO = carDAO;
    }

    /**
     * saveRepairInvoice method - insert repair invoice into db,
     * and update car state to ON_REPAIR in the same transaction
     *
     * @param repairInvoice
     *
     * @author D. Voroniuk
     */
    @Override
    public boolean saveRepairInvoice(RepairInvoice repairInvoice) {
        String sql = "INSERT INTO repair_invoices (date, car_id, " +
                        "repair_info, amount, contractor) " +
                        "values (?, ?, ?, ?, ?)";

        try (Connection connection = dbManager.getConnection()) {

            connection.setAutoCommit(false);

            Car.State currState = carDAO.getCarStateById(connection, repairInvoice.getCar().getId());
            if (currState!= Car.State.FREE) {
                connection.setAutoCommit(true);
                throw new Exception("Car id=" + repairInvoice.getCar().getId() + " is not FREE");
            }

            try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)){
                statement.setDate(1, new Date(repairInvoice.getDate().getTime()));
                statement.setInt(2, repairInvoice.getCar().getId());
                statement.setString(3, repairInvoice.getRepairInfo());
                statement.setInt(4, (int) (repairInvoice.getAmount()*100));
                statement.setString(5, repairInvoice.getContractor());

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        repairInvoice.setId(resultSet.getInt(1));
                    }
                }

                 if (!carDAO.updateCarState(connection, repairInvoice.getCar().getId(), Car.State.ON_REPAIR)) {
                    throw new SQLException();
                }

                connection.commit();
                connection.setAutoCommit(true);

                LOG.info("Repair invoice has been added");
            } catch (Exception e){
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public RepairInvoice findRepairInvoiceById(int id) {
        String sql =  "SELECT id, date, car_id, " +
                "repair_info, amount, contractor, return_date " +
                "FROM repair_invoices as repair_invoices " +
                "WHERE  repair_invoices.id = ? ";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createRepairInvoice(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find RepairInvoice by id: " + id);
        }
        return null;
    }

    /**
     * createRepairInvoice method - creates repair invoice instance,
     * filled from resultset
     *
     * @param resultSet
     *
     * @author D. Voroniuk
     */
    private RepairInvoice createRepairInvoice(ResultSet resultSet) throws SQLException {

        return RepairInvoice.builder()
                .id(resultSet.getInt("id"))
                .car(carDAO.findCarById(resultSet.getInt("car_id")))
                .date(resultSet.getDate("date"))
                .returnDate(resultSet.getDate("return_date"))
                .repairInfo(resultSet.getString("repair_info"))
                .amount((double) resultSet.getInt("amount")/100)
                .contractor(resultSet.getString("contractor"))
                .build();
    }

    @Override
    public List<RepairInvoice> findRepairInvoices(int start, int offset) {
        List<RepairInvoice> res = new LinkedList<>();
        String sql =    "SELECT id, date, car_id, repair_info, amount, contractor, return_date " +
                "FROM repair_invoices as repair_invoices " +
                "LIMIT ?, ?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, start);
            statement.setInt(2, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createRepairInvoice(resultSet));
                }
            } catch (Exception e) {
                LOG.warn(e);
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return res;
    }

    @Override
    public int countRepairInvoices() {
        int result = 0;

        String sql = "SELECT COUNT(*) FROM repair_invoices as repair_invoices ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    result = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }

        return result;
    }

    /**
     * updateReturnFromRepair method - set current date as return_date,
     * and update car state to FREE in the same transaction
     *
     * @param repair_id
     * @param car_id
     *
     * @author D. Voroniuk
     */
    @Override
    public boolean updateReturnFromRepair(int repair_id, int car_id) {
        String sql = "UPDATE repair_invoices " +
                "SET return_date=? WHERE id=?";

        try (Connection connection = dbManager.getConnection()) {

            connection.setAutoCommit(false);
            try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                statement.setDate(1, new Date(System.currentTimeMillis()));
                statement.setInt(2, repair_id);

                statement.executeUpdate();

                if (!carDAO.updateCarState(connection, car_id, Car.State.FREE)) {
                    throw new SQLException();
                }

                connection.commit();
                connection.setAutoCommit(true);

                LOG.info("Repair invoice has been updated (returned car)");
            } catch (SQLException e){
                connection.rollback();
                connection.setAutoCommit(true);
                throw e;
            }
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public CarDAO getCarDAO() {
        return carDAO;
    }
}
