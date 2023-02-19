package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.impl.RepairInvoiceDAOimpl;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.RepairInvoicesCommand;

import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class RepairInvoicesCommandTest {
    private RepairInvoiceDAO repairInvoiceDAO;
    private RepairInvoicesCommand repairInvoicesCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        repairInvoiceDAO = mock(RepairInvoiceDAOimpl.class);
        repairInvoicesCommand = new RepairInvoicesCommand(repairInvoiceDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("repair_invoices");

    }

    @Test
    public void shouldReturnRepairsPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(repairInvoiceDAO.countRepairInvoices()).thenReturn(0);
        when(repairInvoiceDAO.findRepairInvoices(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        String forward = repairInvoicesCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__REPAIR_INVOICES, forward);
    }
}
