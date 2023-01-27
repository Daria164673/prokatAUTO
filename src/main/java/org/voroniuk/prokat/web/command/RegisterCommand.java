package org.voroniuk.prokat.web.command;

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
 * Register command. Provides password encryption.
 * New user can register with role CUSTOMER,
 * ADMIN user can create new manager (role MANAGER)
 *
 * @author D. Voroniuk
 */

public class RegisterCommand implements Command {

    private static final Logger LOG = Logger.getLogger(RegisterCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        String msg="";
        String forward = Path.PAGE__REGISTER;

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirm");

        String strRole = req.getParameter("role");
        User.Role role = User.Role.CUSTOMER;
        if (strRole!=null && !strRole.equals("")) {
            try {
                role = User.Role.valueOf(strRole.toUpperCase());
            } catch (Exception e) {
                LOG.warn("unknown role :" + strRole);
            }
        }

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        if (login == null && password == null) {
            return forward;
        }

        if (login.equals("")){
            msg = rb.getString("error.message.empty_login");
            req.setAttribute("msg", msg);
            return forward;
        }

        if (password.equals("")){
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

        UserDAOimp userDAO = new UserDAOimp();

        if(userDAO.findUserByLogin(login)!=null){
            msg = rb.getString("error.message.user_with_login") + login + rb.getString("error.message.already_exists");
            req.setAttribute("msg", msg);
            return forward;
        }

        User newUser = new User();

        newUser.setLogin(login);
        newUser.setPassword(DigestUtils.md5Hex(password));
        newUser.setRole(role);
        newUser.setLocale(locale);

        if (!userDAO.saveUser(newUser)) {
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            req.setAttribute("login", login);
            return forward;
        }

        String redirect = Path.COMMAND__ACCOUNT;

        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            req.getSession().setAttribute("user", newUser);
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
