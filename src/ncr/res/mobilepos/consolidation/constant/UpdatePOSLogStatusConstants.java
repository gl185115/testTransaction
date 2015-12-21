/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdatePOSLogStatusConstants
 *
 * UpdatePOSLogStatusConstants is a class
 *  that enumerates all the column index values for
 * certain prepared statement on TXL_POSLog table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The UpdatePOSLogStatusConstants is a class that enumerates the column index
 * values for certain prepared statement on TXL_POSLog table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon updating the Transaction Status of a particular Transaction.
 *
 * @see SQLServerConsolidationDAO
 */
public final class UpdatePOSLogStatusConstants {
    /** Default Constructor. */
    private UpdatePOSLogStatusConstants() {    	
    }
    /**
     * status index number.
     */
    public static final int STATUS = 1;
    /**
     * corpid index number.
     */
    public static final int CORPID = 2;
    /**
     * storeid index number.
     */
    public static final int STOREID = 3;
    /**
     * terminalid index number.
     */
    public static final int TERMINALID = 4;
    /**
     * txid index number.
     */
    public static final int TXID = 5;
    /**
     * summarydateid index number.
     */
    public static final int SUMMARYDATEID = 6;
}
