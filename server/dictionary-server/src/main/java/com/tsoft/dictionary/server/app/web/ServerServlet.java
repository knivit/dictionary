package com.tsoft.dictionary.server.app.web;

import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tsoft.dictionary.appengine.LocalAppengineEnvironment;
import com.tsoft.dictionary.server.app.service.ServerServices;
import java.util.logging.Logger;

public abstract class ServerServlet extends RemoteServiceServlet {
    private static final Logger logger = Logger.getLogger(ServerServlet.class.getName());

    @Override
    public String processCall(String payload) throws SerializationException {
        if (System.getProperty("GWTDebugMode") != null) {
            LocalAppengineEnvironment.initForGWTDebugMode();
        }
        return super.processCall(payload);
    }

    protected <T> T lookup(Class<T> serviceClass) {
        T service = ServerServices.get(serviceClass);
        if (service == null) {
            throw new IllegalStateException();
        }
        
        return service;
    }
}
