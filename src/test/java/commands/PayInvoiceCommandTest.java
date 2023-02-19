package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.PayInvoiceCommand;

import java.util.Locale;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class PayInvoiceCommandTest {
    private OrderDAO orderDAO;
    private PayInvoiceCommand payInvoiceCommand;

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
        payInvoiceCommand = new PayInvoiceCommand(orderDAO);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("pay_invoice");

    }

    @Test
    public void shouldReturnPayInvoicePage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(orderDAO.findOrderById(anyInt())).thenReturn(new Order());
        when(request.getParameter("order_id")).thenReturn("1");

        String forward = payInvoiceCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__PAY_INVOICE, forward);

    }

    @Test
    public void caseOrderNotExists() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("order_id")).thenReturn("1");

        when(orderDAO.findOrderById(anyInt())).thenReturn(null);

        String forward = payInvoiceCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
        verify(request).setAttribute(eq("msg"), Mockito.any());
    }
}
