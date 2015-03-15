package com.tsoft.dictionary.client.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.tsoft.dictionary.client.util.Logger;
import com.tsoft.dictionary.server.util.StringHelper;

public class HtmlPage extends HTML {
    private static final Logger logger = new Logger("com.tsoft.dictionary.client.widget.HtmlPage");

    private HandlerManager handlerManager;

    private static final String NOT_A_LINK = "[object]";
    private static final String IE_ANCHOR_START = "<a ";
    private static final String IE_ANCHOR_END = "/a>";

    public HtmlPage() {
        super();
        
        super.addClickHandler(new LinkClickHandler());
    }

    public void setContent(String content) {
        if (content == null) {
            setContentIsUnavailable();
        } else {
            setHTML(content);
        }
    }

    public void setContentIsUnavailable() {
        setContent("<br><p>Content is unavailable</p>");
    }

    private class LinkClickHandler implements ClickHandler {
        @Override
        public void onClick(ClickEvent event) {
            String host = GWT.getHostPageBaseURL();
            logger.debug("onClick", "host=" + host);

            String href = event.getNativeEvent().getEventTarget().toString();
            if (!NOT_A_LINK.equals(href) && href.length() > host.length()) {
                boolean isIE = href.toLowerCase().startsWith(IE_ANCHOR_START) && href.toLowerCase().endsWith(IE_ANCHOR_END);
                if (isIE) {
                    href = StringHelper.getHref(href);
                }
                String arg = href.substring(host.length());

                // if that is not a local ref
                if (href.indexOf('#') == -1) {
                    handlerManager.fireEvent(new HtmlPageClickEvent(arg));
                    event.preventDefault();
                }
            }
        }
    }

    public HandlerRegistration addPageClickHandler(HtmlPageClickHandler handler) {
        return addPageHandler(handler, HtmlPageClickEvent.getType());
    }

    private <H extends EventHandler> HandlerRegistration addPageHandler(final HtmlPageClickHandler handler, GwtEvent.Type<HtmlPageClickHandler> type) {
        return ensurePageHandlers().addHandler(type, handler);
    }

    private HandlerManager ensurePageHandlers() {
        return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
    }
}
