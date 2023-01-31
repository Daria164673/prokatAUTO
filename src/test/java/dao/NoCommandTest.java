package dao;

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
import org.voroniuk.prokat.web.command.NoCommand;

import java.io.IOException;
import java.util.Locale;

import static org.mockito.Mockito.*;

public class NoCommandTest {

    private static NoCommand noCommand = new NoCommand();

    private HttpServletRequest request;
    private HttpServletResponse response;

    private static Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        when(request.getParameter("choosenLang")).thenReturn("EN");
        when(request.getParameter("from")).thenReturn("main");
    }

    @Test
    public void shouldReturnErrorPage() {
        String forward = noCommand.execute(request, response);
        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
    }

}