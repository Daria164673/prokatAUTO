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

import java.util.List;

/**
 * Orders command. Return orders page with pagination
 *
 * @author D. Voroniuk
 */

public class OrdersCommand implements Command{

    private final OrderDAO orderDAO;
    private static final Logger LOG = Logger.getLogger(OrdersCommand.class);

    public OrdersCommand(OrderDAO orderDAO) {
        this.orderDAO = orderDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        User user = (User) req.getSession().getAttribute("user");

        int user_id = 0;
        if (user.getRole()== User.Role.CUSTOMER) {
            user_id = user.getId();
        }

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) orderDAO.countOrders(user_id, null) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);


        List<Order> orders = orderDAO.findOrders(user_id, null, (pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("orders", orders);
        req.setAttribute("state_paid", Order.State.PAID);
        req.setAttribute("state_rejected", Order.State.REJECTED);
        req.setAttribute("state_finished", Order.State.FINISHED);
        req.setAttribute("true", true);

        String forward = Path.PAGE__ORDERS;

        LOG.debug("Command finished");

        return forward;
    }
}
