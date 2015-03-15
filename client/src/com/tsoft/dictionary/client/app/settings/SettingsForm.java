package com.tsoft.dictionary.client.app.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.widget.AbstractForm;

public class SettingsForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, SettingsForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    public SettingsForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() { }
}
