package ncr.res.mobilepos.constant;

import javax.naming.Context;
import javax.naming.NamingException;

import ncr.res.mobilepos.helper.StringUtility;

/**
 * This class is Singleton to store <env-entry> in web.xml.
 * This is initialized once in WebContextListener and loads necessary env-entries and caches them.
 */
public class EnvironmentEntries {
    // The instance.
    private static EnvironmentEntries instance;

    // Keys
    // 1
    private static final String KEY_SERVER_ID = "serverID";
    // 2
    private static final String KEY_DEBUG_LEVEL = "debugLevel";
    // 3
    private static final String KEY_PRINT_PAPER_LENGTH = "Printpaperlength";
    // 4
    private static final String KEY_CUSTOM_MAINTENANCE_BASE_PATH ="customMaintenanceBasePath";
    // 5
    private static final String KEY_CUSTOM_RESOURCE_BASE_PATH = "customResourceBasePath";
    // 6
    private static final String KEY_IOW_PATH = "iowPath";
    // 7
    private static final String KEY_UI_CONSOLE_LOG_PATH = "UiConsoleLogPath";
    // 8
    private static final String KEY_DEVICE_LOG_PATH = "deviceLogPath";
    // 9
    private static final String KEY_TRACE_PATH = "tracePath";
    // 10
    private static final String KEY_SPM_PATH = "Journalization/spmPath";
    // 11
    private static final String KEY_SNAP_PATH = "snapPath";
    // 12
    private static final String KEY_REPORT_FORMAT_NEW_PATH = "ReportFormatNewPath";
    // 13
    private static final String KEY_NR_RCPT_FORMAT_PATH = "nrRcptFormatPath";
    // 14
    private static final String KEY_LOCAL_PRINTER = "localPrinter";
    // 15
    private static final String KEY_SCHEDULE_FILE_PATH = "scheduleFilePath";
    // 16
    private static final String KEY_CUSTOM_PARAM_BASE_PATH = "customParamBasePath";
    // 17
    private static final String KEY_PARA_BASE_PATH = "paraBasePath";
    // 18
    private static final String KEY_POSLOG_TRANSFER_STATUS_COLUMN = "POSLogTransferStatusColumn";
    private static final String DEFAULT_POSLOG_TRANSGER_STATUS_COLUMN = "SendStatus1";

    // 1
    // ServerId for Logger.
    private String serverId;
    // 2
    // DebugLevel for DebugLogger.
    private int debugLevel;
    // 3
    // PrintPaperLength
    private String printPaperLength;
    // 4
    // CustomMaintenanceBasePath
    private String customMaintenanceBasePath;
    // 5
    // CustomResourceBasePath
    private String customResourceBasePath;
    // 6
    // IowPath for Logger.
    private String iowPath;
    // 7
    // UIConsoleLogPath
    private String uiConsoleLogPath;
    // 8
    // DeviceLogPath
    private String deviceLogPath;
    // 9
    // TracePath for DebugLogger.
    private String tracePath;
    // 10
    // Spm path for Spm logger.
    private String spmPath;
    // 11
    // SnapPath for SnapLogger.
    private String snapPath;
    // 12
    // ReportFormatNewPath
    private String reportFormatNewPath;
    // 13
    // nrRcptFormatPath
    private String nrRcptFormatPath;
    // 14
    // LocalPrinter
    private String localPrinter;
    // 15
    // ScheduleFilePath
    private String scheduleFilePath;
    // 16
    // CustomParamBasePath
    private String customParamBasePath;
    // 17
    // ParaBasePath
    private String paraBasePath;
    // 18
    // POSLogTransferStatusColumn
    private String poslogTransferStatusColumn;

    /**
     * Constructor.
     * @throws NamingException Throws if initialization fails.
     * @throws Exception
     */
    private EnvironmentEntries(Context initialContext) throws NamingException {
        loadEnvironmentEntries(initialContext);
    }

    /**
     * Loading.
     * @throws NamingException Throws if initialization fails.
     * @throws Exception
     */
    private void loadEnvironmentEntries(Context initialContext) throws NamingException {
        Context context = (Context)initialContext.lookup("java:comp/env");
        //1
        serverId = loadProperty(KEY_SERVER_ID, context);
        //2
        debugLevel = loadPropertyInt(KEY_DEBUG_LEVEL, context);
        //3
        printPaperLength = loadProperty(KEY_PRINT_PAPER_LENGTH, context);
        //4
        customMaintenanceBasePath = loadProperty(KEY_CUSTOM_MAINTENANCE_BASE_PATH, context);
        //5
        customResourceBasePath = loadProperty(KEY_CUSTOM_RESOURCE_BASE_PATH, context);
        //6
        iowPath = loadProperty(KEY_IOW_PATH, context);
        //7
        uiConsoleLogPath = loadProperty(KEY_UI_CONSOLE_LOG_PATH, context);
        //8
        deviceLogPath = loadProperty(KEY_DEVICE_LOG_PATH, context);
        //9
        tracePath = loadProperty(KEY_TRACE_PATH, context);
        //10
        spmPath = loadProperty(KEY_SPM_PATH, context);
        //11
        snapPath = loadProperty(KEY_SNAP_PATH, context);
        //12
        reportFormatNewPath = loadProperty(KEY_REPORT_FORMAT_NEW_PATH, context);
        //13
        nrRcptFormatPath = loadProperty(KEY_NR_RCPT_FORMAT_PATH, context);
        //14
        localPrinter = loadProperty(KEY_LOCAL_PRINTER, context);
        //15
        scheduleFilePath = loadProperty(KEY_SCHEDULE_FILE_PATH, context);
        //16
        customParamBasePath = loadProperty(KEY_CUSTOM_PARAM_BASE_PATH, context);
        //17
        paraBasePath = loadProperty(KEY_PARA_BASE_PATH, context);
        //18
        poslogTransferStatusColumn = loadParameter(
                KEY_POSLOG_TRANSFER_STATUS_COLUMN, context, DEFAULT_POSLOG_TRANSGER_STATUS_COLUMN);
    }

    /**
     * Loads one parameter from context. If no entry found, then returns default value.
     * If the entry is empty, then return invalid state exception.
     * @param keyName Key name
     * @param context Context
     * @param defaultValue Default value when entry is missing.
     * @return loaded entry.
     */
    private String loadParameter(String keyName, Context context, String defaultValue) {
        try {
            String value = loadProperty(keyName, context);
            if(StringUtility.isNullOrEmpty(value)){
                return null;
            }
            return value;
        } catch (NamingException e){
            return defaultValue;
        }
    }

    /**
     * Loads one property from Context.
     * @param keyName Key name
     * @param context Context
     * @return loaded entry
     */
    private int loadPropertyInt(String keyName, Context context) throws NamingException {
        return (int)context.lookup(keyName);
    }

    /**
     * Loads one property from Context.
     * @param keyName Key name
     * @param context Context
     * @return loaded entry
     */
    private String loadProperty(String keyName, Context context) throws NamingException {
        return (String)context.lookup(keyName);
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
     * @throws Exception
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

    /**
     * Returns printPaperLength
     * @return printPaperLength
     */
    public String getPrintPaperLength() {
        return printPaperLength;
    }

    /**
     * Returns customMaintenanceBasePath
     * @return customMaintenanceBasePath
     */
    public String getCustomMaintenanceBasePath() {
        return customMaintenanceBasePath;
    }

    /**
     * Returns customResourceBasePath
     * @return customResourceBasePath
     */
    public String getCustomResourceBasePath() {
        return customResourceBasePath;
    }

    /**
     * Returns uiConsoleLogPath
     * @return uiConsoleLogPath
     */
    public String getUiConsoleLogPath() {
        return uiConsoleLogPath;
    }

    /**
     * Returns deviceLogPath
     * @return deviceLogPath
     */
    public String getDeviceLogPath() {
        return deviceLogPath;
    }

    /**
     * Returns reportFormatNewPath
     * @return reportFormatNewPath
     */
    public String getReportFormatNewPath() {
        return reportFormatNewPath;
    }

    /**
     * Returns nrRcptFormatPath
     * @return nrRcptFormatPath
     */
    public String getNrRcptFormatPath() {
        return nrRcptFormatPath;
    }

    /**
     * Returns localPrinter
     * @return localPrinter
     */
    public String getLocalPrinter() {
        return localPrinter;
    }

    /**
     * Returns scheduleFilePath
     * @return scheduleFilePath
     */
    public String getScheduleFilePath() {
        return scheduleFilePath;
    }

    /**
     * Returns custom param base path.
     * @return customParamBasePath
     */
    public String getCustomParamBasePath() {
        return customParamBasePath;
    }

    /**
     * Returns para bast path.
     * @return paraBasePath
     */
    public String getParaBasePath() {
        return paraBasePath;
    }

    /**
     * Returns Poslog Transfer Status Column.
     * @return poslogTransferStatusColumn
     */
	public String getPoslogTransferStatusColumn() {
		return poslogTransferStatusColumn;
	}

}
