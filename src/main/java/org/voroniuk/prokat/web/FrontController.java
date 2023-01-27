package org.voroniuk.prokat.web;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.QClassDAO;
import org.voroniuk.prokat.dao.impl.BrandDAOimp;
import org.voroniuk.prokat.dao.impl.QClassDAOimp;
import org.voroniuk.prokat.entity.SiteLocale;

import java.io.IOException;

/**
 * Main controller of application. Retrieves from request and run execution of command.
 *
 * @author D. Voroniuk
 */

@WebServlet(name = "Controller", urlPatterns = {"/", "/controller*"})
public class FrontController extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        BrandDAO brandDAO = new BrandDAOimp();
        QClassDAO qClassDAO = new QClassDAOimp();

        getServletContext().setAttribute("locales", SiteLocale.values());
        getServletContext().setAttribute("brands", brandDAO.findAllBrands());
        getServletContext().setAttribute("qClasses", qClassDAO.findAllQClasses());

    }

    private static final Logger LOG = Logger.getLogger(FrontController.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String resultPage;

        RequestHelper helper = new RequestHelper(req);
        Command command =  helper.getCommand();

        // delegate request to a command object helper
        resultPage = command.execute(req, resp);

        RequestDispatcher dispatcher = req.getRequestDispatcher(resultPage);
        resp.setStatus(resp.SC_TEMPORARY_REDIRECT);
        if (!resultPage.equals("redirect")) {
            try {
                dispatcher.forward(req, resp);
            } catch (Exception e) {
                LOG.error("cannot open page " + resultPage, e);
            }

        }
    }
}
