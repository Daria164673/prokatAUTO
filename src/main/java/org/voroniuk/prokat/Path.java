package org.voroniuk.prokat;

/**
 * Constants that hold mapping of pages and commands of application
 *
 * @author D. Voroniuk
 */

public class Path {

        //pages
        public static final String PAGE__ERROR_PAGE = "/WEB-INF/jsp/error_page.jsp";
        public static final String PAGE__MAIN = "/WEB-INF/jsp/main.jsp";
        public static final String PAGE__REGISTER = "/WEB-INF/jsp/register.jsp";

        public static final String PAGE__USER_ACCOUNT = "/WEB-INF/jsp/user_account.jsp";

        public static final String PAGE__ORDER = "/WEB-INF/jsp/order.jsp";
        public static final String PAGE__ORDERS = "/WEB-INF/jsp/orders.jsp";
        public static final String PAGE__REPAIR_INVOICE = "/WEB-INF/jsp/repair_invoice.jsp";
        public static final String PAGE__REPAIR_INVOICES = "/WEB-INF/jsp/repair_invoices.jsp";

        public static final String PAGE__PAY_INVOICE = "/WEB-INF/jsp/pay_invoice.jsp";

        public static final String PAGE__CAR = "/WEB-INF/jsp/car.jsp";
        public static final String PAGE__CARS = "/WEB-INF/jsp/cars.jsp";
        public static final String PAGE__USERS = "/WEB-INF/jsp/users.jsp";


        //commands
        public static final String COMMAND__MAIN = "controller?command=main";
        public static final String COMMAND__ACCOUNT = "controller?command=account";
        public static final String COMMAND__USER_ACCOUNT = "controller?command=user_account";

        public static final String COMMAND__USERS = "controller?command=users";

        public static final String COMMAND__PAY_INVOICE = "controller?command=pay_invoice";
        public static final String COMMAND__CARS = "controller?command=cars";
        public static final String COMMAND__REPAIR_INVOICES = "controller?command=repair_invoices";
        public static final String COMMAND__ORDERS = "controller?command=orders";
}
