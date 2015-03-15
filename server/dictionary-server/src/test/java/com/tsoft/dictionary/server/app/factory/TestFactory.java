package com.tsoft.dictionary.server.app.factory;

import com.tsoft.dictionary.server.app.UserInfo;

public abstract class TestFactory {
    private UserInfo userInfo;

    public TestFactory(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }
}
