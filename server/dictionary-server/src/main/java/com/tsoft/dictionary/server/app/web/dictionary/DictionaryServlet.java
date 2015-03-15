package com.tsoft.dictionary.server.app.web.dictionary;

import com.tsoft.dictionary.server.app.UserInfo;
import com.tsoft.dictionary.api.Base;
import com.tsoft.dictionary.server.app.web.dictionary.base.BaseHelper;
import com.tsoft.dictionary.api.BaseRecord;
import com.tsoft.dictionary.server.app.service.dictionary.DictionaryInfoTO;
import com.tsoft.dictionary.server.app.service.dictionary.DictionaryService;
import com.tsoft.dictionary.server.app.web.ServerServlet;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import java.util.ArrayList;
import java.util.logging.Logger;

public class DictionaryServlet extends ServerServlet implements DictionaryServletInterface {
    private static final Logger logger = Logger.getLogger(DictionaryServlet.class.getName());

    @Override
    public ListResponseTO getDictionaryList(UserInfo userInfo) {
        ArrayList<String> itemList = BaseHelper.getDictionaryList(getServletContext());

        String userSelection = getSelectedDictionary(userInfo);
        ListResponseTO listBoxTO = new ListResponseTO(itemList, userSelection);
        return listBoxTO;
    }

    private String getSelectedDictionary(UserInfo userInfo) {
        DictionaryInfoTO to = getDictionaryInfo(userInfo);
        return to.getDictionaryName();
    }

    @Override
    public TranslationResponseTO getTranslation(UserInfo userInfo, TranslationRequestTO requestTO) {
        TranslationResponseTO responseTO = new TranslationResponseTO();
        Base base = BaseHelper.getBase(getServletContext(), requestTO.getDictionaryName());
        BaseRecord record = base.getRecord(requestTO.getValue());
        responseTO.setValue(record.getValue());

        setDictionaryInfo(userInfo, requestTO);
        return responseTO;
    }

    private DictionaryInfoTO getDictionaryInfo(UserInfo userInfo) {
        DictionaryService dictionaryService = lookup(DictionaryService.class);
        return dictionaryService.getDictionaryInfo(userInfo);
    }

    private void setDictionaryInfo(UserInfo userInfo, TranslationRequestTO requestTO) {
        DictionaryInfoTO to = new DictionaryInfoTO();
        to.setUserId(userInfo.getId());
        to.setDictionaryName(requestTO.getDictionaryName());

        DictionaryService dictionaryService = lookup(DictionaryService.class);
        dictionaryService.setDictionaryInfo(userInfo, to);
    }
}
