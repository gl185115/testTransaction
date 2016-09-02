/*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * SQLStatement
    *
    * SQLStatement which handles the reading of sqlstatements
    *     from the file(.xml format) by mapping the key-value.
    *
    * Dela Cerna, Jessel
    */

package ncr.res.mobilepos.property;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.Logger;

/**
 * SQLStatement is the class which handles the reading of sql statements
 *     from the file(.xml format) by mapping the key-value.
 *
 */
public class SQLStatement {	
	/**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The file path of the Sql Statements.
     */
    private static final String STATEMENT_FILE_PATH = "sql_statements.xml";
    /**
     * The Properties of the class.
     */
    private Properties properties;
    /**
     * The first index parameter in the prepared Statement.
     */
    public static final int PARAM1 = 1;
    /**
     * The second index parameter in the prepared Statement.
     */
    public static final int PARAM2 = 2;
    /**
     * The third index parameter in the prepared Statement.
     */
    public static final int PARAM3 = 3;
    /**
     * The fourth index parameter in the prepared Statement.
     */
    public static final int PARAM4 = 4;
    /**
     * The fifth index parameter in the prepared Statement.
     */
    public static final int PARAM5 = 5;
    /**
     * The sixth index parameter in the prepared Statement.
     */
    public static final int PARAM6 = 6;
    /**
     * The seventh index parameter in the prepared Statement.
     */
    public static final int PARAM7 = 7;
    /**
     * The eighth index parameter in the prepared Statement.
     */
    public static final int PARAM8 = 8;
    /**
     * The ninth index parameter in the prepared Statement.
     */
    public static final int PARAM9 = 9;
    /**
     * The tenth index parameter in the prepared Statement.
     */
    public static final int PARAM10 = 10;
    /**
     * The eleventh index parameter in the prepared Statement.
     */
    public static final int PARAM11 = 11;
    /**
     * The twelveth index parameter in the prepared Statement.
     */
    public static final int PARAM12 = 12;
    /**
     * The thirteenth index parameter in the prepared Statement.
     */
    public static final int PARAM13 = 13;
    /**
     * The 14 index parameter in the prepared Statement.
     */
    public static final int PARAM14 = 14;
    /**
     * The 15 index parameter in the prepared Statement.
     */
    public static final int PARAM15 = 15;
    /**
     * The 16 index parameter in the prepared Statement.
     */
    public static final int PARAM16 = 16;
    /**
     * The 17 index parameter in the prepared Statement.
     */
    public static final int PARAM17 = 17;
    /**
     * The 18 index parameter in the prepared Statement.
     */
    public static final int PARAM18 = 18;
    /**
     * The 19 index parameter in the prepared Statement.
     */
    public static final int PARAM19 = 19;
    /**
     * The 20 index parameter in the prepared Statement.
     */
    public static final int PARAM20 = 20;
    /**
     * The 21 index parameter in the prepared Statement.
     */
    public static final int PARAM21 = 21;
    /**
     * The 22 index parameter in the prepared Statement.
     */
    public static final int PARAM22 = 22;
    /**
     * The 23 index parameter in the prepared Statement.
     */
    public static final int PARAM23 = 23;
    /**
     * The 24 index parameter in the prepared Statement.
     */
    public static final int PARAM24 = 24;
    /**
     * The 25 index parameter in the prepared Statement.
     */
    public static final int PARAM25 = 25;
    /**
     * The 26 index parameter in the prepared Statement.
     */
    public static final int PARAM26 = 26;
    /**
     * The 27 index parameter in the prepared Statement.
     */
    public static final int PARAM27 = 27;
    /**
     * The 28 index parameter in the prepared Statement.
     */
    public static final int PARAM28 = 28;
    /**
     * The 29 index parameter in the prepared Statement.
     */
    public static final int PARAM29 = 29;
    /**
     * The 30 index parameter in the prepared Statement.
     */
    public static final int PARAM30 = 30;
    // RES 3.1 ‘Î‰ž START
    /**
     * The 31 index parameter in the prepared Statement.
     */
    public static final int PARAM31 = 31;
    /**
     * The 32 index parameter in the prepared Statement.
     */
    public static final int PARAM32 = 32;
    /**
     * The 33 index parameter in the prepared Statement.
     */
    public static final int PARAM33 = 33;
    /**
     * The 34 index parameter in the prepared Statement.
     */
    public static final int PARAM34 = 34;
	/**
     * The 35 index parameter in the prepared Statement.
     */
    public static final int PARAM35 = 35;

    /**
     * The 36 index parameter in the prepared Statement.
     */
    public static final int PARAM36 = 36;
    /**
     * The 37 index parameter in the prepared Statement.
     */
    public static final int PARAM37 = 37;
    /**
     * The 38 index parameter in the prepared Statement.
     */
    public static final int PARAM38 = 38;
    /**
     * The 39 index parameter in the prepared Statement.
     */
    public static final int PARAM39 = 39;
    /**
     * The 40 index parameter in the prepared Statement.
     */
    public static final int PARAM40 = 40;
    /**
     * The 41 index parameter in the prepared Statement.
     */
    public static final int PARAM41 = 41;
    /**
     * The 42 index parameter in the prepared Statement.
     */
    public static final int PARAM42 = 42;
    /**
     * The 43 index parameter in the prepared Statement.
     */
    public static final int PARAM43 = 43;
    /**
     * The 44 index parameter in the prepared Statement.
     */
    public static final int PARAM44 = 44;
    /**
     * The 45 index parameter in the prepared Statement.
     */
    public static final int PARAM45 = 45;
    /**
     * The 46 index parameter in the prepared Statement.
     */
    public static final int PARAM46 = 46;
    /**
     * The 47 index parameter in the prepared Statement.
     */
    public static final int PARAM47 = 47;
    /**
     * The 48 index parameter in the prepared Statement.
     */
    public static final int PARAM48 = 48;
    /**
     * The 49 index parameter in the prepared Statement.
     */
    public static final int PARAM49 = 49;
    /**
     * The 50 index parameter in the prepared Statement.
     */
    public static final int PARAM50 = 50;
    /**
     * The 51 index parameter in the prepared Statement.
     */
    public static final int PARAM51 = 51;
    /**
     * The 52 index parameter in the prepared Statement.
     */
    public static final int PARAM52 = 52;
    /**
     * The 53 index parameter in the prepared Statement.
     */
    public static final int PARAM53 = 53;
    /**
     * The 54 index parameter in the prepared Statement.
     */
    public static final int PARAM54 = 54;
    /**
     * The 55 index parameter in the prepared Statement.
     */
    public static final int PARAM55 = 55;
    /**
     * The 56 index parameter in the prepared Statement.
     */
    public static final int PARAM56 = 56;
    /**
     * The 57 index parameter in the prepared Statement.
     */
    public static final int PARAM57 = 57;
    /**
     * The 58 index parameter in the prepared Statement.
     */
    public static final int PARAM58 = 58;
    /**
     * The 59 index parameter in the prepared Statement.
     */
    public static final int PARAM59 = 59;
    /**
     * The 60 index parameter in the prepared Statement.
     */
    public static final int PARAM60 = 60;
    /**
     * The 61 index parameter in the prepared Statement.
     */
    public static final int PARAM61 = 61;
    /**
     * The 62 index parameter in the prepared Statement.
     */
    public static final int PARAM62 = 62;
    /**
     * The 63 index parameter in the prepared Statement.
     */
    public static final int PARAM63 = 63;
    /**
     * The 64 index parameter in the prepared Statement.
     */
    public static final int PARAM64 = 64;
    /**
     * The 65 index parameter in the prepared Statement.
     */
    public static final int PARAM65 = 65;
    /**
     * The 66 index parameter in the prepared Statement.
     */
    public static final int PARAM66 = 66;
    /**
     * The 67 index parameter in the prepared Statement.
     */
    public static final int PARAM67 = 67;
    /**
     * The 68 index parameter in the prepared Statement.
     */
    public static final int PARAM68 = 68;/**
     * The 69 index parameter in the prepared Statement.
     */
    public static final int PARAM69 = 69;
    /**
     * The 70 index parameter in the prepared Statement.
     */
    public static final int PARAM70 = 70;
    /**
     * The 71 index parameter in the prepared Statement.
     */
    public static final int PARAM71 = 71;
    /**
     * The 72 index parameter in the prepared Statement.
     */
    public static final int PARAM72 = 72;
    /**
     * The 73 index parameter in the prepared Statement.
     */
    public static final int PARAM73 = 73;
    /**
     * The 74 index parameter in the prepared Statement.
     */
    public static final int PARAM74 = 74;
    /**
     * The 75 index parameter in the prepared Statement.
     */
    public static final int PARAM75 = 75;
    /**
     * The 76 index parameter in the prepared Statement.
     */
    public static final int PARAM76 = 76;

    /**
     * The 77 index parameter in the prepared Statement.
     */
    public static final int PARAM77 = 77;
    /**
     * The 78 index parameter in the prepared Statement.
     */
    public static final int PARAM78 = 78;

    /**
     * The 79 index parameter in the prepared Statement.
     */
    public static final int PARAM79 = 79;

    /**
     * The 80 index parameter in the prepared Statement.
     */
    public static final int PARAM80 = 80;

    /**
     * The 81 index parameter in the prepared Statement.
     */
    public static final int PARAM81 = 81;

    /**
     * The 82 index parameter in the prepared Statement.
     */
    public static final int PARAM82 = 82;
    /**
     * The 83 index parameter in the prepared Statement.
     */
    public static final int PARAM83 = 83;
    /**
     * The 84 index parameter in the prepared Statement.
     */
    public static final int PARAM84 = 84;
    /**
     * The 85 index parameter in the prepared Statement.
     */
    public static final int PARAM85 = 85;
    /**
     * The 86 index parameter in the prepared Statement.
     */
    public static final int PARAM86 = 86;
    /**
     * The 87 index parameter in the prepared Statement.
     */
    public static final int PARAM87 = 87;
    /**
     * The 88 index parameter in the prepared Statement.
     */
    public static final int PARAM88 = 88;
    /**
     * The 89 index parameter in the prepared Statement.
     */
    public static final int PARAM89 = 89;
    /**
     * The 90 index parameter in the prepared Statement.
     */
    public static final int PARAM90 = 90;
    /**
     * The 91 index parameter in the prepared Statement.
     */
    public static final int PARAM91 = 91;
    /**
     * The 92 index parameter in the prepared Statement.
     */
    public static final int PARAM92 = 92;
    /**
     * The 93 index parameter in the prepared Statement.
     */
    public static final int PARAM93 = 93;
    /**
     * The 94 index parameter in the prepared Statement.
     */
    public static final int PARAM94 = 94;
    /**
     * The 95 index parameter in the prepared Statement.
     */
    public static final int PARAM95 = 95;
    /**
     * The 96 index parameter in the prepared Statement.
     */
    public static final int PARAM96 = 96;
    /**
     * The 97 index parameter in the prepared Statement.
     */
    public static final int PARAM97 = 97;
    
    // RES 3.1 ‘Î‰ž END

    /**
     * Bill Pugh Singleton pattern.
     */
    private static class SingletonHelper {
        private static final SQLStatement INSTANCE = new SQLStatement();
    }

    /**
     * The Default Constructor.
     * @throws SQLStatementException  Exception thrown when instantiation fails.
     */
    private SQLStatement() {
        readFile();
    }

    /**
      * Retrieves SQLStatement instance.
      * @return SQLStatement instance
      * @throws SQLStatementException   Exception when error occurs.
      */
    public static SQLStatement getInstance() {
        return SingletonHelper.INSTANCE;
    }

    /**
      * Reads the properties represented by the XML document.
      * @throws SQLStatementException   Exception when error occurs.
      */
    private void readFile() throws SQLStatementException {
        try(InputStream stream = SQLStatement.class.getResourceAsStream(STATEMENT_FILE_PATH)) {
            properties = new Properties();
            properties.loadFromXML(stream);
        } catch (IOException | NullPointerException ex) {
            throw new SQLStatementException(
                    "Failed to load SQL statement file in its class path."
                            + STATEMENT_FILE_PATH, ex);
        }
    }

    /**
      * Retrieves property value.
      * @param property name
      * @throws SQLStatementException
      * @return property value
      */
    public final String getProperty(final String property) {
        return properties.getProperty(property);
    }
}
