package ncr.res.mobilepos.helper;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.realgate.util.Trace;

/**
 * class that handles debug logs.
 *
 */
public final class DebugLogger {
    /**
     * the class instance of the path to output file.
     */
    private static String path = null;
    /**
     * the class instance of the level.
     */
    private static Integer level = null;
    /**
     * Store Trace for every thread.
     */
    private static Map<Integer, Trace> traceMap
                                            = new HashMap<Integer, Trace>();
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger

    private static final String PROG_NAME = "DebugLogger";

    /** The default constructor.  */
    private DebugLogger() { 
    	
    }

    /**
     * @param threadID - the id of the thread that the
     * logger is running on.
     * @return Trace
     */
    private static Trace createTrace(final int threadID) {
        Trace tr = null;
        try {
            synchronized (traceMap) {
                if (path == null || level == null) {
                    Context env =
                        (Context) new InitialContext().lookup("java:comp/env");

                    path = (String) env.lookup("tracePath");
                    level = (Integer) env.lookup("debugLevel");

                    File directory = new File(path);
                    if (!directory.exists()) {
                        directory.mkdir();                       
                    }
                }
            }
            // Get Trace from hash map.
            tr = traceMap.get(threadID);
            if (tr == null) {
                tr = new Trace(path + "\\"
                        + "debug_threadid_" + threadID + ".txt");
                tr.setDebugLevel(level);
                synchronized (traceMap) {
                    traceMap.put(threadID, tr);
                }
            }
        } catch (NamingException e) {
        	LOGGER.logAlert(PROG_NAME, PROG_NAME+".createTrace", Logger.RES_EXCEP_GENERAL, "Failed to lookup path. "+e.getMessage());
        }
        return tr;
    }

    /**
     * gets the debug printer.
     * @param threadID - id of the calling thread
     * @param cls - class to create the printer from
     * @return instance of the trace printer
     */
    public static Trace.Printer getDbgPrinter(
            final long threadID, final Class cls) {
        Trace tr = createTrace((int) threadID);
        Trace.Printer tp = tr.createPrinter(cls);
        tp.setTimestampMode(true);
        return tp;
    }
    /**
     * sets the debug level.
     * @param dbgLevel - the level of the debug to set
     */
    @SuppressWarnings("rawtypes")
    public static void setDebugLevel(final int dbgLevel) {
        synchronized (traceMap) {
            level = dbgLevel;
            Iterator iter = traceMap.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                Trace tr = (Trace) entry.getValue();
                tr.setDebugLevel(dbgLevel);
                traceMap.put((Integer) entry.getKey(), tr);
            }
        }
    }

    /**
     * Get the Method Name that invoke this method.
     * @return  The Method Name.
     */
    public static String getCurrentMethodName() {
        return Thread.currentThread()
        .getStackTrace()[2].getMethodName(); //The method calling this function
                                             //is in third depth from the stack
    }
}
