// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Nibble.java,v $
// Revision 1.3  2003/08/04 02:43:12  art
// Javadoc�L�q���C��
//
// Revision 1.2  2003/04/10 07:46:06  art
// �ݒ�n���\�b�h��ǉ�
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: Nibble.java,v 1.3 2003/08/04 02:43:12 art Exp $
//
package ncr.realgate.util;

/**
 * 4�r�b�g�P�ʂŃo�C�g���������[�e�B���e�B�B<br>
 * ���ׂẴ��\�b�h�̓X�^�e�B�b�N�ł���B
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
     * �o�C�g�z����j�u���P�ʂŕ����񉻂���B
     * 
     * @param data �����Ώۂ̃o�C�g�z��
     * @return �A���p�b�N���ꂽ������
     * @throws NullPointerException data�p�����[�^��null
     */
    public static final String toString(byte[] data) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toString");
	}
	return toString(data, 0, data.length);
    }

    /**
     * �o�C�g�z����j�u���P�ʂŕ����񉻂���B<br>
     * ���̃��\�b�h�Ɍ���Aoff�p�����[�^��len�p�����[�^�̍��v���z��̏�����z���Ă��Ă�
     * �G���[�Ƃ����ɁA�z��̏���܂ŏ������s���B
     * 
     * @param data �����Ώۂ̃o�C�g�z��
     * @param off �z��̃I�t�Z�b�g
     * @param len ��������v�f��
     * @return �A���p�b�N���ꂽ������
     * @throws NullPointerException data�p�����[�^��null
     * @throws IndexOutOfBoundsException off�p�����[�^��0�����܂��͗v�f���ȏ�
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
     * 16�i����������o�C�g�z��ɕϊ�����B�ϊ��s�\�ȕ�����0�Ƃ��Ĉ����B<br>
     * ����̏ꍇ�́A�擪��0��₤�B
     * 
     * @param data ������
     * @return �ϊ���o�C�g�z��
     * @throws NullPointerException data�p�����[�^��null
     */
    public static final byte[] toBytes(CharSequence data) {
	if (data == null) {
	    throw new NullPointerException("Nibble.toBytes");
	}
	return toBytes(data, 0, data.length());
    }

    /**
     * 16�i����������o�C�g�z��ɕϊ�����B�ϊ��s�\�ȕ�����0�Ƃ��Ĉ����B<br>
     * ����̏ꍇ�́A�擪��0��₤�B
     * 
     * @param data ������
     * @param off �������J�n����0�x�[�X�̃C���f�b�N�X
     * @param len ��������v�f��
     * @return �ϊ���o�C�g�z��
     * @throws NullPointerException data�p�����[�^��null
     * @throws IndexOutOfBoundsException off�p�����[�^��0�����܂��͕������ȏ�A
     *         �܂���off�p�����[�^��len�p�����[�^�̍��v�������񒷂��z���Ă���
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
     * �w�肵�����l���w�肵��������2�i��10�i���ɕϊ�����B<br>
     * ��) toByte(100, 4) -> 0x00000100
     *     toByte(12345, 2) -> 0x2345 (�E�l)
     * @param data �ϊ��Ώۂ̐��l
     * @param len �ϊ���̃o�C�g�z��̒���(�o�C�g��)
     * @return �ϊ���o�C�g�z��
     */
    public static byte[] toBytes(long data, int len) {
	// DecimalFormat�͒x���̂Œ��ڏ�������B
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
     * BCD������̃G���f�B�A����ϊ�����B
     * 
     * @param sb �ϊ��Ώۂ̕�����
     * @param src �ϊ��J�n�ʒu(�����Ŏw�肵���ʒu����2�������ΏۂƂȂ�)
     * @param dst �ϊ��Ώۈʒu
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
     * H�^�C�v(16�i���R�[�h)�𐮐��ɕϊ�����B
     * 
     * @param dat H�^�C�v�Ŋi�[���ꂽ�f�[�^
     * @param offset H�^�C�v�̐擪�o�C�g�ւ̃I�t�Z�b�g
     * @param len H�^�C�v�f�[�^�̒���
     * @return �ϊ���̐����l
     */
    public static int toHex(byte[] dat, int offset, int len) {
	return toInt(dat, offset, len, 16);
    }

    /**
     * K�^�C�v(2�i��10�i��)�𐮐��ɕϊ�����B
     * 
     * @param dat K�^�C�v�Ŋi�[���ꂽ�f�[�^
     * @param offset K�^�C�v�̐擪�o�C�g�ւ̃I�t�Z�b�g
     * @param len K�^�C�v�f�[�^�̒���(�o�C�g��)
     * @return �ϊ���̐����l(32�r�b�g)
     */
    public static int toInt(byte[] dat, int offset, int len) {
	return toInt(dat, offset, len, 10);
    }

    /**
     * K�^�C�v(2�i��10�i��)�𐮐��ɕϊ�����B
     * 
     * @param dat K�^�C�v�Ŋi�[���ꂽ�f�[�^
     * @param offset K�^�C�v�̐擪�o�C�g�ւ̃I�t�Z�b�g
     * @param len K�^�C�v�f�[�^�̒���(�o�C�g��)
     * @return �ϊ���̐����l(64�r�b�g)
     */
    public static long toLong(byte[] dat, int offset, int len) {
	return toLong(dat, offset, len, 10);
    }

    /**
     * 2�o�C�g��H�^�C�v(16�i���R�[�h-�r�b�O�G���f�B�A��)�𐮐��ɕϊ�����B
     * 
     * @param dat H�^�C�v�Ŋi�[���ꂽ�f�[�^
     * @param offset H�^�C�v�̐擪�o�C�g�ւ̃I�t�Z�b�g
     * @return �ϊ���̐����l
     */
    public static int toHex(byte[] dat, int offset) {
	return toInt(dat, offset, 2, 16);
    }

    /**
     * 2�o�C�g��K�^�C�v(2�i��10�i��)�𐮐��ɕϊ�����B
     * 
     * @param dat K�^�C�v�Ŋi�[���ꂽ�f�[�^
     * @param offset K�^�C�v�̐擪�o�C�g�ւ̃I�t�Z�b�g
     * @return �ϊ���̐����l
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
     * �o�C�g�z��ɖ��ߍ��܂ꂽ���g���G���f�B�A��32�r�b�g�f�[�^����int�����o���B
     *
     * @param data �f�[�^���܂񂾃o�C�g�z��
     * @param offset ���g���G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
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
     * �o�C�g�z��ɖ��ߍ��܂ꂽ���g���G���f�B�A��16�r�b�g�f�[�^����int�����o���B
     *
     * @param data �f�[�^���܂񂾃o�C�g�z��
     * @param offset ���g���G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static int toIntFromLS(byte[] data, int offset) {
	int n = data[offset + 1] & 0xff;
	n <<= 8;
	n += (data[offset] & 0xff);
	return n;
    }

    /**
     * �w�肳�ꂽ���l�����g���G���f�B�A��32�r�b�g�Ƃ��Ĕz��ɐݒ肷��B
     * @param value �ݒ肷��l
     * @param data �ݒ��o�C�g�z��
     * @param offset ���g���G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static void setIntToL(int value, byte[] data, int offset) {
	for (int i = 0; i < 4; i++) {
	    data[i + offset] = (byte)(value >> (8 * i));
	}
    }

    /**
     * �w�肳�ꂽ���l�����g���G���f�B�A��16�r�b�g�Ƃ��Ĕz��ɐݒ肷��B
     * @param value �ݒ肷��l
     * @param data �ݒ��o�C�g�z��
     * @param offset ���g���G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static void setIntToLS(int value, byte[] data, int offset) {
	data[offset] = (byte)value;
	data[offset + 1] = (byte)(value >> 8);
    }

    /**
     * �w�肳�ꂽ���l���r�b�O�G���f�B�A��32�r�b�g�Ƃ��Ĕz��ɐݒ肷��B
     * @param value �ݒ肷��l
     * @param data �ݒ��o�C�g�z��
     * @param offset �r�b�O�G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static void setIntToB(int value, byte[] data, int offset) {
	for (int i = 0; i < 4; i++) {
	    data[i + offset] = (byte)(value >> (24 - 8 * i));
	}
    }

    /**
     * �w�肳�ꂽ���l���r�b�O�G���f�B�A��16�r�b�g�Ƃ��Ĕz��ɐݒ肷��B
     * @param value �ݒ肷��l
     * @param data �ݒ��o�C�g�z��
     * @param offset �r�b�O�G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static void setIntToBS(int value, byte[] data, int offset) {
	data[offset] = (byte)(value >> 8);
	data[offset + 1] = (byte)value;
    }

    /**
     * �o�C�g�z��ɖ��ߍ��܂ꂽ�r�b�O�G���f�B�A��32�r�b�g�f�[�^����int�����o���B
     *
     * @param data �f�[�^���܂񂾃o�C�g�z��
     * @param offset �r�b�O�G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
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
     * �o�C�g�z��ɖ��ߍ��܂ꂽ�r�b�O�G���f�B�A��16�r�b�g�f�[�^����int�����o���B
     *
     * @param data �f�[�^���܂񂾃o�C�g�z��
     * @param offset �r�b�O�G���f�B�A���f�[�^�̊J�n�I�t�Z�b�g
     */
    public static int toIntFromBS(byte[] data, int offset) {
	int n = data[offset] & 0xff;
	n <<= 8;
	n += (data[offset + 1] & 0xff);
	return n;
    }
}

