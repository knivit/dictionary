package com.tsoft.dictionary.server.app.web.tutorial;

import com.tsoft.dictionary.server.app.web.dictionary.DictionaryServlet;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.context.ServletContextHelper;
import java.util.logging.Logger;
import javax.servlet.ServletContext;

public class TutorialServlet extends RemoteServiceServlet implements TutorialServletInterface {
    private static final Logger logger = Logger.getLogger(DictionaryServlet.class.getName());

    @Override
    public TutorialResponseTO getIndex(UserInfo userName) {
        TutorialIndex tutorialIndex = getTutorialIndex();

        TutorialResponseTO responseTO = new TutorialResponseTO();
        tutorialIndex.setValuesTo(responseTO);
        return responseTO;
    }

    private synchronized TutorialIndex getTutorialIndex() {
        ServletContextHelper contextHelper = ServletContextHelper.getInstance();
        TutorialIndex tutorialIndex = contextHelper.getFromContext(getServletContext(), TutorialIndex.class);
        if (tutorialIndex != null) {
            return tutorialIndex;
        }

        tutorialIndex = new TutorialIndex();
        tutorialIndex.load(getTutorialRealPath(getServletContext()));

        ServletContextHelper.getInstance().putToContext(getServletContext(), tutorialIndex);
        return tutorialIndex;
    }

    private String getTutorialRealPath(ServletContext servletContext) {
        String tutorialDirName = "/";
        return servletContext.getRealPath(tutorialDirName);
    }
}

