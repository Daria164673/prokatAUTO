package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.LoginCommand;
import org.voroniuk.prokat.web.command.RegisterCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class LoginCommandTest {
    private UserDAO userDAO;
    private LoginCommand loginCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;

    private final String LOGIN = "test";
    private final String PASSWORD = "test";

    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        userDAO = mock(UserDAOimp.class);
        loginCommand = new LoginCommand(userDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("register");

    }

    @Test
    public void shouldReturnAccountPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn(LOGIN);
        when(request.getParameter("password")).thenReturn(PASSWORD);

        User userLogining = User.builder()
                .login(LOGIN)
                .password(DigestUtils.md5Hex(PASSWORD))
                .blocked(false)
                .build();

        when(userDAO.findUserByLogin(anyString())).thenReturn(userLogining);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = loginCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
    }

    @Test
    public void caseEmptyValues() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn(null);
        when(request.getParameter("password")).thenReturn(PASSWORD);

        User userLogining = User.builder()
                .login(LOGIN)
                .password(DigestUtils.md5Hex(PASSWORD))
                .blocked(false)
                .build();

        when(userDAO.findUserByLogin(anyString())).thenReturn(userLogining);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = loginCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.COMMAND__MAIN, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.empty_login"));
    }

    @Test
    public void caseIncorrectPassword() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn(LOGIN);
        when(request.getParameter("password")).thenReturn(PASSWORD);

        User userLogining = User.builder()
                .login(LOGIN)
                .password(PASSWORD)
                .blocked(false)
                .build();

        when(userDAO.findUserByLogin(anyString())).thenReturn(userLogining);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = loginCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.COMMAND__MAIN, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.login_pass_incorrect"));
    }
}
