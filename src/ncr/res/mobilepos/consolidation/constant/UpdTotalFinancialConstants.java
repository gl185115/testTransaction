/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdTotalFinancialConstants
 *
 * The UpdTotalFinancialConstants is a class that enumerates
 * all the column index values for certain prepared statement
 * on TXU_TOTAL_FINANCIAL table.
 *
 * Dela Cerna, Jessel
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The UpdTotalFinancialConstants is a class that enumerates all the column
 * index values for certain prepared statement on TXU_TOTAL_FINANCIAL table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon selecting a row in the TXU_TOTAL_FINANCIAL Table.
 *
 * @see SQLServerConsolidationDAO
 */
public final class UpdTotalFinancialConstants {
    /** Default Constructor. */
    private UpdTotalFinancialConstants() {    	
    }
    /** The Constant CORPID. */
    public static final int CORPID = 1;

    /** The Constant STOREID. */
    public static final int STOREID = 2;

    /** The Constant TERMINALID. */
    public static final int TERMINALID = 3;

    /** The Constant ACCOUNTCODE. */
    public static final int ACCOUNTCODE = 4;

    /** The Constant ACCOUNTSUBCODE. */
    public static final int ACCOUNTSUBCODE = 5;

    /** The Constant SUMMARYDATEID. */
    public static final int SUMMARYDATEID = 6;

    /** The Constant DEBTOR. */
    public static final int DEBTOR = 7;

    /** The Constant CREDITOR. */
    public static final int CREDITOR = 8;
}
