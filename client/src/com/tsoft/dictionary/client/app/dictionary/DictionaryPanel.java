package com.tsoft.dictionary.client.app.dictionary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.server.callback.ListBoxCallbackHelper;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.client.app.dictionary.callback.DictionaryListRPC;
import com.tsoft.dictionary.client.app.dictionary.callback.TranslationRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.client.widget.ListBoxHelper;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationRequestTO;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationResponseTO;

public class DictionaryPanel extends AbstractPanel<DictionaryForm> implements DictionaryListRPC, TranslationRPC {
    interface Binder extends UiBinder<Widget, DictionaryPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField ListBox dictionaryBox;
    @UiField Button refreshButton;

    public DictionaryPanel() {
        super(binder);
    }

    @UiHandler("refreshButton")
    public void refreshButtonClick(ClickEvent event) {
        getForm().populateDictionaryList();
    }

    private String getSelectedDictionary() {
        return ListBoxHelper.getText(dictionaryBox);
    }

    @Override
    public void beforeDictionaryListRPC(Void requestTO) {
        ListBoxCallbackHelper.beforeRPC(dictionaryBox, requestTO);
        refreshButton.setEnabled(false);
    }

    @Override
    public void afterDictionaryListRPC(ListResponseTO responseTO, boolean isSuccess) {
        ListBoxCallbackHelper.afterRPC(dictionaryBox, responseTO, isSuccess, null);
        refreshButton.setEnabled(true);
    }

    @Override
    public void beforeTranslationRPC(TranslationRequestTO requestTO) {
        requestTO.setDictionaryName(getSelectedDictionary());
        dictionaryBox.setEnabled(false);
        refreshButton.setEnabled(false);
    }

    @Override
    public void afterTranslationRPC(TranslationResponseTO responseTO, boolean isSuccess) {
        dictionaryBox.setEnabled(isSuccess);
        refreshButton.setEnabled(isSuccess);
    }
}
