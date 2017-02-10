// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: UniqueName.java,v $
// Revision 1.1  2005/07/19 03:07:02  art
// 唯一名生成ユーティリティ
//
// $Id: UniqueName.java,v 1.1 2005/07/19 03:07:02 art Exp $
//
package ncr.realgate.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * ファイル名の末尾に付加する文字列など唯一性を持つ文字列を生成するユーティリティ。
 *
 * @version $Revision: 1.1 $ $Date: 2005/07/19 03:07:02 $
 */
public class UniqueName {

    static final Random SEED = new Random();

    /**
     * 現在のスレッド名を元にファイル名として利用可能な文字列を生成する。
     * <br>
     * スレッド名が取得出来る場合はそれを利用するため、同一スレッドからの呼び出しには常に同一の文字列が
     * 返送されるが、利用できない場合には乱数を都度生成するため、同一スレッドからであっても呼び出しの都度
     * 異なる文字列が返送されることに注意が必要である。
     *
     * @return スレッド単位に固有となり得るファイル用識別子<br>
     *         スレッド名が存在しない場合は5桁の数値
     * @throws UnsupportedOperationException UTF-8エンコーディングが利用不可能
     */
    public static String newThreadName() {
        String s = Thread.currentThread().getName();
        if (s == null || s.length() == 0) {
            StringBuffer sb = new StringBuffer(String.valueOf(SEED.nextInt(100000)));
            for (int i = sb.length(); i < 5; i++) {
                sb.insert(0, '0');
            }
            s = sb.toString();
        } else {
            try {
                s = URLEncoder.encode(s, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                UnsupportedOperationException us 
                    = new UnsupportedOperationException("can't encode to utf-8");
                us.initCause(e);
                throw us;
            }
        }
        return s;
    }

    UniqueName() {
    }
}
