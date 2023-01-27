package org.voroniuk.prokat.web;

import java.util.Map;
import java.util.TreeMap;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
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
        commands.put("noCommand", new NoCommand());

        commands.put("main", new MainCommand());
        commands.put("changeLocale", new ChangeLocaleCommand());

        commands.put("login", new LoginCommand());
        commands.put("logout", new LogoutCommand());
        commands.put("register", new RegisterCommand());
        commands.put("change_users_block", new ChangeUsersBlockCommand());
        commands.put("users", new UsersCommand());

        commands.put("account", new AccountCommand());
        commands.put("user_account", new UserAccountCommand());

        commands.put("orders", new OrdersCommand());
        commands.put("order", new OrderCommand());
        commands.put("saveOrder", new SaveOrderCommand());

        commands.put("repair_invoices", new RepairInvoicesCommand());
        commands.put("repair_invoice", new RepairInvoiceCommand());
        commands.put("return_from_repair", new ReturnFromRepairCarCommand());
        commands.put("saveRepairInvoice", new SaveRepairInvoiceCommand());

        commands.put("reject_order", new RejectOrderCommand());
        commands.put("pay_invoice", new PayInvoiceCommand());
        commands.put("pay", new PayCommand());
        commands.put("return_order", new ReturnOrderedCarCommand());

        commands.put("cars", new CarsCommand());
        commands.put("car", new CarCommand());
        commands.put("save_car", new SaveCarCommand());
        commands.put("delete_car", new DeleteCarCommand());

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
