// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: UniqueName.java,v $
// Revision 1.1  2005/07/19 03:07:02  art
// �B�ꖼ�������[�e�B���e�B
//
// $Id: UniqueName.java,v 1.1 2005/07/19 03:07:02 art Exp $
//
package ncr.realgate.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Random;

/**
 * �t�@�C�����̖����ɕt�����镶����ȂǗB�ꐫ����������𐶐����郆�[�e�B���e�B�B
 *
 * @version $Revision: 1.1 $ $Date: 2005/07/19 03:07:02 $
 */
public class UniqueName {

    static final Random SEED = new Random();

    /**
     * ���݂̃X���b�h�������Ƀt�@�C�����Ƃ��ė��p�\�ȕ�����𐶐�����B
     * <br>
     * �X���b�h�����擾�o����ꍇ�͂���𗘗p���邽�߁A����X���b�h����̌Ăяo���ɂ͏�ɓ���̕�����
     * �ԑ�����邪�A���p�ł��Ȃ��ꍇ�ɂ͗�����s�x�������邽�߁A����X���b�h����ł����Ă��Ăяo���̓s�x
     * �قȂ镶���񂪕ԑ�����邱�Ƃɒ��ӂ��K�v�ł���B
     *
     * @return �X���b�h�P�ʂɌŗL�ƂȂ蓾��t�@�C���p���ʎq<br>
     *         �X���b�h�������݂��Ȃ��ꍇ��5���̐��l
     * @throws UnsupportedOperationException UTF-8�G���R�[�f�B���O�����p�s�\
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
