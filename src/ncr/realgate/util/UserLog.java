// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: UserLog.java,v $
// Revision 1.1  2005/07/05 05:19:57  kudo
// IOWLOG�ƕʃt�@�C���ɓ����t�H�[�}�b�g�ł̏o�͂��\�Ƃ���UserLog���쐬
//
// $Id: UserLog.java,v 1.1 2005/07/05 05:19:57 kudo Exp $
//
package ncr.realgate.util;

import java.io.File;

/**
 * ���[�U�w��̃t�@�C����IOWLOG�����̃��b�Z�[�W�o�͂��s���B
 *
 * @author NCR japan Ltd.
 * @version $Revision: 1.1 $ $Date: 2005/07/05 05:19:57 $
 */
public class UserLog extends IoWriter {

    /**
     * �f�t�H���g�̃��O�t�@�C���̃t�@�C����: {@value}
     */
    public static final String USRLOG_FILE = "USRLOG";

    private String fileName = USRLOG_FILE;

    /**
     * ���USRLOG�I�u�W�F�N�g���쐬����B<br>
     */
    public UserLog() {
        super();
    }

    public UserLog(CharSequence path) {
	super(path);
    }

    public UserLog(CharSequence path, String initServerID) {
	super(path, initServerID);
    }

    /**
     * �t�@�C�������w�肵�āAUserLog�I�u�W�F�N�g���쐬����B
     */
    public UserLog(CharSequence path, String initServerID, String name) {
	this(path);
	setFileName(path, name);
	setServerID(initServerID);
    }

    public void setFileName(CharSequence path, String name) {
	fileName = name;
	setPathName(path.toString());
    }

    protected String getFileName() {
	return fileName;
    }
}
