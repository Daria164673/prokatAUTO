package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.RepairInvoiceDAO;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.dao.impl.RepairInvoiceDAOimpl;
import org.voroniuk.prokat.entity.Car;
import org.voroniuk.prokat.entity.Order;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.Command;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Return from repair command. Set return date for repair invoice
 * and state FREE for car, return repair invoices command
 *
 * @author D. Voroniuk
 */

public class ReturnFromRepairCarCommand implements Command{

    private final RepairInvoiceDAO repairInvoiceDAO;

    private static final Logger LOG = Logger.getLogger(ReturnFromRepairCarCommand.class);

    public ReturnFromRepairCarCommand(RepairInvoiceDAO repairInvoiceDAO) {
        this.repairInvoiceDAO = repairInvoiceDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        String msg;
        String forward = Path.COMMAND__REPAIR_INVOICES;

        Locale locale = Utils.getCheckLocale(req);

        ResourceBundle rb = ResourceBundle.getBundle("resources", locale);

        String strId = req.getParameter("repair_id");
        int repair_id;
        try{
            repair_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.repair_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        strId = req.getParameter("car_id");
        int car_id;
        try{
            car_id = Integer.parseInt(strId);
        }catch (NumberFormatException e){
            msg = rb.getString("error.message.car_id");
            req.setAttribute("msg", msg);
            return forward;
        }

        if (!repairInvoiceDAO.updateReturnFromRepair(repair_id, car_id)) {
            msg = rb.getString("error.message.sqlexecept");
            req.setAttribute("msg", msg);
            return forward;
        }

        LOG.debug("Command finished");

        //PRG pattern
        String redirect = Path.COMMAND__REPAIR_INVOICES;
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        resp.setHeader("Location", redirect);
        LOG.debug("Redirect to :" + redirect);

        return redirect;
    }
}
