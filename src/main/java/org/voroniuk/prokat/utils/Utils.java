package org.voroniuk.prokat.utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * Class for holding util methods of application
 *
 * @author D. Voroniuk
 */
public class Utils {
    public static int getPageNoFromRequest(HttpServletRequest req, String paramName, int totalPages) {
        int pageNo = 1;
        try {
            pageNo = Integer.parseInt(req.getParameter(paramName));
        } catch (NumberFormatException e) {
            pageNo = 1;
        }

        if (pageNo < 1) {
            pageNo = 1;
        }
        if (pageNo > totalPages) {
            pageNo = totalPages;
        }
        return pageNo;
    }

    public static String getOrderByQueryText(Map<String, Boolean> sortMap) {
        if (sortMap.isEmpty()) {
            return "";
        }
        StringBuilder sb = new StringBuilder(" ORDER BY ");
        boolean first = true;
        for (Map.Entry<String, Boolean> entry: sortMap.entrySet()) {
            if (entry.getValue()) {
                if (!first) {
                    sb.append(", ");
                }
                first = false;
                sb.append(entry.getKey() + " ");
            }
        }
        return sb.toString();
    }

    public static String getINQueryText(List<String> filterList, String columnName) {
        if (filterList.size() <= 0) {
            return "true ";
        }

        return columnName + " IN (" + String.join(", ", filterList) + ") ";

    }

}
