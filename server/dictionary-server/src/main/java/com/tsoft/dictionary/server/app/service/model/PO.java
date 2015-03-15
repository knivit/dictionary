package com.tsoft.dictionary.server.app.service.model;

import com.tsoft.dictionary.server.app.service.ServerServices;
import java.io.Serializable;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class PO<T> implements Serializable {
    public abstract void getTOValues(T to);

    public abstract void setTOValues(T to);

    public PO() { }

    protected <T> T lookup(Class<T> serviceClass) {
        T service = ServerServices.get(serviceClass);
        if (service == null) {
            throw new IllegalStateException();
        }

        return service;
    }
}
