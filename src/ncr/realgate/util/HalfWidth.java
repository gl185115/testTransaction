// Copyright (c) 2002, 2004 NCR Japan Ltd.
//
// $Log: HalfWidth.java,v $
// Revision 1.3  2004/04/06 02:52:17  art
// 濁点、半濁点を意識した片仮名半角/全角変換処理を追加。
//
// Revision 1.2  2003/01/08 07:57:28  art
// Javadocの参照の記述エラーを修正
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: HalfWidth.java,v 1.3 2004/04/06 02:52:17 art Exp $
//
package ncr.realgate.util;

/**
 * 半角文字を処理するためのユーティリティクラス。<br>
 * すべてのメソッドはスタティックである。
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.3 $ $Date: 2004/04/06 02:52:17 $
 */
public final class HalfWidth {

    private static final String[] hz_kataCV = {
	"｡｢｣､･ｦｧｨｩｪｫｬｭｮｯｰｱｲｳｴｵｶｷｸｹｺｻｼｽｾｿﾀﾁﾂﾃﾄﾅﾆﾇﾈﾉﾊﾋﾌﾍﾎﾏﾐﾑﾒﾓﾔﾕﾖﾗﾘﾙﾚﾛﾜﾝﾞﾟ",
	"。「」、・ヲァィゥェォャュョッーアイウエオカキクケコサシスセソ"
	  + "タチツテトナニヌネノハヒフヘホマミムメモヤユヨラリルレロワン゛゜",
	"?????????????????????ガギグゲゴザジズゼゾ"
	  + "ダヂヅデド?????バビブベボ?????????????????",
	"???????????????????????????????"
	  + "??????????パピプペポ?????????????????",
    };

    private static final String voicedMark = "ﾞﾟ";

    private static final char ALT_YENMARK = '\u00a5';
    private static final char ALT_TILDE = '\u203e';

    // インスタンス作成禁止
    private HalfWidth() {
    }

    /**
     * 与えられた文字が半角文字かを判定する。
     * @param ch 判定を行う文字
     * @return 半角文字であれば真
     */
    public static final boolean isHalfWidth(char ch) {
	if (ch >= '\uff61' && ch <= '\uff9f') {
	    return true;
	} else if (ch >= 0 && ch <= '\u007f') {
	    return true;
	} else if (ch == ALT_YENMARK || ch == ALT_TILDE) {
	    return true;
	}
	return false;
    }

    /**
     * 与えられた文字が全角文字かを判定する。
     * @param ch 判定を行う文字
     * @return 全角文字であれば真
     */
    public static final boolean isFullWidth(char ch) {
	return !isHalfWidth(ch);
    }

    /**
     * 与えられた文字を半角から全角へ変換する。
     * @param ch 半角文字。あらかじめ全角の場合、そのまま返送する。
     * @return パラメータを全角文字へ変換後の文字
     */
    public static final char halfToFull(char ch) {
	if (ch >= '\uff61' && ch <= '\uff9f') {	// Halfwidth Katakana
	    return hz_kataCV[1].charAt(ch - '\uff61');
	} else if (ch == ' ') {			// space
	    return '\u3000';
	} else if (ch == '\\' || ch == ALT_YENMARK) {		// yen
	    return '\uffe5';
	} else if (ch > ' ' && ch <= '\u007e') {// ASCII graph
	    return (char)('\uff00' + (ch - '\u0020'));
	}
	return ch;
    }

    /**
     * 与えられた全角文字を半角文字へ変換する。
     * @param ch 全角文字。変換不可能な文字はそのまま戻す。
     * @return パラメータを半角文字へ変換後の文字（変換不能な場合、元の文字）
     */
    public static final char fullToHalf(char ch) { 
	if (ch >= '\uff01' && ch <= '\uff5e') {        // ASCII graph
	    return (char)(ch - '\uff01' + '\u0021');
	} else if (ch == '\u3000') {
	    return ' ';
	} else if (ch == '\uffe5') {
	    return '\\';
	}
	int index = hz_kataCV[1].indexOf(ch);
	if (index >= 0) {
	    return hz_kataCV[0].charAt(index);
	}
	return ch;
    }

    /**
     * 与えられた文字列内の半角片仮名を全角片仮名へ変換する。<br>
     * 処理範囲はUnicodeの'\uff61'から'\uff9f'である。また、濁点と半濁点は可能ならば
     * 直前の文字と合成する。それ以外の文字はそのまま結果の文字列へコピーする。
     * @param str 全角片仮名へ変換する文字列
     * @return 全角片仮名へ変換後の文字列
     */
    public static final String toFullWidthKana(CharSequence str) {
	StringBuffer sb = new StringBuffer(str.length());
	int prindex = -1;
	int index;

      convert:
	for (int i = 0; i < str.length(); i++, prindex = index) {
	    char ch = str.charAt(i);
	    index = hz_kataCV[0].indexOf(ch);
	    if (index >= 0) {
		if (prindex >= 0 && (ch == 'ﾞ' || ch == 'ﾟ')) {
		    char voiced = hz_kataCV[ch - 'ﾞ' + 2].charAt(prindex);
		    if (voiced != '?') {
			sb.setCharAt(sb.length() - 1, voiced);
			continue convert;
		    }
		}
		sb.append(hz_kataCV[1].charAt(index));
	    } else {
		sb.append(ch);
	    }
	}

	return new String(sb);
    }

    /**
     * 与えられた文字列内の全角片仮名を半角片仮名へ変換する。<br>
     * 濁音や半濁音は2文字の半角片仮名に変換する。<br>
     * 変換後の範囲はUnicodeの'\uff61'から'\uff9f'である。それ以外の文字はそのまま結果の文字列へコピーする。
     * @param str 半角片仮名へ変換する文字列
     * @return 半角片仮名へ変換後の文字列
     */
    public static final String toHalfWidthKana(CharSequence str) {
	StringBuffer sb = new StringBuffer((int)(str.length() * 1.5));
	int index;

      convert:
	for (int i = 0; i < str.length(); i++) {
	    char ch = str.charAt(i);
	    if (ch < '\u0080') {
		sb.append(ch);
		continue;
	    }
	    index = hz_kataCV[1].indexOf(ch);
	    if (index >= 0) {
		sb.append(hz_kataCV[0].charAt(index));
	    } else {
		for (int j = 0; j < 2; j++) {
		    index = hz_kataCV[j + 2].indexOf(ch);
		    if (index >= 0) {
			sb.append(hz_kataCV[0].charAt(index)).append((char)('ﾞ' + j));
			continue convert;
		    }
		}
		sb.append(ch);
	    }
	}

	return new String(sb);
    }

    /**
     * 与えられた文字の桁数を返送する。半角は1、全角を2とする。
     * @param str 桁数を判定する文字列
     * @return 桁数
     * @throws NullPointerException 引数がnullである。
     */
    public static final int getWidth(CharSequence str) {
	if (str == null) {
	    throw new NullPointerException("HalfWidth:getColumns");
	}
	int len = 0;
	for (int i = 0; i < str.length(); i++, len++) {
	    if (isHalfWidth(str.charAt(i)) == false) {
		len++;
	    }
	}
	return len;
    }

    /**
     * ターゲットとして指定された文字列の指定桁以降に、元になる文字列を埋め込む。<br>
     * ターゲットの指定桁および、埋め込み後の最終桁が、全角文字の中間となる場合は、
     * 1桁分のスペースにより抹消される。<br>
     * 例）
     * <pre>
     * set("あいうえお", 3, "かき") -> "あ かき お"
     * </pre>
     * <br>
     * 埋め込み先文字列の長さが桁数に足りない場合は、桁数まで空白で伸長する。<br>
     * 例）
     * <pre>
     * set("あいうえお", 11, "かき") -> "あいうえお かき"
     * </pre>
     * 
     * @param target 埋め込み先の文字列(nullの場合、指定桁数の空白文字列を作成する)
     * @param col 埋め込み先の桁位置
     * @param source 埋め込む文字列
     * @return 合成した文字列
     * @throws NullPointerException 引数sourceがnullである。
     * @throws IllegalArgumentException 引数colが0未満である。
     */
    public static final String set(CharSequence target, int col, CharSequence source) {
	if (source == null) {
	    throw new NullPointerException("HalfWidth:set");
	}
	if (col < 0) {
	    throw new IllegalArgumentException("HalfWidth:set");
	}
	StringBuffer sb = new StringBuffer(col + source.length() + 8);
	if (target != null) {
	    sb.append(target);
	}
	int curr = 0;
	boolean full = false;
	int point = -1;
	for (int i = 0; i < sb.length(); i++, curr++) {
	    if (curr == col) {
		point = i;
	    }
	    if (isHalfWidth(sb.charAt(i)) == false) {
		curr++;
		if (curr == col) {
		    point = i;
		    full = true;
		}
	    }
	}
	if (curr <= col) {
	    for (int i = curr; i < col; i++) {
		sb.append(' ');
	    }
	    sb.append(source);
	    return sb.toString();
	}
	if (full) {
	    sb.setCharAt(point, ' ');
	    sb.insert(point, ' ');
	    point++;
	}
	sb.insert(point, source);
	int ncol = getWidth(source);
	point += source.length();
	int end = point;
	full = false;
	for (; ncol > 0; end++) {
	    if (end >= sb.length()) {
		break;
	    }
	    if (isHalfWidth(sb.charAt(end))) {
		ncol--;
	    } else {
		ncol--;
		if (ncol == 0) {
		    full = true;
		}
		ncol--;
	    }
	}
	sb.delete(point, end);
	if (full) {
	    sb.insert(point, ' ');
	}
	return sb.toString();
    }

    /**
     * 与えられた文字列を指定桁数で分割する。<br>
     * 指定桁が全角文字の中間になる場合には、最初の文字列には1桁分のスペースを埋め、
     * 続く文字列から該当文字を設定する。<br>
     * また、最後の文字列対する指定桁分の空白埋め込みは行わない。
     * 例）
     * <pre>
     *  split("あいうえお", 5) -> {"あい ", "うえ ", "お" }
     * </pre>
     * @param str 分割する文字列
     * @param cols 分割する桁数
     * @return 桁数により分割した文字列の配列
     * @throws NullPointerException 引数strがnullである。
     * @throws IllegalArgumentException 引数colsが0以下である。
     */
    public static final String[] split(CharSequence str, int cols) {
	if (str == null) {
	    throw new NullPointerException("HalfWidth:split");
	}
	if (cols <= 0) {
	    throw new IllegalArgumentException("HalfWidth:split");
	}
	java.util.LinkedList list = new java.util.LinkedList();
	int beg = 0;
	int end = 0;
	int len = str.length();
	for (int i = 0; i < len; i++) {
	    if (isHalfWidth(str.charAt(i))) {
		end++;
	    } else {
		end++;
		if (end == cols) {
		    list.add(str.subSequence(beg, i) + " ");
		    i--;
		    end = 0;
		    beg = i + 1;
		    continue;
		}
		end++;
	    }
	    if (end == cols) {
		list.add(str.subSequence(beg, i + 1));
		end = 0;
		beg = i + 1;
	    }
	}
	if (beg < len) {
	    list.add(str.subSequence(beg, len));
	}
	String[] result = new String[list.size()];
	return (String[])list.toArray(result);
    }

    /**
     * 与えられた文字列を指定桁数で分割し、最初の文字列を返送する。<br>
     * 指定桁が全角文字の中間になる場合には、1桁分のスペースを埋めて返す。
     * 例）
     * <pre>
     *  left("あいうえお", 5, 0) -> "あい "
     * </pre>
     * <br>
     * もし、桁数より文字列が短い場合には、桁数分のスペースを埋めて返す。
     * @param str 分割する文字列
     * @param cols 分割する桁数(0の場合、空文字列を返送する。)
     * @return 桁数により分割した最初の文字列
     * @throws NullPointerException 引数strがnullである。
     * @throws IllegalArgumentException 引数colsが0未満である。
     */
    public static final String left(CharSequence str, int cols) {
	return left(str, cols, null);
    }

    /**
     * {@link #left(CharSequence, int, HalfWidth.NextLine)}メソッドで次行の開始位置を
     * 呼び出し側に通知するために利用するクラス
     *
     * @see #left(CharSequence, int, HalfWidth.NextLine)
     */
    public static class NextLine {
	private int end;
	/**
	 * 初期位置を負値に設定した新規インスタンスを作成する。
	 */
	public NextLine() {
	    end = -1;
	}
	/**
	 * 次行の開始文字位置（桁位置ではない）を0からの整数で返送する。
	 * 文字列をすべて消費し、次行が無い場合、または
	 * {@link #left(CharSequence, int, HalfWidth.NextLine)}メソッドの呼び出し前、
	 * あるいは呼び出しに失敗した場合には負値を返送する。
	 */
	public int end() { return end; }
    }

    /**
     * 与えられた文字列を指定桁数で分割し、最初の文字列を返送する。<br>
     * 指定桁が全角文字の中間になる場合には、1桁分のスペースを埋めて返す。
     * 例）
     * <pre>
     *  left("あいうえお", 5, 0) -> "あい "
     * </pre>
     * <br>
     * もし、桁数より文字列が短い場合には、桁数分のスペースを埋めて返す。
     * @param str 分割する文字列
     * @param cols 分割する桁数(0の場合、空文字列を返送する。)
     * @param next 分割の次行の開始文字位置を設定する。
     * @return 桁数により分割した最初の文字列
     * @throws NullPointerException 引数strがnullである。
     * @throws IllegalArgumentException 引数colsが0未満である。
     */
    public static final String left(CharSequence str, int cols, NextLine next) {
	if (next != null) {
	    next.end = -1;
	}
	if (str == null) {
	    throw new NullPointerException("HalfWidth:split");
	}
	if (cols < 0) {
	    throw new IllegalArgumentException("HalfWidth:split");
	} else if (cols == 0) {
	    return "";
	}
	int end = 0;
	int len = str.length();
	for (int i = 0; i < len; i++) {
	    if (isHalfWidth(str.charAt(i))) {
		end++;
	    } else {
		end++;
		if (end == cols) {
		    if (next != null && i < len) {
			next.end = i;
		    }
		    return str.subSequence(0, i) + " ";
		}
		end++;
	    }
	    if (end == cols) {
		if (next != null && i + 1 < len) {
		    next.end = i + 1;
		}
		return str.subSequence(0, i + 1).toString();
	    }
	}
	StringBuffer sb = new StringBuffer(str.toString());
	for (; end < cols; end++) {
	    sb.append(' ');
	}
	return sb.toString();
    }
}
