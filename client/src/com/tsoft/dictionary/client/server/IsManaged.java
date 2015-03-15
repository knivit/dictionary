package com.tsoft.dictionary.client.server;

public interface IsManaged<ResponseT> {
    public void afterRemoteCall(ResponseT responseTO, boolean isSuccess);
}
