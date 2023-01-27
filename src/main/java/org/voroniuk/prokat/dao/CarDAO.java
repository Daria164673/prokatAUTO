package org.voroniuk.prokat.dao;

import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Car Data Access Object interface
 *
 * @author D. Voroniuk
 */

public interface CarDAO {

    public Car findCarById(int id);

    public List<Car> findCars(int brandId, int q_class_id, Map<String, Boolean> sortMap, int start, int offset);
    public List<Car> findCars(List<String> brandsId, List<String> qClassId, Map<String, Boolean> sortMap, int start, int offset);

    public int countCars(int brandId, int q_class_id);

    public int countCars(List<String> brandsId, List<String> qClassId);

    public boolean updateCarState(Connection connection, int car_id, Car.State state) throws SQLException;

    public boolean saveCar(Car car);

    public boolean updateCar(Car car);

    public boolean updateCarPrice(Car car, double price);

    public boolean insertCarPrice(Car car, double price);

    public boolean setCarPrice(Car car, double price);

    public Integer findCarPrice(Car car);

    public boolean deleteCarById(int car_id);

    public Car.State getCarStateById(Connection connection, int car_id);

}
