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
 * Users account command. Return user account page included user's orders with pagination
 *
 * @author D. Voroniuk
 */

public class UserAccountCommand implements Command {
    private static final Logger LOG = Logger.getLogger(UserAccountCommand.class);
    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.debug("Command starts");
        String forward = Path.PAGE__ERROR_PAGE;

        OrderDAO orderDAO = new OrderDAOimp();

        User user = (User) req.getSession().getAttribute("user");

        int pageNo;
        int pageSize = 7;
        int totalPages = (int) Math.ceil((double) orderDAO.countOrders(user.getId(), null) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        List<Order> orders = orderDAO.findOrders(user.getId(), null, (pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("orders", orders);
        req.setAttribute("state_paid", Order.State.PAID);
        req.setAttribute("state_new", Order.State.NEW);
        req.setAttribute("state_rejected", Order.State.REJECTED);

        req.setAttribute("state_free", Car.State.FREE);

        forward = Path.PAGE__USER_ACCOUNT;

        LOG.debug("Command finished");

        return forward;
    }
}
