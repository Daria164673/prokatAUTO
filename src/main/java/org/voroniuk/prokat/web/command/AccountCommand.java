package org.voroniuk.prokat.web.command;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.Command;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Command return account page according to user role
 *
 * @author D. Voroniuk
 */

public class AccountCommand implements Command {

    private static final Logger LOG = Logger.getLogger(AccountCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        String forward = Path.PAGE__ERROR_PAGE;
        User user = (User) req.getSession().getAttribute("user");

        if (user == null) {
            LOG.debug("User is empty");
            return forward;
        }

        LOG.debug("user role is " + user.getRole());
        if (user.getRole() == User.Role.CUSTOMER) {

            forward = Path.COMMAND__USER_ACCOUNT;
        } else if (user.getRole() == User.Role.MANAGER) {

            //forward = Path.COMMAND__MANAGER_ACCOUNT;
            forward = Path.COMMAND__ORDERS;
        } else if (user.getRole() == User.Role.ADMIN) {
            //forward = Path.COMMAND__ADMIN_ACCOUNT;
            forward = Path.COMMAND__USERS;
        }

        LOG.debug("Command finished");
        LOG.debug("Forward to " + forward);
        return forward;
    }
}
