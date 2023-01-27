package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.Command;

import java.util.List;

/**
 * Users command. Return users page with pagination
 *
 * @author D. Voroniuk
 */

public class UsersCommand implements Command{

    private static final Logger LOG = Logger.getLogger(UsersCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");
        String forward = Path.PAGE__ERROR_PAGE;

        UserDAO userDAO = new UserDAOimp();

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) userDAO.countUsers() / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        List<User> users = userDAO.findUsers((pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("users", users);

        forward = Path.PAGE__USERS;

        LOG.debug("Command finished");

        return forward;
    }
}
