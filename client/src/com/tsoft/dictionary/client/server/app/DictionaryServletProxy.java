package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.dictionary.TranslationResponseTO;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationRequestTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.server.app.web.dictionary.DictionaryServletInterfaceAsync;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;

public class DictionaryServletProxy extends ServerServletProxy {
    private DictionaryServletInterfaceAsync dictionaryServletAsync;

    public DictionaryServletProxy(ServerServlets serverServlets, DictionaryServletInterfaceAsync dictionaryServletAsync) {
        super(serverServlets);
        this.dictionaryServletAsync = dictionaryServletAsync;
    }

    public void getDictionaryList(AsyncCallback<ListResponseTO> callback) {
       dictionaryServletAsync.getDictionaryList(getServerServlets().getUserInfo(), callback);
    }

    public void getTranslation(TranslationRequestTO requestTO, AsyncCallback<TranslationResponseTO> callback) {
        dictionaryServletAsync.getTranslation(getServerServlets().getUserInfo(), requestTO, callback);
    }
}
