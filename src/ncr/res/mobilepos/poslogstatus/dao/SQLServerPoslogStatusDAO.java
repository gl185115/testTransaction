package ncr.res.mobilepos.poslogstatus.dao;

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
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPoslogStatusDAO extends AbstractDao implements IPoslogStatusDAO {

    private final SQLStatement sqlStatement;
    /**
     * database manager.
     */
    private DBManager dbManager;
    /**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer. */
    private Trace.Printer tp;

    private String progName = "PoslogStatusDAO";

    /**
     * The class constructor.
     * 
     * @throws DaoException
     *             Exception thrown when construction fails.
     */
    public SQLServerPoslogStatusDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        this.sqlStatement = SQLStatement.getInstance();
    }

    /**
     * check poslog status and return count of poslog not deal with.
     * 
     * @param consolidation
     * @param transfer
     * @param columnName
     * @return PoslogStatusInfo.
     * @throws DaoException
     *             - if database fails.
     */
    @Override
    public final PoslogStatusInfo checkPoslogStatus(boolean consolidation, boolean transfer, String columnName)
            throws DaoException {
        tp.methodEnter("checkPoslogStatus");
        PoslogStatusInfo poslogStatus = new PoslogStatusInfo();
        try {
            if(consolidation) {
                poslogStatus.setConsolidationResult(countUnsendPoslog("Status"));
            }
            if(transfer) {
                poslogStatus.setTransferResult(countUnsendPoslog(columnName));
            }
        } finally {
            tp.methodExit(poslogStatus.toString());
        }
        return poslogStatus;
    }

    /**
     * check poslog status and return count of poslog not deal with.
     * @param checkColumnName Column name to Check
     * @return PoslogStatusInfo.
     * @throws DaoException - if database fails.
     */
    @Override
    public final int countUnsendPoslog(String checkColumnName)
            throws DaoException {
        tp.methodEnter("countUnsendPoslog");
        int unsendCount = -1;
        try(Connection connection = dbManager.getConnection();
            PreparedStatement statement = prepareStatementCountUnsendPoslog(connection, checkColumnName);
            ResultSet result = statement.executeQuery()){
            result.next();
            unsendCount = result.getInt("UnsendCount");
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.countUnsendPoslog()",
                    Logger.RES_EXCEP_SQL,
                    "Failed to get unsend poslog count." + sqlEx.getMessage());
            throw new DaoException("SQLException: @countUnsendPoslog ", sqlEx);
        } finally {
            tp.methodExit(unsendCount);
        }
        return unsendCount;
    }

    /**
     * Populates PreparedStatement with given SQL name.
     * @param connection DB-connection
     * @param checkColumnName columnName
     * @return
     */
    private PreparedStatement prepareStatementCountUnsendPoslog(Connection connection, String checkColumnName)
            throws SQLException {
        // Replace %s to check column name.
        String query = String.format(sqlStatement.getProperty("count-unsend-poslog"), checkColumnName);
        PreparedStatement statement = connection.prepareStatement(query);
        return statement;
    }
}