package com.tsoft.dictionary.server.app;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.Query;

public class EntityManagerDelegate implements EntityManager {
    private EntityManager em;

    public EntityManagerDelegate(EntityManager em) {
        if (em == null) {
            throw new RuntimeException("Can't create an Entity Manager");
        }
        this.em = em;

        setFlushMode(FlushModeType.COMMIT);
    }

    @Override
    public void persist(Object obj) {
        em.persist(obj);
    }

    @Override
    public <T> T merge(T obj) {
        return em.merge(obj);
    }

    @Override
    public void remove(Object obj) {
        em.remove(obj);
    }

    @Override
    public <T> T find(Class<T> objectClass, Object param) {
        return em.find(objectClass, param);
    }

    @Override
    public <T> T getReference(Class<T> objectClass, Object param) {
        return em.getReference(objectClass, param);
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public void setFlushMode(FlushModeType fmt) {
        em.setFlushMode(fmt);
    }

    @Override
    public FlushModeType getFlushMode() {
        return em.getFlushMode();
    }

    @Override
    public void lock(Object obj, LockModeType lmt) {
        em.lock(obj, lmt);
    }

    @Override
    public void refresh(Object obj) {
        em.refresh(obj);
    }

    @Override
    public void clear() {
        em.clear();
    }

    @Override
    public boolean contains(Object obj) {
        return em.contains(obj);
    }

    @Override
    public Query createQuery(String string) {
        return em.createQuery(string);
    }

    @Override
    public Query createNamedQuery(String string) {
        return em.createNamedQuery(string);
    }

    @Override
    public Query createNativeQuery(String string) {
        return em.createNativeQuery(string);
    }

    @Override
    public Query createNativeQuery(String string, Class type) {
        return em.createNativeQuery(string, type);
    }

    @Override
    public Query createNativeQuery(String string, String string1) {
        return em.createNativeQuery(string, string1);
    }

    @Override
    public void joinTransaction() {
        em.joinTransaction();
    }

    @Override
    public Object getDelegate() {
        return em.getDelegate();
    }

    @Override
    public void close() {
        em.close();
    }

    @Override
    public boolean isOpen() {
        return em.isOpen();
    }

    @Override
    public EntityTransaction getTransaction() {
        return em.getTransaction();
    }
}
