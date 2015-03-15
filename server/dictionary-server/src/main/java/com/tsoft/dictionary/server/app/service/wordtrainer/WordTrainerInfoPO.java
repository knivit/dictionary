package com.tsoft.dictionary.server.app.service.wordtrainer;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.service.model.UserPO;
import com.tsoft.dictionary.server.util.DateHelper;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

@Entity
@NamedQueries( {
    @NamedQuery(name = "WordTrainerInfoPO.findByUser",
        query =
            "SELECT i FROM com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerInfoPO i " +
            "WHERE i.userId = :userId")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class WordTrainerInfoPO extends UserPO<WordTrainerInfoTO> {
    public static final String DEFAULT_DICTIONARY_NAME = "1000first";
    public static final int DEFAULT_HINT_COUNT = 20;
    public static final boolean DEFAULT_NEED_REVERSE = true;

    private String dictionaryName;
    private int hintCount;
    private boolean needReverse;

    private long registerDateGMT;

    public WordTrainerInfoPO() {
        registerDateGMT = DateHelper.getCurrentDateGMTAsLong();
    }

    public static WordTrainerInfoPO createDefault(UserInfo userInfo) {
        WordTrainerInfoPO po = new WordTrainerInfoPO();
        po.setUserId(userInfo.getId());
        po.setDictionaryName(DEFAULT_DICTIONARY_NAME);
        po.setHintCount(DEFAULT_HINT_COUNT);
        po.setNeedReverse(DEFAULT_NEED_REVERSE);

        return po;
    }

    @Override
    public void getTOValues(WordTrainerInfoTO to) {
        setUserId(to.getUserId());
        setDictionaryName(to.getDictionaryName());
        setHintCount(to.getHintCount());
        setNeedReverse(to.isNeedReverse());
    }

    @Override
    public void setTOValues(WordTrainerInfoTO to) {
        to.setUserId(getUserId());
        to.setDictionaryName(getDictionaryName());
        to.setHintCount(getHintCount());
        to.setNeedReverse(isNeedReverse());
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public int getHintCount() {
        return hintCount;
    }

    public void setHintCount(int hintCount) {
        this.hintCount = hintCount;
    }

    public boolean isNeedReverse() {
        return needReverse;
    }

    public void setNeedReverse(boolean needReverse) {
        this.needReverse = needReverse;
    }

    public long getRegisterDateGMT() {
        return registerDateGMT;
    }

    @Override
    public String toString() {
        return WordTrainerInfoPO.class.getName() +
            "[id=" + getId() +
            ", userId=" + getUserId() +
            ", registerDateGMT=" + DateHelper.dateGMTAsString(getRegisterDateGMT()) +
            "]";
    }
}
