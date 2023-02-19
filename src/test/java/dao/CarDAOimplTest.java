package dao;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.QualityClass;

import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.*;

@RunWith(MockitoJUnitRunner.class)
public class CarDAOimplTest {
    @Mock
    private DBManager dbManager;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private CarDAO carDAO;

    @Before
    public void setUp() throws Exception {
        carDAO = new CarDAOimp(dbManager);
        when(dbManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    }

    @Test
    public void testSaveCar() throws Exception {
        Car car = getTestCar();

        Assert.assertTrue(carDAO.saveCar(car));

        verify(preparedStatement, times(1)).setInt(eq(1), eq(car.getBrand().getId()));
        verify(preparedStatement, times(1)).setInt(eq(2), eq(car.getQualityClass().getId()));
        verify(preparedStatement, times(1)).setString(eq(3), eq(car.getModel()));
        verify(preparedStatement, times(1)).setString(eq(4), eq(car.getCar_number()));
        verify(preparedStatement, times(1)).setString(eq(5), eq("free"));
        verify(preparedStatement, times(1)).setString(eq(6), eq(car.getImgPath()));
        verify(preparedStatement, times(1)).executeUpdate();
        verify(preparedStatement, times(1)).getGeneratedKeys();
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt(1);
    }

    @Test
    public void testSaveCarWithException() throws Exception {
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Test Exception"));

        Car car = getTestCar();

        Assert.assertFalse(carDAO.saveCar(car));

        verify(preparedStatement, times(1)).setInt(eq(1), eq(car.getBrand().getId()));
        verify(preparedStatement, times(1)).setInt(eq(2), eq(car.getQualityClass().getId()));
        verify(preparedStatement, times(1)).setString(eq(3), eq(car.getModel()));
        verify(preparedStatement, times(1)).setString(eq(4), eq(car.getCar_number()));
        verify(preparedStatement, times(1)).setString(eq(5), eq("free"));
        verify(preparedStatement, times(1)).setString(eq(6), eq(car.getImgPath()));
        verify(preparedStatement, times(1)).executeUpdate();
        verify(resultSet, never()).next();
        verify(resultSet, never()).getInt(1);
    }

    @Test
    public void testDeleteCarById() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);

        Assert.assertTrue(carDAO.deleteCarById(10000));
    }

    @Test
    public void testCaseThrowDeleteCarById() throws Exception{
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Test Exception"));

        Assert.assertFalse(carDAO.deleteCarById(10000));
    }

    @Test
    public void testUpdateCarPrice() {

        Assert.assertTrue(carDAO.updateCarPrice(getTestCar(), 10000));
    }

    @Test
    public void testCaseThrowUpdateCarPrice() throws Exception{
        when(preparedStatement.executeUpdate()).thenThrow(new SQLException("Test Exception"));

        Assert.assertFalse(carDAO.updateCarPrice(getTestCar(), 10000));
    }

    @Test
    public void testFindCars() throws SQLException {
        List<String> brandsId = Arrays.asList("1", "2");
        List<String> qClassId = Arrays.asList("1", "2");
        Map<String, Boolean> sortMap = new HashMap<>();
        sortMap.put("brand_name", true);
        int start = 0;
        int offset = 10;

        List<Car> expectedCars = new LinkedList<>();
        expectedCars.add(new Car());
        expectedCars.add(new Car());

        // Mock the database connection
        when(dbManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(preparedStatement.getResultSet()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        when(resultSet.getInt("car_id")).thenReturn(1, 2);
        when(resultSet.getInt("brand_id")).thenReturn(1, 2);
        when(resultSet.getString("brand_name")).thenReturn("Brand A", "Brand B");
        when(resultSet.getInt("q_class_id")).thenReturn(1, 2);
        when(resultSet.getString("q_class_name")).thenReturn("Class A", "Class B");
        when(resultSet.getString("model")).thenReturn("Model 1", "Model 2");
        when(resultSet.getString("car_number")).thenReturn("ABC123", "DEF456");
        when(resultSet.getString("img")).thenReturn("path/to/img1", "path/to/img2");
        when(resultSet.getInt("price")).thenReturn(100, 200);
        when(resultSet.getString("curr_state")).thenReturn("free", "rented");

        // Call the method being tested
        List<Car> actualCars = carDAO.findCars(brandsId, qClassId, sortMap, start, offset);

        // Verify the expected SQL statement was executed
        verify(connection).prepareStatement(anyString());

        // Verify that the expected parameters were set
        verify(preparedStatement).setInt(1, 2); // number of brandsId
        verify(preparedStatement).setInt(2, 2); // number of brandsId
    }

    private Car getTestCar() {
        return Car.builder()
                .brand(new Brand("Toyota", 1))
                .qualityClass(new QualityClass( "Economy", 1))
                .model("Corolla")
                .car_number("AB1234CD")
                .price(999)
                .build();
    }
}
