package ncr.res.mobilepos.constant;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * This class is Singleton to store <env-entry> in web.xml.
 * This is initialized once in WebContextListener and loads necessary env-entries and caches them.
 */
public class EnvironmentEntries {
    // The instance.
    private static EnvironmentEntries instance;

    // Keys
    private static final String KEY_SERVER_ID = "serverID";
    private static final String KEY_IOW_PATH = "iowPath";
    private static final String KEY_TRACE_PATH = "tracePath";
    private static final String KEY_DEBUG_LEVEL = "debugLevel";
    private static final String KEY_SNAP_PATH = "snapPath";
    private static final String KEY_SPM_PATH = "Journalization/spmPath";

    // ServerId for Logger.
    private String serverId;
    // IowPath for Logger.
    private String iowPath;
    // TracePath for DebugLogger.
    private String tracePath;
    // DebugLevel for DebugLogger.
    private int debugLevel;
    // SnapPath for SnapLogger.
    private String snapPath;

    // Spm path for Spm logger.
    private String spmPath;

    /**
     * Constructor.
     * @throws NamingException Throws if initialization fails.
     */
    private EnvironmentEntries(Context initialContext) throws NamingException {
        loadEnvironmentEntries(initialContext);
    }

    /**
     * Loading.
     * @throws NamingException Throws if initialization fails.
     */
    private void loadEnvironmentEntries(Context initialContext) throws NamingException {
        Context context = (Context)initialContext.lookup("java:comp/env");

        serverId = (String)loadProperty(KEY_SERVER_ID, context);
        iowPath = (String)loadProperty(KEY_IOW_PATH, context);

        tracePath = (String)loadProperty(KEY_TRACE_PATH, context);
        debugLevel = (int)loadProperty(KEY_DEBUG_LEVEL, context);

        snapPath = (String)loadProperty(KEY_SNAP_PATH, context);

        spmPath = (String)loadProperty(KEY_SPM_PATH, context);
    }

    /**
     * Loads one property from Context.
     * @param keyName Key name
     * @param context Context
     * @return loaded entry
     */
    private Object loadProperty(String keyName, Context context) throws NamingException {
        return context.lookup(keyName);
    }

    /**
     * Returns the instance.
     * @return This container instance.
     */
    public static EnvironmentEntries getInstance(){
        return instance;
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     */
    public static EnvironmentEntries initInstance(Context initialContext) throws NamingException {
        // Resets instance as null.
        instance = null;
        instance = new EnvironmentEntries(initialContext);
        return instance;
    }

    /**
     * Returns ServerID.
     * @return ServerID.
     */
    public String getServerId() {
        return serverId;
    }

    /**
     * Returns Path to IOWLOG.
     * @return IowPath.
     */
    public String getIowPath() {
        return iowPath;
    }

    /**
     * Return path to trace log.
     * @return tracePath.
     */
    public String getTracePath() {
        return tracePath;
    }

    /**
     * Returns debug level.
     * @return debugLevel.
     */
    public int getDebugLevel() {
        return debugLevel;
    }

    /**
     * Returns snap path for SnapLog.
     * @return snapPath.
     */
    public String getSnapPath() {
        return snapPath;
    }

    /**
     * Returns SPM path.
     * @return spmPath.
     */
    public String getSpmPath() {
        return spmPath;
    }

}
