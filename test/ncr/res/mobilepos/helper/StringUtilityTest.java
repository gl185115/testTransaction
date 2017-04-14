package ncr.res.mobilepos.helper;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class StringUtilityTest {
    @Test
    public void isNullOrEmptyOneArg() throws Exception {
        // only one null as String.
        assertTrue(StringUtility.isNullOrEmpty((String)null));

        // only one empty as String.
        assertTrue(StringUtility.isNullOrEmpty(""));

        // one arg non-empty as String.
        assertFalse(StringUtility.isNullOrEmpty((String)"a"));
    }

    @Test
    public void isNullOrEmptyMultipleArgs() throws Exception {
        // two with null, null
        assertTrue(StringUtility.isNullOrEmpty(null, null));

        // two with "", ""
        assertTrue(StringUtility.isNullOrEmpty("", ""));

        // two with "a", null as String[]
        assertTrue(StringUtility.isNullOrEmpty("a", (String[])null));

        // three with "a", null, null.
        assertTrue(StringUtility.isNullOrEmpty("a", null, null));

        // two with null, "a"
        assertTrue(StringUtility.isNullOrEmpty(null, "a"));

        // two with "", null
        assertTrue(StringUtility.isNullOrEmpty("", null));

        // two with null, ""
        assertTrue(StringUtility.isNullOrEmpty(null, ""));

        // two with "a", "b"
        assertFalse(StringUtility.isNullOrEmpty("a", "b"));

        // two with "a", String[]{null}
        assertTrue(StringUtility.isNullOrEmpty("a", new String[] {null} ));

        // three with "a", String[]{"a", null}
        assertTrue(StringUtility.isNullOrEmpty("a", new String[] {null} ));

        // two with "a", String[]{"null"}
        assertFalse(StringUtility.isNullOrEmpty("a", new String[] {"a"} ));
    }

    @Test
    public void convEmptyStringToEmptyTest() {
        assertEquals("", StringUtility.convEmptyStringToEmpty("empty"));
        assertEquals("", StringUtility.convEmptyStringToEmpty("Empty"));
        assertEquals("", StringUtility.convEmptyStringToEmpty("empTy"));
        assertEquals("", StringUtility.convEmptyStringToEmpty(""));
        assertEquals(null, StringUtility.convEmptyStringToEmpty(null));
        assertFalse("".equals(StringUtility.convEmptyStringToEmpty("empty ")));
        assertFalse("".equals(StringUtility.convEmptyStringToEmpty(" empty")));
    }

    @Test
    public void convNullStringToStringTest() {
        assertNull(StringUtility.convNullStringToNull("null"));
        assertNull(StringUtility.convNullStringToNull("nUll"));
        assertNull(StringUtility.convNullStringToNull("Null"));
        assertNull(StringUtility.convNullStringToNull(null));
        assertNotNull(StringUtility.convNullStringToNull("null "));
        assertNotNull(StringUtility.convNullStringToNull(" null"));
        assertNotNull(StringUtility.convNullStringToNull(""));
    }

    @Test
    public void convNullOrEmptyStringTest() {
        assertNull(StringUtility.convNullOrEmptryString("null"));
        assertEquals("", StringUtility.convEmptyStringToEmpty("empty"));
    }

}