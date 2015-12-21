/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * InsrtTotalAcntDayConstants
 *
 * InsrtTotalAcntDayConstants is a class that enumerates all the
 * column index values for certain prepared statement TXU_TOTAL_ACNTDAY table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The InsrtTotalAcntDayConstants is a class that enumerates all the column
 * index values for certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon inserting a row in the TXU_TOTAL_ACNTDAY Table.
 *
 * @see SQLServerConsolidationDAO
 */
public final class InsrtTotalAcntDayConstants {
    /** Default Constructor. */
    private InsrtTotalAcntDayConstants() {    	
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
     * termid index number.
     */
    public static final int TERMINALID = 3;
    /**
     * summarydateid index number.
     */
    public static final int SUMMARYDATEID = 4;
    /**
     * timezonecode index number.
     */
    public static final int TIMEZONECODE = 5;
    /**
     * dpt index number.
     */
    public static final int DPT = 6;
    /**
     * line index number.
     */
    public static final int LINE = 7;
    /**
     * class index number.
     */
    public static final int CLASS = 8;
    /**
     * operatorcode index number.
     */
    public static final int OPERATORCODE = 9;
    /**
     * salesitemcnt index number.
     */
    public static final int SALESITEMCNT = 10;
    /**
     * salescostamt index number.
     */
    public static final int SALESCOSTAMNT = 11;
    /**
     * salessalesamt index number.
     */
    public static final int SALESSALESAMNT = 12;
    /**
     * returnitemcnt index number.
     */
    public static final int RETURNITEMCNT = 13;
    /**
     * returncostamt index number.
     */
    public static final int RETURNCOSTAMNT = 14;
    /**
     * returnsalesamt index number.
     */
    public static final int RETURNSALESAMNT = 15;
    /**
     * voiditemcnt index number.
     */
    public static final int VOIDITEMCNT = 16;
    /**
     * voidcostamt index number.
     */
    public static final int VOIDCOSTAMNT = 17;
    /**
     * voidsalesamt index number.
     */
    public static final int VOIDSALESAMNT = 18;
    /**
     * dcitemcnt index number.
     */
    public static final int DCITEMCNT = 19;
    /**
     * dccostamt index number.
     */
    public static final int DCCOSTAMNT = 20;
    /**
     * dcsalesamt index number.
     */
    public static final int DCSALESAMNT = 21;
}
