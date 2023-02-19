package org.voroniuk.prokat.web.command;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.dao.BrandDAO;
import org.voroniuk.prokat.dao.CarDAO;
import org.voroniuk.prokat.dao.OrderDAO;
import org.voroniuk.prokat.dao.QClassDAO;
import org.voroniuk.prokat.dao.impl.BrandDAOimp;
import org.voroniuk.prokat.dao.impl.CarDAOimp;
import org.voroniuk.prokat.dao.impl.OrderDAOimp;
import org.voroniuk.prokat.dao.impl.QClassDAOimp;
import org.voroniuk.prokat.entity.*;
import org.voroniuk.prokat.utils.Utils;
import org.voroniuk.prokat.web.Command;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Cars command. Return cars page with filters, sorting and pagination
 *
 * @author D. Voroniuk
 */

public class CarsCommand implements Command{

    private final CarDAO carDAO;
    private static final Logger LOG = Logger.getLogger(CarsCommand.class);

    public CarsCommand(CarDAO carDAO) {
        this.carDAO = carDAO;
    }

    @Override
    public String execute(HttpServletRequest req, HttpServletResponse resp) {

        LOG.debug("Command starts");

        String[] strBrandsId = req.getParameterValues("filter_brands");
        List<String> brandsId=new ArrayList<>();
        if (strBrandsId!=null) {
            brandsId = Arrays.stream(strBrandsId).filter(x->{
                    try {
                        Integer.valueOf(x);
                        return true;
                    } catch (NumberFormatException e) {
                        return false;
                    }
            }).collect(Collectors.toList());
        }

        String[] strqClassId = req.getParameterValues("filter_qClass");
        List<String> qClassId=new ArrayList<>();
        if (strqClassId!=null) {
            qClassId = Arrays.stream(strqClassId).filter(x->{
                try {
                    Integer.valueOf(x);
                    return true;
                } catch (NumberFormatException e) {
                    return false;
                }
            }).collect(Collectors.toList());
        }

        int pageNo;
        int pageSize = 10;
        int totalPages = (int) Math.ceil((double) carDAO.countCars(brandsId, qClassId) / pageSize);

        pageNo = Utils.getPageNoFromRequest(req, "page", totalPages);

        String sorting = req.getParameter("sorting");
        Map<String, Boolean> sortMap= new LinkedHashMap<>();
        if (sorting!=null){
            if (sorting.equals("name")) {
                sortMap.put("brand_name", true);
                sortMap.put("model", true);
                sortMap.put("car_number", true);
            } else if (sorting.equals("name_desc")) {
                sortMap.put("brand_name desc", true);
                sortMap.put("model desc", true);
                sortMap.put("car_number desc", true);
            } else if (sorting.equals("price")) {
                sortMap.put("price", true);
            } else if (sorting.equals("price_desc")) {
                sortMap.put("price desc", true);
            }
        }

        List<Car> cars = carDAO.findCars(brandsId, qClassId, sortMap,(pageNo - 1) * pageSize, pageSize);

        String queryStr = req.getQueryString();
        StringJoiner sj = new StringJoiner("&");
        if (queryStr!=null) {
            String[] params = queryStr.split("&");
            for (String param: params) {
                if (param.substring(0,4).equals("page")) {
                    continue;
                }
                sj.add(param);
            }
        }

        req.setAttribute("current_page", req.getRequestURI() + "?" + sj);

        req.setAttribute("pageNo", pageNo);
        req.setAttribute("totalPages", totalPages);
        req.setAttribute("cars", cars);

        req.setAttribute("filter_brands", brandsId);
        req.setAttribute("filter_qClass", qClassId);

        req.setAttribute("curr_sorting", sorting);

        req.setAttribute("state_free", Car.State.FREE);

        String forward = Path.PAGE__CARS;

        LOG.debug("Command finished");

        return forward;
    }
}
