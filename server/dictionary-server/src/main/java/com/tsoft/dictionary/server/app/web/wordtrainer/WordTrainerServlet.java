package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.api.Base;
import com.tsoft.dictionary.server.app.web.dictionary.base.BaseHelper;
import com.tsoft.dictionary.api.BaseWord;
import com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerInfoTO;
import com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerService;
import com.tsoft.dictionary.server.app.web.ServerServlet;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import java.util.ArrayList;
import java.util.logging.Logger;

public class WordTrainerServlet extends ServerServlet implements WordTrainerServletInterface {
    private static final Logger logger = Logger.getLogger(WordTrainerServlet.class.getName());

    @Override
    public ListResponseTO getDictionaryList(UserInfo userInfo) {
        ArrayList<String> itemList = BaseHelper.getDictionaryList(getServletContext());

        WordTrainerInfoTO to = getWordTrainerInfo(userInfo);
        ListResponseTO listBoxTO = new ListResponseTO(itemList, to.getDictionaryName());
        return listBoxTO;
    }

    @Override
    public LessonResponseTO generateLesson(UserInfo userInfo) {
        WordTrainerInfoTO to = getWordTrainerInfo(userInfo);
        Base base = BaseHelper.getBase(getServletContext(), to.getDictionaryName());
        BaseWord word = getRandomWord(base, to.isNeedReverse());

        int hintCount = to.getHintCount();
        ArrayList<String> hintList = new ArrayList<String>(hintCount);
        int correctPos = (int) (Math.random() * hintCount);
        for (int i = 0; i < hintCount; i++) {
            String variantTranslation;
            if (i == correctPos) {
                variantTranslation = word.getValue();
            } else {
                BaseWord variant = getRandomWord(base, to.isNeedReverse());
                variantTranslation = variant.getValue();
            }
            hintList.add(variantTranslation);
        }

        LessonResponseTO responseTO = new LessonResponseTO(to.getDictionaryName(), word.getWord(), word.getValue(), hintList);
        return responseTO;
    }

    private BaseWord getRandomWord(Base base, boolean needReverse) {
        BaseWord word = base.getRandomWord();

        if (needReverse) {
            String temp = word.getWord();
            word.setWord(word.getValue());
            word.setValue(temp);
        }
        return word;
    }

    @Override
    public SettingsResponseTO loadSettings(UserInfo userInfo) {
        WordTrainerInfoTO to = getWordTrainerInfo(userInfo);

        SettingsResponseTO responseTO = new SettingsResponseTO();
        responseTO.setDictionaryName(to.getDictionaryName());
        responseTO.setHintCount(to.getHintCount());
        responseTO.setNeedReverse(to.isNeedReverse());
        return responseTO;
    }

    @Override
    public void saveSettings(UserInfo userInfo, SettingsRequestTO requestTO) {
        WordTrainerInfoTO to = getWordTrainerInfo(userInfo);

        to.setDictionaryName(requestTO.getDictionaryName());
        to.setHintCount(requestTO.getHintCount());
        to.setNeedReverse(requestTO.isNeedReverse());

        WordTrainerService wordTrainerService = lookup(WordTrainerService.class);
        wordTrainerService.setWordTrainerInfo(userInfo, to);
    }

    private WordTrainerInfoTO getWordTrainerInfo(UserInfo userInfo) {
        WordTrainerService wordTrainerService = lookup(WordTrainerService.class);
        return wordTrainerService.getWordTrainerInfo(userInfo);
    }
}
