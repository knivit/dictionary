package com.tsoft.util.sax;

public interface ElementListener {
    public void afterPushElement(String name, ElementStack stack);

    public void afterPopElement(String name, ElementStack stack);

    public void processValue(String value, ElementStack stack);
}
