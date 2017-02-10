// Copyright(c) 2005 arton
//  redistributal under BSD license.
// Modified by NCR Japan.
//
// $Log: DumpObject.java,v $
// Revision 1.1  2005/06/10 06:32:56  art
// オブジェクトダンプユーティリティ
//
// $Id: DumpObject.java,v 1.1 2005/06/10 06:32:56 art Exp $
//
package ncr.realgate.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 指定されたオブジェクトのすべてのフィールドを文字列として出力するユーティリティ。
 *
 * @version $Revision: 1.1 $ $Date: 2005/06/10 06:32:56 $
 */
public class DumpObject {
    static final int INDENT_WIDTH = 2;
    static Method TOSTRING;
    static {
        try {
            TOSTRING = Object.class.getMethod("toString", new Class[0]);
        } catch (Exception e) {
        }
    }
    Object target;
    Map embeded;
    /**
     * ダンプ出力オブジェクトを生成する。
     * @param o ダンプ対象のオブジェクト
     */
    public DumpObject(Object o) {
        target = o;
    }
    /**
     * オブジェクトをダンプする。<br>
     * IOExceptionがスローされた場合は飲み込む。
     * @param writer 出力先
     */
    public void write(Writer writer) {
        embeded = new HashMap();
        try {
            PrintWriter p = new PrintWriter(writer);
            write(p, target, 0);
            p.flush();
        } catch (IOException e) {
        }
    }
    void write(PrintWriter w, Object o, int level) throws IOException {
        if (o == null) {
            w.write("null");
        } else {
            dumpValue(w, o.getClass(), o, level);
        }
    }
    void dumpValue(PrintWriter w, Class c, Object o, int level) throws IOException {
        if (o == null) {
            w.write("null");
            return;
        } else if (c.isPrimitive() || c == Object.class) {
            w.write(o.toString());
            return;
        }
        if (isString(c)) {
            w.print('"');
            w.print(o.toString());
            w.print('"');
        } else if (c.isArray()) {
            w.print("[ ");
            for (int i = 0, n = Array.getLength(o); i < n; i++) {
                write(w, Array.get(o, i), level + 1);
                w.print(", ");
            }
            w.print(']');
        } else {
            if (embeded.containsKey(o)) {
                w.print('(');
                w.print(embeded.get(o));
                w.print(')');
            } else {
                dumpObject(w, c, o, level);
            }
        }
    }
    void dumpObject(PrintWriter w, Class c, Object o, int level) throws IOException {
        assert o != null && !c.isArray() && !c.isPrimitive() && !isString(c);
        try {
            Method m = c.getMethod("toString", new Class[0]);
            if (!TOSTRING.equals(m)) {
                w.write(o.toString());
                return;
            }
        } catch (NoSuchMethodException e) { // never
            w.write(o.toString());
            return;
        }
        embeded.put(o, o.toString());
        w.print(o.toString());
        dumpFields(w, c, o, level);
        c = c.getSuperclass();
        if (c != null && c != Object.class) {
            w.println();
            dumpValue(w, c, o, level);
        }
    }
    void dumpFields(PrintWriter w, Class c, Object o, int level) throws IOException {
        String indent = createIndent(level);
        Field[] f = c.getDeclaredFields();
        for (int i = 0; i < f.length; i++) {
            if(f[i].isSynthetic()) {
                continue;
            }

            w.println();
            w.print(indent);
            w.print(f[i].getName());
            w.print('=');
            boolean old = f[i].isAccessible();
            if (!old) {
                changeAccessible(f[i], true);
            }
            try {
                dumpValue(w, f[i].getType(), f[i].get(o), level + 1);
            } catch (IllegalAccessException e) {
                w.print("EACCESS");
            }
            if (!old) {
                changeAccessible(f[i], false);
            }
        }
    }
    void changeAccessible(Field f, boolean tobe) {
        try {
            f.setAccessible(tobe);
        } catch (SecurityException e) {
        }
    }
    boolean isString(Class c) {
        return (c == String.class || c == StringBuffer.class
                || c == CharSequence.class);
    }
    String createIndent(int level) {
        char[] c = new char[level * INDENT_WIDTH];
        Arrays.fill(c, ' ');
        return new String(c);
    }
}
