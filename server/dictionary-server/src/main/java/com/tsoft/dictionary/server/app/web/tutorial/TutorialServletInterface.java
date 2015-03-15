package com.tsoft.dictionary.server.app.web.tutorial;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("tutorial")
public interface TutorialServletInterface extends RemoteService {
    public TutorialResponseTO getIndex(UserInfo userInfo);
}
