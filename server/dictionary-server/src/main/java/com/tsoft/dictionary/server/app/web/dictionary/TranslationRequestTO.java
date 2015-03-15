package com.tsoft.dictionary.server.app.web.dictionary;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class TranslationRequestTO implements IsSerializable {
    private String dictionaryName;
    private String value;

    public TranslationRequestTO() { }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
