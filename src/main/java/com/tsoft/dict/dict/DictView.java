package com.tsoft.dict.dict;

import com.tsoft.mvc.Form;
import com.tsoft.mvc.View;

public class DictView extends View {
    public static final String WINDOW_CAPTION = "Dictionary";

    @Override
    public DictController getController() {
        return (DictController)controller;
    }

    @Override
    public Class<? extends Form> getMainFormClass() {
        return DictForm.class;
    }

    @Override
    public String getCaption() {
        return WINDOW_CAPTION;
    }
}
