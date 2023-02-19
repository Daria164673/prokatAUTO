package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.Cookie;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.SiteLocale;
import org.voroniuk.prokat.utils.Utils;
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

    private final CarDAO carDAO;
    private static final Logger LOG = Logger.getLogger(MainCommand.class);

    public MainCommand(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Main command starts");

        Locale locale = Utils.getCheckLocale(req);

        String forward = Path.PAGE__MAIN;

        //carousel with cars
        List<Car> cars = carDAO.findCars(0, 0, new HashMap<>(), 1, 10);
        req.getSession().setAttribute("cars", cars);

        LOG.debug("Main command ends forward to " + forward);
        return forward;
    }

}
