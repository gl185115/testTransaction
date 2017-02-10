// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Nibble.java,v $
// Revision 1.3  2003/08/04 02:43:12  art
// Javadoc記述を修正
//
// Revision 1.2  2003/04/10 07:46:06  art
// 設定系メソッドを追加
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Nibble.java,v 1.3 2003/08/04 02:43:12 art Exp $
//
package ncr.realgate.util;

/**
 * 4ビット単位でバイトを扱うユーティリティ。<br>
 * すべてのメソッドはスタティックである。
 * 
 * @author NCR Japan Ltd.
 * @version $Revision: 1.3 $ $Date: 2003/08/04 02:43:12 $
 */
public final class Nibble {
    private static final char hexTbl[] = {
	'0', '1', '2', '3', '4', '5', '6', '7', 
	'8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
    };

    /**
     * バイト配列をニブル単位で文字列化する。
     * 
     * @param data 処理対象のバイト配列
     * @return アンパックされた文字列
     * @throws NullPointerException dataパラメータがnull
     */
    public static final String toString(byte[] data) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toString");
	}
	return toString(data, 0, data.length);
    }

    /**
     * バイト配列をニブル単位で文字列化する。<br>
     * このメソッドに限り、offパラメータとlenパラメータの合計が配列の上限を越えていても
     * エラーとせずに、配列の上限まで処理を行う。
     * 
     * @param data 処理対象のバイト配列
     * @param off 配列のオフセット
     * @param len 処理する要素数
     * @return アンパックされた文字列
     * @throws NullPointerException dataパラメータがnull
     * @throws IndexOutOfBoundsException offパラメータが0未満または要素数以上
     */
    public static final String toString(byte[] data, int off, int len) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toString");
	} else if (off < 0 || off >= data.length) {
	    throw new IndexOutOfBoundsException(
			"Nibble.toString " + off + "/" + data.length);
	}
	int cnt = ((off + len) > data.length) ? data.length : off + len;
	StringBuffer sb = new StringBuffer(cnt * 2);
	for (int i = off; i < cnt; i++) {
	    int x = data[i];
	    if (x < 0) {
		x += 256;
	    }
	    sb.append(hexTbl[x >> 4]);
	    sb.append(hexTbl[x & 15]);
	}
	return sb.toString();
    }

    /**
     * 16進化文字列をバイト配列に変換する。変換不能な文字は0として扱う。<br>
     * 奇数桁の場合は、先頭に0を補う。
     * 
     * @param data 文字列
     * @return 変換後バイト配列
     * @throws NullPointerException dataパラメータがnull
     */
    public static final byte[] toBytes(CharSequence data) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toBytes");
	}
	return toBytes(data, 0, data.length());
    }

    /**
     * 16進化文字列をバイト配列に変換する。変換不能な文字は0として扱う。<br>
     * 奇数桁の場合は、先頭に0を補う。
     * 
     * @param data 文字列
     * @param off 処理を開始する0ベースのインデックス
     * @param len 処理する要素数
     * @return 変換後バイト配列
     * @throws NullPointerException dataパラメータがnull
     * @throws IndexOutOfBoundsException offパラメータが0未満または文字数以上、
     *         またはoffパラメータとlenパラメータの合計が文字列長を越えている
     */
    public static final byte[] toBytes(CharSequence data, int off, int len) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toString");
	} else if (off < 0 || off >= data.length()) {
	    throw new IndexOutOfBoundsException(
			"Nibble.toBytes " + off + "/" + data.length());
	} else if ((off + len) > data.length()) {
	    throw new IndexOutOfBoundsException(
         	"Nibble.toBytes " + off + "+" + len 
		+ " for " + data.length());
	}
	byte[] result = new byte[(len + 1) / 2];
	int start = ((len % 2) == 1) ? 0 : 1;
	int x = (start == 0) ? 0 : Character.digit(data.charAt(off), 16);
	if (x < 0) {
	    x = 0;
	} else {
	    x <<= 4;
	}
	int idx = 0;
	int end = off + len;
	for (int i = start + off;; i++) {
	    int c = Character.digit(data.charAt(i), 16);
	    if (c < 0) {
		result[idx++] = (byte)x;
	    } else {
		result[idx++] = (byte)(x + c);
	    }
	    i++;
	    if (i >= end) break;
	    x = Character.digit(data.charAt(i), 16);
	    if (x < 0) {
		x = 0;
	    } else {
		x <<= 4;
	    }
	}
	return result;
    }

    /**
     * 指定した数値を指定した長さの2進化10進数に変換する。<br>
     * 例) toByte(100, 4) -> 0x00000100
     *     toByte(12345, 2) -> 0x2345 (右詰)
     * @param data 変換対象の数値
     * @param len 変換後のバイト配列の長さ(バイト数)
     * @return 変換後バイト配列
     */
    public static byte[] toBytes(long data, int len) {
	// DecimalFormatは遅いので直接処理する。
	StringBuffer ch = new StringBuffer(len * 2);
	for (int i = 0; i < len * 2; i++) {
	    ch.append('0');
	}
	String s = String.valueOf(data);
	int slen = s.length();
	for (int i = len * 2 - 1; i >= 0 && slen > 0; i--) {
	    ch.setCharAt(i, s.charAt(--slen));
	}
	return toBytes(ch, 0, len * 2);
    }

    /**
     * BCD文字列のエンディアンを変換する。
     * 
     * @param sb 変換対象の文字列
     * @param src 変換開始位置(ここで指定した位置から2文字が対象となる)
     * @param dst 変換対象位置
     */
    public static void changeEndian(StringBuffer sb, int src, int dst) {
	char ch0 = sb.charAt(src);
	char ch1 = sb.charAt(src + 1);
	sb.setCharAt(src, sb.charAt(dst));
	sb.setCharAt(src + 1, sb.charAt(dst + 1));
	sb.setCharAt(dst, ch0);
	sb.setCharAt(dst + 1, ch1);
    }

    /**
     * Hタイプ(16進化コード)を整数に変換する。
     * 
     * @param dat Hタイプで格納されたデータ
     * @param offset Hタイプの先頭バイトへのオフセット
     * @param len Hタイプデータの長さ
     * @return 変換後の整数値
     */
    public static int toHex(byte[] dat, int offset, int len) {
	return toInt(dat, offset, len, 16);
    }

    /**
     * Kタイプ(2進化10進数)を整数に変換する。
     * 
     * @param dat Kタイプで格納されたデータ
     * @param offset Kタイプの先頭バイトへのオフセット
     * @param len Kタイプデータの長さ(バイト数)
     * @return 変換後の整数値(32ビット)
     */
    public static int toInt(byte[] dat, int offset, int len) {
	return toInt(dat, offset, len, 10);
    }

    /**
     * Kタイプ(2進化10進数)を整数に変換する。
     * 
     * @param dat Kタイプで格納されたデータ
     * @param offset Kタイプの先頭バイトへのオフセット
     * @param len Kタイプデータの長さ(バイト数)
     * @return 変換後の整数値(64ビット)
     */
    public static long toLong(byte[] dat, int offset, int len) {
	return toLong(dat, offset, len, 10);
    }

    /**
     * 2バイトのHタイプ(16進化コード-ビッグエンディアン)を整数に変換する。
     * 
     * @param dat Hタイプで格納されたデータ
     * @param offset Hタイプの先頭バイトへのオフセット
     * @return 変換後の整数値
     */
    public static int toHex(byte[] dat, int offset) {
	return toInt(dat, offset, 2, 16);
    }

    /**
     * 2バイトのKタイプ(2進化10進数)を整数に変換する。
     * 
     * @param dat Kタイプで格納されたデータ
     * @param offset Kタイプの先頭バイトへのオフセット
     * @return 変換後の整数値
     */
    public static int toInt(byte[] dat, int offset) {
	return toInt(dat, offset, 2, 10);
    }

    private static int toInt(byte[] dat, int offset, int len, int radix) {
	int n = 0;
	for (int i = offset, l = offset + len; i < l; i++) {
	    n *= radix;
	    n += ((dat[i] >> 4) & 0x0f);
	    n *= radix;
	    n += (dat[i] & 0x0f);
	}
	return n;
    }

    private static long toLong(byte[] dat, int offset, int len, int radix) {
	long n = 0L;
	for (int i = offset, l = offset + len; i < l; i++) {
	    n *= radix;
	    n += ((dat[i] >> 4) & 0x0f);
	    n *= radix;
	    n += (dat[i] & 0x0f);
	}
	return n;
    }

    /**
     * バイト配列に埋め込まれたリトルエンディアン32ビットデータからintを取り出す。
     *
     * @param data データを含んだバイト配列
     * @param offset リトルエンディアンデータの開始オフセット
     */
    public static int toIntFromL(byte[] data, int offset) {
	int n = data[offset + 3] & 0xff;
	for (int i = offset + 2; i >= offset; i--) {
	    n <<= 8;
	    n += (data[i] & 0xff);
	}
	return n;
    }

    /**
     * バイト配列に埋め込まれたリトルエンディアン16ビットデータからintを取り出す。
     *
     * @param data データを含んだバイト配列
     * @param offset リトルエンディアンデータの開始オフセット
     */
    public static int toIntFromLS(byte[] data, int offset) {
	int n = data[offset + 1] & 0xff;
	n <<= 8;
	n += (data[offset] & 0xff);
	return n;
    }

    /**
     * 指定された数値をリトルエンディアン32ビットとして配列に設定する。
     * @param value 設定する値
     * @param data 設定先バイト配列
     * @param offset リトルエンディアンデータの開始オフセット
     */
    public static void setIntToL(int value, byte[] data, int offset) {
	for (int i = 0; i < 4; i++) {
	    data[i + offset] = (byte)(value >> (8 * i));
	}
    }

    /**
     * 指定された数値をリトルエンディアン16ビットとして配列に設定する。
     * @param value 設定する値
     * @param data 設定先バイト配列
     * @param offset リトルエンディアンデータの開始オフセット
     */
    public static void setIntToLS(int value, byte[] data, int offset) {
	data[offset] = (byte)value;
	data[offset + 1] = (byte)(value >> 8);
    }

    /**
     * 指定された数値をビッグエンディアン32ビットとして配列に設定する。
     * @param value 設定する値
     * @param data 設定先バイト配列
     * @param offset ビッグエンディアンデータの開始オフセット
     */
    public static void setIntToB(int value, byte[] data, int offset) {
	for (int i = 0; i < 4; i++) {
	    data[i + offset] = (byte)(value >> (24 - 8 * i));
	}
    }

    /**
     * 指定された数値をビッグエンディアン16ビットとして配列に設定する。
     * @param value 設定する値
     * @param data 設定先バイト配列
     * @param offset ビッグエンディアンデータの開始オフセット
     */
    public static void setIntToBS(int value, byte[] data, int offset) {
	data[offset] = (byte)(value >> 8);
	data[offset + 1] = (byte)value;
    }

    /**
     * バイト配列に埋め込まれたビッグエンディアン32ビットデータからintを取り出す。
     *
     * @param data データを含んだバイト配列
     * @param offset ビッグエンディアンデータの開始オフセット
     */
    public static int toIntFromB(byte[] data, int offset) {
	int n = data[offset] & 0xff;
	for (int i = offset + 1, m = offset + 4; i < m; i++) {
	    n <<= 8;
	    n += (data[i] & 0xff);
	}
	return n;
    }

    /**
     * バイト配列に埋め込まれたビッグエンディアン16ビットデータからintを取り出す。
     *
     * @param data データを含んだバイト配列
     * @param offset ビッグエンディアンデータの開始オフセット
     */
    public static int toIntFromBS(byte[] data, int offset) {
	int n = data[offset] & 0xff;
	n <<= 8;
	n += (data[offset + 1] & 0xff);
	return n;
    }
}

