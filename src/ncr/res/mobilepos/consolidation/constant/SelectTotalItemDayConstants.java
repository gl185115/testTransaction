/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SelectTotalItemDayConstants
 *
 * SelectTotalItemDayConstants is a class that enumerates all the column
 *  index values for
 * certain prepared statement on TXU_TOTAL_ITEMDAY table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/***
 * This class enumerates the numeric constants that signifies the position of
 * the field in the string for selecting entries in TXU_TOTAL_ITEMDAY table
 * found in the sql_statements.xml which will be used for the prepared
 * statement.
 *
 * @see SQLServerConsolidationDAO
 */
public final class SelectTotalItemDayConstants {
    /** Default Constructor. */
    private SelectTotalItemDayConstants() {    	
    }
    /**
     * corpid index number.
     */
    public static final int CORPID = 1;
    /**
     * storeid index number.
     */
    public static final int STOREID = 2;
    /**
     * summarydateid index number.
     */
    public static final int SUMMARYDATEID = 3;
    /**
     * salesprice index number.
     */
    public static final int SALESPRICE = 4;
    /**
     * dpt index number.
     */
    public static final int DPT = 5;
    /**
     * line index number.
     */
    public static final int LINE = 6;
    /**
     * class index number.
     */
    public static final int CLASS = 7;
    /**
     * plu index number.
     */
    public static final int PLU = 8;
}
