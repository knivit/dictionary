package com.tsoft.dictionary.api;

public class BaseRecord {
    public static final BaseRecord EMPTY = new BaseRecord();

    public String getValue() {
        return "<<< Not Found >>>";
    }
}
