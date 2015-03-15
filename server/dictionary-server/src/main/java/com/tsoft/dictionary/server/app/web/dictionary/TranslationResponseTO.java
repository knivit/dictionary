package com.tsoft.dictionary.server.app.web.dictionary;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class TranslationResponseTO implements IsSerializable {
    private String value;

    public TranslationResponseTO() { }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
