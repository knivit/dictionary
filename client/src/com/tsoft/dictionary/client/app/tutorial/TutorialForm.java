package com.tsoft.dictionary.client.app.tutorial;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.client.server.app.ContentServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.app.TutorialServletProxy;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.client.server.callback.ManagedAsyncCallback;
import com.tsoft.dictionary.server.app.web.content.ContentRequestTO;
import com.tsoft.dictionary.server.app.web.content.ContentResponseTO;
import com.tsoft.dictionary.server.app.web.tutorial.TutorialResponseTO;

public class TutorialForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, TutorialForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField GlossaryPanel glossaryPanel;
    @UiField TextPanel textPanel;

    public TutorialForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() {
        glossaryPanel.setForm(this);
        textPanel.setForm(this);

        populateGlossaryTree();
    }

    private TutorialServletProxy getTutorialServlet() {
        return getServerServlets().getTutorialServlet();
    }

    private ContentServletProxy getContentServlet() {
        return getServerServlets().getContentServlet();
    }

    private class TutorialContentCallback extends ManagedAsyncCallback<ContentRequestTO, ContentResponseTO> {
        public TutorialContentCallback(ContentRequestTO requestTO) {
            super(getContentServlet(), requestTO);
        }

        @Override
        public void beforeRemoteCall(ContentRequestTO requestTO) {
            glossaryPanel.beforeContentRPC(requestTO);
            textPanel.beforeContentRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(ContentResponseTO responseTO, boolean isSuccess) {
            glossaryPanel.afterContentRPC(responseTO, isSuccess);
            textPanel.afterContentRPC(responseTO, isSuccess);
        }
    }

    public void getContent() {
        ContentRequestTO requestTO = new ContentRequestTO();
        getContentServlet().getContent(requestTO, new TutorialContentCallback(requestTO));
    }

    private class GlossaryIndexCallback extends DefaultAsyncCallback<Void, TutorialResponseTO> {
        public GlossaryIndexCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            glossaryPanel.beforeGlossaryIndexRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(TutorialResponseTO responseTO, boolean isSuccess) {
            glossaryPanel.afterGlossaryIndexRPC(responseTO, isSuccess);
        }
    }

    private void populateGlossaryTree() {
        getTutorialServlet().getIndex(new GlossaryIndexCallback(null));
    }

    public void openPage(String ref) {
        glossaryPanel.openPage(ref);
    }
}
