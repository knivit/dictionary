package com.tsoft.dictionary.client.widget;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.server.ServerServlets;

public abstract class AbstractPanel<F extends AbstractForm> extends Composite {
    private F form;
    private Widget panel;

    public AbstractPanel(UiBinder binder) {
        super();

        panel = (Widget)binder.createAndBindUi(this);
        initWidget(panel);
    }

    protected F getForm() {
        return form;
    }

    public void setForm(F form) {
        this.form = form;
    }

    protected ServerServlets getServerServlets() {
        return getForm().getServerServlets();
    }

    public Widget getPanel() {
        return panel;
    }
}
