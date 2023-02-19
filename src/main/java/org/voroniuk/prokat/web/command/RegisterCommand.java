package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.utils.ValidationService;
import org.voroniuk.prokat.web.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Register command. Provides password encryption.
 * New user can register with role CUSTOMER,
 * ADMIN user can create new manager (role MANAGER)
 *
 * @author D. Voroniuk
 */

public class RegisterCommand implements Command {

    private final UserDAO userDAO;

    private static final Logger LOG = Logger.getLogger(RegisterCommand.class);

    public RegisterCommand(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String msg;
        String forward = Path.PAGE__REGISTER;

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        String strRole = req.getParameter("role");
        User.Role role = User.Role.CUSTOMER;
        if (strRole!=null && !strRole.equals("")) {
            req.setAttribute("role", strRole);
            try {
                role = User.Role.valueOf(strRole.toUpperCase());
            } catch (Exception e) {
                LOG.warn("unknown role :" + strRole);
            }
        }

        if (login == null && password == null) {
            return forward;
        }

        Locale locale = Utils.getCheckLocale(req);
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        if (login==null || login.equals("")){
            msg = rb.getString("error.message.empty_login");
            req.setAttribute("msg", msg);
            return forward;
        }

        if (password==null || password.equals("")){
            msg = rb.getString("error.message.empty_password");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        if (!password.equals(confirm)){
            msg = rb.getString("error.message.confirm_incorrect");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        String email = req.getParameter("email");
        if (!ValidationService.emailValidator(email)){
            msg = rb.getString("error.message.invalidate_email");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        String firstName = req.getParameter("firstname");
        if (firstName==null || firstName.equals("")){
            msg = rb.getString("error.message.empty_firstName");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        String lastName = req.getParameter("lastname");
        if (lastName==null || lastName.equals("")){
            msg = rb.getString("error.message.empty_lastName");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        if(userDAO.findUserByLogin(login)!=null){
            msg = rb.getString("error.message.user_with_login") + login + rb.getString("error.message.already_exists");
            req.setAttribute("msg", msg);
            return forward;
        }

        User newUser = User.builder()
                .login(login)
                .password(password)
                .role(role)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();

        if (!userDAO.saveUser(newUser)) {
            msg = rb.getString("error.message.sqlexecept");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        String redirect = Path.COMMAND__ACCOUNT;

        if (role == User.Role.CUSTOMER) {

            //invalidate session if existing
            HttpSession session = req.getSession(false);
            if (session!=null) {
                session.invalidate();
            }

            //new session
            session = req.getSession(true);
            session.setAttribute("user", newUser);

            Utils.getCheckLocale(req);
        } else {
            redirect = Path.COMMAND__USERS;
        }

        //PRG pattern
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
