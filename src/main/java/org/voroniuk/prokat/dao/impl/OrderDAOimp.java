package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.entity.*;

import java.sql.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

/**
 * Order Data Access Object implementation
 * provided methods for
 * finding, creating Orders,
 * updates orders states
 *
 * @author D. Voroniuk
 */

public class OrderDAOimp implements OrderDAO {

    private final DBManager dbManager;
    private final CarDAO carDAO;
    private final UserDAO userDAO;
    private final Logger LOG = Logger.getLogger(OrderDAOimp.class);

    public OrderDAOimp(DBManager dbManager, CarDAO carDAO, UserDAO userDAO) {
        this.dbManager = dbManager;
        this.carDAO = carDAO;
        this.userDAO = userDAO;
    }

    @Override
    public CarDAO getCarDAO() {
        return carDAO;
    }

    /**
     * saveOrder method - insert new row into table orders
     * and update car state to ORDERED in the same transaction
     *
     * @param order - saving instance
     *
     * @author D. Voroniuk
     */
    public boolean saveOrder(Order order) {

        String sql = "insert into orders (date , user_id, car_id, passport_data, " +
                        "with_driver, term, state, amount) " +
                        "values (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dbManager.getConnection()) {

            connection.setAutoCommit(false);

            Car.State currState = carDAO.getCarStateById(connection, order.getCar().getId());
            if (currState!= Car.State.FREE) {
                connection.setAutoCommit(true);
                throw new Exception("Car id=" + order.getCar().getId() + " is not FREE");
            }

            try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {
                int k=0;
                statement.setDate(++k, new Date(order.getDate().getTime()));
                statement.setInt(++k, order.getUser().getId());
                statement.setInt(++k, order.getCar().getId());
                statement.setString(++k, order.getPassportData());
                statement.setBoolean(++k, order.getWithDriver());
                statement.setInt(++k, order.getTerm());
                statement.setString(++k, order.getState().name().toLowerCase());
                statement.setInt(++k, (int) (order.getAmount() * 100));

                statement.executeUpdate();

                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        order.setId(resultSet.getInt(1));
                    }
                }

                if (!carDAO.updateCarState(connection, order.getCar().getId(), Car.State.ORDERED)) {
                    throw new SQLException();
                }

                connection.commit();
                connection.setAutoCommit(true);
                LOG.info("Order has been added");
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
    public List<Order> findOrders(int user_id, Order.State state, int start, int offset) {
        List<Order> res = new LinkedList<>();
        String sql =    "SELECT orders.id, orders.date, orders.user_id, orders.car_id, orders.passport_data, " +
                "orders.with_driver, orders.term, orders.state, orders.amount, orders.return_date, orders.reject_reason " +

                "FROM orders as orders " +

                "WHERE CASE WHEN ?>0 THEN orders.user_id = ? ELSE TRUE END " +
                "AND CASE WHEN ? THEN orders.state = ? ELSE TRUE END " +

                "LIMIT ?, ?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int k=0;
            statement.setInt(++k, user_id);
            statement.setInt(++k, user_id);
            if (state == null) {
                statement.setBoolean(++k, false);
                statement.setString(++k, "");
            } else {
                statement.setBoolean(++k, true);
                statement.setString(++k, state.toString().toLowerCase());
            }
            statement.setInt(++k, start);
            statement.setInt(++k, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createOrder(resultSet));
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
    public int countOrders(int user_id, Order.State state) {
        int result = 0;

        String sql = "SELECT COUNT(*) FROM orders as orders " +

                "WHERE CASE WHEN ?>0 THEN orders.user_id = ? ELSE TRUE END " +
                "AND CASE WHEN ? THEN orders.state = ? ELSE TRUE END ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int k=0;
            statement.setInt(++k, user_id);
            statement.setInt(++k, user_id);
            if (state == null) {
                statement.setBoolean(++k, false);
                statement.setString(++k, "");
            } else {
                statement.setBoolean(++k, true);
                statement.setString(++k, state.toString().toLowerCase());
            }

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

    public Order findOrderById(int id) {
        String sql =  "SELECT id, date, user_id, car_id, passport_data, " +
                "with_driver, term, state, amount, return_date, reject_reason " +
                "FROM orders as orders " +
                "WHERE  orders.id = ? ";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createOrder(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find order by id: " + id);
        }
        return null;
    }

    /**
     * createOrder method - create instance
     * and filled it from result set
     *
     * @param resultSet
     *
     * @author D. Voroniuk
     */
    private Order createOrder(ResultSet resultSet) throws SQLException {

        Order.State state = null;
        try {
            state = Order.State.valueOf(resultSet.getString("state").toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
        }

        return Order.builder()
                .id(resultSet.getInt("id"))
                .date(resultSet.getDate("date"))
                .user(userDAO.findUserById(resultSet.getInt("user_id")))
                .car(carDAO.findCarById(resultSet.getInt("car_id")))
                .passportData(resultSet.getString("passport_data"))
                .withDriver(resultSet.getBoolean("with_driver"))
                .term(resultSet.getInt("term"))
                .amount((double) resultSet.getInt("amount")/100)
                .returnDate(resultSet.getDate("return_date"))
                .reject_reason(resultSet.getString("reject_reason"))
                .state(state)
                .build();
    }

    /**
     * updateRejectOrder method - set order's status to REJECTED,
     * set reject_reason into table orders
     * and update car state to FREE in the same transaction
     *
     * @param order_id
     * @param car_id
     * @param reject_reason
     *
     * @author D. Voroniuk
     */
    @Override
    public boolean updateRejectOrder(int order_id, int car_id, String reject_reason) {
        String sql = "UPDATE orders " +
                "SET state=?, reject_reason=? WHERE id=?";

        try (Connection connection = dbManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                int k=0;
                statement.setString(++k, "rejected");
                statement.setString(++k, reject_reason);
                statement.setInt(++k, order_id);

                statement.executeUpdate();

                if (!carDAO.updateCarState(connection, car_id, Car.State.FREE)) {
                    throw new SQLException();
                }

                connection.commit();
                connection.setAutoCommit(true);
                LOG.info("Order has been updated (rejection)");
            } catch (SQLException e) {
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

    /**
     * updateReturnOrder method - set order's status to FINISHED
     * and update car state to FREE in the same transaction
     *
     * @param order_id
     * @param car_id
     *
     * @author D. Voroniuk
     */
    @Override
    public boolean updateReturnOrder(int order_id, int car_id) {

        String sql = "UPDATE orders " +
                "SET state=?, return_date=? WHERE id=?";

        try (Connection connection = dbManager.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

                int k=0;
                statement.setString(++k, "finished");
                statement.setDate(++k, new Date(System.currentTimeMillis()));
                statement.setInt(++k, order_id);

                statement.executeUpdate();

                 if (!carDAO.updateCarState(connection, car_id, Car.State.FREE)) {
                    throw new SQLException();
                }

                connection.commit();
                connection.setAutoCommit(true);

                LOG.info("Order has been updated (returned car)");
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
    public boolean updateOrderState(int order_id, Order.State state) {

        String sql = "UPDATE orders " +
                "SET state=? WHERE id=?";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, state.toString().toLowerCase());
            statement.setInt(2, order_id);

            statement.executeUpdate();

            LOG.info("Order has been updated");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

}
