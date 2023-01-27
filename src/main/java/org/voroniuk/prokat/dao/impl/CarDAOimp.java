package org.voroniuk.prokat.dao.impl;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.QualityClass;
import org.voroniuk.prokat.utils.Utils;

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
    private static final Logger LOG = Logger.getLogger(CarDAOimp.class);

    public Car findCarById(int id) {
        String sql =  "SELECT cars.id as car_id, brand_id, brands.name as brand_name, q_class_id, " +
                "q_classes.name as q_class_name, model, car_number, IFNULL(prices.price,0) as price, " +
                "IFNULL(cars.curr_state,'') as curr_state " +

                "FROM cars as cars " +

                "LEFT JOIN brands as brands " +
                "ON cars.brand_id = brands.id " +

                "LEFT JOIN q_classes as q_classes " +
                "ON cars.q_class_id = q_classes.id " +

                "LEFT JOIN prices as prices " +
                "ON cars.id = prices.car_id " +

                "WHERE  cars.id = ? ";
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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

    public List<Car> findCars(int brandId, int q_class_id, Map<String, Boolean> sortMap, int start, int offset) {
        List<Car> res = new LinkedList<>();
        String sql =    "SELECT cars.id as car_id, brand_id, brands.name as brand_name, q_class_id, " +
                "q_classes.name as q_class_name, model, car_number, IFNULL(prices.price,0) as price, " +
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

                Utils.getOrderByQueryText(sortMap) +

                "LIMIT ?, ?; ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, brandId);
            statement.setInt(2, brandId);
            statement.setInt(3, q_class_id);
            statement.setInt(4, q_class_id);
            statement.setInt(5, start);
            statement.setInt(6, offset);

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

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            statement.setInt(1, brandId);
            statement.setInt(2, brandId);
            statement.setInt(3, q_class_id);
            statement.setInt(4, q_class_id);

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

        Car car = new Car();
        car.setId(resultSet.getInt("car_id"));

        Brand brand = new Brand(resultSet.getString("brand_name"),resultSet.getInt("brand_id"));
        car.setBrand(brand);

        QualityClass q_class = new QualityClass(resultSet.getString("q_class_name"), resultSet.getInt("q_class_id"));
        car.setQualityClass(q_class);

        car.setModel(resultSet.getString("model"));
        car.setCar_number(resultSet.getString("car_number"));

        car.setPrice((double) resultSet.getInt("price")/100);

        try {
            car.setCurr_state(Car.State.valueOf(resultSet.getString("curr_state").toUpperCase()));
        } catch (IllegalArgumentException e) {

        }

        return car;
    }

    @Override
    public boolean saveCar(Car car) {
        String sql = "INSERT INTO cars (brand_id, q_class_id, model, car_number, curr_state) VALUES (?,?,?,?,?)";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, car.getBrand().getId());
            statement.setInt(2, car.getQualityClass().getId());
            statement.setString(3, car.getModel());
            statement.setString(4, car.getCar_number());
            if (car.getCurr_state()==null) {
                statement.setString(5, "free");
            } else {
                statement.setString(5, car.getCurr_state().name().toLowerCase());
            }
            statement.executeUpdate();

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                if (resultSet.next()) {
                    car.setId(resultSet.getInt(1));
                }
            }

            LOG.info("Car has been added");
        } catch (SQLException e) {
            LOG.warn(e);
            return false;
        }
        return true;
    }

    @Override
    public boolean updateCar(Car car) {
        String sql = "UPDATE cars " +
                "SET brand_id=?, q_class_id=?, model=?, car_number=? " +
                "WHERE id=?; ";

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setInt(1, car.getBrand().getId());
            statement.setInt(2, car.getQualityClass().getId());
            statement.setString(3, car.getModel());
            statement.setString(4, car.getCar_number());
            statement.setInt(5, car.getId());

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

        try (Connection connection = DBManager.getInstance().getConnection();
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

        try (Connection connection = DBManager.getInstance().getConnection();
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

        try (Connection connection = DBManager.getInstance().getConnection();
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
        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

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
                "q_classes.name as q_class_name, model, car_number, IFNULL(prices.price,0) as price, " +
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

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            //Array brandIdArray = connection.createArrayOf("INT", brandsId.toArray(new Integer[0]));

            statement.setInt(1, brandsId.size());
            //statement.setArray(2, brandIdArray);
            statement.setInt(2, qClassId.size());
            //statement.setInt(3, q_class_id);
            statement.setInt(3, start);
            statement.setInt(4, offset);

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

        try (Connection connection = DBManager.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);) {

            //Array brandIdArray = connection.createArrayOf("INT", brandsId.toArray(new Integer[0]));

            statement.setInt(1, brandsId.size());
            //statement.setArray(2, brandIdArray);
            //statement.setInt(2, q_class_id);
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
        try (PreparedStatement statement = connection.prepareStatement(sql);) {

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
