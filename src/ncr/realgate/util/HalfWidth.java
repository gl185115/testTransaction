// Copyright (c) 2002, 2004 NCR Japan Ltd.
//
// $Log: HalfWidth.java,v $
// Revision 1.3  2004/04/06 02:52:17  art
// ���_�A�����_���ӎ������Љ������p/�S�p�ϊ�������ǉ��B
//
// Revision 1.2  2003/01/08 07:57:28  art
// Javadoc�̎Q�Ƃ̋L�q�G���[���C��
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
//
// Revision 1.1  2002/12/20 10:22:39  art
// Initial Release
//
// $Id: HalfWidth.java,v 1.3 2004/04/06 02:52:17 art Exp $
//
package ncr.realgate.util;

/**
 * ���p�������������邽�߂̃��[�e�B���e�B�N���X�B<br>
 * ���ׂẴ��\�b�h�̓X�^�e�B�b�N�ł���B
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.3 $ $Date: 2004/04/06 02:52:17 $
 */
public final class HalfWidth {

    private static final String[] hz_kataCV = {
	"���������������������������������������������������������������",
	"�B�u�v�A�E���@�B�D�F�H�������b�[�A�C�E�G�I�J�L�N�P�R�T�V�X�Z�\"
	  + "�^�`�c�e�g�i�j�k�l�m�n�q�t�w�z�}�~���������������������������J�K",
	"?????????????????????�K�M�O�Q�S�U�W�Y�[�]"
	  + "�_�a�d�f�h?????�o�r�u�x�{?????????????????",
	"???????????????????????????????"
	  + "??????????�p�s�v�y�|?????????????????",
    };

    private static final String voicedMark = "��";

    private static final char ALT_YENMARK = '\u00a5';
    private static final char ALT_TILDE = '\u203e';

    // �C���X�^���X�쐬�֎~
    private HalfWidth() {
    }

    /**
     * �^����ꂽ���������p�������𔻒肷��B
     * @param ch ������s������
     * @return ���p�����ł���ΐ^
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
     * �^����ꂽ�������S�p�������𔻒肷��B
     * @param ch ������s������
     * @return �S�p�����ł���ΐ^
     */
    public static final boolean isFullWidth(char ch) {
	return !isHalfWidth(ch);
    }

    /**
     * �^����ꂽ�����𔼊p����S�p�֕ϊ�����B
     * @param ch ���p�����B���炩���ߑS�p�̏ꍇ�A���̂܂ܕԑ�����B
     * @return �p�����[�^��S�p�����֕ϊ���̕���
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
     * �^����ꂽ�S�p�����𔼊p�����֕ϊ�����B
     * @param ch �S�p�����B�ϊ��s�\�ȕ����͂��̂܂ܖ߂��B
     * @return �p�����[�^�𔼊p�����֕ϊ���̕����i�ϊ��s�\�ȏꍇ�A���̕����j
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
     * �^����ꂽ��������̔��p�Љ�����S�p�Љ����֕ϊ�����B<br>
     * �����͈͂�Unicode��'\uff61'����'\uff9f'�ł���B�܂��A���_�Ɣ����_�͉\�Ȃ��
     * ���O�̕����ƍ�������B����ȊO�̕����͂��̂܂܌��ʂ̕�����փR�s�[����B
     * @param str �S�p�Љ����֕ϊ����镶����
     * @return �S�p�Љ����֕ϊ���̕�����
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
		if (prindex >= 0 && (ch == '�' || ch == '�')) {
		    char voiced = hz_kataCV[ch - '�' + 2].charAt(prindex);
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
     * �^����ꂽ��������̑S�p�Љ����𔼊p�Љ����֕ϊ�����B<br>
     * �����┼������2�����̔��p�Љ����ɕϊ�����B<br>
     * �ϊ���͈̔͂�Unicode��'\uff61'����'\uff9f'�ł���B����ȊO�̕����͂��̂܂܌��ʂ̕�����փR�s�[����B
     * @param str ���p�Љ����֕ϊ����镶����
     * @return ���p�Љ����֕ϊ���̕�����
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
			sb.append(hz_kataCV[0].charAt(index)).append((char)('�' + j));
			continue convert;
		    }
		}
		sb.append(ch);
	    }
	}

	return new String(sb);
    }

    /**
     * �^����ꂽ�����̌�����ԑ�����B���p��1�A�S�p��2�Ƃ���B
     * @param str �����𔻒肷�镶����
     * @return ����
     * @throws NullPointerException ������null�ł���B
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
     * �^�[�Q�b�g�Ƃ��Ďw�肳�ꂽ������̎w�茅�ȍ~�ɁA���ɂȂ镶����𖄂ߍ��ށB<br>
     * �^�[�Q�b�g�̎w�茅����сA���ߍ��݌�̍ŏI�����A�S�p�����̒��ԂƂȂ�ꍇ�́A
     * 1�����̃X�y�[�X�ɂ�薕�������B<br>
     * ��j
     * <pre>
     * set("����������", 3, "����") -> "�� ���� ��"
     * </pre>
     * <br>
     * ���ߍ��ݐ敶����̒����������ɑ���Ȃ��ꍇ�́A�����܂ŋ󔒂ŐL������B<br>
     * ��j
     * <pre>
     * set("����������", 11, "����") -> "���������� ����"
     * </pre>
     * 
     * @param target ���ߍ��ݐ�̕�����(null�̏ꍇ�A�w�茅���̋󔒕�������쐬����)
     * @param col ���ߍ��ݐ�̌��ʒu
     * @param source ���ߍ��ޕ�����
     * @return ��������������
     * @throws NullPointerException ����source��null�ł���B
     * @throws IllegalArgumentException ����col��0�����ł���B
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
     * �^����ꂽ��������w�茅���ŕ�������B<br>
     * �w�茅���S�p�����̒��ԂɂȂ�ꍇ�ɂ́A�ŏ��̕�����ɂ�1�����̃X�y�[�X�𖄂߁A
     * ���������񂩂�Y��������ݒ肷��B<br>
     * �܂��A�Ō�̕�����΂���w�茅���̋󔒖��ߍ��݂͍s��Ȃ��B
     * ��j
     * <pre>
     *  split("����������", 5) -> {"���� ", "���� ", "��" }
     * </pre>
     * @param str �������镶����
     * @param cols �������錅��
     * @return �����ɂ�蕪������������̔z��
     * @throws NullPointerException ����str��null�ł���B
     * @throws IllegalArgumentException ����cols��0�ȉ��ł���B
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
     * �^����ꂽ��������w�茅���ŕ������A�ŏ��̕������ԑ�����B<br>
     * �w�茅���S�p�����̒��ԂɂȂ�ꍇ�ɂ́A1�����̃X�y�[�X�𖄂߂ĕԂ��B
     * ��j
     * <pre>
     *  left("����������", 5, 0) -> "���� "
     * </pre>
     * <br>
     * �����A������蕶���񂪒Z���ꍇ�ɂ́A�������̃X�y�[�X�𖄂߂ĕԂ��B
     * @param str �������镶����
     * @param cols �������錅��(0�̏ꍇ�A�󕶎����ԑ�����B)
     * @return �����ɂ�蕪�������ŏ��̕�����
     * @throws NullPointerException ����str��null�ł���B
     * @throws IllegalArgumentException ����cols��0�����ł���B
     */
    public static final String left(CharSequence str, int cols) {
	return left(str, cols, null);
    }

    /**
     * {@link #left(CharSequence, int, HalfWidth.NextLine)}���\�b�h�Ŏ��s�̊J�n�ʒu��
     * �Ăяo�����ɒʒm���邽�߂ɗ��p����N���X
     *
     * @see #left(CharSequence, int, HalfWidth.NextLine)
     */
    public static class NextLine {
	private int end;
	/**
	 * �����ʒu�𕉒l�ɐݒ肵���V�K�C���X�^���X���쐬����B
	 */
	public NextLine() {
	    end = -1;
	}
	/**
	 * ���s�̊J�n�����ʒu�i���ʒu�ł͂Ȃ��j��0����̐����ŕԑ�����B
	 * ����������ׂď���A���s�������ꍇ�A�܂���
	 * {@link #left(CharSequence, int, HalfWidth.NextLine)}���\�b�h�̌Ăяo���O�A
	 * ���邢�͌Ăяo���Ɏ��s�����ꍇ�ɂ͕��l��ԑ�����B
	 */
	public int end() { return end; }
    }

    /**
     * �^����ꂽ��������w�茅���ŕ������A�ŏ��̕������ԑ�����B<br>
     * �w�茅���S�p�����̒��ԂɂȂ�ꍇ�ɂ́A1�����̃X�y�[�X�𖄂߂ĕԂ��B
     * ��j
     * <pre>
     *  left("����������", 5, 0) -> "���� "
     * </pre>
     * <br>
     * �����A������蕶���񂪒Z���ꍇ�ɂ́A�������̃X�y�[�X�𖄂߂ĕԂ��B
     * @param str �������镶����
     * @param cols �������錅��(0�̏ꍇ�A�󕶎����ԑ�����B)
     * @param next �����̎��s�̊J�n�����ʒu��ݒ肷��B
     * @return �����ɂ�蕪�������ŏ��̕�����
     * @throws NullPointerException ����str��null�ł���B
     * @throws IllegalArgumentException ����cols��0�����ł���B
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
