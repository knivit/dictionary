package com.tsoft.dictionary.appengine;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalUserServiceTestConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * See spec http://code.google.com/intl/en/appengine/docs/java/howto/unittesting.html for details
 */
public final class LocalAppengineEnvironment {
    private static final Logger logger = Logger.getLogger(LocalAppengineEnvironment.class.getName());

    public static final String USER_NICKNAME = "GWTDebugMode";

    private static Map<Thread, LocalServiceTestHelper> map = new HashMap<Thread, LocalServiceTestHelper>();

    private static LocalServiceTestHelper gwtDebugEnvironment;

    private static LocalServiceTestHelper generateDatabaseEnvironment;

    private LocalAppengineEnvironment() { }

    public static synchronized void initForUnitTest() {
        Thread thread = Thread.currentThread();
        LocalServiceTestHelper localEnvironment = map.get(thread);
        if (localEnvironment == null) {
            localEnvironment = createLocalAppengineEnvironment(new LocalDatastoreServiceTestConfig());
            map.put(thread, localEnvironment);
        }
    }

    public static synchronized void initForGeneratedDatabase(String fileName) {
        if (generateDatabaseEnvironment == null) {
            logger.log(Level.INFO, "Generate Test Database mode. Create a database stored in a file.");
            LocalDatastoreServiceTestConfig datastoreConfig = new LocalDatastoreServiceTestConfig();
            datastoreConfig.setNoStorage(false);
            datastoreConfig.setBackingStoreLocation(fileName);
            generateDatabaseEnvironment = createLocalAppengineEnvironment(datastoreConfig);

            printDatastoreConfig(datastoreConfig);
        }
    }

    public static synchronized void initForGWTDebugMode() {
        if (gwtDebugEnvironment == null) {
            logger.log(Level.INFO, "GWT Debug Mode detected. Create Local Google App Engine Environment.");
            LocalDatastoreServiceTestConfig datastoreConfig = new LocalDatastoreServiceTestConfig();
            datastoreConfig.setNoStorage(false);
            datastoreConfig.setBackingStoreLocation("database.dat");
            gwtDebugEnvironment = createLocalAppengineEnvironment(datastoreConfig);

            printDatastoreConfig(datastoreConfig);
        }
    }

    private static LocalServiceTestHelper createLocalAppengineEnvironment(LocalDatastoreServiceTestConfig datastoreConfig) {
        LocalUserServiceTestConfig userConfig = new LocalUserServiceTestConfig();
        LocalServiceTestHelper localEnvironment = new LocalServiceTestHelper(datastoreConfig, userConfig);

        localEnvironment.setEnvEmail(USER_NICKNAME + "@mail.com");
        localEnvironment.setUp();
        return localEnvironment;
    }

    public static void printDatastoreConfig(LocalDatastoreServiceTestConfig config) {
        StringBuilder buf = new StringBuilder();
        buf.append("Datastore Config:\n");
        buf.append("  InMemoryDatabase (NoStorage)=").append(config.isNoStorage()).append('\n');
        if (!config.isNoStorage()) {
            buf.append("  BackingStoreLocation=").append(config.getBackingStoreLocation()).append('\n');
            buf.append("  MaxQueryLifetimeMs=").append(config.getMaxQueryLifetimeMs()).append('\n');
            buf.append("  MaxTxnLifetimeMs=").append(config.getMaxTxnLifetimeMs()).append('\n');
            buf.append("  StoreDelayMs=").append(config.getStoreDelayMs()).append('\n');
        }

        logger.info(buf.toString());
    }

    public static synchronized void destroy() {
        Thread thread = Thread.currentThread();
        LocalServiceTestHelper localEnvironment = map.get(thread);
        if (localEnvironment != null) {
            map.remove(thread);
            //localEnvironment.tearDown();
        }
    }
}
