package ncr.res.mobilepos.terminalInfo.dao;

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
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.terminalInfo.model.TerminalInfo;

/**
 * A Data Access Object implementation for System Setting.
 */
public class SQLServerTerminalInfoDAO extends AbstractDao implements ITerminalInfoDAO {
    /**
     * Database Manager.
     */
    private DBManager dbManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * Program Name.
     */
    private static final String PROG_NAME = "TerminalInfoDAO";

    /**
     * SQL Server for System Setting.
     *
     * @throws DaoException Exception when error occurs
     */
    public SQLServerTerminalInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    @Override
    public TerminalInfo getTxidInfo(String companyid, String storeid, String workstationid, String businessDayDate,
            int trainingmode) throws DaoException {

        String functionName = "getTxidInfo";
        tp.methodEnter(functionName);
        Connection conn = null;
        PreparedStatement getTxidInfoPrpStmnt = null;
        ResultSet result = null;
        TerminalInfo terminalInfo = new TerminalInfo();

        try {
            conn = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            getTxidInfoPrpStmnt = conn.prepareStatement(sqlStatement.getProperty("get-last-txid-server"));
            getTxidInfoPrpStmnt.setString(SQLStatement.PARAM1, companyid);
            getTxidInfoPrpStmnt.setString(SQLStatement.PARAM2, storeid);
            getTxidInfoPrpStmnt.setString(SQLStatement.PARAM3, workstationid);
            getTxidInfoPrpStmnt.setString(SQLStatement.PARAM4, businessDayDate);
            getTxidInfoPrpStmnt.setInt(SQLStatement.PARAM5, trainingmode);

            result = getTxidInfoPrpStmnt.executeQuery();
            if (result.next()) {
                terminalInfo.setTxid(result.getString("SequenceNumber"));
            } else {
                terminalInfo.setTxid("0");
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the TxidInfo: " + ex.getMessage());
            throw new DaoException("SQLStatementException: @" + functionName, ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the TxidInfo: " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName, ex);
        } finally {
            closeConnectionObjects(conn, getTxidInfoPrpStmnt, result);

            if (null != terminalInfo) {
                tp.methodExit(terminalInfo.toString());
            } else {
                tp.methodExit("null");
            }
        }
        return terminalInfo;
    }

}
