package com.tsoft.dictionary.server.app.service.dictionary;

import com.tsoft.dictionary.server.app.UserInfo;

public interface DictionaryService {
    public DictionaryInfoTO getDictionaryInfo(UserInfo userInfo);

    public void setDictionaryInfo(UserInfo userInfo, DictionaryInfoTO to);
}
