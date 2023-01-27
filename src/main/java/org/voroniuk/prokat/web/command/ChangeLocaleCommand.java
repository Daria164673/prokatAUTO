package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;

/**
 * Command changes locale of application. Forwards to current page
 *
 * @author D. Voroniuk
 *
 */

public class ChangeLocaleCommand implements Command {
    private static final Logger LOG = Logger.getLogger(ChangeLocaleCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String from = req.getParameter("from");
        if(from.equals("")){
            from = "main";
        }

        String forward = "/controller?command=" + from;

        Locale locale = SiteLocale.valueOf(req.getParameter("choosenLang")).getLocale();
        req.getSession().setAttribute("locale", locale);

        User user = (User) req.getSession().getAttribute("user");
        if (user!=null) {
            user.setLocale(locale);
        }

        LOG.debug("Locale changed to " + locale);

        return forward;
    }
}
