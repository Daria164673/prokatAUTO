package org.voroniuk.prokat.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Main interface for the Command pattern implementation.
 * provided method execute()
 * @author D. Voroniuk
 */

public interface Command {
    String execute(HttpServletRequest req, HttpServletResponse resp);
}
