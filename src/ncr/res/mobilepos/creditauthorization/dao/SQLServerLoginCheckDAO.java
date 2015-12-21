package ncr.res.mobilepos.creditauthorization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * A Data Access Object implementation for Credit Authorization.
 */
public class SQLServerLoginCheckDAO extends AbstractDao implements
        ILoginCheckDAO {

    /** The Constant PROGNAME. */
    private static final String PROGNAME = "AuthPP";

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
     *             thrown when database error occurs.
     */
    public SQLServerLoginCheckDAO() throws DaoException {
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

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.creditauthorization.dao.ILoginCheckDAO#getLoggingInfo
     * (ncr.res.pastelport.platform.CommonTx)
     */
    @Override
    public final boolean getLoggingInfo(final CommonTx tx) throws DaoException,
            JSONException {
        tp.methodEnter("get-logginginfo");
        // credit number
        boolean loggingInfo = false;
        Connection connection = null;
        PreparedStatement getLoggingInfoPrepStmnt = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getLoggingInfoPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-logginginfo"));
            String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");
            String txid = tx.getFieldValue("txid");
            String paymentseq = tx.getFieldValue("paymentseq");
            String guid = tx.getFieldValue("guid");
            
            getLoggingInfoPrepStmnt.setString(SQLStatement.PARAM1,
                    storeid);
            getLoggingInfoPrepStmnt.setString(SQLStatement.PARAM2,
                    terminalid);
            getLoggingInfoPrepStmnt.setInt(SQLStatement.PARAM3,
                    Integer.valueOf(txid));
            getLoggingInfoPrepStmnt.setInt(SQLStatement.PARAM4,
                    Integer.valueOf(paymentseq));
            getLoggingInfoPrepStmnt.setString(SQLStatement.PARAM5,
                    guid);
            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);
            tp.println("txid", txid);
            tp.println("paymentseq", paymentseq);
            tp.println("guid", guid);

            rs = getLoggingInfoPrepStmnt.executeQuery();

            if (rs.next()) {
                loggingInfo = true;
            }
        } catch (SQLException sqlStmtEx) {
            LOGGER.logAlert(SQLServerLoginCheckDAO.PROGNAME,
                    "SQLServerLoginCheckDAO.getLoggingInfo().",
                    Logger.RES_EXCEP_SQL, "SQL Statement Error occured. \n"
                            + sqlStmtEx.getMessage());
        } catch (SQLStatementException sqlEx) {
            LOGGER.logAlert(SQLServerLoginCheckDAO.PROGNAME,
                    "SQLServerLoginCheckDAO.getLoggingInfo().",
                    Logger.RES_EXCEP_SQLSTATEMENT, "SQL Error occured. \n"
                            + sqlEx.getMessage());
        } finally {
            closeConnectionObjects(connection, getLoggingInfoPrepStmnt, rs);
            
            tp.methodExit(loggingInfo);
        }
        return loggingInfo;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.creditauthorization.dao.ILoginCheckDAO#saveLoggingInfo
     * (ncr.res.pastelport.platform.CommonTx)
     */
    @Override
    public final boolean saveLoggingInfo(final CommonTx tx)
            throws DaoException, JSONException {
        tp.methodEnter("saveLoggingInfo");
        boolean result = false;
        Connection connection = null;
        PreparedStatement saveLoggingInfoPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveLoggingInfoPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-LoggingInfo"));
            String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");
            String txid = tx.getFieldValue("txid");
            String paymentseq = tx.getFieldValue("paymentseq");
            String guid = tx.getFieldValue("guid");
            
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM1,
                    storeid);
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM2,
                    terminalid);
            saveLoggingInfoPrepStmnt.setInt(SQLStatement.PARAM3,
                    Integer.valueOf(txid));
            saveLoggingInfoPrepStmnt.setInt(SQLStatement.PARAM4,
                    Integer.valueOf(paymentseq));
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM5,
                    guid);
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM6,
                    tx.getFieldValue("brand"));
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM7,
                    tx.getFieldValue("service"));
            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);
            tp.println("txid", txid);
            tp.println("paymentseq", paymentseq);
            tp.println("guid", guid);

            try {
                if (0 == tx.getFieldValue("amount").trim().length()) {
                    saveLoggingInfoPrepStmnt.setLong(SQLStatement.PARAM8,
                            0L);
                } else {
                    saveLoggingInfoPrepStmnt.setLong(SQLStatement.PARAM8,
                            Long.valueOf(tx.getFieldValue("amount")));
                }
            } catch (SQLException e) {
                saveLoggingInfoPrepStmnt.setLong(SQLStatement.PARAM8,
                        0L);
            } catch (NumberFormatException e) {
                saveLoggingInfoPrepStmnt.setLong(SQLStatement.PARAM8,
                        0L);
            } catch (JSONException e) {
                saveLoggingInfoPrepStmnt.setLong(SQLStatement.PARAM8,
                        0L);
            }

            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM9,
                    DateFormatUtility
                        .localeTime(tx.getFieldValue("txdatetime")));

            // rwid: this service didn't need this field
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM10, null);

            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM11,
                    tx.getFieldValue("rwkind"));
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM12,
                    tx.getFieldValue("txtype"));

            // xmldocument: not using
            saveLoggingInfoPrepStmnt.setString(SQLStatement.PARAM13, "");

            if (saveLoggingInfoPrepStmnt.executeUpdate() > 0) {
                connection.commit();
                result = true;
            } else {
                connection.rollback();
                result = false;
                tp.println("Failed to save pastelport log info.");
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(SQLServerLoginCheckDAO.PROGNAME,
                    "SQLServerLoginCheckDAO.saveLoggingInfo()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerLoginCheckDAO:saveLoggingInfo",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(SQLServerLoginCheckDAO.PROGNAME,
                    "SQLServerLoginCheckDAO.saveLoggingInfo()",
                    Logger.RES_EXCEP_SQL, "SQL Exception Error occured. \n"
                            + sqlEx.getMessage());
            rollBack(connection, "@SQLServerLoginCheckDAO:saveLoggingInfo",
                    sqlEx);

        } finally {
            closeConnectionObjects(connection, saveLoggingInfoPrepStmnt);
            
            tp.methodExit(result);
        }
        return result;
    }
}
