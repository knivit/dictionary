package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class SettingsResponseTO implements IsSerializable {
    private String dictionaryName;
    private int hintCount;
    private boolean needReverse;

    public SettingsResponseTO() { }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public int getHintCount() {
        return hintCount;
    }

    public void setHintCount(int hintCount) {
        this.hintCount = hintCount;
    }

    public boolean isNeedReverse() {
        return needReverse;
    }

    public void setNeedReverse(boolean needReverse) {
        this.needReverse = needReverse;
    }
}
