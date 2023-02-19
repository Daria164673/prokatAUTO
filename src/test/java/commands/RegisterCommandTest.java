package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.RegisterCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

public class RegisterCommandTest {
    private UserDAO userDAO;
    private RegisterCommand registerCommand;

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
        registerCommand = new RegisterCommand(userDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("register");

    }

    @Test
    public void shouldReturnRegisterPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
    }

    @Test
    public void caseIncorrectLogin() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.ua");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.empty_login"));
    }

    @Test
    public void caseIncorrectEmail() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.invalidate_email"));
    }

    @Test
    public void caseIncorrectPassword() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.ua");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.empty_password"));
    }

    @Test
    public void caseIncorrectConfirm() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test1");
        when(request.getParameter("email")).thenReturn("test@test.ua");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.confirm_incorrect"));
    }

    @Test
    public void caseIncorrectFirstName() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.ua");
        when(request.getParameter("firstname")).thenReturn("");
        when(request.getParameter("lastname")).thenReturn("test");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.empty_firstName"));
    }

    @Test
    public void caseIncorrectLastName() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.ua");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__REGISTER, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.empty_lastName"));
    }

    @Test
    public void caseSettingRole() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("login")).thenReturn("testihbihb");
        when(request.getParameter("password")).thenReturn("test");
        when(request.getParameter("confirm")).thenReturn("test");
        when(request.getParameter("email")).thenReturn("test@test.com");
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");
        when(request.getParameter("role")).thenReturn("CUSTOMER");

        when(userDAO.findUserByLogin(anyString())).thenReturn(null);
        when(userDAO.saveUser(any())).thenReturn(true);

        String forward = registerCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__ACCOUNT, forward);
        verify(request).setAttribute("role", "CUSTOMER");
    }
}
