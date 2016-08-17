package ncr.res.mobilepos.queuebuster.constant;

/**
 * The SelectTransactionInQueueConstant is a class that enumerates
 * all the column index values for certain prepared statement
 * on TXL_FORWARD_ITEM table.
 *
 * The class is dedicated only to help for {@link SQLServerQueueBusterDao}
 * upon inserting a row in the TXL_FORWARD_ITEM Table.
 *
 * @see SQLServerQueueBusterDao
 */
public final class SelectTransactionInQueueConstant {
    /**
     * Default constructor.
     * Prevent instantiation of this class.
     *
     */
    private SelectTransactionInQueueConstant() {
        // TODO Auto-generated constructor stub
    }

    /**
     * The CorpID column index.
     */
    public static final int CORPID = 1;
    /**
     * The StoreID column index.
     */
    public static final int STOREID = 2;
    /**
     * The POS Terminal id column index.
     */
    public static final int POSTERMID = 3;
    /**
     * The Terminal ID column index.
     */
    public static final int TERMID = 4;
    /**
     * The Transaction column index.
     */
    public static final int TXID = 5;
}
