package com.tsoft.dictionary.server.app.web.library;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;
import java.util.HashMap;
import java.util.Map;

@GWTClient
public class BooksFilterRequestTO implements IsSerializable {
    private Map<String, String> parameters = new HashMap<String, String>();

    public BooksFilterRequestTO() { }

    public void add(String name, String value) {
        parameters.put(name, value);
    }

    public String get(String name) {
        return parameters.get(name);
    }
}
