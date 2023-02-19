package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Login command. Checks login password
 * create new session and return
 * account command if login password is correct
 *
 * @author D. Voroniuk
 */

public class LoginCommand implements Command {

    private final UserDAO userDAO;
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    public LoginCommand(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        Locale locale = Utils.getCheckLocale(req);

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String login = req.getParameter("login");
        LOG.debug("request param: login: " + login);

        String password = req.getParameter("password");
        String passHash = DigestUtils.md5Hex(password);

        String forward = Path.COMMAND__ACCOUNT;
        String errorMessage;

        if (login == null || password == null || login.isEmpty() || password.isEmpty()) {
            errorMessage = rb.getString("error.message.empty_login");
            req.setAttribute("msg", errorMessage);
            LOG.warn("errorMessage --> " + errorMessage);
            forward = Path.COMMAND__MAIN;
            return forward;
        }

        User user = userDAO.findUserByLogin(login);
        LOG.trace("Found user: " + user);

        if (user != null && user.getPassword().equals(passHash)) {

            if (user.getIsBlocked()) {
                errorMessage = rb.getString("error.message.user_is_blocked");
                req.setAttribute("msg", errorMessage);
                forward = Path.COMMAND__MAIN;
            } else {
                User.Role userRole = user.getRole();
                LOG.trace("User role: " + userRole);

                //invalidate session if existing
                HttpSession session = req.getSession(false);
                if (session!=null) {
                    session.invalidate();
                }

                //new session
                session = req.getSession(true);
                session.setAttribute("user", user);

                Utils.getCheckLocale(req);

                forward = Path.COMMAND__ACCOUNT;

                LOG.trace("Set session attribute: user = " + user);
                LOG.info("User " + user + " logged in as " + user.getRole());
            }
        } else {
            errorMessage = rb.getString("error.message.login_pass_incorrect");
            req.setAttribute("msg", errorMessage);
            forward = Path.COMMAND__MAIN;
        }

        LOG.debug("Command finished, forward to: " + forward);
        return forward;
    }
}
