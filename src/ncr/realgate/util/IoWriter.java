// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: IoWriter.java,v $
// Revision 1.12  2010/09/09 01:48:21  hiroshi
//
// マルチスレッド環境で、
// IOWLOGが崩れる不具合（スレッドセーフでない部分）を修正。
//
// Revision 1.11  2005/07/05 05:19:57  kudo
// IOWLOGと別ファイルに同等フォーマットでの出力を可能とするUserLogを作成
//
// Revision 1.10  2005/07/05 04:45:58  art
// 継承クラスを作成可能なようにコンストラクタを整理、IO実行部のアクセス指定を変更
//
// Revision 1.9  2005/03/01 07:14:13  art
// 無引数コンストラクタで生成したオブジェクトでも書き込みを可能なように修正。
//
// Revision 1.8  2005/03/01 07:02:10  art
// サーバIDの初期値を設定することで途中で例外を起こさないように修正。
//
// Revision 1.7  2005/01/20 08:16:50  art
// デフォルトコンストラクタとプロパティ設定によってインスタンスを初期化可能に変更。
//
// Revision 1.6  2003/06/20 03:18:53  art
// 同一JVM内ではLockFileが共用されることがわかったので、同期処理を併用するように修正した。
//
// Revision 1.5  2003/06/20 02:23:31  art
// ロック待ちを行った場合の、ファイルサイズの再取得処理を修正。
//
// Revision 1.4  2003/06/17 03:47:20  art
// IOWLOGロック後、書き込み位置を再取得するように修正
//
// Revision 1.3  2003/03/27 09:11:31  eda
// 店舗サーバーからのIOW出力用メソッドwriteの追加
//
// Revision 1.2  2003/03/19 07:56:59  hiroshi
//
// IOWLOGの継続行を表す’－’を１カラム目から２カラム目に変更。
// ログのレベル表示を継続行にも表示するように変更。
// スナップ情報の開始カラムを１カラム目から２カラム目に変更。
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// ディレクトリ構成がおかしかったので修正。ついでにJavadocのpackage名も修正。
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: IoWriter.java,v 1.12 2010/09/09 01:48:21 hiroshi Exp $
//
package ncr.realgate.util;

import java.io.*;
import java.nio.channels.*;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * IOライターへのメッセージ出力を行うためにQueueインターフェイスを行う。<br>
 * メッセージはシステム共通のIOWLOGファイルに出力される。
 * 当クラスはバッチプログラムから直接インスタンスを作成して使用するか、あるいは
 * AppContextインターフェイスから間接的に使用する。<br>
 * レコードフォーマット
 * <table border="1">
 * <tr><hd>項目</hd><hd>長さ</hd><hd>備考</hd></tr>
 * <tr><td>レベル</td><td>1</td><td>エラーレベル</td></tr>
 * <tr><td>-</td><td>1</td><td>空白(継続ならー)/td></tr>
 * <tr><td>サーバID</td><td>4</td><td>アプリケーションID</td></tr>
 * <tr><td>-</td><td>1</td><td>空白</td></tr>
 * <tr><td>時刻</td><td>8</td><td>hh:mm:ss</td></tr>
 * <tr><td>-</td><td>1</td><td>空白</td></tr>
 * <tr><td>プログラムID</td><td>8</td><td></td></tr>
 * <tr><td>-</td><td>3</td><td>空白</td></tr>
 * <tr><td>エラーID</td><td>2</td><td>エラー識別子</td></tr>
 * <tr><td>セパレータ</td><td>1</td><td>1行目は:、継続行は空白</td></tr>
 * <tr><td>データ</td><td>0～49</td><td>可変長(シフトJIS)</td></tr>
 * <tr><td>改行</td><td>1</td><td>LF</td></tr>
 * </table>
 *
 * @see ncr.realgate.platform.AppContext
 *
 * @author NCR japan Ltd.
 * @version $Revision: 1.12 $ $Date: 2010/09/09 01:48:21 $
 */
public class IoWriter {

    private byte[] serverID;
    private final byte[] LFS = { (byte)'\n' };
    /**
     * IOライターログファイルのファイル名: {@value}
     */
    public static final String IOWLOG_FILE = "IOWLOG";

    /**
     * 1回の呼び出しで書き込み可能な最大行数({@value})
     */
    public static final int MAX_LINES = 10;

    /**
     * FileChannelの最大許容バッファ数(システム依存なので調整する必要がある。)
     */
    private static final int MAX_BUFFS = 10;

    private String pathName;

    /**
     * 派生クラス用、フィールドの初期化のみを実行するコンストラクタ。
     * すべてのコンストラクタは当メソッドを呼び出す必要がある。
     * @param dummy メソッドシグネチャ変更用のダミーパラメータ。内容は無視する。
     */
    protected IoWriter(boolean dummy) {
        serverID = new byte[] { (byte)' ', (byte)' ', (byte)' ', (byte)' ',};
    }
    
    protected byte[] newContLine() {
        byte[] contLine = new byte[30];
        Arrays.fill(contLine, (byte)' ');
        contLine[1] = (byte)'-';
        return contLine;
    }

    /**
     * 空のIOライタオブジェクトを作成する。<br>
     * 最初の書き込み前に{@link #setPathName}メソッドを呼び出して書き込みディレクトリを
     * 指定しなければならない。また、ログにサーバIDが必要ならば{@link #setServerID}を
     * 呼び出してサーバIDを設定しなけれならない。
     * @see #setPathName
     * @see #setServerID
     */
    public IoWriter() {
        this(System.getProperty("java.io.tmpdir"));
    }

    /**
     * ログに出力するサーバIDを設定する。
     * @param id 設定するサーバID。4桁の16進文字列でなければならない。
     * @throws IllegalArgumentException 4桁の16進文字列でない。
     */
    public void setServerID(String id) {
        if (id == null || id.length() != 4) {
            throw new IllegalArgumentException("ServerID=" + id);
        }
        serverID = id.getBytes();
    }

    /**
     * 出力先ディレクトリを設定する。
     * @param path 出力先ディレクトリのパス名。
     * @throws IllegalArgumentException pathがディレクトリでは無いか書き込み不可
     */
    public void setPathName(String path) {
        setupFile(path);
    }

    /**
     * 書き込みに使用するディレクトリを指定し、IOライタオブジェクトを作成する。<br>
     * 書き込み不可能であったり存在しないディレクトリが指定された場合、例外をスローする。<br>
     * 当コンストラクタを使用した場合、サーバIDは設定されない。これは当コンストラクタがデバッグ用
     * だからである。
     * @param path IOWLOGが存在するディレクトリ
     * @throws IllegalArgumentException pathがディレクトリでは無いか書き込み不可
     * @see #IoWriter(CharSequence, String)
     */
    public IoWriter(CharSequence path) {
        this(false);
        setupFile(path);
    }

    /**
     * 書き込みに使用するディレクトリを指定し、IOライタオブジェクトを作成する。<br>
     * 書き込み不可能であったり存在しないディレクトリが指定された場合、例外をスローする。
     * @param path IOWLOGが存在するディレクトリ
     * @param initServerID 表示に使用するサーバID
     * @throws IllegalArgumentException pathがディレクトリでは無いか書き込み不可、
     *                                  またはサーバIDが4文字では無い。
     */
    public IoWriter(CharSequence path, String initServerID) {
        this(path);
        setServerID(initServerID);
    }

    protected String getFileName() {
        return IOWLOG_FILE;
    }

    private void setupFile(CharSequence path) {
        File file = new File(path.toString());
        if (!file.isDirectory()) {
            throw new IllegalArgumentException(path + " is not a directory");
        } else if (!file.canWrite()) {
            throw new IllegalArgumentException(path + " not writable");
        }

        StringBuffer sb = new StringBuffer(path.toString());
        if (sb.length() > 0 
                && sb.charAt(sb.length() - 1) != File.separatorChar) {
            sb.append(File.separatorChar);
        }
        sb.append(getFileName());
        pathName = sb.toString();
        file = new File(pathName);
        if (file.exists()) {
            if (!file.canWrite()) {
                throw new IllegalArgumentException(pathName + " not writable");
            }
        }
    }

    /**
     * 実際のIOライターへの出力は行わず、IOWLOGファイルのみに書き込みを行う({@value})。
     * @see #write
     */
    public static final char LOG = 'L';
    /**
     * 障害発生時に通常のエラー出力を行う({@value})。
     * @see #write
     */
    public static final char ERROR = ' ';
    /**
     * パトライト操作を含む。システム運行に影響する渋滞な障害検出時に指定する({@value})。
     * @see #write
     */
    public static final char ALERT = '#';
    /**
     * 警告情報付きで出力を行う。障害とは断定できないが調査が必要な場合に指定する({@value})。
     * @see #write
     */
    public static final char WARNING = 'W';
    /**
     * スナップ情報行の開始マーク。アプリケーションは指定してはならない({@value})。
     */
    public static final char SNAP_LINE = ';';

    /**
     * IOライターのメッセージの最大桁数を示す値（{@value}）
     */
    public static final int WIDTH = 49;

    /**
     * levelで指定された方法によってIOWLOGおよびIOライターへメッセージを出力する。<br>
     * 出力するメッセージに関連してスナップを採取した場合は、SnapInfo配列を引数に取る
     * メソッドを使用しなければならない。<br>
     * メッセージは1行あたり49桁で、最大MAX_LINES行を'\n'で区切って指定する。<br>
     * 処理開始後に発生したすべての例外は無視するため、引数が正当である限り、呼び出し元に例外は
     * 通知されない。
     *
     * @param level 出力方法を指定する
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。
     * @throws NullPointerException name, id, msgのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     */
    public void write(char level, String name, String id, CharSequence msg) {
        write(level, name, id, msg, new Snap.SnapInfo[0]);
    }

    /**
     * levelで指定された方法によってIOWLOGおよびIOライターへメッセージを出力する。<br>
     * メッセージは1行あたり49桁で、最大MAX_LINES行を'\n'で区切って指定する。<br>
     * 処理開始後に発生したすべての例外は無視するため、引数が正当である限り、呼び出し元に例外は
     * 通知されない。<br>
     *
     * @param level 出力方法を指定する
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。
     * @param snaps メッセージに関連するスナップ情報を格納した配列
     * @throws NullPointerException name, id, msg, snapsのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     */
    public void write(char level, String name, String id, CharSequence msg,
              Snap.SnapInfo[] snaps) {
        if (name == null || id == null || msg == null || snaps == null) {
            throw new NullPointerException("IoWriter:write");
        }
        java.util.regex.Pattern ptn = java.util.regex.Pattern.compile("\n");
        String[] as = ptn.split(msg);
        ArrayList al = new ArrayList();
        for (int i = 0, n = as.length; i < n; i++) {
            if (HalfWidth.getWidth(as[i]) > WIDTH) {
                al.addAll(Arrays.asList(HalfWidth.split(as[i], WIDTH)));
            } else {
                al.add(as[i]);
            }
        }
        write(level, name, id, 
              (String[])al.toArray(new String[0]), snaps);
    }

    /**
     * levelで指定された方法によってIOWLOGおよびIOライターへメッセージを出力する。<br>
     * 出力するメッセージに関連してスナップを採取した場合は、SnapInfo配列を引数に取る
     * メソッドを使用しなければならない。<br>
     * 当メソッドは、HalfWidthクラスのsplitメソッドと組み合わせて使用する。<br>
     * 例）
     * <pre>
     * CharBuffer buff = CharBuffer.allocate(1000);
     * buff.put(message);
     * buff.put(data);
     * buff.put(message-2);
     * iow.write(IoWriter.ERROR, appName, id, 
     *     HalfWidth.split((CharSequence)buff.positon(0), IoWriter.WIDTH));
     * </pre>
     *
     * @param level 出力方法を指定する
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。<br>
     *            配列の要素数がMAX_LINESを越える場合は10を越えた分は出力しない。
     *            配列要素が0の場合は何も出力しない。
     * @throws NullPointerException name, id, msgのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     * @see ncr.realgate.util.HalfWidth#split
     */
    public void write(char level, String name, String id, String[] msg) {
        write(level, name, id, msg, new Snap.SnapInfo[0]);
    }

    /**
     * levelで指定された方法によってIOWLOGおよびIOライターへメッセージを出力する。<br>
     * 当メソッドは、HalfWidthクラスのsplitメソッドと組み合わせて使用する。<br>
     * 例）
     * <pre>
     * CharBuffer buff = CharBuffer.allocate(1000);
     * buff.put(message);
     * buff.put(data);
     * buff.put(message-2);
     * iow.write(IoWriter.ERROR, appName, id, 
     *     HalfWidth.split((CharSequence)buff.positon(0), IoWriter.WIDTH),
     *     new Snap.SnapInfo[] {
     *         snap.write("bad transaction", tx.toString()),
     *         snap.write("got exception", excep),
     *     });
     * </pre>
     *
     * @param level 出力方法を指定する
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。<br>
     *            配列の要素数がMAX_LINESを越える場合は10を越えた分は出力しない。
     *            配列要素が0の場合は何も出力しない。
     * @param snaps メッセージに関連するスナップ情報を格納した配列
     * @throws NullPointerException name, id, msg, snapsのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     * @see ncr.realgate.util.HalfWidth#split
     */
    public void write(char level, String name, String id, String[] msg,
            Snap.SnapInfo[] snaps) {
        byte[] contLine = newContLine();
        if (name == null || id == null || msg == null || snaps == null) {
            throw new NullPointerException("IoWriter:write");
        }
        int max = (msg.length > MAX_LINES) ? MAX_LINES : msg.length;
        if (max == 0) {
            return;
        }
        for (int i = 0; i < max; i++) {
            if (msg[i] == null) {
                throw new NullPointerException("IoWriter:write index " + i);
            }
        }
        byte[] hd = createHeader(level, name, id);
        ByteBuffer[] buffs = new ByteBuffer[max * 3 + countSnap(snaps)];
        buffs[0] = ByteBuffer.wrap(hd);
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                contLine[0] = (byte)level;
                buffs[i * 3] = ByteBuffer.wrap(contLine);
            }
            buffs[i * 3 + 1] = ByteBuffer.wrap(msg[i].getBytes());
            buffs[i * 3 + 2] = ByteBuffer.wrap(LFS);
        }
        setSnapInfo(buffs, max * 3, snaps);
        writeBuffers(buffs);
    }


    private byte[] createHeader(char level, String name, String id) {
        byte[] hd = new byte[30];
        byte[] svrid = serverID;
        Arrays.fill(hd, (byte)' ');
        hd[0] = (byte)level;
        for (int i = 0; i < 4; i++) {
            hd[i + 2] = svrid[i];
        }
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        String tm = fmt.format(new java.util.Date());
        for (int i = 0; i < 8; i++) {
            hd[i + 7] = (byte)tm.charAt(i);
        }
        for (int i = 0, n = (name.length() > 8) ? 8 : name.length(); i < n; i++) {
            hd[i + 16] = (byte)name.charAt(i);
        }
        if (id.length() < 2) {
            hd[27] = (byte)'0';
            hd[28] = (byte)Character.toUpperCase(id.charAt(0));
        } else {
            hd[27] = (byte)Character.toUpperCase(id.charAt(0));
            hd[28] = (byte)Character.toUpperCase(id.charAt(1));
        }
        hd[29] = (byte)':';
        return hd;
    }

    private void setSnapInfo(ByteBuffer[] buffs, int offset, Snap.SnapInfo[] snaps) {
        StringBuffer sb = new StringBuffer(80);
        for (int i = 0, n = snaps.length; i < n; i++) {
            if (snaps[i] == null || snaps[i].isEmpty()) {
                continue;
            }
            sb.setLength(0);
            sb.append(' ').append(SNAP_LINE).append(snaps[i].getPathName()).append(',');
            sb.append(snaps[i].getOffset()).append(',');
            sb.append(snaps[i].getComment()).append('\n');
            buffs[offset + i] = ByteBuffer.wrap(sb.toString().getBytes());
        }
    }

    /**
     * 指定されたバッファをIOWLOGに書き出す。
     * 最初にIOWLOGをファイルロックし、IOWLOGをオープン、書き込み、クローズ、アンロックする。
     * @param buffs 出力バッファ
     */
    protected void writeBuffers(ByteBuffer[] buffs) {
        assert pathName != null;

        FileLock lk = null;
        FileOutputStream fo = null;
        try {
            synchronized(pathName) {
                fo = new FileOutputStream(pathName, true);
                lk = fo.getChannel().lock();
                FileChannel fc = lk.channel();
                if (buffs.length > MAX_BUFFS) {
                    int rm = buffs.length % MAX_BUFFS;
                    int last = buffs.length - rm;
                    for (int i = 0; i < last; i += MAX_BUFFS) {
                        fc.write(buffs, i, MAX_BUFFS);
                    }
                    if (rm != 0) {
                        fc.write(buffs, last, rm);
                    }
                } else {
                    fc.write(buffs);
                }
                fc.force(true);
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            if (lk != null) {
                try { lk.release(); } catch (Exception e) {}
            }
            if (fo != null) {
                try { fo.close(); } catch (Exception e) {}
            }
        }
    }

    /**
     * levelで指定された方法、serverIDで指定されたServerID, timeで指定された時刻によって
     * IOWLOGおよびIOライターへメッセージを出力する。<br>
     * このメソッドは、店舗サーバーからのIOWLOGメッセージを出力するための特殊メソッドであり
     * 通常のアプリケーションは使用しない。<br>
     * メッセージは1行あたり49桁で、最大MAX_LINES行を'\n'で区切って指定する。<br>
     * 処理開始後に発生したすべての例外は無視するため、引数が正当である限り、呼び出し元に例外は
     * 通知されない。
     * @param level 出力方法を指定する
     * @param serverID サーバーID4桁を指定する。
     * @param time 時刻をHH:MM:SS形式で指定する。
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。
     * @throws NullPointerException name, id, msgのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     */
    public void write(char level, String serverID, String time, String name, String id, CharSequence msg) {
        if (serverID == null || serverID.length() != 4 || 
                name == null || id == null || msg == null) {
            throw new NullPointerException("IoWriter:write");
        }
            
        if (time == null || time.length() != 8) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
            time = fmt.format(new java.util.Date());
        }
            
        java.util.regex.Pattern ptn = java.util.regex.Pattern.compile("\n");
        String[] as = ptn.split(msg);
        ArrayList al = new ArrayList();
        for (int i = 0, n = as.length; i < n; i++) {
            if (HalfWidth.getWidth(as[i]) > WIDTH) {
                al.addAll(Arrays.asList(HalfWidth.split(as[i], WIDTH)));
            } else {
                al.add(as[i]);
            }
        }
            
        write(level, serverID, time, name, id, 
              (String[])al.toArray(new String[0]), new Snap.SnapInfo[0]);
    }
    
    /**
     * levelで指定された方法、serverIDで指定されたServerID, timeで指定された時刻によって
     * IOWLOGおよびIOライターへメッセージを出力する。<br>
     * このメソッドは、店舗サーバーからのIOWLOGメッセージを出力するための特殊メソッドであり
     * 通常のアプリケーションは使用しない。<br>
     * 当メソッドは、HalfWidthクラスのsplitメソッドと組み合わせて使用する。<br>
     * @param level 出力方法を指定する
     * @param serverID サーバーID4桁を指定する。
     * @param time 時刻をHH:MM:SS形式で指定する。
     * @param name プログラム名8バイトを指定する
     * @param id プログラムID2桁を指定する
     * @param msg 出力メッセージを指定する。<br>
     *            配列の要素数がMAX_LINESを越える場合は10を越えた分は出力しない。
     *            配列要素が0の場合は何も出力しない。
     * @param snaps メッセージに関連するスナップ情報を格納した配列
     * @throws NullPointerException name, id, msg, snapsのいずれかがnull
     * @see #ALERT
     * @see #ERROR
     * @see #WARNING
     * @see #LOG
     * @see #MAX_LINES
     * @see ncr.realgate.util.HalfWidth#split
     */
    public void write(char level, String serverID, String time, String name, String id, String[] msg,
            Snap.SnapInfo[] snaps) {
        byte[] contLine = newContLine();
        if (serverID == null || serverID.length() != 4 || 
                name == null || id == null || msg == null || snaps == null) {
            throw new NullPointerException("IoWriter:write");
        }
            
        if (time == null || time.length() != 8) {
            SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
            time = fmt.format(new java.util.Date());
        }
        int max = (msg.length > MAX_LINES) ? MAX_LINES : msg.length;
        if (max == 0) {
            return;
        }
        for (int i = 0; i < max; i++) {
            if (msg[i] == null) {
                throw new NullPointerException("IoWriter:write index " + i);
            }
        }
        byte[] hd = createHeader(level, serverID, time, name, id);
        ByteBuffer[] buffs = new ByteBuffer[max * 3 + countSnap(snaps)];
        buffs[0] = ByteBuffer.wrap(hd);
        for (int i = 0; i < max; i++) {
            if (i > 0) {
                contLine[0] = (byte)level;
                buffs[i * 3] = ByteBuffer.wrap(contLine);
            }
            buffs[i * 3 + 1] = ByteBuffer.wrap(msg[i].getBytes());
            buffs[i * 3 + 2] = ByteBuffer.wrap(LFS);
        }
        setSnapInfo(buffs, max * 3, snaps);
        writeBuffers(buffs);
    }
    
    private byte[] createHeader(char level, String serverID, String time, String name, String id) {
        byte[] hd = new byte[30];
        Arrays.fill(hd, (byte)' ');
        hd[0] = (byte)level;
        for (int i = 0; i < 4; i++) {
            hd[i + 2] = (byte)serverID.charAt(i);
        }
        
        for (int i = 0; i < 8; i++) {
            hd[i + 7] = (byte)time.charAt(i);
        }
        for (int i = 0, n = (name.length() > 8) ? 8 : name.length(); i < n; i++) {
            hd[i + 16] = (byte)name.charAt(i);
        }
        if (id.length() < 2) {
            hd[27] = (byte)'0';
            hd[28] = (byte)Character.toUpperCase(id.charAt(0));
        } else {
            hd[27] = (byte)Character.toUpperCase(id.charAt(0));
            hd[28] = (byte)Character.toUpperCase(id.charAt(1));
        }
        hd[29] = (byte)':';
        return hd;
    }
    
    
    /**
     * 配列内の有効なSnapの数を返送する。
     */
    private int countSnap(Snap.SnapInfo[] snaps) {
        int n = 0;
        for (int i = 0, m = snaps.length; i < m; i++) {
            if (snaps[i] != null && !snaps[i].isEmpty()) {
                n++;
            }
        }
        return n;
    }
}
