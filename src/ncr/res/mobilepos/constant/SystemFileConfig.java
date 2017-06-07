package ncr.res.mobilepos.constant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.naming.NamingException;

public class SystemFileConfig {
    private static SystemFileConfig instance;

    private String companyId;
    private String storeId;
    private static final String COMPANYID_FILENAME = "COMPANYID";
    private static final String STOREID_FILENAME = "STOREID";


    private SystemFileConfig(String systemPath) throws IOException, NamingException {
        loadSystemPathFiles(systemPath);
    }

    /**
     * Initializes the instance.
     * @throws NamingException throws if initialization fails.
     * @throws IOException
     */
    public static SystemFileConfig initInstance(String systemPath) throws NamingException, IOException {
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
    private void loadSystemPathFiles(String systemPath) throws NamingException, IOException {
        //1
        companyId = loadFile(systemPath, COMPANYID_FILENAME);
        //2
        storeId = loadFile(systemPath, STOREID_FILENAME);
    }

    /**
     * Loads systemPath files.
     * @throws IOException
     * @throws Exception
     */
    private String loadFile(String systemPath, String fileName) throws NamingException,IOException{

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
