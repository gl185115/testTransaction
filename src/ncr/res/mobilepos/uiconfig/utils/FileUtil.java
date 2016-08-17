package ncr.res.mobilepos.uiconfig.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

public class FileUtil {

//	/**
//	 * ファイルを読んで、ファイルコンテンツを取る。
//	 *
//	 * @param pReadFile : ファイルパス
//	 * @param pEncoding : Encoding
//	 * @return ファイルコンテンツ
//	 */
	public static String fileRead(File pReadFile, String pEncoding) {

		String content = "";
		FileInputStream reader = null;
		byte[] buffer = new byte[0];

		try {
			if (pReadFile.exists()) {

				reader = new FileInputStream(pReadFile);
				buffer = new byte[reader.available()];
				reader.read(buffer);
				content = new String(buffer, pEncoding);

				reader.close();
			} else {
			}
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException io) {
			}
		}

		return content;
	}

	/**
	 * ファイルを読んで、ファイルコンテンツを取る。
	 *
	 * @param pReadFile : ファイルパス
	 * @return ファイルコンテンツ
	 */
	public static String fileRead(File pReadFile) {

		String content = "";

		try {
			if (isUtf8Encoding(pReadFile)) {
				content = fileRead(pReadFile, StaticParameter.code_UTF8);
			} else {
				content = fileRead(pReadFile, StaticParameter.code_MS932);
			}
		} catch (Exception e) {
		}

		return content;
	}
//
//	/**
//	 * ファイルを読んで、第一行目コンテンツを取る。
//	 *
//	 * @param pReadFile : The target file path.
//	 * @return 第一行目コンテンツ
//	 */
//	public static String fileReadFirstLine(File pReadFile) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		String content = "";
//		String retLine = "";
//
//		try {
//			if (pReadFile.exists()) {
//				content = fileRead(pReadFile);
//
//				for (String line : content.split(StaticParameter.str_enter)) {
//					if (!StringUtility.isNullOrEmpty(line)) {
//						retLine = line;
//						break;
//					}
//				}
//			} else {
//				LOG.info(method + LogUtil.RES_NOTEXIST + pReadFile.getPath());
//			}
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + retLine);
//		return retLine;
//	}

	/**
	 * ファイルを保存する。
	 *
	 * @param pSaveFile       : ファイルパス
	 * @param pContent        : コンテンツ
	 * @param pAppend         : true（追加）／false(新規)
	 * @param pCharsetEncoder : Encoding
	 * @return boolean        : true（成功）／false（失敗）
	 */
	public static boolean fileSave(File pSaveFile, String pContent, boolean pAppend, String pCharsetEncoder) {

		FileOutputStream output = null;
		OutputStreamWriter stream = null;
		BufferedWriter writer = null;
		boolean resFlg = false;

		try {
			output = new FileOutputStream(pSaveFile, pAppend);
			stream = new OutputStreamWriter(output, pCharsetEncoder);
			writer = new BufferedWriter(stream);
			writer.write(pContent);
			writer.flush();
			writer.close();

			resFlg = true;
		} catch (Exception e) {
			resFlg = false;
		} finally {
			try {
				if (writer != null) {
					writer.close();
				}
				if (stream != null) {
					stream.close();
				}
				if (output != null) {
					output.close();
				}
			} catch (IOException e) {
			}
		}
		return resFlg;
	}

//	/**
//	 * ファイルのバックアップファイルを削除する。
//	 *
//	 * @param pFile    : ファイルパス
//	 * @return boolean : true（成功）／false（失敗）
//	 */
//	public static boolean fileBackupDelete(File pFile) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		boolean resFlg = false;
//		File deleteBackupFile = null;
//
//		try {
//			if (!pFile.exists()) {
//				LOG.error(method + LogUtil.RES_NOTEXIST + pFile.getPath());
//				LOG.debug(LogUtil.methodExit(method) + resFlg);
//				return false;
//			}
//
//			if (!pFile.isFile()) {
//				LOG.error(method + LogUtil.RES_NOTFILE + pFile.getPath());
//				LOG.debug(LogUtil.methodExit(method) + resFlg);
//				return false;
//			}
//
//			deleteBackupFile = new File(pFile.getParentFile(), pFile.getName() + StaticParameter.file_bk);
//			if (deleteBackupFile.delete()) {
//				resFlg = true;
//				LOG.debug(method + LogUtil.RES_SUCCESS + deleteBackupFile.getPath());
//			} else {
//				resFlg = fileDeleteByCmdDel(deleteBackupFile);
//			}
//
//		} catch (Exception e) {
//			resFlg = false;
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + resFlg);
//		return resFlg;
//	}
//
	/**
	 * ファイルのバックアップファイルをファイルに戻る。
	 *
	 * @param pFile    : ファイルパス
	 * @return boolean : true（成功）／false（失敗）
	 */
	public static boolean fileRollback(File pFile) {

		boolean resFlg = false;
		File backupFile = null;

		try {
			if (pFile.exists()) {
				fileDelete(pFile);
				return false;
			} else {
			}

			if (!pFile.isFile()) {
				return false;
			}

			backupFile = new File(pFile.getParentFile(), pFile.getName() + StaticParameter.file_bk);
			if (fileCopyByCmdCopy(backupFile, pFile)) {
				resFlg = true;
			} else {
				if (fileBackupByCmdCopy(backupFile, false)) {
					if (backupFile.renameTo(pFile)) {
						resFlg = true;
					} else {
						resFlg = false;
					}
				} else {
					resFlg = false;
				}
			}

		} catch (Exception e) {
			resFlg = false;
		}

		return resFlg;
	}

//	public static String getFileContent(byte[] pBuff) {
//
//		String method = LogUtil.getCurrentMethodName();
//		String content = "";
//		byte[] utf8Buff = new byte[0];
//
//		try {
//			if (pBuff == null || pBuff.length == 0) {
//				LOG.info(method + "File content is null or empty!");
//				return content;
//			}
//
//			utf8Buff = new String(pBuff, StaticParameter.code_UTF8).getBytes(StaticParameter.code_UTF8);
//			if (Arrays.equals(utf8Buff, pBuff)) {
//				content = new String(pBuff, StaticParameter.code_UTF8);
//			} else {
//				content = new String(pBuff, StaticParameter.code_MS932);
//			}
//
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		return content;
//	}
//
	public static boolean isUtf8Encoding(File pFile) {

		FileInputStream reader = null;
		byte[] buffer = new byte[0];

		try {
			if (pFile.exists()) {

				reader = new FileInputStream(pFile);
				buffer = new byte[reader.available()];
				reader.read(buffer);

				byte[] tmpUTF8 = new String(buffer, StaticParameter.code_UTF8).getBytes(StaticParameter.code_UTF8);
				if (Arrays.equals(tmpUTF8, buffer)) {
					return true;
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			} catch (IOException io) {
			}
		}

		return false;
	}
//
//	/**
//	 * ファイルを読んで、ファイルコンテンツを取る。
//	 *
//	 * @param pFile           : The target file path.
//	 * @param pCharsetEncoder : Encoder
//	 * @return String : The string of target file.
//	 */
//	public static String getSmbFileContent(SmbFile pSmbFile) {
//
//		String method = LogUtil.getCurrentMethodName();
//		String logContent = "";
//		BufferedInputStream reader = null;
//		byte[] buffer = new byte[0];
//
//		try {
//			buffer = new byte[pSmbFile.getContentLength()];
//			reader = new BufferedInputStream(new SmbFileInputStream(pSmbFile));
//			reader.read(buffer);
//			reader.close();
//
//			logContent = getFileContent(buffer);
//
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//			return "";
//		} finally {
//			try {
//				if (reader != null) {
//					reader.close();
//				}
//			} catch (IOException io) {
//			}
//		}
//
//		logContent = logContent == null ? "" : logContent;
//		LOG.info(method + pSmbFile.getURL().getHost() + pSmbFile.getURL().getPath()
//						+ StaticParameter.str_enter + logContent);
//		return logContent;
//	}
//
	/**
	 * ファイルを削除する。
	 *
	 * @param pDeleteFile : ファイルパス
	 * @return boolean    : true（成功）／false（失敗）
	 */
	public static boolean fileDeleteByCmdDel(File pDeleteFile) {

		String command = "";
		boolean retFlg = false;

		try {
			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
				return false;
			}

			if (!pDeleteFile.exists()) {
				return true;
			}

			if (!pDeleteFile.isFile()) {
				return false;
			}

			command = "cmd.exe /c del /f /s /q /a " + pDeleteFile.getPath();
			retFlg = ProcessUtil.processExectue(command, 2, 100);

		} catch (Exception e) {
			retFlg = false;
		}
		return retFlg;
	}
//
	/**
	 * ファイルのコピー。
	 *
	 * @param pCopyFrom : コピー元
	 * @param pCopyTo   : コピー先
	 * @return boolean  : true（成功）／false（失敗）
	 */
	public static boolean fileCopyByCmdCopy(File pCopyFrom, File pCopyTo) {

		String command = "";
		boolean retFlg = false;

		try {
			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
				return false;
			}

			if (!pCopyFrom.exists()) {
				return false;
			}

			if (!pCopyFrom.isFile()) {
				return false;
			}

			if (pCopyTo.exists()) {
				fileDelete(pCopyTo);
			}

			if (!pCopyTo.exists()) {
				if (!pCopyTo.getParentFile().exists()) {
					pCopyTo.getParentFile().mkdirs();
				}
			}

			command = "cmd.exe /c copy /Y " + pCopyFrom + " " + pCopyTo;
			retFlg = ProcessUtil.processExectue(command, 2, 100);

		} catch (Exception e) {
			retFlg = false;
		}

		return retFlg;
	}

	/**
	 * ファイルバックアップ。
	 *
	 * @param pTargetFile  : 古いファイル
	 * @param pAppendTimes : 最終更新日の追加
	 * @return boolean     : true（成功）／false（失敗）
	 */
	public static boolean fileBackupByCmdCopy(File pTargetFile, boolean pAppendTimes) {

		String command = "";
		String lastModified = "";
		File copyFileTo = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(DATETIME);
		boolean retFlg = false;

		try {
			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
				return false;
			}

			if (!pTargetFile.exists()) {
				return false;
			}

			if (!pTargetFile.isFile()) {
				return false;
			}

			if (pAppendTimes) {
				lastModified = dateFormat.format(new Date(pTargetFile.lastModified()));
				copyFileTo = new File(pTargetFile.getParentFile(), pTargetFile.getName() + "_bk_" + lastModified);
			} else {
				copyFileTo = new File(pTargetFile.getParentFile(), pTargetFile.getName() + "_bk");
			}

			if (copyFileTo.exists()) {
				fileDelete(copyFileTo);
			}

			command = "cmd.exe /c copy /Y " + pTargetFile + " " + copyFileTo;
			retFlg = ProcessUtil.processExectue(command, 2, 100);

		} catch (Exception e) {
			retFlg = false;
		}

		return retFlg;
	}
//
	/**
	 * ディレクトリを削除する。
	 *
	 * @param pDeleteDir : ファイルパス
	 */
	public static boolean dirDeleteByCmdRmdir(File pDeleteDir) {

		String command = "";
		boolean retFlg = false;

		try {
			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
				return false;
			}

			if (!pDeleteDir.exists()) {
				return false;
			}

			if (!pDeleteDir.isDirectory()) {
				return false;
			}

			command = "cmd.exe /c rmdir /S /Q " + pDeleteDir.getPath();
			retFlg = ProcessUtil.processExectue(command, 1, 100);

		} catch (Exception e) {
			retFlg = false;
		}

		return retFlg;
	}
//
//	/**
//	 * ディレクトリを削除する。
//	 *
//	 * @param pDeleteDir : ファイルパス
//	 */
//	public static boolean dirDeleteByCmdRmdirTry(File pDeleteDir) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		String command = "";
//		boolean retFlg = false;
//
//		try {
//			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
//				LOG.error(method + "The system is not Windows : " + StaticParameter.sys_osname);
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (!pDeleteDir.exists()) {
//				LOG.warn(method + LogUtil.RES_NOTEXIST + pDeleteDir.getPath());
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (!pDeleteDir.isDirectory()) {
//				LOG.warn(method + LogUtil.RES_NOTDIRECTORY + pDeleteDir.getPath());
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (!pDeleteDir.canRead()) {
//				pDeleteDir.setReadable(true);
//			}
//
//			if (!pDeleteDir.canWrite()) {
//				pDeleteDir.setWritable(true);
//			}
//
//			command = "cmd.exe /c rmdir /S /Q " + pDeleteDir.getPath();
//			retFlg = ProcessUtil.processExectue(command, 2, 50);
//
//		} catch (Exception e) {
//			retFlg = false;
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + retFlg);
//		return retFlg;
//	}
//
//	/**
//	 * ファイルのコピー／バックアップ。
//	 *
//	 * @param pTargetFile : コピーから
//	 * @param pBackupTo   : コピーまで（パスが入らないとき、ファイル_bkを作成する）
//	 *
//	 * @return boolean    : true（成功）／false（失敗）
//	 */
//	public static boolean dirBackupByCmdXCopy(File pTargetFile, File pBackupTo) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		String command = "";
//		boolean retFlg = false;
//
//		try {
//			if (!StaticParameter.sys_osname.startsWith(StaticParameter.sys_windows)) {
//				LOG.error(method + "The system is not Windows : " + StaticParameter.sys_osname);
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (!pTargetFile.exists()) {
//				LOG.error(method + LogUtil.RES_NOTEXIST + pTargetFile.getPath());
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (pTargetFile.isFile()) {
//				LOG.error(method + LogUtil.RES_NOTDIRECTORY + pTargetFile.getPath());
//				fileDelete(pTargetFile);
//				LOG.debug(LogUtil.methodExit(method) + retFlg);
//				return false;
//			}
//
//			if (StringUtility.isNullOrEmpty(pBackupTo)) {
//				pBackupTo = new File(pTargetFile.getParent(), pTargetFile.getName() + StaticParameter.file_bk);
//			}
//
//			if (pBackupTo.exists()) {
//				if (pBackupTo.isFile()) {
//					LOG.warn(method + LogUtil.RES_NOTDIRECTORY + pTargetFile.getPath());
//					fileDelete(pBackupTo);
//				}
//			}
//
//			if (!pBackupTo.exists()) {
//				pBackupTo.mkdirs();
//			}
//
//			command = "cmd.exe /c xcopy /y /c /q /e " + pTargetFile.getPath() + " " + pBackupTo.getPath();
//			retFlg = ProcessUtil.processExectue(command, 2, 100);
//
//		} catch (Exception e) {
//			retFlg = false;
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + retFlg);
//		return retFlg;
//	}
//
//	/**
//	 * ファイル重命名。
//	 *
//	 * @param pTargetFile  : 古いファイル
//	 * @param pAppendTimes : 最終更新日の追加
//	 * @return boolean     : true（成功）／false（失敗）
//	 */
//	public static boolean fileBackup(File pTargetFile) {
//
//		String method = LogUtil.getCurrentMethodName();
//		File renameFileTo = null;
//
//		try {
//			if (!pTargetFile.exists()) {
//				LOG.error(method + LogUtil.RES_NOTEXIST + pTargetFile.getPath());
//				return false;
//			}
//
//			renameFileTo = new File(pTargetFile.getParentFile(), pTargetFile.getName() + StaticParameter.file_bk);
//			return fileRename(pTargetFile, renameFileTo);
//
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//			return false;
//		}
//	}
//
//	/**
//	 * ファイル重命名。
//	 *
//	 * @param pTargetFile : 古いファイル
//	 * @param pRenameTo   : 新しファイル名前
//	 * @return boolean    : true（成功）／false（失敗）
//	 */
//	public static boolean fileRenameTo(File pTargetFile, String pToName) {
//
//		String method = LogUtil.getCurrentMethodName();
//
//		try {
//			File renameTo = new File(pTargetFile.getParentFile(), pToName);
//			return fileRename(pTargetFile, renameTo);
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//			return false;
//		}
//	}
//
//	/**
//	 * ファイル重命名。
//	 *
//	 * @param pTargetFile : 古いファイル
//	 * @param pRenameTo   : 新しファイル名前
//	 * @return boolean    : true（成功）／false（失敗）
//	 */
//	public static boolean fileRename(File pTargetFile, File pRenameTo) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		int tryCount = 0;
//		boolean retFlg = false;
//
//		try {
//			if (!pTargetFile.exists()) {
//				LOG.error(method + LogUtil.RES_NOTEXIST + pTargetFile.getPath());
//				return false;
//			}
//
//			if (StringUtility.isNullOrEmpty(pRenameTo)) {
//				LOG.error(method + "The rename of file is null or empty!");
//				return false;
//			}
//
//			do {
//				tryCount++;
//
//				fileDelete(pRenameTo);
//				LOG.debug(method + " FROM " + pTargetFile.getPath() + " -TO- " +  pRenameTo.getPath());
//
//				if (pTargetFile.renameTo(pRenameTo)) {
//					retFlg = true;
//					LOG.debug(LogUtil.methodExit(method) + retFlg);
//					return retFlg;
//				} else {
//					Thread.sleep(50);
//				}
//			} while (tryCount < 2);
//
//			if (tryCount >= 2) {
//				retFlg = false;
//				LOG.warn(method + LogUtil.RES_TIMEOUT + pTargetFile.getPath());
//			}
//
//		} catch (Exception e) {
//			retFlg = false;
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + retFlg);
//		return retFlg;
//	}
//
	/**
	 * ファイルを削除する。
	 *
	 * @param pCheckFile : ファイルパス
	 */
	public static void fileDelete(File pCheckFile) {


		if (pCheckFile != null && pCheckFile.exists()) {
			if (!pCheckFile.canRead()) pCheckFile.setReadable(true);
			if (!pCheckFile.canWrite()) pCheckFile.setWritable(true);

			if (pCheckFile.exists()) {
				if (pCheckFile.isFile()) {
					if (pCheckFile.delete()) {
					} else {
						fileDeleteByCmdDel(pCheckFile);
					}
				} else {
					dirDeleteByCmdRmdir(pCheckFile);
				}
			} else {
			}
		}
	}
//
//	public static boolean fileDownloadTry(SmbFile pSmbFileZip, File pDownloadToPath) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		int tryCount = 0;
//		boolean retFlag = false;
//
//		try {
//			do {
//				tryCount++;
//				LOG.debug(method + "START > try count : " + tryCount);
//				retFlag = FileUtil.fileDownload(pSmbFileZip, pDownloadToPath);
//
//				if (retFlag) {
//					LOG.debug(method + "END   < SUCCESS");
//					LOG.debug(LogUtil.methodExit(method));
//					return true;
//				} else {
//					Thread.sleep(50);
//				}
//			} while (tryCount < 2);
//
//			if (tryCount >= 2) {
//				LOG.debug(method + "END   < TIMEOUT");
//			}
//
//		} catch (Exception e) {
//			retFlag = false;
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		LOG.debug(LogUtil.methodExit(method) + retFlag);
//		return retFlag;
//	}
//
//	public static boolean fileDownload(SmbFile pStoreSerZip, File pToPath) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		File zipFile = null;
//		SmbFileInputStream smbIn = null;
//		BufferedInputStream in = null;
//		BufferedOutputStream out = null;
//		byte buffer[] = new byte[2048];
//		boolean retFlag = false;
//
//		LOG.debug(method + "Download From - " + pStoreSerZip.getPath());
//		LOG.debug(method + "Download  TO  - " + pToPath.getPath());
//
//		try {
//			if (pToPath.exists()) {
//				if (!pToPath.isDirectory()) {
//					if (pToPath.delete()) {
//						pToPath.mkdirs();
//					} else {
//						if (FileUtil.fileDeleteByCmdDel(pToPath)) {
//							pToPath.mkdirs();
//						}
//					}
//				}
//			} else {
//				pToPath.mkdirs();
//			}
//
//			if (!pToPath.canRead()) {
//				pToPath.setReadable(true);
//			}
//
//			if (!pToPath.canWrite()) {
//				pToPath.setWritable(true);
//			}
//
//			if (pStoreSerZip.exists()) {
//				zipFile = new File(pToPath, pStoreSerZip.getName());
//				if (zipFile.exists()) {
//					if (!zipFile.delete()) {
//						FileUtil.fileDelete(zipFile);
//					}
//				}
//
//				out = new BufferedOutputStream(new FileOutputStream(zipFile));
//				smbIn = new SmbFileInputStream(pStoreSerZip);
//				in = new BufferedInputStream(smbIn);
//
//				while (in.read(buffer) != -1) {
//					out.write(buffer);
//					buffer = new byte[2048];
//				}
//
//				out.close();
//				in.close();
//				smbIn.close();
//
//				zipFile.setLastModified(pStoreSerZip.getLastModified());
//				retFlag = true;
//			} else {
//				LOG.error(method + LogUtil.RES_NOTEXIST + pStoreSerZip.getPath());
//			}
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		} finally {
//			try {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					in.close();
//				}
//				if (smbIn != null) {
//					smbIn.close();
//				}
//			} catch (IOException e) {
//			}
//		}
//
//		LOG.debug(LogUtil.methodExit(method)  + "Download Result : " + String.valueOf(retFlag));
//		return retFlag;
//	}
//
	private final static String DATETIME = "yyyyMMddHHmmss";
}
