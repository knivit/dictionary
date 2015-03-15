package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.server.app.web.wordtrainer.SettingsRequestTO;
import com.tsoft.dictionary.server.app.web.wordtrainer.SettingsResponseTO;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.server.app.web.wordtrainer.WordTrainerServletInterfaceAsync;

public class WordTrainerServletProxy extends ServerServletProxy {
    private WordTrainerServletInterfaceAsync wordTrainerServletAsync;

    public WordTrainerServletProxy(ServerServlets serverServlets, WordTrainerServletInterfaceAsync wordTrainerServletAsync) {
        super(serverServlets);
        this.wordTrainerServletAsync = wordTrainerServletAsync;
    }

    public void getDictionaryList(AsyncCallback<ListResponseTO> callback) {
       wordTrainerServletAsync.getDictionaryList(getServerServlets().getUserInfo(), callback);
    }

    public void generateLesson(AsyncCallback<LessonResponseTO> callback) {
        wordTrainerServletAsync.generateLesson(getServerServlets().getUserInfo(), callback);
    }

    public void loadSettings(AsyncCallback<SettingsResponseTO> callback) {
        wordTrainerServletAsync.loadSettings(getServerServlets().getUserInfo(), callback);
    }

    public void saveSettings(SettingsRequestTO requestTO, AsyncCallback<Void> callback) {
        wordTrainerServletAsync.saveSettings(getServerServlets().getUserInfo(), requestTO, callback);
    }
}
