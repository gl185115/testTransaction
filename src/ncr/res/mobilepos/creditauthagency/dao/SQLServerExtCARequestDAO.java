/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerExtCARequestDAO
 *
 * Implementation for IExtCARequestDAO
 *
 * jd185128
 */

package ncr.res.mobilepos.creditauthagency.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.constant.CreditAuthRespConstants;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.constants.PosLogRespConstants;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for External
 * CA Request Interface(IExtCARequestDAO).
 *
 * @see IExtCARequestDAO
 *
 */
public class SQLServerExtCARequestDAO
extends AbstractDao implements IExtCARequestDAO {
    /** Success CA inquiry result.*/
    private static String constSuccessCaInquired = "1";
    /** A private member variable used for database manipulation. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /** Class name. */
    private String className = "SQLServerExtCARequestDAO";
    /** Prog name. */
    private String progName = "ExtCADAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Constructor.
     * @throws DaoException exception
     */
    public SQLServerExtCARequestDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    @Override
    public final List<CreditAuthorizationResp> getExtCARequest(
            final String storeid,
            final String termid,
            final String txdate) throws DaoException {

        String functionName = className + "getExtCARequest";
        tp.methodEnter("getExtCARequest");
        tp.println("StoreID", storeid).println("TerminalID", termid).
            println("TransactionDate", txdate);

        Connection connection = null;
        PreparedStatement getExtCAReq = null;
        ResultSet rs = null;
        List<CreditAuthorizationResp> caRequests =
            new ArrayList<CreditAuthorizationResp>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            getExtCAReq = connection.prepareStatement(
                    sqlStatement.getProperty("get-extCAReq"));
            getExtCAReq.setString(SQLStatement.PARAM1, storeid);
            getExtCAReq.setString(SQLStatement.PARAM2, termid);
            getExtCAReq.setString(SQLStatement.PARAM3, txdate);

            rs = getExtCAReq.executeQuery();
            CreditAuthorizationResp caRequest = null;

            while (rs.next()) {
                caRequest = new CreditAuthorizationResp();
                caRequest.setStatus(rs.getString(
                        rs.findColumn("Status")));
                //Is the CA Request Already on process?
                //If yes, the searching of item should not be successful.
                if (caRequest.getStatus().equals(
                        CreditAuthRespConstants.STATUS_ERROR_END)) {
                    tp.println("Searching of CA request was not successful.");
                    break; // exit while loop
                }
                caRequest.setAmount(rs.getString(
                        rs.findColumn("Amount")).trim());
                caRequest.setStoreid(storeid);
                caRequest.setTerminalid(termid);
                caRequest.setTxid(rs.getString(
                        rs.findColumn("SequenceNumber")).trim());
                String slipnumber = rs.getString(
                        rs.findColumn("SlipNumber"));
                if (!StringUtility.isNullOrEmpty(slipnumber)) {
                    slipnumber = slipnumber.trim();
                }
                caRequest.setTillid(slipnumber);
                caRequest.setTxdatetime(rs.getString(rs.findColumn("BusinessDayDate")));
                caRequests.add(caRequest);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "SQLException - Failed to"
                    + "search for a Credit Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequest "
                    + "- Failed to search for a Credit Authorization request",
                      ex);
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQLException - Failed to search for a"
                    + " Credit Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequest "
                    + "- Failed to search for a Credit Authorization request",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "SQLException - Failed to search for a"
                    + " Credit Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequest "
                    + "- Failed to search for a Credit Authorization request",
                    ex);
        } finally {
            closeConnectionObjects(connection, getExtCAReq, rs);

            tp.methodExit(caRequests.size());
        }
        return caRequests;
    }

    @Override
    public final CreditAuthorizationResp getExtCARequestStatus(
            final String storeid,
            final String termid,
            final String txid,
            final String txdate) throws DaoException {

        String functionName = className + "getExtCARequestStatus";
        tp.methodEnter("getExtCARequestStatus");
        tp.println("StoreID", storeid).println("TerminalID", termid).
            println("TransactionID", txid).println("Date", txdate);

        Connection connection = null;
        PreparedStatement getExtCAReq = null;
        ResultSet rs = null;
        CreditAuthorizationResp caRequest =  new CreditAuthorizationResp();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            getExtCAReq = connection.prepareStatement(
                    sqlStatement.getProperty("get-extCAReq-status"));
            getExtCAReq.setString(SQLStatement.PARAM1, storeid);
            getExtCAReq.setString(SQLStatement.PARAM2, termid);
            getExtCAReq.setString(SQLStatement.PARAM3, txid);
            getExtCAReq.setString(SQLStatement.PARAM4, txdate);
            rs = getExtCAReq.executeQuery();

            if (rs.next()) {

                caRequest.setStatus(rs.getString(rs.findColumn("Status")));

                //Is the CA Request was not finished Process?
                //If yes, return the status only
                if (!caRequest.getStatus().equals(constSuccessCaInquired)) {
                    tp.println("CA request process was not finished.");
                    return caRequest;
                }

                caRequest.setAmount(rs.getString(rs.findColumn("Amount")));
                caRequest.setStoreid(rs.getString(rs.findColumn("RetailStoreId")));
                caRequest.setTerminalid(
                        rs.getString(rs.findColumn("WorkstationId")));
                caRequest.setTxid(rs.getString(rs.findColumn("SequenceNumber")));
                caRequest.setTillid(rs.getString(rs.findColumn("SlipNumber")));
                caRequest.setTxdatetime(rs.getString(rs.findColumn("BusinessDayDate")));
                caRequest.setPan(rs.getString(rs.findColumn("Pan")));
                caRequest.setExpdate(rs.getString(rs.findColumn("ExpDate")));
                caRequest.setPaymentmethod(
                        rs.getString(rs.findColumn("PaymentMethod")));
                caRequest.setCreditcompanyname(
                        rs.getString(rs.findColumn("CreditCompanyName")));
                caRequest.setCreditcompanycode(
                        rs.getString(rs.findColumn("CreditCompanyCode")));
                caRequest.setCreditstatus(
                        rs.getString(rs.findColumn("CreditStatus")));
                caRequest.setApprovalcode(
                        rs.getString(rs.findColumn("ApprovalCode")));
                caRequest.setTracenum(
                        rs.getString(rs.findColumn("CafisTraceNum")));
                caRequest.setErrorcode("    ");
                caRequest.setErrormessage("  ");
            } else {
                caRequest.setStatus("2");
                caRequest.setErrormessage(
                        "No transaction Found for StoreID:" + storeid
                        + "Terminal ID: " +  termid
                        + "Transaction ID: " + txid
                        + "TxDate: " + txdate);
                tp.println("Transaction not found.");
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "SQLException - Failed to search"
                    + " for a Credit Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequestStatus "
                    + "- Failed to search for a Credit Authorization request",
                    ex);
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQLException - Failed to search for a Credit "
                    + "Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequestStatus "
                    + "- Failed to search for a Credit Authorization request",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "SQLException - Failed to search for a "
                    + "Credit Authorization request.\n"
                    + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerExtCARequestDAO.getExtCARequestStatus "
                    + "- Failed to search for a Credit Authorization request",
                    ex);
        } finally {
            closeConnectionObjects(connection, getExtCAReq, rs);

            tp.methodExit(caRequest);
        }
        return caRequest;
    }

    @Override
    public final int saveExtCARequest(
                     final PosLog posLog) throws DaoException {

        String functionName = className + "saveExtCARequest";
        tp.methodEnter("saveExtCARequest");

        int result = 0;
        Connection connection = null;
        PreparedStatement saveExtCAReq = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveExtCAReq =
                connection.prepareStatement(
                        sqlStatement.getProperty("save-extCAReq"));
            saveExtCAReq.setString(SQLStatement.PARAM1,
                    posLog.getTransaction().getRetailStoreID());
            saveExtCAReq.setString(SQLStatement.PARAM2,
                    posLog.getTransaction().getWorkStationID().getValue());
            saveExtCAReq.setString(SQLStatement.PARAM3,
                    posLog.getTransaction().getBusinessDayDate());
            saveExtCAReq.setString(SQLStatement.PARAM4,
                    posLog.getTransaction().getSequenceNo());
            saveExtCAReq.setString(SQLStatement.PARAM5,
                    PosLogRespConstants.NORMAL_END);
            saveExtCAReq.setString(SQLStatement.PARAM6,
                    posLog.getTransaction().getTillID());
            saveExtCAReq.setBigDecimal(SQLStatement.PARAM7,
                    new BigDecimal(posLog.getTransaction()
                            .getRetailTransaction()
                    .getLineItems().get(0).getTender().getAmount()));
            saveExtCAReq.setString(SQLStatement.PARAM8,
                    posLog.getTransaction().getOperatorID().getValue());

            result = saveExtCAReq.executeUpdate();

            connection.commit();

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to save external CA Request"
                    + " in the database.\n" + sqlStmtEx.getMessage());

            rollBack(connection, "SQLServerExtCARequestDAO: @"
                    +
                    "saveExtCARequest()", sqlStmtEx);
            throw new DaoException(sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to save external CA Request in the database.\n"
                    + sqlEx.getMessage());

            rollBack(connection,
                    "SQLServerExtCARequestDAO: @saveExtCARequest()", sqlEx);
            throw new DaoException(sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to save external CA Request in the database.\n"
                    + ex.getMessage());
            rollBack(connection,
                    "SQLServerExtCARequestDAO: @saveExtCARequest()", ex);
            throw  new DaoException(ex);
        } finally {
            closeConnectionObjects(connection, saveExtCAReq);

            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Saves the CARequest from the external source with pastelport response.
     *
     * @param pastelPortResp    The PastelPortResp object
     * @param storeId            The store identifier
     * @param posterminalId        The POS terminal identifier
     * @param txDate            The transaction Date
     * @param txId                The transaction number
     * @param type              The type of credit authorization.
     * @param status            The status of CARequest.
     * @param operatorCode        The operator number
     * @return                    the number of rows affected in the database.
     * @throws DaoException         The exception thrown when the process fail.
     */
    @Override
    public final int saveExtCARequest(
            final PastelPortResp pastelPortResp,
            final String storeId,
            final String posterminalId,
            final String txDate,
            final String txId,
            final int type,
            final String status,
            final String operatorCode) throws DaoException {

        String functionName = className + "saveExtCARequest";

        tp.methodEnter("saveExtCARequest");
        tp.println("StoreID", storeId).println("POSTerminalID", posterminalId).
            println("TransactionDate", txDate).println("TransactionID", txId).
            println("OperatorCode", operatorCode);

        String thisTxDate = txDate;
        int result = 0;
        Connection connection = null;
        PreparedStatement saveExtCAReq = null;

        try {
            SimpleDateFormat dateformatter =
                new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            thisTxDate = sdfDate.format(
                    dateformatter.parse(thisTxDate).getTime());

            connection = dbManager.getConnection();
            connection.setAutoCommit(false);

            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveExtCAReq = connection.prepareStatement(
                    sqlStatement.getProperty("save-extCAReq-pastelport"));
            saveExtCAReq.setString(SQLStatement.PARAM1, storeId);
            saveExtCAReq.setString(SQLStatement.PARAM2, posterminalId);
            saveExtCAReq.setString(SQLStatement.PARAM3, thisTxDate);
            saveExtCAReq.setString(SQLStatement.PARAM4, txId);
            saveExtCAReq.setString(SQLStatement.PARAM5, status);
            saveExtCAReq.setString(SQLStatement.PARAM6, ""); //Set by fixation
            saveExtCAReq.setBigDecimal(SQLStatement.PARAM7,
                    new BigDecimal(pastelPortResp.getAmount()));
            saveExtCAReq.setString(SQLStatement.PARAM8, operatorCode);
            saveExtCAReq.setString(SQLStatement.PARAM9,
                    pastelPortResp.getCreditcompanycode());
            saveExtCAReq.setString(SQLStatement.PARAM10,
                    pastelPortResp.getCreditcompanycode());
            saveExtCAReq.setString(SQLStatement.PARAM11,
                    pastelPortResp.getCreditcompanyname());
            saveExtCAReq.setString(SQLStatement.PARAM12,
                    pastelPortResp.getPaymentmethod());
            saveExtCAReq.setString(SQLStatement.PARAM13,
                    pastelPortResp.getApprovalcode());
            saveExtCAReq.setString(SQLStatement.PARAM14,
                    pastelPortResp.getCreditstatus());
            saveExtCAReq.setString(SQLStatement.PARAM15,
                    pastelPortResp.getTracenum());
            saveExtCAReq.setString(SQLStatement.PARAM16,
                    pastelPortResp.getPan4());
            saveExtCAReq.setString(SQLStatement.PARAM17,
                    pastelPortResp.getExpdate());
            saveExtCAReq.setInt(SQLStatement.PARAM18, type);

            result += saveExtCAReq.executeUpdate();

            connection.commit();

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to save external"
                    + " CA Request with pastelport response"
                    + " in the database.\n" + sqlStmtEx.getMessage());

            rollBack(connection, "SQLServerExtCARequestDAO: @"
                    + "saveExtCARequest()", sqlStmtEx);
            throw new DaoException(sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to save external CA Request with"
                    + " pastelport response in the database.\n"
                    + sqlEx.getMessage());
            rollBack(connection, "SQLServer ExtCARequestDAO:"
                    + " @saveExtCARequest()", sqlEx);

        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to save external CA Request with pastelport"
                    + " response in the database.\n" + ex.getMessage());
            rollBack(connection, "SQLServerExtCARequestDAO:"
                    + " @saveExtCARequest()", ex);

        } finally {
            closeConnectionObjects(connection, saveExtCAReq);

            tp.methodExit(result);
        }

        return result;
    }
}
