/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * Logger
 *
 * An IoWriter that handles the log messages and sends
 *  output to the specified file.
 * IoWriter provides thread-safe write.
 * Every time write occurs, this opens and closes the file exclusively.
 *
 * Dela Cerna, Jessel (jd185128)
 *
 * Initial code.
 *
 * Campos, Carlos (cc185102)
 *
 *
 * Singleton
 */

package ncr.res.mobilepos.helper;

import java.io.File;
import java.io.IOException;

import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Snap;

/**
 * Inherit from an IoWriter class.
 */
public final class Logger extends IoWriter {
    /**
     * the class instance of Logger
     */
    private static Logger instance;

    /**
     * The message id of General message log.
     */
    public static final String LOG_MSGID = "00";

    /**
     * The message id of No Data found Exception.
     */
    public static final String RES_EXCEP_NODATAFOUND = "80";
    
    /**
     * The message id of file is empty Exception.
     */
    public static final String RES_EXCEP_FILEEMPTY = "81";
    
    /**
     * The message id of Exception.
     */
    public static final String RES_EXCEP_GENERAL = "90";

    /**
     * The message id of SQL Exception.
     */
    public static final String RES_EXCEP_SQL = "91";

    /**
     * The message id of SQL statement Exception.
     */
    public static final String RES_EXCEP_SQLSTATEMENT = "92";

    /**
     * The message id of Parse Exception.
     */
    public static final String RES_EXCEP_PARSE = "93";

    /**
     * The message id of DAO Exception.
     */
    public static final String RES_EXCEP_DAO = "94";

    /**
     * The message id of JAXB Exception.
     */
    public static final String RES_EXCEP_JAXB = "95";

    /**
     * The message id of error restriction.
     */
    public static final String RES_ERROR_RESTRICTION = "96";

    /**
     * The message id of unsupported encoding Exception.
     */
    public static final String RES_EXCEP_ENCODING = "97";

    /**
     * The message id of Not found file Exception.
     */
    public static final String RES_EXCEP_FILENOTFOUND = "98";
    /**
     * The message id of IO Exception.
     */
    public static final String RES_EXCEP_IO = "99";
    /**
     * The message id of Till Exception. (Concurrent access scenario).
     */
    public static final String RES_EXCEP_TILL = "100";
    /**
     * The message id of NamingException.
     */
    public static final String RES_EXCEP_NAMINGEXC = "101";

    /**
     * The message id of SQL statement Exception.
     */
    public static final String RES_EXCEP_JOURNAL = "102";
    /**
     * Create a file
     */
    public static final String RES_EXCEP_CREAT = "103";
    /**
     * file already exists
     */
    public static final String RES_EXCEP_EXISTS = "104";
    /**
     * Set file to writable failed 
     */
    public static final String RES_EXCEP_FILEWRITABLE = "105";
    /**
     * SUCCESS 
     */
    public static final String RES_SUCCESS = "0";
    /**
     * File save failed
     */
    public static final String RES_EXCEP_FILESAVEFAILED = "106";
    /**
     * Removed the schedule task
     */
    public static final String RES_EXCEP_REMOVEDTASK = "107";
    /**
     * The message id of para error
     */
    public static final String RES_PARA_ERR = "108";

    /**
     * Constructor.
     * @param path Path.
     * @param serverId Server ID.
     */
    private Logger(String path, String serverId) {
        super(path, serverId);
    }

    /**
     * Returns single instance of Logger object.
     * @return Single instance of Logger.
     */
    public static Logger getInstance() {
        return instance;
    }

    /**
     * Initializes IowLogger with Path and ServerId
     * @param path Path to SPM file.
     * @param serverId ServerId
     * @return IowLogger
     * @throws IOException
     */
    public static Logger initInstance(String path, String serverId) {
        instance = null;
        // Creats parent directory if necessary.
        File directory = new File(path);
        if(!directory.isDirectory()  && !directory.mkdirs() ) {
            // If directory doesn't exist and mkdirs fails.
            // Throws Exception.
            throw new IllegalStateException("Failed to create directory for IOWLOG Path:" + path);
        }
        instance = new Logger(path, serverId);
        return instance;
    }

    /**
     * Write error, warning or log messages
     * to an output file specify by an IoWriter.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     */
    public void write(final char level, final String progname,
            final int id, final CharSequence message) {
        try {
            this.write(level, progname, Integer.toHexString(id), message);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Write error, warning or log messages
     * to an output file specify by an IoWriter.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     */
    @Override
    public void write(final char level,
            final String progname, final String id,
            final CharSequence message) {
        try {
            super.write(level, progname, id, message);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Write SnapInfo array.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     * @param infos - SnapInfo array
     */
    @Override
    public void write(final char level, final String progname,
            final String id, final CharSequence message,
            final Snap.SnapInfo[] infos) {
        try {
            super.write(level, progname, id, message, infos);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Write SnapInfo array.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     * @param info - SnapInfo
     */
    public void write(final char level, final String progname,
            final String id, final CharSequence message,
            final Snap.SnapInfo info) {
        try {
            this.write(level, progname, id, message,
                           new Snap.SnapInfo[] {info});
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Prevents the Cloning of the Logger object because it is a singleton.
     * @return not used
     * @throws CloneNotSupportedException Exception thrown when a
     *                                     cloning of the object took place.
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    /**
     * Setter for the function Name indicator for the preceding series of logs.
     *
     * @param progName - the current class body name
     * @param functionName - the current function body name
     */
    public void logFunctionEntry(final String progName,
            final String functionName) {
        try {
            this.write(IoWriter.LOG, progName, LOG_MSGID, "+ " + functionName);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Setter for the function Name indicator for the preceding series of logs.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param params - the parameters of the function to log
     */
    public void logFunctionEntry(final String progName,
            final String functionName,
            final String ... params) {
        try {
            StringBuilder messageBuff =
                new StringBuilder("+ " + functionName + ": Params{");
            for (String param : params) {
                messageBuff.append("(" + param + ")");
            }
            messageBuff.append("}");
            this.write(IoWriter.LOG, progName,
                    LOG_MSGID, messageBuff.toString());
        } catch (Exception e) {
            // ignore logger exception
        }

    }

    /**
     * Function to log an indication that the function is about to exit.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param message - additional message to log
     */
    public void logFunctionExit(final String progName,
            final String functionName, final String message) {
        try {
            this.write(IoWriter.LOG, progName, LOG_MSGID,
                    "- " + functionName + ": " + message);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log an indication that the function is about to exit.
     *
     * @param progName for the log
     * @param functionName for the log
     * @param code to include in the log entry
     * @param message to include in the function exit log
     */
    public void logFunctionExit(final String progName,
            final String functionName,
            final String code, final String message) {
        try {
            this.write(IoWriter.LOG, progName, code,
                    "- " + functionName + ": " + message);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log a normal flow or message.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param logMessage message to include in the log entry
     */
    public void logNormal(final String progName,
            final String functionName, final String logMessage) {
        try {
            this.write(IoWriter.LOG, progName, LOG_MSGID,
                    functionName + ": " + logMessage);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log an abnormal flow.
     * or message (e.g SQL and other exceptions)
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param code error code to include in the log
     * @param logMessage message to include in the log entry
     */
    public void logError(final String progName, final String functionName,
            final String code, final String logMessage) {
        try {
            this.write(IoWriter.ERROR, progName,
                    code, functionName + ": " + logMessage);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log an abnormal flow. or message (e.g SQL and other
     * exceptions)
     *
     * @param progName
     *            - the current class body name
     * @param code
     *            error code to include in the log
     * @param logMessage
     *            message to include in the log entry
     * @param throwable
     *            exception object
     */
    public void logSnapException(final String progName, final String code,
            final CharSequence logMessage, final Throwable throwable) {
        try {
            this.write(IoWriter.ERROR, progName, code, logMessage + "\n"
                    + StringUtility.printStackTrace(throwable), SnapLogger
                    .getInstance().write("Exception", throwable));
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log an abnormal flow. or message (e.g SQL and other exceptions)
     * @param progName - the current class body name
     * @param code - error code to include in the log
     * @param logMessage - message to include in the log entry
     * @param throwable - exception object
     */
    public void logSnapException(	final char level,
    								final String progName,
    								final String code,
    								final CharSequence logMessage,
    								final Throwable throwable) {
        try {
            this.write(level, progName, code, logMessage,
            		SnapLogger.getInstance().write(throwable.getClass().getSimpleName() , throwable));
        } catch (Exception e) {
            // ignore logger exception
        }
    }
    
    /**
     * Function to log a warning message.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param code error code to include in the log
     * @param logMessage message to include in the log entry
     */
    public void logWarning(final String progName, final String functionName,
            final String code, final String logMessage) {
        try {
            this.write(IoWriter.WARNING, progName, code,
                    functionName + ": " + logMessage);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log a warning message.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param code error code to include in the log
     * @param throwable to print the stack trace error message.
     */
    public void logWarning(final String progName, final String functionName,
            final String code, final Throwable throwable) {
        try {
            this.write(IoWriter.WARNING, progName, code, functionName + ": "
                    + StringUtility.printStackTrace(throwable));
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log an alert message.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param code error code to include in the log
     * @param logMessage message to include in the log entry
     */
    public void logAlert(final String progName,
            final String functionName, final String code,
            final String logMessage) {
        try {
            this.write(IoWriter.ALERT, progName, code,
                    functionName + ": " + logMessage);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Function to log a snap line.
     * @param progName - the current class body name
     * @param functionName - the current function body name
     * @param code error code to include in the log
     * @param logMessage message to include in the log entry
     * @throws ClassNotFoundException - exception thrown when
     *  class is not found
     */
    public void logSnapLine(final String progName,
            final String functionName, final String code,
            final String logMessage) throws ClassNotFoundException {
        try {
            this.write(IoWriter.SNAP_LINE, progName, code,
                    functionName + ": " + logMessage);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Output to Snap file as given log level.
     * @param level log level
     * @param progname - the current progress name
     * @param functionName - the current method name
     * @param message - comment message
     * @param info - Snap information
     */
    public void logSnap(char level, final String progname, final String functionName, final String message,
                        final Snap.SnapInfo info) {
        try {
            this.write(level, progname, RES_EXCEP_GENERAL, functionName + ":" + message, info);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Output to Snap file.
     * @param progname - the current progress name
     * @param functionName - the current method name
     * @param message - comment message
     * @param info - Snap information
     */
    public void logSnap(final String progname, final String functionName,
            final String message, final Snap.SnapInfo info) {
        try {
            this.write(IoWriter.ALERT, progname, RES_EXCEP_GENERAL,
                    functionName + ":" + message, info);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Output to Snap file.
     * @param progname - the current progress name
     * @param functionName - the current method name
     * @param message - comment message
     * @param infos - array of Snap informations
     */
    public void logSnap(final String progname, final String functionName,
            final String message, final Snap.SnapInfo[] infos) {
        try {
            this.write(IoWriter.ALERT, progname, RES_EXCEP_GENERAL,
                    functionName + ":" + message, infos);
        } catch (Exception e) {
            // ignore logger exception
        }
    }
    /**
     * Output to Snap file.
     * @param progname - the current progress name
     * @param code	- the log message identification.
     * @param functionName - the current method name
     * @param message - comment message
     * @param infos - array of Snap informations
     */
    public void logSnap(final String progname, final String code,
            final String functionName, final String message,
            final Snap.SnapInfo[] infos) {
        try {
            this.write(IoWriter.ALERT, progname, code,
                    functionName + ":" + message, infos);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    public void logTransaction(final String progname, final String code, 
                               final String message, final String bigdata) {
        try {
            this.write(IoWriter.LOG, progname, code, message,
                  SnapLogger.getInstance().write("transaction", bigdata));
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Output stack trace error message.
     * @param progName - the current class body name
     * @param code - error code to include in the log
     * @param logMessage - message to include in the log entry
     * @param t throwable - exception object
     */
    public void logAlert(	final String progName,
    						final String code,
    						final CharSequence logMessage,
    						final Throwable t) {
        logSnapException(IoWriter.ALERT, progName, code, logMessage, t);
    }

	/**
     * Output stack trace error message.
     * @param progName - the current class body name
     * @param code - error code to include in the log
     * @param logMessage - message to include in the log entry
     * @param t throwable - exception object
     */
    public void logAlert(final String progName, final String code,
                         String funcName,
            final CharSequence logMessage, final Throwable t) {
        // for convenience to replace existing wrong logAlert method.
        // ignore funcName. it can be looked up from exception trace.
        logSnapException(IoWriter.ALERT, progName, code, logMessage, t);
    }
    

    /**
     * Log bytes with error flag.
     *
     * @param progname
     *            - the current class body name
     * @param code
     *            - error code to include in the log
     * @param message
     *            - message to include in the log entry
     * @param data
     *            - bytes to be log.
     */
    public void logBytes(final String progname, final String code, 
                         final String message, byte[] data) {
        try {
            this.write(IoWriter.ERROR, progname, code, message,
                  SnapLogger.getInstance().write("dump", data));
        } catch (Exception e) {
            // ignore logger exception
        }
    }
}
