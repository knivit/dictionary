package com.tsoft.dictionary.server.app.service.wordtrainer;

import java.io.Serializable;

public class WordTrainerInfoTO implements Serializable {
    private String userId;
    private String dictionaryName;
    private int hintCount;
    private boolean needReverse;

    public WordTrainerInfoTO() { }

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

    @Override
    public String toString() {
        return getClass().getName() +
            "[userId=" + userId + ", dictionaryName=" + dictionaryName +
            ", hintCount=" + hintCount + ", needReverse=" + needReverse + "]";
    }
}
