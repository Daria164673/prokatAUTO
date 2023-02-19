package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.ChangeUsersBlockCommand;
import org.voroniuk.prokat.web.command.RejectOrderCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class ChangeUserBlockCommandRTest {
    private UserDAO userDAO;
    private ChangeUsersBlockCommand changeUsersBlockCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        userDAO = mock(UserDAOimp.class);
        changeUsersBlockCommand = new ChangeUsersBlockCommand(userDAO);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("orders");

    }

    @Test
    public void shouldReturnUsersPage() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("user_id")).thenReturn("1");
        when(request.getParameter("isBlocked_value")).thenReturn("true");

        when(userDAO.changeIsBlockedValueById(anyInt(), anyBoolean())).thenReturn(true);

        String forward = changeUsersBlockCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__USERS, forward);
        //verify(request).setAttribute(eq("state_free"), Mockito.any());
    }

    @Test
    public void caseIncorrectUserId() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("user_id")).thenReturn("t");
        when(request.getParameter("isBlocked_value")).thenReturn("true");

        when(userDAO.changeIsBlockedValueById(anyInt(), anyBoolean())).thenReturn(true);

        String forward = changeUsersBlockCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.user_id"));
    }

    @Test
    public void caseSQLexception() {

        when(session.getAttribute("user")).thenReturn(User.builder().role(User.Role.CUSTOMER).build());
        when(request.getParameter("user_id")).thenReturn("1");
        when(request.getParameter("isBlocked_value")).thenReturn("true");

        when(userDAO.changeIsBlockedValueById(anyInt(), anyBoolean())).thenReturn(false);

        String forward = changeUsersBlockCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.sqlexecept"));
    }
}
