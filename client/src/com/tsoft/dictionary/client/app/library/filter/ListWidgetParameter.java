package com.tsoft.dictionary.client.app.library.filter;

import com.google.gwt.user.client.ui.ListBox;
import com.tsoft.dictionary.client.widget.ListBoxHelper;
import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO.ListParameter;

public class ListWidgetParameter extends WidgetParameter<ListParameter, ListBox> {
    public ListWidgetParameter(ListParameter parameter) {
        super(parameter);

        widget = new ListBox();
        ListBoxHelper.populateItems(widget, parameter.getValues(), parameter.getSelectedIndex(), null);
    }

    @Override
    public String getValue() {
        return ListBoxHelper.getText(widget);
    }
}
