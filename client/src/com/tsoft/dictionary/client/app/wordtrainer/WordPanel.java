package com.tsoft.dictionary.client.app.wordtrainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.wordtrainer.callback.LessonRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;

public class WordPanel extends AbstractPanel<WordTrainerForm> implements LessonRPC {
    interface Binder extends UiBinder<Widget, WordPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField HTML word;

    public WordPanel() {
        super(binder);
    }

    @Override
    public void beforeLessonRPC() { }

    @Override
    public void afterLessonRPC(LessonResponseTO responseTO, boolean isSuccess) {
        if (isSuccess) {
            word.setText(responseTO.getWord());
        }
    }
}
