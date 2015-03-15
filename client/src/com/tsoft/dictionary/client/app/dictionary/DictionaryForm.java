package com.tsoft.dictionary.client.app.dictionary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.client.server.app.DictionaryServletProxy;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationRequestTO;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationResponseTO;

public class DictionaryForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, DictionaryForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField DictionaryPanel dictionaryPanel;
    @UiField WordPanel wordPanel;

    public DictionaryForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() {
        dictionaryPanel.setForm(this);
        wordPanel.setForm(this);

        populateDictionaryList();
    }

    private DictionaryServletProxy getDictionaryServlet() {
        return getServerServlets().getDictionaryServlet();
    }

    private class DictionaryListCallback extends DefaultAsyncCallback<Void, ListResponseTO> {
        public DictionaryListCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            dictionaryPanel.beforeDictionaryListRPC(requestTO);
            wordPanel.beforeDictionaryListRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(ListResponseTO responseTO, boolean isSuccess) {
            dictionaryPanel.afterDictionaryListRPC(responseTO, isSuccess);
            wordPanel.afterDictionaryListRPC(responseTO, isSuccess);
        }
    }

    public void populateDictionaryList() {
        getDictionaryServlet().getDictionaryList(new DictionaryListCallback(null));
    }

    private class TranslationAsyncCallback extends DefaultAsyncCallback<TranslationRequestTO, TranslationResponseTO> {
        public TranslationAsyncCallback(TranslationRequestTO requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(TranslationRequestTO requestTO) {
            dictionaryPanel.beforeTranslationRPC(requestTO);
            wordPanel.beforeTranslationRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(TranslationResponseTO responseTO, boolean isSuccess) {
            dictionaryPanel.afterTranslationRPC(responseTO, isSuccess);
            wordPanel.afterTranslationRPC(responseTO, isSuccess);
        }
    }

    public void translate() {
        TranslationRequestTO requestTO = new TranslationRequestTO();
        getDictionaryServlet().getTranslation(requestTO, new TranslationAsyncCallback(requestTO));
    }
}
