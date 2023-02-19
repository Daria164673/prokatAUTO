package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.RepairInvoiceDAOimpl;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.SaveRepairInvoiceCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class SaveRepairInvoiceCommandTest {

    private RepairInvoiceDAO repairInvoiceDAO;
    private CarDAO carDAO;
    private SaveRepairInvoiceCommand saveRepairInvoiceCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        carDAO = mock(CarDAOimp.class);
        repairInvoiceDAO = mock(RepairInvoiceDAOimpl.class);
        when(repairInvoiceDAO.getCarDAO()).thenReturn(carDAO);
        saveRepairInvoiceCommand = new SaveRepairInvoiceCommand(repairInvoiceDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);

        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("repair_invoice");

    }

    @Test
    public void shouldReturnCarsPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("repairInfo")).thenReturn("test");
        when(request.getParameter("amount")).thenReturn("10");
        when(request.getParameter("contractor")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(repairInvoiceDAO.saveRepairInvoice(any())).thenReturn(true);

        String forward = saveRepairInvoiceCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__REPAIR_INVOICES, forward);
    }

    @Test
    public void incorrectAmount() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("repairInfo")).thenReturn("test");
        when(request.getParameter("amount")).thenReturn("10eeee");
        when(request.getParameter("contractor")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(repairInvoiceDAO.saveRepairInvoice(any())).thenReturn(true);

        String forward = saveRepairInvoiceCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REPAIR_INVOICE, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.amount"));
    }

    @Test
    public void incorrectCarId() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("repairInfo")).thenReturn("test");
        when(request.getParameter("amount")).thenReturn("100");
        when(request.getParameter("contractor")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1sff");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(repairInvoiceDAO.saveRepairInvoice(any())).thenReturn(true);

        String forward = saveRepairInvoiceCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REPAIR_INVOICE, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_id"));
    }
}
