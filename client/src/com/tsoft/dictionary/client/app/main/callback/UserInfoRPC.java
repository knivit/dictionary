package com.tsoft.dictionary.client.app.main.callback;

import com.tsoft.dictionary.server.app.web.login.UserInfoResponseTO;

public interface UserInfoRPC {
    public void beforeUserInfoRPC(Void requestTO);

    public void afterUserInfoRPC(UserInfoResponseTO responseTO, boolean isSuccess);
}
