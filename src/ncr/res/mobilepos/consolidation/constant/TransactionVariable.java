/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * LineItem
 *
 * Model Class for LineItem
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.consolidation.constant;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2015.01.21      FENGSHA       前受金の取消レシート対応.
 */
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
     * BankTransfer (現金振込)
     */
    public static final String BANKTRANSFER = "BankTransfer";
    /**
     * The Tender Type value for CreditDebit.
     */
    public static final String CREDITDEBIT = "CreditDebit";
    /**
     *The CreditDebit Type value for Credit.
     */
    public static final String CREDIT = "Credit";
    /**
     * The CreditDebit Type value for ICCredit.
     */
    public static final String ICCREDIT = "IC";
    /**
     * The CreditDebit Type value for UnionPay.
     */
    public static final String UNIONPAY = "UnionPay";
    /**
     * 前受金登録
     */
    public static final String LAYAWAY = "Layaway";
    /**
     * 前受金精算
     */
    public static final String PREVIOUSLAYAWAY = "PreviousLayaway";
    /**
     * RainCheck
     */
    public static final String RAINCHECK = "RainCheck";
    /**
     * 前受金一括取消タイプ
     */
    public static final String ADVANCEVOIDTYPE = "AdvanceVoidType";
    /**
     * hold void
     */
    public static final String HOLDVOID = "HoldVoid";
    /**
     * customer void
     */
    public static final String CUSTOMERORDERVOID = "CustomerOrderVoid";
    /**
     * The Transaction Status value for VOID.
     */
    public static final String TRANSACTION_STATUS_RETURNED = "Returned";
    /**
     * 呼出返品
     */
    public static final String TRANSACTION_STATUS_RETURNEDLINK = "ReturnedLink";
    /**
     * The Transaction Status value for VOID.
     */
    public static final String TRANSACTION_STATUS_VOIDED = "Voided";
    /**
     * The Transaction Status value for CANCEL.
     */
    public static final String TRANSACTION_STATUS_CANCELED = "Canceled";
    /**
     * The Transaction Status value for PickUp.
     */
    public static final String PICKUP = "PickUp";
    /**
     * The Transaction Status value for PickUp.
     */
    public static final String LOAN = "Loan";
    /**
     * The Transaction Status value for PickUp.
     */
    public static final String EXCHANGE = "Exchange";
    /**
     * The Transaction Status value of PayOut.
     */
    public static final String PAYOUT = "PayOut";
    /**
     * The Transaction Status value of PayIn.
     */
    public static final String PAYIN = "PayIn";
    /**
     * The Transaction Status value of Guarantee.
     */
    public static final String GUARANTEE = "Guarantee";
    /**
     * The Transaction Status value of StartOfDay.
     */
    public static final String SOD = "StartOfDay";
    /***
     * The Transaction Status value of EndOfDay.
     */
    public static final String EOD = "EndOfDay";
    /**
     * The Transaction Status value of Balancing.
     */
    public static final String BALANCING = "Balancing";
    
    public static final String POSTPOINT = "PostPoint";
    
    public static final String POINTTICKET = "PointTicket";
    /**
     * セール
     */
    public static final String SALEMARK = "S";
    /**
     * プロパー
     */
    public static final String PRICEOVERMARK = "P";
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
