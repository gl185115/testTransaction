/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResource
 *
 * Resource which provides Web Service for credit authorization
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.xml.bind.JAXBException;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.creditauthagency.dao.IExtCARequestDAO;
import ncr.res.mobilepos.creditauthagency.resource.CreditAuthAgencyResource;
import ncr.res.mobilepos.creditauthorization.check.LoginCheck;
import ncr.res.mobilepos.creditauthorization.check.SchemaCheck;
import ncr.res.mobilepos.creditauthorization.check.VoidCheck;
import ncr.res.mobilepos.creditauthorization.constant.CreditAuthRespConstants;
import ncr.res.mobilepos.creditauthorization.dao.ILogDAO;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerLogDAO;
import ncr.res.mobilepos.creditauthorization.helper.CreditAuthMsgLogger;
import ncr.res.mobilepos.creditauthorization.helper.MobileDecryption;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortAdmin;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortCreditSummary;
import ncr.res.mobilepos.creditauthorization.helper.PrivateKey;
import ncr.res.mobilepos.creditauthorization.helper.Sha256KeyGenerator;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.Key;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.constants.PosLogRespConstants;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.networkreceipt.model.SettlementManagement;
import ncr.res.pastelport.platform.CommonTx;
import ncr.res.pastelport.platform.PastelPortIf;
import atg.taglib.json.util.JSONException;

/**
 * CreditAuthorizationResource Web Resource.
 *
 * <P>
 * Processes the authorization of the credit card payment.
 */
@Path("/creditauthorization")
public class CreditAuthorizationResource {
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * Instance of MultiSnap for outputing error transaction to snap file.
     */
    private SnapLogger snap;
    /** The program name use in logging. */
    private String progName = "CredAuth";

    /** The class name use in logging. */
    private String className = "CreditAuthorizationResource";

    /** The Constant CARDINPUTTYPE_JIS1. */
    private static final String CARDINPUTTYPE_JIS1 = "1";

    /** The Constant GENDATE_FIXED. */
    private static final String GENDATE_FIXED = "fixed";

    /** The private key. */
    private PrivateKey privateKey = null;

    /** The key value. */
    private static Key result = new Key();

    /** The sha256 for encryption. */
    private Sha256KeyGenerator sha256 = null;

    /** The credit authorization message logger. */
    private CreditAuthMsgLogger creditAuthMsgLogger = new CreditAuthMsgLogger();

    /** The CaAgency resource instance. */
    private CreditAuthAgencyResource caAgencyRes =
        new CreditAuthAgencyResource();

    /**
     * Gets the sha256.
     *
     * @return the sha256.
     */
    public final Sha256KeyGenerator getSha256() {
        return sha256;
    }

    /**
     * Sets the sha256.
     *
     * @param sha256ToSet
     *            the new sha256
     */
    public final void setSha256(final Sha256KeyGenerator sha256ToSet) {
        this.sha256 = sha256ToSet;
    }

    /** The context. */
    @Context
    private ServletContext context;

    /**
     * Sets the context.
     *
     * @param contextToSet
     *            the new context
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Gets the private key.
     *
     * @return the private key
     */
    public final PrivateKey getPrivateKey() {
        return privateKey;
    }

    /**
     * Sets the private key.
     *
     * @param privateKeyToSet
     *            the new private key
     */
    public final void setPrivateKey(final PrivateKey privateKeyToSet) {
        this.privateKey = privateKeyToSet;
    }

    /**
     * Instantiates a new credit authorization resource.
     */
    public CreditAuthorizationResource() {
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
        this.snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * internal use only.
     * The web resource method called by the Web Service to implement the Credit
     * Authorization of a transaction.
     *
     * @param creditAuthorizationXml
     *            The Credit Authorization XML format
     * @return The Credit Authorization Response data that tells if the Credit
     *         Authorization has been successful
     */
    protected CreditAuthorizationResp authorizeExtCredit(
            @FormParam("creditauthxml") final String creditAuthorizationXml) {

        String functionName = className + ".authorizeExtCredit";
        tp.methodEnter("authorizeExtCredit");
        tp.println("creditAuthorizationXml", creditAuthorizationXml);
        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        CreditAuthorizationResp creditResponse = new CreditAuthorizationResp();

        CreditAuthMsgLogger creditAuthMsgLoggr = new CreditAuthMsgLogger();
        try {
            // Set the second parameter to true to tell that The CA came from
            // the external POS
            creditResponse = creditAuthMsgLoggr.log(creditAuthorizationXml,
                    true);

        } catch (DaoException e) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to process ExtCreditAuthorization.\n"
                            + e.getMessage());
            creditResponse.setStatus(CreditAuthRespConstants.STATUS_ERROR_END);
            creditResponse
                    .setCreditstatus(CreditAuthRespConstants.CREDITSTATUS);
            creditResponse
                    .setErrorcode(CreditAuthRespConstants.ERRORCODE_ERROR_END);
            creditResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            Snap.SnapInfo info =
                    snap.write("The XML data of credit authorization.\n",
                    creditAuthorizationXml);
            LOGGER.logSnap(progName, functionName,
                    "Output credit authorization xml data to snap", info);
        } catch (Exception e) {
            creditResponse.setStatus(CreditAuthRespConstants.STATUS_ERROR_END);
            creditResponse
                    .setCreditstatus(CreditAuthRespConstants.CREDITSTATUS);
            creditResponse
                    .setErrorcode(CreditAuthRespConstants.ERRORCODE_ERROR_END);

            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process ExtCreditAuthorization.\n"
                            + e.getMessage());
            creditResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            Snap.SnapInfo info =
                    snap.write("The XML data of credit authorization.\n",
                    creditAuthorizationXml);
            LOGGER.logSnap(progName, functionName,
                    "Output credit authorization xml data to snap", info);
        } finally {
            tp.methodExit(creditResponse);
        }

        return creditResponse;
    }

    /**
     * The web resource method called by the Web Service to implement the
     * LOCK/RELEAS of Credit Authorization of a transaction.
     *
     * @param toLock
     *            The Lock Flag
     * @param storeid
     *            The Store ID of CA Request
     * @param postermid
     *            The POS TerminalID of CA request
     * @param txid
     *            The Transaction ID of CA request
     * @param txdate
     *            The Transaction Date of CA request
     * @return The Restriction codes
     */
    @Path("/requestcreditlock/{lockflag}")
    @POST
    @Produces({ MediaType.APPLICATION_JSON })
    public final ResultBase lockExtCredit(
            @PathParam("lockflag") final boolean toLock,
            @FormParam("storeid") final String storeid,
            @FormParam("termid") final String postermid,
            @FormParam("txid") final String txid,
            @FormParam("txdate") final String txdate) {

        String lockFlg = "";
        if (toLock) {
            lockFlg = "Lock";
        } else {
            lockFlg =  "Release";
        }

        String functionName = className + "lockExtCredit";
        tp.methodEnter("lockExtCredit");
        tp.println("lockFlag", lockFlg);
        tp.println("storeId", storeid);
        tp.println("posTermId", postermid);
        tp.println("txId", txid);
        tp.println("txDate", txdate);
        ResultBase res = new ResultBase();

        CreditAuthMsgLogger creditAuthMsgLoggr = new CreditAuthMsgLogger();
        try {
            CreditAuthorization credit = new CreditAuthorization();
            credit.setTerminalid(postermid);
            credit.setTxdatetime(txdate);
            credit.setTxid(txid);
            credit.setStoreid(storeid);
            // Set the second parameter to true to tell that The CA came from
            // the external POS
            int rowResult = creditAuthMsgLoggr.lock(credit, toLock);

            if (rowResult == -1) {
                res.setNCRWSSResultCode(ResultBase.RESEXTCA_ERROR_LOCKFAIL);
                tp.println("Failed to lock CA entry.");
            } else {
                res.setNCRWSSResultCode(0);
            }
        } catch (DaoException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    "It is failed to lockExtCredit\n" + ex.getMessage());
            res.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "It is failed to lockExtCredit\n" + ex.getMessage());
            res.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(res);
        }

        return res;
    }

    /**
     * @deprecated
     * Manual authorize credit.
     *
     * @param posLogXml
     *            the POSLog xml
     * @param creditAuthorizationXml
     *            the Credit Authorization xml
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @return the credit authorization resp
     */
    @Deprecated
    @Path("/requestmanualauthorize")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final CreditAuthorizationResp manualAuthorizeCredit(
            @FormParam("poslogxml") final String posLogXml,
            @FormParam("creditauthxml") final String creditAuthorizationXml,
            @FormParam("deviceno") final String deviceNo,
            @FormParam("operatorno") final String operatorNo) {

        String functionName = className + "manualAuthorizeCredit";
        tp.methodEnter("manualAuthorizeCredit");
        tp.println("deviceNo", deviceNo);
        tp.println("operatorNo", operatorNo);
        tp.println("posLogXml", posLogXml);
        tp.println("creditAuthorizationXml", creditAuthorizationXml);

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        CreditAuthorizationResp creditAuthorizationResp =
            new CreditAuthorizationResp();

        // Create external CA request
        CreditAuthAgencyResource ag = new CreditAuthAgencyResource();
        PosLogResp posLogResponse = ag.uploadCARequest(posLogXml);

        try {
            if (PosLogRespConstants.ERROR_END_1 != posLogResponse.getStatus()) {
                PosLog posLog = new PosLog();
                XmlSerializer<PosLog> xml = new XmlSerializer<PosLog>();
                posLog = (PosLog) xml.unMarshallXml(posLogXml, PosLog.class);

                // Acquire lock of the request
                Transaction trax = posLog.getTransaction();

                SimpleDateFormat dateInputFormat = new SimpleDateFormat(
                        "yyyy-MM-dd");
                SimpleDateFormat dateOutputFormat = new SimpleDateFormat(
                        "yyyyMMddHHmmss");
                String businessDateString = dateOutputFormat
                        .format(dateInputFormat.parse(trax
                                .getBusinessDayDate()));

                ResultBase acquireLock = this.lockExtCredit(true,
                        trax.getRetailStoreID(), trax.getWorkStationID().getValue(),
                        trax.getSequenceNo(), businessDateString);

                if (0 == acquireLock.getNCRWSSResultCode()) {
                    // Authorize request
                    creditAuthorizationResp = this
                            .authorizeExtCredit(creditAuthorizationXml);
                } else {
                    creditAuthorizationResp.setStatus(Integer
                            .toString(acquireLock.getNCRWSSResultCode()));
                    creditAuthorizationResp
                            .setMessage(acquireLock.getMessage());
                    creditAuthorizationResp.setErrormessage(acquireLock
                            .getMessage());
                    creditAuthorizationResp.setNCRWSSResultCode(acquireLock
                            .getNCRWSSResultCode());
                }

            } else {
                creditAuthorizationResp.setStatus(posLogResponse.getStatus());
                creditAuthorizationResp.setMessage(posLogResponse.getMessage());
                creditAuthorizationResp.setErrormessage(posLogResponse
                        .getMessage());
                creditAuthorizationResp.setNCRWSSResultCode(posLogResponse
                        .getNCRWSSResultCode());
            }

        } catch (JAXBException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_JAXB,
                    "Failed to process manual invoice credit"
                            + " authorization request.\n" + ex.getMessage());
            creditAuthorizationResp
                    .setStatus(CreditAuthRespConstants.STATUS_ERROR_END);
            creditAuthorizationResp.setMessage("Invalid POSLOg XML");
            creditAuthorizationResp.setErrormessage(ex.getMessage());
            creditAuthorizationResp
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);

            // Output error transaction data to snap file.
            Snap.SnapInfo[] infos = new Snap.SnapInfo[2];
            infos[0] = snap.write("posLog xml data", posLogXml);
            infos[1] = snap.write("CA xml data", creditAuthorizationXml);
            // Logging in IOW.
            LOGGER.logSnap(progName, functionName,
                    "Output error transaction data to snap file.", infos);
        } catch (ParseException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_JAXB,
                    "Failed to process manual invoice credit authorization"
                            + " request. Invalid date format.\n"
                            + ex.getMessage());
            creditAuthorizationResp
                    .setStatus(CreditAuthRespConstants.STATUS_ERROR_END);
            creditAuthorizationResp.setMessage("Invalid date format");
            creditAuthorizationResp.setErrormessage(ex.getMessage());
            creditAuthorizationResp
                    .setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            // Output error transaction data to snap file.
            Snap.SnapInfo[] infos = new Snap.SnapInfo[2];
            infos[0] = snap.write("posLog xml data", posLogXml);
            infos[1] = snap.write("CA xml data", creditAuthorizationXml);
            // Logging in IOW.
            LOGGER.logSnap(progName, functionName,
                    "Output error transaction data to snap file.", infos);
        } finally {
            tp.methodExit(creditAuthorizationResp);
        }

        return creditAuthorizationResp;
    }

    /**
     * The web resource method called by the Web Service to implement the Credit
     * Authorization of a transaction with Pastelport.
     *
     * @param strMsgjson
     *            The Credit Authorization Json format
     * @return The Credit Authorization Response data that tells if the Credit
     *         Authorization has been successful
     */
    @Path("/pastelport")
    @POST
    @Produces({ MediaType.APPLICATION_JSON  + ";charset=UTF-8" })
    public PastelPortResp authorizeCreditWithPastelPort(
            final String strMsgjson, final String companyId,
            final String storeId) {

        String functionName = className + ".authorizeCreditWithPastelPort";
        tp.methodEnter("authorizeCreditWithPastelPort");
        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }
        Date d = new Date();
        long beforeMs = d.getTime();
        PastelPortResp resp = new PastelPortResp();
        
        // for return immediately when is needed
        for (int i = 0; i < 1; i++) {
            try {
                CommonTx tx = new CommonTx();
                VoidCheck voidCheck = new VoidCheck();
                tx.setJsonValue(strMsgjson);
                // when is DECRYPTION,didn't need logincheck
                if (!"DECRYPTION".equals(tx.getFieldValue("service"))) {
                    // Before credit authorization service, update lasttxid in deviceinfo.
                    try {
                        IDeviceInfoDAO iDeviceInfoDao =
                                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER)
                                .getDeviceInfoDAO();
                        boolean lasttxidUpdated = true;
                        lasttxidUpdated =
                                iDeviceInfoDao.updateLastTxidAtCreditAuth(strMsgjson);
                        if (!lasttxidUpdated) {
                            PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                                    "その他エラー");
                            tp.methodExit("Failed to update last txid.");
                            return resp;
                        }
                    } catch (DaoException daoEx) {
                        PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                                "その他エラー");
                        LOGGER.logAlert(progName, functionName,
                                Logger.RES_EXCEP_GENERAL,
                                "DaoException when update last txid" + daoEx.getMessage());
                        tp.methodExit("Failed to update last txid.");
                        return resp;
                    }
                    
                    // login
                    LoginCheck loginCheck = new LoginCheck();
                    if (loginCheck.loginCheck(tx)) {
                        PastelPortResp.communicationIsFailed(resp, "9", "07",
                                "MC99", "その他エラー");
                        tx.setDuplicate(true);
                        break;
                    }
                }

                // SchemaCheck
                if (!SchemaCheck.check(tx)) {
                    LOGGER.logAlert(progName, functionName,
                            Logger.RES_EXCEP_GENERAL,
                            "check of commonTx is failed!");
                    PastelPortResp.communicationIsFailed(resp, "9", "07",
                            "MC99", "その他エラー");
                    break;
                }

                // decryption
                setValueByDecryption(tx, resp, context, companyId, storeId);
                // RES3.1
                // データ検証
                if ("VOID".equals(tx.getService())
                        || "REFUND".equals(tx.getService())) {
                    voidCheck.voidcheck(tx, resp);
                }
                if ("DECRYPTION".equals(tx.getFieldValue("service"))) {
                    tp.println("DECRYPTION execute");
                    break;
                }
                // Get pastelPortEnv
                PastelPortEnv pastelPortEnv = (PastelPortEnv) context
                        .getAttribute(GlobalConstant.PASTELPORTENV_PARAM_KEY
                                .toString());

                // include calculate the settlement number
                setValueBeforeStart(tx, resp, pastelPortEnv);

                // for the moment just deal with retail
                String service = tx.getService();
                if ("SUBTRACT".equals(service) || "VOID".equals(service)
                        || "REFUND".equals(service)) {
                    tx.setOtherSysIF(true);
                    tx.setIsneedmanage(true);
                } else if ("TEST".equals(service)
                        || "TESTSUBTRACT".equals(service)
                        || "TESTVOID".equals(service)
                        || "TESTREFUND".equals(service)) {
                    // don't communicate with CA/DC
                    tx.setOtherSysIF(true);
                    tx.setIsneedmanage(false);
                } else if ("RETURNONLY".equals(service)) {
                    // don't communicate with pastelport and don't communicate
                    // with
                    // CA/DC
                    tx.setOtherSysIF(false);
                    tx.setIsneedmanage(false);
                } else if ("INITIALIZE".equals(service)
                        || "SERVICECLOSE".equals(service)) {
                    // don't communicate with pastelport
                    tx.setOtherSysIF(false);
                    tx.setIsneedmanage(true);
                } else {
                    PastelPortResp.communicationIsFailed(resp, "9", "07",
                            "MC99", "その他エラー");
                    break;
                }

                // for the moment just deal with retail
                if (!"02".equals(tx.getFieldValue("txtype"))
                        && !"16".equals(tx.getFieldValue("txtype"))
                        && !"98".equals(tx.getFieldValue("txtype"))
                        && !"99".equals(tx.getFieldValue("txtype"))) {
                    PastelPortResp.communicationIsFailed(resp, "9", "07",
                            "MC99", "その他エラー");
                    break;
                }

                // initialize the record
                PastelPortAdmin ppAdmin = new PastelPortAdmin();
                if (tx.isNeedmanage()) {
                    if ("02".equals(tx.getFieldValue("txtype"))) {
                        ppAdmin.sendca(tx, resp);
                    } else {
                        ppAdmin.senddc(tx, resp);
                    }
                }
                d = new Date();
                long beforeExternalPastelMs = d.getTime();
                // the other system is running
                if (tx.getOtherSysIF()) {
                    PastelPortIf.communicationWithPastalPort(pastelPortEnv, tx,
                            resp);
                }
                d = new Date();
                long afterExternalPastelMs = d.getTime()
                        - beforeExternalPastelMs;
                tp.println("The time it takes for External pastelport"
                                + " to finish is " + afterExternalPastelMs
                                + " ms");

                // update the record
                if (tx.isNeedmanage()) {
                    if ("02".equals(tx.getFieldValue("txtype"))) {
                        ppAdmin.recvca(tx, resp);
                    } else {
                        ppAdmin.recvdc(tx, resp);
                    }
                    PastelPortCreditSummary ppSummary =
                        new PastelPortCreditSummary();
                    if ("INITIALIZE".equals(service) && tx.getTxType() == 16) {
                        ppSummary.init(tx);
                    } else if ("SERVICECLOSE".equals(service)
                            && tx.getTxType() == 16) {
                        ppSummary.finish(tx);
                    } else {
                        ppSummary.update(tx);
                    }
                }

            } catch (JSONException e) {
                PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                        "その他エラー");
                LOGGER.logAlert(progName, functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "JSONException" + e.getMessage());
                // Output error transaction data to snap file.
                Snap.SnapInfo info = snap.write("CA JSON data",
                        strMsgjson);
                // Logging in IOW.
                LOGGER.logSnap(progName, functionName,
                        "Output error transaction data to snap file.", info);
            } catch (DaoException e) {
                PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                        "その他エラー");
                LOGGER.logAlert(progName, functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "DaoException" + e.getMessage());
                // Output error transaction data to snap file.
                Snap.SnapInfo info = snap.write("CA JSON data",
                        strMsgjson);
                // Logging in IOW.
                LOGGER.logSnap(progName, functionName,
                        "Output error transaction data to snap file.", info);
            } catch (RuntimeException e) {
                PastelPortResp.communicationIsFailed(resp, "9", "07", "MC99",
                        "その他エラー");
                LOGGER.logAlert(progName, functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "RuntimeException" + e.getMessage());
                // Output error transaction data to snap file.
                Snap.SnapInfo info = snap.write("CA JSON data",
                        strMsgjson);
                // Logging in IOW.
                LOGGER.logSnap(progName, functionName,
                        "Output error transaction data to snap file.", info);
            } catch (Exception e) {
                // check is the error message already Has been set up
                if ("0".equals(resp.getStatus())) {
                    PastelPortResp.communicationIsFailed(resp, "9", "07",
                            "MC99", "その他エラー");
                }
                LOGGER.logAlert(progName, functionName,
                        Logger.RES_EXCEP_GENERAL, "Exception" + e.getMessage());
                // Output error transaction data to snap file.
                Snap.SnapInfo info = snap.write("CA JSON data",
                        strMsgjson);
                // Logging in IOW.
                LOGGER.logSnap(progName, functionName,
                        "Output error transaction data to snap file.", info);
            }
        }
        d = new Date();
        long afterMs = d.getTime() - beforeMs;
        tp.println(
                "Time it takes for credit authorization to finish is "
                + afterMs + " ms");
        tp.methodExit(resp);
        return resp;
    }

    /**
     * decryption the carddata and keep the value in tx.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the PastelPort response
     * @param contextParam
     *            : must have a PastelPortEnv
     * @throws Exception
     *             the exception
     */
    public final void setValueByDecryption(final CommonTx tx,
            final PastelPortResp resp, final ServletContext contextParam,
            final String companyId, final String storeId)
            throws Exception {

        String functionName = className + "setValueByDecryption";
        tp.methodEnter("setValueByDecryption");

        try {
            MobileDecryption decryption = new MobileDecryption(
                    tx.getFieldValue("carddata"), contextParam,
                    companyId, storeId);

            if ("".equals(decryption.getPan()) || null == decryption.getPan()) {
                tp.println("PAN is null.");
                throw new Exception("Pan in null");
            }
            tx.setFieldValue("pan", decryption.getPan());

            if ("".equals(decryption.getPan4())
                    || null == decryption.getPan4()) {
                tp.println("PAN4 is null.");
                throw new Exception("pan4 in null");
            }
            tx.setFieldValue("pan4", decryption.getPan4());

            if ("".equals(decryption.getPan6())
                    || null == decryption.getPan6()) {
                tp.println("PAN6 is null.");
                throw new Exception("pan6 in null");
            }
            tx.setFieldValue("pan6", decryption.getPan6());
            tx.setFieldValue("expirationdate", decryption.getExpirationDate());
            tx.setFieldValue("creditcompanycode",
                    decryption.getCreditCompanyCode());
            tx.setFieldValue("creditcompanyname",
                    decryption.getCreditCompanyName());
            tx.setFieldValue("crcompanycode",
                    decryption.getCreditCompanyCode());

            if (decryption.getCardInputType().equals(CARDINPUTTYPE_JIS1)) {
                tx.setFieldValue("Jis1CardData", decryption.getJis1data());
            } else {
                tx.setFieldValue("Jis2CardData", decryption.getJis2data());
                tx.setFieldValue("jis2data",
                        decryption.getJis2dataInformation());
            }

            resp.setPan4(tx.getFieldValue("pan4"));
            resp.setPan6(tx.getFieldValue("pan6"));
            resp.setExpdate(tx.getFieldValue("expirationdate"));
            resp.setCorpid(tx.getFieldValue("corpid"));
            resp.setStoreid(tx.getFieldValue("storeid"));
            resp.setTerminalid(tx.getFieldValue("terminalid"));
            resp.setTxid(tx.getFieldValue("txid"));
            resp.setAmount(tx.getFieldValue("amount"));
            resp.setPaymentmethod(tx.getFieldValue("paymentmethod"));
            resp.setCreditcompanycode(tx.getFieldValue("creditcompanycode"));
            resp.setCreditcompanyname(tx.getFieldValue("creditcompanyname"));

            // Decryption is successful
            resp.communicationIsSuccessful();
        } catch (Exception e) {
            PastelPortResp.communicationIsFailed(resp, "9", "07", "MC10",
                    "復号エラー");
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "復号エラー");
            throw e;
        } finally {
            tp.methodExit();
        }
    }

    /**
     * Include calculate the settlement number and set field value in tx that is
     * needed.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the PastelPort response
     * @param pastelPortEnv
     *            the pastel port env
     * @throws JSONException
     *             : when don't have a tag in tx
     * @throws DaoException
     *             the dao exception
     */
    public final void setValueBeforeStart(final CommonTx tx,
            final PastelPortResp resp, final PastelPortEnv pastelPortEnv)
            throws Exception {
        tp.methodEnter("setValueBeforeStart");
        tx.setFieldValue("alliance", "0");
        tx.setFieldValue("locationdecision", " ");
        tx.setFieldValue("responsedata", "0");
        tx.setFieldValue("alliancecode", "  ");

        // 2012/5/14
        String paymentseq = null;
        ILogDAO dao = new SQLServerLogDAO();
        // RES3.1の対応 START
        int rePaymentseq = dao.getMaxServerPaymentseq(tx);
        // RES3.1の対応 END
        try {
            // RES3.1の対応 START
            paymentseq = tx.getFieldValue("paymentseq");
            if (0 == Integer.valueOf(paymentseq)) {
                // RES3.1の対応 END
                if (0 == rePaymentseq) {
                    paymentseq = "1";
                } else {
                    paymentseq = Integer.toString(rePaymentseq);
                }
            }
        } catch (JSONException ex) {
            paymentseq = Integer.toString(rePaymentseq);
            if ("0".equals(paymentseq)) {
                paymentseq = "1";
            }
        }
        tx.setFieldValue("paymentseq", paymentseq);
        resp.setPaymentseq(paymentseq);
        // calculate the Settlement of parameter
        SettlementManagement settlementManagement = new SettlementManagement(tx
                .getFieldValue("txdatetime").substring(0, 6),
                tx.getFieldValue("storeid"), tx.getFieldValue("terminalid"),
                tx.getFieldValue("paymentseq"), "03",
                tx.getFieldValue("crcompanycode"), tx.getFieldValue("amount"),
                tx.getFieldValue("pan"), tx.getFieldValue("expirationdate"),
                tx.getFieldValue("paymentmethod"));
        String settlementnum = settlementManagement.getSettlementNum();

        tx.setFieldValue("settlementnum", settlementnum);

        tx.setFieldValue("recvcompanycode",
                pastelPortEnv.getHandledCompanyCode());

        resp.setTillid(tx.getFieldValue("paymentseq"));
        // Add Recvcompanycode and Settlementnum for RES-3794
        resp.setRecvcompanycode(tx.getFieldValue("recvcompanycode"));
        resp.setSettlementnum(settlementnum);
        tp.methodExit();
    }

    /**
     * Web resource method used to retrieve current aes key used by AES
     * encryption.
     * 
     * @param companyId the company ID
     * @param storeId the store ID
     * @return the aes key
     */
    @Path("/key")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final Key getAesKey(
            @FormParam("companyid") final String companyId,
            @FormParam("storeid") final String storeId) {

        String functionName = className + ".getAesKey ";
        tp.methodEnter("getAesKey")
            .println("companyid", companyId)
            .println("storeid", storeId);

        // for when Flag in PRM_SYSTEM_CONFIG is true
        String flag = (String) context.getAttribute(GlobalConstant.FLAG);
        if (flag != null && "true".equalsIgnoreCase(flag)) {
            String key = (String) context.getAttribute(GlobalConstant.KEYVALUE);

            String resultkey = "";

            try {
                byte[] hash = key.getBytes();
                resultkey = Base64.getEncoder().encodeToString(hash);
            } catch (Exception e) {
                LOGGER.logAlert(
                        progName,
                        functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "Error in generating Base64. AESKey flag is true\n"
                                + e.getMessage());
                tp.methodExit("Failed to generate fixed key.");
                return new Key();
            }

            result = new Key();
            result.setGenDate(GENDATE_FIXED);
            result.setKey64(resultkey);
            tp.methodExit(result);
            return result;
        }

        // Import keystore file to java keystore
        try {
            privateKey = new PrivateKey(
                    (String) context.getInitParameter("AESKeyStorePath"),
                    (String) context.getInitParameter("AESKeyStorePass"));
        } catch (Exception e) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "AESKeyStorePath is "
                            + (String) context
                                    .getInitParameter("AESKeyStorePath")
                            + "\n AESKeyStorePass is "
                            + (String) context
                                    .getInitParameter("AESKeyStorePass") + "\n"
                            + e.getMessage());
            tp.methodExit("Failed to read keystore parameters from context.");
            return result;
        }

        // Get key AESKeyGenDate in keystore file
        String genDate = privateKey.getKey((String) context
                .getInitParameter("AESKeyStoreGenDateAlias"));

        if (sha256 == null) {
            String masterKey = privateKey.getAesKey();
            sha256 = new Sha256KeyGenerator(masterKey);
        }
        String now = sha256.now();
        // Get the businessDayDate
        JournalizationResource jourService =
            new JournalizationResource(context);
        String businessDayDate = jourService.getBussinessDate(companyId, storeId);

        // Is AESKeyGenDate available in keystore file?
        if (genDate.isEmpty()) {
            result = generateAesKeyToday(now); // Generate AesKey
            // for today and store it in keystore
        } else {
            SimpleDateFormat sdfDateTime = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyyMMdd");
            SimpleDateFormat sdfBussDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfTime = new SimpleDateFormat("HHmmss");

            // Get yyyyMMdd of AESKeyGenDate & BusinessDayDate
            String genDateInKeyStore = parseDate(sdfDate, sdfDateTime, genDate);
            String businessDate = parseDate(sdfDate, sdfBussDate,
                    businessDayDate);

            // Is AESKeyGenDate equals to businessdate?
            if (!genDateInKeyStore.equals(businessDate)) {
                String timeNow = parseDate(sdfTime, sdfDateTime, now);
                String requestedDateTime = businessDate + timeNow;

                try {
                    result = generateAesKeyToday(requestedDateTime);
                } catch (Exception e) {
                    LOGGER.logAlert(progName, functionName,
                            Logger.RES_EXCEP_GENERAL, e.getMessage());
                    tp.println("Failed to generate key for "
                            + requestedDateTime);
                }
            } else {
                result = generateAesKeyToday(genDate);
            }
        }
        tp.methodExit(result);
        return result;
    }

    /**
     * Web resource method used to retrieve the aes key for the specific version
     * AES encryption.
     *
     * @param genDate
     *            the gen date
     * @return the aes key with date
     */
    @Path("/key/version/{gendate}")
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    public final Key getAesKeyWithDate(
            @PathParam("gendate") final String genDate) {

        String functionName = className + ".getAesKey ";
        tp.methodEnter("getAesKeyWithDate");
        tp.println("genDate", genDate);
        if (genDate != null && "fixed".equalsIgnoreCase(genDate)) {
            String key = (String) context.getAttribute(GlobalConstant.KEYVALUE);

            String resultkey = "";

            try {
                byte[] hash = key.getBytes();
                resultkey = Base64.getEncoder().encodeToString(hash);
            } catch (Exception e) {
                LOGGER.logAlert(
                        progName,
                        functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "Error in generating Base64. AESKey flag is true\n"
                                + e.getMessage());
                tp.methodExit("Failed to generate fixed key.");
                return new Key();
            }

            result = new Key();
            result.setGenDate(GENDATE_FIXED);
            result.setKey64(resultkey);
            tp.methodExit(result);
            return result;
        }

        // Import keystore file to java keystore
        try {
            privateKey = new PrivateKey(
                    (String) context.getInitParameter("AESKeyStorePath"),
                    (String) context.getInitParameter("AESKeyStorePass"));
        } catch (Exception e) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "AESKeyStorePath is "
                            + (String) context
                                    .getInitParameter("AESKeyStorePath")
                            + "\nAESKeyStorePass is "
                            + (String) context
                                    .getInitParameter("AESKeyStorePass") + "\n"
                            + e.getMessage());
            tp.methodExit("Failed to read keystore parameters from context.");
            return result;
        }

        if (sha256 == null) {
            String masterKey = privateKey.getAesKey();
            sha256 = new Sha256KeyGenerator(masterKey);
        }

        // Is AESKeyGenDate available in keystore file?
        if (genDate != null && "1".equals(genDate)) {
            byte[] key = new byte[] {'1', '1', '1', '1', '1', '1', '1', '1',
                    '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
                    '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1'
                    };
            String resultkey = Base64.getEncoder().encodeToString(key);
            result = new Key();
            result.setKey64(resultkey);
            result.setGenDate("1");
        } else {
            try {
                result = generateAesKeyToday(genDate);
            } catch (Exception e) {
                LOGGER.logAlert(progName, functionName,
                        Logger.RES_EXCEP_GENERAL, e.getMessage());
                tp.methodExit("Failed to generate key for "
                        + genDate);
            }
        }
        tp.methodExit(result);
        return result;
    }

    /**
     * Clear AES KEY on the memory by Batch program.
     */
    public final void clearAESKey() {
        CreditAuthorizationResource.result = null;
    }

    /**
     * Parse and format date(milliseconds).
     *
     * @param sdfTo
     *            Parse date to formatted date
     * @param sdfFrom
     *            Format date to formatted date
     * @param dateInMs
     *            String of formatted date
     * @return the string
     */
    private String parseDate(final SimpleDateFormat sdfTo,
            final SimpleDateFormat sdfFrom, final String dateInMs) {
        String date = "";
        try {
            Date currDate = sdfFrom.parse(dateInMs);
            date = sdfTo.format(currDate.getTime());
        } catch (ParseException e) {
            LOGGER.logAlert(progName, className + " parseDate()",
                    Logger.RES_EXCEP_PARSE, e.getMessage());
        }
        return date;
    }

    /**
     * * get the aeskey using the Sha-256.
     *
     * @param now
     *            the today's date time.
     * @return Key with key, key64 and gendate
     */
    private Key generateAesKeyToday(final String now) {
        Key key = new Key();
        try {
            key.setKey(sha256.getKeyHex(now));
            key.setKey64(sha256.getKeyBase64(now));
            privateKey.setKey((String) context
                    .getInitParameter("AESKeyStoreGenDateAlias"), now);
            key.setGenDate(privateKey.getKey((String) context
                    .getInitParameter("AESKeyStoreGenDateAlias")));
        } catch (Exception e) {
            LOGGER.logAlert(progName, className + ".getAesKey ",
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
        }
        return key;
    }

    /**
     * The web resource method called by the Web Service to implement the
     * External Credit Authorization via PastelPort.
     *
     * @param strmsgjson
     *            The string data in json format to be send to pastelport
     * @param storeid
     *            The store identifier
     * @param posterminalid
     *            The POS terminal identifier
     * @param txdate
     *            The transaction datetime
     * @param txid
     *            The transaction identifier
     * @return The PastelPortResp in XML format
     */
    @Path("/requestextauthorizetopastelport")
    @POST
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final PastelPortResp authorizeExtCreditToPastelPort(
            @FormParam("strmsgjson") final String strmsgjson,
            @FormParam("companyid") final String companyid,
            @FormParam("storeid") final String storeid,
            @FormParam("posterminalid") final String posterminalid,
            @FormParam("txdate") final String txdate,
            @FormParam("txid") final String txid) {

        String functionName = className + "authorizeExtCreditToPasterPort";
        tp.methodEnter("authorizeExtCreditToPasterPort");
        tp.println("storeid", storeid);
        tp.println("posterminalid", posterminalid);
        tp.println("txdate", txdate);
        tp.println("txid", txid);
        PastelPortResp pastelPortResp = new PastelPortResp();

        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }

        try {
            pastelPortResp = this.authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);

            if (null != pastelPortResp.getApprovalcode()
                    && !pastelPortResp.getApprovalcode().isEmpty()) {
                CreditAuthorizationResp creditResponse = this
                        .getCreditAuthMsgLogger().logExtCA(pastelPortResp,
                                storeid, posterminalid, txdate, txid);

                if (creditResponse.getStatus().equals(
                        CreditAuthRespConstants.STATUS_ERROR_END)) {
                    pastelPortResp
                            .setNCRWSSResultCode(ResultBase
                                    .RESEXTCA_ERROR_ABEND);
                    pastelPortResp.setMessage(creditResponse.getMessage());
                } else {
                    pastelPortResp
                            .setNCRWSSResultCode(Integer
                                    .parseInt(CreditAuthRespConstants
                                            .STATUS_NORMAL_END));
                }
            } else {
                pastelPortResp
                        .setNCRWSSResultCode(ResultBase
                                .RESEXTCA_ERROR_PASTELPORTERR);
                pastelPortResp.setMessage(pastelPortResp.getErrormessage());
            }

        } catch (DaoException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to process ExtCreditAuthorization to PastelPort.\n"
                            + e.getMessage());
            pastelPortResp.setMessage(e.getMessage());
            pastelPortResp.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            // Output error transaction data to snap file.
            Snap.SnapInfo info = snap.write("CA JSON data",
                    strmsgjson);
            // Logging in IOW.
            LOGGER.logSnap(progName, functionName,
                    "Output error transaction data to snap file.", info);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to process ExtCreditAuthorization to PastelPort.\n"
                            + e.getMessage());
            pastelPortResp.setMessage(e.getMessage());
            pastelPortResp.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            // Output error transaction data to snap file.
            Snap.SnapInfo info = snap.write("CA JSON data",
                    strmsgjson);
            // Logging in IOW.
            LOGGER.logSnap(progName, functionName,
                    "Output error transaction data to snap file.", info);
        } finally {
            tp.methodExit(pastelPortResp);
        }

        return pastelPortResp;
    }

    /**
     * The web resource method called by the Web Service to implement of Credit
     * Authorization for manual transaction via PastelPort.
     *
     * @param strmsgjson
     *            The string data in json format to be send to pastelport
     * @param companyid
     *            The company identifier
     * @param storeid
     *            The store identifier
     * @param posterminalid
     *            The POS terminal identifier
     * @param txdate
     *            The transaction datetime
     * @param txid
     *            The transaction identifier
     * @param operatorcode
     *            the operatorcode
     * @return The PastelPortResp in XML format
     */
    @Path("/requestmanualauthorizetopastelport")
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces({ MediaType.APPLICATION_XML + ";charset=UTF-8" })
    public final PastelPortResp manualAuthorizeCreditToPastelPort(
            @FormParam("strmsgjson") final String strmsgjson,
            @FormParam("companyid") final String companyid,
            @FormParam("storeid") final String storeid,
            @FormParam("posterminalid") final String posterminalid,
            @FormParam("txdate") final String txdate,
            @FormParam("txid") final String txid,
            @FormParam("operatorno") final String operatorcode) {

        String functionName = className + "manualAuthorizeCredit";
        tp.methodEnter("manualAuthorizeCreditToPastelPort");
        tp.println("companyid", companyid);
        tp.println("storeid", storeid);
        tp.println("posterminalid", posterminalid);
        tp.println("txdate", txdate);
        tp.println("txid", txid);
        tp.println("operatorcode", operatorcode);
        if (snap == null) {
            snap = (SnapLogger) SnapLogger.getInstance();
        }
        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        IExtCARequestDAO extCAReqDAO;
        // type = 2, means Manual Bill entry.
        int type = 2;
        String defaultStatus = "1";
        PastelPortResp pastelPortResp = new PastelPortResp();
        try {
            extCAReqDAO = daoFactory.getExtCAReqDAO();
            pastelPortResp = this.authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);

            if (null != pastelPortResp.getApprovalcode()
                    && !pastelPortResp.getApprovalcode().isEmpty()) {
                
                String status = (extCAReqDAO.saveExtCARequest(
                        pastelPortResp, storeid, posterminalid, txdate,
                        txid, type, defaultStatus, operatorcode)
                        >= SQLResultsConstants.ONE_ROW_AFFECTED)
                        ? PosLogRespConstants.NORMAL_END
                                : PosLogRespConstants.ERROR_END_1;
                
                PosLogResp posLogResponse = new PosLogResp();
                posLogResponse.setStatus(status);

                if (posLogResponse.getStatus().equals(
                        PosLogRespConstants.NORMAL_END)) {
                    pastelPortResp.setNCRWSSResultCode(Integer
                            .parseInt(PosLogRespConstants.NORMAL_END));
                } else {
                    pastelPortResp.setNCRWSSResultCode(Integer
                            .parseInt(PosLogRespConstants.ERROR_END_1));
                    pastelPortResp.setMessage(posLogResponse.getMessage());
                }
            } else {
                pastelPortResp
                        .setNCRWSSResultCode(ResultBase
                                .RESEXTCA_ERROR_PASTELPORTERR);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_JAXB,
                    "Failed to process manual invoice credit "
                            + "authorization request. Invalid date format.\n"
                            + ex.getMessage());
            pastelPortResp.setMessage(ex.getMessage());
            pastelPortResp.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            // Output error transaction data to snap file.
            Snap.SnapInfo info = snap.write("CA JSON data",
                    strmsgjson);
            // Logging in IOW.
            LOGGER.logSnap(progName, functionName,
                    "Output error transaction data to snap file.", info);
        } finally {
            tp.methodExit(pastelPortResp);
        }

        return pastelPortResp;
    }

    /**
     * Sets the CreditAuthAgencyResource webservice interface.
     *
     * @param caAgencyResource
     *            the new CaAgency resource
     */
    public final void setCaAgencyRes(
            final CreditAuthAgencyResource caAgencyResource) {
        this.caAgencyRes = caAgencyResource;
    }

    /**
     * Gets the CreditAuthAgencyResource webservice interface.
     *
     * @return the CaAgency resource
     */

    public final CreditAuthAgencyResource getCaAgencyRes() {
        return caAgencyRes;
    }

    /**
     * Gets CreditAuthMsgLogger class interface.
     *
     * @return the CreditAuthMsgLogger
     */

    public final CreditAuthMsgLogger getCreditAuthMsgLogger() {
        return creditAuthMsgLogger;
    }

    /**
     * Sets CreditAuthMsgLogger class interface.
     *
     * @param creditAuthMsgLoggr
     *            the new CreditAuthMsgLogger
     */
    public final void setCreditAuthMsgLogger(
            final CreditAuthMsgLogger creditAuthMsgLoggr) {
        this.creditAuthMsgLogger = creditAuthMsgLoggr;
    }
}
