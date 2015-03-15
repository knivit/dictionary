package com.tsoft.dictionary.client.app.wordtrainer.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.TextBox;
import com.tsoft.dictionary.client.widget.DialogForm;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.app.WordTrainerServletProxy;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.server.app.web.wordtrainer.SettingsRequestTO;
import com.tsoft.dictionary.server.app.web.wordtrainer.SettingsResponseTO;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.client.server.callback.ListBoxCallbackHelper;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.client.widget.ListBoxHelper;
import com.tsoft.dictionary.client.widget.handler.EnterKeyPressHandler;

public class SettingsForm extends AbstractForm<Panel> {
    interface Binder extends UiBinder<Panel, SettingsForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField ListBox dictionaryBox;
    @UiField Button refreshButton;
    @UiField TextBox hintCountField;
    @UiField CheckBox needReverseField;

    private Command closeCommand;

    private DialogForm dialogForm;

    public SettingsForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    public void open() {
        super.open();
        dialogForm.open();
    }

    @Override
    protected void createElements() {
        dialogForm = new DialogForm(getFormPanel());

        dictionaryBox.addKeyPressHandler(new EnterKeyPressHandler(dialogForm.getOkButton()));
        hintCountField.addKeyPressHandler(new EnterKeyPressHandler(dialogForm.getOkButton()));

        dialogForm.setNeedOkButton(true);
        dialogForm.setNeedCancelButton(true);
        dialogForm.setDialogBoxCloseCommand(new DialogBoxCloseCommand());

        populateDictionaryList();

        loadSettings();
    }

    private class DialogBoxCloseCommand implements Command {
        @Override
        public void execute() {
            if (dialogForm.isOk()) {
                saveSettings();
            }
        }
    }

    public boolean isOk() {
        return dialogForm.isOk();
    }

    @UiHandler("refreshButton")
    public void refreshButtonClick(ClickEvent event) {
        populateDictionaryList();
    }

    private String getDictionaryName() {
        return ListBoxHelper.getText(dictionaryBox);
    }

    private int getHintCount() {
        return Integer.parseInt(hintCountField.getText());
    }
    
    private void setHintCount(int hintCount) {
        hintCountField.setText(Integer.toString(hintCount));
    }
    
    private boolean isNeedReverse() {
        return needReverseField.getValue();
    }
    
    private void setNeedReverse(boolean isNeedReverse) {
        needReverseField.setValue(isNeedReverse);
    }

    private WordTrainerServletProxy getWordTrainerServlet() {
        return getServerServlets().getWordTrainerServlet();
    }

    public void setCloseCommand(Command closeCommand) {
        this.closeCommand = closeCommand;
    }

    private class DictionaryListCallback extends DefaultAsyncCallback<Void, ListResponseTO> {
        public DictionaryListCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            ListBoxCallbackHelper.beforeRPC(dictionaryBox, requestTO);
        }

        @Override
        public void afterRemoteCall(ListResponseTO responseTO, boolean isSuccess) {
            ListBoxCallbackHelper.afterRPC(dictionaryBox, responseTO, isSuccess, null);
        }
    }

    public void populateDictionaryList() {
        getWordTrainerServlet().getDictionaryList(new DictionaryListCallback(null));
    }

    private class LoadSettingsCallback extends DefaultAsyncCallback<Void, SettingsResponseTO> {
        public LoadSettingsCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) { }

        @Override
        public void afterRemoteCall(SettingsResponseTO responseTO, boolean isSuccess) {
            if (isSuccess) {
                setHintCount(responseTO.getHintCount());
                setNeedReverse(responseTO.isNeedReverse());
            }
        }
    }

    public void loadSettings() {
        getWordTrainerServlet().loadSettings(new LoadSettingsCallback(null));
    }

    private class SaveSettingsCallback extends DefaultAsyncCallback<SettingsRequestTO, Void> {
        public SaveSettingsCallback(SettingsRequestTO requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(SettingsRequestTO requestTO) {
            requestTO.setDictionaryName(getDictionaryName());
            requestTO.setHintCount(getHintCount());
            requestTO.setNeedReverse(isNeedReverse());
        }

        @Override
        public void afterRemoteCall(Void responseTO, boolean isSuccess) {
            if (isSuccess) {
                closeCommand.execute();
            }
        }
    }

    public void saveSettings() {
        SettingsRequestTO requestTO = new SettingsRequestTO();
        getWordTrainerServlet().saveSettings(requestTO, new SaveSettingsCallback(requestTO));
    }
}
