package com.tsoft.dictionary.server.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class StringHelperTest {
    @Test
    public void getPatternValue() {
        // soft match
        assertEquals("andy%", StringHelper.getPatternValue("andy"));
        assertEquals("andy%", StringHelper.getPatternValue(" AnDy   "));

        // strict match
        assertEquals("andy%", StringHelper.getPatternValue("  \"andy\"   "));
        assertEquals("An dY%", StringHelper.getPatternValue("  \"An dY\"   "));

        // both
        assertEquals("andy%an dy%", StringHelper.getPatternValue(" andy   \"an dy\"   "));
        assertEquals("an%dy%andy%an%dy%", StringHelper.getPatternValue(" an dy\"andy\"an dy   "));

        // bound cases
        assertEquals("%", StringHelper.getPatternValue(null));
        assertEquals("%", StringHelper.getPatternValue(""));
        assertEquals("%", StringHelper.getPatternValue("   "));
        assertEquals("%", StringHelper.getPatternValue(" \"\"  "));
    }
}
