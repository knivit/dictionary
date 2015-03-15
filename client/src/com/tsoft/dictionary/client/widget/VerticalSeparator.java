package com.tsoft.dictionary.client.widget;

import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;

public class VerticalSeparator extends Widget {
    public VerticalSeparator() {
        setElement(Document.get().createDivElement());
        setStyleName("gwt-Separator");
        addStyleDependentName("spring");
    }

    public VerticalSeparator(int height) {
        this(Integer.toString(height) + "px");
    }

    public VerticalSeparator(String height) {
        setElement(Document.get().createDivElement());
        setStyleName("gwt-Separator");
        getElement().getStyle().setProperty("marginTop", height);
    }
}
