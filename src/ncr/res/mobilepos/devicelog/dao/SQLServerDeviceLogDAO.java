/**
 *
 */
package ncr.res.mobilepos.devicelog.dao;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.devicelog.model.DeviceLog;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

import org.apache.commons.fileupload.FileItem;

/**
 * @author Developer
 *
 */
public class SQLServerDeviceLogDAO extends AbstractDao
implements IDeviceLogDAO {
    /**
     * class instance of DBManager.
     */
    private DBManager dbManager;
    /**
     * class instance of Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * full name of the class.
     */
    private String className = "SQLServerDeviceLogDao";
    /**
     * shortened name of the class used for logging.
     */
    private String progName = "DeviceLog";
    /**
     * instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * constructor.
     * @throws DaoException - thrown when an exception occurs
     */
    public SQLServerDeviceLogDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.devicelog.dao.IDeviceLogDao#saveLogFile
     * (java.lang.String, java.util.Date,
     * org.apache.commons.fileupload.FileItem)
     */
    @Override
    public final DeviceLog saveLogFile(final String udid, final Date logDate,
            final FileItem logFile) throws DaoException {

        tp.methodEnter("saveLogFile");
        tp.println("udid", udid)
            .println("logDate", (null != logDate)
                    ? logDate.toString() : null)
            .println("logFile", (null != logFile)
                    ? logFile.getContentType() : null);

        DeviceLog result = new DeviceLog();
        Connection connection = null;
        CallableStatement saveStatement = null;
        ResultSet rs = null;

        // Cannot accept empty udid or logDate
        if (null == udid || udid.trim().isEmpty() || null == logDate) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Invalid universal device ID or log date.");
            tp.methodExit("Invalid universal device ID or log date.");
            return result;
        } else if (null == logFile) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Error reading log file.");
            tp.methodExit("Error reading log file.");
            return result;
        }

        try {
            connection = dbManager.getConnection();
            InputStream fileStream = logFile.getInputStream();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveStatement = connection.prepareCall(sqlStatement
                    .getProperty("save-DeviceLogFile"));

            java.sql.Date sqlDate = new java.sql.Date(logDate.getTime());

            saveStatement.setString(SQLStatement.PARAM1, udid.trim());
            saveStatement.setDate(SQLStatement.PARAM2, sqlDate);
            saveStatement.setBinaryStream(SQLStatement.PARAM3, fileStream,
                    (int) logFile.getSize());
            saveStatement.execute();

            processQueryResult(saveStatement, rs, result);

            connection.commit();

        } catch (IOException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
            result.setMessage(e.getMessage());
            rollBack(connection, "@SQLServerDeviceLogDAO#saveLogFile", e);
        } catch (SQLException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
            result.setMessage(e.getMessage());
            rollBack(connection, "@SQLServerDeviceLogDAO#saveLogFile", e);
        } catch (SQLStatementException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQLSTATEMENT);
            result.setMessage(e.getMessage());
            rollBack(connection, "@SQLServerDeviceLogDAO#saveLogFile", e);
        } finally {
            closeConnectionObjects(connection, saveStatement, rs);
            
            tp.methodExit(result.toString());
        }
        return result;
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.devicelog.dao.
     * IDeviceLogDao#updateLogFile(java.lang.String,
     * org.apache.commons.fileupload.FileItem)
     */
    @Override
    public final DeviceLog updateLogFile(final String rowId,
            final FileItem logFile) throws DaoException {

        tp.methodEnter("updateLogFile");
        tp.println("rowId", rowId)
            .println("logFile", (null != logFile)
                    ? logFile.getContentType() : null);

        DeviceLog result = new DeviceLog();
        Connection connection = null;
        CallableStatement updateStatement = null;
        ResultSet rs = null;

        if (null == rowId || rowId.isEmpty()) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Row ID is empty.");
            tp.methodExit("Row ID is empty.");
            return result;
        } else if (null == logFile) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Error reading log file.");
            tp.methodExit("Error reading log file.");
            return result;
        }

        try {
            connection = dbManager.getConnection();
            InputStream fileStream = logFile.getInputStream();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStatement = connection.prepareCall(
                    sqlStatement.getProperty("update-DeviceLogFile"));
            updateStatement.setString(2, rowId.trim());
            updateStatement.setBinaryStream(1, fileStream,
                    (int) logFile.getSize());
            updateStatement.execute();

            processQueryResult(updateStatement, rs, result);
            if (null == result.getRowId() || result.getRowId().isEmpty()) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Row ID '" + rowId + "' does not exists.");
                tp.println("Row ID '" + rowId + "' does not exists.");
            }

            connection.commit();

        } catch (SQLException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
            result.setMessage(e.getMessage() + "\n\nPossible Cause:"
                    + " Invalid rowId=" + rowId);
            rollBack(connection, "@SQLServerDeviceLogDAO#updateLogFile"
                    + "\n\nPossible Cause: Invalid rowId=" + rowId, e);
        } catch (IOException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
            result.setMessage(e.getMessage());
            rollBack(connection, "@SQLServerDeviceLogDAO#updateLogFile", e);
        } catch (SQLStatementException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQLSTATEMENT);
            result.setMessage(e.getMessage());
            rollBack(connection, "@SQLServerDeviceLogDAO#updateLogFile", e);
        } finally {
            closeConnectionObjects(connection, updateStatement, rs);
            
            tp.methodExit(result.toString());
        }
        return result;
    }

    @Override
    public final DeviceLog getDeviceLogInfo(final String rowId)
            throws DaoException {

        tp.methodEnter("getDeviceLogInfo");
        tp.println("rowId", rowId);

        DeviceLog result = new DeviceLog();
        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet rs = null;

        if (null == rowId) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Row ID is empty.");
            tp.methodExit("Row ID is empty.");
            return result;
        }

        try {
            connection = dbManager.getConnection();

            selectStatement = connection.prepareStatement("SELECT TOP 1 id, "
                    + "udid, logdate, uploadtime, lastupdated "
                    +          "FROM DEVICE_LOG WHERE id = ?;");

            selectStatement.setString(1, rowId.trim());
            rs = selectStatement.executeQuery();

            if (rs.next()) {
                readResultData(result, rs);
            } else {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Row ID '" + rowId + "' does not exists.");
                tp.println("Row ID '" + rowId + "' does not exists.");
            }

        } catch (SQLException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
            result.setMessage(e.getMessage()
                    + "\n\nPossible Cause: Invalid rowId: "
                    + rowId);
        } finally {
            closeConnectionObjects(connection, selectStatement, rs);
            
            tp.methodExit(result.toString());
        }

        return result;
    }

    @Override
    public final DeviceLog[] getLogsOfDevice(final String udid,
            final int startIndex, final int endIndex) throws DaoException {

        tp.methodEnter("getLogsOfDevice");
        tp.println("udid", udid)
            .println("startIndex", Integer.toString(startIndex))
            .println("endIndex", Integer.toString(endIndex));

        Vector<DeviceLog> deviceLogs =
            new Vector<DeviceLog>((endIndex >= startIndex)
                    ? endIndex - startIndex + 1 : 0, 1);
        DeviceLog[] temp = new DeviceLog[]{};

        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStatement =
                connection.prepareStatement(
                        sqlStatement.getProperty("get-DeviceLogs"));

            selectStatement.setString(SQLStatement.PARAM1, udid.trim());
            selectStatement.setInt(SQLStatement.PARAM2, startIndex + 1);
            selectStatement.setInt(SQLStatement.PARAM3, endIndex + 1);
            rs = selectStatement.executeQuery();

            DeviceLog aDeviceLog;

            while (rs.next()) {
                aDeviceLog = new DeviceLog();
                readResultData(aDeviceLog, rs);
                deviceLogs.add(aDeviceLog);
            }

        } catch (SQLException e) {
            throw new DaoException("SQLException: @getLogsOfDevice - "
                    + e.getMessage());
        } catch (SQLStatementException e) {
            throw new DaoException("SQLStatementException: @getLogsOfDevice - "
                    + e.getMessage());
        } finally {
            closeConnectionObjects(connection, selectStatement, rs);
            
            tp.methodExit(deviceLogs.size());
        }
        return deviceLogs.toArray(temp);
    }

    @Override
    public final boolean readBinaryData(final String rowId,
            final OutputStream out) throws DaoException {

        boolean success = false;

        String functionName = className + "#readBinaryData";

        tp.methodEnter("readBinaryData");
        tp.println("rowId", rowId)
            .println("out", out.toString());

        Connection connection = null;
        PreparedStatement selectStatement = null;
        ResultSet rs = null;
        InputStream logFile = null;

        try {
            connection = dbManager.getConnection();
            selectStatement = connection.prepareStatement("SELECT TOP 1 "
                    + "logfile FROM DEVICE_LOG WHERE id = ? ;");
            selectStatement.setString(SQLStatement.PARAM1, rowId);

            rs = selectStatement.executeQuery();
            if (rs.next()) {
                int b;
                logFile = rs.getBinaryStream("logfile");
                if (null != logFile) {
                    while ((b = logFile.read()) != -1) {
                        out.write(b);
                    }
                }
                success = true;
            } else {
                success = false;
                tp.println("ResultSet is empty.");
            }

        } catch (SQLException e) {
            success = false;
            LOGGER.logError(progName, functionName,
                    Integer.toString(ResultBase.RES_ERROR_SQL), e.getMessage());
        } catch (IOException e) {
            success = false;
            LOGGER.logError(progName, functionName,
                    Integer.toString(ResultBase.RES_ERROR_IOEXCEPTION),
                    e.getMessage());
        } finally {
            closeConnectionObjects(connection, selectStatement, rs);
            
            tp.methodExit(success);
        }

        return success;
    }

    /**
     * reads the result data.
     * @param dest - device log to set
     * @param src - result set to get the data from
     * @throws SQLException - exception thrown when
     * there is a problem in sql
     */
    private static void readResultData(final DeviceLog dest,
            final ResultSet src) throws SQLException {
        dest.setRowId(src.getString("id"));
        dest.setUdid(src.getString("udid"));
        dest.setLogDate(src.getDate("logdate"));
        dest.setUploadTime(src.getTimestamp("uploadtime"));
        dest.setLastUpdated(src.getTimestamp("lastupdated"));
    }

    /**
     * process the query result.
     * @param sqlProcStmt - statement to be called
     * @param resultSetContainer - result set to contain result
     * @param dest - device log data which will be filled with
     * the data
     * @throws SQLException - exception thrown when
     * there is a problem in sql
     */
    private static void processQueryResult(final CallableStatement sqlProcStmt,
            ResultSet resultSetContainer,
            final DeviceLog dest) throws SQLException {
        boolean nexResult = sqlProcStmt.getMoreResults();
        int updateCount = sqlProcStmt.getUpdateCount();
        while (!(!nexResult && -1 == updateCount)) {
            if (nexResult) {
                resultSetContainer = sqlProcStmt.getResultSet();
                if (resultSetContainer.next()) {
                    readResultData(dest, resultSetContainer);
                    break;
                }
            }
            nexResult = sqlProcStmt.getMoreResults();
            updateCount = sqlProcStmt.getUpdateCount();
        }
    }

}
