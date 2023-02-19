
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.Assert;
import org.junit.Test;

import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.RequestHelper;
import org.voroniuk.prokat.web.command.CarCommand;
import org.voroniuk.prokat.web.command.MainCommand;


import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

import static org.mockito.Mockito.*;

public class CommonTests {

    @Test
    public void requestHelperTest() {

        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getParameter("command")).thenReturn("main").thenReturn(null).thenReturn("car");

        RequestHelper helper = new RequestHelper(request);

        Assert.assertEquals(MainCommand.class, helper.getCommand().getClass());
        Assert.assertEquals(MainCommand.class, helper.getCommand().getClass());
        Assert.assertEquals(CarCommand.class, helper.getCommand().getClass());
    }

    @Test
    public void getOrderByQueryTextTest() {

        Map<String, Boolean> sortMap= new LinkedHashMap<>();
        sortMap.put("brand_name", true);
        sortMap.put("model", true);
        sortMap.put("car_number", true);

        Assert.assertEquals(" ORDER BY brand_name , model , car_number ", Utils.getOrderByQueryText(sortMap));

    }

    @Test
    public void getPageNoFromRequestTest() {

        String param_name = "test";
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameter(param_name)).thenReturn("10").thenReturn("11").thenReturn("notnumber");

        Assert.assertEquals(10, Utils.getPageNoFromRequest(req, param_name, 10));
        Assert.assertEquals(10, Utils.getPageNoFromRequest(req, param_name, 10));
        Assert.assertEquals(1, Utils.getPageNoFromRequest(req, param_name, 10));
    }

    @Test
    public void getCheckLocaleTest() {

        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(req.getSession()).thenReturn(session);
        when(session.getAttribute("locale")).thenReturn(Locale.ENGLISH).thenReturn(null);
        doNothing().when(session).setAttribute(any(String.class), any(Object.class));

        Assert.assertEquals(Locale.ENGLISH, Utils.getCheckLocale(req));
        Assert.assertEquals(Locale.getDefault(), Utils.getCheckLocale(req));

        Cookie cookie = mock(Cookie.class);
        Cookie[] cookies = {cookie};
        when(cookie.getName()).thenReturn("locale");
        when(cookie.getValue()).thenReturn("UA");
        when(req.getCookies()).thenReturn(cookies);

        Assert.assertEquals(new Locale("uk", "UA"), Utils.getCheckLocale(req));

    }

}
