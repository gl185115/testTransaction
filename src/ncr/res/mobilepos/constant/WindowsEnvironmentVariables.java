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
    // 2
    private static final String KEY_SERVER_TYPE = "SERVERTYPE";

    // 1 ServerId for Logger.
    private String systemPath;
    // 2 ServerType
    private String serverType;

    /**
     * Constructor.
     *
     * @throws NamingException Throws if initialization fails.
     */
    private WindowsEnvironmentVariables() throws NamingException {
        loadWindowsEnvironmentVariables();
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     */
    public static WindowsEnvironmentVariables initInstance() throws NamingException {
        // Resets instance as null.
        instance = null;
        instance = new WindowsEnvironmentVariables();
        return instance;
    }

    /**
     * Loads environment variables.
     * @throws NamingException
     */
    private void loadWindowsEnvironmentVariables() throws NamingException {
        //1
        systemPath = loadVariable(KEY_SYSTEM_PATH);
        //2
        serverType = loadVariable(KEY_SERVER_TYPE);
    }

    /**
     * Loads one parameter from windows system environment.
     * NamingException is thrown if the variable is not defined in the system environment.
     * @param keyName Key name
     * @return loaded variable
     * @throws NamingException is thrown if the variable is not defined in the system environment
     */
    private String loadVariable(String keyName) throws NamingException {
        String value = System.getenv(keyName);
        if(value == null){
            throw new NamingException(keyName + " is not defined in Windows Environment Variables.");
        }
        return value;
    }

    /**
     * Returns system path,
     * @return system path.
     */
    public String getSystemPath() {
        return systemPath;
    }

    /**
     * Returns if server type is enterprise as true. (or false for host.)
     * @return true: ENTERPRISE, false:STORE
     */
    public boolean isServerTypeEnterprise() {
        return ServerTypes.ENTERPRISE.equalsIgnoreCase(serverType);
    }


    /**
     * Returns the instance.
     * @return This container instance.
     */
    public static WindowsEnvironmentVariables getInstance(){
        return instance;
    }

}
