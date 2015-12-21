package ncr.res.mobilepos.creditauthorization.helper;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.dao.ILogDAO;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerLogDAO;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * To manage the progress of trade PastelPort.
 */
public class PastelPortAdmin {

    /** The logger. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The Constant PROGNAME. */
    protected static final String PROGNAME = "AuthPP";

    /** The Constant ERR_DUP. */
    protected static final String ERR_DUP = "MC09";

    /** The dao log interface. */
    private ILogDAO dao;

    /** The dcifmode. */
    private String dcifmode = "DONTKNOW";
    /** The DCSTATUS_7. */
    private static final int DCSTATUS_7 = 7;
    /** The DCSTATUS_8. */
    private static final int DCSTATUS_8 = 8;
    /** The TXTYPE_98. */
    private static final int TXTYPE_98 = 98;
    /** The TXTYPE_99. */
    private static final int TXTYPE_99 = 99;
    /** The TXTYPE_16.*/
    private static final int TXTYPE_16 = 16;
    /** The CASTATUS_0.*/
    private static final int CASTATUS_0 = 0;
    /** The CASTATUS_10.*/
    private static final int CASTATUS_10 = 10;
    /** The CASTATUS_7.*/
    private static final int CASTATUS_7 = 7;

    /** The Debug Log Trace Printer. */
    private Trace.Printer tp;
    /**
     * To create a new instance of PastelPortAdmin.
     *
     * @throws DaoException
     *             thrown when cannot create instance of iowriter and dao.
     */
    public PastelPortAdmin() throws DaoException {
        try {
            dao = new SQLServerLogDAO();
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        } catch (DaoException e) {
			LOGGER.logAlert(PROGNAME, "PastelPortAdmin()", "A1",
					"PastelPortAdmin初期化で例外発生\n" + e.getMessage());
            throw e;
        }
    }

    /**
     * DC to set the mode of data integration.
     *
     * @param mode
     *            the new dc send mode
     */
    public final void setDcSendMode(final String mode) {
        dcifmode = mode;
    }

    /**
     * Determination process of the transaction cancellation return true in the
     * case of void otherwise return false.
     *
     * @param tx
     *            the tx
     * @return true, if is void tx
     * @throws JSONException
     *             the jSON exception
     */
    protected final boolean isVoidTx(final CommonTx tx) throws JSONException {
        return "VOID".equals(tx.getService())
            || "REFUND".equals(tx.getService());
    }

    /**
     * Determination of abnormal transaction processing aborted return true in
     * the case of 98 or 99 otherwise return false.
     *
     * @param tx
     *            the tx
     * @return true, if is abort tx
     * @throws JSONException
     *             the jSON exception
     */
    protected final boolean isAbortTx(final CommonTx tx) throws JSONException {
        return tx.getTxType() == TXTYPE_98 || tx.getTxType() == TXTYPE_99;
    }

    /**
     * Progress of state management process before sending CA.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the resp
     * @throws DaoException
     *             the dao exception
     * @throws JSONException
     *             the jSON exception
     */
    public final void sendca(final CommonTx tx, final PastelPortResp resp)
            throws Exception {
        tp.methodEnter("sendca")
          .println("CommonTx", tx.toString())
          .println("PastelPortResp", resp.toString());
        try {
            boolean exist = dao.select(tx, new CommonTx());
            if (!exist && (!dao.insert(tx))) {
                    exist = true;
            }
            tx.setOtherSysIF(checkCaIf(tx, resp, exist));
            if (exist) {
                resp.setPosReturn(true);
                resp.setCreditstatus("07");
                resp.setErrorcode(PastelPortAdmin.ERR_DUP);
                resp.setErrormessage("取引重複");
                tp.println("取引重複エラー");
            }
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "sendca", "A1",
					"データベース処理で例外発生\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "sendca", "A1",
					"JSON内部が例外発生\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.println("CFS/CN連動対象");
            tp.methodExit();
        }
    }

    /**
     * To determine whether the external interface to the system this CA.
     *
     * @param tx
     *            CommonTx stored transaction information
     * @param resp
     *            PastelPortResp stored transaction information
     * @param exist
     *            True ,false otherwise When records exist in the DB
     * @return If the required interface return true otherwise return false
     */
    protected final boolean checkCaIf(final CommonTx tx,
            final PastelPortResp resp, final boolean exist) {
        tp.methodEnter("checkCalf");
        boolean caif = false;
        if (!exist && !resp.getPosReturn()) {
            caif = true;
        }
        tp.methodExit(caif);
        return caif;
    }

    /**
     * Progress of the state managemant process after receiving CA.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastelport response
     * @throws DaoException
     *             thrown when there is database error.
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     */
    public final void recvca(final CommonTx tx, final PastelPortResp resp)
            throws Exception {
        tp.methodEnter("recvca")
        .println("CommonTX", tx.toString())
        .println("PastelPortResp", resp.toString());
        try {
            setupTxStatus(tx, resp);
            String errcd = resp.getErrorcode();
            if (errcd != null && errcd.equals(ERR_DUP)) {
                tp.println("重複CAのため更新対象外");
                return;
            }

            if (!dao.updateRca(tx, resp)) {
                tp.println("UPDATE対象レコードなし");
                throw new DaoException("UPDATE対象レコードなし");
            }

            // when is autoVoid(auto)
            String cancelnoticeflag = resp.getCancelnoticeflag();
            if ((cancelnoticeflag != null) 
                    && ("1".equals(cancelnoticeflag))
                    && (0 == dao.updateServerPaymentseq(tx, resp))) {
             // RES3.1の対応 START
                    tp.println("更新ServerPaymentSeq情報時、エラーが発生した。");
                    throw new DaoException(
                            "更新ServerPaymentSeq情報時、エラーが発生した。");
            // RES3.1の対応 END
            }

            if (isVoidTx(tx)) {
                voidCaProc(tx, resp);
            }
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "recvca", "A1",
					"データベース処理で例外発生\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "recvca", "A1",
					"JSON内部が例外発生\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * Update processing of information after receiving the cancellation of the
     * CA.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastelport response
     * @throws DaoException
     *             thrown when there is database error.
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     */
    protected final void voidCaProc(final CommonTx tx,
            final PastelPortResp resp) throws Exception {
        tp.methodEnter("voidCaProc");
        int castatus = Integer.parseInt(resp.getCreditstatus());
        if ((castatus == CASTATUS_0) || (castatus == CASTATUS_10)) {
            CommonTx vdtx = setupVoidTx(tx);
            if (!dao.updateVoid(vdtx)) {
                tp.println("取消対象UPDATEエラー");
            }
        }
        tp.methodExit();
    }

    /**
     * Progress of the state management process before sending DC.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastelport response
     * @throws DaoException
     *             thrown when there is database error.
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     * @see setDcSendMode
     */
    public final void senddc(final CommonTx tx, final PastelPortResp resp)
            throws Exception {
        tp.methodEnter("senddc")
        .println("CommonTX", tx.toString())
        .println("PastelPortResp", resp.toString());
        try {
            CommonTx dbtx = new CommonTx();
            boolean exist = dao.select(tx, dbtx);
            if (!exist) {
                if (!dao.insert(tx)) {
                    exist = true;
                    dao.select(tx, dbtx);
                } else {
                    if (tx.getTxType() == TXTYPE_16) {
                        PastelPortResp.communicationIsFailed(resp, "9", "07",
                                "MC99", "その他エラー");
                        tp.println("その他エラー");
                        resp.setPosReturn(true);
                    } else if (tx.getTxType() == TXTYPE_98) {
                        PastelPortResp.communicationIsFailed(resp, "9", "07",
                                "MC02", "取消対象取引なし");
                        tp.println("取消対象取引なし");
                        resp.setPosReturn(true);
                    }
                }
            }
            int castatus = 0;
            if (exist) {
                if (dbtx.getTxType() != 2) {
                    tp.println("既存レコードがCAでない");
                    throw new DaoException("既存レコードがCAでない");
                }
                if (!dao.updateSdc(tx)) {
                    tp.println("UPDATE処理で異常発生");
                    throw new DaoException("UPDATE処理で異常発生");
                }
                if (tx.getTxType() == TXTYPE_98) {
                    // when is autoVoid then must be false
                    resp.setPosReturn(false);
                } else {
                    resp.setPosReturn(true);
                    resp.setCreditstatus("00");
                }
                try {
                    castatus = Integer.parseInt(dbtx.getFieldValue("castatus"));
                } catch (Exception e) {
                    castatus = CASTATUS_7;
                }
            }
            tx.setOtherSysIF(checkDcIf(tx, resp, exist, castatus));
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "senddc", "A1",
					"データベース処理で例外発生\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "senddc", "A1",
					"JSON内部が例外発生\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.println("CFS/CN連動対象" + tx.getOtherSysIF());
            tp.methodExit();
        }
    }

    /**
     * to determine whether this DC external interface to the system.
     *
     * @param tx
     *            CommonTx stored transaction information
     * @param resp
     *            PastelPortResp stored transaction information
     * @param exist
     *            when records exist in the db return true otherwise return
     *            false
     * @param castatus
     *            Castatus of the value of the DB
     * @return true, if successful
     * @throws JSONException
     *             the jSON exception
     */
    protected final boolean checkDcIf(final CommonTx tx,
            final PastelPortResp resp, final boolean exist, final int castatus)
            throws JSONException {
        tp.methodEnter("checkDcIf");
        boolean dcif = false;
        if (!exist) {
            if (!isAbortTx(tx) && !resp.getPosReturn()) {
                dcif = true;
            }
        } else {
            if (isAbortTx(tx) && (castatus == 0)) {
                dcif = true;
            }
            if ("CAFIS".equals(dcifmode) && !resp.getPosReturn()) {
                dcif = true;
            }
        }
        tp.methodExit(dcif);
        return dcif;
    }

    /**
     * Progress of the state management process after receiving DC.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the resp
     * @throws DaoException
     *             the dao exception
     * @throws JSONException
     *             the jSON exception
     */
    public final void recvdc(final CommonTx tx, final PastelPortResp resp)
            throws Exception {
        tp.methodEnter("recvdc").println("CommonTX", tx.toString())
        .println("PastelPortResp", resp.toString());
        try {
            setupTxStatus(tx, resp);

            if (!dao.updateRdc(tx, resp)) {
                tp.println("UPDATE対象レコードなし");
                throw new DaoException("UPDATE対象レコードなし");
            }

            // when is autoVoid(manual)
            if (isAbortTx(tx)) {
                if (0 == dao.updateServerPaymentseq(tx, resp)) {
                    tp.println("更新ServerPaymentSeq情報時、エラーが発生した。");
                    throw new DaoException(
                            "更新ServerPaymentSeq情報時、エラーが発生した。");
                }
                abortDcProc(tx, resp);
            }
            tp.println("最終返送データ" + resp.toString());
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "recvdc", "A1",
					"データベース処理で例外発生\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "recvdc", "A1",
					"JSON内部が例外発生\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * creation process of trading status after receiving CA/DC.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastelport response
     * @throws DaoException
     *             thrown when there is database error.
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     */
    protected final void setupTxStatus(final CommonTx tx,
            final PastelPortResp resp) throws DaoException, JSONException {
        tp.methodEnter("setupTxStatus");
        String txstatus;
        String crstatus = "00" + resp.getCreditstatus();
        crstatus = crstatus.substring(crstatus.length() - 2);
        // Processing in the case of CA
        if (tx.getTxType() == 2) {
            txstatus = crstatus + "  ";
        } else {
         // Processing in the case of DC
            CommonTx dbtx = new CommonTx();
            dao.select(tx, dbtx);
            try {
                txstatus = dbtx.getFieldValue("txstatus");
                txstatus = txstatus.substring(0, 2) + crstatus;
            } catch (JSONException e) {
                txstatus = "  " + crstatus;
            }

            String approvalcode = null;
            try {
                approvalcode = dbtx.getFieldValue("approvalcode");
                tx.setFieldValue("approvalcode", approvalcode);
            } catch (JSONException ex) {
                tx.setFieldValue("approvalcode", approvalcode);
            }
            if (resp.getTracenum() == null) {
                try {
                    String tracenum = dbtx.getFieldValue("tracenum");
                    resp.setTracenum(tracenum);
                } catch (JSONException ex) {
					LOGGER.logAlert(PROGNAME, "setupTxStatus", "A1",
							"JSON内部が例外発生\n" + ex.getMessage());
                }
            }
            if (tx.getTxType() == TXTYPE_99) {
                String amount = dbtx.getFieldValue("amount");
                if (amount != null) {
                    tx.setFieldValue("amount", amount);
                    tp.println("集計金額設定" + amount);
                }
            }
        }
        tx.setFieldValue("txstatus", txstatus);
        tp.println("txstatus" + txstatus);
        tp.methodExit();
    }

    /**
     * CommonTx setup process for updating the revocation information.
     *
     * @param tx
     *            the tx
     * @return the common tx
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     */
    protected final CommonTx setupVoidTx(final CommonTx tx)
            throws JSONException {
        tp.methodEnter("setupVoidTx");
        CommonTx vdtx = new CommonTx();
        vdtx.setFieldValue("corpid", tx.getFieldValue("voidcorpid"));
        vdtx.setFieldValue("storeid", tx.getFieldValue("voidstoreid"));
        vdtx.setFieldValue("terminalid", tx.getFieldValue("voidterminalid"));
        vdtx.setFieldValue("paymentseq", tx.getFieldValue("voidtxid"));
        vdtx.setFieldValue("txdatetime", tx.getFieldValue("voidtxdatetime"));
        vdtx.setFieldValue("voidflag", "1");
        vdtx.setFieldValue("voidcorpid", tx.getFieldValue("corpid"));
        vdtx.setFieldValue("voidstoreid", tx.getFieldValue("storeid"));
        vdtx.setFieldValue("voidterminalid", tx.getFieldValue("terminalid"));
        vdtx.setFieldValue("voidpaymentseq", tx.getFieldValue("paymentseq"));
        vdtx.setFieldValue("voidtxid", tx.getFieldValue("txid"));
        vdtx.setFieldValue("voidtxdatetime", tx.getFieldValue("txdatetime"));
        tp.methodExit();
        return vdtx;
    }

    /**
     * DC after receiving treatment discontinuation.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastelport response
     * @throws DaoException
     *             thrown when there is database error.
     * @throws JSONException
     *             thrown when there is JSON parsing error.
     */
    protected final void abortDcProc(final CommonTx tx,
            final PastelPortResp resp) throws Exception {
        tp.methodEnter("abortDcProc");
        if (!tx.getOtherSysIF()) {
            tp.methodExit();
            return;
        }

        int dcstatus = Integer.parseInt(resp.getCreditstatus());
        if (dcstatus == 0) {
            CommonTx cantx = setupCancelTx(tx);
            dao.updateVoid(cantx);
            if ("VOID".equals(tx.getService())) {
                CommonTx vdtx = setupVoidTx(tx);
                vdtx.setFieldValue("voidflag", "0");
                if (!dao.updateVoid(vdtx)) {
                    tp.println("取消対象UPDATEエラー");
                }
            }
        }
        if ((dcstatus == DCSTATUS_7) || (dcstatus == DCSTATUS_8)) {
            tp.println("障害取消拒否報告 (" + resp.getErrorcode() + ")");
            tx.setFieldValue("alertflag", "3");
        }
        tp.methodExit();
    }

    /**
     * CommonTx setup process for updating the revocation information failure.
     *
     * @param tx
     *            the tx
     * @return the common tx
     * @throws JSONException
     *             the jSON exception
     */
    protected final CommonTx setupCancelTx(final CommonTx tx)
            throws JSONException {
        tp.methodEnter("setupCancelTx");
        CommonTx cantx = new CommonTx();
        cantx.setFieldValue("corpid", tx.getFieldValue("corpid"));
        cantx.setFieldValue("storeid", tx.getFieldValue("storeid"));
        cantx.setFieldValue("terminalid", tx.getFieldValue("terminalid"));
        cantx.setFieldValue("paymentseq", tx.getFieldValue("paymentseq"));
        cantx.setFieldValue("txdatetime", tx.getFieldValue("txdatetime"));
        cantx.setFieldValue("voidflag", "2");
        cantx.setFieldValue("voidcorpid", tx.getFieldValue("corpid"));
        cantx.setFieldValue("voidstoreid", tx.getFieldValue("storeid"));
        cantx.setFieldValue("voidterminalid", tx.getFieldValue("terminalid"));
        cantx.setFieldValue("voidpaymentseq", tx.getFieldValue("paymentseq"));
        cantx.setFieldValue("voidtxid", tx.getFieldValue("txid"));
        cantx.setFieldValue("voidtxdatetime", tx.getFieldValue("txdatetime"));
        tp.methodExit();
        return cantx;
    }
}
