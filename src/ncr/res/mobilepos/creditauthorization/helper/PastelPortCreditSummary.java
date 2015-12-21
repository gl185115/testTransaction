package ncr.res.mobilepos.creditauthorization.helper;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.dao.ISummaryDAO;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerSummaryDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * CommonTx summary class.
 */
public class PastelPortCreditSummary {

    /** The Constant PROGNAME. */
    protected static final String PROGNAME = "AuthPP";

    /** The io writer. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The dao summary interface. */
    private ISummaryDAO dao;

    /** The Debug Logger Trace Printer. */
    private Trace.Printer tp;

    /** The TXTYPE_98. */
    private static final int TXTYPE_98 = 98;

    /** The TXTYPE_99. */
    private static final int TXTYPE_99 = 99;

    /** The TXTYPE_16. */
    private static final int TXTYPE_16 = 16;

    /** The TXTYPE_02. */
    private static final int TXTYPE_02 = 2;

    /** The Constant SERVICE_SUBTRACT. */
    public static final String SERVICE_SUBTRACT = "SUBTRACT";

    /** The Constant SERVICE_VOID. */
    public static final String SERVICE_VOID = "VOID";

    /** The Constant SERVICE_REFUND. */
    public static final String SERVICE_REFUND = "REFUND";

    /**
     * new a PastelPortCreditSummary.
     *
     * @throws DaoException
     *             thrown when DAO error occurs.
     */
    public PastelPortCreditSummary() throws DaoException {
        try {
            dao = new SQLServerSummaryDAO();
            tp = DebugLogger.getDbgPrinter(
                    Thread.currentThread().getId(), getClass());
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "PastelPortCreditSummary()", "A1",
					"PastelPortCreditSummary�������ŗ�O����\n" + ex.getMessage());
            throw ex;
        }
    }

    /**
     * when service is SUBTRACT/VOID/REFUND summary.
     *
     * @param tx
     *            the tx
     * @throws DaoException
     *             thrown when DAO error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    public final void update(final CommonTx tx) throws Exception {
        tp.methodEnter("update").println("CommonTX", tx.toString());
        CommonTx dbtx = new CommonTx();
        try {
            if (!dao.select(tx, dbtx)) {
                dao.insert(tx);
            }
            setupUpdateTx(tx, dbtx);
            if (!dao.updateSales(dbtx)) {
                tp.println("UPDATE�Ώۃ��R�[�h�Ȃ�");
                throw new DaoException("UPDATE�Ώۃ��R�[�h�Ȃ�");
            }
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "update", "A1",
					"�f�[�^�x�[�X�����ŗ�O����\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "update", "A1",
					"JSON��������O����\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * update summary.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    private void setupUpdateTx(final CommonTx tx, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("setupUpdateTx");
        dbtx.setFieldValue("corpid", tx.getFieldValue("corpid"));
        dbtx.setFieldValue("storeid", tx.getFieldValue("storeid"));
        dbtx.setFieldValue("terminalid", tx.getFieldValue("terminalid"));
        dbtx.setFieldValue("salesamount", "0");
        dbtx.setFieldValue("salescount", "0");
        dbtx.setFieldValue("voidamount", "0");
        dbtx.setFieldValue("voidcount", "0");
        dbtx.setFieldValue("refundamount", "0");
        dbtx.setFieldValue("refundcount", "0");
        dbtx.setFieldValue("cancelamount", "0");
        dbtx.setFieldValue("cancelcount", "0");
        long amount = Math.abs(Long.parseLong(tx.getFieldValue("amount")));
        long count = 0L;
        String txstatus = tx.getFieldValue("txstatus");
        switch (tx.getTxType()) {
        case PastelPortCreditSummary.TXTYPE_02:
            if ("00  ".equals(txstatus) || "10  ".equals(txstatus)) {
                count = 1L;
            }
            break;
        case PastelPortCreditSummary.TXTYPE_16:
            if (this.isValidTXTYPE16(txstatus)) {
                count = 1L;                
            }
            break;
        case PastelPortCreditSummary.TXTYPE_98:
        case PastelPortCreditSummary.TXTYPE_99:
            if ("0000".equals(txstatus) || "1000".equals(txstatus)) {
                count = -1L;
            }
            break;
        default:
            break;
        }
        amount *= count;
        String service = tx.getService();
        if (amount != 0L) {
            if (service.equals(PastelPortCreditSummary.SERVICE_SUBTRACT)) {
                dbtx.setFieldValue("salesamount", Long.toString(amount));
                dbtx.setFieldValue("salescount", Long.toString(count));
            } else if (service.equals(PastelPortCreditSummary.SERVICE_VOID)) {
                dbtx.setFieldValue("voidamount", Long.toString(amount));
                dbtx.setFieldValue("voidcount", Long.toString(count));
            } else if (service.equals(PastelPortCreditSummary.SERVICE_REFUND)) {
                dbtx.setFieldValue("refundamount", Long.toString(amount));
                dbtx.setFieldValue("refundcount", Long.toString(count));
            }
            if (amount < 0L) {
                dbtx.setFieldValue("cancelamount", Long.toString(amount * -1L));
                dbtx.setFieldValue("cancelcount", Long.toString(count * -1L));
            }
        }
        tp.methodExit();
    }

    /**
     * POS init(service=INITIALIZE,txtype=16) get message process.
     *
     * @param tx
     *            the tx
     * @throws DaoException
     *             thrown when DAO error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    public final void init(final CommonTx tx) throws Exception {
        tp.methodEnter("init").println("CommonTX", tx);
        try {
            dao.updateInit(tx);
            dao.insert(tx);
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "init", "A1",
					"�f�[�^�x�[�X�����ŗ�O����\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "init", "A1",
					"JSON��������O����\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * close POS(service=SERVICECLOSE,txtype=16)process.
     *
     * @param tx
     *            the tx
     * @throws DaoException
     *             thrown when DAO error occurs.
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    public final void finish(final CommonTx tx) throws Exception {
        tp.methodEnter("finish")
        .println("CommonTX", tx);
        CommonTx dbtx = new CommonTx();
        try {
            if (!dao.select(tx, dbtx)) {
                tp.println("�T�}���[���R�[�h�Ȃ�(POS�s��v)");
            } else {
                if (!checkSummary(tx, dbtx)) {
                    tp.println(makeIowMessage(tx, dbtx));
                } else {
                    tp.println("POS��SVR�ŁATOTAL�ɍ��ق͂���܂���");
                }
            }
            dao.updateFinish(tx);
            dao.insert(tx);
        } catch (DaoException ex) {
			LOGGER.logAlert(PROGNAME, "finish", "A1",
					"�f�[�^�x�[�X�����ŗ�O����\n" + ex.getMessage());
            throw ex;
        } catch (JSONException ex) {
			LOGGER.logAlert(PROGNAME, "finish", "A1",
					"JSON��������O����\n" + ex.getMessage());
            throw ex;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * check summary.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @return true, if successful
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    private boolean checkSummary(final CommonTx tx, final CommonTx dbtx)
            throws JSONException {
        tp.methodEnter("checkSummary");
        boolean bresult = true;
        if (Long.parseLong(dbtx.getFieldValue("salesamount")) != (Long
                .parseLong(tx.getFieldValue("totalsubtractamount")))) {
            tp.println("�s��v(salesAmount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("salescount")) != (Long
                .parseLong(tx.getFieldValue("totalsubtractcount")))) {
            tp.println("�s��v(salesCount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("voidamount")) != (Long
                .parseLong(tx.getFieldValue("totalvoidamount")))) {
            tp.println("�s��v(voidAmount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("voidcount")) != (Long
                .parseLong(tx.getFieldValue("totalvoidcount")))) {
            tp.println("�s��v(voidCount)");
            bresult =  false;
        } else if (Long.parseLong(dbtx.getFieldValue("refundamount")) != (Long
                .parseLong(tx.getFieldValue("totalrefundamount")))) {
            tp.println("�s��v(refundAmount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("refundcount")) != (Long
                .parseLong(tx.getFieldValue("totalrefundcount")))) {
            tp.println("�s��v(refundCount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("cancelamount")) != (Long
                .parseLong(tx.getFieldValue("totalauthcancelamount")))) {
            tp.println("�s��v(cancelAmount)");
            bresult = false;
        } else if (Long.parseLong(dbtx.getFieldValue("cancelcount")) != (Long
                .parseLong(tx.getFieldValue("totalauthcancelcount")))) {
            tp.println("�s��v(cancelCount)");
            bresult = false;
        }
        tp.methodExit(bresult);
        return bresult;
    }

    /**
     * when exception write log.
     *
     * @param tx
     *            the tx
     * @param dbtx
     *            the dbtx
     * @return the string
     * @throws JSONException
     *             thrown when JSON parsing occurs.
     */
    private String makeIowMessage(final CommonTx tx, final CommonTx dbtx)
            throws JSONException {
        StringBuilder sb = new StringBuilder();
        sb.append("TOTAL�ɍ��ق�����܂��B\n");
        sb.append(java.text.MessageFormat.format(
                "������z�@�@ POS[{0,number,0000000000}] "
                        + "SVR[{1,number,0000000000}]\n"
                        + "���㌏���@�@ POS[{2,number,0000000000}] "
                        + "SVR[{3,number,0000000000}]\n"
                        + "������z�@�@ POS[{4,number,0000000000}] "
                        + "SVR[{5,number,0000000000}]\n"
                        + "��������@�@ POS[{6,number,0000000000}] "
                        + "SVR[{7,number,0000000000}]\n"
                        + "�ԕi���z�@�@ POS[{8,number,0000000000}] "
                        + "SVR[{9,number,0000000000}]\n"
                        + "�ԕi�����@�@ POS[{10,number,0000000000}] "
                        + "SVR[{11,number,0000000000}]\n"
                        + "��Q������z POS[{12,number,0000000000}] "
                        + "SVR[{13,number,0000000000}]\n"
                        + "��Q������� POS[{14,number,0000000000}] "
                        + "SVR[{15,number,0000000000}]",
                new Object[] {
                        new Long(tx.getFieldValue("totalsubtractamount")),
                        new Long(dbtx.getFieldValue("salesamount")),
                        new Long(tx.getFieldValue("totalsubtractcount")),
                        new Long(dbtx.getFieldValue("salescount")),
                        new Long(tx.getFieldValue("totalvoidamount")),
                        new Long(dbtx.getFieldValue("voidamount")),
                        new Long(tx.getFieldValue("totalvoidcount")),
                        new Long(dbtx.getFieldValue("voidcount")),
                        new Long(tx.getFieldValue("totalrefundamount")),
                        new Long(dbtx.getFieldValue("refundamount")),
                        new Long(tx.getFieldValue("totalrefundcount")),
                        new Long(dbtx.getFieldValue("refundcount")),
                        new Long(tx.getFieldValue("totalauthcancelamount")),
                        new Long(dbtx.getFieldValue("cancelamount")),
                        new Long(tx.getFieldValue("totalauthcancelcount")),
                        new Long(dbtx.getFieldValue("cancelcount")), }));
        return sb.toString();
    }
    
    private boolean isValidTXTYPE16(String txstatus){
    	return "  00".equals(txstatus) || "  10".equals(txstatus)
                || "  08".equals(txstatus) || "  18".equals(txstatus)
                || "0800".equals(txstatus) || "0810".equals(txstatus)
                || "1800".equals(txstatus) || "1810".equals(txstatus)
                || "0808".equals(txstatus) || "0818".equals(txstatus)
                || "1808".equals(txstatus) || "1818".equals(txstatus);
    }
    
}
