package com.tsoft.dictionary.server.app;

import com.tsoft.dictionary.server.app.InjectorHelper.InjectorFieldList;
import com.tsoft.dictionary.server.app.service.ServerServices;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class Injector {
    private static final Logger logger = Logger.getLogger(Injector.class.getName());

    private Object obj;

    public Injector(Object obj) {
        this.obj = obj;
    }

    public void injectServerServices() throws Exception {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        for (Field field : fieldList) {
            Object service = createServerService(field);
            setDependency(field, service);
        }
    }

    public void injectEntityManager() throws Exception {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        if (fieldList.getEntityManagerField() != null) {
            Object em = EMF.createEntityManager();
            setDependency(fieldList.getEntityManagerField(), em);
        }
    }

    public void rejectServerServices() {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        for (Field field : fieldList) {
            releaseService(field);
        }
    }

    public void rejectEntityManager() throws Exception {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        if (fieldList.getEntityManagerField() != null) {
            closeEntityManager(fieldList.getEntityManagerField());
        }
    }

    private Object createServerService(Field field) {
        Class serviceInterface = field.getType();
        Object service = ServerServices.get(serviceInterface);
        if (service == null) {
            throw new RuntimeException("Service " + field.getType().getName() + " not found");
        }

        return service;
    }

    private void setDependency(Field field, Object value) throws Exception {
        try {
            field.set(obj, value);
        } catch (Exception exception) {
            logger.log(Level.SEVERE, "Can't inject a value into " + obj.getClass().getName() + '.' + field.getName(), exception);
            throw exception;
        }
    }

    private void closeEntityManager(Field field) throws Exception {
        EntityManager em = null;
        try {
            em = (EntityManager) field.get(obj);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't get a value from " + obj.getClass().getName() + '.' + field.getName() + " to close the Entity Manager", ex);
            throw ex;
        }

        if (em != null) {
            try {
                em.close();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Can't close the Entity Manager " + obj.getClass().getName() + '.' + field.getName(), ex);
                throw ex;
            }
        }
    }

    private void releaseService(Field field) {
        Object service = null;
        try {
            service = field.get(obj);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't get a value from " + obj.getClass().getName() + '.' + field.getName() + " to release the Server Service", ex);
            return;
        }

        if (service != null) {
            ServerServices.release(obj, service);
        }
    }
}
