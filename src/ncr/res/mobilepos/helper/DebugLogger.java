package ncr.res.mobilepos.helper;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ncr.realgate.util.Trace;

/**
 * class that handles debug logs.
 *
 */
public final class DebugLogger {
    /**
     * the class instance of the destinationDirPath to output file.
     */
    private static String destinationDirPath;
    /**
     * the class instance of the logLevel.
     */
    private static Integer logLevel;
    /**
     * Store Trace for every thread. <ThreadId, Trace>
     */
    private static Map<Long, Trace> traceMap;
    /**
     * File extension for trace log.
     */
    private static final String TRACE_FILE_EXTENSION = ".txt";
    /**
     * Filename prefix.
     */
    private static final String TRACE_FILENAME_PREFIX = "debug_threadid_";

    private static final String PROG_NAME = "DebugLogger";

    /**
     * Gets true after initialization.
     */
    private static boolean isInitialized = false;

    /** The default constructor.  */
    private DebugLogger() {
    }

    /**
     * Initializes this debug logger.
     * @param tracePath Path to store log
     * @param debugLevel Debug logLevel
     */
    public static void initInstance(String tracePath, int debugLevel) {
        isInitialized = false;
        // Creates parent directory if necessary.
        File directory = new File(tracePath);
        if(!directory.isDirectory() && !directory.mkdirs()) {
            // If directory doesn't exist and mkdirs fails.
            throw new IllegalStateException("Failed to create directory for MethodTraceLogger" +
                    " Path:" + tracePath);
        }
        destinationDirPath = tracePath;
        logLevel = debugLevel;
        traceMap = new ConcurrentHashMap<>();
        isInitialized = true;
    }


    /**
     * getTrace can be accessed by multi-threads. But each thread has different key, threadid.
     * @param threadID
     * @return
     */
    private static Trace getTrace(final long threadID) {
        // If given threadID doesn't exist in the map, then create new one and put it into map.
        // This is performed atomically.
        return traceMap.computeIfAbsent(threadID, key -> newTrace(key));
    }

    /**
     * Creates new Trace.
     * @param threadId threadId.
     * @return created instance.
     */
    private static Trace newTrace(long threadId) {
        Trace trace = new Trace(destinationDirPath +  File.separator
                + TRACE_FILENAME_PREFIX + threadId + TRACE_FILE_EXTENSION);
        trace.setDebugLevel(logLevel);
        return trace;
    }

    /**
     * gets the debug printer.
     * @param threadID - id of the calling thread
     * @param cls - class to create the printer from
     * @return instance of the trace printer
     */
    public static Trace.Printer getDbgPrinter(final long threadID, final Class cls) {
        if(!isInitialized) {
            return null;
        }
        Trace tr = getTrace(threadID);
        Trace.Printer tp = tr.createPrinter(cls);
        tp.setTimestampMode(true);
        return tp;
    }

    /**
     * Explicitly close the Trace instance.
     * @param threadID
     */
    public static void closeDbgPrinter(final long threadID) {
        Trace tr = traceMap.remove(threadID);
        if(tr != null) {
            tr.close();
        }
    }

    /**
     * Explicitly close all the Trace instance.
     */
    public static void closeAllDbgPrinter() {
        for(Trace tr : traceMap.values()) {
            tr.close();
        }
        traceMap.clear();
    }

    /**
     * sets the debug logLevel.
     * @param dbgLevel - the logLevel of the debug to set
     */
    public static void setDebugLevel(final int dbgLevel) {
        synchronized (traceMap) {
            logLevel = dbgLevel;
            for(Trace trace : traceMap.values()) {
                trace.setDebugLevel(dbgLevel);
            }
        }
    }
}
