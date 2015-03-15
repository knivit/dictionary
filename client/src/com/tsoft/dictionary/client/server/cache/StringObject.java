package com.tsoft.dictionary.client.server.cache;

import com.tsoft.dictionary.server.app.web.model.HasSize;

public class StringObject implements HasSize {
    private String obj;

    public StringObject(String obj) {
        if (obj == null) {
            throw new IllegalArgumentException("Obj must be not null");
        }
        this.obj = obj;
    }

    @Override
    public int getItemSize() {
        return obj == null ? 0 : obj.length();
    }
}
