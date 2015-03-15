package com.tsoft.util.sax;

public class AbstractElementListener implements ElementListener {
    @Override
    public void afterPushElement(String name, ElementStack stack) { }

    @Override
    public void afterPopElement(String name, ElementStack stack) { }

    @Override
    public void processValue(String value, ElementStack stack) { }
}
