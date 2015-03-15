package com.tsoft.dictionary.server.app.web.login;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("login")
public interface LoginServletInterface extends RemoteService {
    public UserInfoResponseTO getUserInfo(UserInfo userInfo);
}
