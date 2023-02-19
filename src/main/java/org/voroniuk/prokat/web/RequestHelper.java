package org.voroniuk.prokat.web;

import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;
import org.voroniuk.prokat.dao.*;
import org.voroniuk.prokat.dao.impl.*;
import org.voroniuk.prokat.web.command.*;

/**
 * Holder for all commands.
 *
 * @author D. Voroniuk
 *
 */
public class RequestHelper {
    public RequestHelper(HttpServletRequest req) {
        this.req = req;
    }

    private final HttpServletRequest req;
    private static final Logger LOG = Logger.getLogger(RequestHelper.class);

    private static Map<String, Command> commands = new TreeMap<>();

    static {

        DBManager dbManager = DBManager.getInstance();

        UserDAO userDAO = new UserDAOimp(dbManager);
        BrandDAO brandDAO = new BrandDAOimp(dbManager);
        CarDAO carDAO = new CarDAOimp(dbManager);
        OrderDAO orderDAO = new OrderDAOimp(dbManager, carDAO, userDAO);
        QClassDAO qClassDAO = new QClassDAOimp(dbManager);
        RepairInvoiceDAO repairInvoiceDAO = new RepairInvoiceDAOimpl(dbManager, carDAO);

        commands.put("noCommand", new NoCommand());

        commands.put("main", new MainCommand(carDAO));
        commands.put("changeLocale", new ChangeLocaleCommand());

        commands.put("login", new LoginCommand(userDAO));
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterCommand(userDAO));
        commands.put("change_users_block", new ChangeUsersBlockCommand(userDAO));
        commands.put("users", new UsersCommand(userDAO));

        commands.put("account", new AccountCommand());
        commands.put("user_account", new UserAccountCommand(orderDAO));

        commands.put("orders", new OrdersCommand(orderDAO));
        commands.put("order", new OrderCommand(carDAO));
        commands.put("saveOrder", new SaveOrderCommand(orderDAO));

        commands.put("repair_invoices", new RepairInvoicesCommand(repairInvoiceDAO));
        commands.put("repair_invoice", new RepairInvoiceCommand(carDAO));
        commands.put("return_from_repair", new ReturnFromRepairCarCommand(repairInvoiceDAO));
        commands.put("saveRepairInvoice", new SaveRepairInvoiceCommand(repairInvoiceDAO));

        commands.put("reject_order", new RejectOrderCommand(orderDAO));
        commands.put("pay_invoice", new PayInvoiceCommand(orderDAO));
        commands.put("pay", new PayCommand(orderDAO));
        commands.put("return_order", new ReturnOrderedCarCommand(orderDAO));

        commands.put("cars", new CarsCommand(carDAO));
        commands.put("car", new CarCommand(carDAO));
        commands.put("save_car", new SaveCarCommand(carDAO, brandDAO, qClassDAO));
        commands.put("delete_car", new DeleteCarCommand(carDAO));

        LOG.debug("Command container was successfully initialized");
        LOG.trace("Number of commands --> " + commands.size());
    }

    public Command getCommand(){

        String commandName = req.getParameter("command");

        LOG.debug("Trying to get command " + commandName);
        if(commandName == null){
           commandName="main";
        }

        if (!commands.containsKey(commandName)) {
            LOG.trace("Command not found, name --> " + commandName);
            return commands.get("noCommand");
        }

        LOG.debug("return command " + commandName);
        return commands.get(commandName);
    }
}
