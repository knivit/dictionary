package com.tsoft.ui.binder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Listener {
    private String component;
    private String type;
    private List<Event> events = new ArrayList<Event>();

    public Listener() {
    }

    public String getComponent() {
        return component;
    }

    public String getType() {
        return type;
    }

    public List<Event> getEvents() {
        return Collections.unmodifiableList(events);
    }

    void setComponent(String component) {
        this.component = component;
    }

    void setType(String type) {
        this.type = type;
    }

    void addEvent(Event event) {
        events.add(event);
    }

    void removeEvent(Event event) {
        events.remove(event);
    }
}
