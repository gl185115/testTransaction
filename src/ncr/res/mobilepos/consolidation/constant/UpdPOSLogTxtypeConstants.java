/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdPosLogTxtypeConstants
 *
 * UpdPosLogTxtypeConstants is a class that enumerates all
 *  the column index values for
 * certain prepared statement on TXL_POSLog table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The UpdPosLogTxtypeConstants is a class that enumerates the column index
 * values for certain prepared statement on TXL_POSLog table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon retrieving the Transaction Type of a particular Transaction.
 *
 * @see SQLServerConsolidationDAO
 */
public final class UpdPOSLogTxtypeConstants {
    /** Default Constructor. */
    private UpdPOSLogTxtypeConstants() {    	
    }
    /**
     * txtype index number.
     */
    public static final int TXTYPE = 1;
    /**
     * corpid index number.
     */
    public static final int CORPID = 2;
    /**
     * storeid index number.
     */
    public static final int STOREID = 3;
    /**
     * termid index number.
     */
    public static final int TERMID = 4;
    /**
     * txid index number.
     */
    public static final int TXID = 5;
    /**
     * summarydateid index number.
     */
    public static final int SMRYID = 6;
}
