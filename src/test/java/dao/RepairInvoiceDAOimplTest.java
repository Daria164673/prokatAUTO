package dao;

import org.junit.Assert;
import org.junit.Before;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.RepairInvoiceDAOimpl;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.RepairInvoice;

import java.sql.*;
import java.util.Date;

public class RepairInvoiceDAOimplTest {
    private DBManager dbManager;
    private Connection connection;

    private PreparedStatement preparedStatement;

    private ResultSet resultSet;

    private CarDAO carDAO;
    private UserDAO userDAO;

    private RepairInvoiceDAO repairInvoiceDAO;

    @Before
    public void setUp() throws SQLException {
        dbManager = mock(DBManager.class);
        carDAO = mock(CarDAOimp.class);
        userDAO = mock(UserDAOimp.class);
        repairInvoiceDAO = new RepairInvoiceDAOimpl(dbManager, carDAO);
        preparedStatement = mock(PreparedStatement.class);
        resultSet = mock(ResultSet.class);
        connection = mock(Connection.class);

        when(dbManager.getConnection()).thenReturn(connection);
        when(connection.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(preparedStatement);
        when(preparedStatement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);
    }

    @Test
    public void testSaveRepairInvoice() throws Exception {
        RepairInvoice repairInvoice = new RepairInvoice();
        Car car = new Car();
        car.setId(1);
        repairInvoice.setCar(car);
        repairInvoice.setRepairInfo("Replace engine oil");
        repairInvoice.setAmount(100.0);
        repairInvoice.setContractor("ABC Corp");
        repairInvoice.setDate(new Date());

        when(carDAO.getCarStateById(connection, 1)).thenReturn(Car.State.FREE);
        when(carDAO.updateCarState(connection, 1, Car.State.ON_REPAIR)).thenReturn(true);

        Assert.assertTrue(repairInvoiceDAO.saveRepairInvoice(repairInvoice));

        verify(connection, times(1)).prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS));
        verify(preparedStatement, times(1)).setDate(1, new java.sql.Date(repairInvoice.getDate().getTime()));
        verify(preparedStatement, times(1)).setInt(2, repairInvoice.getCar().getId());
        verify(preparedStatement, times(1)).setString(3, repairInvoice.getRepairInfo());
        verify(preparedStatement, times(1)).setInt(4, (int) (repairInvoice.getAmount() * 100));
        verify(preparedStatement, times(1)).setString(5, repairInvoice.getContractor());
        verify(preparedStatement, times(1)).executeUpdate();
        verify(resultSet, times(1)).next();
        verify(resultSet, times(1)).getInt(1);

        verify(carDAO, times(1)).getCarStateById(connection, 1);
        verify(carDAO, times(1)).updateCarState(connection, 1, Car.State.ON_REPAIR);

        verify(connection, times(1)).commit();
    }

    @Test
    public void testSaveRepairInvoiceWithNonFreeCar() throws Exception {
            RepairInvoice repairInvoice = new RepairInvoice();
            Car car = new Car();
            car.setId(1);
            repairInvoice.setCar(car);
            repairInvoice.setRepairInfo("Replace engine oil");
            repairInvoice.setAmount(100.0);
            repairInvoice.setContractor("ABC Corp");
            repairInvoice.setDate(new Date());

            when(carDAO.getCarStateById(connection, repairInvoice.getCar().getId())).thenReturn(Car.State.ON_REPAIR);

            Assert.assertFalse(repairInvoiceDAO.saveRepairInvoice(repairInvoice));

            verify(connection).setAutoCommit(false);
            verify(connection).setAutoCommit(true);

            verify(carDAO).getCarStateById(connection, repairInvoice.getCar().getId());

            verify(preparedStatement, never()).executeUpdate();
            verify(carDAO, never()).updateCarState(any(), anyInt(), any());
    }
}
