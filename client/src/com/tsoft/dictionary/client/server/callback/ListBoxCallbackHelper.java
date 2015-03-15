package com.tsoft.dictionary.client.server.callback;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import com.tsoft.dictionary.client.widget.ListBoxHelper;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import java.util.ArrayList;

public class ListBoxCallbackHelper {
    private ListBoxCallbackHelper() { }

    public static void beforeRPC(ListBox listBox, Void requestTO) {
        listBox.setEnabled(false);
    }

    public static void afterRPC(ListBox listBox, ListResponseTO responseTO, boolean isSuccess, ChangeHandler changeHandler) {
        if (isSuccess && responseTO  != null) {
            int index = responseTO.getSelectedIndex();
            ArrayList<String> itemList = responseTO.getItemList();
            ListBoxHelper.populateItems(listBox, itemList, index, changeHandler);
        } else {
            listBox.clear();
        }

        listBox.setEnabled(isSuccess);
    }
}
