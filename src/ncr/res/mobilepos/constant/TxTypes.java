/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* TxTypes
*
* TxTypes is a class that enumerates all the values for Transaction Types
*
* Campos, Carlos (cc185102)
*/
package ncr.res.mobilepos.constant;

/**
 * TxTypes is a class that enumerates all the values
 * for Transaction Types.
 */
public final class TxTypes {
    /** Default Constructor. */
    private TxTypes() {
    }
    /**
     * The Transaction Type value for Voided Transaction.
     */
    public static final String VOIDED = "VD";
    /**
     * The Transaction Type value for Normal Transaction.
     */
    public static final String NORMAL = "NR";
    /**
     * The Transaction Type value that Voids a Transaction.
     */
    public static final String VOIDER = "VR";
    /**
     * The Transaction Type value for Canceled Transaction.
     */
    public static final String CANCELED = "CN";
    /**
     * The Transaction Type value that Returns a Transaction.
     */
    public static final String RETURNER = "RR";
    /**
     * The Transaction Type value for Returned Transaction.
     */
    public static final String RETURNED = "RD";
    /**
     * The Transaction Type value for Not Available.
     */
    public static final String NOTAVAILABLE = "NA";

    public static final String OTHER = "Other";
    public static final String SALES = "Sales";
    public static final String ECSALES = "ECSales";
    public static final String RETURN = "Return";
    public static final String ECRETURN = "ECReturn";
    public static final String VOID = "Void";
    public static final String CANCEL = "Cancel";
    public static final String SOD = "SOD";
    public static final String EOD = "EOD";
    public static final String LOAN = "Loan";
    public static final String PICKUP = "Pickup";
    public static final String SUMMARY_RECEIPT = "SummaryReceipt";
    public static final String RECEIPT_REPRINT = "ReceiptReprint";
    public static final String PAYIN = "PayIn";
    public static final String SIGNON = "SignOn";
    public static final String SIGNOFF = "SignOff";
    public static final String AUTOSIGNOFF = "AutoSignOff";
    public static final String POSSOD = "PosSod";
    public static final String POSEOD = "PosEod";
    public static final String EXCHANGE = "Exchange";
    public static final String INQ = "Inq";
    public static final String CASHINDRAWER = "CashInDrawer";
    public static final String LAYAWAY = "Layaway";
    public static final String HOLD = "Hold";
    public static final String RESERVATION = "Reservation";
    public static final String CUSTOMERORDER = "CustomerOrder";
    public static final String CASHTODRAWER = "CashToDrawer";
    public static final String BALANCING = "Balancing";
    public static final String PAYOUT = "PayOut";
    public static final String GUARANTEE = "Guarantee";
    public static final String NEWMEMBER = "NewMember";
    public static final String POSTPOINT = "PostPoint";
    public static final String CARDSWITCH = "CardSwitch";
    public static final String CARDMERGE = "CardMerge";
    public static final String CARDSTOP = "CardStop";
    public static final String POINTTICKET = "PointTicket";
	public static final String CASHIN = "CashIn";
    public static final String CASHOUT = "CashOut";
    public static final String POSTPOINTVOID = "PostPointVoid";
    public static final String CHARGESALES = "ChargeSales";
    public static final String CHARGESALESVOID = "ChargeSalesVoid";
    public static final String EXCHANGERETURN = "ExchangeReturn";
    public static final String PURCHASE = "Purchase";
    public static final String RETURNVOID = "ReturnVoid";
    public static final String EXCHANGESALES = "ExchangeSales";
    public static final String EXCHANGESALESVOID = "ExchangeSalesVoid";
    public static final String EXCHANGERETURNVOID = "ExchangeReturnVoid";
    public static final String REMAINED = "Remained";
    public static final String REMAINEDVOID = "RemainedVoid";
    public static final String LAYAWAYVOID = "LayawayVoid";
    public static final String PLUCHANGE = "PluChange";
    public static final String PURCHASEVOID = "PurchaseVoid";
    public static final String PURCHASERETURN = "PurchaseReturn";
    public static final String PURCHASERETURNVOID = "PurchaseReturnVoid";
    public static final String CASHINVOID = "CashInVoid";
    public static final String CASHOUTVOID = "CashOutVoid";
    public static final String SUSPEND = "Suspend";
    public static final String RESUME = "Resume";
}
