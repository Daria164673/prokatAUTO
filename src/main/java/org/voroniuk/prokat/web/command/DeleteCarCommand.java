package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.QClassDAO;
import org.voroniuk.prokat.dao.impl.BrandDAOimp;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.QClassDAOimp;
import org.voroniuk.prokat.entity.Brand;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.QualityClass;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Delete car command. Delete car from db, return cars command
 *
 * @author D. Voroniuk
 */

public class DeleteCarCommand implements Command{

    private static final Logger LOG = Logger.getLogger(DeleteCarCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg="";
        String forward = Path.PAGE__CARS;

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String strId = req.getParameter("car_id");
        int car_id = 0;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        CarDAO carDAO = new CarDAOimp();

        if (!carDAO.deleteCarById(car_id)) {
            msg = rb.getString("error.message");
            req.setAttribute("msg", msg);
            return forward;
        }

        String redirect = Path.COMMAND__CARS;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
