package com.tsoft.dictionary.server.app.web.content;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("content")
public interface ContentServletInterface extends RemoteService {
    public ContentResponseTO getContent(UserInfo userName, ContentRequestTO requestTO);
}
