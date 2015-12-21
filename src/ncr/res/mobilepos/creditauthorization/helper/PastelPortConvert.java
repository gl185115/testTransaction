package ncr.res.mobilepos.creditauthorization.helper;

import java.io.IOException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.pastelport.platform.CommonTx;
import ncr.res.pastelport.platform.PPPaymentMethod;
import ncr.res.pastelport.platform.PastelPortTxRecvBase;
import ncr.res.pastelport.platform.PastelPortTxRecvImpl;
import ncr.res.pastelport.platform.PastelPortTxSendBase;
import ncr.res.pastelport.platform.PastelPortTxSendImpl;
import atg.taglib.json.util.JSONException;

/**
 * Helper class for PastelPort.
 */
public class PastelPortConvert {
    /** The Desired number of digits for Store ID. */
    private static final int STORE_ID_LENGTH = 4;
    /** The Desired number of digits for POS NO. */
    private static final int POS_NO_LENGTH = 4;
    /** The Desired number of digits for Transaction No. */
    private static final int TRANS_NO_LENGTH = 4;
    /** The Desired number of digits for POS Operate Time Lengths. */
    private static final int POS_OPERATE_TIME_LENGTH = 14;
    /** The Maximum ammount length. */
    private static final int MAX_AMOUNT_LENGTH = 9;
    /** The Expiration Length FIRST Type. */
    private static final int EXP_DATE_LENGTH_1 = 4;
    /** The Expiration Length FIRST Type. */
    private static final int EXP_DATE_LENGTH_2 = 6;
    /** The Maximum Payment Sequence digit.  */
    private static final int MAX_PAYMENT_SEQ_LENGTH = 13;
    /** The Maximum Tax others length. */
    private static final int MAX_TAX_OTHERS_LENGTH = 7;
    /** The Maximum JIS 2 Card Length. */
    private static final int MAX_JIS2_CARD_LENGTH = 69;
    /** The Maximum JIS 2 Card Length. */
    private static final int MAX_JIS1_CARD_LENGTH = 37;
    /** The transaction type value for identification when autovoid. */
    private static final int IDENTIFICATION2WHENAUTOVOID_TXTYPE = 98;
    /** The Info Length of Card Input type 1.  */
    private static final int CARD_INPUT_TYPE_1_INFO_LENGTH = 10;
    /** The Info Length of Card Input type 1.  */
    private static final int CARD_INPUT_TYPE_MAX_INFO_LENGTH = 20;
    /** The value for IC Related data.*/
    private static final int IC_RELATED_VALUE = 1998;
    /** The number of digits for IC Related data.*/
    private static final int IC_RELATED_VALIDITY_DIGIT = 4;
    /** Security Code Length. */
    private static final int SECURITY_CODE_LENGTH = 6;
    /** Security Code Length. */
    private static final int NO_SECURITY_CODE_LENGTH = 4;
    /** Error Code Length. */
    public static final int ERROR_CODE_LENGTH = 4;
    /** Identification nap length. */
    private static final int IDENTIFICATION_NAP_LENGTH = 3;
    /** Connection Company code length. */
    private static final int CONNECTION_COMPANY_CODE_LENGTH = 11;
    /** The Connect Company. */
    private static final int CONNECT_COMPANY = 10;
    /** The Print Message length. */
    private static final int PRINT_MESSAGE_LENGTH = 155;
    /** The Display Message Length. */
    private static final int DISPLAY_MESSAGE_LENGTH = 93;
    /** The Print validity date Length. */
    private static final int PRINT_VALIDITY_DATE_LENGTH = 4;
    /** The Print Member ID Length. */
    private static final int PRINT_MEMBER_ID_LENGTH = 19;
    /** The Authorization Agency ID Length. */
    private static final int AUTHORIZATION_AGENCY_ID_LENGTH = 11;
    /** The Permission Code Length. */
    private static final int PERMISSION_CODE_LENGTH = 6;
    /** The Center Receipt Date Length. */
    private static final int CENTER_RECEIPT_DATE_LENGTH = 12;
    /** The System trace Audit Number. */
    private static final int SYSTEM_TRACE_AUDIT_NUMBER = 6;
    /** The CAFIS Date Length. */
    private static final int CAFIS_DATE_LENGTH = 4;
    /** The CAFIS Date Length. */
    private static final int CAFIS_SEQUENCE_LENGTH = 6;
    /** The Reservation 2 Length. */
    private static final int RESERVE2_LENGTH = 37;
    /** The Goods Code Length. */
    private static final int GOODS_CODE_LENGTH = 7;
    /** The Tax Shipping Length. */
    private static final int TAX_SHIPPING_LENGTH = 7;
    /** The Tax Carriage. */
    private static final int TAX_CARRIAGE = 7;
    /** The blank Area 20 Length. */
    private static final int BLANK_AREA_20_LENGTH = 22;
    /** The Approval Number Length. */
    private static final int APPROVAL_NUMBER_LENGTH = 7;
    /** The Sub Code Length. */
    private static final int SUBCODE_LENGTH = 11;
    /** The Default Security Code Length. */
    private static final int DEFAULT_SECURITY_CODE_LENGTH = 4;
    /** The JIS 2 Security Code Info Length. */
    private static final int JIS2_SECURITY_INFO_LENGTH = 6;
    /** The Reserved Field Length. */
    private static final int RESERVED_FIELD_LENGTH = 10;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_54 = 6;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_2 = 4;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_4 = 9;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_5 = 4;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_7 = 3;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_8 = 4;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_11 = 4;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_13 = 4;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_14 = 3;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_18 = 9;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_19 = 9;
    /** The System reservation teritory length. */
    private static final int SYSTEM_RESRV_TERITORY_LENGTH_44 = 22;
    /** The Finish Flag Length. */
    private static final int FINISH_FLAG_LENGTH = 3;
    /** The Payment Count. */
    private static final int PAYMENT_COUNT = 3;
    /** The Payment Method Id. */
    private static final int PAYMENT_METHODID = 3;
    /** The Product Total. */
    private static final int PRODUCT_TOTAL = 9;
    /** The Product Code. */
    private static final int PRODUCT_CODE = 7;
    /** The Original Voucher Number. */
    private static final int ORIGINAL_VOUCHER_NO = 13;
    /** The Terminal Validity. */
    private static final int TERMINAL_VALIDITY = 6;
    /** The Passwordd Digit.  */
    private static final int PASSWORD_DIGIT = 4;
    /** The Request Code length. */
    private static final int REQUEST_CODE_LENGTH = 4;
    /** The Card Sort. */
    private static final int CARD_SORT = 3;
    /** The Pastel Port Digit. */
    private static final int PASTELPORT_DIGIT = 4;
    /** The Pastel Port Length. */
    private static final int PASTEL_PORT_LENGTH = 6;

    /**
     * Instantiates a new pastel port convert.
     *
     * @param pastelPortEnvToSet The PasteL Environment to set
     *            the pastel port environment
     */
    public PastelPortConvert(final PastelPortEnv pastelPortEnvToSet) {
        super();
        this.pastelPortEnv = pastelPortEnvToSet;
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Instantiates a new pastel port convert.
     */
    public PastelPortConvert() {
        super();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /** The Constant IOWDISP. */
    protected static final String IOWDISP = "CNCNV";

    /** The index string. */
    private int indexString = 0;

    /** The Constant LINE2. */
    public static final String LINE2 = "2432";

    /** The Constant IDENTIFICATION2. */
    public static final String IDENTIFICATION2 = "C1";

    /** The Constant IDENTIFICATION2WHENAUTOVOID. */
    public static final String IDENTIFICATION2WHENAUTOVOID = "C3";

    /** The Constant TYPE. */
    public static final String TYPE = "000";

    /** The Constant ERROR_CODE. */
    public static final String ERROR_CODE = "    ";

    /** The Constant SERVICE_SUBTRACT. */
    public static final String SERVICE_SUBTRACT = "SUBTRACT";

    /** The Constant SERVICE_VOID. */
    public static final String SERVICE_VOID = "VOID";

    /** The Constant SERVICE_REFUND. */
    public static final String SERVICE_REFUND = "REFUND";

    /** The Constant SERVICE_TEST_SUBTRACT. */
    public static final String SERVICE_TEST_SUBTRACT = "TESTSUBTRACT";

    /** The Constant SERVICE_TEST_VOID. */
    public static final String SERVICE_TEST_VOID = "TESTVOID";

    /** The Constant SERVICE_TEST_REFUND. */
    public static final String SERVICE_TEST_REFUND = "TESTREFUND";

    /** The Constant SERVICE_TEST. */
    public static final String SERVICE_TEST = "TEST";

    /** The Constant MESSAGETYPEID. */
    private static final String MESSAGETYPEID = "9900";

    /** The Constant REQUESTCODE_SELL. */
    protected static final String REQUESTCODE_SELL = "A101";

    /** The Constant REQUESTCODE_SELLCANCEL. */
    protected static final String REQUESTCODE_SELLCANCEL = "A501";

    /** The Constant REQUESTCODE_SELLRETURED. */
    protected static final String REQUESTCODE_SELLRETURED = "A701";

    /** The Constant REQUESTCODE_NOTICERESULT. */
    protected static final String REQUESTCODE_NOTICERESULT = "A811";

    /** The Constant REQUESTCODE_AUTOVOID. */
    protected static final String REQUESTCODE_AUTOVOID = "A112";

    /** The Constant REQUESTCODE_VOID. */
    protected static final String REQUESTCODE_VOID = "A512";

    /** The Constant ALLIANCE. */
    protected static final String ALLIANCE = "1";

    /** The Constant LOCATIONDECISION. */
    protected static final String LOCATIONDECISION = "1";

    /** The Constant RESPONSEDATA. */
    protected static final String RESPONSEDATA = "1";

    /** The Constant ALLIANCECODE. */
    protected static final String ALLIANCECODE = "99";

    /** The Constant IDENTIFICATIONAP. */
    protected static final String IDENTIFICATIONAP = "MS1";

    /** The Constant SERIALNUM. */
    protected static final String SERIALNUM = "  ";

    /** The Constant AFFILIATEDSTOREBIZCODE. */
    protected static final String AFFILIATEDSTOREBIZCODE = "    ";

    /** The Constant PROCESSICCARD. */
    protected static final String PROCESSICCARD = "0";

    /** The Constant RESERVE2. */
    private static final String RESERVE2 = "             ";

    /** The Constant ICRELATEDNUM. */
    protected static final String ICRELATEDNUM = "0000";

    /** The Constant DEAL_JUDGE_ERROR. */
    private static final String DEAL_JUDGE_ERROR = "000080";

    /** The pastel port env. */
    private PastelPortEnv pastelPortEnv;

    /** The instance of PastelPortTxSendBase. */
    private PastelPortTxSendBase pastelPortSendBase;

    /** The pastelport payment method. */
    private PPPaymentMethod ppPaymentMethod;

    /** The Debug Logger Trace Printer.  */
    private Trace.Printer tp;

    /**
     * information form CommonTx Transaction to PastelPortTxSendImpl.
     *
     * @param tx
     *            the tx
     * @return the pastel port tx send base
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    public final PastelPortTxSendBase convertSendTx(final CommonTx tx)
            throws JSONException {
        tp.methodEnter("convertSendTx")
        .println("CommonTX", tx.toString());
        pastelPortSendBase = new PastelPortTxSendImpl();
        // setting the Fixed head
        setupFixed(pastelPortSendBase, tx);

        // setting the other part
        if (setupRequestCode(pastelPortSendBase, tx)) {
            // when is for real
            setupValue(pastelPortSendBase, tx);
        } else {
            // when is for test
            setupTestValue(pastelPortSendBase, tx);
        }
        tp.methodExit(pastelPortSendBase);
        return pastelPortSendBase;
    }

    /**
     * setting the Fixed head value which object mast be check.
     *
     * @param pastelPort The Pastel Port Send Base.
     * @param tx    The Common TX.
     * @throws JSONException    The exception thrown.
     */
    private void setupFixed(final PastelPortTxSendBase pastelPort,
            final CommonTx tx)
            throws JSONException {
        pastelPort.setPosno(tx.getFieldValue("terminalid")); // POSNoÅD
     // POS
        // process
        // time
        pastelPort.setPosoperatetime(tx.getFieldValue("txdatetime"));
        String storeid = tx.getFieldValue("storeid");
        if (storeid.length() == STORE_ID_LENGTH) {
            pastelPort.setStoreid(storeid); // store id
        } else if (storeid.length() > STORE_ID_LENGTH) {
            pastelPort.setStoreid(storeid.substring(storeid.length()
                    - STORE_ID_LENGTH));
        } else {
            pastelPort.setStoreid("0000");
        }
        pastelPort.setTxid(tx.getFieldValue("txid"));
    }

    /**
     * set request code.
     *
     * @param pastelPort    The PastelPort Send Base.
     * @param tx    The Common Tx
     * @throws JSONException    The Exception Thrown.
     * @return True, if success else false.
     */
    private boolean setupRequestCode(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) throws JSONException {
        boolean bService = false;
        String servicetype = tx.getFieldValue("service");
        // Åiset A112 when auto void Åj
        if ("98".equals(tx.getFieldValue("txtype"))) {
            if (PastelPortConvert.SERVICE_VOID.equals(servicetype)) {
                // void
                pastelPort.setRequestcode(PastelPortConvert.REQUESTCODE_VOID);
            } else if (PastelPortConvert.SERVICE_SUBTRACT.equals(servicetype)) {
                // subtract
                pastelPort
                        .setRequestcode(PastelPortConvert.REQUESTCODE_AUTOVOID);
            }
            bService = true;
        } else {

            if (servicetype.substring(0, STORE_ID_LENGTH)
                    .equals(SERVICE_TEST)) {
                bService = false;
            } else {
                bService = true;
                if (PastelPortConvert.SERVICE_SUBTRACT.equals(servicetype)) {
                    // subtract
                    pastelPort
                            .setRequestcode(PastelPortConvert.REQUESTCODE_SELL);
                } else if (PastelPortConvert.SERVICE_VOID.equals(servicetype)) {
                    // void
                    pastelPort
                            .setRequestcode(PastelPortConvert
                                    .REQUESTCODE_SELLCANCEL);
                } else if (PastelPortConvert.SERVICE_REFUND
                        .equals(servicetype)) {
                    // refund
                    pastelPort
                            .setRequestcode(PastelPortConvert
                                    .REQUESTCODE_SELLRETURED);
                }
            }
        }

        return bService;
    }

    /**
     * setting the other part when is for real.
     *
     * @param pastelPort    The PastelPort Send Base.
     * @param tx    The Common Tx.
     * @throws JSONException    The Exception thrown when error occur.
     */
    private void setupValue(final PastelPortTxSendBase pastelPort,
            final CommonTx tx)
            throws JSONException {
        setupCardDetermine(pastelPort, tx);
        setupTradeHeadData(pastelPort, tx);
        setupCreditBlankareaData(pastelPort);

        // ------------------ credit 47-52 ---------------------
        // 48 terminalInfo
        setupCreditTerminalinfo(pastelPort);
        // 49
        setupSubcode(pastelPort, tx);
        // 50-52
        setupSecuinfo(pastelPort, tx);
        setupCooperationDate(pastelPort, tx);

        setupOther(pastelPort, tx);

        // 20-37
        ppPaymentMethod = new PPPaymentMethod();
        ppPaymentMethod.paymentMethod(pastelPort, tx);
        // 37
        String amount = tx.getFieldValue("amount");
        if (amount.length() <= MAX_AMOUNT_LENGTH) {
            pastelPort.setTransferamount(getRepeatString("0",
                    MAX_AMOUNT_LENGTH - amount.length())
                    + tx.getFieldValue("amount"));
        } else {
            throw new JSONException("amount is beyonded");
        }
    }

    /**
     * setting the credit value which data type need be check.
     *
     * @param pastelPort    The Pastel Port Send Base.
     * @param tx    The Common TX.
     * @throws JSONException    The Exception thrown.
     */
    private void setupOther(final PastelPortTxSendBase pastelPort,
            final CommonTx tx)
            throws JSONException {
        // 1
        pastelPort.setLine2(LINE2);
        // 2
        if (tx.getTxType() == IDENTIFICATION2WHENAUTOVOID_TXTYPE) {
            pastelPort
                    .setIdentification2(
                            PastelPortConvert.IDENTIFICATION2WHENAUTOVOID);
        } else {
            pastelPort.setIdentification2(PastelPortConvert.IDENTIFICATION2);
        }

        // 10
        try {
            pastelPort.setCode(tx.getFieldValue("pin"));
        } catch (JSONException e) {
            pastelPort.setCode("0000");
        }
        // 12
        try {
            String expirationdate = tx.getFieldValue("expirationdate");
            if (expirationdate.length() == EXP_DATE_LENGTH_1) {
                pastelPort.setExpirationdate("20" + expirationdate);
            } else if (expirationdate.length() == EXP_DATE_LENGTH_2) {
                pastelPort.setExpirationdate(expirationdate);
            } else {
                pastelPort.setExpirationdate("000000");
            }
        } catch (JSONException e) {
            pastelPort.setExpirationdate("000000");
        }

        // -------------------- credit 13-17 ------------------
        // No. 13
        boolean isAutoVoid = "98".equals(tx.getFieldValue("txtype"));
        try {
            String paymentseq = tx.getFieldValue("paymentseq");
            pastelPort.setPaymentseq(getRepeatString("0",
                    MAX_PAYMENT_SEQ_LENGTH - paymentseq.length())
                    + paymentseq);
        } catch (JSONException e) {
            pastelPort.setPaymentseq(getRepeatString("0",
                    MAX_PAYMENT_SEQ_LENGTH));
        }
        // No. 14
        try {
            String paymentseq = tx.getFieldValue("paymentseq");
            if (isAutoVoid) {
                pastelPort.setLastpaymentseq(getRepeatString("0",
                        MAX_PAYMENT_SEQ_LENGTH
                            - paymentseq.length()) + paymentseq);
            } else if (PastelPortConvert.SERVICE_VOID.equals(tx.getService())
                  || PastelPortConvert.SERVICE_REFUND.equals(tx.getService())) {
                String voidpaymentseq = tx.getFieldValue("voidpaymentseq");
                pastelPort.setLastpaymentseq(getRepeatString("0",
                        MAX_PAYMENT_SEQ_LENGTH
                            - voidpaymentseq.length()) + voidpaymentseq);
            } else {
                paymentseq = "00000";
                pastelPort.setLastpaymentseq(getRepeatString("0",
                        MAX_PAYMENT_SEQ_LENGTH - paymentseq.length())
                        + paymentseq);
            }
        } catch (JSONException e) {
            pastelPort.setLastpaymentseq(
                    getRepeatString("0", MAX_PAYMENT_SEQ_LENGTH));
        }
        // 15 goods code
        pastelPort.setGoodscode("0000000");
        // 17
        setupTaxshipping(pastelPort, tx);

        // 39
        pastelPort.setErrorcode(PastelPortConvert.ERROR_CODE);
        // 41
        try {
            pastelPort.setApprovalnum(" " + tx.getFieldValue("approvalcode"));
        } catch (Exception e) {
            pastelPort.setApprovalnum("       ");
        }

        // 47 message type code "9900"
        pastelPort.setMessagecode(PastelPortConvert.MESSAGETYPEID);
        // credit data 57
        pastelPort.setIdentificationap(PastelPortConvert.IDENTIFICATIONAP);
        // credit data 58
        pastelPort.setSerialnum(PastelPortConvert.SERIALNUM);
        // credit data 59
        pastelPort
                .setAffiliatedstorebizcode(
                        PastelPortConvert.AFFILIATEDSTOREBIZCODE);
        // credit data 60
        pastelPort.setProcessiccard(PastelPortConvert.PROCESSICCARD);
        // credit data 61
        pastelPort.setReserve2(PastelPortConvert.RESERVE2);
        // credit data 62
        pastelPort.setIcrelatednum(PastelPortConvert.ICRELATEDNUM);
        // credit data 63
        pastelPort.setIcrelateddata(getRepeatString("20",
                IC_RELATED_VALUE / 2));
    }

    /**
     * setup tax feeding about credit.
     * @param pastelPort The Pastel Port Send Base.
     * @param tx The Common Tx.
     */
    private void setupTaxshipping(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) {
        StringBuilder strBid = new StringBuilder("");
        try {
            String taxothers = tx.getFieldValue("taxothers").toString();
            pastelPort.setTaxshipping(strBid.append(
                    getRepeatString("0",
                            MAX_TAX_OTHERS_LENGTH - taxothers.length())
                            + tx.getFieldValue("taxothers")).toString());
        } catch (JSONException e) {
            pastelPort.setTaxshipping("0000000");
        }
    }

    /**
     * method of value format.
     * @param src   The Source.
     * @param count The Count.
     * @return repeatSrc    The Repoeated Source.
     */
    private String getRepeatString(final String src, final int count) {
        StringBuilder repeatSrc = new StringBuilder("");
        for (int i = 0; i < count; i++) {
            repeatSrc.append(src);
        }
        return repeatSrc.toString();
    }

    /**
     * some check of credit object.
     * @param pastelPort The Pastel Port Send Base.
     * @param tx    The Common Tx.
     * @throws JSONException    The exception thrown when error occur.
     */
    private void setupCardDetermine(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) throws JSONException {
        // credit data 3-7
        String cardInputType;
        try {
            cardInputType = tx.getFieldValue("cardinputtype");
        } catch (JSONException e1) {
            cardInputType = "1";
        }

        // In our system : "1" is jis1 "2" is jis2
        if ("1".equals(cardInputType)) {
            pastelPort.setDetermine("2");
            cardInputType = "2";
        } else if ("2".equals(cardInputType)) {
            pastelPort.setDetermine("1");
            cardInputType = "1";
        } else {
            pastelPort.setDetermine(cardInputType);
        }

        pastelPort.setType(TYPE);

        if ("0".equals(cardInputType)) {
            pastelPort.setInfo(tx.getFieldValue("cardinputtype")
                    + getRepeatString(" ", CARD_INPUT_TYPE_1_INFO_LENGTH));
            pastelPort.setJis1Info(getRepeatString(" ", MAX_JIS1_CARD_LENGTH));
            pastelPort.setJis2Info(getRepeatString(" ",
                    MAX_JIS2_CARD_LENGTH));

        } else if ("1".equals(cardInputType)) {
            pastelPort.setInfo(getRepeatString(" ",
                    CARD_INPUT_TYPE_MAX_INFO_LENGTH));
            pastelPort.setJis1Info(getRepeatString(" ", MAX_JIS1_CARD_LENGTH));
            String jis2CardData = tx.getFieldValue("Jis2CardData");
            pastelPort.setJis2Info(jis2CardData
                    + getRepeatString(" ", MAX_JIS2_CARD_LENGTH
                            - jis2CardData.length()));

        } else if ("2".equals(cardInputType)) {
            pastelPort.setInfo(getRepeatString(" ",
                    CARD_INPUT_TYPE_MAX_INFO_LENGTH));
            String jis1CardData = tx.getFieldValue("Jis1CardData");
            pastelPort.setJis1Info(jis1CardData
                    + getRepeatString(" ",
                            MAX_JIS1_CARD_LENGTH - jis1CardData.length()));
            pastelPort.setJis2Info(getRepeatString(" ", MAX_JIS2_CARD_LENGTH));

        } else if ("9".equals(cardInputType)) {
            try {
                String accountNo = tx.getFieldValue("accountno");
                pastelPort.setInfo(accountNo
                        + getRepeatString(" ",
                        CARD_INPUT_TYPE_MAX_INFO_LENGTH - accountNo.length()));
            } catch (JSONException e) {
                // when don't have the account no tag
                pastelPort.setInfo(getRepeatString(" ",
                        CARD_INPUT_TYPE_MAX_INFO_LENGTH));
            }

            try {
                String jis1CardData = tx.getFieldValue("Jis1CardData");
                pastelPort.setJis1Info(jis1CardData
                        + getRepeatString(" ",
                                MAX_JIS1_CARD_LENGTH - jis1CardData.length()));
            } catch (JSONException e) {
                // when don't have the Jis1CardData tag
                pastelPort.setJis1Info(getRepeatString(" ",
                        MAX_JIS1_CARD_LENGTH));
            }

            try {
                String jis2CardData = tx.getFieldValue("Jis2CardData");
                pastelPort.setJis2Info(jis2CardData
                        + getRepeatString(" ",
                                MAX_JIS2_CARD_LENGTH - jis2CardData.length()));
            } catch (JSONException e) {
                // when don't have the Jis2CardData tag
                pastelPort.setJis2Info(getRepeatString(" ",
                        MAX_JIS2_CARD_LENGTH));
            }
        }
    }

    /**
     * Set up Company SubCode.
     * @param pastelPort    The Pastel Port Send Base.
     * @param tx    The Common Transaction.
     */
    private void setupSubcode(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) {
        // credit data 49
        String handledcommpanyString = pastelPortEnv.getHandledCompanyCode();
        pastelPort.setSubcode(handledcommpanyString
                + pastelPortEnv.getHandledCompanySubCode());
    }

    /**
     * setup Security information.
     * @param pastelPort    The Pastel Port Send Base.
     * @param tx    The Common Tx.
     * @throws JSONException    The Exception thrown.
     */
    private void setupSecuinfo(final PastelPortTxSendBase pastelPort,
            final CommonTx tx)
            throws JSONException {

        boolean ishavesecurityCode = false;
        boolean ishavejis2data = false;
        String securityType = "1";

        // credit data 50
        try {
            String securityInfo = tx.getFieldValue("securityinfo");
            if ("1".equals(securityInfo.substring(0, 1))
                    || "1".equals(securityInfo.substring(1, 1))) {
                pastelPort.setSecucode(securityInfo.substring(2,
                        SECURITY_CODE_LENGTH));
                ishavesecurityCode = true;
            } else {
                ishavesecurityCode = false;
                pastelPort.setSecucode(getRepeatString(" ",
                        NO_SECURITY_CODE_LENGTH));
            }
        } catch (JSONException e) {
            ishavesecurityCode = false;
            pastelPort.setSecucode(getRepeatString(" ",
                    NO_SECURITY_CODE_LENGTH));
        }

        try {
            String jis2data = tx.getFieldValue("jis2data");
            pastelPort.setJis2Secuinfo(jis2data.substring(2,
                    SECURITY_CODE_LENGTH + 2));
            ishavejis2data = true;
        } catch (JSONException e) {
            ishavejis2data = false;
            pastelPort.setJis2Secuinfo(getRepeatString(" ",
                    NO_SECURITY_CODE_LENGTH + 2));
        }

        if (ishavesecurityCode) {
            if (ishavejis2data) {
                securityType = "1";
            } else {
                securityType = "2";
            }
        } else {
            if (ishavejis2data) {
                securityType = "3";
            } else {
                securityType = "4";
            }
        }
        // 50
        pastelPort.setSecuinfo(securityType);
    }

    /**
     * Set up Cooperation Date.
     * @param pastelPort    The pastel Port Send Base.
     * @param tx    The Common Tx.
     */
    private void setupCooperationDate(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) {
        // credit data 53-56
        try {
            pastelPort.setAlliance(tx.getFieldValue("alliance"));
        } catch (JSONException e) {
            pastelPort.setAlliance(PastelPortConvert.ALLIANCE);
        }
        try {
            pastelPort
                    .setLocationdecision(tx.getFieldValue("locationdecision"));
        } catch (JSONException e) {
            pastelPort.setAlliance(PastelPortConvert.LOCATIONDECISION);
        }
        try {
            pastelPort.setResponsedata(tx.getFieldValue("responsedata"));
        } catch (JSONException e) {
            pastelPort.setAlliance(PastelPortConvert.RESPONSEDATA);
        }
        try {
            pastelPort.setAlliancecode(tx.getFieldValue("alliancecode"));
        } catch (JSONException e) {
            pastelPort.setAlliance(ALLIANCECODE);
        }
    }

    /**
     * @param pastelPort    The Pastel Port Send Base.
     * @param tx    The Common Tx.
     * @throws JSONException    The Exception thrown when error occured.
     */
    public final void setupTestValue(final PastelPortTxSendBase pastelPort,
            final CommonTx tx)
            throws JSONException {

        tp.methodEnter("setupTestValue");

        setupTradeHeadData(pastelPort, tx);
        setupCreditTerminalinfo(pastelPort);
        setupCreditValueFixedWhichNotSet(pastelPort);
        setupCreditValueReferenceWhichNotSet(pastelPort);
        setupCreditBlankareaData(pastelPort);

        tp.methodExit();
    }

    /**
     * setup PastelPortTxSendBase TradeHeadData.
     *
     * @param pastelPort The Pastel Port Send Base.
     * @param tx    The Common TX.
     * @throws JSONException    The exception thrown when error occur.
     */
    private void setupTradeHeadData(final PastelPortTxSendBase pastelPort,
            final CommonTx tx) throws JSONException {
        // head data 1
        pastelPort.setLine1("0067");
        // head data 2
        pastelPort.setIdentification1("U2");
        // head data 3
        pastelPort.setBlankarea1("0000");
        // head data 4
        pastelPort.setBlankarea2("00");
        // head data 5
        pastelPort.setBlankarea3("01");
        // head data 6
        pastelPort.setBlankarea4("1");
        // head data 7
        pastelPort.setBlankarea5("0000");
        // head data 8
        String service = tx.getFieldValue("service");
        if (PastelPortConvert.SERVICE_REFUND.equals(service)
                || PastelPortConvert.SERVICE_TEST_REFUND.equals(service)) {
            pastelPort.setClassification("1000");
        } else if (PastelPortConvert.SERVICE_VOID.equals(service)
                || PastelPortConvert.SERVICE_TEST_VOID.equals(service)) {
            pastelPort.setClassification("2000");
        } else {
            pastelPort.setClassification("0000");
        }
        // head data 9
        pastelPort.setDescription("0000000000010000");
        // head data 10
        pastelPort.setBlankarea6("00000000");
        // head data 11
        pastelPort.setBlankarea7("0000");
        // head data 12
        pastelPort.setBlankarea8("00000000");
        // head data 13
        pastelPort.setBlankarea9("00000000");
    }

    /**
     * setup credit terminal information.
     * @param pastelPort    The Pastel Port Send Base.
     */
    private void setupCreditTerminalinfo(
            final PastelPortTxSendBase pastelPort) {
        String commpanyString = pastelPortEnv.getCompanyCode();
        String storeidString = pastelPort.getStoreid();
        String posNoString = pastelPort.getPosno();

        // credit data 48
        pastelPort
                .setTerminalinfo(commpanyString + storeidString + posNoString);
    }

    /**
     * setup credit value fixed which not set.
     * @param pastelPort    The Pastel Port Send Base.
     */
    private void setupCreditValueFixedWhichNotSet(
            final PastelPortTxSendBase pastelPort) {

        // credit data 1
        pastelPort.setLine2(PastelPortConvert.LINE2);
        // credit data 2
        pastelPort.setIdentification2(PastelPortConvert.IDENTIFICATION2);
        // credit data 3
        pastelPort.setDetermine("0");
        // credit data 4
        pastelPort.setType("000");
        // credit data 39 errorCode
        pastelPort.setErrorcode(getRepeatString(" ",
                ERROR_CODE_LENGTH));
        // credit data 47 messageCode
        pastelPort.setMessagecode("0000");
        // credit data 53
        pastelPort.setAlliance("0");
        // credit data 54
        pastelPort.setLocationdecision(" ");
        // credit data 55
        pastelPort.setResponsedata("0");
        // credit data 56
        pastelPort.setAlliancecode(getRepeatString(" ", 2));
        // credit data 57 AP
        pastelPort.setIdentificationap(getRepeatString(" ",
                IDENTIFICATION_NAP_LENGTH));
        // credit data 58
        pastelPort.setSerialnum(getRepeatString(" ", 2));
        // credit data 59
        pastelPort.setAffiliatedstorebizcode(getRepeatString(" ",
                STORE_ID_LENGTH));
        // credit data 60
        pastelPort.setProcessiccard("0");
        // credit data 61 reserve
        pastelPort.setReserve2(getRepeatString(" ", RESERVE2.length()));
        // credit data 62
        pastelPort.setIcrelatednum("0000");
        // credit data 63 ('20' is total space in 16 hex)
        pastelPort.setIcrelateddata(getRepeatString("20",
                IC_RELATED_VALUE / 2));
    }

    /**
     * setup credit value reference which not set.
     * @param pastelPort The Pastel Port Send Base.
     */
    private void setupCreditValueReferenceWhichNotSet(
            final PastelPortTxSendBase pastelPort) {

        // credit data 5
        pastelPort.setInfo(getRepeatString(" ",
                CARD_INPUT_TYPE_MAX_INFO_LENGTH));
        // credit data 6
        pastelPort.setJis1Info(getRepeatString(" ", MAX_JIS1_CARD_LENGTH));
        // credit data 7
        pastelPort.setJis2Info(getRepeatString(" ", MAX_JIS2_CARD_LENGTH));
        // credit data 10
        pastelPort.setCode("0000");
        // credit data 12
        pastelPort.setExpirationdate("000000");
        // credit data 13
        pastelPort.setPaymentseq(getRepeatString("0",
                MAX_PAYMENT_SEQ_LENGTH));
        // credit data 14
        pastelPort.setLastpaymentseq(getRepeatString("0",
                MAX_PAYMENT_SEQ_LENGTH));
        // credit data 15
        pastelPort.setGoodscode(getRepeatString("0", GOODS_CODE_LENGTH));
        // credit data 17
        pastelPort.setTaxshipping(getRepeatString("0", TAX_SHIPPING_LENGTH));
        // credit data 20
        pastelPort.setHandledivision("000");
        // credit data 21
        pastelPort.setStartpayment("00");
        // credit data 22
        pastelPort.setFirfirst(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 23
        pastelPort.setSplitnum("000");
        // credit data 24
        pastelPort.setAmountdivide1(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 25
        pastelPort.setAmountdivide2(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 26
        pastelPort.setAmountdivide3(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 27
        pastelPort.setAmountdivide4(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 28
        pastelPort.setAmountdivide5(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 29
        pastelPort.setAmountdivide6(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 30
        pastelPort.setBonusnum("00");
        // credit data 31
        pastelPort.setMonbonus1("00");
        // credit data 32
        pastelPort.setMonbonus2("00");
        // credit data 33
        pastelPort.setMonbonus3("00");
        // credit data 34
        pastelPort.setMonbonus4("00");
        // credit data 35
        pastelPort.setMonbonus5("00");
        // credit data 36
        pastelPort.setMonbonus6("00");
        // credit data 37
        pastelPort.setTransferamount(getRepeatString("0", MAX_AMOUNT_LENGTH));
        // credit data 41
        pastelPort.setApprovalnum(getRepeatString(" ", APPROVAL_NUMBER_LENGTH));
        // credit data 46
        pastelPort.setRequestcode("EB00");
        // credit data 49
        pastelPort.setSubcode(getRepeatString(" ", SUBCODE_LENGTH));
        // credit data 50
        pastelPort.setSecuinfo("4");
        // credit data 51
        pastelPort.setSecucode(getRepeatString(" ",
                DEFAULT_SECURITY_CODE_LENGTH));
        // credit data 52
        pastelPort.setJis2Secuinfo(getRepeatString(" ",
                JIS2_SECURITY_INFO_LENGTH));
    }

    /**
     * setup credit blank area data.
     * @param pastelPort The Pastel Port Send Base.
     */
    private void setupCreditBlankareaData(
            final PastelPortTxSendBase pastelPort) {

        // credit data 8
        pastelPort.setBlankarea10("    ");
        // credit data 9
        pastelPort.setBlankarea11("0");
        // credit data 11
        pastelPort.setBlankarea12("    ");
        // credit data 16
        pastelPort.setBlankarea13("000000000");
        // credit data 18
        pastelPort.setBlankarea14("000000000");
        // credit data 19
        pastelPort.setBlankarea15("000000000");
        // credit data 38
        pastelPort.setBlankarea16("00");
        // credit data 40
        pastelPort.setBlankarea17("00");
        // credit data 42
        pastelPort.setBlankarea18("00");
        // credit data 43
        pastelPort.setBlankarea19("00");
        // credit data 44
        pastelPort.setBlankarea20(getRepeatString(" ", BLANK_AREA_20_LENGTH));
        // credit data 45
        pastelPort.setBlankarea21("  ");
    }

    /**
     * @param lenth the length
     * @return  The sum of index String length.
     */
    private int addIndex(final int lenth) {
        indexString = indexString + lenth;
        return lenth;
    }

    /**
     * Convert String to Object.
     * @param pOPString The pointer of the String.
     * @return The {@link PastelPortTxRecvBase}
     * @throws IOException The Exception forInput and Output exception.
     */
    public final PastelPortTxRecvBase stringToObject(final byte[] pOPString)
            throws IOException {
        tp.methodEnter("stringToObject");
        if (null == pOPString || pOPString.length == 0) {
            tp.println("recvMsg didn't right");
            throw new IOException("recvMsg didn't right");
        }
        int length = 0;
        PastelPortTxRecvBase pastelPort = new PastelPortTxRecvImpl();
        indexString = 0;
        pastelPort.setLegth(new String(pOPString, indexString,
                addIndex(PASTEL_PORT_LENGTH)));
        // length
        length = Integer.valueOf(pastelPort.getLegth());
        if (pOPString.length != length) {
            tp.println("recvMsg didn't right"
                    + " recvLen is: " + pOPString.length
                    + " should be: " + length);
            throw new IOException("recvMsg didn't right");
        }

        // systemReservationTerritory2
        pastelPort.setSystemreservationterritory2(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_2)));
        // itemDiscrimination1
        pastelPort.setItemdiscrimination1(new String(pOPString, indexString,
                addIndex(2)));
        // systemReservationTerritory4
        pastelPort.setSystemreservationterritory4(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_4)));
        // systemReservationTerritory5
        pastelPort.setSystemreservationterritory5(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_5)));
        // countCheckFlag
        pastelPort.setCountcheckflag(new String(pOPString, indexString,
                addIndex(1)));
        // systemReservationTerritory7
        pastelPort.setSystemreservationterritory7(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_7)));
        // inqMessage
        pastelPort
                .setInqmessage(new String(pOPString, indexString, addIndex(1)));
        // sourceFlag
        pastelPort
                .setSourceflag(new String(pOPString, indexString, addIndex(1)));
        // applicationErrorFlag
        pastelPort.setApplicationerrorflag(new String(pOPString, indexString,
                addIndex(1)));
        // resultFlag
        pastelPort
                .setResultflag(new String(pOPString, indexString, addIndex(1)));
        // reserveField
        pastelPort.setReservefield(new String(pOPString, indexString,
                addIndex(RESERVED_FIELD_LENGTH)));
        // systemReservationTerritory13
        pastelPort.setSystemreservationterritory13(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_13)));
        // cancelNoticeFlag
        pastelPort.setCancelnoticeflag(new String(pOPString, indexString,
                addIndex(1)));
        // storeid
        pastelPort.setStoreid(new String(pOPString,
                indexString, addIndex(STORE_ID_LENGTH)));
        // posNo
        pastelPort.setPosno(new String(pOPString, indexString,
                addIndex(POS_NO_LENGTH)));
        // txid
        pastelPort.setTxid(new String(pOPString, indexString,
                addIndex(TRANS_NO_LENGTH)));
        // posOperateTime
        pastelPort.setPosoperatetime(new String(pOPString, indexString,
                addIndex(POS_OPERATE_TIME_LENGTH)));
        // systemReservationTerritory14
        pastelPort.setSystemreservationterritory14(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_14)));
        // finishFlag
        pastelPort
                .setFinishflag(new String(pOPString, indexString,
                        addIndex(FINISH_FLAG_LENGTH)));

        if (!pastelPort.getLegth().equals(PastelPortConvert.DEAL_JUDGE_ERROR)
                && "1".equals(pastelPort.getInqmessage())
                && "1".equals(pastelPort.getSourceflag())
                && "2".equals(pastelPort.getApplicationerrorflag())) {
            // when is deal judge error,only have the fixed head.
            tp.methodExit(pastelPort);
            return pastelPort;
        }

        // digit
        pastelPort.setDigit(new String(pOPString, indexString,
                addIndex(PASTELPORT_DIGIT)));
        // ItemDiscrimination2
        pastelPort.setItemdiscrimination2(new String(pOPString, indexString,
                addIndex(2)));
        // cardJudgeMethod
        pastelPort.setCardjudgemethod(new String(pOPString, indexString,
                addIndex(1)));
        // cardSort
        pastelPort.setCardsort(new String(pOPString, indexString,
                addIndex(CARD_SORT)));
        // cardInfo
        pastelPort
                .setCardinfo(new String(pOPString, indexString,
                        addIndex(CARD_INPUT_TYPE_MAX_INFO_LENGTH)));
        // cardInfoJIS1
        pastelPort.setCardinfojis1(new String(pOPString, indexString,
                addIndex(MAX_JIS1_CARD_LENGTH)));
        // cardInfoJIS2
        pastelPort.setCardinfojis2(new String(pOPString, indexString,
                addIndex(MAX_JIS2_CARD_LENGTH)));
        // systemReservationTerritory8
        pastelPort.setSystemreservationterritory8(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_8)));
        // systemReservationTerritory9
        pastelPort.setSystemreservationterritory9(new String(pOPString,
                indexString, addIndex(1)));
        // password
        pastelPort.setPassword(new String(pOPString, indexString,
                addIndex(PASSWORD_DIGIT)));
        // systemReservationTerritory11
        pastelPort.setSystemreservationterritory11(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_11)));
        // termValidity
        pastelPort.setTermvalidity(new String(pOPString, indexString,
                addIndex(TERMINAL_VALIDITY)));
        // voucherNo
        pastelPort.setPaymentseq(new String(pOPString, indexString,
                addIndex(MAX_PAYMENT_SEQ_LENGTH)));
        // originalVoucherNo
        pastelPort.setOriginalvoucherno(new String(pOPString, indexString,
                addIndex(ORIGINAL_VOUCHER_NO)));
        // productCode
        pastelPort.setProductcode(new String(pOPString, indexString,
                addIndex(PRODUCT_CODE)));
        // productTotal
        pastelPort.setProducttotal(new String(pOPString, indexString,
                addIndex(PRODUCT_TOTAL)));
        // taxCarriage
        pastelPort.setTaxcarriage(new String(pOPString, indexString,
                addIndex(TAX_CARRIAGE)));
        // systemReservationTerritory18
        pastelPort.setSystemreservationterritory18(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_18)));
        // systemReservationTerritory19
        pastelPort.setSystemreservationterritory19(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_19)));
        // PaymentMethodId
        pastelPort.setPaymentmethodid(new String(pOPString, indexString,
                addIndex(PAYMENT_METHODID)));
        // beginningDate
        pastelPort.setBeginningdate(new String(pOPString, indexString,
                addIndex(2)));
        // InitialAmount
        pastelPort.setInitialamount(new String(pOPString, indexString,
                addIndex(MAX_AMOUNT_LENGTH)));
        // paymentCount
        pastelPort.setPaymentcount(new String(pOPString, indexString,
                addIndex(PAYMENT_COUNT)));
        // payAmount1
        pastelPort
                .setPayamount1(new String(pOPString,
                        indexString, addIndex(MAX_AMOUNT_LENGTH)));
        // payAmount2
        pastelPort
                .setPayamount2(new String(pOPString, indexString,
                        addIndex(MAX_AMOUNT_LENGTH)));
        // payAmount3
        pastelPort
                .setPayamount3(new String(pOPString, indexString,
                        addIndex(MAX_AMOUNT_LENGTH)));
        // payAmount4
        pastelPort
                .setPayamount4(new String(pOPString, indexString,
                        addIndex(MAX_AMOUNT_LENGTH)));
        // payAmount5
        pastelPort
                .setPayamount5(new String(pOPString, indexString,
                        addIndex(MAX_AMOUNT_LENGTH)));
        // payAmount6
        pastelPort
                .setPayamount6(new String(pOPString, indexString,
                        addIndex(MAX_AMOUNT_LENGTH)));
        // bonusCount
        pastelPort
                .setBonuscount(new String(pOPString, indexString, addIndex(2)));
        // bonus1
        pastelPort.setBonus1(new String(pOPString, indexString, addIndex(2)));
        // bonus2
        pastelPort.setBonus2(new String(pOPString, indexString, addIndex(2)));
        // bonus3
        pastelPort.setBonus3(new String(pOPString, indexString, addIndex(2)));
        // bonus4
        pastelPort.setBonus4(new String(pOPString, indexString, addIndex(2)));
        // bonus5
        pastelPort.setBonus5(new String(pOPString, indexString, addIndex(2)));
        // bonus6
        pastelPort.setBonus6(new String(pOPString, indexString, addIndex(2)));
        // transferFund
        pastelPort.setTransferfund(new String(pOPString, indexString,
                addIndex(MAX_AMOUNT_LENGTH)));
        // systemReservationTerritory38
        pastelPort.setSystemreservationterritory38(new String(pOPString,
                indexString, addIndex(2)));
        // errorCode
        pastelPort
                .setErrorcode(new String(pOPString, indexString,
                        addIndex(ERROR_CODE_LENGTH)));
        // systemReservationTerritory40
        pastelPort.setSystemreservationterritory40(new String(pOPString,
                indexString, addIndex(2)));
        // approvalcode
        pastelPort.setApprovalcode(new String(pOPString, indexString,
                addIndex(APPROVAL_NUMBER_LENGTH)));
        // systemReservationTerritory42
        pastelPort.setSystemreservationterritory42(new String(pOPString,
                indexString, addIndex(2)));
        // systemReservationTerritory43
        pastelPort.setSystemreservationterritory43(new String(pOPString,
                indexString, addIndex(2)));
        // systemReservationTerritory44
        pastelPort.setSystemreservationterritory44(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_44)));
        // reservation1
        pastelPort.setReservation1(new String(pOPString, indexString,
                addIndex(2)));
        // requestCode
        pastelPort.setRequestcode(new String(pOPString, indexString,
                addIndex(REQUEST_CODE_LENGTH)));
        // messageTypeId
        pastelPort.setMessagetypeid(new String(pOPString, indexString,
                addIndex(MESSAGETYPEID.length())));
        // connectCompany
        pastelPort.setConnectcompany(new String(pOPString, indexString,
                addIndex(CONNECT_COMPANY)));
        // cafisSeq:CAFIS
        pastelPort.setCafisseq(new String(pOPString,
                indexString, addIndex(CAFIS_SEQUENCE_LENGTH)));
        // cafisDate
        pastelPort
                .setCafisdate(new String(pOPString,
                        indexString, addIndex(CAFIS_DATE_LENGTH)));
        // cafisApprovalcode
        pastelPort.setCafisapprovalcode(new String(pOPString, indexString,
                addIndex(1)));
        // systemTraceAuditNumber
        pastelPort.setSystemtraceauditnumber(new String(pOPString, indexString,
                addIndex(SYSTEM_TRACE_AUDIT_NUMBER)));
        // centerReceiptDate
        pastelPort.setCenterreceiptdate(new String(pOPString, indexString,
                addIndex(CENTER_RECEIPT_DATE_LENGTH)));
        // systemReservationTerritory54
        pastelPort.setSystemreservationterritory54(new String(pOPString,
                indexString, addIndex(SYSTEM_RESRV_TERITORY_LENGTH_54)));
        // permissionCode
        pastelPort.setPermissioncode(new String(pOPString, indexString,
                addIndex(PERMISSION_CODE_LENGTH)));
        // authorizationAgencyID
        pastelPort.setAuthorizationagencyid(new String(pOPString, indexString,
                addIndex(AUTHORIZATION_AGENCY_ID_LENGTH)));
        // printMemberID
        pastelPort.setPrintmemberid(new String(pOPString, indexString,
                addIndex(PRINT_MEMBER_ID_LENGTH)));
        // printValidityDate
        pastelPort.setPrintvaliditydate(new String(pOPString, indexString,
                addIndex(PRINT_VALIDITY_DATE_LENGTH)));
        // displayMessage
        pastelPort.setDisplaymessage(new String(pOPString, indexString,
                addIndex(DISPLAY_MESSAGE_LENGTH)));
        // printMessage
        pastelPort.setPrintmessage(new String(pOPString, indexString,
                addIndex(PRINT_MESSAGE_LENGTH)));
        // connectCompanyCode
        pastelPort.setConnectcompanycode(new String(pOPString, indexString,
                addIndex(CONNECTION_COMPANY_CODE_LENGTH)));
        // cooperationCode
        pastelPort.setCooperationcode(new String(pOPString, indexString,
                addIndex(2)));
        // reservation2
        pastelPort.setReservation2(new String(pOPString, indexString,
                addIndex(RESERVE2_LENGTH)));
        // iCRelatedDataValidityDigit
        pastelPort.setIcrelateddatavaliditydigit(new String(pOPString,
                indexString, addIndex(IC_RELATED_VALIDITY_DIGIT)));
        // iCRelatedData
        pastelPort.setIcrelateddata(new String(pOPString, indexString,
                addIndex(IC_RELATED_VALUE)));

        if (pastelPortEnv.isPrintPPLog()) {
            pastelPort.check();
        }
        tp.methodExit();
        return pastelPort;
    }
}
