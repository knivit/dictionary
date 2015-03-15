package com.tsoft.dictionary.server.app.service.model;

import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;

@Entity
@MappedSuperclass
public abstract class UserPO<T> extends IdPO<T> {
    private String userId;

    public UserPO() { }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
