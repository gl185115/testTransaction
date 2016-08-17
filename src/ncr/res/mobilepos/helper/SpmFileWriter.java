package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Single Instance of FileWriter for SPM File.
 * @author JD185128
 *
 */
public class SpmFileWriter extends FileWriter {
	/**
	 * Single instance of SpmFileWriter object.
	 */
	private static volatile SpmFileWriter instance;
	/**
	 * Validates creation of header titles.
	 */
	private boolean hasHeader = false;
	
	/**
	 * Singleton constructor.
	 * @param file	target file.
	 * @param isAppend true if appended to the last line.
	 * @throws IOException if directory file does not exist.
	 */
	private SpmFileWriter(File file, boolean isAppend) throws IOException{
		super(file, isAppend);
	}

	/**
	 * Invokes single instance of SpmFileWriter object.
	 * 
	 * @param file
	 *            the target file.
	 * @param isAppend
	 *            true if appended to the last line.
	 * @return Single instance of SpmFileWriter.
	 * @throws IOException
	 *             if cannot create file to its path.
	 */
	public static SpmFileWriter getInstance(File file, boolean isAppend) throws IOException{
		if(instance == null){
			instance = new SpmFileWriter(file, isAppend);
		}
		return instance;
	}

	/**
	 * Closes the file stream.
	 * 
	 * @throws IOException
	 *             if file stream was closed or null.
	 */
	public void close() throws IOException {
		if (instance != null) {
			super.close();
			instance = null;
		}
	}

	/**
	 * Writes to Spm File.
	 * 
	 * @param filedata
	 *            String data to write to file.
	 * @throws IOException
	 *             if file stream was closed or null.
	 */
	public void write(String filedata) throws IOException {
		if (instance != null) {
			if (filedata.equals(JrnSpm.HEADER)) {
				if (!hasHeader) {
					super.write(filedata);
					hasHeader = true;
				}
			} else {
				super.write(filedata);
			}
		}
	}
}
