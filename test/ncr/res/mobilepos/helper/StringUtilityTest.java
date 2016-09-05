package ncr.res.mobilepos.helper;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class StringUtilityTest {
    @Test
    public void isNullOrEmptyOneArg() throws Exception {

        // only one null as Object.
        assertTrue(StringUtility.isNullOrEmpty((Object)null));

        // only one null as String.
        assertTrue(StringUtility.isNullOrEmpty((String)null));

        // only one empty as Object.
        assertTrue(StringUtility.isNullOrEmpty((Object)""));

        // only one empty as String.
        assertTrue(StringUtility.isNullOrEmpty(""));

        // one arg non-empty as String.
        assertFalse(StringUtility.isNullOrEmpty((String)"a"));

        // one arg non-empty as Object.
        assertFalse(StringUtility.isNullOrEmpty((Object)"a"));
    }

    @Test
    public void isNullOrEmptyMultipleArgs() throws Exception {
        // two with null, null
        assertTrue(StringUtility.isNullOrEmpty(null, null));

        // two with "", ""
        assertTrue(StringUtility.isNullOrEmpty("", ""));

        // two with "a", null as String[]
        assertTrue(StringUtility.isNullOrEmpty("a", (String[])null));

        // two with "a", null as String.
        assertTrue(StringUtility.isNullOrEmpty("a", (Object)null));

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
    }

}