package com.tsoft.dictionary.server.app.service;

import java.lang.reflect.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class ServerServices {
    private static final Logger logger = Logger.getLogger(ServerServices.class.getName());

    private static final int MAX_SERVICES = 8192;
    private static final ServerServices INSTANCE = new ServerServices();

    private ServerService[] services = new ServerService[MAX_SERVICES];

    private ServerServices() { }

    private class ServerService {
        private ServerServiceProxy service;
        private Object proxy;
        private Thread thread;

        public ServerService(Thread thread, ServerServiceProxy service, Object proxy) {
            this.thread = thread;
            this.service = service;
            this.proxy = proxy;
        }

        public Thread getThread() {
            return thread;
        }

        public ServerServiceProxy getService() {
            return service;
        }

        public Object getProxy() {
            return proxy;
        }
    }

    public synchronized static <T> T get(Class<T> clazz) {
        ServerService serverService = INSTANCE.findServiceByThread(Thread.currentThread(), clazz);
        if (serverService == null) {
            try {
                serverService = INSTANCE.createServerService(clazz);
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Can't create a server service for '" + clazz.getName() + "'", ex);
                return null;
            }

            int index = INSTANCE.getEmptySlotIndex();
            if (index == -1) {
                logger.log(Level.SEVERE, "All Server Services' slots are used. Can not get an empty slot.");
                return null;
            }

            INSTANCE.put(index, serverService);
        }

        return (T)serverService.getProxy();
    }

    public synchronized static void release(Object obj, Object proxy) {
        ServerService serverService = INSTANCE.findServiceByProxyAndReleaseSlot(proxy);
        
        ServerServiceProxy serviceProxy = serverService.getService();
        try {
            serviceProxy.deactivateService();
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't deactivate " + obj.getClass().getName() + ':' + serviceProxy.getServerSeviceClass().getName() + " service");
        }
    }

    public synchronized static int getUsedSlotsPercent() {
        int slotCount = getSlotCount();
        int emptySlotCount = getEmptySlotCount();
        return emptySlotCount * 100 / slotCount;
    }

    public synchronized static int getEmptySlotCount() {
        int n = 0;
        for (int i = 0; i < MAX_SERVICES; i ++) {
            if (INSTANCE.services[i] == null) {
                n ++;
            }
        }
        return n;
    }

    public static int getSlotCount() {
        return MAX_SERVICES;
    }

    private ServerService createServerService(Class serviceInterface) throws Exception {
        ServerService serverService = null;
        String serviceClassName = serviceInterface.getName() + "Bean";
        try {
            serverService = createServerService(serviceInterface, Class.forName(serviceClassName));
        } catch (ClassNotFoundException ex) {
            logger.log(Level.SEVERE, "Can't find " + serviceClassName + " class", ex);
        }

        return serverService;
    }

    private ServerService createServerService(Class serviceInterface, Class serviceClass) throws Exception {
        ServerServiceProxy serviceProxy = null;
        serviceProxy = new ServerServiceProxy(serviceClass.newInstance());

        if (serviceProxy == null) {
            throw new IllegalStateException();
        }

        serviceProxy.activateService();

        Object proxy = Proxy.newProxyInstance(serviceInterface.getClassLoader(), new Class[] { serviceInterface }, serviceProxy);
        return new ServerService(Thread.currentThread(), serviceProxy, proxy);
    }

    private int getEmptySlotIndex() {
        for (int i = 0; i < MAX_SERVICES; i ++) {
            if (services[i] == null) {
                return i;
            }
        }
        return -1;
    }

    private void put(int index, ServerService service) {
        services[index] = service;
    }

    private ServerService findServiceByThread(Thread thread, Class clazz) {
        for (int i = 0; i < MAX_SERVICES; i ++) {
            if (services[i] == null) {
                continue;
            }
            
            if (clazz.isAssignableFrom(services[i].getProxy().getClass()) && services[i].getThread().equals(thread)) {
                return services[i];
            }
        }

        return null;
    }

    private ServerService findServiceByProxyAndReleaseSlot(Object proxy) {
        for (int i = 0; i < MAX_SERVICES; i ++) {
            if (services[i] != null && services[i].getProxy() == proxy) {
                ServerService serverService = services[i];
                services[i] = null;
                return serverService;
            }
        }

        throw new IllegalStateException("Can't find given proxy to release a slot");
    }
}
