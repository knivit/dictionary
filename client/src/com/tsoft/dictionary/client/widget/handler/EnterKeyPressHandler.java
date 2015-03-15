package com.tsoft.dictionary.client.widget.handler;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.ui.Button;

public class EnterKeyPressHandler implements KeyPressHandler {
    private Button button;

    public EnterKeyPressHandler(Button button) {
        this.button = button;
    }

    @Override
    public void onKeyPress(KeyPressEvent event) {
        if (event.getCharCode() == 13) {
            button.click();
        }
    }
}
