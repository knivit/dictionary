package com.tsoft.dictionary.client.server.cache;

import java.util.ArrayList;

public class CacheItemList {
    private ArrayList<CacheItem> itemList = new ArrayList<CacheItem>();

    public int getSize() {
        int size = 0;
        for (CacheItem item : itemList) {
            size += item.getSize();
        }
        return size;
    }

    public CacheItem get(HasId id) {
        for (CacheItem item : itemList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void add(CacheItem item) {
        itemList.add(item);
    }

    public void remove(HasId id) {
        CacheItem item = get(id);
        if (item != null) {
            itemList.remove(item);
        }
    }

    public void release(int size) {
        ArrayList<CacheItem> removeList = new ArrayList<CacheItem>();

        // find the objects for removal
        int currentSize = 0;
        for (CacheItem item : itemList) {
            currentSize += item.getSize();
            removeList.add(item);

            if (currentSize >= size) {
                break;
            }
        }

        // remove
        for (CacheItem item : removeList) {
            itemList.remove(item);
        }
    }
}
