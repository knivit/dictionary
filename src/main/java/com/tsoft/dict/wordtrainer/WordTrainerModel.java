package com.tsoft.dict.wordtrainer;

import com.tsoft.dict.AppConfig;
import com.tsoft.mvc.Model;
import com.tsoft.dict.base.Base;
import com.tsoft.dict.base.BaseFactory;
import com.tsoft.config.ConfigSection;
import com.tsoft.dict.base.BaseWord;
import com.tsoft.dict.base.Stat;
import java.io.File;

public class WordTrainerModel extends Model {
    private Base base;

    @Override
    public void init() throws Exception { }

    @Override
    public WordTrainerController getController() {
        return (WordTrainerController)controller;
    }

    @Override
    public void close() {
        if (getBase() != null) {
            getBase().close();
            setBase(null);
        }
    }

    private Base getBase() {
        return base;
    }
    
    private void setBase(Base base) {
        this.base = base;
    }

    private Base getCurrentBase() throws Exception {
        if (getBase() == null) {
            ConfigSection section = getController().getConfig().getSection(AppConfig.WORD_TRAINER_SECTION);
            String baseDir = section.getString(AppConfig.WORD_TRAINER_DICTIONARY_DIR);
            String fileName = section.getString(AppConfig.WORD_TRAINER_FILE);
            String baseFileName = baseDir + File.separator + fileName;

            setBase(BaseFactory.getBase(baseFileName));
        }

        return getBase();
    }

    public BaseWord getRandomWord() throws Exception {
        return getCurrentBase().getRandomWord();
    }

    public Stat getStat(BaseWord word) throws Exception {
        return getCurrentBase().getStat(word);
    }
}
