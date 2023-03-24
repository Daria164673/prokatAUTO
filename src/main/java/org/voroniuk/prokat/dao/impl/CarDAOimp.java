package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.QualityClass;
import org.voroniuk.prokat.utils.Utils;

import java.io.File;
import java.sql.*;
import java.util.*;

/**
 * Car Data Access Object implementation
 * provided methods for
 * finding, creating Cars,
 * updates car state, updates price
 *
 * @author D. Voroniuk
 */

public class CarDAOimp implements CarDAO {
    private final DBManager dbManager;

    private static final Logger LOG = Logger.getLogger(CarDAOimp.class);

    public CarDAOimp(DBManager dbManager) {
        this.dbManager = dbManager;
    }

    public Car findCarById(int id) {
        String sql =  "SELECT cars.id as car_id, brand_id, brands.name as brand_name, q_class_id, " +
                "q_classes.name as q_class_name, model, car_number, img, IFNULL(prices.price,0) as price, " +
                "IFNULL(cars.curr_state,'') as curr_state " +

                "FROM cars as cars " +

                "LEFT JOIN brands as brands " +
                "ON cars.brand_id = brands.id " +

                "LEFT JOIN q_classes as q_classes " +
                "ON cars.q_class_id = q_classes.id " +

                "LEFT JOIN prices as prices " +
                "ON cars.id = prices.car_id " +

                "WHERE  cars.id = ? ";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return createCar(resultSet);
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find car by id: " + id);
        }
        return null;
    }

    public List<Car> findCars(int brandId, int q_class_id, Car.State state, Map<String, Boolean> sortMap, int start, int offset) {
        List<Car> res = new LinkedList<>();
        String sql =    "SELECT cars.id as car_id, brand_id, brands.name as brand_name, q_class_id, " +
                "q_classes.name as q_class_name, model, car_number, img, IFNULL(prices.price,0) as price, " +
                "IFNULL(cars.curr_state,'') as curr_state "+

                "FROM cars as cars " +

                "LEFT JOIN brands as brands " +
                "ON cars.brand_id = brands.id " +

                "LEFT JOIN q_classes as q_classes " +
                "ON cars.q_class_id = q_classes.id " +

                "LEFT JOIN prices as prices " +
                "ON cars.id = prices.car_id " +

                "WHERE CASE WHEN ?>0 THEN brand_id = ? ELSE TRUE END " +
                "AND CASE WHEN ?>0 THEN q_class_id = ? ELSE TRUE END " +
                "AND CASE WHEN ?<>'' THEN IFNULL(cars.curr_state,'') = ? ELSE TRUE END " +

                Utils.getOrderByQueryText(sortMap) +

                "LIMIT ?, ?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int k=0;
            statement.setInt(++k, brandId);
            statement.setInt(++k, brandId);
            statement.setInt(++k, q_class_id);
            statement.setInt(++k, q_class_id);
            statement.setString(++k, state==null?"":state.name().toLowerCase(Locale.ROOT));
            statement.setString(++k, state==null?"":state.name().toLowerCase(Locale.ROOT));
            statement.setInt(++k, start);
            statement.setInt(++k, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createCar(resultSet));
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
    public int countCars(int brandId, int q_class_id) {
        int result = 0;

        String sql = "SELECT COUNT(*) FROM cars as cars " +
                "WHERE CASE WHEN ?>0 THEN brand_id = ? ELSE TRUE END " +
                "AND CASE WHEN ?>0 THEN q_class_id = ? ELSE TRUE END ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int k=0;
            statement.setInt(++k, brandId);
            statement.setInt(++k, brandId);
            statement.setInt(++k, q_class_id);
            statement.setInt(++k, q_class_id);

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

    @Override
    public boolean updateCarState(Connection connection, int car_id, Car.State state) throws SQLException{
        String sql = "UPDATE cars " +
                "SET curr_state=? WHERE id=?";

        try (PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, state.toString().toLowerCase());
            statement.setInt(2, car_id);

            statement.executeUpdate();

            LOG.info("Delivery has been updated");
        } catch (SQLException e) {
            LOG.warn(e);
            throw e;
        }
        return true;
    }

    public Car createCar(ResultSet resultSet) throws SQLException {

        Brand brand = new Brand(resultSet.getString("brand_name"),resultSet.getInt("brand_id"));
        QualityClass q_class = new QualityClass(resultSet.getString("q_class_name"), resultSet.getInt("q_class_id"));

        Car.State currState=null;
        try {
            currState = Car.State.valueOf(resultSet.getString("curr_state").toUpperCase());
        } catch (IllegalArgumentException e) {
            LOG.warn(e);
        }

        return Car.builder()
                .id(resultSet.getInt("car_id"))
                .brand(brand)
                .qualityClass(q_class)
                .model(resultSet.getString("model"))
                .car_number(resultSet.getString("car_number"))
                .price((double) resultSet.getInt("price")/100)
                .imgPath(resultSet.getString("img"))
                .curr_state(currState)
                .build();
    }

    @Override
    public boolean saveCar(Car car) {
        String sql = "INSERT INTO cars (brand_id, q_class_id, model, car_number, curr_state, img) VALUES (?,?,?,?,?,?)";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            int k=0;
            statement.setInt(++k, car.getBrand().getId());
            statement.setInt(++k, car.getQualityClass().getId());
            statement.setString(++k, car.getModel());
            statement.setString(++k, car.getCar_number());
            if (car.getCurr_state()==null) {
                statement.setString(++k, "free");
            } else {
                statement.setString(++k, car.getCurr_state().name().toLowerCase());
            }
            statement.setString(++k, car.getImgPath());
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    car.setId(resultSet.getInt(1));
                }
            }

            LOG.info("Car has been added");
        } catch (Exception e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCar(Car car) {
        String sql = "UPDATE cars " +
                "SET brand_id=?, q_class_id=?, model=?, car_number=?, img=? " +
                "WHERE id=?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            int k=0;
            statement.setInt(++k, car.getBrand().getId());
            statement.setInt(++k, car.getQualityClass().getId());
            statement.setString(++k, car.getModel());
            statement.setString(++k, car.getCar_number());
            statement.setString(++k, car.getImgPath());
            statement.setInt(++k, car.getId());

            statement.executeUpdate();

            LOG.info("Delivery has been updated");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean deleteCarById(int car_id) {
        String sql = "DELETE FROM cars WHERE id=?";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, car_id);
            statement.executeUpdate();

            LOG.info("Car has been deleted");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCarPrice(Car car, double price) {
        String sql = "UPDATE prices SET price=? WHERE car_id=?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, (int) (car.getPrice()*100));
            statement.setInt(2, car.getId());

            statement.executeUpdate();

            LOG.info("Car price has been updated");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean setCarPrice(Car car, double price) {
        Integer currPrice = findCarPrice(car);
        if (currPrice==null) {
            return insertCarPrice(car, price);
        } else {
            return updateCarPrice(car, price);
        }
    }

    @Override
    public boolean insertCarPrice(Car car, double price) {
        String sql = "INSERT INTO prices (car_id, price) VALUES (?,?)";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, car.getId());
            statement.setInt(2, (int) (car.getPrice()*100));
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    car.setId(resultSet.getInt(1));
                }
            }
            LOG.info("Car price has been added");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public Integer findCarPrice(Car car) {
        String sql =    "SELECT prices.price FROM prices " +
                "WHERE prices.car_id=?";
        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, car.getId());
            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    return resultSet.getInt("price");
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
        }
        return null;
    }

    @Override
    public List<Car> findCars(List<String> brandsId, List<String> qClassId, Map<String, Boolean> sortMap, int start, int offset) {
        List<Car> res = new LinkedList<>();
        String sql =    "SELECT cars.id as car_id, brand_id, brands.name as brand_name, q_class_id, " +
                "q_classes.name as q_class_name, model, car_number, img, IFNULL(prices.price,0) as price, " +
                "IFNULL(cars.curr_state,'') as curr_state "+

                "FROM cars as cars " +

                "LEFT JOIN brands as brands " +
                "ON cars.brand_id = brands.id " +

                "LEFT JOIN q_classes as q_classes " +
                "ON cars.q_class_id = q_classes.id " +

                "LEFT JOIN prices as prices " +
                "ON cars.id = prices.car_id " +

                "WHERE CASE WHEN ?>0 THEN " + Utils.getINQueryText(brandsId, "brand_id") + " ELSE TRUE END " +
                "AND CASE WHEN ?>0 THEN " + Utils.getINQueryText(qClassId, "q_class_id") + " ELSE TRUE END " +

                Utils.getOrderByQueryText(sortMap) +

                "LIMIT ?, ?; ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            int k=0;
            statement.setInt(++k, brandsId.size());
            statement.setInt(++k, qClassId.size());
            statement.setInt(++k, start);
            statement.setInt(++k, offset);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                while (resultSet.next()) {
                    res.add(createCar(resultSet));
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
    public int countCars(List<String> brandsId, List<String> qClassId) {
        int result = 0;

        String sql = "SELECT COUNT(*) FROM cars as cars " +
                "WHERE CASE WHEN ?>0 THEN " + Utils.getINQueryText(brandsId, "brand_id") + " ELSE TRUE END " +
                "AND CASE WHEN ?>0 THEN " + Utils.getINQueryText(qClassId, "q_class_id") + " ELSE TRUE END ";

        try (Connection connection = dbManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, brandsId.size());
            statement.setInt(2, qClassId.size());

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

    @Override
    public Car.State getCarStateById(Connection connection, int car_id) {
        String sql =  "SELECT cars.id as car_id, IFNULL(cars.curr_state,'') as curr_state " +
                        "FROM cars as cars " +
                        "WHERE  cars.id = ? ";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, car_id);

            statement.executeQuery();
            try (ResultSet resultSet = statement.getResultSet()) {

                if (resultSet.next()) {
                    try {
                        return Car.State.valueOf(resultSet.getString("curr_state").toUpperCase());
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            LOG.warn(e);
            LOG.info("Can't find car state, id: " + car_id);
        }
        return null;
    }
}
