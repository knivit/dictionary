package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.tsoft.dictionary.server.app.web.TestHelper;
import javax.servlet.ServletContext;

public class WordTrainerServletMock extends WordTrainerServlet {
    @Override
    public ServletContext getServletContext() {
        return TestHelper.getDictionaryServletContext();
    }
}
