package org.voroniuk.prokat.web.listener;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.dao.UserDAO;
import org.voroniuk.prokat.dao.impl.UserDAOimp;
import org.voroniuk.prokat.entity.User;

import java.util.Locale;


/**
 * Listen HttpSession events, count total active sessions
 *
 * @author D. Voroniuk
 */

public class SessionListener implements HttpSessionListener {
    private static final Logger LOG = Logger.getLogger(SessionListener.class);

    private static int totalSessions;

    public static int getTotalSessions() {
        return totalSessions;
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String id = se.getSession().getId();

        totalSessions++;

        LOG.debug("Session " + id + " created. (total " + totalSessions + ")");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id = se.getSession().getId();

        Locale locale = (Locale) se.getSession().getAttribute("locale");
        if (locale == null) {
            locale = Locale.getDefault();
        }

        User user = (User) se.getSession().getAttribute("user");
        if (user != null) {
            UserDAO userDAO = new UserDAOimp();
            userDAO.setUsersLocale(user, locale);
        }

        totalSessions--;
        LOG.debug("Session " + id + " destroyed. (total " + totalSessions + ")");
    }
}
