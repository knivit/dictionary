package com.tsoft.dictionary.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;

public class DialogForm extends AbstractForm<Panel> {
    interface Binder extends UiBinder<Panel, DialogForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    private DialogBox dialogBox = new DialogBox();

    @UiField HTMLPanel contentPanel;
    @UiField Button okButton;
    @UiField Button cancelButton;

    private boolean needOkButton;
    private boolean needCancelButton;

    private int resultCode;

    public static final int CANCEL_RESULT_CODE = 0;
    public static final int OK_RESULT_CODE = 1;

    private Panel content;

    private Command closeCommand;

    public DialogForm(Panel content) {
        super(null, binder);

        this.content = content;
    }

    @Override
    protected void createElements() { }

    @Override
    public void open() {
        open("Dialog");
    }

    public void open(String text) {
        if (!isElementsCreated()) {
            super.open();

            dialogBox.setHeight("300px");
            dialogBox.setWidth("400px");
            dialogBox.setGlassEnabled(true);
            dialogBox.setModal(true);
            dialogBox.center();

            contentPanel.add(content, "content");

            // The default position is Relative (style="position: relative;")
            // and this is prevents the form to get full window size
            Element topElem = contentPanel.getElementById("content");
            topElem.getFirstChildElement().getStyle().setPosition(Position.STATIC);

            dialogBox.setWidget(getFormPanel());

            dialogBox.addCloseHandler(new DialogCloseHandler());
        }

        dialogBox.setText(text);
        dialogBox.show();
    }

    public int getResultCode() {
        return resultCode;
    }

    public boolean isNeedOkButton() {
        return needOkButton;
    }

    public void setNeedOkButton(boolean needOkButton) {
        this.needOkButton = needOkButton;
    }

    public boolean isNeedCancelButton() {
        return needCancelButton;
    }

    public void setNeedCancelButton(boolean needCancelButton) {
        this.needCancelButton = needCancelButton;
    }

    @UiHandler("okButton")
    public void okButtonClick(ClickEvent event) {
        resultCode = OK_RESULT_CODE;
        dialogBox.hide();
    }

    @UiHandler("cancelButton")
    public void cancelButtonClick(ClickEvent event) {
        resultCode = CANCEL_RESULT_CODE;
        dialogBox.hide();
    }

    private class DialogCloseHandler implements CloseHandler {
        @Override
        public void onClose(CloseEvent event) {
            if (closeCommand != null) {
                DeferredCommand.addCommand(closeCommand);
            }
        }
    }
    public boolean isOk() {
        return getResultCode() == OK_RESULT_CODE;
    }

    public boolean isCancel() {
        return getResultCode() == CANCEL_RESULT_CODE;
    }

    public void setDialogBoxCloseCommand(Command command) {
        this.closeCommand = command;
    }

    public Button getOkButton() {
        return okButton;
    }
}
