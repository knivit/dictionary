package com.tsoft.dictionary.client.server.cache;

import com.tsoft.dictionary.server.app.web.model.HasSize;
import com.tsoft.dictionary.client.util.Logger;

public class ContentCache<T extends HasSize> implements Cache<T> {
    private static final Logger logger = new Logger("com.tsoft.dictionary.client.server.cache.ContentCache");

    private CacheItemList itemList = new CacheItemList();
    private int sizeLimit;

    public ContentCache(int sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    @Override
    public boolean inCache(HasId id) {
        return itemList.get(id) != null;
    }

    @Override
    public T get(HasId id) {
        CacheItem item = itemList.get(id);
        if (item == null) {
            return null;
        }

        item.incHitCount();
        logger.debug("get", "Item '" + id.toString() + "', hit count=" + item.getHitCount());

        return (T)item.getObj();
    }

    @Override
    public synchronized void put(HasId id, T obj) {
        itemList.remove(id);

        int size = obj.getItemSize();
        int itemListSize = itemList.getSize();
        int newSize = itemListSize + size;
        if (newSize > sizeLimit) {
            int releaseSize = newSize - sizeLimit;

            // new object is too large
            if (releaseSize > sizeLimit) {
                return;
            }

            itemList.release(releaseSize);
        }

        CacheItem item = new CacheItem(id, obj);
        
        itemList.add(item);

        logger.debug("put", "Item='" + id.toString() + "', Item list size=" + itemList.getSize()/1024 + "Kb");
    }

    @Override
    public void evict(HasId id) {
        itemList.remove(id);
    }
}
