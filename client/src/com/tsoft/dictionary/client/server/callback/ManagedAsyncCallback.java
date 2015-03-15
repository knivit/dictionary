package com.tsoft.dictionary.client.server.callback;

import com.tsoft.dictionary.client.server.IsManaged;

public abstract class ManagedAsyncCallback<RequestT, ResponseT> extends DefaultAsyncCallback<RequestT, ResponseT> {
    private IsManaged managedServlet;

    public ManagedAsyncCallback(IsManaged managedServlet, RequestT requestTO) {
        super(requestTO);
        this.managedServlet = managedServlet;
    }

    @Override
    public void afterManagedRemoteCall(ResponseT responseTO, boolean isSuccess) {
        managedServlet.afterRemoteCall(responseTO, isSuccess);
    }
}
