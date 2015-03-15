package com.tsoft.dictionary.client.server.callback;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public abstract class DefaultAsyncCallback<RequestT, ResponseT> implements AsyncCallback<ResponseT> {
    public abstract void beforeRemoteCall(RequestT requestTO);

    public abstract void afterRemoteCall(ResponseT responseTO, boolean isSuccess);

    public DefaultAsyncCallback(RequestT requestTO) {
        beforeRemoteCall(requestTO);
    }

    public void afterManagedRemoteCall(ResponseT responseTO, boolean isSuccess) { }

    @Override
    public void onSuccess(ResponseT responseTO) {
        afterManagedRemoteCall(responseTO, true);
        afterRemoteCall(responseTO, true);
    }

    @Override
    public void onFailure(Throwable caught) {
        afterManagedRemoteCall(null, false);
        afterRemoteCall(null, false);

        processFailure(caught);
    }

    public void processFailure(Throwable caught) {
        Window.alert("An error during of remote call is occured");

        throw new IllegalStateException();
    }
}
