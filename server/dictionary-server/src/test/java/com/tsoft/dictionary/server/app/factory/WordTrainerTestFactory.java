package com.tsoft.dictionary.server.app.factory;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerInfoPO;
import com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerInfoTO;
import com.tsoft.dictionary.server.app.web.TestHelper;

public class WordTrainerTestFactory extends TestFactory {
    public WordTrainerTestFactory(UserInfo userInfo) {
        super(userInfo);
    }

    public WordTrainerInfoTO createWordTrainerInfoTO() {
        WordTrainerInfoTO to = new WordTrainerInfoTO();
        to.setUserId(getUserInfo().getId());
        to.setDictionaryName(TestHelper.DICT02_NAME);
        to.setHintCount(2);
        to.setNeedReverse(true);
        return to;
    }

    public WordTrainerInfoPO createPersistedWordTrainerInfo(WordTrainerInfoTO to) throws Exception {
        WordTrainerInfoPO po = new WordTrainerInfoPO();
        po.getTOValues(to);
        DatastoreHelper.persist(po);
        return po;
    }

    public WordTrainerInfoPO createPersistedWordTrainerInfo() throws Exception {
        WordTrainerInfoTO to = createWordTrainerInfoTO();
        return createPersistedWordTrainerInfo(to);
    }
}
