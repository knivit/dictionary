package com.tsoft.dictionary.server.app.factory;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.tsoft.dictionary.appengine.LocalAppengineEnvironment;
import com.tsoft.dictionary.server.app.EMF;
import com.tsoft.dictionary.server.app.service.model.IdPO;
import com.tsoft.dictionary.server.app.web.TestHelper;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;

public final class DatastoreHelper {
    private static final Logger logger = Logger.getLogger(DatastoreHelper.class.getName());
    
    private DatastoreHelper() { }

    /**
     * Persist an entity through JPA
     * http://code.google.com/intl/ru-RU/appengine/docs/java/datastore/relationships.html#Unowned_Relationships
     */
    public static void persist(IdPO po) throws Exception {
        EntityManager em = EMF.createEntityManager();
        try {
            em.getTransaction().begin();
            try {
                em.persist(po);
                em.getTransaction().commit();
            } catch (Exception ex) {
                logger.log(Level.SEVERE, "Can't persist an Entity", ex);
                if (em.getTransaction().isActive()) {
                    em.getTransaction().rollback();
                }
                throw ex;
            }
        } finally {
            em.close();
        }
    }

    /**
     * Put an entity to the DataStore using Google API
     */
    public static long put(IdPO po) throws Exception {
        Entity e = new Entity(po.getClass().getSimpleName());

        Method[] methods = po.getClass().getMethods();
        for (Method method : methods) {
            String methodName = method.getName();
            String propertyName = getPropertyName(methodName);
            if (propertyName != null) {
                Object val = method.invoke(po);
                if (val != null) {
                    setProperty(e, propertyName, val);
                }
            }
        }

        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        ds.put(e);

        return e.getKey().getId();
    }

    /**
     * Get an entity from the DataStore using Google API
     */
    public static <T> T get(Class<T> clazz, Long id) throws EntityNotFoundException {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        
        Key key = KeyFactory.createKey(clazz.getSimpleName(), id);
        return (T)ds.get(key);
    }
    
    private static String getPropertyName(String methodName) {
        boolean objectProperty = methodName.startsWith("get") && !methodName.equals("getClass");
        if (objectProperty) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        }

        boolean booleanProperty = methodName.startsWith("is");
        if (booleanProperty) {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        }

        return null;
    }

    private static void setProperty(Entity e, String propertyName, Object val) throws EntityNotFoundException {
        if (val instanceof IdPO) {
            IdPO po = (IdPO)val;
            propertyName = propertyName + ".id";
            val = po.getId();
        }
        e.setProperty(propertyName, val);
    }

    /**
     * Create a database file and fill it with a generated data
     *
     * Possible args:
     *  [0] - database file name (by default it will be created in the current folder)
     */
    public static void main(String[] args) throws Exception {
        String fileName = "database.dat";
        if (args.length > 0) {
            fileName = args[0];
        }

        LocalAppengineEnvironment.initForGeneratedDatabase(fileName);
        try {
            LibraryTestFactory.generateTestData(100, TestHelper.getDefaultUserInfo(), 5);

            logger.info("Database was generated into '" + fileName + "'");
        } finally {
            LocalAppengineEnvironment.destroy();
        }
    }
}
