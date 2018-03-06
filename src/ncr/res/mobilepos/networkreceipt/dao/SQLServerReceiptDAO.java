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
}
