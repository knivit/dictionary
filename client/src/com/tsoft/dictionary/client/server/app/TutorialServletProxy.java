package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.tutorial.TutorialResponseTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialServletInterfaceAsync;

public class TutorialServletProxy extends ServerServletProxy {
    private TutorialServletInterfaceAsync tutorialServletAsync;

    public TutorialServletProxy(ServerServlets serverServlets, TutorialServletInterfaceAsync tutorialServletAsync) {
        super(serverServlets);
        this.tutorialServletAsync = tutorialServletAsync;
    }

    public void getIndex(AsyncCallback<TutorialResponseTO> callback) {
       tutorialServletAsync.getIndex(getServerServlets().getUserInfo(), callback);
    }
}