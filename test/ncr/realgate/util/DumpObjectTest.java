package ncr.realgate.util;

import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class DumpObjectTest  {
    static final String LF = System.getProperty("line.separator");

    @Test
    public void testPrimitive() throws Exception {
        StringWriter sw = new StringWriter();
        new DumpObject(new Integer(0)).write(sw);
        assertEquals("0", sw.toString());
        sw = new StringWriter();
        new DumpObject(new Double(0.1)).write(sw);
        assertEquals("0.1", sw.toString());
    }

    @Test
    public void testString() throws Exception {
        StringWriter sw = new StringWriter();
        new DumpObject("A").write(sw);
        assertEquals("\"A\"", sw.toString());
        StringBuffer b = new StringBuffer("B");
        sw = new StringWriter();
        new DumpObject(b).write(sw);
        assertEquals("\"B\"", sw.toString());
        CharSequence c = "C";
        sw = new StringWriter();
        new DumpObject(c).write(sw);
        assertEquals("\"C\"", sw.toString());
    }

    static class SimpleClass {
        String strValue = "string";
        int intValue = 32;
        byte[] b = new byte[] { 1, 2, 3, 4, 5 };
    }

    @Test
    public void testSimpleObject() throws Exception {
        StringWriter sw = new StringWriter();
        Object o = new SimpleClass();
        new DumpObject(o).write(sw);
        assertEquals(o + LF + "strValue=\"string\"" + LF +
                     "intValue=32" + LF + "b=[ 1, 2, 3, 4, 5, ]", sw.toString());
    }

    static class ToStringImplementedClass {
        String strValue = "string";
        int intValue = 32;
        byte[] b = new byte[] { 1, 2, 3, 4, 5 };
        public String toString() {
            return "this is an instance of ToStringImplementedClass";
        }
    }

    @Test
    public void testToStringImplementedClass() throws Exception {
        ToStringImplementedClass o = new ToStringImplementedClass();
        StringWriter sw = new StringWriter();
        new DumpObject(o).write(sw);
        assertEquals(o.toString(), sw.toString());
    }

    static class EmbeddedClass {
        String start = "start";
        private SimpleClass simple = new SimpleClass();
        String end = "end";
    }

    @Test
    public void testEmbeddedClass() throws Exception {
        EmbeddedClass e = new EmbeddedClass();
        String expected = e.toString() + LF + "start=\"start\"" + LF +
            "simple=" + e.simple.toString() + LF +
            "  strValue=\"string\"" + LF +
            "  intValue=32" + LF + "  b=[ 1, 2, 3, 4, 5, ]" + LF +
            "end=\"end\"";
        StringWriter sw = new StringWriter();
        new DumpObject(e).write(sw);
        assertEquals(expected, sw.toString());
    }

    static class LoopedClass {
        LoopedClass(boolean b) {
        }
        LoopedClass() {
            embedded = new LoopedClass(false);
            embedded.embedded = this;
        }
        private String start = "start";
        private LoopedClass embedded;
        private String end = "end";
    }

    @Test
    public void testLoopedClass() throws Exception {
        LoopedClass l = new LoopedClass();
        String expected = l.toString() + LF + "start=\"start\"" + LF +
            "embedded=" + l.embedded.toString() + LF +
            "  start=\"start\"" + LF +
            "  embedded=(" + l.toString() + ")" + LF +
            "  end=\"end\"" + LF +
            "end=\"end\"";
        StringWriter sw = new StringWriter();
        new DumpObject(l).write(sw);
        assertEquals(expected, sw.toString());
    }

//    public static void main(String[] args) {
//        junit.textui.TestRunner.run(DumpObjectTest.class);
//    }
}



