package com.tsoft.dictionary.server.app.service;

import com.tsoft.dictionary.server.app.Injector;
import com.tsoft.dictionary.server.app.Transactor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServerServiceProxy implements InvocationHandler {
    private static final Logger logger = Logger.getLogger(ServerServiceProxy.class.getName());

    private Object serverService;

    private Injector injector;
    private Transactor transactor;
    private int level;

    public ServerServiceProxy(Object serverService) {
        this.serverService = serverService;
        injector = new Injector(serverService);
        transactor = new Transactor(serverService);
    }

    public void activateService() throws Exception {
        injector.injectServerServices();
    }

    public void deactivateService() throws Exception {
        injector.rejectServerServices();
    }

    public Class getServerSeviceClass() {
        return serverService.getClass();
    }

    @Override
    public Object invoke(Object proxy, Method m, Object[] args) throws Throwable {
        if (level == 0) {
            injector.injectEntityManager();
        }
        
        Object result = null;
        try {
            try {
                if (level == 0) {
                    transactor.startTransaction();
                }

                level ++;
                result = m.invoke(serverService, args);
                level --;
            } catch (Throwable ex) {
                level = 0;
                logExceptionDetails(m, args, ex);
                transactor.rollbackTransaction();
                throw ex;
            } finally {
                if (level == 0) {
                    transactor.commitTransaction();
                }
            }
        } finally {
            if (level == 0) {
                injector.rejectEntityManager();
            }
        }
        
        return result;
    }

    private void logExceptionDetails(Method m, Object[] args, Throwable ex) {
        String msg = "An exception occured during an invocation of " + serverService.getClass().getName() + '.' + m.getName();
        if (args.length > 0) {
            StringBuilder buf = new StringBuilder();
            boolean isFirst = true;
            for (Object arg : args) {
                if (!isFirst) {
                    buf.append(", ");
                }
                buf.append(arg == null ? "null" : arg.toString());
                isFirst = false;
            }
            msg = msg + "(" + buf.toString() + ")";
        }

        logger.log(Level.SEVERE, msg, ex);
    }
}