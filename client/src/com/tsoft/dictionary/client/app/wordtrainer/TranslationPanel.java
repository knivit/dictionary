package com.tsoft.dictionary.client.app.wordtrainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.wordtrainer.callback.LessonRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;
import com.tsoft.dictionary.client.widget.handler.EnterKeyPressHandler;

public class TranslationPanel extends AbstractPanel<WordTrainerForm> implements LessonRPC {
    interface Binder extends UiBinder<Widget, TranslationPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField TextBox translationText;
    @UiField Button checkButton;
    @UiField Button tipButton;
    @UiField Button nextButton;

    private String translatedWord;

    public TranslationPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        translationText.addKeyPressHandler(new EnterKeyPressHandler(checkButton));
    }

    private String getTranslation() {
        return translationText.getText();
    }

    private void nextLesson() {
        getForm().startLesson();
    }

    @UiHandler("checkButton")
    public void checkButtonClick(ClickEvent event) {
        if (getTranslation() != null && getTranslation().equalsIgnoreCase(translatedWord)) {
            Window.alert("Correct !");
            nextLesson();
        } else {
            Window.alert("No, try again");
            translationText.setText(null);
            translationText.setFocus(true);
        }
    }

    @UiHandler("tipButton")
    public void tipButtonClick(ClickEvent event) {
        if (translatedWord != null) {
            Window.alert(translatedWord);
            translationText.setFocus(true);
        }
    }

    @UiHandler("nextButton")
    public void nextButtonClick(ClickEvent event) {
        nextLesson();
    }

    private void enableElements(boolean isEnable) {
        translationText.setEnabled(isEnable);
        checkButton.setEnabled(isEnable);
        tipButton.setEnabled(isEnable);
        nextButton.setEnabled(isEnable);
    }

    @Override
    public void beforeLessonRPC() {
        enableElements(false);
    }

    @Override
    public void afterLessonRPC(LessonResponseTO responseTO, boolean isSuccess) {
        translationText.setEnabled(isSuccess);
        checkButton.setEnabled(isSuccess);
        tipButton.setEnabled(isSuccess);
        nextButton.setEnabled(true);

        if (isSuccess) {
            translationText.setText(null);
            this.translatedWord = responseTO.getTranslatedWord();
            translationText.setFocus(true);
        }
    }
}
