package com.tsoft.dictionary.client.server.app;

import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.tsoft.dictionary.client.server.ServerServletProxy;
import com.tsoft.dictionary.client.server.ServerServlets;
import com.tsoft.dictionary.server.app.web.login.LoginServletInterfaceAsync;

public class LoginServletProxy extends ServerServletProxy {
    private LoginServletInterfaceAsync loginServletAsync;

    public LoginServletProxy(ServerServlets serverServlets, LoginServletInterfaceAsync loginServletAsync) {
        super(serverServlets);
        this.loginServletAsync = loginServletAsync;
    }

    public void getUserInfo(AsyncCallback<UserInfoResponseTO> callback) {
       loginServletAsync.getUserInfo(getServerServlets().getUserInfo(), callback);
    }
}