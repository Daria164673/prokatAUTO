package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Change user block command. Set isBlocked attribute from req parameter to db
 * Returns users command
 *
 * @author D. Voroniuk
 */

public class ChangeUsersBlockCommand implements Command {
    private static final Logger LOG = Logger.getLogger(ChangeUsersBlockCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg="";
        String forward = Path.PAGE__ERROR_PAGE;

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String strId = req.getParameter("user_id");
        int user_id = 0;
        try{
            user_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        String strIsBlock = req.getParameter("isBlocked_value");
        boolean isBlock = false;
        if (strIsBlock.equals("true")) {
            isBlock = true;
        }

        UserDAO userDAO = new UserDAOimp();
        if (!userDAO.changeIsBlockedValueById(user_id, isBlock)) {
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        //PRG pattern
        String redirect = Path.COMMAND__USERS;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
