package com.tsoft.dictionary.client.app.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.tutorial.callback.TutorialContentRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.web.content.ContentRequestTO;
import com.tsoft.dictionary.server.app.web.content.ContentResponseTO;
import com.tsoft.dictionary.client.widget.HtmlPage;
import com.tsoft.dictionary.client.widget.HtmlPageClickEvent;
import com.tsoft.dictionary.client.widget.HtmlPageClickHandler;

public class TextPanel extends AbstractPanel<TutorialForm> implements TutorialContentRPC {
    interface Binder extends UiBinder<Widget, TextPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField HtmlPage page;

    public TextPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        page.addPageClickHandler(new TextClickHandler());
    }

    private class TextClickHandler implements HtmlPageClickHandler {
        @Override
        public void onClick(HtmlPageClickEvent event) {
            String ref = event.getArg();
            getForm().openPage(ref);
        }
    }

    @Override
    public void beforeContentRPC(ContentRequestTO requestTO) { }

    @Override
    public void afterContentRPC(ContentResponseTO responseTO, boolean isSuccess) {
        page.setContent(responseTO.getContent());
    }
}
