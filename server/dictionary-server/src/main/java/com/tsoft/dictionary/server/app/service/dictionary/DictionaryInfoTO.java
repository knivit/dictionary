package com.tsoft.dictionary.server.app.service.dictionary;

import java.io.Serializable;

public class DictionaryInfoTO implements Serializable {
    private String userId;
    private String dictionaryName;

    public DictionaryInfoTO() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    @Override
    public String toString() {
        return DictionaryInfoTO.class.getName() +
            "[userId=" + userId + ", dictionaryName=" + dictionaryName + "]";
    }
}
