/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdTotalAcntDayConstants
 *
 * UpdTotalAcntDayConstants is a class that
 * enumerates all the column index values for
 * certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The UpdTotalAcntDayConstants is a class that enumerates all the column index
 * values for certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon updating a row in the TXU_TOTAL_ACNTDAY Table.
 *
 * @see SQLServerConsolidationDAO
 */
public final class UpdTotalAcntDayConstants {
    /** Default Constructor. */
    private UpdTotalAcntDayConstants() {    	
    }
    /** The Constant SALESITEMCNT. */
    public static final int SALESITEMCNT = 1;

    /** The Constant SALESCOSTAMNT. */
    public static final int SALESCOSTAMNT = 2;

    /** The Constant SALESSALESAMNT. */
    public static final int SALESSALESAMNT = 3;

    /** The Constant RETURNITEMCNT. */
    public static final int RETURNITEMCNT = 4;

    /** The Constant RETURNCOSTAMNT. */
    public static final int RETURNCOSTAMNT = 5;

    /** The Constant RETURNSALESAMNT. */
    public static final int RETURNSALESAMNT = 6;

    /** The Constant VOIDITEMCNT. */
    public static final int VOIDITEMCNT = 7;

    /** The Constant VOIDCOSTAMNT. */
    public static final int VOIDCOSTAMNT = 8;

    /** The Constant VOIDSALESAMNT. */
    public static final int VOIDSALESAMNT = 9;

    /** The Constant DCITEMCNT. */
    public static final int DCITEMCNT = 10;

    /** The Constant DCCOSTAMNT. */
    public static final int DCCOSTAMNT = 11;

    /** The Constant DCSALESAMNT. */
    public static final int DCSALESAMNT = 12;

    /** The Constant CORPID. */
    public static final int CORPID = 13;

    /** The Constant STOREID. */
    public static final int STOREID = 14;

    /** The Constant TERMINALID. */
    public static final int TERMINALID = 15;

    /** The Constant SUMMARYDATEID. */
    public static final int SUMMARYDATEID = 16;

    /** The Constant TIMEZONECODE. */
    public static final int TIMEZONECODE = 17;

    /** The Constant DPT. */
    public static final int DPT = 18;

    /** The Constant LINE. */
    public static final int LINE = 19;

    /** The Constant CLASS. */
    public static final int CLASS = 20;

    /** The Constant OPERATORCODE. */
    public static final int OPERATORCODE = 21;
}
