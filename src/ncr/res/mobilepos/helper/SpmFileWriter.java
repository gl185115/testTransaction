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
	private static SpmFileWriter instance;
	/**
	 * Validates creation of header titles.
	 */
	private boolean hasHeader = false;
    /** SPM filename. */
    public static final String SPM_FILENAME = "SPM_JOURNALIZATION";
    /** Class name. */
    private static final String PROG_NAME = "SpmFileWriter";

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
     * Initializes SpmFileWriter.
     * @param spmDirPath Path to SPM file.
     * @param serverId ServerId.
     * @param append true if opens Writer with append mode.
     * @return SpmFileWriter instance.
     * @throws IOException if cannot create file to its path.
     */
    public static SpmFileWriter initInstance(String spmDirPath, String serverId, boolean append)
            throws IOException {
		instance = null;
        // Creates parent directory if necessary.
        File spmDir = new File(spmDirPath);
        if(!spmDir.isDirectory() && !spmDir.mkdirs() ) {
            // If directory doesn't exist and mkdirs fails.
            throw new IllegalStateException("Failed to create directory for " +
                    "SpmFileWriter Path:" + spmDirPath);
        }

        String spmFilePath = spmDirPath + File.separator + SPM_FILENAME + "_" + serverId;
        File spmFile = new File(spmFilePath);

        instance = new SpmFileWriter(spmFile, append);
        return instance;
    }

	/**
	 * Returns SpmFileWriter. This method does not create new instance.
	 * If no instance is available, then returns null.
	 * @return SpmFileWriter
	 */
	public static SpmFileWriter getInstance() {
		return instance;
	}

    /**
     * Closes SpmFileWriter. This is not supported.
     * close() should be controlled by factory method, SpmFileWriter.closeInstance().
     * @throws IOException
     *             if file stream was closed or null.
     */
    @Override
    public final void close() {
        throw new UnsupportedOperationException(
                "Don't close SpmFileWriter directly, call SpmFileWriter.closeInstance() instead.");
    }

    /**
     * Closes this singleton instance. Only allows private access.
     * @throws IOException
     */
    private void closeInternally() throws IOException {
        super.close();
    }

    /**
     * Closes the file stream.
     * @throws IOException
     *             if file stream was closed or null.
     */
    public static void closeInstance() throws IOException{
        if(instance != null){
            instance.closeInternally();
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
		if (filedata.equals(JrnSpm.HEADER)) {
			if (hasHeader) {
				// Skips if it attempts to write header again.
				return;
			}
			hasHeader = true;
		}
		super.write(filedata);
	}
}
