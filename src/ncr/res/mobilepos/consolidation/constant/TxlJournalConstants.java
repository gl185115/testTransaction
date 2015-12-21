/**
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SelectTotalItemDayConstants
 *
 * SelectTotalItemDayConstants is a class that enumerates
 * all the column index values for
 * certain prepared statement on TXU_TOTAL_ACNTDAY table.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.constant;

/**
 * The Txl_JournalConstants is a class that enumerates all the column index
 * values for certain prepared statement on TXL_JOURNAL table.
 *
 * The class is dedicated only to help for {@link SQLServerConsolidationDAO}
 * upon saving each LineItem in the TXL_JOURNAL Table.
 *
 * @see SQLServerPosLogDAO
 */
public final class TxlJournalConstants {
    /** Default Constructor. */
    private TxlJournalConstants() {    	
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
    public static final int TERMID = 3;
    /**
     * tranno index number.
     */
    public static final int TRANNO = 4;
    /**
     * tranlineno index number.
     */
    public static final int TRANLINENO = 5;
    /**
     * summarydateid index number.
     */
    public static final int SUMMARYDATEID = 6;
    /**
     * operatorcode index number.
     */
    public static final int OPERATORCODE = 7;
    /**
     * plu index number.
     */
    public static final int PLU = 8;
    /**
     * description index number.
     */
    public static final int DESCRIPTION = 9;
    /**
     * accountcode index number.
     */
    public static final int ACCOUNT_CODE = 10;
    /**
     * itemcnt index number.
     */
    public static final int ITEMCOUNT = 11;
    /**
     * actsalesamnt index number.
     */
    public static final int ACTSALESAMOUNT = 12;
    /**
     * returnflag index number.
     */
    public static final int RETURNFLAG = 13;
    /**
     * voidflag index number.
     */
    public static final int VOIDFLAG = 14;
    /**
     * cancelflag index number.
     */
    public static final int CANCELFLAG = 15;
    /**
     * debtor index number.
     */
    public static final int DEBTOR = 16;
    /**
     * creditor index number.
     */
    public static final int CREDITOR = 17;
    /**
     * customerid index number.
     */
    public static final int CUSTOMERID = 18;
    /**
     * originaltranno index number.
     */
    public static final int ORIGINALTRANNO = 19;
    /**
     * upddate index number.
     */
    public static final int UPPDATE = 20;
}
