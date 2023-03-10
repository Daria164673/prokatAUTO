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
 * Reject order command. Set reject reason and status REJECTED for order,
 * state FREE for car, return account command
 *
 * @author D. Voroniuk
 */

public class RejectOrderCommand implements Command{

    private final OrderDAO orderDAO;
    private static final Logger LOG = Logger.getLogger(RejectOrderCommand.class);

    public RejectOrderCommand(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.COMMAND__ACCOUNT;

        Locale locale = Utils.getCheckLocale(req);

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String reject_reason = req.getParameter("reject_reason");

        String strId = req.getParameter("order_id");
        int order_id;
        try{
            order_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.order_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        strId = req.getParameter("car_id");
        int car_id;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.car_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        if (!orderDAO.updateRejectOrder(order_id, car_id, reject_reason)) {
            msg = rb.getString("error.message.sqlexecept");
            req.setAttribute("msg", msg);
            return forward;
        }

        //PRG pattern
        String redirect = Path.COMMAND__ACCOUNT;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
