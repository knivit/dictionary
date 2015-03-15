package com.tsoft.dict;

import com.tsoft.config.ConfigController;
import com.tsoft.config.SectionParam;
import java.io.Reader;

public class AppConfig extends ConfigController {
    public static final String CONFIG_FILE_NAME = "dict.ini";

    public static final String WORD_TRAINER_SECTION = "Word Trainer Section";
        public static final SectionParam WORD_TRAINER_REVERSE_DICTIONARY = new SectionParam("reverseDictionary", false);
        public static final SectionParam WORD_TRAINER_DICTIONARY_DIR = new SectionParam("dictionaryDir", "base/EnRu");
        public static final SectionParam WORD_TRAINER_FILE = new SectionParam("dictionaryFile", "1000first.txt");
        public static final SectionParam WORD_TRAINER_WORDS_ON_PAGE = new SectionParam("wordsOnPage", 20);

    public static final String DICTIONARY_SECTION = "Dictionary Section";
        public static final SectionParam DICTIONARY_FILE = new SectionParam("dictionaryFile", "base/EnRu/95000giant.txt");
        public static final SectionParam DICTIONARY_REVERSE_DICTIONARY = new SectionParam("reverseDictionary", false);

    public static final String SETTINGS_SECTION = "Settings Section";
        public static final SectionParam SETTINGS_ONE_TRAY_CLICK = new SectionParam("oneTrayClick", false);

    public AppConfig() {
        super(CONFIG_FILE_NAME);
    }

    public AppConfig(Reader reader) {
        super(reader);
    }
}
