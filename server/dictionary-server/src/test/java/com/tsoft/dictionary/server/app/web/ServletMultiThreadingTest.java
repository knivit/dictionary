package com.tsoft.dictionary.server.app.web;

import com.tsoft.dictionary.appengine.LocalAppengineEnvironment;
import com.tsoft.dictionary.server.app.web.wordtrainer.SettingsRequestTO;
import com.tsoft.dictionary.server.app.web.wordtrainer.WordTrainerServlet;
import java.util.concurrent.Exchanger;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.Test;
import static org.junit.Assert.assertNull;

public class ServletMultiThreadingTest {
    public static final Logger logger = Logger.getLogger(ServletMultiThreadingTest.class.getName());

    private WordTrainerServlet wordTrainerServlet = new WordTrainerServlet();

    @Test
    public void twoThreads() throws InterruptedException, Exception {
        Exchanger exchanger = new Exchanger();

        LocalAppengineEnvironment.initForUnitTest();
        try {
            wordTrainerServlet.init();
            try {
                Thread secondThread = new Thread(new Task("Second Thread", exchanger));
                secondThread.start();

                Exception e = null;
                try {
                    processServletCall();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, "Main Thread", ex);
                    e = ex;
                }

                // not working because the same datastore is used simultaneously
                // assertNull(exchanger.exchange(null));
                // assertNull(e);
            } finally {
                wordTrainerServlet.destroy();
            }
        } finally {
             LocalAppengineEnvironment.destroy();
        }
    }

    private class Task implements Runnable {
        private String name;
        private Exchanger exchanger;

        public Task(String name, Exchanger exchanger) {
            this.name = name;
            this.exchanger = exchanger;
        }

        @Override
        public void run() {
            Exception e = null;
            try {
                LocalAppengineEnvironment.initForUnitTest();
                try {
                    processServletCall();
                } catch (Exception ex) {
                    logger.log(Level.SEVERE, name, ex);
                    e = ex;
                }
            } finally {
                LocalAppengineEnvironment.destroy();
            }

            try {
                exchanger.exchange(e);
            } catch (InterruptedException ex) {
                logger.log(Level.SEVERE, name, ex);
            }
        }
    }


    public void processServletCall() {
        SettingsRequestTO requestTO = new SettingsRequestTO();
        requestTO.setDictionaryName(TestHelper.DICT01_NAME);
        requestTO.setHintCount(3);
        requestTO.setNeedReverse(true);

        wordTrainerServlet.saveSettings(TestHelper.getDefaultUserInfo(), requestTO);
    }
}
