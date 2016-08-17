package ncr.res.mobilepos.helper;

import java.io.File;

/**
 * Utility class for file handling. 
 * @author RES
 *
 */
public class FileHandler {
	private static final Logger LOGGER = (Logger) Logger.getInstance(); 
	/***
	 * Creates directory path.<br/>
	 * Logs to IOWLog if cannot create directory path.
	 * @param dirPath
	 * @param ioWriter
	 * @return
	 */
	public static boolean createDirectory(String dirPath) {
		boolean isCreated = false;
		File directory = new File(dirPath);
		if (!directory.exists()) {
			isCreated = directory.mkdir();
			if (!isCreated) {
				LOGGER.logAlert("FileHdlr", "FileHandler.createDirectory",
						Logger.RES_EXCEP_FILENOTFOUND, dirPath
								+ "is not created.");
			}
		}
		return isCreated;
	}
}
