package com.tsoft.dictionary.client.widget;

import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.ListBox;
import java.util.List;

public final class ListBoxHelper {
    public ListBoxHelper() { }

    public static String getText(ListBox listBox) {
        int selectedIndex = listBox.getSelectedIndex();
        if (selectedIndex != -1) {
            return listBox.getItemText(selectedIndex);
        }
        return null;
    }

    public static void populateItems(ListBox listBox, List<String> itemList, int selectedIndex, ChangeHandler changeHandler) {
        boolean isContentChanged = itemList.size() != listBox.getItemCount();
        if (!isContentChanged) {
            for (int i = 0; i < itemList.size(); i++) {
                if (!listBox.getItemText(i).equals(itemList.get(i))) {
                    isContentChanged = true;
                    break;
                }
            }
        }

        if (isContentChanged) {
            listBox.clear();
            for (String item : itemList) {
                listBox.addItem(item);
            }
        }

        boolean isSelectionChanged = (listBox.getSelectedIndex() != selectedIndex) || isContentChanged;
        if (isSelectionChanged) {
            listBox.setSelectedIndex(selectedIndex);
            if (changeHandler != null) {
                changeHandler.onChange(null);
            }
        }
    }
}
