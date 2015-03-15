package com.tsoft.dictionary.server.app.web.dictionary;

import com.tsoft.dictionary.server.app.web.TestHelper;
import javax.servlet.ServletContext;

public class DictionaryServletMock extends DictionaryServlet {
    @Override
    public ServletContext getServletContext() {
        return TestHelper.getDictionaryServletContext();
    }
}
