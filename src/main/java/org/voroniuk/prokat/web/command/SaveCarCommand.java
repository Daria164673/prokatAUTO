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
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.dao.impl.QClassDAOimp;
import org.voroniuk.prokat.entity.*;
import org.voroniuk.prokat.web.Command;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Save car command. Save car to db, return cars command
 *
 * @author D. Voroniuk
 */

public class SaveCarCommand implements Command{

    private static final Logger LOG = Logger.getLogger(SaveCarCommand.class);

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.PAGE__CAR;

        Locale locale = (Locale) req.getSession().getAttribute("locale");
        if(locale == null){
            locale = Locale.getDefault();
        }
        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String model = req.getParameter("model");
        String car_number = req.getParameter("car_number");
        String strQId = req.getParameter("qualityClass");
        String strBrandId = req.getParameter("brand");
        String strPrice = req.getParameter("price");
        String strId = req.getParameter("car_id");

        if (car_number==null || car_number.equals("")) {
            msg = rb.getString("error.message.car_number");
            req.setAttribute("msg", msg);
            return forward;
        }

        int brand_id;
        try{
            brand_id = Integer.parseInt(strBrandId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.brand_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        int qClass_id;
        try{
            qClass_id = Integer.parseInt(strQId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.qclass_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        double price;
        try{
            price = Double.parseDouble(strPrice);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.price");
            req.setAttribute("msg", msg);
            return forward;
        }

        int car_id = 0;
        if (strId!=null && !strId.equals("")) {
            try{
                car_id = Integer.parseInt(strId);
            }catch (NumberFormatException e){
                msg = rb.getString("error.message.car_id");
                req.setAttribute("msg", msg);
                return forward;
            }
        }

        CarDAO carDAO = new CarDAOimp();

        Car car = new Car();
        car.setId(car_id);

        BrandDAO brandDAO = new BrandDAOimp();
        Brand brand = brandDAO.findBrandById(brand_id);
        car.setBrand(brand);

        QClassDAO qClassDAO = new QClassDAOimp();
        QualityClass q_class = qClassDAO.findQClassById(qClass_id);
        car.setQualityClass(q_class);

        car.setModel(model);
        car.setCar_number(car_number);

        car.setPrice(price);

        if (car_id==0) {
            if (carDAO.saveCar(car)) {
                carDAO.insertCarPrice(car, car.getPrice());
            } else {
                msg = rb.getString("error.message.sqlexecept");
                req.setAttribute("msg", msg);
                return forward;
            }
        } else {
            if (carDAO.updateCar(car)) {
                carDAO.setCarPrice(car, car.getPrice());
            }else {
                msg = rb.getString("error.message.sqlexecept");
                req.setAttribute("msg", msg);
                return forward;
            }
        }

        //PRG pattern
        String redirect = Path.COMMAND__CARS;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
