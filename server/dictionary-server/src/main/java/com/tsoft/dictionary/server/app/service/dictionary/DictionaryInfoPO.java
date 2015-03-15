package com.tsoft.dictionary.server.app.service.dictionary;

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
    @NamedQuery(name = "DictionaryInfoPO.findByUser",
        query =
            "SELECT i FROM com.tsoft.dictionary.server.app.service.dictionary.DictionaryInfoPO i " +
            "WHERE i.userId = :userId")
})
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class DictionaryInfoPO extends UserPO<DictionaryInfoTO> {
    public static final String DEFAULT_DICTIONARY_NAME = "9500giant";

    private String dictionaryName;

    private long registerDateGMT;

    public DictionaryInfoPO() {
        registerDateGMT = DateHelper.getCurrentDateGMTAsLong();
    }

    public static DictionaryInfoPO createDefault(UserInfo userInfo) {
        DictionaryInfoPO po = new DictionaryInfoPO();
        po.setUserId(userInfo.getId());
        po.setDictionaryName(DEFAULT_DICTIONARY_NAME);

        return po;
    }

    @Override
    public void getTOValues(DictionaryInfoTO to) {
        setDictionaryName(to.getDictionaryName());
    }

    @Override
    public void setTOValues(DictionaryInfoTO to) {
        to.setUserId(getUserId());
        to.setDictionaryName(getDictionaryName());
    }

    public String getDictionaryName() {
        return dictionaryName;
    }

    public void setDictionaryName(String dictionaryName) {
        this.dictionaryName = dictionaryName;
    }

    public long getRegisterDateGMT() {
        return registerDateGMT;
    }

    @Override
    public String toString() {
        return getClass().getName() +
            "[id=" + getId() +
            ", userId=" + getUserId() +
            ", registerDateGMT=" + DateHelper.dateGMTAsString(getRegisterDateGMT()) +
            "]";
    }
}
