package com.tsoft.dictionary.client.app.library.filter;

import com.google.gwt.user.client.ui.Widget;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.ListParameter;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.Parameter;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.StringParameter;

public abstract class WidgetParameter<P extends Parameter, W extends Widget> {
    private Parameter parameter;
    protected W widget;

    public abstract String getValue();

    public static WidgetParameter newInstance(Parameter parameter) {
        switch (parameter.getType()) {
            case STRING: {
                return new StringWidgetParameter((StringParameter)parameter);
            }

            case LIST: {
                return new ListWidgetParameter((ListParameter)parameter);
            }
        }

        throw new IllegalArgumentException("Unknown parameter type '" + parameter.getType().toString() + "'");
    }

    public WidgetParameter(P parameter) {
        this.parameter = parameter;
    }

    public String getParameterName() {
        return parameter.getName();
    }

    public W getWidget() {
        return widget;
    }
}
