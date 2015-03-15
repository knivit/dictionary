package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class BookIdRequestTO implements IsSerializable {
    private String id;

    public BookIdRequestTO() { }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
