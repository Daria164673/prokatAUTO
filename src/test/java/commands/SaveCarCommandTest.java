package commands;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.QClassDAO;
import org.voroniuk.prokat.dao.impl.BrandDAOimp;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.QClassDAOimp;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.QualityClass;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.command.SaveCarCommand;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class SaveCarCommandTest {
    private CarDAO carDAO;
    private BrandDAO brandDAO;
    private QClassDAO qClassDAO;

    private SaveCarCommand saveCarCommand;

    private HttpSession session;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ServletContext servletContext;


    private static final Locale en = SiteLocale.EN.getLocale();

    @Before
    public void init(){
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        servletContext = mock(ServletContext.class);

        carDAO = mock(CarDAOimp.class);
        brandDAO = mock(BrandDAOimp.class);
        qClassDAO = mock(QClassDAOimp.class);
        saveCarCommand = new SaveCarCommand(carDAO, brandDAO, qClassDAO);

        when(request.getSession(anyBoolean())).thenReturn(session);
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(en);
        when(request.getServletContext()).thenReturn(servletContext);
        when(servletContext.getAttribute("fileImgPath")).thenReturn(null);

        doNothing().when(session).invalidate();

        when(request.getParameter("from")).thenReturn("car");

    }

    @Test
    public void shouldReturnCarsPage() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("111");
        when(request.getParameter("car_id")).thenReturn("1");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        Assert.assertEquals(Path.COMMAND__CARS, forward);
    }

    @Test
    public void incorrectQClass() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("test");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("111");
        when(request.getParameter("car_id")).thenReturn("1");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.qclass_id"));
    }

    @Test
    public void incorrectCarID() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("111");
        when(request.getParameter("car_id")).thenReturn("test");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_id"));
    }

    @Test
    public void incorrectCarNumber() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn(null);
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("111");
        when(request.getParameter("car_id")).thenReturn("1");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_number"));
    }

    @Test
    public void incorrectBrandId() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("test");
        when(request.getParameter("price")).thenReturn("111");
        when(request.getParameter("car_id")).thenReturn("1");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.brand_id"));
    }

    @Test
    public void incorrectPrice() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("test");
        when(request.getParameter("car_id")).thenReturn("1");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.price"));
    }

    @Test
    public void incorrectCarId() {

        User user = new User();
        user.setRole(User.Role.ADMIN);

        when(session.getAttribute("user")).thenReturn(user);

        when(request.getParameter("model")).thenReturn("test");
        when(request.getParameter("car_number")).thenReturn("test");
        when(request.getParameter("qualityClass")).thenReturn("1");
        when(request.getParameter("brand")).thenReturn("1");
        when(request.getParameter("price")).thenReturn("1");
        when(request.getParameter("car_id")).thenReturn("145ffff");

        when(brandDAO.findBrandById(anyInt())).thenReturn(new Brand());
        when(qClassDAO.findQClassById(anyInt())).thenReturn(new QualityClass());

        when(carDAO.saveCar(any())).thenReturn(true);
        when(carDAO.insertCarPrice(any(), anyDouble())).thenReturn(true);
        when(carDAO.updateCar(any())).thenReturn(true);
        when(carDAO.setCarPrice(any(), anyDouble())).thenReturn(true);

        String forward = saveCarCommand.execute(request, response);

        ResourceBundle rb = ResourceBundle.getBundle("resources", en);

        Assert.assertEquals(Path.PAGE__CAR, forward);
        verify(request).setAttribute("msg", rb.getString("error.message.car_id"));
    }
}
