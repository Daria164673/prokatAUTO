package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Pay invoice command. Return page invoice page,
 * which provides ability to pay it or print
 *
 * @author D. Voroniuk
 */

public class PayInvoiceCommand implements Command{

    private static final Logger LOG = Logger.getLogger(PayInvoiceCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.PAGE__PAY_INVOICE;

        req.setAttribute("state_new", Order.State.NEW);

        OrderDAO orderDAO = new OrderDAOimp();

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String strId = req.getParameter("order_id");
        int order_id;
        try{
            order_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.order_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        Order order = orderDAO.findOrderById(order_id);
        if (order == null) {
            msg = rb.getString("error.message.cant_find_order_id") + order_id;
            req.setAttribute("msg", msg);
            return forward;
        }
        req.setAttribute("order", order);

        return forward;
    }
}
