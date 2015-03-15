package com.tsoft.dict.wordtrainer;

import com.tsoft.mvc.Form;
import com.tsoft.mvc.View;

public class WordTrainerView extends View {
    public static final String WINDOW_CAPTION = "Word Trainer";

    @Override
    public WordTrainerController getController() {
        return (WordTrainerController)controller;
    }

    @Override
    public Class<? extends Form> getMainFormClass() {
        return WordTrainerForm.class;
    }

    @Override
    public String getCaption() {
        return WINDOW_CAPTION;
    }
}
