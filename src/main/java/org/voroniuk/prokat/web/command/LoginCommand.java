package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.User;
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
    private static final Logger LOG = Logger.getLogger(LoginCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        UserDAOimp userDAO = new UserDAOimp();

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if (locale == null) {
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String login = req.getParameter("login");
        LOG.debug("request param: login: " + login);

        String password = req.getParameter("password");
        String passHash = DigestUtils.md5Hex(password);

        String forward = Path.PAGE__ERROR_PAGE;
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
                errorMessage = rb.getString("login.message.user_is_blocked");
                req.setAttribute("msg", errorMessage);
                forward = Path.COMMAND__MAIN;
            } else {
                User.Role userRole = user.getRole();
                LOG.trace("User role: " + userRole);

                HttpSession session = req.getSession(false);
                if (session!=null) {
                    session.invalidate();
                }
                session = req.getSession(true);

                forward = Path.COMMAND__ACCOUNT;

                session.setAttribute("user", user);
                session.setAttribute("locale", user.getLocale());

                LOG.trace("Set session attribute: user = " + user);
                LOG.info("User " + user + " logged in as " + user.getRole());
            }
        } else {
            errorMessage = rb.getString("login.message.login_pass_incorrect");
            req.setAttribute("msg", errorMessage);
            forward = Path.COMMAND__MAIN;
        }

        LOG.debug("Command finished, forward to: " + forward);
        return forward;
    }
}
