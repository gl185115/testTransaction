// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Snap.java,v $
// Revision 1.10  2008/09/26 07:06:59  art
// for test on win32
//
// Revision 1.9  2006/01/12 05:56:09  art
// Snap#closeの処理をMultiSnapは無効化できるように別メソッドへのリダイレクトとした。
//
// Revision 1.8  2005/07/05 07:10:52  art
// 出力先をFileOutputStreamから単なるOutputStreamに修正(StdOutSnap用)
// IO実行部をオーバーライド可能に修正
//
// Revision 1.7  2005/06/10 06:32:28  art
// ダンプ機能を追加
//
// Revision 1.6  2005/06/10 04:55:41  art
// 派生クラスがコードを共用できるようにテンプレートなどを作成
//
// Revision 1.5  2005/06/10 02:51:27  art
// MultiSnap(マルチファイルシングルインスタンス)を作成するため、finalクラスから継承可能クラスに変更。
//
// Revision 1.4  2005/01/20 08:16:50  art
// デフォルトコンストラクタとプロパティ設定によってインスタンスを初期化可能に変更。
//
// Revision 1.3  2003/08/04 02:43:12  art
// Javadoc記述を修正
//
// Revision 1.2  2003/06/17 03:46:51  art
// 同一ファイルを複数のスレッドに割り当てたような場合の競合を回避するように修正
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Snap.java,v 1.10 2008/09/26 07:06:59 art Exp $
//
package ncr.realgate.util;

import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.text.*;
import java.util.Date;

/**
 * 6GRSスナップフォーマットのファイルを作成する。<br>
 * アプリケーションが異常状態を検出時に、異常の元となったトランザクション、データ、例外などを
 * 後刻、障害調査可能なように保存するために使用する。
 * <p>
 * キャプチャーファイルは以下のフォーマットを持つ。<br>
 * <table border="1">
 * <tr><th>フィールド名</th><th>型</th><th>長さ</th><th>備考</th></tr>
 * <tr><td>VLI</td><td>int</td><td>4</td><td>リトルエンディアン</td></tr>
 * <tr><td>時刻</td><td>byte[]</td><td>7</td><YYMMDDHHMMSSMM</td></tr>
 * <tr><td>タイプ</td><td>byte</td><td>1</td><td>0:AIW,1:free</td></tr>
 * <tr><td>-</td><td>byte[]</td><td>4</td><td>未使用</td></tr>
 * <tr><td>コメント</td><td>byte[]</td><td>16</td><td>-</td></tr>
 * </table>
 * <br>
 * なお、当クラスを利用する場合、上記のタイプフィールド値は常に1となる。また、VLIはヘッダ部を
 * 含む長さである。
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.10 $ $Date: 2008/09/26 07:06:59 $
 */
public class Snap {

    /**
     * スナップファイルのファイル名の長さの規定値({@value})
     */
    public static final int FILENAME_LENGTH = 12;

    /**
     * スナップファイルのヘッダ部の長さ({@value})
     */
    public static final int HEADER_LENGTH = 32;

    /**
     * AIWフォーマットのスナップレコード({@value})
     */
    public static final byte TYPE_AIW = 0;

    /**
     * フリーフォーマットのスナップレコード({@value})
     */
    public static final byte TYPE_FREE = 1;

    private String pathName;

    /**
     * 当オブジェクトが内部的に使用するファイルストリーム
     */
    private volatile FileOutputStream out;

    /**
     * 書き込みに利用するストリームを取得する。
     * @return スナップファイルのストリーム
     */
    protected OutputStream getOut() {
        return out;
    }

    /**
     * 書き込みに利用するストリームを設定する。
     * @param o スナップファイルのストリーム
     */
    protected void setOut(OutputStream o, CharSequence s) {
        out = (FileOutputStream)o;
        pathName = (s == null) ? null : s.toString();
    }

    /**
     * 空のインスタンスを生成する。<br>
     * 最初の書き込み前に{@link #setPathName}メソッドを呼び出してスナップファイルを
     * 設定しなければならない。
     */
    public Snap() {
    }

    /**
     * 指定したパス名でスナップファイルを作成する。既に該当ファイルが存在する場合は、
     * 追記モードでオープンする。<br>
     * スナップファイルのマークであるファイル名先頭の'S'は呼び出し元であらかじめ
     * 設定しておく必要がある。
     * @param pathName スナップを作成するパス名
     */
    public Snap(CharSequence pathName) {
	setPathName(pathName);
    }

    /**
     * 指定したパス名でスナップファイルを作成する。既に該当ファイルが存在する場合は、
     * 追記モードでオープンする。ファイル名先頭の'S'は当コンストラクタ内で生成する。
     * @param path スナップを作成するディレクトリ名(末尾'/'は不要)
     * @param name 12文字以内でファイル名を指定する。ただし当メソッドで長さのチェックは行わない。
     */
    public Snap(CharSequence path, CharSequence name) {
	setPathName(createPathName(path, name));
    }

    /**
     * ディレクトリ名とファイル名からスナップファイル名を作成する。
     *
     * @param path スナップを作成するディレクトリ名(末尾'/'は不要)
     * @param name 12文字以内でファイル名を指定する。ただし当メソッドで長さのチェックは行わない。
     */
    protected static CharSequence createPathName(CharSequence path, CharSequence name) {
	if (path == null) {
	    throw new NullPointerException("path is null");
	} else if (name == null) {
	    throw new NullPointerException("name is null");
	}
	StringBuffer sb = new StringBuffer(path.toString());
	if (sb.length() > 0 
	    && sb.charAt(sb.length() - 1) != File.separatorChar) {
	    sb.append(File.separatorChar);
	}
	sb.append('S');
	sb.append(name);
        return sb;
    }

    /**
     * このスナップのフルパス名を返送する。
     * @return スナップファイルのフルパス名
     */
    public String getPathName() {
	return pathName;
    }

    /**
     * このスナップのフルパス名の不変部を返送する。
     * @return スナップファイルのフルパス名
     */
    protected String getInternalPathName() {
	return pathName;
    }

    /**
     * このスナップのパス名の不変部を設定する。
     * @param s スナップファイルのフルパス名
     */
    protected void setInternalPathName(CharSequence s) {
        pathName = (s == null) ? "" : s.toString();
    }

    /**
     * このスナップにフルパス名を設定する。<br>
     * 現在使用中のスナップファイルが存在する場合は、クローズして新たに指定されたスナップファイルを
     * 使用する。<br>
     * 既に該当ファイルが存在する場合は、追記モードでオープンする。<br>
     * スナップファイルのマークであるファイル名先頭の'S'は呼び出し元であらかじめ設定しておく必要が
     * ある。
     * @param newPathName スナップファイルのフルパス名
     * @throws NullPointerException newPathNameパラメータがnull
     * @throws IllegalArgumentException 指定パス名でファイルが作成できない
     */
    public void setPathName(CharSequence newPathName) {
	if (newPathName == null) {
	    throw new NullPointerException("path is null");
	}
	internalClose();
        String p = newPathName.toString();
	try {
	    setOut(new FileOutputStream(p, true), p);
	} catch (FileNotFoundException e) {
	    try {
		setOut(new FileOutputStream(p), p);
	    } catch (Exception ex) {
                setOut(null, null);
		throw new IllegalArgumentException("can't open file:" + p);
	    }
	}
    }

    /**
     * 現在のスナップファイルをクローズする。サイズが0の場合は、ファイルを削除する。
     */
    public void close() {
        internalClose();
    }

    /**
     * 現在のスナップファイルをクローズする。サイズが0の場合は、ファイルを削除する。
     */
    protected void internalClose() {
	if (getOut() != null) {
	    try {
		getOut().close();
		File f = new File(getPathName());
		if (f.exists() && f.length() == 0) {
		    f.delete();
		}
	    } catch (IOException ex) {
		; // no-op
	    }
	    setOut(null, null);
	}
    }

    /**
     *  指定バッファの内容を書き出す（この時、ファイルをロックする）
     * ここでエラーが出てもどうにもならないので無視する。
     * @param header スナップヘッダ
     * @param data スナップデータ
     * @param info 出力情報を格納スルオブジェクト
     */
    protected SnapInfo write(ByteBuffer header, ByteBuffer data, SnapInfo info) {
        FileOutputStream o = (FileOutputStream)getOut();
	synchronized (o) {
	    FileChannel fc = o.getChannel();
	    FileLock fl = null;
	    try {
		fl = fc.lock();
		if (info != null) {
		    info.setOffset(fc.size());
		}
		fc.write(new ByteBuffer[] {header, data});
                fc.force(true);
	    } catch (IOException e) {
		System.out.println(e.getMessage());
	    } finally {
		try {
		    if (fl != null) {
			fl.release();
		    }
		} catch (IOException e) {
		    System.out.println(e.getMessage());
		}
	    }
	}
	return info;
    }

    /**
     * 指定メッセージをスナップファイルへ出力する。
     * @param comment 最大16バイトをコメントとして出力する。nullの場合空文字列とみなす
     * @param data 出力するデータ
     * @return スナップ書き込み情報
     * @throws NullPointerException dataがnull
     */
    public SnapInfo write(CharSequence comment, CharSequence data) {
	if (data == null) {
	    throw new NullPointerException("Snap.write data is null");
	}
	if (getOut() != null) {
	    byte[] b = data.toString().getBytes();
	    return write(comment, b, 0, b.length);
	}
	return noSnap;
    }

    /**
     * 指定データをスナップファイルへ出力する。
     * @param comment 最大16バイトをコメントとして出力する。nullの場合空文字列とみなす
     * @param data 出力するデータ
     * @param offset 出力を開始するデータ位置
     * @param length 出力するバイト数
     * @return スナップ書き込み情報
     * @throws NullPointerException dataにnullを渡された。
     * @throws IndexOutOfBoundsException offsetで指定された位置が負または配列より大きい
     */
    public SnapInfo write(CharSequence comment, byte[] data, int offset, int length) {
	if (data == null) {
	    throw new NullPointerException("Snap.write data is null");
	} else if (offset < 0 || offset > data.length) {
	    throw new IndexOutOfBoundsException("Snap.write bad offset");
	}
	if (getOut() != null) {
	    int len = ((offset + length) > data.length) ? data.length - offset
 						        : length;
	    ByteBuffer hd = ByteBuffer.allocateDirect(HEADER_LENGTH);
	    hd.order(ByteOrder.LITTLE_ENDIAN);
	    hd.putInt(len + HEADER_LENGTH);
	    SimpleDateFormat fmt = new SimpleDateFormat("yyMMddHHmmssSS");
	    StringBuffer sb = new StringBuffer(16);
	    fmt.format(new Date(), sb, new FieldPosition(0)).setLength(14);
	    hd.put(Nibble.toBytes(sb));
	    hd.put(TYPE_FREE);
	    hd.putInt(0);
	    String cmt = "*";
	    if (comment == null) {
		for (int i = 0; i < 16; i++) {
		    hd.put((byte)0x20);
		}
	    } else {
		cmt = comment.toString();
		byte[] cm = cmt.getBytes();
		int cb = cm.length;
		if (cb >= 16) {
		    hd.put(cm, 0, 16);
		} else {
		    hd.put(cm);
		    for (; cb < 16; cb++) {
			hd.put((byte)0x20);
		    }
		}
	    }
	    hd.position(0);
	    SnapInfo info = new SnapInfo(getPathName(), cmt);
	    return write(hd, ByteBuffer.wrap(data, offset, len), info);
	}
	return noSnap;
    }

    /**
     * 指定データのオフセット以降すべてをスナップファイルへ出力する。
     * @param comment 最大16バイトをコメントとして出力する。nullの場合空文字列とみなす
     * @param data 出力するデータ
     * @param offset 出力を開始するデータ位置
     * @return スナップ書き込み情報
     * @throws NullPointerException dataにnullを渡された。
     * @throws IndexOutOfBoundsException offsetで指定された位置が負または配列より大きい
     */
    public SnapInfo write(CharSequence comment, byte[] data, int offset) {
	if (data == null) {
	    throw new NullPointerException("Snap.write data is null");
	}
	return write(comment, data, offset, data.length - offset);
    }

    /**
     * 指定例外オブジェクトのスタックトレースをスナップファイルへ出力する。
     * @param comment 最大16バイトをコメントとして出力する。nullの場合空文字列とみなす
     * @param data スタックトレースを出力する例外オブジェクト
     * @return スナップ書き込み情報
     * @throws NullPointerException dataにnullを渡された。
     */
    public SnapInfo write(CharSequence comment, Throwable data) {
	if (data == null) {
	    throw new NullPointerException("Snap.write data is null");
	}
	if (getOut() != null) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream(8192);
	    PrintWriter pw = new PrintWriter(new OutputStreamWriter(ba));
	    data.printStackTrace(pw);
	    pw.flush();
	    pw.close();
	    return write(comment, ba.toByteArray(), 0);
	}
	return noSnap;
    }

    /**
     * オブジェクトの全イメージをスナップファイルへ出力する。
     * @param comment 最大16バイトをコメントとして出力する。nullの場合空文字列とみなす
     * @param data イメージを出力するオブジェクト
     * @return スナップ書き込み情報
     */
    public SnapInfo writeObject(CharSequence comment, Object data) {
        if (getOut() != null) {
            ByteArrayOutputStream ba = new ByteArrayOutputStream(16384);
	    Writer w = new OutputStreamWriter(ba);
            new DumpObject(data).write(w);
            try {
                w.close();
                return write(comment, ba.toByteArray(), 0);
            } catch (IOException e) {
                assert false; // ByteArrayOutputStream never throw the exception
            }
        }
	return noSnap;
    }

    /**
     * スナップ出力時に、出力情報を返送するために使用されるオブジェクト。
     */
    public static class SnapInfo {
	private long offset;
	private String comment;
	private String pathName;
	private SnapInfo(String initPathName, String initComment) {
	    pathName = initPathName;
	    comment = initComment;
	}
	private void setOffset(long newOffset) {
	    offset = newOffset;
	}
	
	/**
	 * 関連したwriteメソッドで書き込みを行ったスナップファイルのパスを取得する。
	 * @return スナップのパス名
	 */
	public String getPathName() {
	    return pathName;
	}
	/**
	 * 関連したwriteメソッドで書き込んだコメントを取得する。<br>
	 * コメントが無指定の場合、*を返送する。
	 * @return コメント文字列
	 */
	public String getComment() {
	    return comment;
	}
	/**
	 * 関連したwriteメソッドで書き込みを行ったファイルオフセットを取得する。
	 * @return スナップファイル内のオフセット
	 */
	public long getOffset() {
	    return offset;
	}
	/**
	 * このインスタンスが無効インスタンスかどうかを判定する。
	 * @return 真ならば無効なインスタンス
	 */
	public boolean isEmpty() {
	    return this == noSnap;
	}
    }

    /**
     * 内容が無い、SnapInfoのインスタンスを返送する。<br>
     * ここで返送されたSnapInfoは情報を持たない。
     * @return 無効なSnapInfo
     */
    public static SnapInfo getEmptyInfo() {
	return noSnap;
    }
    private static SnapInfo noSnap = new SnapInfo("/dev/null", "");
}
