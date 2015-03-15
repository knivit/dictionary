package com.tsoft.dictionary.client.app.wordtrainer.callback;

import com.tsoft.dictionary.server.app.web.model.ListResponseTO;

public interface DictionaryListRPC {
    public void beforeDictionaryListRPC(Void requestTO);

    public void afterDictionaryListRPC(ListResponseTO responseTO, boolean isSuccess);
}
