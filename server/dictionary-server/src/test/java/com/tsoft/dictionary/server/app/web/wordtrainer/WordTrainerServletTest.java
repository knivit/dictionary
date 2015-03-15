package com.tsoft.dictionary.server.app.web.wordtrainer;

import com.tsoft.dictionary.server.app.factory.WordTrainerTestFactory;
import com.tsoft.dictionary.server.app.Inject;
import com.tsoft.dictionary.server.app.service.wordtrainer.WordTrainerInfoTO;
import com.tsoft.dictionary.server.app.web.AbstractServletTest;
import com.tsoft.dictionary.server.app.web.TestHelper;
import com.tsoft.dictionary.server.app.web.model.ListResponseTO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class WordTrainerServletTest extends AbstractServletTest {
    @Inject
    public WordTrainerServletMock wordTrainerServlet;

    @Test
    public void getDictionaryList_Default() throws Exception {
        ListResponseTO responseTO = wordTrainerServlet.getDictionaryList(TestHelper.getDefaultUserInfo());

        assertEquals(0, responseTO.getSelectedIndex());
        assertEquals(2, responseTO.getItemList().size());
        assertEquals(TestHelper.DICT01_NAME, responseTO.getItemList().get(0));
        assertEquals(TestHelper.DICT02_NAME, responseTO.getItemList().get(1));
    }

    @Test
    public void getDictionaryList() throws Exception {
        WordTrainerTestFactory tf = new WordTrainerTestFactory(TestHelper.getDefaultUserInfo());
        tf.createPersistedWordTrainerInfo();

        ListResponseTO responseTO = wordTrainerServlet.getDictionaryList(TestHelper.getDefaultUserInfo());

        assertEquals(1, responseTO.getSelectedIndex());
        assertEquals(2, responseTO.getItemList().size());
        assertEquals(TestHelper.DICT01_NAME, responseTO.getItemList().get(0));
        assertEquals(TestHelper.DICT02_NAME, responseTO.getItemList().get(1));
    }

    @Test
    public void generateLesson() throws Exception {
        final int HINT_COUNT = 2;

        // current state
        WordTrainerTestFactory tf = new WordTrainerTestFactory(TestHelper.getDefaultUserInfo());
        WordTrainerInfoTO to = tf.createWordTrainerInfoTO();
        to.setHintCount(HINT_COUNT);
        tf.createPersistedWordTrainerInfo(to);

        // generate a lesson
        LessonResponseTO responseTO = wordTrainerServlet.generateLesson(TestHelper.getDefaultUserInfo());

        assertNotNull(responseTO.getWord());
        assertNotNull(responseTO.getTranslatedWord());
        assertEquals(HINT_COUNT, responseTO.getHintList().size());
        assertNotNull(responseTO.getHintList().get(0));
    }

    @Test
    public void saveSettings() {
        SettingsRequestTO requestTO = new SettingsRequestTO();
        requestTO.setDictionaryName(TestHelper.DICT01_NAME);
        requestTO.setHintCount(3);
        requestTO.setNeedReverse(true);

        wordTrainerServlet.saveSettings(TestHelper.getDefaultUserInfo(), requestTO);

        SettingsResponseTO responseTO = wordTrainerServlet.loadSettings(TestHelper.getDefaultUserInfo());

        assertEquals(TestHelper.DICT01_NAME, responseTO.getDictionaryName());
        assertEquals(3, responseTO.getHintCount());
        assertEquals(true, responseTO.isNeedReverse());
    }
}
