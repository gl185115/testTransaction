package ncr.res.mobilepos.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.naming.NamingException;

/**
 * SystemFileConfig, a container class for a host information.
 * Parameter files should be located under system path, usually x:/software/ncr/res/sys.
 */
public class SystemFileConfig {
    private static SystemFileConfig instance;

    /**
     * 1. CompanyId
     */
    private String companyId;
    /**
     * 2. StoreId
     */
    private String storeId;

    private static final String COMPANYID_FILENAME = "COMPANYID";
    private static final String STOREID_FILENAME = "STOREID";


    private SystemFileConfig(String systemPath) throws IOException {
        loadSystemPathFiles(systemPath);
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     * @throws IOException
     */
    public static SystemFileConfig initInstance(String systemPath) throws IOException {
        // Resets instance as null.
        instance = null;
        instance = new SystemFileConfig(systemPath);
        return instance;
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
     * Returns the instance.
     * @return This container instance.
     */
    public static SystemFileConfig getInstance(){
        return instance;
    }

    /**
     * Loads systemPath variables.
     * @throws IOException
     * @throws Exception
     */
    private void loadSystemPathFiles(String systemPath) throws IOException {
        //1
        companyId = readFirstLine(systemPath, COMPANYID_FILENAME);
        //2
        storeId = readFirstLine(systemPath, STOREID_FILENAME);
    }

    /**
     * Loads systemPath files. null for empty file.
     * @param systemPath
     * @param fileName
     * @return the first line of the file. Null for empty file.
     * @throws IOException
     */
    public static String readFirstLine(final String systemPath, final String fileName) throws IOException{
        File file = new File(systemPath + File.separator + fileName);
        if (!file.isFile() || !file.exists()) {
            throw new IOException(fileName + " file not found." + "(" + systemPath + ")");
        }

        // Reads only the first line.
        String value;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            value = reader.readLine();
            if (value == null){
                throw new IOException("File is empty. " + file.getAbsolutePath());
            }
            value = value.trim();
        }
        return value;
    }

}
