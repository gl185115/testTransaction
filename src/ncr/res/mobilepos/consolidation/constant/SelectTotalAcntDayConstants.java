/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SelectTotalAcntDayConstants
 *
 * SelectTotalAcntDayConstants is a class that enumerates all the
 * column index values
 * for certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * Campos, Carlos
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The SelectTotalAcntDayConstants is a class that enumerates all the column
 * index values for certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon selecting a row in the TXU_TOTAL_ACNTDAY Table.
 *
 * @see SQLServerConsolidationDAO
 */
public final class SelectTotalAcntDayConstants {
    /** Default Constructor. */
    private SelectTotalAcntDayConstants() {    	
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
     * terminalid index number.
     */
    public static final int TERMINALID = 3;
    /**
     * deviceno index number.
     */
    public static final int DEVICENO = 4;
    /**
     * summarydateid index number.
     */
    public static final int SUMMARYDATEID = 5;
    /**
     * timezonecode index number.
     */
    public static final int TIMEZONECODE = 6;
    /**
     * dpt index number.
     */
    public static final int DPT = 7;
    /**
     * line index number.
     */
    public static final int LINE = 8;
    /**
     * class index number.
     */
    public static final int CLASS = 9;
    /**
     * operatorcode index number.
     */
    public static final int OPERATORCODE = 10;
}
