/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * LineItem
 *
 * Model Class for LineItem
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * TransactionVariable is a class that enumerates all the constant values for
 * Transaction purposes.
 */
public final class TransactionVariable {
    /** Default Constructor. */
    private TransactionVariable() {    	
    }
    /**
     * The Authentication code for Sale.
     */
    public static final String ITEM_CODE = "0060";
    /**
     * The Authentication code for Cash.
     */
    public static final String CASH_CODE = "0013";
    /**
     * The Authentication code for CreditDebit.
     */
    public static final String CREDITDEBIT_CODE = "0014";
    /**
     * The Authentication code for Discount.
     */
    public static final String DISCOUNT_CODE = "0061";
    /**
     * The Authentication code for ItemDiscount.
     */
    public static final String ITEM_DISCOUNT_CODE = "1060";
    /**
     * The Authentication code for Voucher.
     */
    public static final String VOUCHER_CODE = "0015";
    /**
     * The Tender Type value for Cash.
     */
    public static final String CASH = "Cash";
    /**
     * The Tender Type value for StoredValue.
     */
    public static final String STOREVALUE = "StoredValue";
    /**
     * The StoredValueInstrument Type value for SmartCard.
     */
    public static final String SUCIA = "SmartCard";
    /**
     * The Tender Type value for Voucher
     */
    public static final String VOUCHER = "Voucher";
    /**
     * The Tender Type value for CreditDebit.
     */
    public static final String CREDITDEBIT = "CreditDebit";
    /**
     * The voucher type value for Gift card.
     */
    public static final String VOUCHER_TYPE_GIFT_CARD = "GiftCard";
    /**
     * The voucher type value for Gift certificate.
     */
    public static final String VOUCHER_TYPE_GIFT_CERTIFICATE = "GiftCertificate";
    /**
     * The Transaction Status value for VOID.
     */
    public static final String TRANSACTION_STATUS_RETURNED = "Returned";
    /**
     * The Transaction Status value for VOID.
     */
    public static final String TRANSACTION_STATUS_VOIDED = "Voided";
    /**
     * The Transaction Status value for CANCEL.
     */
    public static final String TRANSACTION_STATUS_CANCELED = "Canceled";

    /**
     * Contains type of codes.
     */
    public enum TransactionCode {
        /**
         * Sale LineItem code.
         */
        ITEMCODE(60),
        /**
         * Cash Tender LineItem code.
         */
        CASHCODE(13),
        /**
         * Credit Tender LinteItem code.
         */
        CREDITDEBITCODE(14),
        /**
         * Discount LineItem code.
         */
        DISCOUNTCODE(61),
        /**
         * Sale Item Discount LineItem code.
         */
        ITEMDISCOUNTCODE(1060),
        /**
         * The Voucher LineItem code.
         */
        VOUCHERCODE(15);
        /**
         * Holds key value of each code.
         */
        private static final Map<Integer, TransactionCode> LOOK_UP =
            new HashMap<Integer, TransactionCode>();

        static {
            for (TransactionCode code : EnumSet.allOf(TransactionCode.class)) {
                LOOK_UP.put(code.getCode(), code);
            }
        }

        /**
         * Holds the transaction code.
         */
        private int code;

        /**
         * Default constructor.
         *
         * @param codeToSet
         *            The transaction code to set.
         */
        private TransactionCode(final int codeToSet) {
            this.code = codeToSet;
        }

        /**
         * Gets the code.
         *
         * @return transaction code.
         */
        public int getCode() {
            return code;
        }

        /**
         * Gets transaction code in the list.
         *
         * @param code
         *            The code to look up.
         * @return TransactionCode object with key and value.
         */
        public static TransactionCode get(final int code) {
            return LOOK_UP.get(code);
        }
    }

}
