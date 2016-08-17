package ncr.res.mobilepos.queuebuster.constant;

/**
 * The InsrtTransactionInQueueConstant is a class that enumerates
 * all the column index values for certain prepared statement
 * on TXL_FORWARD_ITEM table.
 *
 * The class is dedicated only to help for {@link SQLServerQueueBusterDao}
 * upon inserting a row in the TXL_FORWARD_ITEM Table.
 *
 * @see SQLServerQueueBusterDao
 */
public final class InsrtTransactionInQueueConstant {
    /**
     * Default Constructor.
     * Disallow construction of instance of this class.
     */
    private InsrtTransactionInQueueConstant() {    	
    }

    /**
     * The CORPID column index.
     */
    public static final int CORPID = 1;
    /**
     * The STOREID column index.
     */
    public static final int STOREID = 2;
    /**
     * The TERMINAL ID column index.
     */
    public static final int TERMID = 3;
    /**
     * The TRANSACTION ID column index.
     */
    public static final int TXID = 4;
    /**
     * The Transaction Date Column index.
     */
    public static final int TXDATE = 5;
    /**
     * The POS Terminal ID column index.
     */
    public static final int POSTERMID = 6;
    /**
     * The Transaction column index.
     */
    public static final int TX = 7;
}
