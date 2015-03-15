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
        check("������", "lampshade, shade");
    }

    @Test
    public void testModel02() {
        check("�����������", "vanguard");
    }

    @Test
    public void testModel03() {
        check("������������", "tire-cover, (outer) tire");
    }

    @Test
    public void testModel04() {
        check("�����������(��)", "");
    }

    @Test
    public void testModel05() {
        check("���������", "need, want, require; stand* in need (of)");
    }
}
