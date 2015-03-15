package com.tsoft.ui.binder;

public class Event extends ActionContainer {
    private String method;

    public Event() {
    }

    public String getMethod() {
        return method;
    }

    void setMethod( String method ) {
        this.method = method;
    }
}
