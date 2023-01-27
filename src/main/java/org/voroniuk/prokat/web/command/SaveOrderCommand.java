package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.Command;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Save order command. Save customer's order to db with status NEW, return pay order command
 *
 * @author D. Voroniuk
 */

public class SaveOrderCommand implements Command{

    private static final Logger LOG = Logger.getLogger(SaveOrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg="";
        String forward = Path.PAGE__ORDER;

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String withDriver = req.getParameter("withDriver");
        String strTerm = req.getParameter("term");
        int term = 0;
        try{
            term = Integer.parseInt(strTerm);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.term");
            req.setAttribute("msg", msg);
            return forward;
        }

        String email = req.getParameter("email");
        String passportData = req.getParameter("passportData");
        if (passportData == null || passportData == "") {
            msg = rb.getString("error.message.passportData");
            req.setAttribute("msg", msg);
            return forward;
        }

        String strId = req.getParameter("car_id");
        int car_id = 0;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        CarDAOimp carDAO = new CarDAOimp();
        Car car = carDAO.findCarById(car_id);
        if (car == null) {
            return forward;
        }

        OrderDAOimp orderDAO = new OrderDAOimp();

        Order newDoc = new Order();
        newDoc.setCar(car);
        newDoc.setState(Order.State.NEW);
        newDoc.setDate(new Date(System.currentTimeMillis()));
        newDoc.setAmount(car.getPrice());
        newDoc.setTerm(term);
        newDoc.setPassportData(passportData);

        newDoc.setUser((User) req.getSession().getAttribute("user"));
        if (withDriver!=null && withDriver.equals("on")) {
            newDoc.setWithDriver(true);
        }

        if (!orderDAO.saveOrder(newDoc)) {
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        //PRG pattern
        String redirect = Path.COMMAND__PAY_INVOICE + "&order_id="+newDoc.getId();
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
