// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: MultiSnap.java,v $
// Revision 1.6  2013/04/02 05:04:45  ushi
// ファイルポインタクローズ漏れ、リソースリーク対応（参照が外れた時にクリーンアップする）。
//
// Revision 1.5  2006/01/12 05:56:09  art
// Snap#closeの処理をMultiSnapは無効化できるように別メソッドへのリダイレクトとした。
//
// Revision 1.4  2005/07/22 03:30:09  art
// スレッド限定クローズを追加
//
// Revision 1.3  2005/07/19 03:10:48  art
// スレッド名のエンコードを実行するように修正
//
// Revision 1.2  2005/07/05 07:11:11  art
// Snapのメソッドシグネチャ修正に追随
//
// Revision 1.1  2005/06/10 04:56:39  art
// マルチスレッドでの利用を前提として単一インスタンスマルチファイルを実装したSnap
//
// $Id: MultiSnap.java,v 1.6 2013/04/02 05:04:45 ushi Exp $
//
package ncr.realgate.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Set;

import ncr.realgate.util.WeakHashSet.GarbageListener;

/**
 * 同一インスタンスでスレッド単位にファイルを作成／管理するSnap。<br>
 * 当クラスを利用して作成したスナップファイルの末尾には区切り文字として#に続けてスレッドIDが付加される。
 * スレッドIDとしては、Thread#getNameメソッドの結果を利用するが、nullの場合には4桁のランダムな数字を
 * 設定する。
 *
 * @version $Revision: 1.6 $ $Date: 2013/04/02 05:04:45 $
 */
public class MultiSnap extends Snap {

    Set allSnaps = newHashSet();
    ThreadLocal snapHolder = new ThreadLocal();
    
    WeakHashSet newHashSet() {
        WeakHashSet set = new WeakHashSet();
        set.setGarbageListener(new GarbageListener () {
            public void garbage(Object obj) {
                if (obj != null && obj instanceof SnapFile) {
                    SnapFile snpf = (SnapFile) obj;
                    snpf.close();
                }
            }
        });
        return set;
    }

    /**
     * 空のインスタンスを生成する。<br>
     * 最初の書き込み前に{@link #setPathName}メソッドを呼び出してスナップファイルを
     * 設定しなければならない。
     */
    public MultiSnap() {
    }

    /**
     * 指定したパス名にスレッドIDを付加したスナップファイルを作成する。既に該当ファイルが存在する場合は、
     * 追記モードでオープンする。<br>
     * スナップファイルのマークであるファイル名先頭の'S'は呼び出し元であらかじめ
     * 設定しておく必要がある。
     * @param pathName スナップを作成するパス名
     */
    public MultiSnap(CharSequence pathName) {
        super();
        setPathName(pathName);
    }

    /**
     * 指定したパス名にスレッドIDを付加したスナップファイルを作成する。既に該当ファイルが存在する場合は、
     * 追記モードでオープンする。ファイル名先頭の'S'は当コンストラクタ内で生成する。
     * @param path スナップを作成するディレクトリ名(末尾'/'は不要)
     * @param name 12文字以内でファイル名を指定する。ただし当メソッドで長さのチェックは行わない。
     */
    public MultiSnap(CharSequence path, CharSequence name) {
        this(createPathName(path, name));
    }

    class SnapFile {
        String name;
        FileOutputStream out;
        SnapFile(FileOutputStream o, CharSequence s) {
            out = o;
            name = s.toString();
            synchronized (allSnaps) {
                allSnaps.add(this);
            }
            snapHolder.set(this);
        }
        void close() {
            try {
                out.close();
                File f = new File(name);
                if (f.exists() && f.length() == 0) {
                    f.delete();
                }
            } catch (IOException e) {
                // スナップの書き込みがエラーになった場合はスナップが取れないので何もしない。
            }
            // close時呼び出し時にIteratorを利用して一括closeを行うため、HashSetに対する
            // 操作は行わない。
            snapHolder.set(null);
        }
        public int hashCode() {
            return name.hashCode();
        }
        public boolean equals(Object o) {
            if (o instanceof SnapFile) {
                return ((SnapFile)o).name.equals(name);
            }
            return false;
        }
    }

    protected OutputStream getOut() {
        if (getInternalPathName() == null) {
            return null;
        }
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf == null) {
            setPathName(getInternalPathName());
            sf = (SnapFile)snapHolder.get();
        }
        return sf.out;
    }

    protected void setOut(OutputStream o, CharSequence s) {
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf != null) {
            sf.close();
            synchronized (allSnaps) {
                allSnaps.remove(sf);
            }
        }
        if (o != null) {
            assert s != null : "no name, but valid stream";
            newSnapFile((FileOutputStream)o, s);
        }
    }
    
    protected SnapFile newSnapFile(FileOutputStream fs, CharSequence s) {
        return new SnapFile(fs, s);
    }

    protected void internalClose() {
        // no-op
    }

    /**
     * 現在オープンされているすべてのSnapファイルをクローズする。
     */
    public void close() {
        synchronized (allSnaps) {
            for (Iterator i = allSnaps.iterator(); i.hasNext();) {
                SnapFile sf = (SnapFile)i.next();
                sf.close();
            }
            allSnaps.clear();
        }
    }

    /**
     * このスレッドが利用しているSnapファイルのみをクローズする。
     */
    public void closeThis() {
        super.close();
    }

    /**
     * このスレッドが使用するスナップのフルパス名を返送する。
     * @return スナップファイルのフルパス名
     */
    public String getPathName() {
        SnapFile sf = (SnapFile)snapHolder.get();
        if (sf != null) {
            return sf.name;
        }
        return "";
    }

    public void setPathName(CharSequence newPathName) {
        setInternalPathName(newPathName);
        super.setPathName(newPathName + "#" + UniqueName.newThreadName());
    }

}

