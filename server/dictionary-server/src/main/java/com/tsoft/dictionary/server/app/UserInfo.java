package com.tsoft.dictionary.server.app;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;

@GWTClient
public class UserInfo implements IsSerializable {
    public final static UserInfo UNDEFINED_USERINFO = new UserInfo();

    public final static String ANONIMOUS_NAME = "Anonimous";

    private String id;
    private String name;
    private String loginUrl;
    private String logoutUrl;
    private boolean isAnonimous;

    public UserInfo() { }

    public UserInfo(UserInfoResponseTO to) {
        this.id = to.getUserId();
        this.name = to.getUserName();
        this.loginUrl = to.getLoginUrl();
        this.logoutUrl = to.getLogoutUrl();
        this.isAnonimous = to.isAnonimous();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public boolean isAnonimous() {
        return isAnonimous;
    }

    public void setIsAnonimous(boolean isAnonimous) {
        this.isAnonimous = isAnonimous;
    }

    @Override
    public String toString() {
        return "com.tsoft.dictionary.client.server.UserInfo" +
            "[id=" + id + ", name=" + name + ",loginUrl=" + loginUrl + ", logoutUrl=" + logoutUrl + "]";
    }
}
