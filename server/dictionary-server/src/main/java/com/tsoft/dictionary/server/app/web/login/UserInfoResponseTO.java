package com.tsoft.dictionary.server.app.web.login;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class UserInfoResponseTO implements IsSerializable {
    private String userId;
    private String userName;
    private String loginUrl;
    private String logoutUrl;
    private boolean isAnonimous;

    public UserInfoResponseTO() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getLogoutUrl() {
        return logoutUrl;
    }

    public void setLogoutUrl(String logoutUrl) {
        this.logoutUrl = logoutUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isAnonimous() {
        return isAnonimous;
    }

    public void setIsAnonimous(boolean isAnonimous) {
        this.isAnonimous = isAnonimous;
    }

    @Override
    public String toString() {
        return UserInfoResponseTO.class.getName() +
           "[userId=" + userId + ", userName=" + userName +
           ", loginUrl=" + loginUrl + ", logoutUrl=" + logoutUrl + 
           ", isAnonimous=" + isAnonimous + "]";
    }
}
