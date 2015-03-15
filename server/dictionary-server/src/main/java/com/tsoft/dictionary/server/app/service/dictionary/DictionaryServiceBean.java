package com.tsoft.dictionary.server.app.service.dictionary;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.Inject;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class DictionaryServiceBean implements DictionaryService {
    private static final Logger logger = Logger.getLogger(DictionaryServiceBean.class.getName());

    @Inject
    public EntityManager em;

    public DictionaryServiceBean() { }

    @Override
    public DictionaryInfoTO getDictionaryInfo(UserInfo userInfo) {
        DictionaryInfoPO po = findDictionaryInfo(userInfo);
        if (po == null) {
            po = DictionaryInfoPO.createDefault(userInfo);
        }

        DictionaryInfoTO to = new DictionaryInfoTO();
        po.setTOValues(to);
        return to;
    }

    @Override
    public void setDictionaryInfo(UserInfo userInfo, DictionaryInfoTO to) {
        DictionaryInfoPO persistedPo = findDictionaryInfo(userInfo);
        if (persistedPo == null) {
            DictionaryInfoPO po = new DictionaryInfoPO();
            po.setUserId(userInfo.getId());
            po.getTOValues(to);
            em.persist(po);
        } else {
            persistedPo.getTOValues(to);
        }
    }

    public DictionaryInfoPO findDictionaryInfo(UserInfo userInfo) {
        Query query = em.createNamedQuery("DictionaryInfoPO.findByUser");
        query.setParameter("userId", userInfo.getId());

        DictionaryInfoPO po = null;
        try {
            po = (DictionaryInfoPO) query.getSingleResult();
        } catch (NoResultException ex) { }

        return po;
    }
}
