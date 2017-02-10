// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Trace.java,v $
// Revision 1.13  2008/05/16 02:36:16  kudo
// REAL上のアプリケーションでは、Contextから動的に変更されたデバッグレベルを
// 取得できないため、Traceから現在のデバッグレベルを取得出来るように変更した。
//
// Revision 1.12  2006/02/15 02:50:50  art
// flushメソッドを追加
//
// Revision 1.11  2005/12/15 09:58:08  art
// デバッグ出力の削除
//
// Revision 1.10  2005/12/15 09:57:09  art
// Boolean変換時に0で始まらない数値は真とみなすCルール(ただし中途半端)を適用。
//
// Revision 1.9  2005/09/15 04:44:09  art
// バイト配列のトレースにHexDumpを利用するように修正。
//
// Revision 1.8  2005/09/15 02:25:47  art
// パラメータエラー時に例外とせずに、与えられたパラメータの状態を示す文字列を出力するように修正。
//
// Revision 1.7  2005/01/25 05:35:42  art
// サブクラスの導出を可能とした。
//
// Revision 1.6  2005/01/20 08:16:50  art
// デフォルトコンストラクタとプロパティ設定によってインスタンスを初期化可能に変更。
//
// Revision 1.5  2003/08/26 00:59:24  art
// 時刻印字付きの単なる文字列出力メソッドをPrinterに追加。
//
// Revision 1.4  2003/07/15 11:52:44  art
// クローズ時にサイズが0なら削除するように変更。
//
// Revision 1.3  2003/06/11 07:01:27  art
// メソッドヘッダ用バッファサイズを拡大した。
//
// Revision 1.2  2003/05/27 11:23:38  art
// メソッド退出時の印字で例外とならないように防御を加えた。
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Trace.java,v 1.13 2008/05/16 02:36:16 kudo Exp $
//
package ncr.realgate.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.text.ChoiceFormat;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * デバッグ用トレースファイルの制御を行うクラス。実際のトレースファイルへの出力処理は、
 * Printerオブジェクトによって行う。<br>
 * デバッグ用機能なので、内部で発生したすべての例外を無視する。呼び出し元へは例外は通知されない。
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.13 $ $Date: 2008/05/16 02:36:16 $
 */
public class Trace {
    // デバッグレベルによって出力を切り替える
    private int rootDebugLevel = DEFAULT_LEVEL;
    /**
     * 既定のデバッグレベルを設定する。
     * @param newLevel 新たに設定するデバッグレベル。
     */
    protected void setRootDebugLevel(int newLevel) {
        rootDebugLevel = newLevel;
    }
    /**
     * 既定のデバッグレベルを取得する。
     * @return 既定のデバッグレベル。
     */
    protected int getRootDebugLevel() {
        return rootDebugLevel;
    }
    
    static final int METHOD_ONLY = 1;
    static final int DEFAULT_LEVEL = 3;

    /**
     * 出力する情報量を切り替える。<br>
     * このメソッドではデバッグ出力の停止／再開を行うことはできない。
     * <table border="1">
     * <tr><th>値</th><th>出力</th></tr>
     * <tr><td>0</td><td>出力停止</td></tr>
     * <tr><td>1</td><td>methodEnter, methodExit</td></tr>
     * <tr><td>2以上</td><td>すべて</td></tr>
     * </table>
     * @param newLevel 新たに設定するデバッグレベル
     */
    public void setDebugLevel(int newLevel) {
	setRootDebugLevel(newLevel);
	synchronized (printers) {
	    for (Iterator it = printers.values().iterator(); it.hasNext();) {
		Printer p = (Printer)it.next();
		p.debugLevel = newLevel;
	    }
	}
    }

    /**
     * このオブジェクトに設定されているデバッグレベルを取得する。
     * @return 現在のデバッグレベル
     */
    public int getDebugLevel() {
        return getRootDebugLevel();
    }

    /**
     * 内部で使用する文字列出力用のPrintWriter。デバッグ中でなければnull。
     */
    protected PrintWriter orgOut;
    /**
     * クライアントからの書き込み要求で利用するプリントライタのインスタンスを返送する。<br>
     * サブクラスがそのまま利用する書き込み処理はこのメソッドから出力先を取得しなければならない。
     * @return プリントライタのインスタンス(オープンされていなければnull)。
     */
    protected PrintWriter getOut() {
        return orgOut;
    }
    /**
     * クライアントからの書き込み要求で利用するプリントライタのインスタンスを設定する。<br>
     * @param newOut プリントライタのインスタンス(消去時はnull)。
     */
    protected void setOut(PrintWriter newOut) {
        orgOut = newOut;
    }

    /**
     * Printerオブジェクト格納用
     */
    private HashMap printers = new HashMap();

    /**
     * クローズしても良いかどうかを判定する。<br>
     * ストリーム上にトレースを作成した場合にはクローズしない。
     */
    private boolean isClosable;

    private String pathName;
    /**
     * ファイルのパス名を返送する。<br>
     * 未オープンまたは直接ストリームを設定した場合はnullが返送される。
     * @return トレースファイルのパス名。
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * 指定されたパス名でトレースファイルを作成する。
     * @param path 出力ファイル名(ダミーを作成したい場合には/dev/nullを指定するより、
     *             無引数のコンストラクタを使用すべきである)
     */
    public Trace(CharSequence path) {
	initNew(path);
    }

    /**
     * 指定されたストリーム上にトレースファイルを作成する。
     * @param stream 出力対象のストリーム
     */
    public Trace(OutputStream stream) {
	initNew(stream);
    }

    /**
     * 指定されたライタ上にトレースファイルを作成する。
     * @param writer 出力対象のライター
     */
    public Trace(Writer writer) {
	initNew(writer);
    }

    /**
     * 何も行わないトレースファイルを作成する。
     */
    public Trace() {
    }

    /**
     * 現在のトレースファイルのバッファを書き出す。<br>
     * もしファイルが存在しなければ何も行わない。
     */
    public void flush() {
        PrintWriter out = getOut();
        if (out != null) {
            out.flush();
        }
    }


    /**
     * 現在のトレースファイルをクローズする。<br>
     * もしサイズが0ならば削除する。
     */
    public void close() {
	synchronized (printers) {
	    for (Iterator it = printers.values().iterator(); it.hasNext();) {
		Printer p = (Printer)it.next();
		p.initNew();
	    }
	}
        PrintWriter out = getOut();
	if (out != null) {
	    if (isClosable) {
		out.close();
		File f = new File(pathName);
		if (f.length() == 0) {
		    f.delete();
		}
		pathName = null;
	    }
	    setOut(null);
	    isClosable = false;
	}
    }

    /**
     * 出力先ファイルを設定する。
     * @param path トレース出力ファイルのフルパス名。
     */
    public void setPathName(String path) {
        initNew(path);
    }

    /**
     * 出力先ファイル名をセーブする。<br>
     * {@link #setPathName}と異なりファイルのオープンを伴わない。
     * @parama path トレース出力ファイルのフルパス名。
     */
    protected void setFilePathName(CharSequence path) {
        pathName = path.toString();
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定された新たなファイルを作成する。
     * <br>デバッグ出力レベルはデフォルト(3)を設定する。
     * @param nextPath 次に使用するトレースファイル名
     */
    public void initNew(CharSequence nextPath) {
	initNew(nextPath, DEFAULT_LEVEL);
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定された新たなファイルを追加モードで作成する。
     * @param nextPath 次に使用するトレースファイル名
     * @param level デバッグ出力レベル
     */
    public void initNew(CharSequence nextPath, int level) {
        PrintWriter out = getOut();
	if (out != null) {
	    close();
	}
	try {
	    pathName = nextPath.toString();
	    setOut(new PrintWriter(new BufferedWriter(new FileWriter(pathName, true))));
	    setRootDebugLevel(level);
	    isClosable = true;
	} catch (IOException e) {
            // このファイルはデバッグ用なので作成に失敗しても無視する。
	    setOut(null);
	}
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定されたストリーム上でトレースを行う。
     * <br>デバッグ出力レベルはデフォルト(3)を設定する。
     * @param stream トレースを出力するストリーム
     */
    public void initNew(OutputStream stream) {
	initNew(stream, DEFAULT_LEVEL);
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定されたストリーム上でトレースを行う。
     * @param stream トレースを出力するストリーム
     * @param level デバッグ出力レベル
     */
    public void initNew(OutputStream stream, int level) {
        PrintWriter out = getOut();
	if (out != null) {
	    close();
	}
	setOut(new PrintWriter(new BufferedWriter(new OutputStreamWriter(stream))));
	setRootDebugLevel(level);
	isClosable = false;
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定されたライタ上でトレースを行う。<br>
     * デバッグ出力レベルはデフォルト(3)を設定する。
     * @param writer トレースを出力するライタ
     */
    public void initNew(Writer writer) {
	initNew(writer, DEFAULT_LEVEL);
    }

    /**
     * もしオープン中であれば現在のトレースファイルをクローズする。
     * その後パラメータで指定されたライタ上でトレースを行う。
     * @param writer トレースを出力するライタ
     * @param level デバッグ出力レベル
     */
    public void initNew(Writer writer, int level) {
        PrintWriter out = getOut();
	if (out != null) {
	    close();
	}
	setOut(new PrintWriter(writer));
	setRootDebugLevel(level);
	isClosable = false;
    }

    /**
     * 実際の書き込み処理を行うオブジェクトであり、個々のアプリケーションのトレースを管理する。
     * methodEnter/methodExitのペアを利用することによりトレースファイル内でインデントが
     * 行われる。<br>
     * printlnメソッドは、キャプションと値の組み合わせを区切り文字(デフォルトは'=')で結合
     * して出力する。<br>
     * 例）
     * <pre>
     * int myMethod(int x, String y, long z) {
     *   Trace.Printer ptr = trace.createPrinter(getClass());
     *   ptr.methodEnter("myMethod");
     *   ptr.println("arg:x", x).println("arg:y", y).println("arg:z", z);
     *   ....
     *   return ptr.methodExit(result);
     * }
     * </pre>
     * <br>出力結果<br>
     * <pre>
     * hh:mm:ss:mss ClassName.myMethod -----enter-----
     *              arg:x=1
     *              arg:y="yyyyy"
     *              arg:z=300000
     * hh:mm:ss:mss ClassName.myMethod=result -----exit-----
     * </pre>
     * <br>
     * デバッグレベルが1の場合は、methodEnter,methodExitの両メソッドのみ出力を行う。
     * 他のメソッドによる出力を行うにはデバッグレベルを2以上に設定しなければならない。
     * <br>
     * @see #createPrinter(String)
     * @see #createPrinter(Class)
     */
    public class Printer {
	public static final int TIME_WIDTH = 13;
	private String appName;
	private int nestLevel;
	private List methodNames;
	private boolean timestamp;
	private char separator;
	private DateFormat fmt = new SimpleDateFormat("HH:mm:ss:SSS");
	private ChoiceFormat hexFmt;
	int debugLevel;

	/** トレースファイルの切り替わり時に呼び出される。
	 * 現在のフィールドのうち、メソッド関連の設定をリセットする。
	 */
	void initNew() {
	    debugLevel = getRootDebugLevel();
	    nestLevel = 0;
	    methodNames.clear();
	}

	/**
         * プリンターオブジェクトを生成する。
         * @param name このプリンターのキャプションに利用する文字列。
         */
	protected Printer(String name) {
	    debugLevel = getRootDebugLevel();
	    appName = name;
	    nestLevel = 0;
	    timestamp = false;
	    separator = '=';
	    methodNames = new ArrayList();
	}

	private void printMethodHeader(boolean start) {
	    StringBuffer sb = new StringBuffer(200);
	    if (!start) {
		nestLevel--;
	    }
	    for (int i = 0; i < nestLevel; i++) {
		sb.append("--");
	    }
	    sb.append(fmt.format(new java.util.Date()));
	    sb.append(' ');
	    sb.append(appName);
	    sb.append('.');
	    // アプリケーションがmethodEnterを呼ばずにmethodExitを呼び出した場合
	    // 例外となるので、防御する。
	    int index = methodNames.size() - 1;
	    if (index >= 0) {
		sb.append(methodNames.get(index));
	    } else {
		sb.append("!! no method entering information !!");
	    }
            PrintWriter out = getOut();
	    out.print(sb);
	    if (start) {
		nestLevel++;
	    } else {
		if (index >= 0) {
		    methodNames.remove(index);
		}
	    }
	}

	private void printTime() {
            PrintWriter out = getOut();
	    for (int i = 0; i < nestLevel; i++) {
		out.print("  ");
	    }
	    if (timestamp) {
		out.print(fmt.format(new java.util.Date()));
		out.print(' ');
	    } else {
		for (int i = 0; i < TIME_WIDTH; i++) {
		    out.print(' ');
		}
	    }
	}

	/**
	 * デバッグレベルを設定する。
	 * @param newLevel 呼び出し以降に適用するデバッグレベル
	 * @see #setDebugLevel
	 */
	public void setDebugLevel(int newLevel) {
	    debugLevel = newLevel;
	}

	/**
	 * printlnメソッドのcaptionパラメータとvalueパラメータの区切り文字を設定する。
	 * @param ch 区切り文字
	 */
	public void setSeparatorChar(char ch) {
	    separator = ch;
	}

	/**
	 * printlnメソッドのcaptionパラメータとvalueパラメータの区切り文字を返送する。
	 * @return 区切り文字
	 */
	public char getSeparatorChar() {
	    return separator;
	}

	/**
	 * 時刻印字モードを設定する。<br>
	 * 当モードが影響するのは、printlnメソッドだけである。
	 * @param enabled 真に設定すると印字行の先頭にhh:mm:ss:ms の形式で
	 *                現在時刻を印字する。<br>
	 *                偽に設定すると、TIME_WIDTH桁の空白が印字される。
	 */
	public Printer setTimestampMode(boolean enabled) {
	    timestamp = enabled;
	    return this;
	}

	/**
	 * 現在の時刻印字モードを取得する。
	 * @return 真であれば時刻印字モード
	 */
	public boolean isTimestampMode() {
	    return timestamp;
	}

	/**
	 * メソッドの開始を現在時刻とともに出力し改行する。
	 * @param newMethodName 出力するメソッド名
	 */
	public Printer methodEnter(String newMethodName) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		methodNames.add(newMethodName);
		printMethodHeader(true);
		out.println(" -----enter-----");
	    }
	    return this;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 */
	public void methodExit() {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(' ');
		out.println(" -----exit-----");
		out.flush();
	    }
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力するint値
	 * @return resultを返す
	 */
	public int methodExit(int result) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		out.print(result);
		out.println(" -----exit-----");
		out.flush();
	    }
	    return result;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力するlong値
	 * @return resultを返す
	 */
	public long methodExit(long result) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		out.print(result);
		out.println(" -----exit-----");
		out.flush();
	    }
	    return result;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力するブール値
	 * @return resultを返す
	 */
	public boolean methodExit(boolean result) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		out.print(result);
		out.println(" -----exit-----");
		out.flush();
	    }
	    return result;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力するdouble値
	 * @return resultを返す
	 */
	public double methodExit(double result) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		out.print(result);
		out.println(" -----exit-----");
		out.flush();
	    }
	    return result;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力する文字列値(nullの場合<null>と出力する)
	 * @return resultを文字列として返す
	 */
	public String methodExit(CharSequence result) {
	    String s = (result == null) ? null : result.toString();
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		if (s == null) {
		    out.print("<null>");
		} else {
		    out.print(s);
		}
		out.println(" -----exit-----");
		out.flush();
	    }
	    return s;
	}

	/**
	 * メソッドの終了を現在時刻とともに出力し改行する。<br>
	 * メソッド名は直近のmethodEnterで指定されたものを使用する。
	 * @param result 出力するオブジェクト値（内部でtoString()を呼び出す）<br>
	 *               nullの場合、<null>と出力する。
	 * @return resultを返す
	 */
	public Object methodExit(Object result) {
            PrintWriter out = getOut();
	    if (debugLevel >= METHOD_ONLY && out != null) {
		printMethodHeader(false);
		out.print(separator);
		if (result == null) {
		    out.print("<null>");
		} else {
		    out.print(result.toString());
		}
		out.println(" -----exit-----");
		out.flush();
	    }
	    return result;
	}

	/**
	 *改行する。
	 */
	public Printer println() {
            PrintWriter out = getOut();
	    if (debugLevel > METHOD_ONLY && out != null) {
		out.println();
	    }
	    return this;
	}

	/**
	 * 指定文字列を出力し、改行する。{@link #write}と異なりタイムスタンプを
	 * モードに応じて出力する。
	 */
	public Printer println(CharSequence s) {
            PrintWriter out = getOut();
	    if (debugLevel > METHOD_ONLY && out != null) {
		printTime();
		out.println(s.toString());
	    }
	    return this;
	}

	/**
	 *指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するint値
	 */
	public Printer println(String caption, int value) {
            PrintWriter out = getOut();
	    if (debugLevel > METHOD_ONLY && out != null) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するlong値
	 */
	public Printer println(String caption, long value) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するブール値
	 */
	public Printer println(String caption, boolean value) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するdouble値
	 */
	public Printer println(String caption, double value) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力する文字列
	 */
	public Printer println(String caption, CharSequence value) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するバイト配列(nullの場合、"null"と出力)
	 * @throws NullPointerException valueがnull
	 */
	public Printer println(String caption, byte[] value) {
	    if (value == null) {
		return println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するバイト配列(nullの場合、"null"と出力)
	 * @param off 出力を開始する配列オフセット
	 * @param len 出力する要素数
	 */
	public Printer println(String caption, byte[] value, int off, int len) {
	    if (value == null) {
                return println(caption, "null");
	    } else if (off < 0 || off >= value.length) {
                return println(caption, 
                               "no output: off=" + off + ", array len=" + value.length);
	    }
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.print('{');
                HexDump.dump(value, caption, off, len, out);
                out.println('}');
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するint配列(nullの場合、"null"と出力)
	 */
	public Printer println(String caption, int[] value) {
	    if (value == null) {
                return println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するint配列(nullの場合、"null"と出力)
	 * @param off 出力を開始する配列オフセット
	 * @param len 出力する要素数
	 */
	public Printer println(String caption, int[] value, int off, int len) {
	    if (value == null) {
                return println(caption, "null");
	    } else if (off < 0 || off >= value.length) {
                return println(caption, 
                               "no output: off=" + off + ", array len=" + value.length);
	    }
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		int cnt = (len > (value.length - off)) ? (value.length - off) : len;
		printTime();
		out.print(caption);
		out.print(separator);
		out.print('{');
		for (int i = off; i < cnt; i++) {
		    out.print(value[i]);
		    if ((i + 1) < cnt) {
			out.print(',');
		    }
		}
		out.println("}");
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するlong配列(nullの場合、"null"と出力)
	 */
	public Printer println(String caption, long[] value) {
	    if (value == null) {
                println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するlong配列(nullの場合、"null"と出力)
	 * @param off 出力を開始する配列オフセット
	 * @param len 出力する要素数
	 */
	public Printer println(String caption, long[] value, int off, int len) {
	    if (value == null) {
                return println(caption, "null");
	    } else if (off < 0 || off >= value.length) {
                return println(caption, 
                               "no output: off=" + off + ", array len=" + value.length);
	    }
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		int cnt = (len > (value.length - off)) ? (value.length - off) : len;
		printTime();
		out.print(caption);
		out.print(separator);
		out.print('{');
		for (int i = off; i < cnt; i++) {
		    out.print(value[i]);
		    if ((i + 1) < cnt) {
			out.print(',');
		    }
		}
		out.println("}");
	    }
	    return this;
	}

	/**
	 * 指定された値を出力し、改行する。
	 * @param caption 出力するキャプション
	 * @param value 出力するObject値(nullの場合、nullと出力される)
	 */
	public Printer println(String caption, Object value) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
		printTime();
		out.print(caption);
		out.print(separator);
		out.println(value);
	    }
	    return this;
	}

	/**
	 * 文字列を出力する。<br>
	 * CharBufferの場合は現在のポジションを保存した上で、0から現在のポジションまでを
	 * 出力し、メソッド退出時に元のポジションを復元する。<br>
	 * 当メソッドを使用した書き込みでは、末尾改行、時刻印字のいずれも行われない。
	 * @param data 出力する文字列(nullの場合、"<null>"と出力)
	 */
	public Printer write(CharSequence data) {
            PrintWriter out = getOut();
	    if (out != null && debugLevel > METHOD_ONLY) {
                if (data == null) {
                    out.write("<null>");
                } else {
                    out.write(data.toString());
                }
	    }
	    return this;
	}

	/**
	 * このストリームをクローズする。
	 */
	public void close() {
	    flush();
	}

	/**
	 * バッファをフラッシュする。
	 */
	public void flush() {
            PrintWriter out = getOut();
	    if (out != null) {
		out.flush();
	    }
	}

    }

    /**
     * アプリケーション名からプリンターオブジェクトを返送する。<br>
     * まだ該当アプリケーション用プリンターが存在しなければ新たに作成する。
     */
    protected Printer getPrinter(String name) {
	Printer p = (Printer)printers.get(name);
	if (p == null) {
	    p = new Printer(name);
	    synchronized (printers) {
		printers.put(name, p);
	    }
	}
	return p;
    }

    /**
     * トレース出力用のPrinterオブジェクトを返送する。<br>
     * Printerオブジェクトは作成時点では非時刻印字モードに設定されている。<br>
     * 引数としてバナー出力に使用されるアプリケーションのIDを指定しなければならない。<br>
     * なお同一アプリケーションからの同一ファイルに対する要求については常に同一のプリンターを
     * 返送する。
     * @param name アプリケーション名
     * @return トレース出力用オブジェクトのインスタンス
     * @throws NullPointerException nameパラメータがnull
     */
    public Printer createPrinter(String name) {
	return getPrinter(name);
    }

    /**
     * トレース出力用のPrinterオブジェクトを返送する。<br>
     * Printerオブジェクトは作成時点では非時刻印字モードに設定されている。<br>
     * 引数としてバナー出力に使用されるアプリケーションを指定しなければならない。
     * @param klass アプリケーション
     * @return トレース出力用オブジェクトのインスタンス
     * @throws NullPointerException nameパラメータがnull
     */
    public Printer createPrinter(Class klass) {
	String s = klass.getName();
	if (s.length() > 1) {
	    int n = s.lastIndexOf('.');
	    if (n > 0) {
		return getPrinter(s.substring(n + 1));
	    }
	}
	return getPrinter(s);
    }
}
