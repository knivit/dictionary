package com.tsoft.dictionary.server.app.service.wordtrainer;

import com.tsoft.dictionary.server.app.UserInfo;

public interface WordTrainerService {
    public WordTrainerInfoTO getWordTrainerInfo(UserInfo userInfo);

    public void setWordTrainerInfo(UserInfo userInfo, WordTrainerInfoTO to);

    public WordTrainerInfoPO findWordTrainerInfo(UserInfo userInfo);
}
