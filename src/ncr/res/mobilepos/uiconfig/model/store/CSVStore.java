package ncr.res.mobilepos.uiconfig.model.store;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * CSVStore
 */
public class CSVStore {
    // Store Names are never used.
    private String storeNameEnglish;
    private String storeNameJapanese;

    private final List<String> storeIDs;

    // Index in CSV.
    private static final int STORE_NAME_ENGLISH_INDEX = 0;
    private static final int STORE_NAME_JAPANESE_INDEX = 1;
    private static final int STORE_ID_INDEX = 2;
    private static final String CSV_DELIMITER = ",";

    /**
     * Constructor.
     *
     * @param line One CSV line.
     */
    public CSVStore(String line) {
        String[] columns = line.split(CSV_DELIMITER);
        storeIDs = new ArrayList<>();
        // Checks if the line has proper columns,
        if (STORE_ID_INDEX < columns.length) {
            storeNameEnglish = columns[STORE_NAME_ENGLISH_INDEX];
            storeNameJapanese = columns[STORE_NAME_JAPANESE_INDEX];
            storeIDs.addAll(Arrays.asList(columns).subList(STORE_ID_INDEX, columns.length));
        }
    }

    /**
     * Checks if this has given storeID.
     *
     * @param storeID storeID to check.
     * @return true if given storeID is contained. false otherwise.
     */
    public boolean hasStoreID(String storeID) {
        return storeIDs.contains(storeID);
    }
}
