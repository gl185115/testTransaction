package ncr.res.mobilepos.creditauthorization.check;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.dao.ILoginCheckDAO;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerLoginCheckDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * Checking the existence and validity of the transaction be canceled.
 */
public class LoginCheck {

    /** The Constant PROGNAME. */
    private static final String PROGNAME = "AuthPP";

    /** The Constant ERR_DUP. */
    protected static final String ERR_DUP = "MC09";

    /** The logger. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;

    /** The interface of LoginCheckDAO. */
    private ILoginCheckDAO loginCheckDAO;

    /**
     * LoginCheck to generate a new object.
     *
     * @throws DaoException
     *             thrown when database error occurs
     */
    public LoginCheck() throws DaoException {
        try {
            loginCheckDAO = new SQLServerLoginCheckDAO();
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        } catch (DaoException e) {
            throw e;
        }
    }

    /**
     * Login check.
     *
     * @param tx
     *            the tx
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    public final boolean loginCheck(final CommonTx tx) throws DaoException {
        tp.methodEnter("loginCheck");
        boolean reLogin = false;
        try {
            if (!loginCheckDAO.getLoggingInfo(tx)) {
                loginCheckDAO.saveLoggingInfo(tx);
            } else {
                LOGGER.logAlert(LoginCheck.PROGNAME,
                        "LoginCheck.loginCheck()", LoginCheck.ERR_DUP,
                        "このレコードをTXL_NPS_TxLogに存在しています。異常応答返信");
                tp.println("このレコードをTXL_NPS_TxLogに存在しています。異常応答返信");
                reLogin = true;
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(LoginCheck.PROGNAME, "LoginCheck.loginCheck()",
                    Logger.RES_EXCEP_DAO, "SQL例外発生" + ex.getMessage());
            throw new DaoException("SQL例外発生" + ex.getMessage(), ex);
        } catch (JSONException ex) {
            LOGGER.logAlert(LoginCheck.PROGNAME, "LoginCheck.loginCheck()",
                    Logger.RES_EXCEP_PARSE, "タグなし" + ex.getMessage());
            throw new DaoException("タグなし" + ex.getMessage(), ex);
        }catch (Exception ex) {
            LOGGER.logAlert(LoginCheck.PROGNAME, "LoginCheck.loginCheck()",
                    Logger.RES_EXCEP_GENERAL, ex.getMessage());
            throw new DaoException(ex.getMessage(), ex);
        }
        tp.methodExit(reLogin);
        return reLogin;
    }
}
