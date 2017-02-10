// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: IoWriter.java,v $
// Revision 1.12  2010/09/09 01:48:21  hiroshi
//
// �}���`�X���b�h���ŁA
// IOWLOG�������s��i�X���b�h�Z�[�t�łȂ������j���C���B
//
// Revision 1.11  2005/07/05 05:19:57  kudo
// IOWLOG�ƕʃt�@�C���ɓ����t�H�[�}�b�g�ł̏o�͂��\�Ƃ���UserLog���쐬
//
// Revision 1.10  2005/07/05 04:45:58  art
// �p���N���X���쐬�\�Ȃ悤�ɃR���X�g���N�^�𐮗��AIO���s���̃A�N�Z�X�w���ύX
//
// Revision 1.9  2005/03/01 07:14:13  art
// �������R���X�g���N�^�Ő��������I�u�W�F�N�g�ł��������݂��\�Ȃ悤�ɏC���B
//
// Revision 1.8  2005/03/01 07:02:10  art
// �T�[�oID�̏����l��ݒ肷�邱�Ƃœr���ŗ�O���N�����Ȃ��悤�ɏC���B
//
// Revision 1.7  2005/01/20 08:16:50  art
// �f�t�H���g�R���X�g���N�^�ƃv���p�e�B�ݒ�ɂ���ăC���X�^���X���������\�ɕύX�B
//
// Revision 1.6  2003/06/20 03:18:53  art
// ����JVM���ł�LockFile�����p����邱�Ƃ��킩�����̂ŁA���������𕹗p����悤�ɏC�������B
//
// Revision 1.5  2003/06/20 02:23:31  art
// ���b�N�҂����s�����ꍇ�́A�t�@�C���T�C�Y�̍Ď擾�������C���B
//
// Revision 1.4  2003/06/17 03:47:20  art
// IOWLOG���b�N��A�������݈ʒu���Ď擾����悤�ɏC��
//
// Revision 1.3  2003/03/27 09:11:31  eda
// �X�܃T�[�o�[�����IOW�o�͗p���\�b�hwrite�̒ǉ�
//
// Revision 1.2  2003/03/19 07:56:59  hiroshi
//
// IOWLOG�̌p���s��\���f�|�f���P�J�����ڂ���Q�J�����ڂɕύX�B
// ���O�̃��x���\�����p���s�ɂ��\������悤�ɕύX�B
// �X�i�b�v���̊J�n�J�������P�J�����ڂ���Q�J�����ڂɕύX�B
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
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
 * IO���C�^�[�ւ̃��b�Z�[�W�o�͂��s�����߂�Queue�C���^�[�t�F�C�X���s���B<br>
 * ���b�Z�[�W�̓V�X�e�����ʂ�IOWLOG�t�@�C���ɏo�͂����B
 * ���N���X�̓o�b�`�v���O�������璼�ڃC���X�^���X���쐬���Ďg�p���邩�A���邢��
 * AppContext�C���^�[�t�F�C�X����ԐړI�Ɏg�p����B<br>
 * ���R�[�h�t�H�[�}�b�g
 * <table border="1">
 * <tr><hd>����</hd><hd>����</hd><hd>���l</hd></tr>
 * <tr><td>���x��</td><td>1</td><td>�G���[���x��</td></tr>
 * <tr><td>-</td><td>1</td><td>��(�p���Ȃ�[)/td></tr>
 * <tr><td>�T�[�oID</td><td>4</td><td>�A�v���P�[�V����ID</td></tr>
 * <tr><td>-</td><td>1</td><td>��</td></tr>
 * <tr><td>����</td><td>8</td><td>hh:mm:ss</td></tr>
 * <tr><td>-</td><td>1</td><td>��</td></tr>
 * <tr><td>�v���O����ID</td><td>8</td><td></td></tr>
 * <tr><td>-</td><td>3</td><td>��</td></tr>
 * <tr><td>�G���[ID</td><td>2</td><td>�G���[���ʎq</td></tr>
 * <tr><td>�Z�p���[�^</td><td>1</td><td>1�s�ڂ�:�A�p���s�͋�</td></tr>
 * <tr><td>�f�[�^</td><td>0�`49</td><td>�ϒ�(�V�t�gJIS)</td></tr>
 * <tr><td>���s</td><td>1</td><td>LF</td></tr>
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
     * IO���C�^�[���O�t�@�C���̃t�@�C����: {@value}
     */
    public static final String IOWLOG_FILE = "IOWLOG";

    /**
     * 1��̌Ăяo���ŏ������݉\�ȍő�s��({@value})
     */
    public static final int MAX_LINES = 10;

    /**
     * FileChannel�̍ő勖�e�o�b�t�@��(�V�X�e���ˑ��Ȃ̂Œ�������K�v������B)
     */
    private static final int MAX_BUFFS = 10;

    private String pathName;

    /**
     * �h���N���X�p�A�t�B�[���h�̏������݂̂����s����R���X�g���N�^�B
     * ���ׂẴR���X�g���N�^�͓����\�b�h���Ăяo���K�v������B
     * @param dummy ���\�b�h�V�O�l�`���ύX�p�̃_�~�[�p�����[�^�B���e�͖�������B
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
     * ���IO���C�^�I�u�W�F�N�g���쐬����B<br>
     * �ŏ��̏������ݑO��{@link #setPathName}���\�b�h���Ăяo���ď������݃f�B���N�g����
     * �w�肵�Ȃ���΂Ȃ�Ȃ��B�܂��A���O�ɃT�[�oID���K�v�Ȃ��{@link #setServerID}��
     * �Ăяo���ăT�[�oID��ݒ肵�Ȃ���Ȃ�Ȃ��B
     * @see #setPathName
     * @see #setServerID
     */
    public IoWriter() {
        this(System.getProperty("java.io.tmpdir"));
    }

    /**
     * ���O�ɏo�͂���T�[�oID��ݒ肷��B
     * @param id �ݒ肷��T�[�oID�B4����16�i������łȂ���΂Ȃ�Ȃ��B
     * @throws IllegalArgumentException 4����16�i������łȂ��B
     */
    public void setServerID(String id) {
        if (id == null || id.length() != 4) {
            throw new IllegalArgumentException("ServerID=" + id);
        }
        serverID = id.getBytes();
    }

    /**
     * �o�͐�f�B���N�g����ݒ肷��B
     * @param path �o�͐�f�B���N�g���̃p�X���B
     * @throws IllegalArgumentException path���f�B���N�g���ł͖������������ݕs��
     */
    public void setPathName(String path) {
        setupFile(path);
    }

    /**
     * �������݂Ɏg�p����f�B���N�g�����w�肵�AIO���C�^�I�u�W�F�N�g���쐬����B<br>
     * �������ݕs�\�ł������葶�݂��Ȃ��f�B���N�g�����w�肳�ꂽ�ꍇ�A��O���X���[����B<br>
     * ���R���X�g���N�^���g�p�����ꍇ�A�T�[�oID�͐ݒ肳��Ȃ��B����͓��R���X�g���N�^���f�o�b�O�p
     * ������ł���B
     * @param path IOWLOG�����݂���f�B���N�g��
     * @throws IllegalArgumentException path���f�B���N�g���ł͖������������ݕs��
     * @see #IoWriter(CharSequence, String)
     */
    public IoWriter(CharSequence path) {
        this(false);
        setupFile(path);
    }

    /**
     * �������݂Ɏg�p����f�B���N�g�����w�肵�AIO���C�^�I�u�W�F�N�g���쐬����B<br>
     * �������ݕs�\�ł������葶�݂��Ȃ��f�B���N�g�����w�肳�ꂽ�ꍇ�A��O���X���[����B
     * @param path IOWLOG�����݂���f�B���N�g��
     * @param initServerID �\���Ɏg�p����T�[�oID
     * @throws IllegalArgumentException path���f�B���N�g���ł͖������������ݕs�A
     *                                  �܂��̓T�[�oID��4�����ł͖����B
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
     * ���ۂ�IO���C�^�[�ւ̏o�͍͂s�킸�AIOWLOG�t�@�C���݂̂ɏ������݂��s��({@value})�B
     * @see #write
     */
    public static final char LOG = 'L';
    /**
     * ��Q�������ɒʏ�̃G���[�o�͂��s��({@value})�B
     * @see #write
     */
    public static final char ERROR = ' ';
    /**
     * �p�g���C�g������܂ށB�V�X�e���^�s�ɉe������a�؂ȏ�Q���o���Ɏw�肷��({@value})�B
     * @see #write
     */
    public static final char ALERT = '#';
    /**
     * �x�����t���ŏo�͂��s���B��Q�Ƃ͒f��ł��Ȃ����������K�v�ȏꍇ�Ɏw�肷��({@value})�B
     * @see #write
     */
    public static final char WARNING = 'W';
    /**
     * �X�i�b�v���s�̊J�n�}�[�N�B�A�v���P�[�V�����͎w�肵�Ă͂Ȃ�Ȃ�({@value})�B
     */
    public static final char SNAP_LINE = ';';

    /**
     * IO���C�^�[�̃��b�Z�[�W�̍ő包���������l�i{@value}�j
     */
    public static final int WIDTH = 49;

    /**
     * level�Ŏw�肳�ꂽ���@�ɂ����IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * �o�͂��郁�b�Z�[�W�Ɋ֘A���ăX�i�b�v���̎悵���ꍇ�́ASnapInfo�z��������Ɏ��
     * ���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ��B<br>
     * ���b�Z�[�W��1�s������49���ŁA�ő�MAX_LINES�s��'\n'�ŋ�؂��Ďw�肷��B<br>
     * �����J�n��ɔ����������ׂĂ̗�O�͖������邽�߁A�����������ł������A�Ăяo�����ɗ�O��
     * �ʒm����Ȃ��B
     *
     * @param level �o�͕��@���w�肷��
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B
     * @throws NullPointerException name, id, msg�̂����ꂩ��null
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
     * level�Ŏw�肳�ꂽ���@�ɂ����IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * ���b�Z�[�W��1�s������49���ŁA�ő�MAX_LINES�s��'\n'�ŋ�؂��Ďw�肷��B<br>
     * �����J�n��ɔ����������ׂĂ̗�O�͖������邽�߁A�����������ł������A�Ăяo�����ɗ�O��
     * �ʒm����Ȃ��B<br>
     *
     * @param level �o�͕��@���w�肷��
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B
     * @param snaps ���b�Z�[�W�Ɋ֘A����X�i�b�v�����i�[�����z��
     * @throws NullPointerException name, id, msg, snaps�̂����ꂩ��null
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
     * level�Ŏw�肳�ꂽ���@�ɂ����IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * �o�͂��郁�b�Z�[�W�Ɋ֘A���ăX�i�b�v���̎悵���ꍇ�́ASnapInfo�z��������Ɏ��
     * ���\�b�h���g�p���Ȃ���΂Ȃ�Ȃ��B<br>
     * �����\�b�h�́AHalfWidth�N���X��split���\�b�h�Ƒg�ݍ��킹�Ďg�p����B<br>
     * ��j
     * <pre>
     * CharBuffer buff = CharBuffer.allocate(1000);
     * buff.put(message);
     * buff.put(data);
     * buff.put(message-2);
     * iow.write(IoWriter.ERROR, appName, id, 
     *     HalfWidth.split((CharSequence)buff.positon(0), IoWriter.WIDTH));
     * </pre>
     *
     * @param level �o�͕��@���w�肷��
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B<br>
     *            �z��̗v�f����MAX_LINES���z����ꍇ��10���z�������͏o�͂��Ȃ��B
     *            �z��v�f��0�̏ꍇ�͉����o�͂��Ȃ��B
     * @throws NullPointerException name, id, msg�̂����ꂩ��null
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
     * level�Ŏw�肳�ꂽ���@�ɂ����IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * �����\�b�h�́AHalfWidth�N���X��split���\�b�h�Ƒg�ݍ��킹�Ďg�p����B<br>
     * ��j
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
     * @param level �o�͕��@���w�肷��
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B<br>
     *            �z��̗v�f����MAX_LINES���z����ꍇ��10���z�������͏o�͂��Ȃ��B
     *            �z��v�f��0�̏ꍇ�͉����o�͂��Ȃ��B
     * @param snaps ���b�Z�[�W�Ɋ֘A����X�i�b�v�����i�[�����z��
     * @throws NullPointerException name, id, msg, snaps�̂����ꂩ��null
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
     * �w�肳�ꂽ�o�b�t�@��IOWLOG�ɏ����o���B
     * �ŏ���IOWLOG���t�@�C�����b�N���AIOWLOG���I�[�v���A�������݁A�N���[�Y�A�A�����b�N����B
     * @param buffs �o�̓o�b�t�@
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
     * level�Ŏw�肳�ꂽ���@�AserverID�Ŏw�肳�ꂽServerID, time�Ŏw�肳�ꂽ�����ɂ����
     * IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * ���̃��\�b�h�́A�X�܃T�[�o�[�����IOWLOG���b�Z�[�W���o�͂��邽�߂̓��ꃁ�\�b�h�ł���
     * �ʏ�̃A�v���P�[�V�����͎g�p���Ȃ��B<br>
     * ���b�Z�[�W��1�s������49���ŁA�ő�MAX_LINES�s��'\n'�ŋ�؂��Ďw�肷��B<br>
     * �����J�n��ɔ����������ׂĂ̗�O�͖������邽�߁A�����������ł������A�Ăяo�����ɗ�O��
     * �ʒm����Ȃ��B
     * @param level �o�͕��@���w�肷��
     * @param serverID �T�[�o�[ID4�����w�肷��B
     * @param time ������HH:MM:SS�`���Ŏw�肷��B
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B
     * @throws NullPointerException name, id, msg�̂����ꂩ��null
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
     * level�Ŏw�肳�ꂽ���@�AserverID�Ŏw�肳�ꂽServerID, time�Ŏw�肳�ꂽ�����ɂ����
     * IOWLOG�����IO���C�^�[�փ��b�Z�[�W���o�͂���B<br>
     * ���̃��\�b�h�́A�X�܃T�[�o�[�����IOWLOG���b�Z�[�W���o�͂��邽�߂̓��ꃁ�\�b�h�ł���
     * �ʏ�̃A�v���P�[�V�����͎g�p���Ȃ��B<br>
     * �����\�b�h�́AHalfWidth�N���X��split���\�b�h�Ƒg�ݍ��킹�Ďg�p����B<br>
     * @param level �o�͕��@���w�肷��
     * @param serverID �T�[�o�[ID4�����w�肷��B
     * @param time ������HH:MM:SS�`���Ŏw�肷��B
     * @param name �v���O������8�o�C�g���w�肷��
     * @param id �v���O����ID2�����w�肷��
     * @param msg �o�̓��b�Z�[�W���w�肷��B<br>
     *            �z��̗v�f����MAX_LINES���z����ꍇ��10���z�������͏o�͂��Ȃ��B
     *            �z��v�f��0�̏ꍇ�͉����o�͂��Ȃ��B
     * @param snaps ���b�Z�[�W�Ɋ֘A����X�i�b�v�����i�[�����z��
     * @throws NullPointerException name, id, msg, snaps�̂����ꂩ��null
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
     * �z����̗L����Snap�̐���ԑ�����B
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
