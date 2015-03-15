package com.tsoft.dictionary.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.server.ServerServlets;

public abstract class AbstractForm<E extends Widget> extends Widget {
    private ServerServlets serverServlets;
    private UiBinder binder;

    private E formPanel;
    private boolean isElementsCreated;

    protected abstract void createElements();

    public AbstractForm(ServerServlets serverServlets, UiBinder binder) {
        this.serverServlets = serverServlets;
        this.binder = binder;
    }

    public void open() {
        if (!isElementsCreated()) {
            createAndBindUi();
            createElements();
            elementsCreated();
        }
    }

    public ServerServlets getServerServlets() {
        return serverServlets;
    }

    public E getFormPanel() {
        if (formPanel == null) {
            open();
        }
        return formPanel;
    }

    public boolean isElementsCreated() {
        return isElementsCreated;
    }

    protected void elementsCreated() {
        isElementsCreated = true;
    }

    private void createAndBindUi() {
        formPanel = (E)binder.createAndBindUi(this);
    }
}
