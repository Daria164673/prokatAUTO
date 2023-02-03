package org.voroniuk.prokat.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.Resources;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.connectionpool.DBManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringJoiner;

/**
 * Class for holding util methods of application
 *
 * @author D. Voroniuk
 */
public class Utils {

    private static final Logger LOG = Logger.getLogger(Utils.class);
    private static Properties properties;

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

    public static Properties getProperties() {
        if (properties==null) {
            properties = new Properties();
            try {
                properties.load(Resources.getInputStream("app.properties"));
            } catch (IOException e) {
                properties = null;
                LOG.error(e.getMessage());
            }
        }
        return properties;
    }

    public static String getImgPath() {
        return (properties.getProperty("fileImgPath")==null? "": properties.getProperty("fileImgPath"));
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
