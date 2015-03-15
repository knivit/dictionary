package com.tsoft.dictionary.server.app.service.wordtrainer;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.server.app.Inject;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

public class WordTrainerServiceBean implements WordTrainerService {
    private static final Logger logger = Logger.getLogger(WordTrainerServiceBean.class.getName());

    @Inject
    public EntityManager em;

    public WordTrainerServiceBean() { }

    @Override
    public WordTrainerInfoTO getWordTrainerInfo(UserInfo userInfo) {
        WordTrainerInfoPO po = findWordTrainerInfo(userInfo);
        if (po == null) {
            po = WordTrainerInfoPO.createDefault(userInfo);
        }

        WordTrainerInfoTO to = new WordTrainerInfoTO();
        po.setTOValues(to);
        return to;
    }

    @Override
    public void setWordTrainerInfo(UserInfo userInfo, WordTrainerInfoTO to) {
        WordTrainerInfoPO persistedPo = findWordTrainerInfo(userInfo);
        if (persistedPo == null) {
            WordTrainerInfoPO po = new WordTrainerInfoPO();
            po.setUserId(userInfo.getId());
            po.getTOValues(to);
            em.persist(po);
        } else {
            persistedPo.getTOValues(to);
        }
    }

    @Override
    public WordTrainerInfoPO findWordTrainerInfo(UserInfo userInfo) {
        Query query = em.createNamedQuery("WordTrainerInfoPO.findByUser");
        query.setParameter("userId", userInfo.getId());

        WordTrainerInfoPO po = null;
        try {
            po = (WordTrainerInfoPO) query.getSingleResult();
        } catch (NoResultException ex) { }

        return po;
    }
}
