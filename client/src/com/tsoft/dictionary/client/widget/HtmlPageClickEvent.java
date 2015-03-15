package com.tsoft.dictionary.client.widget;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

public class HtmlPageClickEvent extends GwtEvent<HtmlPageClickHandler> {
    private static final Type<HtmlPageClickHandler> TYPE =
            new Type<HtmlPageClickHandler>("click", new HtmlPageClickEvent());

    private String arg;

    public static class Type<H extends EventHandler> extends GwtEvent.Type<H> {
        private Type(String string, HtmlPageClickEvent pageRendererClickEvent) {
        }
    }

    public HtmlPageClickEvent() {
    }

    public HtmlPageClickEvent(String arg) {
        this.arg = arg;
    }

    public String getArg() {
        return arg;
    }

    public static Type<HtmlPageClickHandler> getType() {
        return TYPE;
    }

    @Override
    public Type<HtmlPageClickHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(HtmlPageClickHandler handler) {
        handler.onClick(this);
    }
}
