package com.tsoft.dictionary.server.util.logger;

import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoggerUtil {
    private LoggerUtil() { }

    public void logRequest(Logger logger, HttpServletRequest request) {
        StringBuilder buf = new StringBuilder("\nRequest: ");
        buf.append(request.getRequestURL().toString()).append('\n');
        buf.append("Method=").append(request.getMethod()).append('\n');
        buf.append("Locale=").append(request.getLocale()).append('\n');
        buf.append("Headers:\n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            buf.append(headerName);
            buf.append('=');
            buf.append(request.getHeader(headerName).toString());
            buf.append('\n');
        }

        logger.log(Level.FINE, buf.toString());
    }

    public void logResponse(Logger logger, HttpServletResponse response) {
        StringBuilder buf = new StringBuilder("\nResponse:\n");
        buf.append("CharacterEncoding=").append(response.getCharacterEncoding()).append('\n');
        buf.append("ContentType=").append(response.getContentType()).append('\n');

        logger.log(Level.FINE, buf.toString());
    }
}
