package ncr.res.mobilepos.helper;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;

import ncr.realgate.util.MultiSnap;
import ncr.realgate.util.Snap;

/**
 * Class that handles Snap logs.
 */
public final class SnapLogger {
    /**
     * The path of snap log file output.
     */
    private static String path;
    /**
     * Instance of MultiSnap for output.
     */
    private static MultiSnap snap;
    /**
     * Instance of MultiSnap (volatile).
     */
    private static volatile SnapLogger instance;
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "SnapLogger";
    
    /**
     * Constructor of SnapLogger.
     */
    private SnapLogger() {
        try {
            if (path == null || "".equals(path)) {
                Context env =
                        (Context) new InitialContext().lookup("java:comp/env");
                    path = (String) env.lookup("snapPath");
            }
            File directory  = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();               
            }
            snap = new MultiSnap(path, "SNAP");
        } catch (Exception e) {
        	LOGGER.logAlert(PROG_NAME, "Constructor", Logger.RES_EXCEP_GENERAL , e);
        } finally {
            File directory  = new File(path);
            if (!directory.exists() && directory.mkdirs()) {
                snap = new MultiSnap(path, "SNAP");
            }
        }
    }
    /**
     * Creates single instance.
     * @return snap.
     */
    private static SnapLogger tryCreateInstance() {
        if (instance == null) {
            instance = new SnapLogger();
        }
        return instance;
    }
    /**
     * Get the Instance of snap logger.
     * @return instance of snap logger.
     */
    public static synchronized SnapLogger getInstance() {
        SnapLogger snapInstance = instance;
        if (snapInstance == null) {
            snapInstance = tryCreateInstance();
        }
        return snapInstance;
    }
    /**
     * Write message to Snap file.
     * @param comment Output max 16 bytes comment.
     *        If comment is null, change to "".
     * @param data output data.
     * @return SnapInfo
     */
    public Snap.SnapInfo write(final CharSequence comment, final CharSequence data) {
		Snap.SnapInfo snapInfo = null;
		try {
			snapInfo = snap.write(comment, data);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, "SnapLogger.write", Logger.RES_EXCEP_GENERAL , e);
		}
		return snapInfo;
    }

    /*******************************************************/
    /**
     * Snap throwable with its stack trace.
     * @param comment max 16 chars comment.
     * @param data throwable which was caught.
     * @return the filename and offset pair to be logged.
     */
    public Snap.SnapInfo write(final CharSequence comment, final Throwable data) {
        return snap.write(comment, data);
    }

    /**
     * Snap binary data.
     * @param comment max 16 chars comment.
     * @param data bytes to be dumped.
     * @return the filename and offset pair to be logged.
     */
    public Snap.SnapInfo write(final CharSequence comment, final byte[] data) {
        return snap.write(comment, data, 0, data.length);
    }

    /**
     * Snap binary data.
     * @param comment max 16 chars comment.
     * @param data bytes to be dumped.
     * @param offset from
     * @param length to from + length
     * @return the filename and offset pair to be logged.
     */
    public Snap.SnapInfo write(final CharSequence comment, final byte[] data, final int offset, final int length) {
        return snap.write(comment, data, offset, length);
    }

    /**
     * return Snap itself.
     */
    public Snap getSnap() {
        return snap;
    }
}
