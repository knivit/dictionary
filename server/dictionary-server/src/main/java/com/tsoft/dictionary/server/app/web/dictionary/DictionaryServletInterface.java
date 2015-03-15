package com.tsoft.dictionary.server.app.web.dictionary;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import com.tsoft.dictionary.server.app.GWTClient;

@GWTClient
@RemoteServiceRelativePath("dictionary")
public interface DictionaryServletInterface extends RemoteService {
    public ListResponseTO getDictionaryList(UserInfo userName);

    public TranslationResponseTO getTranslation(UserInfo userInfo, TranslationRequestTO requestTO);
}
