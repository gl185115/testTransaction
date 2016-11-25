package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.IOException;

import ncr.realgate.util.MultiSnap;
import ncr.realgate.util.Snap;

/**
 * Class that handles Snap logs.
 */
public final class SnapLogger extends MultiSnap {
    /**
     * Instance of SnapLogger.
     */
    private static SnapLogger instance;

    /**
     * Class name for
     */
    private static final String PROG_NAME = "SnapLogger";

    /**
     * Snap log file.
     */
    private static final String SNAP_NAME = "SNAP";

    /**
     * Constructor.
     * @param path path for snap files.
     */
    private SnapLogger(String path) {
        super(path , SNAP_NAME);
    }

    /**
     * Get the Instance of snap logger.
     * @return instance of multi snap.
     */
    public static synchronized SnapLogger getInstance() {
        return instance;
    }

    /**
     * Initializes SnapLogger instance with given snapPath.
     * @param snapPath snapPath to write snap log.
     * @return SnapLogger instance.
     */
    public static SnapLogger initInstance(String snapPath) {
        instance = null;
        // Creats parent directory if necessary.
        File directory = new File(snapPath);
        if(!directory.isDirectory() &&  !directory.mkdirs() ) {
            // If directory doesn't exist and mkdirs fails.
            throw new IllegalStateException("Failed to create directory for SnapLogger Path:" + snapPath);
        }
        instance = new SnapLogger(snapPath);
        return instance;
    }

    @Override
    public final void close() {
        throw new UnsupportedOperationException(
                "Don't close SnapLogger directly, call SnapLogger.closeInstance() instead.");
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
     * @throws IOException if file stream was closed or null.
     */
    public static void closeInstance() throws IOException{
        if(instance != null){
            instance.closeInternally();
            instance = null;
        }
    }

    /**
     * Write message to Snap file.
     * @param comment Output max 16 bytes comment.
     *        If comment is null, change to "".
     * @param data output data.
     * @return SnapInfo
     */
    @Override
    public Snap.SnapInfo write(final CharSequence comment, final CharSequence data) {
        return super.write(comment, data);
    }

    /**
     * Snap throwable with its stack trace.
     * @param comment max 16 chars comment.
     * @param data throwable which was caught.
     * @return the filename and offset pair to be logged.
     */
    @Override
    public Snap.SnapInfo write(final CharSequence comment, final Throwable data) {
        return super.write(comment, data);
    }

    /**
     * Snap binary data.
     * @param comment max 16 chars comment.
     * @param data bytes to be dumped.
     * @return the filename and offset pair to be logged.
     */
    public Snap.SnapInfo write(final CharSequence comment, final byte[] data) {
        return this.write(comment, data, 0, data.length);
    }

    /**
     * Snap binary data.
     * @param comment max 16 chars comment.
     * @param data bytes to be dumped.
     * @param offset from
     * @param length to from + length
     * @return the filename and offset pair to be logged.
     */
    @Override
    public Snap.SnapInfo write(final CharSequence comment, final byte[] data, final int offset, final int length) {
        return super.write(comment, data, offset, length);
    }

}
