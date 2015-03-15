package com.tsoft.dictionary.server.context;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.servlet.ServletContext;

public class ServletContextHelper {
    private static final ServletContextHelper instance = new ServletContextHelper();

    private ServletContextHelper() { }

    public static ServletContextHelper getInstance() {
        return instance;
    }

    public <T extends ContextPersistent> T getFromContext(ServletContext context, Class<T> objectClass) {
        return (T)context.getAttribute(objectClass.getName());
    }

    public <T extends ContextPersistent> ArrayList<T> getAllFromContext(ServletContext context, Class<T> objectClass) {
        ArrayList<T> objectList = new ArrayList<T>();
        Enumeration attributeNames = context.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String attributeName = (String)attributeNames.nextElement();
            Object value = context.getAttribute(attributeName);
            if (objectClass.isInstance(value)) {
                objectList.add((T)value);
            }
        }
        return objectList;
    }

    public <T extends ContextPersistent> T getFromContext(ServletContext context, Class<T> objectClass, String name) {
        if (name == null) {
            return null;
        }

        T value = (T)context.getAttribute(name);
        if (!(objectClass.isInstance(value))) {
            value = null;
        }
        return value;
    }

    public <T extends ContextPersistent> void putToContext(ServletContext context, T value) {
        if (value != null) {
            context.setAttribute(value.getClass().getName(), value);
        }
    }

    public <T extends ContextPersistent> void putToContext(ServletContext context, T value, String name) {
        if (value != null && name != null) {
            context.setAttribute(name, value);
        }
    }
}
