package ncr.res.mobilepos.queuebuster.constant;

/**
 * The UpdTransStatusInQueueConstant is a class that enumerates
 * all the column index values for certain prepared statement
 * on TXL_FORWARD_ITEM table.
 *
 * The class is dedicated only to help for {@link SQLServerQueueBusterDao}
 * upon updating a status in the TXL_FORWARD_ITEM Table.
 *
 * @see SQLServerQueueBusterDao
 */
public final class UpdTransStatusInQueueConstant {
    /**
     * The Default constructor.
     * Prevent instantiation of this class.
     */
    private UpdTransStatusInQueueConstant() {
        // TODO Auto-generated constructor stub
    }
    /**
     * The Status column index.
     */
    public static final int STATUS = 1;
    /**
     * The  CorpID column index.
     */
    public static final int CORPID = 2;
    /**
     * The Store ID column index.
     */
    public static final int STOREID = 3;
    /**
     * The Terminal ID column index.
     */
    public static final int TERMID = 4;
    /**
     * The Transaction column index.
     */
    public static final int TXID = 5;
    /**
     * The Businessdate column index.
     */
    public static final int TXDATE = 6;
    /**
     * The POS Terminal ID column index.
     */
    public static final int POSTERMID = 7;
}
