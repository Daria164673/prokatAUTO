package commands;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.AccountCommand;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

public class AccountCommandTest {

    private static final AccountCommand accountCommand = new AccountCommand();

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("main");
    }

    @Test
    public void shouldReturnCustomerAccount() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        String forward = accountCommand.execute(request, response);
        Assert.assertEquals(Path.COMMAND__USER_ACCOUNT, forward);
    }

    @Test
    public void shouldReturnManagerAccount() {

        User user = new User();
        user.setRole(User.Role.MANAGER);

        when(session.getAttribute("user")).thenReturn(user);

        String forward = accountCommand.execute(request, response);
        Assert.assertEquals(Path.COMMAND__ORDERS, forward);
    }

    @Test
    public void shouldReturnAdminAccount() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        String forward = accountCommand.execute(request, response);
        Assert.assertEquals(Path.COMMAND__USERS, forward);
    }

    @Test
    public void shouldReturnErrorWhenNullUser() {

        User user = null;

        when(session.getAttribute("user")).thenReturn(user);

        String forward = accountCommand.execute(request, response);
        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
        Assert.assertNotNull(forward);
    }



}