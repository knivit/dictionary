package com.tsoft.dictionary.client.app.wordtrainer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.wordtrainer.callback.LessonRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.wordtrainer.LessonResponseTO;
import java.util.ArrayList;

public class HintPanel extends AbstractPanel<WordTrainerForm> implements LessonRPC {
    interface Binder extends UiBinder<Widget, HintPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    private ArrayList<HTML> hintList = new ArrayList<HTML>();

    @UiField HTMLPanel contentPanel;
    private VerticalPanel content = new VerticalPanel();

    public HintPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        contentPanel.add(content, "content");
    }

    private void resetPanel() {
        content.clear();
    }

    private Panel getContent() {
        return content;
    }

    @Override
    public void beforeLessonRPC() { }

    @Override
    public void afterLessonRPC(LessonResponseTO responseTO, boolean isSuccess) {
        ArrayList<String> newHintList;
        if (isSuccess) {
            newHintList = responseTO.getHintList();
        } else {
            newHintList = new ArrayList<String>();
        }

        if (newHintList.size() != hintList.size()) {
            resetPanel();

            hintList = new ArrayList<HTML>(newHintList.size());
            for (int i = 0; i < newHintList.size(); i ++) {
                HTML hint = new HTML();
                getContent().add(hint);
                hintList.add(hint);
            }
        }

        for (int i = 0; i < hintList.size(); i ++) {
            HTML hint = hintList.get(i);
            hint.setText(newHintList.get(i));
        }
    }
}
