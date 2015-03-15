package com.tsoft.dict.model.app;

import org.junit.Test;
import org.junit.Before;

public class DSLModelTest extends ModelTest {
    @Before
    @Override
    public void setUp() throws Exception {
        setUp("base/Ru-En-Smirnitsky.dsl");
    }

    @Test
    public void testModel01() {
        check("абажур", "lampshade, shade");
    }

    @Test
    public void testModel02() {
        check("авангардный", "vanguard");
    }

    @Test
    public void testModel03() {
        check("автопокрышка", "tire-cover, (outer) tire");
    }

    @Test
    public void testModel04() {
        check("присмотреть(ся)", "");
    }

    @Test
    public void testModel05() {
        check("нуждаться", "need, want, require; stand* in need (of)");
    }
}
