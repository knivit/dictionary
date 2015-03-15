package com.tsoft.dictionary.client.widget;

import com.google.gwt.user.client.ui.Widget;

public final class StyleHelper {
    private StyleHelper() { }

    public static void setBackground(Widget widget, String color) {
        widget.getElement().getStyle().setProperty("background", color);
    }

    public static void setHeight(Widget widget, int height) {
        setHeight(widget, Integer.toString(height) + "px");
    }

    public static void setHeight(Widget widget, String height) {
        widget.getElement().getStyle().setProperty("height", height);
    }

    public static void setMaxHeight(Widget widget) {
        setHeight(widget, "100%");
    }

    public static void setWidth(Widget widget, int width) {
        setWidth(widget, Integer.toString(width) + "px");
    }

    public static void setWidth(Widget widget, String width) {
        widget.getElement().getStyle().setProperty("width", width);
    }

    public static void setMaxWidth(Widget widget) {
        setWidth(widget, "100%");
    }

    public static void setSize(Widget widget, int height, int width) {
        setHeight(widget, height);
        setWidth(widget, width);
    }

    public static void setSize(Widget widget, String height, String width) {
        setHeight(widget, height);
        setWidth(widget, width);
    }

    public static void setMaxSize(Widget widget) {
        setMaxHeight(widget);
        setMaxWidth(widget);
    }
}
