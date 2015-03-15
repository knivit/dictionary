package com.tsoft.dict.dict.settings;

import com.tsoft.mvc.Form;
import com.tsoft.mvc.View;

public class SettingsView extends View {
    @Override
    public Class<? extends Form> getMainFormClass() {
        return SettingsForm.class;
    }

    @Override
    public String getCaption() {
        return "Dectionary Settings";
    }
}
