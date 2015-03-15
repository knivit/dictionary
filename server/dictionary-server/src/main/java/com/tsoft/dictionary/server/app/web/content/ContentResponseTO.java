package com.tsoft.dictionary.server.app.web.content;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.web.model.HasSize;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class ContentResponseTO implements IsSerializable, HasSize {
    private String pageName;
    private String content;

    public ContentResponseTO() { }

    public String getPageName() {
        return pageName;
    }

    public void setPageName(String pageName) {
        this.pageName = pageName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemSize() {
        return content == null ? 0 : content.length();
    }
}
