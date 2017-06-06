package ncr.res.mobilepos.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.naming.NamingException;
import javax.servlet.ServletContext;

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

    private static final String COMPANYID_FILENAME = "COMPANYID";

    private static final String STOREID_FILENAME = "STOREID";

    // 1 ServerId for Logger.
    private String systemPath;
    // 2 ServerType
    private String serverType;

    private String companyId;
    private String storeId;
    private String businessDate;

    private static ServletContext context; // to access the web.xml

    /**
     * Constructor.
     *
     * @throws NamingException Throws if initialization fails.
     * @throws IOException
     */
    private WindowsEnvironmentVariables() throws NamingException, IOException {
        loadWindowsEnvironmentVariables();
        loadSystemPathFiles();
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     * @throws IOException
     */
    public static WindowsEnvironmentVariables initInstance() throws NamingException, IOException {
        // Resets instance as null.
        instance = null;
        instance = new WindowsEnvironmentVariables();
        return instance;
    }

    /**
     * Loads environment variables.
     * @throws Exception
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
     * Returns company Id
     * @return company Id
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * Returns store Id
     * @return store Id
     */
    public String getStoreId() {
        return storeId;
    }

    /**
     * Returns business Date
     * @return business Date
     */
    public String getBusinessDate() {
        return businessDate;
    }

    /**
     * Returns the instance.
     * @return This container instance.
     */
    public static WindowsEnvironmentVariables getInstance(){
        return instance;
    }


    /**
     * Loads systemPath variables.
     * @throws IOException
     * @throws Exception
     */
    private void loadSystemPathFiles() throws NamingException, IOException {
        //1
        companyId = loadFile(COMPANYID_FILENAME);
        //2
        storeId = loadFile(STOREID_FILENAME);
    }

    /**
     * Loads systemPath files.
     * @throws IOException
     * @throws Exception
     */
    private String loadFile(String fileName) throws NamingException,IOException{

    	BufferedReader reader = null;
    	String value = null;

    	try {
    		File file = new File(systemPath + File.separator + fileName);

    		if(!file.isFile() || !file.exists()) {
    			throw new NamingException(fileName + " file not found." + "(" + systemPath + ")");
            }

    		reader = new BufferedReader(new FileReader(file));
    		value = reader.readLine();
            if (value != null){
            	value = value.trim();
            }

    	} catch (IOException e) {
    		throw e;
    	} finally {
            if (reader != null) {
                try {
                	reader.close();
                } catch (IOException e1) {
                    throw e1;
                }
            }
        }

    	return value;
    }
}
