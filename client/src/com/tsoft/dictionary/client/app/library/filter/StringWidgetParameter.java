package com.tsoft.dictionary.client.app.library.filter;

import com.google.gwt.user.client.ui.TextBox;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.StringParameter;

public class StringWidgetParameter extends WidgetParameter<StringParameter, TextBox> {
    public StringWidgetParameter(StringParameter parameter) {
        super(parameter);
        widget = new TextBox();
    }

    @Override
    public String getValue() {
        return widget.getText();
    }
}
