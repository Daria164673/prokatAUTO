package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.*;
import org.voroniuk.prokat.web.command.SaveOrderCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;

public class SaveOrderCommandTest {
    private OrderDAO orderDAO;
    private CarDAO carDAO;
    private SaveOrderCommand saveOrderCommand;

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
        orderDAO = mock(OrderDAOimp.class);
        when(orderDAO.getCarDAO()).thenReturn(carDAO);
        saveOrderCommand = new SaveOrderCommand(orderDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);

        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("order");

    }

    @Test
    public void shouldReturnCarsPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("withDriver")).thenReturn("on");
        when(request.getParameter("term")).thenReturn("10");
        when(request.getParameter("passportData")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(orderDAO.saveOrder(any())).thenReturn(true);

        String forward = saveOrderCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__PAY_INVOICE, forward.substring(0, Path.COMMAND__PAY_INVOICE.length()));
    }

    @Test
    public void incorrectTerm() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("withDriver")).thenReturn("on");
        when(request.getParameter("term")).thenReturn("10dddd");
        when(request.getParameter("passportData")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(orderDAO.saveOrder(any())).thenReturn(true);

        String forward = saveOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__ORDER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.term"));
    }

    @Test
    public void incorrectPassportData() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("withDriver")).thenReturn("on");
        when(request.getParameter("term")).thenReturn("1");
        when(request.getParameter("passportData")).thenReturn(null);
        when(request.getParameter("car_id")).thenReturn("1");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(orderDAO.saveOrder(any())).thenReturn(true);

        String forward = saveOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__ORDER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.passportData"));
    }

    @Test
    public void incorrectCarId() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("withDriver")).thenReturn("on");
        when(request.getParameter("term")).thenReturn("1");
        when(request.getParameter("passportData")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1gggg");

        when(carDAO.findCarById(anyInt())).thenReturn(new Car());
        when(orderDAO.saveOrder(any())).thenReturn(true);

        String forward = saveOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__ORDER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_id"));
    }
}
