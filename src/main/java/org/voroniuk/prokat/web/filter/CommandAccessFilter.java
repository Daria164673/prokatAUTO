package org.voroniuk.prokat.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.voroniuk.prokat.Path;
import org.voroniuk.prokat.entity.User;

import java.io.IOException;
import java.util.*;


/**
 * Security filter.
 *
 * @author D. Voroniuk
 */

public class CommandAccessFilter implements Filter {

	private static final Logger LOG = Logger.getLogger(CommandAccessFilter.class);

	// commands access	
	private static Map<User.Role, List<String>> accessMap = new HashMap<User.Role, List<String>>();
	private static List<String> commons = new ArrayList<String>();
	private static List<String> outOfControl = new ArrayList<String>();
	
	public void destroy() {
		LOG.debug("Filter destruction starts");
		// do nothing
		LOG.debug("Filter destruction finished");
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		LOG.debug("Filter starts");
		
		if (accessAllowed(request)) {
			LOG.debug("Filter finished");
			chain.doFilter(request, response);
		} else {
			String errorMessasge = "You do not have permission to access the requested resource";
			
			request.setAttribute("errorMessage", errorMessasge);
			LOG.trace("Set the request attribute: errorMessage --> " + errorMessasge);
			
			request.getRequestDispatcher(Path.PAGE__ERROR_PAGE)
					.forward(request, response);
		}
	}
	
	private boolean accessAllowed(ServletRequest request) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;

		String commandName = request.getParameter("command");
		if (commandName == null || commandName.isEmpty())
			return true;
		
		if (outOfControl.contains(commandName))
			return true;
		
		HttpSession session = httpRequest.getSession(false);
		if (session == null) 
			return false;

		User user = (User) session.getAttribute("user");
		if (user == null)
			return false;

		User.Role userRole = user.getRole();

		LOG.debug("User role = " + userRole + ". Trying get command = " + commandName);
		
		return accessMap.get(userRole).contains(commandName)
				|| commons.contains(commandName);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		LOG.debug("Filter initialization starts");
		
		// roles
		accessMap.put(User.Role.MANAGER, asList(fConfig.getInitParameter("manager")));
		accessMap.put(User.Role.CUSTOMER, asList(fConfig.getInitParameter("customer")));
		accessMap.put(User.Role.ADMIN, asList(fConfig.getInitParameter("admin")));
		LOG.trace("Access map --> " + accessMap);

		// commons
		commons = asList(fConfig.getInitParameter("common"));
		LOG.trace("Common commands --> " + commons);

		// out of control
		outOfControl = asList(fConfig.getInitParameter("out-of-control"));
		LOG.trace("Out of control commands --> " + outOfControl);
		
		LOG.debug("Filter initialization finished");
	}
	
	/**
	 * Extracts parameter values from string.
	 * 
	 * @param str
	 *            parameter values string.
	 * @return list of parameter values.
	 */
	private List<String> asList(String str) {
		List<String> list = new ArrayList<String>();
		StringTokenizer st = new StringTokenizer(str);
		while (st.hasMoreTokens()) list.add(st.nextToken());
		return list;		
	}
	
}