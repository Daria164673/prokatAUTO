package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.RejectOrderCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RejectOrderCommandTest {
    private OrderDAO orderDAO;
    private RejectOrderCommand rejectOrderCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        orderDAO = mock(OrderDAOimp.class);
        rejectOrderCommand = new RejectOrderCommand(orderDAO);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("orders");

    }

    @Test
    public void shouldReturnPayInvoicePage() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("car_id")).thenReturn("1");
        when(request.getParameter("reject_reason")).thenReturn("test");

        when(orderDAO.updateRejectOrder(anyInt(), anyInt(), anyString())).thenReturn(true);

        String forward = rejectOrderCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
    }

    @Test
    public void caseIncorrectOrderId() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("order_id")).thenReturn("t");
        when(request.getParameter("car_id")).thenReturn("1");
        when(request.getParameter("reject_reason")).thenReturn("test");

        when(orderDAO.updateRejectOrder(anyInt(), anyInt(), anyString())).thenReturn(true);

        String forward = rejectOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.order_id"));
    }

    @Test
    public void caseIncorrectCarId() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("car_id")).thenReturn("t");
        when(request.getParameter("reject_reason")).thenReturn("test");

        when(orderDAO.updateRejectOrder(anyInt(), anyInt(), anyString())).thenReturn(true);

        String forward = rejectOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_id"));
    }

    @Test
    public void caseSqlExcept() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("order_id")).thenReturn("1");
        when(request.getParameter("car_id")).thenReturn("1");
        when(request.getParameter("reject_reason")).thenReturn("test");

        when(orderDAO.updateRejectOrder(anyInt(), anyInt(), anyString())).thenReturn(false);

        String forward = rejectOrderCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.sqlexecept"));
    }
}
