package com.tsoft.dictionary.server.context;

import javax.servlet.ServletContextEvent;

public interface ContextPersistent {
    public void contextDestroyed(ServletContextEvent event);
}
