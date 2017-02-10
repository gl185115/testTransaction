// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Guid.java,v $
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Guid.java,v 1.1 2002/12/25 01:57:37 kudo Exp $
//
package ncr.realgate.util;

/**
 * GUID(Global Unique IDentity)またはuuid(Universion Unique Identity)を変換／作成するユーティリティ。
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.1 $ $Date: 2002/12/25 01:57:37 $
 */
public final class Guid {

    /**
     * GUID(uuid)のバイト数
     */
    public static final int LENGTH = 16;

    private Guid() {
    }

    /** 
     * uuid文字列を16バイトの配列に変換する。
     * @param uuid {xxxxxxxx-xx-..}形式のGUID
     * @return 16バイト固定長バイト配列によるGUID
     * @throws NullPointerException uuidパラメータがnull
     * @throws IllegalArgumentException uuidパラメータが正当な文字列表現ではない。
     */
    public static byte[] toBytes(CharSequence uuid) {
	if (uuid == null) {
	    throw new NullPointerException("Guid#toBytes");
	}
	StringBuffer sb = new StringBuffer(32);
	for (int i = 0; i < uuid.length(); i++) {
	    char ch = uuid.charAt(i);
	    if (ch != '-' && ch != '{' && ch != '}')
		sb.append(ch);
	}
	if (sb.length() != 32) {
	    throw new IllegalArgumentException("Bad GUID:" + uuid);
	}
	Nibble.changeEndian(sb, 0, 6);
	Nibble.changeEndian(sb, 2, 4);
	Nibble.changeEndian(sb, 8, 10);
	Nibble.changeEndian(sb, 12, 14);
	byte[] buuid = new byte[16];
	for (int i = 0; i < 16; i++) {
	    buuid[i] = (byte)Integer.parseInt(sb.substring(i * 2, i * 2 + 2), 16);
	}
	return buuid;
    }

    /**
     * uuidを可読化フォーマットに変換する。
     *
     * @param uuid 元のuuidを含むバイト配列
     * @param offset uuidの開始オフセット
     * @return {xxxx...} 形式の文字列
     * @throws IndexOutOfBoundsException uuidパラメータのoffsetパラメータで指定された位置以降が16バイトに満たない。
     * @throws NullPointerException uuidパラメータがnull
     */
    public static String toString(byte[] uuid, int offset) {
	if (uuid == null) {
	    throw new NullPointerException("Guid#toString");
	} else if (offset < 0 || (uuid.length - offset) < 16) {
	    throw new IndexOutOfBoundsException("Guid#toString length=" + 
					       uuid.length + ", offset=" +
					       offset);
	}
	StringBuffer sb = new StringBuffer(64);
	sb.append('{');
	sb.append(Nibble.toString(uuid, offset, 4));
	Nibble.changeEndian(sb, 1, 7);
	Nibble.changeEndian(sb, 3, 5);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 4, 2));
	Nibble.changeEndian(sb, 10, 12);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 6, 2));
	Nibble.changeEndian(sb, 15, 17);
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 8, 2));
	sb.append('-');
	sb.append(Nibble.toString(uuid, offset + 10, 6));
	sb.append('}');
	return sb.toString();
    }

    /**
     * uuidを可読化フォーマットに変換する。
     *
     * @param uuid 元のuuid
     * @return {xxxx...} 形式の文字列
     * @throws IndexOutOfBoundsException uuidパラメータの長さが16バイトに満たない。
     * @throws NullPointerException uuidパラメータがnull
     */
    public static String toString(byte[] uuid) {
	return toString(uuid, 0);
    }

    /**
     * uuidを生成する。
     *
     * @param aid AIWIDを指定する。
     * @return 16バイト固定長のuuid
     * @throws NullPointerException idパラメータがnull
     */
    public static byte[] create(String aid) {
	if (aid == null) {
	    throw new NullPointerException("Guid#createGuid");
	}
	byte u[] = new byte[16];
	java.util.Arrays.fill(u, (byte)0);
	java.rmi.server.UID uid = new java.rmi.server.UID();
	java.io.ByteArrayOutputStream bo = new java.io.ByteArrayOutputStream();
	try {
	    java.io.DataOutputStream dx = new java.io.DataOutputStream(bo);
	    uid.write(dx);
	    byte[] b = bo.toByteArray();
	    for (int i = 0, n = b.length; i < n && i < 16; i++) {
		u[i] = b[i];
	    }
	    dx.close();
	} catch (java.io.IOException e) {
	    e.printStackTrace();
	}
	byte[] id = Nibble.toBytes(aid);
	for (int i = 0, n = id.length; i < n && i < 2; i++) {
	    u[14 + i] = id[i];
	}
	return u;
    }
}

