package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.OrderDAO;
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

import java.util.List;

/**
 * Repair invoices command. Return repair invoices page with pagination
 *
 * @author D. Voroniuk
 */

public class RepairInvoicesCommand implements Command{

    private final RepairInvoiceDAO repairInvoiceDAO;
    private static final Logger LOG = Logger.getLogger(RepairInvoicesCommand.class);

    public RepairInvoicesCommand(RepairInvoiceDAO repairInvoiceDAO) {
        this.repairInvoiceDAO = repairInvoiceDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) repairInvoiceDAO.countRepairInvoices() / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        List<RepairInvoice> repairs = repairInvoiceDAO.findRepairInvoices((pageNo - 1) * pageSize, pageSize);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);

        req.setAttribute("repairs", repairs);

        String forward = Path.PAGE__REPAIR_INVOICES;

        LOG.debug("Command finished");

        return forward;
    }
}
