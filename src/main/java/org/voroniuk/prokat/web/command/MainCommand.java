package org.voroniuk.prokat.web.command;

import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.web.Command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * Main command.
 * Can be accessed by unregistered user.
 * User can login or link to register page
 *
 * @author D. Voroniuk
 */

public class MainCommand implements Command {

    private static final Logger LOG = Logger.getLogger(MainCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Main command starts");

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if (locale == null) {
            locale = Locale.getDefault();
            req.getSession().setAttribute("locale", locale);
        }

        String forward = Path.PAGE__MAIN;

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }

}
