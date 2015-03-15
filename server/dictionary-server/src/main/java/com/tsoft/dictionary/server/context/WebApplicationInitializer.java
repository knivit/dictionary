package com.tsoft.dictionary.server.context;

import java.util.ArrayList;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class WebApplicationInitializer implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        ServletContextHelper contextHelper = ServletContextHelper.getInstance();
        ArrayList<ContextPersistent> objectList = contextHelper.getAllFromContext(event.getServletContext(), ContextPersistent.class);
        for (ContextPersistent obj : objectList) {
            obj.contextDestroyed(event);
        }
    }
}
