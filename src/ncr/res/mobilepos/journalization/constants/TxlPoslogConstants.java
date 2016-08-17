/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Txl_PoslogConstants
 *
 * Enumerates the column index used for PreparedStatement in PosLogDAO
 *
 * Carlos Campos
 */

package ncr.res.mobilepos.journalization.constants;

/**
 * The Txl_PoslogConstants is a class that enumerates
 * all the column index values for certain prepared statement.
 * of TXL_POSLOG table.
 *
 * The class is dedicated only to help for {@link SQLServerPosLogDAO}
 * upon saving
 * a POSLog xml in the TXL_POSLOG Table.
 *
 * @see SQLServerPosLogDAO
 */
public final class TxlPoslogConstants {
        /**
         * The Default constructor.
         * Do not allow the instantiation of the object of this class.
         */
        private TxlPoslogConstants() {        	
        }
        
        /** The column index for StoreID. */
        public static final int STOREID = 1;
        /** The column index for Terminal ID. */
        public static final int TERMID = 2;
        /** The column index for Transaction ID. */
        public static final int TXID = 3;
        /** The column index for Summary date. */
        public static final int SMRY_ID = 4;
        /** The column index for transaction. */
        public static final int TX = 5;
        /** The column index for transaction type. */
        public static final int TXTYPE = 6;
        /** The column index for status. */
        public static final int STATUS = 7;
        /** The column index for GUID. */
        public static final int GUID = 8;
}
