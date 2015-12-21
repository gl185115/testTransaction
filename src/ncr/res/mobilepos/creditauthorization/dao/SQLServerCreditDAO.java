/*

 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCreditDAO
 *
 * DAO which handles the operations in the table specific for CreditLog
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Credit Authorization.
 *
 * @see ICreditDAO
 */
public class SQLServerCreditDAO extends AbstractDao implements ICreditDAO {
    /**
     * CA Lock status.
     */
    public static final int STATUS_LOCK = 9;
    /**
     * CA Release status.
     */
    public static final int STATUS_RELEASE = 0;
    /** The Constant ABNORMAL_CA_PROCESSS. */
    private static final int ABNORMAL_CA_PROCESSS = -1;
    /**
     * A private member variable that represents the Database Manager for the
     * class.
     */
    private DBManager dbManager;
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * The Class Constructor.
     *
     * @throws DaoException
     *             thrown when database error occurs
     */
    public SQLServerCreditDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager.
     *
     * @return Return an database manager instance
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * @deprecated
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public final int saveCreditAuthorization(final CreditAuthorization credit,
            final String creditAuthorization) throws DaoException {

        // Start log.
        tp.methodEnter("saveCreditAuthorization");
        int result = 0;
        Connection connection = null;
        PreparedStatement saveCreditPrepStmnt = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveCreditPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-credit"));

            saveCreditPrepStmnt.setString(SQLStatement.PARAM1,
                    credit.getCorpid());
            saveCreditPrepStmnt.setString(SQLStatement.PARAM2,
                    credit.getStoreid());
            saveCreditPrepStmnt.setString(SQLStatement.PARAM3,
                    credit.getTerminalid());
            saveCreditPrepStmnt.setString(SQLStatement.PARAM4,
                    credit.getTxid());
            saveCreditPrepStmnt.setString(SQLStatement.PARAM5,
                    creditAuthorization);

            result = saveCreditPrepStmnt.executeUpdate();

            connection.commit();

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO.saveCreditAuthorization()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerCreditDAO:saveCreditAuthorization",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO.saveCreditAuthorization()",
                    Logger.RES_EXCEP_SQL, "SQL Exception Error occured. \n"
                            + sqlEx.getMessage());
            rollBack(connection, "@SQLServerCreditDAO:saveCreditAuthorization",
                    sqlEx);

        } finally {
            closeConnectionObjects(connection, saveCreditPrepStmnt);

            tp.methodExit(result);
        }
        return result;
    }

    /**
     *@deprecated 
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public final int saveExtCreditAuthorization(
            final CreditAuthorization credit, final String creditAuthorization)
            throws DaoException {
        // Start log.
        tp.methodEnter("saveExtCreditAuthorization");
        int result = 0;
        Connection connection = null;
        PreparedStatement saveCreditPrepStmnt = null;
        PreparedStatement getCaReqStatusPrepStmt = null;
        PreparedStatement setCaReqStatusPrepStmt = null;
        PreparedStatement setCaReqPrepStmt = null;
        PreparedStatement getCreditCompanyPrepStmt = null;
        SQLStatement sqlStatement = null;
        ResultSet rs = null;
        ResultSet resSet = null;

        String storeid = credit.getStoreid();
        String termid = credit.getTerminalid();
        String txid = credit.getTxid();
        String txdate = credit.getTxdatetime().substring(0, 8); // yyyyMMddHHmmss to yyyyMMdd
        try {
            tp.println("storeid", storeid).println("termid", termid)
            .println("txid", txid).println("txdate", txdate);

            // 1st try catch statement gets the status,
            // checks it and updates to "In Progress"
            connection = dbManager.getConnection();
            sqlStatement = SQLStatement.getInstance();

            // Get the Credit information
            getCreditCompanyPrepStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-credCompany"));
            getCreditCompanyPrepStmt.setString(1, credit.getPan());
            getCreditCompanyPrepStmt.setString(2, credit.getPan());

            resSet = getCreditCompanyPrepStmt.executeQuery();

            if (!resSet.next()) {
                LOGGER.logAlert("CredAuth",
                        "SQLServerCreditDAO.saveExtCreditAuthorization().",
                        Logger.RES_EXCEP_GENERAL,
                        "Credit Company does not exist");
                result = ABNORMAL_CA_PROCESSS;
                tp.println("Credit company does not exist.");
                throw new SQLException("Credit Company does not exist");
            }

            saveCreditPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-credit"));
            saveCreditPrepStmnt.setString(SQLStatement.PARAM1,
                    credit.getCorpid());
            saveCreditPrepStmnt.setString(SQLStatement.PARAM2, storeid);
            saveCreditPrepStmnt.setString(SQLStatement.PARAM3, termid);
            saveCreditPrepStmnt.setString(SQLStatement.PARAM4, txid);
            saveCreditPrepStmnt.setString(SQLStatement.PARAM5,
                    creditAuthorization);

            result += saveCreditPrepStmnt.executeUpdate();

            setCaReqPrepStmt = connection.prepareStatement(sqlStatement
                    .getProperty("set-extCAReq"));
            setCaReqPrepStmt.setString(SQLStatement.PARAM1, "1");
            setCaReqPrepStmt.setString(SQLStatement.PARAM2,
                    resSet.getString("companycode"));
            setCaReqPrepStmt.setString(SQLStatement.PARAM3,
                    resSet.getString("companyname"));
            setCaReqPrepStmt.setString(SQLStatement.PARAM4, "10");
            setCaReqPrepStmt.setString(SQLStatement.PARAM5, txid);
            setCaReqPrepStmt.setString(SQLStatement.PARAM6, "00");
            setCaReqPrepStmt.setString(SQLStatement.PARAM7, txid);
            setCaReqPrepStmt.setString(SQLStatement.PARAM8, credit.getPan());
            setCaReqPrepStmt.setString(SQLStatement.PARAM9,
                    credit.getExpirationdate());
            setCaReqPrepStmt.setString(SQLStatement.PARAM10, storeid);
            setCaReqPrepStmt.setString(SQLStatement.PARAM11, termid);
            setCaReqPrepStmt.setString(SQLStatement.PARAM12, txdate);
            setCaReqPrepStmt.setString(SQLStatement.PARAM13, txid);

            int nExtSuccess = setCaReqPrepStmt.executeUpdate();

            if (nExtSuccess < 0) {
                tp.println("TXL_EXTERNAL_CA_REQ not"
                        + " updated properly.");
                throw new Exception("TXL_EXTERNAL_CA_REQ not"
                        + " updated properly.");
            }

            result += nExtSuccess;
            connection.commit();

        } catch (SQLStatementException sqlStmtEx) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                            "SQL Statement Error occured. \n" + sqlStmtEx.getMessage(), sqlStmtEx);
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    Logger.RES_EXCEP_SQL, "SQL Exception Error occured. \n"
                            + sqlEx.getMessage(), sqlEx);
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization", sqlEx);
        } catch (Exception e) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    Logger.RES_EXCEP_GENERAL, "SQL Exception Error occured. \n"
                            + e.getMessage(), e);
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization", e);
        } finally {
            boolean hasErrorOccur = false;
            try {
                // Is the CA Request Failed?
                // If yes, set the CA requestto Abnormal
                if (result == ABNORMAL_CA_PROCESSS) {
                    setCaReqStatusPrepStmt = connection
                            .prepareStatement(SQLStatement.getInstance()
                                    .getProperty("set-extCAReqStatus"));
                    setCaReqStatusPrepStmt.setString(SQLStatement.PARAM1, "2");
                    setCaReqStatusPrepStmt.setString(SQLStatement.PARAM2,
                            storeid);
                    setCaReqStatusPrepStmt.setString(SQLStatement.PARAM3,
                            termid);
                    setCaReqStatusPrepStmt.setString(SQLStatement.PARAM4,
                            txdate);
                    setCaReqStatusPrepStmt.setString(SQLStatement.PARAM5,
                            txid);

                    setCaReqStatusPrepStmt.executeUpdate();
                    connection.commit();
                    tp.println("Failed to process CA. Status is set "
                            + "to ABNORMAL.");
                }
            } catch (SQLException ex) {
                LOGGER.logAlert("CredAuth",
                        Logger.RES_EXCEP_SQL,
                        "Failed to set TXL_EXTERNAL_CA_REQ status to 2 "
                                + "(ABNORMAL) " + ex.getMessage(), ex);
                hasErrorOccur = true;
            } catch (Exception ex) {
                LOGGER.logAlert("CredAuth",
                        Logger.RES_EXCEP_GENERAL,
                        "Failed to set TXL_EXTERNAL_CA_REQ status to 2 "
                                + "(ABNORMAL) " + ex.getMessage(), ex);
                hasErrorOccur = true;
            } finally {            	
            	closeConnectionObjects(null, setCaReqStatusPrepStmt, null);
            	closeConnectionObjects(null, getCaReqStatusPrepStmt, null);
            	closeConnectionObjects(null, setCaReqPrepStmt, null);
            	closeConnectionObjects(null, getCreditCompanyPrepStmt, resSet);
            	closeConnectionObjects(connection, saveCreditPrepStmnt, rs);
            }
            
            tp.methodExit(result);
        }
        return result;

    }

    /**
     * @deprecated
     * Gets credit number from TXL_CREDITLOG.
     *
     * @param txid
     *            the txid
     * @return credit number
     * @throws DaoException
     *             thrown when database error occurs
     */
    @Deprecated
    public final String getCreditPan(final String txid) throws DaoException {
        tp.methodEnter("getCreditPan");
        // credit number
        String creditPan = "";
        Connection connection = null;
        PreparedStatement getCreditPanPrepStmnt = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getCreditPanPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-credit-byid"));
            getCreditPanPrepStmnt.setString(SQLStatement.PARAM1, txid);

            rs = getCreditPanPrepStmnt.executeQuery();
            if (rs.next()) {
                creditPan = rs.getString("txPan");
                tp.println("PAN retrieval success.");
            } else {
                tp.println("PAN was not found.");
            }
        } catch (SQLException sqlStmtEx) {
            LOGGER.logAlert("CredAuth", "SQLServerCreditDAO.getCreditPan().",
                    Logger.RES_EXCEP_SQL, "SQL Statement Error occured. \n"
                            + sqlStmtEx.getMessage());
        } catch (SQLStatementException sqlEx) {
            LOGGER.logAlert("CredAuth", "SQLServerCreditDAO.getCreditPan().",
                    Logger.RES_EXCEP_SQLSTATEMENT, "SQL Error occured. \n"
                            + sqlEx.getMessage());
        } finally {
            closeConnectionObjects(connection, getCreditPanPrepStmnt, rs);
            
            tp.methodExit();
        }
        return creditPan;
    }

    /**
     * Lock Or Release A Credit Authorization Request from getting Authorize.
     *
     * @param credit
     *            The Credit under Lock/Release
     * @param isToLock
     *            The Flag that tells if Lock(true) or Release(false)
     * @return 0 when released and 9 when locked
     * @throws DaoException
     *             The Exception returned when the method failed.
     */
    public final int lockOrReleaseCARequest(final CreditAuthorization credit,
            final boolean isToLock) throws DaoException {
        // Start log.
        String isToLockStr = "";
        if (isToLock) {
            isToLockStr = "Lock";
        } else {
            isToLockStr = "Release";
        }
        tp.methodEnter("lockOrReleaseCARequest")
            .println("isToLock", isToLockStr);
        Connection connection = null;
        PreparedStatement setCaReqStatusPrepStmt = null;
        int result = -1;
        String txdate = "";
        ResultSet rs = null;
        try {
            String[] dateFormats = {"yyyyMMddHHmmss", "yyyy-MM-dd"};
            Date date = DateFormatUtility.parse(credit.getTxdatetime(),
                    dateFormats);
            if (date == null) {
                tp.println("The txdate is invalid.");
                return result;
            }
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            // Set the TXL_EXTERNAL_CA_REQ [status] to busy "9"
            setCaReqStatusPrepStmt = connection.prepareStatement(sqlStatement
                    .getProperty("set-extCAReqStatus-updlock"));
            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM1,
                    credit.getStoreid());
            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM2,
                    credit.getTerminalid());
            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM3, txdate);
            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM4,
                    "" + credit.getTxid());

            String lock = "9";
            if (isToLock) {
                lock = "0";
            }

            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM5, lock);

            lock = "0";
            if (isToLock) {
                lock = "9";
            }

            setCaReqStatusPrepStmt.setString(SQLStatement.PARAM6,
                    lock);

            rs = setCaReqStatusPrepStmt.executeQuery();
            connection.commit();

            result = STATUS_RELEASE;
            if (isToLock) {
                result = STATUS_LOCK;
            }

        } catch (SQLException ex) {
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO.lockOrReleaseCARequest().",
                    Logger.RES_EXCEP_SQL,
                    "SQL Error occured. \n" + ex.getMessage());
            rollBack(connection, "@SQLServerCreditDAO.lockOrReleaseCARequest",
                    ex);
        } catch (SQLStatementException ex) {
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO.lockOrReleaseCARequest().",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + ex.getMessage());
            rollBack(connection, "@SQLServerCreditDAO.lockOrReleaseCARequest",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO.lockOrReleaseCARequest().",
                    Logger.RES_EXCEP_GENERAL,
                    "General Error occured. \n" + ex.getMessage());
            rollBack(connection, "@SQLServerCreditDAO.lockOrReleaseCARequest",
                    ex);
        } finally {
            closeConnectionObjects(connection, setCaReqStatusPrepStmt, rs);

            tp.methodExit(result);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    public final int saveExtCreditAuthorization(
            final PastelPortResp pastelPortResp, final String storeId,
            final String posterminalId, final String txDate, final String txId)
            throws DaoException {

        tp.methodEnter("saveExtCreditAuthorization")
        .println("storeId", storeId).println("posterminalId", posterminalId)
        .println("txDate", txDate).println("txId", txId);
        int result = 0;
        Connection connection = null;
        PreparedStatement updateCaReqStmt = null;
        PreparedStatement updateCaReqStatusStmt = null;
        SQLStatement sqlStatement = null;

        try {
            SimpleDateFormat dateformatter = new SimpleDateFormat(
                    "yyyyMMddHHmmss");
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            String transactionDate = txDate;
            transactionDate = sdfDate.format(dateformatter
                    .parse(transactionDate).getTime());
            tp.println("transactionDate", transactionDate);

            connection = dbManager.getConnection();
            sqlStatement = SQLStatement.getInstance();

            updateCaReqStmt = connection.prepareStatement(sqlStatement
                    .getProperty("set-extCAReq"));
            updateCaReqStmt.setString(SQLStatement.PARAM1, "1");
            updateCaReqStmt.setString(SQLStatement.PARAM2,
                    pastelPortResp.getCreditcompanycode());
            updateCaReqStmt.setString(SQLStatement.PARAM3,
                    pastelPortResp.getCreditcompanyname());
            updateCaReqStmt.setString(SQLStatement.PARAM4,
                    pastelPortResp.getPaymentmethod());
            updateCaReqStmt.setString(SQLStatement.PARAM5,
                    pastelPortResp.getApprovalcode());
            updateCaReqStmt.setString(SQLStatement.PARAM6,
                    pastelPortResp.getCreditstatus());
            updateCaReqStmt.setString(SQLStatement.PARAM7,
                    pastelPortResp.getTracenum());
            updateCaReqStmt.setString(SQLStatement.PARAM8,
                    pastelPortResp.getPan4());
            updateCaReqStmt.setString(SQLStatement.PARAM9,
                    pastelPortResp.getExpdate());
            updateCaReqStmt.setString(SQLStatement.PARAM10, storeId);
            updateCaReqStmt.setString(SQLStatement.PARAM11, posterminalId);
            updateCaReqStmt.setString(SQLStatement.PARAM12, transactionDate);
            updateCaReqStmt.setString(SQLStatement.PARAM13, txId);
            
            int nExtSuccess = updateCaReqStmt.executeUpdate();

            result += nExtSuccess;
            connection.commit();

        } catch (SQLStatementException sqlStmtEx) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO:saveExtCreditAuthorization",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO:saveExtCreditAuthorization",
                    Logger.RES_EXCEP_SQL, "SQL Exception Error occured. \n"
                            + sqlEx.getMessage());
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization", sqlEx);
        } catch (Exception e) {
            result = ABNORMAL_CA_PROCESSS;
            LOGGER.logAlert("CredAuth",
                    "SQLServerCreditDAO:saveExtCreditAuthorization",
                    Logger.RES_EXCEP_GENERAL, "SQL Exception Error occured. \n"
                            + e.getMessage());
            rollBack(connection,
                    "@SQLServerCreditDAO:saveExtCreditAuthorization", e);
        } finally {
            boolean hasErrorOccur = false;
            hasErrorOccur = closeObject(updateCaReqStmt);
            tp.println("CredAuth",
                    "SQLServerCreditDAO.saveExtCreditAuthorization()\n" +
                    "(1)hasErrorOccur: " + hasErrorOccur);

            try {
                // Is the CA Request Failed?
                // If yes, set the CA request to Abnormal
                if (result == ABNORMAL_CA_PROCESSS) {
                    updateCaReqStatusStmt = connection
                            .prepareStatement(SQLStatement.getInstance()
                                    .getProperty("set-extCAReqStatus"));
                    updateCaReqStatusStmt.setString(SQLStatement.PARAM1, "2");
                    updateCaReqStatusStmt.setString(SQLStatement.PARAM2,
                            storeId);
                    updateCaReqStatusStmt.setString(SQLStatement.PARAM3,
                            posterminalId);
                    updateCaReqStatusStmt.setString(SQLStatement.PARAM4,
                            txDate);
                    updateCaReqStatusStmt.setString(SQLStatement.PARAM5, txId);

                    updateCaReqStatusStmt.executeUpdate();
                    connection.commit();
                    tp.println("Failed to process CA. Status is set "
                            + "to ABNORMAL.");
                }
            } catch (SQLException ex) {
                LOGGER.logAlert("CredAuth",
                        "SQLServerCreditDAO:saveExtCreditAuthorization",
                        Logger.RES_EXCEP_SQL,
                        "Failed to set TXL_EXTERNAL_CA_REQ status to"
                                + " 2 (ABNORMAL) " + ex.getMessage());
                hasErrorOccur = true;
            } catch (Exception ex) {
                LOGGER.logAlert("CredAuth",
                        "SQLServerCreditDAO:saveExtCreditAuthorization",
                        Logger.RES_EXCEP_SQL,
                        "Failed to set TXL_EXTERNAL_CA_REQ status to"
                                + " 2 (ABNORMAL) " + ex.getMessage());
                hasErrorOccur = true;
            } finally {
                closeConnectionObjects(connection, updateCaReqStatusStmt);
            }

            tp.methodExit(result);
        }
        return result;
    }
}
