package com.tsoft.dict.model.app;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class TXTModelTest extends ModelTest {
    @Before
    @Override
    public void setUp() throws Exception {
        setUp("base/RuEn/15000words.txt");
    }

    @Test
    public void testModel01() throws ClassNotFoundException, InstantiationException, IllegalAccessException, FileNotFoundException, IOException {
        check("крик", "cry, shouting");
    }
}
