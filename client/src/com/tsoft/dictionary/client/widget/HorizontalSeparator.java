package com.tsoft.dictionary.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalSeparator extends Widget {
    public HorizontalSeparator() {
        setElement(Document.get().createDivElement());
        setStyleName("gwt-Separator");
        addStyleDependentName("spring");
    }

    public HorizontalSeparator(int width) {
        this(Integer.toString(width) + "px");
    }

    public HorizontalSeparator(String width) {
        setElement(Document.get().createDivElement());
        setStyleName("gwt-Separator");
        getElement().getStyle().setProperty("marginLeft", width);
    }
}
