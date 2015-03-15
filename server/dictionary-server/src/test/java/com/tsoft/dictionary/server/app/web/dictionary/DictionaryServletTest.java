package com.tsoft.dictionary.server.app.web.dictionary;

import com.tsoft.dictionary.server.app.Inject;
import com.tsoft.dictionary.server.app.web.AbstractServletTest;
import com.tsoft.dictionary.server.app.web.TestHelper;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class DictionaryServletTest extends AbstractServletTest {
    @Inject
    public DictionaryServletMock dictionaryServlet;

    @Test
    public void getDictionaryList() {
        ListResponseTO responseTO = dictionaryServlet.getDictionaryList(TestHelper.getDefaultUserInfo());

        assertEquals(2, responseTO.getItemList().size());
        assertEquals(TestHelper.DICT01_NAME, responseTO.getItemList().get(0));
        assertEquals(TestHelper.DICT02_NAME, responseTO.getItemList().get(1));
    }

    @Test
    public void getTranslation() {
        TranslationRequestTO requestTO = new TranslationRequestTO();
        requestTO.setDictionaryName(TestHelper.DICT01_NAME);
        requestTO.setValue("fair");

        TranslationResponseTO responseTO = dictionaryServlet.getTranslation(TestHelper.getDefaultUserInfo(), requestTO);

//        assertEquals("�������, �������", responseTO.getValue());
    }
}
