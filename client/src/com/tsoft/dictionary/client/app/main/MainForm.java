package com.tsoft.dictionary.client.app.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.tsoft.dictionary.client.widget.AbstractForm;
import com.tsoft.dictionary.client.server.app.LoginServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.client.server.callback.DefaultAsyncCallback;
import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;

public class MainForm extends AbstractForm<DockLayoutPanel> {
    interface Binder extends UiBinder<DockLayoutPanel, MainForm> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField MenuPanel menuPanel;
    @UiField TabPanel tabPanel;

    public MainForm(ServerServlets serverServlets) {
        super(serverServlets, binder);
    }

    @Override
    protected void createElements() {
        menuPanel.setForm(this);
        tabPanel.setForm(this);

        // Special-case stuff to make menuPanel overhang a bit.
        Element topElem = getFormPanel().getWidgetContainerElement(menuPanel);
        topElem.getStyle().setZIndex(2);
        topElem.getStyle().setOverflow(Overflow.VISIBLE);
    }

    private LoginServletProxy getLoginServlet() {
        return getServerServlets().getLoginServlet();
    }

    private class UserInfoCallback extends DefaultAsyncCallback<Void, UserInfoResponseTO> {
        public UserInfoCallback(Void requestTO) {
            super(requestTO);
        }

        @Override
        public void beforeRemoteCall(Void requestTO) {
            menuPanel.beforeUserInfoRPC(requestTO);
        }

        @Override
        public void afterRemoteCall(UserInfoResponseTO responseTO, boolean isSuccess) {
            menuPanel.afterUserInfoRPC(responseTO, isSuccess);
        }
    }

    public void getUserInfo() {
        UserInfoCallback callback = new UserInfoCallback(null);
        getLoginServlet().getUserInfo(callback);
    }
}
