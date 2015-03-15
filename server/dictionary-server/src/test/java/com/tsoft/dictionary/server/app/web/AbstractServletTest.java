package com.tsoft.dictionary.server.app.web;

import com.tsoft.dictionary.appengine.LocalAppengineEnvironment;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractServletTest {
    @Before
    public void init() throws InstantiationException, IllegalAccessException {
        LocalAppengineEnvironment.initForUnitTest();
        TestHelper.injectServlet(this);
    }

    @After
    public void destroy() throws IllegalArgumentException, IllegalAccessException {
        LocalAppengineEnvironment.destroy();
    }
}
