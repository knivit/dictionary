package com.tsoft.dictionary.client.server.cache;

import com.tsoft.dictionary.server.app.web.model.HasSize;

public class CacheItem {
    private HasId id;
    private HasSize obj;
    private int hitCount;
    private int size;

    public CacheItem(HasId id, HasSize obj) {
        this.id = id;
        this.obj = obj;
        this.size = obj.getItemSize();
    }

    public HasId getId() {
        return id;
    }

    public HasSize getObj() {
        return obj;
    }

    public int getSize() {
        return size;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void incHitCount() {
        hitCount ++;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CacheItem other = (CacheItem) obj;
        if (this.id != other.id && (this.id == null || !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}