package com.tsoft.dict.model.app;

import com.tsoft.dict.dict.DictModel;
import com.tsoft.dict.AppConfig;
import com.tsoft.config.ConfigController;
import java.io.StringReader;
import junit.framework.TestCase;
import org.junit.After;

public abstract class ModelTest extends TestCase {
    protected DictModel model;

    public void setUp(String baseFileName) throws Exception {
        ConfigController config = new AppConfig(new StringReader(
            "[" + AppConfig.DICTIONARY_SECTION + "]\n" +
            AppConfig.DICTIONARY_FILE.getName() + "=" + baseFileName
        ));

        model = new DictModel();
        model.init();
    }

    @After
    public void shutDown() throws Exception {
        model.close();
    }

    protected void check(String word, String value) {
        assertEquals(value, model.getRecord(word).getValue());
    }
}
