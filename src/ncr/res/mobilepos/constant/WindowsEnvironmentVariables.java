package ncr.res.mobilepos.constant;

import javax.naming.NamingException;

/**
 * Container to store values from Windows Environment Variables.
 */
public class WindowsEnvironmentVariables {
    // The instance.
    private static WindowsEnvironmentVariables instance;
    // 1
    private static final String KEY_SYSTEM_PATH = "SYS";
    // 1
    // ServerId for Logger.
    private String systemPath;

    /**
     * Constructor.
     *
     * @throws NamingException Throws if initialization fails.
     */
    private WindowsEnvironmentVariables() {
        loadWindowsEnvironmentVariables();
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     */
    public static WindowsEnvironmentVariables initInstance() {
        // Resets instance as null.
        instance = null;
        instance = new WindowsEnvironmentVariables();
        return instance;
    }

    /**
     * Loads environment variables.
     * @throws NamingException Throws if initialization fails.
     */
    private void loadWindowsEnvironmentVariables() {
        //1
        systemPath = System.getenv(KEY_SYSTEM_PATH);
    }

    /**
     * Returns system path,
     * @return system path.
     */
    public String getSystemPath() {
        return systemPath;
    }

    /**
     * Returns the instance.
     * @return This container instance.
     */
    public static WindowsEnvironmentVariables getInstance(){
        return instance;
    }


}
