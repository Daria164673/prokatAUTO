package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.utils.Utils;
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

    private final OrderDAO orderDAO;
    private static final Logger LOG = Logger.getLogger(SaveOrderCommand.class);

    public SaveOrderCommand(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.PAGE__ORDER;

        Locale locale = Utils.getCheckLocale(req);

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String withDriver = req.getParameter("withDriver");
        String strTerm = req.getParameter("term");
        int term;
        try{
            term = Integer.parseInt(strTerm);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.term");
            req.setAttribute("msg", msg);
            return forward;
        }

        String passportData = req.getParameter("passportData");
        if (passportData == null || passportData.equals("")) {
            msg = rb.getString("error.message.passportData");
            req.setAttribute("msg", msg);
            return forward;
        }

        String strId = req.getParameter("car_id");
        int car_id;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.car_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        Car car = orderDAO.getCarDAO().findCarById(car_id);
        if (car == null) {
            return forward;
        }

        Order newDoc = Order.builder()
                .car(car)
                .state(Order.State.NEW)
                .date(new Date(System.currentTimeMillis()))
                .amount(car.getPrice())
                .term(term)
                .passportData(passportData)
                .user((User) req.getSession().getAttribute("user"))
                .build();

        if (withDriver!=null && withDriver.equals("on")) {
            newDoc.setWithDriver(true);
        }

        if (!orderDAO.saveOrder(newDoc)) {
            msg = rb.getString("error.message.sqlexecept");
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
