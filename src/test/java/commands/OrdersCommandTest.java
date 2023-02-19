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
import org.voroniuk.prokat.web.command.OrdersCommand;

import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class OrdersCommandTest {
    private OrderDAO orderDAO;
    private OrdersCommand ordersCommand;

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
        ordersCommand = new OrdersCommand(orderDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("orders");

    }

    @Test
    public void shouldReturnOrdersPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(orderDAO.countOrders(anyInt(), any())).thenReturn(0);
        when(orderDAO.findOrders(anyInt(), any(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        String forward = ordersCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__ORDERS, forward);
    }
}
