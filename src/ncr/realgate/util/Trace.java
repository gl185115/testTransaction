// Copyright(c) 2002 NCR Japan Ltd.
//
// $Log: Trace.java,v $
// Revision 1.13  2008/05/16 02:36:16  kudo
// REAL��̃A�v���P�[�V�����ł́AContext���瓮�I�ɕύX���ꂽ�f�o�b�O���x����
// �擾�ł��Ȃ����߁ATrace���猻�݂̃f�o�b�O���x�����擾�o����悤�ɕύX�����B
//
// Revision 1.12  2006/02/15 02:50:50  art
// flush���\�b�h��ǉ�
//
// Revision 1.11  2005/12/15 09:58:08  art
// �f�o�b�O�o�͂̍폜
//
// Revision 1.10  2005/12/15 09:57:09  art
// Boolean�ϊ�����0�Ŏn�܂�Ȃ����l�͐^�Ƃ݂Ȃ�C���[��(���������r���[)��K�p�B
//
// Revision 1.9  2005/09/15 04:44:09  art
// �o�C�g�z��̃g���[�X��HexDump�𗘗p����悤�ɏC���B
//
// Revision 1.8  2005/09/15 02:25:47  art
// �p�����[�^�G���[���ɗ�O�Ƃ����ɁA�^����ꂽ�p�����[�^�̏�Ԃ�������������o�͂���悤�ɏC���B
//
// Revision 1.7  2005/01/25 05:35:42  art
// �T�u�N���X�̓��o���\�Ƃ����B
//
// Revision 1.6  2005/01/20 08:16:50  art
// �f�t�H���g�R���X�g���N�^�ƃv���p�e�B�ݒ�ɂ���ăC���X�^���X���������\�ɕύX�B
//
// Revision 1.5  2003/08/26 00:59:24  art
// �����󎚕t���̒P�Ȃ镶����o�̓��\�b�h��Printer�ɒǉ��B
//
// Revision 1.4  2003/07/15 11:52:44  art
// �N���[�Y���ɃT�C�Y��0�Ȃ�폜����悤�ɕύX�B
//
// Revision 1.3  2003/06/11 07:01:27  art
// ���\�b�h�w�b�_�p�o�b�t�@�T�C�Y���g�債���B
//
// Revision 1.2  2003/05/27 11:23:38  art
// ���\�b�h�ޏo���̈󎚂ŗ�O�ƂȂ�Ȃ��悤�ɖh����������B
//
// Revision 1.1  2002/12/25 01:57:37  kudo
// �f�B���N�g���\�����������������̂ŏC���B���ł�Javadoc��package�����C���B
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
 * �f�o�b�O�p�g���[�X�t�@�C���̐�����s���N���X�B���ۂ̃g���[�X�t�@�C���ւ̏o�͏����́A
 * Printer�I�u�W�F�N�g�ɂ���čs���B<br>
 * �f�o�b�O�p�@�\�Ȃ̂ŁA�����Ŕ����������ׂĂ̗�O�𖳎�����B�Ăяo�����ւ͗�O�͒ʒm����Ȃ��B
 *
 * @author NCR Japan Ltd.
 * @version $Revision: 1.13 $ $Date: 2008/05/16 02:36:16 $
 */
public class Trace {
    // �f�o�b�O���x���ɂ���ďo�͂�؂�ւ���
    private int rootDebugLevel = DEFAULT_LEVEL;
    /**
     * ����̃f�o�b�O���x����ݒ肷��B
     * @param newLevel �V���ɐݒ肷��f�o�b�O���x���B
     */
    protected void setRootDebugLevel(int newLevel) {
        rootDebugLevel = newLevel;
    }
    /**
     * ����̃f�o�b�O���x�����擾����B
     * @return ����̃f�o�b�O���x���B
     */
    protected int getRootDebugLevel() {
        return rootDebugLevel;
    }
    
    static final int METHOD_ONLY = 1;
    static final int DEFAULT_LEVEL = 3;

    /**
     * �o�͂�����ʂ�؂�ւ���B<br>
     * ���̃��\�b�h�ł̓f�o�b�O�o�͂̒�~�^�ĊJ���s�����Ƃ͂ł��Ȃ��B
     * <table border="1">
     * <tr><th>�l</th><th>�o��</th></tr>
     * <tr><td>0</td><td>�o�͒�~</td></tr>
     * <tr><td>1</td><td>methodEnter, methodExit</td></tr>
     * <tr><td>2�ȏ�</td><td>���ׂ�</td></tr>
     * </table>
     * @param newLevel �V���ɐݒ肷��f�o�b�O���x��
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
     * ���̃I�u�W�F�N�g�ɐݒ肳��Ă���f�o�b�O���x�����擾����B
     * @return ���݂̃f�o�b�O���x��
     */
    public int getDebugLevel() {
        return getRootDebugLevel();
    }

    /**
     * �����Ŏg�p���镶����o�͗p��PrintWriter�B�f�o�b�O���łȂ����null�B
     */
    protected PrintWriter orgOut;
    /**
     * �N���C�A���g����̏������ݗv���ŗ��p����v�����g���C�^�̃C���X�^���X��ԑ�����B<br>
     * �T�u�N���X�����̂܂ܗ��p���鏑�����ݏ����͂��̃��\�b�h����o�͐���擾���Ȃ���΂Ȃ�Ȃ��B
     * @return �v�����g���C�^�̃C���X�^���X(�I�[�v������Ă��Ȃ����null)�B
     */
    protected PrintWriter getOut() {
        return orgOut;
    }
    /**
     * �N���C�A���g����̏������ݗv���ŗ��p����v�����g���C�^�̃C���X�^���X��ݒ肷��B<br>
     * @param newOut �v�����g���C�^�̃C���X�^���X(��������null)�B
     */
    protected void setOut(PrintWriter newOut) {
        orgOut = newOut;
    }

    /**
     * Printer�I�u�W�F�N�g�i�[�p
     */
    private HashMap printers = new HashMap();

    /**
     * �N���[�Y���Ă��ǂ����ǂ����𔻒肷��B<br>
     * �X�g���[����Ƀg���[�X���쐬�����ꍇ�ɂ̓N���[�Y���Ȃ��B
     */
    private boolean isClosable;

    private String pathName;
    /**
     * �t�@�C���̃p�X����ԑ�����B<br>
     * ���I�[�v���܂��͒��ڃX�g���[����ݒ肵���ꍇ��null���ԑ������B
     * @return �g���[�X�t�@�C���̃p�X���B
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * �w�肳�ꂽ�p�X���Ńg���[�X�t�@�C�����쐬����B
     * @param path �o�̓t�@�C����(�_�~�[���쐬�������ꍇ�ɂ�/dev/null���w�肷����A
     *             �������̃R���X�g���N�^���g�p���ׂ��ł���)
     */
    public Trace(CharSequence path) {
	initNew(path);
    }

    /**
     * �w�肳�ꂽ�X�g���[����Ƀg���[�X�t�@�C�����쐬����B
     * @param stream �o�͑Ώۂ̃X�g���[��
     */
    public Trace(OutputStream stream) {
	initNew(stream);
    }

    /**
     * �w�肳�ꂽ���C�^��Ƀg���[�X�t�@�C�����쐬����B
     * @param writer �o�͑Ώۂ̃��C�^�[
     */
    public Trace(Writer writer) {
	initNew(writer);
    }

    /**
     * �����s��Ȃ��g���[�X�t�@�C�����쐬����B
     */
    public Trace() {
    }

    /**
     * ���݂̃g���[�X�t�@�C���̃o�b�t�@�������o���B<br>
     * �����t�@�C�������݂��Ȃ���Ή����s��Ȃ��B
     */
    public void flush() {
        PrintWriter out = getOut();
        if (out != null) {
            out.flush();
        }
    }


    /**
     * ���݂̃g���[�X�t�@�C�����N���[�Y����B<br>
     * �����T�C�Y��0�Ȃ�΍폜����B
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
     * �o�͐�t�@�C����ݒ肷��B
     * @param path �g���[�X�o�̓t�@�C���̃t���p�X���B
     */
    public void setPathName(String path) {
        initNew(path);
    }

    /**
     * �o�͐�t�@�C�������Z�[�u����B<br>
     * {@link #setPathName}�ƈقȂ�t�@�C���̃I�[�v���𔺂�Ȃ��B
     * @parama path �g���[�X�o�̓t�@�C���̃t���p�X���B
     */
    protected void setFilePathName(CharSequence path) {
        pathName = path.toString();
    }

    /**
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ�V���ȃt�@�C�����쐬����B
     * <br>�f�o�b�O�o�̓��x���̓f�t�H���g(3)��ݒ肷��B
     * @param nextPath ���Ɏg�p����g���[�X�t�@�C����
     */
    public void initNew(CharSequence nextPath) {
	initNew(nextPath, DEFAULT_LEVEL);
    }

    /**
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ�V���ȃt�@�C����ǉ����[�h�ō쐬����B
     * @param nextPath ���Ɏg�p����g���[�X�t�@�C����
     * @param level �f�o�b�O�o�̓��x��
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
            // ���̃t�@�C���̓f�o�b�O�p�Ȃ̂ō쐬�Ɏ��s���Ă���������B
	    setOut(null);
	}
    }

    /**
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ�X�g���[����Ńg���[�X���s���B
     * <br>�f�o�b�O�o�̓��x���̓f�t�H���g(3)��ݒ肷��B
     * @param stream �g���[�X���o�͂���X�g���[��
     */
    public void initNew(OutputStream stream) {
	initNew(stream, DEFAULT_LEVEL);
    }

    /**
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ�X�g���[����Ńg���[�X���s���B
     * @param stream �g���[�X���o�͂���X�g���[��
     * @param level �f�o�b�O�o�̓��x��
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
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ���C�^��Ńg���[�X���s���B<br>
     * �f�o�b�O�o�̓��x���̓f�t�H���g(3)��ݒ肷��B
     * @param writer �g���[�X���o�͂��郉�C�^
     */
    public void initNew(Writer writer) {
	initNew(writer, DEFAULT_LEVEL);
    }

    /**
     * �����I�[�v�����ł���Ό��݂̃g���[�X�t�@�C�����N���[�Y����B
     * ���̌�p�����[�^�Ŏw�肳�ꂽ���C�^��Ńg���[�X���s���B
     * @param writer �g���[�X���o�͂��郉�C�^
     * @param level �f�o�b�O�o�̓��x��
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
     * ���ۂ̏������ݏ������s���I�u�W�F�N�g�ł���A�X�̃A�v���P�[�V�����̃g���[�X���Ǘ�����B
     * methodEnter/methodExit�̃y�A�𗘗p���邱�Ƃɂ��g���[�X�t�@�C�����ŃC���f���g��
     * �s����B<br>
     * println���\�b�h�́A�L���v�V�����ƒl�̑g�ݍ��킹����؂蕶��(�f�t�H���g��'=')�Ō���
     * ���ďo�͂���B<br>
     * ��j
     * <pre>
     * int myMethod(int x, String y, long z) {
     *   Trace.Printer ptr = trace.createPrinter(getClass());
     *   ptr.methodEnter("myMethod");
     *   ptr.println("arg:x", x).println("arg:y", y).println("arg:z", z);
     *   ....
     *   return ptr.methodExit(result);
     * }
     * </pre>
     * <br>�o�͌���<br>
     * <pre>
     * hh:mm:ss:mss ClassName.myMethod -----enter-----
     *              arg:x=1
     *              arg:y="yyyyy"
     *              arg:z=300000
     * hh:mm:ss:mss ClassName.myMethod=result -----exit-----
     * </pre>
     * <br>
     * �f�o�b�O���x����1�̏ꍇ�́AmethodEnter,methodExit�̗����\�b�h�̂ݏo�͂��s���B
     * ���̃��\�b�h�ɂ��o�͂��s���ɂ̓f�o�b�O���x����2�ȏ�ɐݒ肵�Ȃ���΂Ȃ�Ȃ��B
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

	/** �g���[�X�t�@�C���̐؂�ւ�莞�ɌĂяo�����B
	 * ���݂̃t�B�[���h�̂����A���\�b�h�֘A�̐ݒ�����Z�b�g����B
	 */
	void initNew() {
	    debugLevel = getRootDebugLevel();
	    nestLevel = 0;
	    methodNames.clear();
	}

	/**
         * �v�����^�[�I�u�W�F�N�g�𐶐�����B
         * @param name ���̃v�����^�[�̃L���v�V�����ɗ��p���镶����B
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
	    // �A�v���P�[�V������methodEnter���Ă΂���methodExit���Ăяo�����ꍇ
	    // ��O�ƂȂ�̂ŁA�h�䂷��B
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
	 * �f�o�b�O���x����ݒ肷��B
	 * @param newLevel �Ăяo���ȍ~�ɓK�p����f�o�b�O���x��
	 * @see #setDebugLevel
	 */
	public void setDebugLevel(int newLevel) {
	    debugLevel = newLevel;
	}

	/**
	 * println���\�b�h��caption�p�����[�^��value�p�����[�^�̋�؂蕶����ݒ肷��B
	 * @param ch ��؂蕶��
	 */
	public void setSeparatorChar(char ch) {
	    separator = ch;
	}

	/**
	 * println���\�b�h��caption�p�����[�^��value�p�����[�^�̋�؂蕶����ԑ�����B
	 * @return ��؂蕶��
	 */
	public char getSeparatorChar() {
	    return separator;
	}

	/**
	 * �����󎚃��[�h��ݒ肷��B<br>
	 * �����[�h���e������̂́Aprintln���\�b�h�����ł���B
	 * @param enabled �^�ɐݒ肷��ƈ󎚍s�̐擪��hh:mm:ss:ms �̌`����
	 *                ���ݎ������󎚂���B<br>
	 *                �U�ɐݒ肷��ƁATIME_WIDTH���̋󔒂��󎚂����B
	 */
	public Printer setTimestampMode(boolean enabled) {
	    timestamp = enabled;
	    return this;
	}

	/**
	 * ���݂̎����󎚃��[�h���擾����B
	 * @return �^�ł���Ύ����󎚃��[�h
	 */
	public boolean isTimestampMode() {
	    return timestamp;
	}

	/**
	 * ���\�b�h�̊J�n�����ݎ����ƂƂ��ɏo�͂����s����B
	 * @param newMethodName �o�͂��郁�\�b�h��
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂���int�l
	 * @return result��Ԃ�
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂���long�l
	 * @return result��Ԃ�
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂���u�[���l
	 * @return result��Ԃ�
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂���double�l
	 * @return result��Ԃ�
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂��镶����l(null�̏ꍇ<null>�Əo�͂���)
	 * @return result�𕶎���Ƃ��ĕԂ�
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
	 * ���\�b�h�̏I�������ݎ����ƂƂ��ɏo�͂����s����B<br>
	 * ���\�b�h���͒��߂�methodEnter�Ŏw�肳�ꂽ���̂��g�p����B
	 * @param result �o�͂���I�u�W�F�N�g�l�i������toString()���Ăяo���j<br>
	 *               null�̏ꍇ�A<null>�Əo�͂���B
	 * @return result��Ԃ�
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
	 *���s����B
	 */
	public Printer println() {
            PrintWriter out = getOut();
	    if (debugLevel > METHOD_ONLY && out != null) {
		out.println();
	    }
	    return this;
	}

	/**
	 * �w�蕶������o�͂��A���s����B{@link #write}�ƈقȂ�^�C���X�^���v��
	 * ���[�h�ɉ����ďo�͂���B
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
	 *�w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���int�l
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���long�l
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���u�[���l
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���double�l
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂��镶����
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���o�C�g�z��(null�̏ꍇ�A"null"�Əo��)
	 * @throws NullPointerException value��null
	 */
	public Printer println(String caption, byte[] value) {
	    if (value == null) {
		return println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���o�C�g�z��(null�̏ꍇ�A"null"�Əo��)
	 * @param off �o�͂��J�n����z��I�t�Z�b�g
	 * @param len �o�͂���v�f��
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���int�z��(null�̏ꍇ�A"null"�Əo��)
	 */
	public Printer println(String caption, int[] value) {
	    if (value == null) {
                return println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���int�z��(null�̏ꍇ�A"null"�Əo��)
	 * @param off �o�͂��J�n����z��I�t�Z�b�g
	 * @param len �o�͂���v�f��
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���long�z��(null�̏ꍇ�A"null"�Əo��)
	 */
	public Printer println(String caption, long[] value) {
	    if (value == null) {
                println(caption, "null");
	    }
	    return println(caption, value, 0, value.length);
	}

	/**
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���long�z��(null�̏ꍇ�A"null"�Əo��)
	 * @param off �o�͂��J�n����z��I�t�Z�b�g
	 * @param len �o�͂���v�f��
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
	 * �w�肳�ꂽ�l���o�͂��A���s����B
	 * @param caption �o�͂���L���v�V����
	 * @param value �o�͂���Object�l(null�̏ꍇ�Anull�Əo�͂����)
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
	 * ��������o�͂���B<br>
	 * CharBuffer�̏ꍇ�͌��݂̃|�W�V������ۑ�������ŁA0���猻�݂̃|�W�V�����܂ł�
	 * �o�͂��A���\�b�h�ޏo���Ɍ��̃|�W�V�����𕜌�����B<br>
	 * �����\�b�h���g�p�����������݂ł́A�������s�A�����󎚂̂�������s���Ȃ��B
	 * @param data �o�͂��镶����(null�̏ꍇ�A"<null>"�Əo��)
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
	 * ���̃X�g���[�����N���[�Y����B
	 */
	public void close() {
	    flush();
	}

	/**
	 * �o�b�t�@���t���b�V������B
	 */
	public void flush() {
            PrintWriter out = getOut();
	    if (out != null) {
		out.flush();
	    }
	}

    }

    /**
     * �A�v���P�[�V����������v�����^�[�I�u�W�F�N�g��ԑ�����B<br>
     * �܂��Y���A�v���P�[�V�����p�v�����^�[�����݂��Ȃ���ΐV���ɍ쐬����B
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
     * �g���[�X�o�͗p��Printer�I�u�W�F�N�g��ԑ�����B<br>
     * Printer�I�u�W�F�N�g�͍쐬���_�ł͔񎞍��󎚃��[�h�ɐݒ肳��Ă���B<br>
     * �����Ƃ��ăo�i�[�o�͂Ɏg�p�����A�v���P�[�V������ID���w�肵�Ȃ���΂Ȃ�Ȃ��B<br>
     * �Ȃ�����A�v���P�[�V��������̓���t�@�C���ɑ΂���v���ɂ��Ă͏�ɓ���̃v�����^�[��
     * �ԑ�����B
     * @param name �A�v���P�[�V������
     * @return �g���[�X�o�͗p�I�u�W�F�N�g�̃C���X�^���X
     * @throws NullPointerException name�p�����[�^��null
     */
    public Printer createPrinter(String name) {
	return getPrinter(name);
    }

    /**
     * �g���[�X�o�͗p��Printer�I�u�W�F�N�g��ԑ�����B<br>
     * Printer�I�u�W�F�N�g�͍쐬���_�ł͔񎞍��󎚃��[�h�ɐݒ肳��Ă���B<br>
     * �����Ƃ��ăo�i�[�o�͂Ɏg�p�����A�v���P�[�V�������w�肵�Ȃ���΂Ȃ�Ȃ��B
     * @param klass �A�v���P�[�V����
     * @return �g���[�X�o�͗p�I�u�W�F�N�g�̃C���X�^���X
     * @throws NullPointerException name�p�����[�^��null
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
