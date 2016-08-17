package ncr.res.mobilepos.uiconfig.constants;

import ncr.res.mobilepos.helper.Logger;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * UiConfigProperties. Singleton.
 */
public class UiConfigProperties {
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    // Singleton instance.
    private static UiConfigProperties myInstance;

    // Properties Keys.
    private static final String KEY_CUSTOM_MAINTENANCE_BASE_PATH = "customMaintenanceBasePath";
    private static final String KEY_CUSTOM_RESOURCE_BASE_PATH = "customResourceBasePath";
    private static final String KEY_SCHEDULE_FILE_PATH = "scheduleFilePath";
    private static final String KEY_STORES_CSV_FILE_PATH = "storesCsvFilePath";
    private static final String KEY_DEPLOY_STATUS_FILE_PATH = "deployStatusFilePath";

    // Absolute path to 'custom'.
    private String customMaintenanceBasePath;
    private String customResourceBasePath;
    // Relative paths.
    private String scheduleFilePath;
    private String storesCsvFilePath;
    private String deployStatusFilePath;

    /**
     * Constructor.
     *
     * @throws NamingException
     */
    private UiConfigProperties() {
        // Loads UiConfig related properties from web.xml.
        Context context;
        try {
            context = (Context) new InitialContext().lookup("java:comp/env");
        } catch (NamingException e) {
            // Failed to load env.
            e.printStackTrace();
            return;
        }
        // Path to 'custom' directory.
        customMaintenanceBasePath = loadProperty(KEY_CUSTOM_MAINTENANCE_BASE_PATH, context);
        customResourceBasePath = loadProperty(KEY_CUSTOM_RESOURCE_BASE_PATH, context);
        scheduleFilePath = loadProperty(KEY_SCHEDULE_FILE_PATH, context);

        storesCsvFilePath = loadProperty(KEY_STORES_CSV_FILE_PATH, context);
        deployStatusFilePath = loadProperty(KEY_DEPLOY_STATUS_FILE_PATH, context);
    }

    /**
     * Loads one property and records error when it fails.
     *
     * @param keyName
     * @param context
     * @return
     */
    private String loadProperty(String keyName, Context context) {
        try {
            return (String) context.lookup(keyName);
        } catch (NamingException e) {
            // Failed to load a property.
            LOGGER.logAlert(this.getClass().getSimpleName(), "loadProperty", e.getMessage(), e);
        }
        return "";
    }

    /**
     * Singleton factory.
     *
     * @return
     */
    public static UiConfigProperties getInstance() {
        if (myInstance == null) {
            // Create the Singleton.
            myInstance = new UiConfigProperties();
        }
        // Return the Singleton.
        return myInstance;
    }

    // Simple getters.
    public String getCustomMaintenanceBasePath() {
        return customMaintenanceBasePath;
    }
    
    public String getCustomResourceBasePath() {
        return customResourceBasePath;
    }

    public String getStoreCsvFilePath() {
        return storesCsvFilePath;
    }

    public String getDeployStatusFileName() {
        return deployStatusFilePath;
    }

    public String getScheduleFilePath() {
        return scheduleFilePath;
    }

    // Getters for full paths.
    public String getStoresCsvFileFullPath() {
        return customResourceBasePath + storesCsvFilePath;
    }

    public String getScheduleFileFullPath() {
        return customResourceBasePath + scheduleFilePath;
    }

    public String getDeployStatusFileFullPath() {
        return customResourceBasePath + deployStatusFilePath;
    }

}
