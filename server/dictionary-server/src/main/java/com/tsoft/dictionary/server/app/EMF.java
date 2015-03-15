package com.tsoft.dictionary.server.app;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class EMF {
    private static final EntityManagerFactory INSTANCE = Persistence.createEntityManagerFactory("dictionary");

    private EMF() { }

    private static EntityManagerFactory get() {
        return INSTANCE;
    }

    public static EntityManager createEntityManager() {
        return new EntityManagerDelegate(get().createEntityManager());
    }
}
