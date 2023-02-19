package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.dao.impl.RepairInvoiceDAOimpl;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.entity.RepairInvoice;
import org.voroniuk.prokat.entity.User;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.Command;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Save repair command. Save repair invoice to db, return account command
 *
 * @author D. Voroniuk
 */

public class SaveRepairInvoiceCommand implements Command{

    private final RepairInvoiceDAO repairInvoiceDAO;
    private static final Logger LOG = Logger.getLogger(SaveRepairInvoiceCommand.class);

    public SaveRepairInvoiceCommand(RepairInvoiceDAO repairInvoiceDAO) {
        this.repairInvoiceDAO = repairInvoiceDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.PAGE__REPAIR_INVOICE;

        Locale locale = Utils.getCheckLocale(req);

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String repairInfo = req.getParameter("repairInfo");
        String strAmount = req.getParameter("amount");
        double amount = 0;
        if (!strAmount.isEmpty()) {
            try{
                amount = Double.parseDouble(strAmount);
            }catch (NumberFormatException e){
                msg = rb.getString("error.message.amount");
                req.setAttribute("msg", msg);
                return forward;
            }
        }

        String contractor = req.getParameter("contractor");


        String strId = req.getParameter("car_id");
        int car_id;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.car_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        Car car = repairInvoiceDAO.getCarDAO().findCarById(car_id);
        if (car == null) {
            return forward;
        }

        RepairInvoice newDoc = RepairInvoice.builder()
                .car(car)
                .date(new Date(System.currentTimeMillis()))
                .repairInfo(repairInfo)
                .amount(amount)
                .contractor(contractor)
                .build();

        if (!repairInvoiceDAO.saveRepairInvoice(newDoc)) {
            msg = rb.getString("error.message.sqlexecept");
            req.setAttribute("msg", msg);
            return forward;
        }

        //PRG pattern
        String redirect = Path.COMMAND__REPAIR_INVOICES;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
