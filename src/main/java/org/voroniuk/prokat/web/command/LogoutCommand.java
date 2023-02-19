package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Logout command. invalidate current session
 *
 * @author D. Voroniuk
 */

public class LogoutCommand implements Command {
    private static final Logger LOG = Logger.getLogger(LogoutCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String forward = Path.COMMAND__MAIN;

        //invalidate session if existing
        HttpSession session = req.getSession(false);
        if (session!=null) {
            session.invalidate();
        }

        LOG.debug("Logged out");

        return forward;
    }
}
