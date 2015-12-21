/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * InsrtTotalItemDayConstants
 *
 * class enumerates the numeric values which signifies the of the
 * field in the string for inserting a new entry in TXU_TOTAL_ITEMDAY
 *
 * Menesses, Chris Niven
 */
package ncr.res.mobilepos.consolidation.constant;

/***
 * This class enumerates the numeric values which signifies the of the field in
 * the string for inserting a new entry in TXU_TOTAL_ITEMDAY table found in the
 * sql_statements.xml which will be used for the prepared statement.
 *
 * @see SQLServerConsolidationDAO
 */
public final class InsrtTotalItemDayConstants {
    /** Default Constructor. */
    private InsrtTotalItemDayConstants() {    	
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
    /**
     * salesitemcnt index number.
     */
    public static final int SALESITEMCNT = 9;
    /**
     * salescostamt index number.
     */
    public static final int SALESCOSTAMNT = 10;
    /**
     * salessalesamt index number.
     */
    public static final int SALESSALESAMNT = 11;
    /**
     * returnitemcnt index number.
     */
    public static final int RETURNITEMCNT = 12;
    /**
     * returncostamt index number.
     */
    public static final int RETURNCOSTAMNT = 13;
    /**
     * returnsalesamnt index number.
     */
    public static final int RETURNSALESAMNT = 14;
    /**
     * voiditemcnt index number.
     */
    public static final int VOIDITEMCNT = 15;
    /**
     * voidcostamnt index number.
     */
    public static final int VOIDCOSTAMNT = 16;
    /**
     * voidsalesamnt index number.
     */
    public static final int VOIDSALESAMNT = 17;
    /**
     * dcitemcnt index number.
     */
    public static final int DCITEMCNT = 18;
    /**
     * dccostamnt index number.
     */
    public static final int DCCOSTAMNT = 19;
    /**
     * dcsalesamnt index number.
     */
    public static final int DCSALESAMNT = 20;
}
