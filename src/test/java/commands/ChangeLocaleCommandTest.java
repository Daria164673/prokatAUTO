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
import org.voroniuk.prokat.web.command.ChangeLocaleCommand;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

public class ChangeLocaleCommandTest {

    private static final ChangeLocaleCommand changeLocaleCommand = new ChangeLocaleCommand();

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
        when(request.getParameter("choosenLang")).thenReturn("EN");
        when(request.getParameter("from")).thenReturn("main");
    }

    @Test
    public void shouldChangeLocale(){
        changeLocaleCommand.execute(request, response);
        verify(session).setAttribute("locale", en);
    }

    @Test
    public void shouldReturnBack() {
        String forward = changeLocaleCommand.execute(request, response);
        Assert.assertEquals(Path.COMMAND__MAIN, forward.replace("/",""));
    }

}