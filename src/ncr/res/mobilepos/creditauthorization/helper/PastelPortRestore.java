package ncr.res.mobilepos.creditauthorization.helper;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.pastelport.platform.PastelPortTxRecvBase;
import atg.taglib.json.util.JSONException;

/**
 * The Class PastelPortRestore.
 */
public class PastelPortRestore {


    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger

    /** The Constant IOWDISP. */
    protected static final String IOWDISP = "PASTELPORTRESTORE";

    /** The response table path. */
    private String responseTablePath;
    /** The Debug Logger Trace Printer. */
    private Trace.Printer tp;

    /**
     * Gets the response table path.
     *
     * @return the response table path
     */
    public final String getResponseTablePath() {
        return responseTablePath;
    }

    /**
     * Sets the response table path.
     *
     * @param respTablePath
     *            the new response table path
     */
    public final void setResponseTablePath(final String respTablePath) {
        this.responseTablePath = respTablePath;
    }

    /**
     * Create a new PastelPortRestore Object.
     */
    public PastelPortRestore() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * restore data to PastelPortResp.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the resp
     * @throws JSONException
     *             thrown when JSON parsing occurs
     */
    public final void restore(final PastelPortTxRecvBase cfstx,
            final PastelPortResp resp) throws JSONException {
        tp.methodEnter("restore")
        .println("PastelPortTxRecvBase", cfstx)
        .println("PastelPortResp", resp);

        // be careful of sequence of set value
        setupCreditstatusStatus(cfstx, resp);
        setupStatus(resp);
        setupResultcode(resp);
        setupExtendedResultCode(cfstx, resp);

      //RES3.1
        setupError(resp);
        setupApprovalCode(cfstx, resp);
        setupTraceNum(cfstx, resp);
        setupPaymentseq(cfstx, resp);
        tp.methodExit(resp);
    }

    /**
     * Set resultcode value.
     *
     * @param resp
     *            the new up resultcode
     */
    protected final void setupResultcode(final PastelPortResp resp) {
        tp.methodEnter("setupResultcode");

        resp.setResultCode(resp.getStatus());

        tp.methodExit();
    }

    /**
     * Set ExtendedResultCode value.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     */
    protected final void setupExtendedResultCode(
            final PastelPortTxRecvBase cfstx, final PastelPortResp resp) {
        tp.methodEnter("setupExtendedResultCode");
        resp.setExtendedResultCode(getStatus(cfstx, resp));
        tp.methodExit();
    }

    /**
     * Set status value.
     *
     * @param resp
     *            the new up status
     */
    protected final void setupStatus(final PastelPortResp resp) {
        tp.methodEnter("setupStatus");
        String applicationErrorFlag = resp.getCreditstatus();
        if ("00".equals(applicationErrorFlag)) {
            resp.setStatus("0");
        } else {
            resp.setStatus("9");
        }
        tp.methodExit();
    }

    /**
     * Set Creditstatus value.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     */
    protected final void setupCreditstatusStatus(
            final PastelPortTxRecvBase cfstx, final PastelPortResp resp) {
        tp.methodEnter("setupCreditstatusStatus");
        resp.setCreditstatus(getStatus(cfstx, resp));
        tp.methodExit();
    }

    /**
     * Sets the up error.
     *
     * @param resp
     *            the new up error
     * @throws JSONException
     *             thrown when JSON parsing occurs
     */
    protected final void setupError(final PastelPortResp resp)
            throws JSONException {
        tp.methodEnter("setupError");
        String errorMsg = resp.getErrorcode();
        String errMsg = "";
        if (!"0".equals(errorMsg)) {
            if ("MC09".equals(errorMsg)) {
                errMsg = "取引重複";
            } else if ("MC10".equals(errorMsg)) {
                errMsg = "復号エラー";
            } else {
                errMsg = "その他エラー";
            }
        }
        resp.setErrormessage(errMsg);
        tp.methodExit();
    }

    /**
     * Setup approval code.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     */
    protected final void setupApprovalCode(final PastelPortTxRecvBase cfstx,
            final PastelPortResp resp) {
        tp.methodEnter("setupApprovalCode");
        String approval = cfstx.getApprovalcode().trim();
        if (0 < approval.length()) {
            resp.setApprovalcode(approval);
        }
        tp.methodExit();
    }

    /**
     * Setup tracenum.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     */
    protected final void setupTraceNum(final PastelPortTxRecvBase cfstx,
            final PastelPortResp resp) {
        tp.methodEnter("setupTraceNum");
        String tracenum = cfstx.getCafisseq();
        if (0 < tracenum.length()) {
            resp.setTracenum(tracenum);
        }
        tp.methodExit();
    }

    /**
     * Gets status.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     * @return the status
     */
    protected final String getStatus(final PastelPortTxRecvBase cfstx,
            final PastelPortResp resp) {
        tp.methodEnter("getStatus");
        String ret = "";

        // cancelnoticeflag
        String cancelnoticeflag = cfstx.getCancelnoticeflag();
        resp.setCancelnoticeflag(cancelnoticeflag);
        // resultflag
        String resultflag = cfstx.getResultflag();
        // applicationerrorflag
        String applicationErrorFlag = cfstx.getApplicationerrorflag();

        String errorcodeString = cfstx.getErrorcode(); // errorcode
        String message = "";
        String creditstatus = "07";
        if ("0".equals(cancelnoticeflag)) {
            if ("0".equals(resultflag)) {
                if ("0".equals(applicationErrorFlag)) {
                    if ("    ".equals(errorcodeString)) {
                        ret = "0";
                        creditstatus = "00";
                    } else if ("G300".equals(errorcodeString)) {
                        message =
                            "カード会社センタで「取引保留（有人判定）」エラーが発生した。";
                        ret = "PP31";
                    } else {
                        ret = errorcodeString; // errorcodeString
                        message = "PastelPortセンタ／CAFISセンタ／"
                                + "カード会社センタでエラーが発生した。";
                    }
                } else if ("A".equals(applicationErrorFlag)) {
                    message = "環境エラーが発生した。";
                    ret = "PP35";
                } else if ("2".equals(applicationErrorFlag)) {
                    message = "オーソリ要求データのチェックが失敗した。";
                    ret = "PP36";
					LOGGER.logAlert(IOWDISP, "getStatus", ret, message);                 
                } else if ("1".equals(applicationErrorFlag)
                        || "4".equals(applicationErrorFlag)
                        || "6".equals(applicationErrorFlag)
                        || "7".equals(applicationErrorFlag)
                        || "B".equals(applicationErrorFlag)
                        || "C".equals(applicationErrorFlag)
                        || "E".equals(applicationErrorFlag)) { // 1,4,6,7,B,C,E
                    message = "オーソリ要求データの送信が失敗した。";
                    ret = "PP37";
                } else if ("3".equals(applicationErrorFlag)
                        || "5".equals(applicationErrorFlag)
                        || "8".equals(applicationErrorFlag)
                        || "9".equals(applicationErrorFlag)
                        || "D".equals(applicationErrorFlag)
                        || "Z".equals(applicationErrorFlag)) { // 3,5,8,9,D,Z
                    message = "オーソリ報告データの受信が失敗した。";
                    ret = "PP51";
                } else {
                    message = "想定外のエラーが発生した。";
                    ret = "PP38";
                }
            } else {
                message = "想定外のエラーが発生した。";
                ret = "PP39";
            }
        } else if ("1".equals(cancelnoticeflag)) {
            if ("0".equals(resultflag)) { // 0
                message = "自動取消完了。";
                ret = "PP40";
                LOGGER.logAlert(IOWDISP, "getStatus", ret, message);                
            } else if ("1".equals(resultflag) || "2".equals(resultflag)
                    || "4".equals(resultflag) || "6".equals(resultflag)
                    || "7".equals(resultflag) || "A".equals(resultflag)
                    || "B".equals(resultflag) || "C".equals(resultflag)
                    || "E".equals(resultflag)) { // 1,2,4,6,7,A,B,C,E
                message = "自動取消要求電文の送信が失敗した。";
                ret = "PP52";
            } else if ("3".equals(resultflag) || "5".equals(resultflag)
                    || "8".equals(resultflag) || "9".equals(resultflag)
                    || "D".equals(resultflag) || "Z".equals(resultflag)) {
                // 3,5,8,9,D,Z
                message = "自動取消報告電文の受信が失敗した。";
                ret = "PP53";
            } else {
                message = "想定外のエラーが発生した。";
                ret = "PP41";
            }
        } else {
            message = "想定外のエラーが発生した。";
            ret = "PP42";
        }

        tp.println(message);
        resp.setMessage(message);
        resp.setErrorcode(ret);

        tp.methodExit(creditstatus);
        return creditstatus;
    }

    /**
     * Set error code.
     *
     * @param code
     *            the error code
     * @return the error code
     */
    protected final String getErrorCode(final String code) {
        String errorCode = code;
        if ("OK".equals(errorCode)) {
            errorCode = "";
        }
        return (errorCode + "    ").substring(0, 4);
    }

    /**
     * Set PaymentSeq value.
     *
     * @param cfstx
     *            the cfstx
     * @param resp
     *            the pastel port response
     * @throws JSONException
     *             thrown when JSON parsing occurs
     */
    protected final void setupPaymentseq(final PastelPortTxRecvBase cfstx,
            final PastelPortResp resp) throws JSONException {
        tp.methodEnter("setupPaymentseq");
        resp.setPaymentseq(cfstx.getPaymentseq());
        tp.methodExit();
    }
}
