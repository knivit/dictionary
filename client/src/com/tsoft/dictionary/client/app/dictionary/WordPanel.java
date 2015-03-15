package com.tsoft.dictionary.client.app.dictionary;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.client.app.dictionary.callback.DictionaryListRPC;
import com.tsoft.dictionary.client.app.dictionary.callback.TranslationRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationRequestTO;
import com.tsoft.dictionary.server.app.web.dictionary.TranslationResponseTO;
import com.tsoft.dictionary.client.widget.handler.EnterKeyPressHandler;

public class WordPanel extends AbstractPanel<DictionaryForm> implements DictionaryListRPC, TranslationRPC {
    interface Binder extends UiBinder<Widget, WordPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField TextBox wordText;
    @UiField Button translateButton;
    @UiField TextArea translationTextArea;

    public WordPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        wordText.addKeyPressHandler(new EnterKeyPressHandler(translateButton));
    }

    private String getWord() {
        return wordText.getText() == null ? "" : wordText.getText().trim();
    }

    private void translate() {
        getForm().translate();
    }

    @UiHandler("translateButton")
    public void translateButtonClick(ClickEvent event) {
        if (getWord().isEmpty()) {
            Window.alert("Enter a word for translation");
            wordText.setFocus(true);
            return;
        }

        translate();
    }

    @Override
    public void beforeDictionaryListRPC(Void requestTO) {
        translateButton.setEnabled(false);
    }

    @Override
    public void afterDictionaryListRPC(ListResponseTO responseTO, boolean isSuccess) {
        translateButton.setEnabled(true);
    }

    @Override
    public void beforeTranslationRPC(TranslationRequestTO requestTO) {
        requestTO.setValue(getWord());
        translateButton.setEnabled(false);
        translationTextArea.setText(null);
    }

    @Override
    public void afterTranslationRPC(TranslationResponseTO responseTO, boolean isSuccess) {
        translateButton.setEnabled(isSuccess);
        if (isSuccess) {
            translationTextArea.setText(responseTO.getValue());
            wordText.setFocus(true);
            wordText.selectAll();
        }
    }
}
