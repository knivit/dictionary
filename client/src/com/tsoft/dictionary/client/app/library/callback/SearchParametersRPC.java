package com.tsoft.dictionary.client.app.library.callback;

import com.tsoft.dictionary.server.app.web.library.BookParametersResponseTO;

public interface SearchParametersRPC {
    public void beforeSearchParameterListRPC(Void requestTO);

    public void afterSearchParameterListRPC(BookParametersResponseTO responseTO, boolean success);
}
