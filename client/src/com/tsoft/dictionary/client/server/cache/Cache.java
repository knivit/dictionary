package com.tsoft.dictionary.client.server.cache;

import com.tsoft.dictionary.server.app.web.model.HasSize;

public interface Cache<T extends HasSize> {
    public boolean inCache(HasId id);

    public T get(HasId id);

    public void put(HasId id, T obj);

    public void evict(HasId id);
}
