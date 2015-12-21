package ncr.res.mobilepos.creditauthorization.check;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerLogDAO;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * Checking the existence and validity of the transaction be canceled.
 *
 * @version $Revision: 1.4 $ $Date: 2012/04/18 07:58:21 $
 */
public class VoidCheck {

    /** The Constant PROGNAME. */
    private static final String PROGNAME = "AuthPP";

    /** The logger. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The LogDAO interface. */
    private SQLServerLogDAO dao;

    /** The ioWriter display value. */
    protected static final String IOWDISP = "VOIDCHK";

    /** The err nodata. */
    protected static final String ERR_NODATA = "MC02";

    /** The err canceled. */
    protected static final String ERR_CANCELED = "MC03";

    /** The err amount. */
    protected static final String ERR_AMOUNT = "MC04";

    /** The err company. */
    protected static final String ERR_COMPANY = "MC05";

    /** The err method. */
    protected static final String ERR_METHOD = "MC06";

    /** The err pan. */
    protected static final String ERR_PAN = "MC07";

    /** The err expdate. */
    protected static final String ERR_EXPDATE = "MC08";

    /** The err status. */
    protected static final String ERR_STATUS = "08";

    /** The TXSTYPE_16. */
    protected static final int TXSTYPE_16 = 16;

    /** The TXSTYPE_2. */
    protected static final int TXSTYPE_2 = 2;

    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * Creates a VoidCheck Object.
     *
     * @throws DaoException
     *             thrown when database error occurs
     */
    public VoidCheck() throws DaoException {
        dao = new SQLServerLogDAO();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Checking the validity of the subject transaction cancellation of input
     * transactions.<br>
     * Confirmation of the existence of the original transaction
     * TXU_NPS_CreditLog, do a pre-cancellation confirmation. Amount /
     * expiration date / PAN / classification / payment credit card company must
     * be pre-exist cancellation Perform consistency checks。Legitimacy when an
     * error is to set the 08 to creditstatus。
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     * @throws DaoException
     *             thrown when database error occurs
     */
    public final void voidcheck(final CommonTx tx, final PastelPortResp resp)
            throws DaoException {
        tp.methodEnter("voidcheck");
        CommonTx dbtx = new CommonTx();
        try {
            if (dao.selectVoid(tx, dbtx)) {
                checkValidTx(tx, resp, dbtx);
                if (!resp.getPosReturn()) {
                    checkTxStatus(tx, resp, dbtx);
                }
                if (!resp.getPosReturn()) {
                    checkVoidFlag(tx, resp, dbtx);
                }
                if (!resp.getPosReturn()) {
                    checkAmount(tx, resp, dbtx);
                }
                if (!resp.getPosReturn()) {
                    checkCompany(tx, resp, dbtx);
                }
                if (!resp.getPosReturn()) {
                    checkMethod(tx, resp, dbtx);
                }
                if (!resp.getPosReturn()) {
                    String paymentseq = dbtx.getFieldValue("paymentseq");
                    if (paymentseq != null || !"".equals(paymentseq)) {
                        tx.setFieldValue("voidpaymentseq",
                                dbtx.getFieldValue("paymentseq"));
                    } else {
                        LOGGER.logAlert(PROGNAME, "VoidCheck.voidcheck",
                                Logger.RES_ERROR_RESTRICTION,
                                "voidpaymentseqにセット時、エラーが発生した。");
                    }
                }
            } else {
                nodata(tx, resp, dbtx);
            }
        } catch (JSONException je) {
            LOGGER.logAlert(PROGNAME, "VoidCheck.voidcheck",
                    Logger.RES_EXCEP_PARSE, "データ変換処理で例外発生" + je.getMessage());
            throw new DaoException("データ変換処理で例外発生" + je.getMessage(), je);

        } catch (DaoException de) {
            de.printStackTrace();
            LOGGER.logAlert(PROGNAME, "VoidCheck.voidcheck",
                    Logger.RES_EXCEP_DAO, "データベース処理で例外発生" + de.getMessage());
            throw new DaoException("データベース処理で例外発生" + de.getMessage(), de);
        } catch (IndexOutOfBoundsException ie) {
            LOGGER.logAlert(PROGNAME, "VoidCheck.voidcheck",
                    Logger.RES_ERROR_RESTRICTION,
                    "データベース処理で例外発生" + ie.getMessage());
            throw new DaoException("データベース処理で例外発生" + ie.getMessage(), ie);
        }
        tp.methodExit();
    }

    /**
     * If the processing of data to search for does not exist.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void nodata(final CommonTx tx, final PastelPortResp resp,
            final CommonTx dbtx) throws JSONException {
        tp.methodEnter("nodata");
        setResponse(tx, resp, ERR_NODATA, "取消対象取引なし");
        tp.methodExit();
    }

    /**
     * Check whether the transaction processing for cancellation.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkValidTx(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkValidTx");
        if (("SUBTRACT".equals(dbtx.getService()))
            && ((dbtx.getTxType() == TXSTYPE_2)
                        || (dbtx.getTxType() == TXSTYPE_16))
                && ("MSCREDIT".equals(dbtx.getBrand()) || 
                		"ICCREDIT".equalsIgnoreCase(dbtx.getBrand()))) {
            tp.methodExit("VALID");
            return;
        }

        setResponse(tx, resp, ERR_NODATA, "取消対象取引なし");
        tp.methodExit("ERR_NODATA");
    }

    /**
     * Check whether the transaction processing can be undone.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkTxStatus(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkTxStatus");
        String castat = dbtx.getFieldValue("txstatus");
        String castatSC = castat.substring(1, 2);
        String castatSFC = castat.substring(0, 2);
        String castatSF = castat.substring(3, 4);
        if ("0".equals(castatSC)
                || ("  ".equals(castatSFC) && "0".equals(castatSF))
                || ("8".equals(castatSC) && "8".equals(castatSF))
                || ("  ".equals(castatSFC) && "8".equals(castatSF))
                || ("8".equals(castatSC) && "0".equals(castatSF))) {
            tp.methodExit(castat);
            return;
        }
        setResponse(tx, resp, ERR_NODATA, "取消対象取引なし");
        tp.methodExit("ERR_NODATA");
    }

    /**
     * Check whether the pre-cancellation processing.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkVoidFlag(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkVoidFlag");
        int vflg = Integer.parseInt(dbtx.getFieldValue("voidflag"));
        if (vflg != 0) {
            setResponse(tx, resp, ERR_CANCELED, "取消済み");
        }
        tp.methodExit(vflg);
    }

    /**
     * Match the amount of check processing.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkAmount(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {

        tp.methodEnter("checkAmount");
        long oamount = Long.parseLong(dbtx.getFieldValue("amount"));
        long iamount = Long.parseLong(tx.getFieldValue("amount"));
        if (Math.abs(oamount) != Math.abs(iamount)) {
            setResponse(tx, resp, ERR_AMOUNT, "取消金額エラー");
        }
        tp.println("oamount", oamount).println("iamount", iamount);
        tp.methodExit();

    }

    /**
     * Check processing company match of code.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkCompany(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkCompany");
        int ocmp = Integer.parseInt(dbtx.getFieldValue("crcompanycode"));
        int icmp = Integer.parseInt(tx.getFieldValue("crcompanycode"));
        if (icmp != ocmp) {
            setResponse(tx, resp, ERR_COMPANY, "取消会社コードエラー");
        }
        tp.println("ocmp", ocmp).println("icmp", icmp);
        tp.methodExit();
    }

    /**
     * Check payment processing division match.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    protected final void checkMethod(final CommonTx tx,
            final PastelPortResp resp, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkMethod");
        int omethod = Integer.parseInt(dbtx.getFieldValue("paymentmethod"));
        int imethod = Integer.parseInt(tx.getFieldValue("paymentmethod"));
        if (imethod != omethod) {
            setResponse(tx, resp, ERR_METHOD, "取消支払区分エラー");
        }
        tp.println("omethod", omethod).println("imethod", imethod);
        tp.methodExit();
    }

    /**
     * Field settings PastelPortResp.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @param code
     *            the code
     * @param msg
     *            the msg
     * @throws JSONException
     *             thrown when JSON parsing error occurs
     */
    final void setResponse(final CommonTx tx, final PastelPortResp resp,
            final String code, final String msg) throws JSONException {
        tp.methodEnter("setResponse").println("code", code).println("msg", msg);
        if (tx.getTxType() != TXSTYPE_16) {
            resp.setPosReturn(true);
        }
        resp.setCreditstatus(ERR_STATUS);
        resp.setErrorcode(code);
        resp.setErrormessage(msg);
        tp.methodExit();
    }

}
