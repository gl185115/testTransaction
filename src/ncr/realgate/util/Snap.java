// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Snap.java,v $
// Revision 1.10  2008/09/26 07:06:59  art
// for test on win32
//
// Revision 1.9  2006/01/12 05:56:09  art
// Snap#close�̏�����MultiSnap�͖������ł���悤�ɕʃ��\�b�h�ւ̃��_�C���N�g�Ƃ����B
//
// Revision 1.8  2005/07/05 07:10:52  art
// �o�͐��FileOutputStream����P�Ȃ�OutputStream�ɏC��(StdOutSnap�p)
// IO���s�����I�[�o�[���C�h�\�ɏC��
//
// Revision 1.7  2005/06/10 06:32:28  art
// �_���v�@�\��ǉ�
//
// Revision 1.6  2005/06/10 04:55:41  art
// �h���N���X���R�[�h�����p�ł���悤�Ƀe���v���[�g�Ȃǂ��쐬
//
// Revision 1.5  2005/06/10 02:51:27  art
// MultiSnap(�}���`�t�@�C���V���O���C���X�^���X)���쐬���邽�߁Afinal�N���X����p���\�N���X�ɕύX�B
//
// Revision 1.4  2005/01/20 08:16:50  art
// �f�t�H���g�R���X�g���N�^�ƃv���p�e�B�ݒ�ɂ���ăC���X�^���X���������\�ɕύX�B
//
// Revision 1.3  2003/08/04 02:43:12  art
// Javadoc�L�q���C��
//
// Revision 1.2  2003/06/17 03:46:51  art
// ����t�@�C���𕡐��̃X���b�h�Ɋ��蓖�Ă��悤�ȏꍇ�̋������������悤�ɏC��
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
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
 * 6GRS�X�i�b�v�t�H�[�}�b�g�̃t�@�C�����쐬����B<br>
 * �A�v���P�[�V�������ُ��Ԃ����o���ɁA�ُ�̌��ƂȂ����g�����U�N�V�����A�f�[�^�A��O�Ȃǂ�
 * �㍏�A��Q�����\�Ȃ悤�ɕۑ����邽�߂Ɏg�p����B
 * <p>
 * �L���v�`���[�t�@�C���͈ȉ��̃t�H�[�}�b�g�����B<br>
 * <table border="1">
 * <tr><th>�t�B�[���h��</th><th>�^</th><th>����</th><th>���l</th></tr>
 * <tr><td>VLI</td><td>int</td><td>4</td><td>���g���G���f�B�A��</td></tr>
 * <tr><td>����</td><td>byte[]</td><td>7</td><YYMMDDHHMMSSMM</td></tr>
 * <tr><td>�^�C�v</td><td>byte</td><td>1</td><td>0:AIW,1:free</td></tr>
 * <tr><td>-</td><td>byte[]</td><td>4</td><td>���g�p</td></tr>
 * <tr><td>�R�����g</td><td>byte[]</td><td>16</td><td>-</td></tr>
 * </table>
 * <br>
 * �Ȃ��A���N���X�𗘗p����ꍇ�A��L�̃^�C�v�t�B�[���h�l�͏��1�ƂȂ�B�܂��AVLI�̓w�b�_����
 * �܂ޒ����ł���B
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.10 $ $Date: 2008/09/26 07:06:59 $
 */
public class Snap {

    /**
     * �X�i�b�v�t�@�C���̃t�@�C�����̒����̋K��l({@value})
     */
    public static final int FILENAME_LENGTH = 12;

    /**
     * �X�i�b�v�t�@�C���̃w�b�_���̒���({@value})
     */
    public static final int HEADER_LENGTH = 32;

    /**
     * AIW�t�H�[�}�b�g�̃X�i�b�v���R�[�h({@value})
     */
    public static final byte TYPE_AIW = 0;

    /**
     * �t���[�t�H�[�}�b�g�̃X�i�b�v���R�[�h({@value})
     */
    public static final byte TYPE_FREE = 1;

    private String pathName;

    /**
     * ���I�u�W�F�N�g�������I�Ɏg�p����t�@�C���X�g���[��
     */
    private volatile FileOutputStream out;

    /**
     * �������݂ɗ��p����X�g���[�����擾����B
     * @return �X�i�b�v�t�@�C���̃X�g���[��
     */
    protected OutputStream getOut() {
        return out;
    }

    /**
     * �������݂ɗ��p����X�g���[����ݒ肷��B
     * @param o �X�i�b�v�t�@�C���̃X�g���[��
     */
    protected void setOut(OutputStream o, CharSequence s) {
        out = (FileOutputStream)o;
        pathName = (s == null) ? null : s.toString();
    }

    /**
     * ��̃C���X�^���X�𐶐�����B<br>
     * �ŏ��̏������ݑO��{@link #setPathName}���\�b�h���Ăяo���ăX�i�b�v�t�@�C����
     * �ݒ肵�Ȃ���΂Ȃ�Ȃ��B
     */
    public Snap() {
    }

    /**
     * �w�肵���p�X���ŃX�i�b�v�t�@�C�����쐬����B���ɊY���t�@�C�������݂���ꍇ�́A
     * �ǋL���[�h�ŃI�[�v������B<br>
     * �X�i�b�v�t�@�C���̃}�[�N�ł���t�@�C�����擪��'S'�͌Ăяo�����ł��炩����
     * �ݒ肵�Ă����K�v������B
     * @param pathName �X�i�b�v���쐬����p�X��
     */
    public Snap(CharSequence pathName) {
	setPathName(pathName);
    }

    /**
     * �w�肵���p�X���ŃX�i�b�v�t�@�C�����쐬����B���ɊY���t�@�C�������݂���ꍇ�́A
     * �ǋL���[�h�ŃI�[�v������B�t�@�C�����擪��'S'�͓��R���X�g���N�^���Ő�������B
     * @param path �X�i�b�v���쐬����f�B���N�g����(����'/'�͕s�v)
     * @param name 12�����ȓ��Ńt�@�C�������w�肷��B�����������\�b�h�Œ����̃`�F�b�N�͍s��Ȃ��B
     */
    public Snap(CharSequence path, CharSequence name) {
	setPathName(createPathName(path, name));
    }

    /**
     * �f�B���N�g�����ƃt�@�C��������X�i�b�v�t�@�C�������쐬����B
     *
     * @param path �X�i�b�v���쐬����f�B���N�g����(����'/'�͕s�v)
     * @param name 12�����ȓ��Ńt�@�C�������w�肷��B�����������\�b�h�Œ����̃`�F�b�N�͍s��Ȃ��B
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
     * ���̃X�i�b�v�̃t���p�X����ԑ�����B
     * @return �X�i�b�v�t�@�C���̃t���p�X��
     */
    public String getPathName() {
	return pathName;
    }

    /**
     * ���̃X�i�b�v�̃t���p�X���̕s�ϕ���ԑ�����B
     * @return �X�i�b�v�t�@�C���̃t���p�X��
     */
    protected String getInternalPathName() {
	return pathName;
    }

    /**
     * ���̃X�i�b�v�̃p�X���̕s�ϕ���ݒ肷��B
     * @param s �X�i�b�v�t�@�C���̃t���p�X��
     */
    protected void setInternalPathName(CharSequence s) {
        pathName = (s == null) ? "" : s.toString();
    }

    /**
     * ���̃X�i�b�v�Ƀt���p�X����ݒ肷��B<br>
     * ���ݎg�p���̃X�i�b�v�t�@�C�������݂���ꍇ�́A�N���[�Y���ĐV���Ɏw�肳�ꂽ�X�i�b�v�t�@�C����
     * �g�p����B<br>
     * ���ɊY���t�@�C�������݂���ꍇ�́A�ǋL���[�h�ŃI�[�v������B<br>
     * �X�i�b�v�t�@�C���̃}�[�N�ł���t�@�C�����擪��'S'�͌Ăяo�����ł��炩���ߐݒ肵�Ă����K�v��
     * ����B
     * @param newPathName �X�i�b�v�t�@�C���̃t���p�X��
     * @throws NullPointerException newPathName�p�����[�^��null
     * @throws IllegalArgumentException �w��p�X���Ńt�@�C�����쐬�ł��Ȃ�
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
     * ���݂̃X�i�b�v�t�@�C�����N���[�Y����B�T�C�Y��0�̏ꍇ�́A�t�@�C�����폜����B
     */
    public void close() {
        internalClose();
    }

    /**
     * ���݂̃X�i�b�v�t�@�C�����N���[�Y����B�T�C�Y��0�̏ꍇ�́A�t�@�C�����폜����B
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
     *  �w��o�b�t�@�̓��e�������o���i���̎��A�t�@�C�������b�N����j
     * �����ŃG���[���o�Ă��ǂ��ɂ��Ȃ�Ȃ��̂Ŗ�������B
     * @param header �X�i�b�v�w�b�_
     * @param data �X�i�b�v�f�[�^
     * @param info �o�͏����i�[�X���I�u�W�F�N�g
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
     * �w�胁�b�Z�[�W���X�i�b�v�t�@�C���֏o�͂���B
     * @param comment �ő�16�o�C�g���R�����g�Ƃ��ďo�͂���Bnull�̏ꍇ�󕶎���Ƃ݂Ȃ�
     * @param data �o�͂���f�[�^
     * @return �X�i�b�v�������ݏ��
     * @throws NullPointerException data��null
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
     * �w��f�[�^���X�i�b�v�t�@�C���֏o�͂���B
     * @param comment �ő�16�o�C�g���R�����g�Ƃ��ďo�͂���Bnull�̏ꍇ�󕶎���Ƃ݂Ȃ�
     * @param data �o�͂���f�[�^
     * @param offset �o�͂��J�n����f�[�^�ʒu
     * @param length �o�͂���o�C�g��
     * @return �X�i�b�v�������ݏ��
     * @throws NullPointerException data��null��n���ꂽ�B
     * @throws IndexOutOfBoundsException offset�Ŏw�肳�ꂽ�ʒu�����܂��͔z����傫��
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
     * �w��f�[�^�̃I�t�Z�b�g�ȍ~���ׂĂ��X�i�b�v�t�@�C���֏o�͂���B
     * @param comment �ő�16�o�C�g���R�����g�Ƃ��ďo�͂���Bnull�̏ꍇ�󕶎���Ƃ݂Ȃ�
     * @param data �o�͂���f�[�^
     * @param offset �o�͂��J�n����f�[�^�ʒu
     * @return �X�i�b�v�������ݏ��
     * @throws NullPointerException data��null��n���ꂽ�B
     * @throws IndexOutOfBoundsException offset�Ŏw�肳�ꂽ�ʒu�����܂��͔z����傫��
     */
    public SnapInfo write(CharSequence comment, byte[] data, int offset) {
	if (data == null) {
	    throw new NullPointerException("Snap.write data is null");
	}
	return write(comment, data, offset, data.length - offset);
    }

    /**
     * �w���O�I�u�W�F�N�g�̃X�^�b�N�g���[�X���X�i�b�v�t�@�C���֏o�͂���B
     * @param comment �ő�16�o�C�g���R�����g�Ƃ��ďo�͂���Bnull�̏ꍇ�󕶎���Ƃ݂Ȃ�
     * @param data �X�^�b�N�g���[�X���o�͂����O�I�u�W�F�N�g
     * @return �X�i�b�v�������ݏ��
     * @throws NullPointerException data��null��n���ꂽ�B
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
     * �I�u�W�F�N�g�̑S�C���[�W���X�i�b�v�t�@�C���֏o�͂���B
     * @param comment �ő�16�o�C�g���R�����g�Ƃ��ďo�͂���Bnull�̏ꍇ�󕶎���Ƃ݂Ȃ�
     * @param data �C���[�W���o�͂���I�u�W�F�N�g
     * @return �X�i�b�v�������ݏ��
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
     * �X�i�b�v�o�͎��ɁA�o�͏���ԑ����邽�߂Ɏg�p�����I�u�W�F�N�g�B
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
	 * �֘A����write���\�b�h�ŏ������݂��s�����X�i�b�v�t�@�C���̃p�X���擾����B
	 * @return �X�i�b�v�̃p�X��
	 */
	public String getPathName() {
	    return pathName;
	}
	/**
	 * �֘A����write���\�b�h�ŏ������񂾃R�����g���擾����B<br>
	 * �R�����g�����w��̏ꍇ�A*��ԑ�����B
	 * @return �R�����g������
	 */
	public String getComment() {
	    return comment;
	}
	/**
	 * �֘A����write���\�b�h�ŏ������݂��s�����t�@�C���I�t�Z�b�g���擾����B
	 * @return �X�i�b�v�t�@�C�����̃I�t�Z�b�g
	 */
	public long getOffset() {
	    return offset;
	}
	/**
	 * ���̃C���X�^���X�������C���X�^���X���ǂ����𔻒肷��B
	 * @return �^�Ȃ�Ζ����ȃC���X�^���X
	 */
	public boolean isEmpty() {
	    return this == noSnap;
	}
    }

    /**
     * ���e�������ASnapInfo�̃C���X�^���X��ԑ�����B<br>
     * �����ŕԑ����ꂽSnapInfo�͏��������Ȃ��B
     * @return ������SnapInfo
     */
    public static SnapInfo getEmptyInfo() {
	return noSnap;
    }
    private static SnapInfo noSnap = new SnapInfo("/dev/null", "");
}
