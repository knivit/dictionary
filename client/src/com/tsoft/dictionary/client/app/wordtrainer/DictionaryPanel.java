package com.tsoft.dictionary.client.app.wordtrainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.wordtrainer.callback.LessonRPC;
import com.tsoft.dictionary.client.app.wordtrainer.settings.SettingsForm;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;

public class DictionaryPanel extends AbstractPanel<WordTrainerForm> implements LessonRPC {
    interface Binder extends UiBinder<Widget, DictionaryPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField HTML dictionaryField;
    @UiField Button settingsButton;

    private SettingsForm settingsForm;

    public DictionaryPanel() {
        super(binder);
    }

    @UiHandler("settingsButton")
    public void settingsButtonClick(ClickEvent event) {
        getSettingsForm().open();
    }

    private SettingsForm getSettingsForm() {
        if (settingsForm == null) {
            settingsForm = new SettingsForm(getForm().getServerServlets());
            settingsForm.setCloseCommand(new SettingsFormCloseCommand());
        }
        return settingsForm;
    }

    private class SettingsFormCloseCommand implements Command {
        @Override
        public void execute() {
            if (settingsForm.isOk()) {
                getForm().startLesson();
            }
        }
    }

    @Override
    public void beforeLessonRPC() {
        settingsButton.setEnabled(false);
    }

    @Override
    public void afterLessonRPC(LessonResponseTO responseTO, boolean isSuccess) {
        if (isSuccess) {
            dictionaryField.setText(responseTO.getDictionaryName());
        } else {
            dictionaryField.setText("");
        }
        settingsButton.setEnabled(true);
    }
}
