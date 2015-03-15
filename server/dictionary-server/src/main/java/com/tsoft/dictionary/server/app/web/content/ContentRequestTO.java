package com.tsoft.dictionary.server.app.web.content;

import com.google.gwt.user.client.rpc.IsSerializable;

import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class ContentRequestTO implements IsSerializable {
    private String contentName;
    private String pageName;

    public ContentRequestTO() { }

    public String getContentName() {
        return contentName;
    }

    public void setContentName(String contentName) {
        this.contentName = contentName;
    }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }
}
