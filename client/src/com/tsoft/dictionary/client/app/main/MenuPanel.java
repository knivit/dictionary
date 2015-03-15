package com.tsoft.dictionary.client.app.main;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.client.app.main.callback.UserInfoRPC;
import com.tsoft.dictionary.client.widget.AbstractPanel;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;

public class MenuPanel extends AbstractPanel<MainForm> implements UserInfoRPC {
    interface Binder extends UiBinder<Widget, MenuPanel> { }
    private static final Binder binder = GWT.create(Binder.class);

    @UiField HTML userName;
    @UiField Anchor logInLink;
    @UiField Anchor logOutLink;
    @UiField Anchor aboutLink;

    public MenuPanel() {
        super(binder);
    }

    @Override
    protected void onLoad() {
        populateValues();
    }

    private void populateValues() {
        UserInfo userInfo = getForm().getServerServlets().getUserInfo();

        userName.setText(userInfo.getName());
        logInLink.setEnabled(userInfo.isAnonimous());
        if (logInLink.isEnabled()) {
            logInLink.setHref(userInfo.getLoginUrl());
        } else {
            logInLink.setHref(null);
        }
        
        logOutLink.setEnabled(!userInfo.isAnonimous());
        if (logOutLink.isEnabled()) {
            logOutLink.setHref(userInfo.getLogoutUrl());
        } else {
            logOutLink.setHref(null);
        }
    }

    @Override
    public void beforeUserInfoRPC(Void requestTO) {
        logInLink.setEnabled(false);
    }

    private class LoginRedirectCommand implements Command {
        private String loginUrl;

        public LoginRedirectCommand(String loginUrl) {
            this.loginUrl = loginUrl;
        }

        @Override
        public void execute() {
            Window.Location.assign(loginUrl);
        }
    }

    @Override
    public void afterUserInfoRPC(UserInfoResponseTO responseTO, boolean isSuccess) {
        if (isSuccess) {
            if (responseTO.getUserId() == null) {
                if (responseTO.getLoginUrl() != null) {
                    Command command = new LoginRedirectCommand(responseTO.getLoginUrl());
                    DeferredCommand.addCommand(command);
                } else {
                    Window.alert("Can't create a Login URL");
                }
            } else {
                UserInfo userInfo = new UserInfo(responseTO);
                getForm().getServerServlets().setUserInfo(userInfo);

                populateValues();
            }
        } else {
            logInLink.setEnabled(true);
            logOutLink.setEnabled(false);
        }
    }
}
