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
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.CarCommand;

import java.util.Locale;

import static org.mockito.Mockito.*;

public class CarCommandTest {

    private CarDAO carDAO;
    private CarCommand carCommand;

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
        carCommand = new CarCommand(carDAO);

        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getParameter("from")).thenReturn("car");

    }

    @Test
    public void shouldReturnCarPage() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("car_id")).thenReturn("1");
        when(carDAO.findCarById(1)).thenReturn(new Car());

        String forward = carCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__CAR, forward);
    }

    @Test
    public void CaseCarIdNotDefined() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("car_id")).thenReturn(null);

        String forward = carCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__ERROR_PAGE, forward);
        verify(request).setAttribute(eq("msg"), Mockito.any());

    }

    @Test
    public void CaseCarIdIsDefined() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("car_id")).thenReturn("1");
        when(carDAO.findCarById(1)).thenReturn(new Car());

        String forward = carCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute(eq("car"), Mockito.any());

    }

    @Test
    public void CaseCopyingCar() {

        User user = new User();
        user.setRole(User.Role.CUSTOMER);

        when(session.getAttribute("user")).thenReturn(user);
        when(request.getParameter("car_id")).thenReturn("1");
        when(request.getParameter("copy")).thenReturn("true");
        when(carDAO.findCarById(1)).thenReturn(new Car());

        String forward = carCommand.execute(request, response);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute(eq("car"), Mockito.any());

    }


}