package com.tsoft.dictionary.server.app.web;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class MockServletContext implements ServletContext {
    private Map<String, Object> attributes = new HashMap<String, Object>();

    @Override
    public ServletContext getContext(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMajorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getMinorVersion() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getMimeType(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set getResourcePaths(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public URL getResource(String string) throws MalformedURLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public InputStream getResourceAsStream(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public RequestDispatcher getNamedDispatcher(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Servlet getServlet(String string) throws ServletException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration getServlets() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration getServletNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(Exception excptn, String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void log(String string, Throwable thrwbl) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRealPath(String path) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getServerInfo() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getInitParameter(String string) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Enumeration getInitParameterNames() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Object getAttribute(String attributeName) {
        return attributes.get(attributeName);
    }

    @Override
    public Enumeration getAttributeNames() {
        Vector table = new Vector();
        table.addAll(attributes.keySet());
        return table.elements();
    }

    @Override
    public void setAttribute(String attributeName, Object obj) {
        attributes.put(attributeName, obj);
    }

    @Override
    public void removeAttribute(String attributeName) {
        attributes.remove(attributeName);
    }

    @Override
    public String getServletContextName() {
        throw new UnsupportedOperationException();
    }
}
