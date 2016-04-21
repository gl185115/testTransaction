/*
 * Copyright (c) 2016 NCR/JAPAN Corporation SW-R&D
 *
 * ConsoleLogger
 *
 * An UserLog that handles the log messages and sends
 *  output to the specified file.
 *
 * $Id:$
 *
 *
 * Singleton
 */

package ncr.res.mobilepos.helper;

import java.io.File;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.UserLog;

/**
 * Inherit from an UserLog class.
 * @author miura
 *
 */
public final class ConsoleLogger extends UserLog {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "ConsoleLogger";
    /**
     * the class instance of the userlog. (volatile)
     */
    private static volatile UserLog userlog;
    /**
     * the class instance of Logger
     */
    private static volatile ConsoleLogger instance;
    /**
     * The message id of General message log.
     */
    public static final String LOG_MSGID = "00";
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
     * Constructor of the Logger.
     */
    private ConsoleLogger() {
        String path = null;
        String serverid = null;
        try {
            //Get environment data
            Context env = (Context) new InitialContext().lookup("java:comp/env");
            path = (String) env.lookup("UiConsoleLogPath");
            serverid = (String) env.lookup("serverID");
            File file = new File(path);
            File directory = new File(file.getParent());
            if (!directory.exists()) {
                directory.mkdirs();
            }
            userlog = new UserLog(file.getParent(), serverid, file.getName());
        } catch (NamingException e) {
            LOGGER.logAlert(PROG_NAME, "Constructor", Logger.RES_EXCEP_NAMINGEXC, "Failed to lookup path. " + e.getMessage());
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, "Constructor", Logger.RES_EXCEP_GENERAL, "Failed to get file path. " + e.getMessage());
        }
    }

    /**
     * Private method that creates an instance of the Logger.
     * @return UserLog Base class of the Logger object.
     */
    private static ConsoleLogger tryCreateInstance() {
        if (instance == null) {
            instance = new ConsoleLogger();
        }
        return instance;
    }

    /**
     * Public method that creates an instance of the Logger.
     * @return UserLog Base class of the Logger object.
     */
    public static synchronized ConsoleLogger getInstance() {
        ConsoleLogger userloginstance = instance;
        if (userloginstance == null) {
            userloginstance = tryCreateInstance();
        }
        return userloginstance;
    }

    /**
     * Write error, warning or log messages
     * to an output file specify by an UserLog.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     */
    public void write(final char level, final String progname,
            final int id, final CharSequence message) {
        try {
            userlog.write(level, progname, Integer.toHexString(id), message);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Write error, warning or log messages
     * to an output file specify by an UserLog.
     * @param level - level of the log
     * @param progname - name of the class or program
     * @param id - message id
     * @param message - the message to log
     */
    public void write(final char level,
            final String progname, final String id,
            final CharSequence message) {
        try {
            userlog.write(level, progname, id, message);
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
    public void write(final char level, final String progname,
            final String id, final CharSequence message,
            final Snap.SnapInfo[] infos) {
        try {
            userlog.write(level, progname, id, message, infos);
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
            userlog.write(level, progname, id, message,
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
            this.write(UserLog.LOG, progName, LOG_MSGID, "+ " + functionName);
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
            this.write(UserLog.LOG, progName,
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
            this.write(UserLog.LOG, progName, LOG_MSGID,
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
            this.write(UserLog.LOG, progName, code,
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
            this.write(UserLog.LOG, progName, LOG_MSGID,
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
            this.write(UserLog.ERROR, progName,
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
     * @param functionName
     *            - the current function body name
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
            this.write(UserLog.ERROR, progName, code, logMessage + "\n"
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
    public void logSnapException(final char level,
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
            this.write(UserLog.WARNING, progName, code,
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
            this.write(UserLog.WARNING, progName, code, functionName + ": "
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
            this.write(UserLog.ALERT, progName, code,
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
            this.write(UserLog.SNAP_LINE, progName, code,
                    functionName + ": " + logMessage);
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
            this.write(UserLog.ALERT, progname, RES_EXCEP_GENERAL,
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
            this.write(UserLog.ALERT, progname, RES_EXCEP_GENERAL,
                    functionName + ":" + message, infos);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    /**
     * Output to Snap file.
     * @param progname - the current progress name
     * @param code - the log message identification.
     * @param functionName - the current method name
     * @param message - comment message
     * @param infos - array of Snap informations
     */
    public void logSnap(final String progname, final String code,
            final String functionName, final String message,
            final Snap.SnapInfo[] infos) {
        try {
            this.write(UserLog.ALERT, progname, code,
                    functionName + ":" + message, infos);
        } catch (Exception e) {
            // ignore logger exception
        }
    }

    public void logTransaction(final String progname, final String code,
                               final String message, final String bigdata) {
        try {
            write(UserLog.LOG, progname, code, message,
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
     * @param throwable - exception object
     */
    public void logAlert(final String progName, final String code,
                         final CharSequence logMessage, final Throwable t) {
        logSnapException(UserLog.ALERT, progName, code, logMessage, t);
    }

    /**
     * Output stack trace error message.
     * @param progName - the current class body name
     * @param code - error code to include in the log
     * @param logMessage - message to include in the log entry
     * @param throwable - exception object
     */
    public void logAlert(final String progName, final String code,
                         String funcName,
            final CharSequence logMessage, final Throwable t) {
        // for convenience to replace existing wrong logAlert method.
        // ignore funcName. it can be looked up from exception trace.
        logSnapException(UserLog.ALERT, progName, code, logMessage, t);
    }

    /**
     * Log bytes with error flag.
     *
     * @param progName - the current class body name
     * @param code - error code to include in the log
     * @param message - message to include in the log entry
     * @param data - bytes to be log.
     */
    public void logBytes(final String progname, final String code,
                         final String message, byte[] data) {
        try {
            write(UserLog.ERROR, progname, code, message,
                  SnapLogger.getInstance().write("dump", data));
        } catch (Exception e) {
            // ignore logger exception
        }
    }
}
