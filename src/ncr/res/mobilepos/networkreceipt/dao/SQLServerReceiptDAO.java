/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.networkreceipt.model.ReceiptCredit;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * A Data Access Object implementation for Receipt Generation.
 */
public class SQLServerReceiptDAO extends AbstractDao implements IReceiptDAO {
    /**
     * class instance of the DBManager.
     */
    private DBManager dbManager;
    /**
     * class instance of the Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * class instance of the debug log printer.
     */
    private Trace.Printer tp;
    /**
     * constant. the company id of guest accounts.
     */
    private static final String GUEST_CORP_ID = "9999";
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "RecptDAO";

    /**
     * constructor.
     *
     * @throws DaoException - thrown when any exception occurs
     */
    public SQLServerReceiptDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {

        return dbManager;
    }

    @Override
    public final NetPrinterInfo getPrinterInfo(final String storeid,
            final String termid) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid:" + storeid)
                .println("termid:" + termid);

        NetPrinterInfo netPrinterInfo = new NetPrinterInfo();
        Connection connection = null;
        PreparedStatement getnetPrinterInfoStmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getnetPrinterInfoStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-netprinterinfo"));
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM1, storeid);
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM2, termid);

            rs = getnetPrinterInfoStmt.executeQuery();
            if (rs.next()) {
                netPrinterInfo.setUrl(rs.getString("IpAddress"));
                netPrinterInfo.setPortTCP(rs.getInt("PortNumTcp"));
                netPrinterInfo.setPortUDP(rs.getInt("PortNumUdp"));
            } else {
                tp.println("Printer Info is not found for Store=" + storeid
                        + ";Terminal=" + termid);
                netPrinterInfo = null;
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get printer info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getnetPrinterInfoStmt, rs);

            tp.methodExit(netPrinterInfo);
        }

        return netPrinterInfo;
    }

    @Override
    public final String getLogoFilePath(final String storeid)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeid", storeid);

        String logoPath = null;
        Connection connection = null;
        PreparedStatement getLogoPathStmt = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getLogoPathStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-stamp-tax-path"));
            getLogoPathStmt.setString(1, storeid);

            rs = getLogoPathStmt.executeQuery();
            if (rs.next()) {
                logoPath = rs.getString("storelogo");
            } else {
                tp.println("Logo path not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get logo path.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getLogoPathStmt, rs);

            tp.methodExit(logoPath);
        }
        return logoPath;
    }

    /**
     * gets the settlement number.
     *
     * @param settlementNum - the settlement number
     * @return String - formatted settlement number
     */
    private String getSettlementNum(final String settlementNum) {

        if (settlementNum.length() != 24) {
            return settlementNum;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(settlementNum.substring(0, 6)).append("-");
        sb.append(settlementNum.substring(6, 14)).append("-");
        sb.append(settlementNum.substring(14, 19)).append("-");
        sb.append(settlementNum.substring(19));

        return sb.toString();
    }

    /**
     * get credit number from TXL_CREDITLOG.
     *
     * @param txid - transaction id
     * @return credit number
     * @throws DaoException - thrown when any exception occurs
     */
    public final ReceiptCredit getReceiptCredit(final String txid)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("txid", txid);

        ReceiptCredit receiptCredit = new ReceiptCredit();
        Connection connection = null;
        PreparedStatement getCreditStmnt = null;
        ResultSet rs = null;
        try {

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getCreditStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-credit-byid"));
            getCreditStmnt.setString(1, txid);

            rs = getCreditStmnt.executeQuery();
            if (rs.next()) {
                receiptCredit.setPan(rs.getString("txPan"));
                receiptCredit.setExpirationdate(rs
                        .getString("txExpirationDate"));
            } else {
                tp.println("Credit Info not found");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get credit info.", sqlEx);
        } finally {
            closeConnectionObjects(connection, getCreditStmnt, rs);

            tp.methodExit(receiptCredit.toString());
        }
        return receiptCredit;
    }

    /**
     * get operator name from MST_USER_CREDENTIALS.
     *
     * @param operatorNo - operator number to query
     * @return String - the operator name
     * @throws DaoException - thrown when any exception occurs
     */
    @Override
    public final String getOperatorName(final String operatorNo) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("operatorNo", operatorNo);

        String operatorName = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement prepdStatement = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            prepdStatement = connection.prepareStatement(sqlStatement
                    .getProperty("get-operator-status"));
            prepdStatement.setString(1, operatorNo);
            result = prepdStatement.executeQuery();
            if (result.next()) {
                operatorName = result.getString("OperatorName");
            } else {
                tp.println("Operator not found");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get operator.", e);
            throw new DaoException("SQLException:" + " @SQLServerReceiptDAO."
                    + functionName + " - Error get operator name", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, result);

            tp.methodExit(operatorName);
        }

        return operatorName;
    }

    /**
     * Get the transaction POSLog XML by specifying the transaction number.
     *
     * @param terminalid    The Device Terminal ID
     * @param storeid       The Store ID
     * @param txid          The Transaction Number
     * @param summarydateid The Summary Date Id
     * @param trainingflag  The Training Flag
     * @param connection The Database Connection
     * @return The POSLog XML representation for the Transaction
     * @throws DaoException The Exception thrown when the getting of Transaction
     *             fails.
     */
    private String getTransaction(final String terminalid,
            final String storeid, final String txid,
            final String summarydateid, final Connection connection,
            final int trainingflag)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("terminalid", terminalid)
                .println("storeid", storeid).println("txid", txid)
                .println("summarydate", summarydateid)
                .println("trainingflag", trainingflag);

        PreparedStatement select = null;
        ResultSet result = null;
        String posLogXML = "";
        //start add by mlwang 2014/09/03
        java.sql.Date summarydate = java.sql.Date.valueOf(summarydateid);
        //end add 2014/09/03
        try {
            /*
             * Retrieves sql query statement from
             * /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection
                    .prepareStatement(sqlStatement
                            .getProperty("get-poslog-xml-by-terminalid-storeid-txid-summarydateid"));

            select.setString(SQLStatement.PARAM1, storeid);
            select.setString(SQLStatement.PARAM2, terminalid);
            select.setString(SQLStatement.PARAM3, txid);
            //start add by mlwang 2014/09/03
            select.setDate(SQLStatement.PARAM4, summarydate);
            select.setInt(SQLStatement.PARAM5, trainingflag);
            //end add 2014/09/03

           result = select.executeQuery();
            if (result.next()) {
                posLogXML = result.getString(result.findColumn("Tx"));
            } else {
                tp.println("No transaction found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get poslog xml.", ex);
            throw new DaoException("Exception: @getTransaction - "
                    + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(null, select, result);

            tp.methodExit();
        }
        return posLogXML;
    }
}
