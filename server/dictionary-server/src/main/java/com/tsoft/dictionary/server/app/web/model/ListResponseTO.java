package com.tsoft.dictionary.server.app.web.model;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
public class ListResponseTO implements IsSerializable {
    private ArrayList<String> itemList = new ArrayList<String>();
    private int selectedIndex;

    public ListResponseTO() { }

    public ListResponseTO(ArrayList<String> itemList, String selectedValue) {
        setItemList(itemList);

        selectedIndex = 0;
        if (selectedValue != null) {
            selectedIndex = itemList.indexOf(selectedValue);
            if (selectedIndex == -1) {
                selectedIndex = 0;
            }
        }
    }

    public ArrayList<String> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<String> itemList) {
        this.itemList.clear();
        this.itemList.addAll(itemList);
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
