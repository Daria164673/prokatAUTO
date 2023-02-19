package commands;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.CarsCommand;

import java.util.ArrayList;
import java.util.Locale;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class CarsCommandTest {

    private CarDAO carDAO;
    private CarsCommand carsCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);

        carDAO = mock(CarDAOimp.class);
        carsCommand = new CarsCommand(carDAO);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("cars");

    }

    @Test
    public void shouldReturnCarPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);

        when(carDAO.countCars(anyInt(), anyInt())).thenReturn(0);
        when(carDAO.findCars(anyInt(), anyInt(), anyMap(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        String forward = carsCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__CARS, forward);
        verify(request).setAttribute(eq("state_free"), Mockito.any());
    }

    @Test
    public void caseFilters() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameterValues("filter_brands")).thenReturn(new String[]{"1"});
        when(request.getParameterValues("filter_qClass")).thenReturn(new String[]{"1"});
        when(request.getParameter("sorting")).thenReturn("name");

        when(carDAO.countCars(anyInt(), anyInt())).thenReturn(0);
        when(carDAO.findCars(anyInt(), anyInt(), anyMap(), anyInt(), anyInt())).thenReturn(new ArrayList<>());

        String forward = carsCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__CARS, forward);
        verify(request).setAttribute(eq("state_free"), Mockito.any());
    }
}
