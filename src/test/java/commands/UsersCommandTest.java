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
import org.voroniuk.prokat.web.command.UsersCommand;

import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class UsersCommandTest {
    private UserDAO userDAO;
    private UsersCommand usersCommand;

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
        usersCommand = new UsersCommand(userDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("users");

    }

    @Test
    public void shouldReturnUsersPage() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(userDAO.countUsers()).thenReturn(0);
        when(userDAO.findUsers(anyInt(), anyInt())).thenReturn(new ArrayList<>());

        String forward = usersCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__USERS, forward);
    }
}
