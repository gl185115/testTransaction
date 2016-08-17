package ncr.res.mobilepos.promotion.helper;

import java.util.Map;

/**
 * The Class TerminalItemsHandler. A helper class that handles the manipulation
 * of the list of TerminalItems through add, get and delete method.
 */
public final class TerminalItemsHandler {

    /**
     * Instantiates a new terminal items handler.
     */
    private TerminalItemsHandler() {
    }

    /**
     * Adds the TerminalItem to HashMap.
     *
     * @param retailStoreId
     *            the store identifier string.
     * @param workStationId
     *            the workstation identifier string.
     * @param sequenceNo
     *            the sequence number of the terminalitem.
     * @param terminalItemsHashMap
     *            the HashMap containing a list of TerminalItems.
     * @return The new {@link TerminalItem} bein added.
     */
    public static TerminalItem add(final String retailStoreId,
            final String workStationId, final String sequenceNo,
            final Map<String, TerminalItem> terminalItemsHashMap) {
        TerminalItem terminalItem = new TerminalItem(retailStoreId,
                workStationId, sequenceNo);
        add(terminalItem, terminalItemsHashMap);
        return terminalItem;
    }

    /**
     * Gets the TerminalItem from HashMap.
     *
     * @param retailStoreId
     *            the store identifier string.
     * @param workStationId
     *            the workstation identifier string.
     * @param sequenceNo
     *            the sequence number of the terminalitem.
     * @param map
     *            the HashMap containing a list of TerminalItems.
     * @return the TerminalItem
     */
    public static TerminalItem get(final String retailStoreId,
            final String workStationId, final String sequenceNo,
            final Map<String, TerminalItem> map) {
        if (null == map) {
            return null;
        }
        String id =  constructTerminalItemId(retailStoreId, workStationId);
        TerminalItem terminalItem = map.get(id);
        if (null == terminalItem
                || !terminalItem.getSequenceNumber().equals(sequenceNo)) {
            return null;
        } else {
            return terminalItem;
        }
    }

    /**
     * Deletes the TerminalItem from HashMap.
     *
     * @param retailStoreId
     *            the store identifier string.
     * @param workStationId
     *            the workstation identifier string.
     * @param map
     *            the HashMap containing a list of TerminalItems.
     * @return true if success else false
     */
    public static boolean delete(final String retailStoreId,
            final String workStationId,
            final Map<String, TerminalItem> map) {
        String id = constructTerminalItemId(retailStoreId, workStationId);
        TerminalItem terminalItem = map.get(id);
        if (null != terminalItem) {
            map.remove(terminalItem.getId());
            return true;
        }
        return false;
    }

    /**
     * Adds the TerminalItem to HashMap.
     * @param terminalItem The TerminalItem to be added.
     * @param terminalItemsHashMap  The Map<?, ?> for Terminal Item.
     */
    public static void add(final TerminalItem terminalItem,
            final Map<String, TerminalItem> terminalItemsHashMap) {
        String terminalItemId = terminalItem.getId();
        // If terminalitem exists, replace the new terminalitem
        if (terminalItemsHashMap.containsKey(terminalItemId)) {
            terminalItemsHashMap.remove(terminalItemId);
        }
        terminalItemsHashMap.put(terminalItemId, terminalItem);
    }

    public static String constructTerminalItemId(String retailStoreId, String workStationId) {
    	 String terminalItemId = String.format("%6s",
    			 retailStoreId).replace(" ", "0");
    	 terminalItemId = terminalItemId + String.format("%6s",
        		 workStationId).replace(" ", "0");
    	 return terminalItemId;
    }
    
    public static int[] sortIntArray(int[] array) {
        int temp;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }

    public static double[] sortDoubleArray(double[] array) {
        double temp;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                }
            }
        }
        return array;
    }
    
    public static Map<String,Integer> addMapToMap(Map<String,Integer> map1, Map<String,Integer>map2){
        for(Map.Entry<String, Integer> newMap1 : map1.entrySet()){
            for(Map.Entry<String, Integer> newMap2 : map2.entrySet()){
                if(newMap1.getKey().equals(newMap2.getKey())){
                    map1.put(newMap1.getKey(), newMap1.getValue() + newMap2.getValue());
                }
            }
        }
        map2.putAll(map1);
    return map2;
}

}
