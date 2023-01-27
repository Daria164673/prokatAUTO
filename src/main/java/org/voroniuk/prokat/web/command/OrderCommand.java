package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.web.Command;

/**
 * Order command. Return order page, which
 * allows the user to fill in the fields for new order
 * and save it
 *
 * @author D. Voroniuk
 */

public class OrderCommand implements Command{

    private static final Logger LOG = Logger.getLogger(OrderCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {
        LOG.debug("Command starts");

        String strId = req.getParameter("car_id");
        int id;

        try{
            id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            return Path.COMMAND__ACCOUNT;
        }

        CarDAO carDAO = new CarDAOimp();
        Car car = carDAO.findCarById(id);

        req.setAttribute("car", car);

        String redirect = Path.PAGE__ORDER;

        return redirect;
    }
}
