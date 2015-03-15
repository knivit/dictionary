package com.tsoft.dictionary.client.app.wordtrainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.client.server.app.WordTrainerServletProxy;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;

public class WordTrainerForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, WordTrainerForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField DictionaryPanel dictionaryPanel;
    @UiField WordPanel wordPanel;
    @UiField TranslationPanel translationPanel;
    @UiField HintPanel hintPanel;

    public WordTrainerForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() {
        dictionaryPanel.setForm(this);
        wordPanel.setForm(this);
        translationPanel.setForm(this);
        hintPanel.setForm(this);
    }

    private WordTrainerServletProxy getWordTrainerServlet() {
        return getServerServlets().getWordTrainerServlet();
    }

    private class LessonCallback extends DefaultAsyncCallback<Void, LessonResponseTO> {
        public LessonCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            dictionaryPanel.beforeLessonRPC();
            hintPanel.beforeLessonRPC();
            translationPanel.beforeLessonRPC();
            wordPanel.beforeLessonRPC();
        }

        @Override
        public void afterRemoteCall(LessonResponseTO responseTO, boolean isSuccess) {
            dictionaryPanel.afterLessonRPC(responseTO, isSuccess);
            hintPanel.afterLessonRPC(responseTO, isSuccess);
            translationPanel.afterLessonRPC(responseTO, isSuccess);
            wordPanel.afterLessonRPC(responseTO, isSuccess);
        }
    }

    public void startLesson() {
        getWordTrainerServlet().generateLesson(new LessonCallback(null));
    }
}
