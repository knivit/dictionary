package com.tsoft.dict.dict;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Model;
import com.tsoft.dict.base.Base;
import com.tsoft.dict.base.BaseFactory;
import com.tsoft.dict.base.BaseRecord;
import com.tsoft.config.ConfigSection;

public class DictModel extends Model {
    private Base base;

    @Override
    public void init() throws Exception {
        ConfigSection section = getController().getConfig().getSection(AppConfig.DICTIONARY_SECTION);
        String baseFileName = section.getString(AppConfig.DICTIONARY_FILE);

        base = BaseFactory.getBase(baseFileName);
    }

    @Override
    public DictController getController() {
        return (DictController)controller;
    }

    public BaseRecord getRecord(String word) {
        BaseRecord record = getBase().getRecord(word);
        return record;
    }

    @Override
    public void close() {
        getBase().close();
    }

    private Base getBase() {
        return base;
    }
}
