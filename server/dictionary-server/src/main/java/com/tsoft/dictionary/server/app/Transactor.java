package com.tsoft.dictionary.server.app;

import com.tsoft.dictionary.server.app.InjectorHelper.InjectorFieldList;
import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public class Transactor {
    private static final Logger logger = Logger.getLogger(Transactor.class.getName());

    private Object obj;

    public Transactor(Object obj) {
        this.obj = obj;
    }

    public void startTransaction() throws Exception {
        entityManagerStartTransaction();
    }

    public void commitTransaction() throws Exception {
        entityManagerCommitTransaction();
    }

    public void rollbackTransaction() throws Exception {
        entityManagerRollbackTransaction();
    }

    private void entityManagerStartTransaction() {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        Field field = fieldList.getEntityManagerField();
        if (field != null) {
            EntityManager em = getEntityManager(field);
            if (em != null && !em.getTransaction().isActive()) {
                em.getTransaction().begin();
            }
        }
    }

    private void entityManagerCommitTransaction() {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        Field field = fieldList.getEntityManagerField();
        if (field != null) {
            EntityManager em = getEntityManager(field);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().commit();
            }
        }
    }

    private void entityManagerRollbackTransaction() {
        InjectorFieldList fieldList = InjectorHelper.getFieldList(obj.getClass());

        // rollback Entity Manager's transaction
        Field field = fieldList.getEntityManagerField();
        if (field != null) {
            EntityManager em = getEntityManager(field);
            if (em != null && em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
        }
    }

    private EntityManager getEntityManager(Field field) {
        try {
            return (EntityManager) field.get(obj);
        } catch (Exception ex) {
            logger.log(Level.SEVERE, "Can't get a value from " + obj.getClass().getName() + '.' + field.getName() + " to close the Entity Manager", ex);
        }

        return null;
    }
}
