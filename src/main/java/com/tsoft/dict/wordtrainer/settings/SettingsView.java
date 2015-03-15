package com.tsoft.dict.wordtrainer.settings;

import com.tsoft.mvc.Form;
import com.tsoft.mvc.View;

public class SettingsView extends View {
    public static final String WINDOW_CAPTION = "Word Trainer Settings";

    @Override
    public SettingsController getController() {
        return (SettingsController)controller;
    }

    @Override
    public Class<? extends Form> getMainFormClass() {
        return SettingsForm.class;
    }

    @Override
    public String getCaption() {
        return WINDOW_CAPTION;
    }
}
