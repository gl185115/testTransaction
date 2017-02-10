// Copyright(c) 2005 NCR Japan Ltd.
//
// $Log: UserLog.java,v $
// Revision 1.1  2005/07/05 05:19:57  kudo
// IOWLOGと別ファイルに同等フォーマットでの出力を可能とするUserLogを作成
//
// $Id: UserLog.java,v 1.1 2005/07/05 05:19:57 kudo Exp $
//
package ncr.realgate.util;

import java.io.File;

/**
 * ユーザ指定のファイルにIOWLOG同等のメッセージ出力を行う。
 *
 * @author NCR japan Ltd.
 * @version $Revision: 1.1 $ $Date: 2005/07/05 05:19:57 $
 */
public class UserLog extends IoWriter {

    /**
     * デフォルトのログファイルのファイル名: {@value}
     */
    public static final String USRLOG_FILE = "USRLOG";

    private String fileName = USRLOG_FILE;

    /**
     * 空のUSRLOGオブジェクトを作成する。<br>
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
     * ファイル名を指定して、UserLogオブジェクトを作成する。
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
