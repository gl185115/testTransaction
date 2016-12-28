package ncr.res.mobilepos.systemsetting.dao;

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
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemsetting.model.DateSetting;

/**
 * A Data Access Object implementation for System Setting.
 */
public class SQLServerSystemSettingDAO
extends AbstractDao implements ISystemSettingDAO {
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
    private static final String PROG_NAME = "BizDAO";
     /**
     * SQL Server for System Setting.
     * @throws DaoException     Exception when error occurs
     */
    public SQLServerSystemSettingDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int setDateSetting(final String companyid, final String storeid,
            final String businessdate, final String eod, final String skips)
    throws DaoException {
        String functionName = "SQLServerSystemSettingDAO.setDateSetting";

        tp.methodEnter("setDateSetting");
        tp.println("businessdate", businessdate)
            .println("eod", eod)
            .println("skips", skips);

        Connection connection = null;
        int result = 0;

        try {
            connection = dbManager.getConnection();
            boolean hasBusinessDate =
                null != businessdate && !businessdate.isEmpty();
            boolean hasEOD = null != eod && !eod.isEmpty();
            boolean hasSkips = null != skips && !skips.isEmpty();

            //Is there BusinessDate to Set?
            //If yes, then Set the business date
            if (hasBusinessDate) {
                result = this.updateSystemSetting(connection, businessdate,
                        "update-todaydate-setting");
                tp.println("Businessdate set to " + businessdate);
            }

            //Is there E.O.D. Time to be set?
            //If yes, then set the End of Day
            if (hasEOD) {
                result += this.updateSystemSetting(connection, eod,
                        "update-switchtime-setting");
                tp.println("EOD set to " + eod);
            }

            //Is there Skips to be Set?
            //If yes, then set the Skips
            if (hasSkips) {
                result += this.updateSystemSetting(connection, skips,
                        "update-skips-setting");
                tp.println("Skip set to " + skips);
            }

            //Are there NO updated Date Settings
            //And, given that there are new Date Settings value?
            //If yes, Try to insert the new values instead
            if ((result == 0) && hasBusinessDate && hasEOD) {
                result += this.insertSystemSetting(connection, businessdate,
                        eod, skips);
                tp.println("New values set.");
            }

            connection.commit();
        } catch (DaoException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to set the DateSettings - "
                    + "BusinessDate" + businessdate
                    + "EOD " + eod
                    + "Skips " + skips + " : " + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLStatementException: @" + functionName
                    + " - Error Set DateSetting process", ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to set the DateSettings - "
                    + "BusinessDate" + businessdate
                    + "EOD " + eod
                    + "Skips " + skips + " : " + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName
                    + " - Error Set Date process", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to set the DateSettings - "
                    + "BusinessDate" + businessdate
                    + "EOD " + eod
                    + "Skips " + skips + " : " + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName
                    + " - Error Set Date process", ex);
        }  finally {
            closeConnectionObjects(connection, null, null);
            
            tp.methodExit(result);
        }
        return result;
    }

    /**
     * Helper Method used to Update the given Date Setting in the MST_BIZDAY.
     *
     * @param conn                The SQL Connection
     * @param value                The new value of the Date Setting
     * @param stmnt                The SQL statement needed
     * @return                    The number of rows affected
     * @throws DaoException     The Exception thrown when the method fail
     */
    private int updateSystemSetting(
            final Connection conn, final String value, final String stmnt)
    throws DaoException {
        String functionName = "SSQLServerSystemSettingDAO.updateSystemSetting";

        tp.methodEnter("updateSystemSetting");
        tp.println("value", value).println("stmnt", stmnt);

        int result = 0;
        PreparedStatement updateBizDayPrpStmnt = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateBizDayPrpStmnt =
                conn.prepareStatement(sqlStatement.getProperty(stmnt));
            updateBizDayPrpStmnt.setString(1, value);
            result = updateBizDayPrpStmnt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to set the Date Settings -  "
                    + "Having statement of " + stmnt
                    + "with value of " + value
                    + " : " + ex.getMessage());
            throw new DaoException("SQLStatementException: @"
                    + functionName + " - Error update Business Date process",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to set the Date Settings -  "
                    + "Having statement of " + stmnt
                    + "with value of " + value
                    + " : " + ex.getMessage());
            throw new DaoException("Exception: @"
                    + functionName + " - Error update Business Date process",
                    ex);
        } finally {
            closeConnectionObjects(null, updateBizDayPrpStmnt, null);
            
            tp.methodExit(result);
        }
        return result;
    }

    /**
     * Helper Method used to Insert the given Date Setting in the MST_BIZDAY.
     *
     * @param conn                The SQL Connection
     * @param params            The new DateSettings value
     * @return                    The number of rows affected
     * @throws DaoException     The Exception thrown when the method fail
     */
    private int insertSystemSetting(
            final Connection conn, final String ... params)
    throws DaoException {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        int result = 0;
        PreparedStatement updateBizDayPrpStmnt = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateBizDayPrpStmnt = conn.prepareStatement(
                    sqlStatement.getProperty("insert-date-setting"));
            updateBizDayPrpStmnt.setString(SQLStatement.PARAM1, params[0]);
            updateBizDayPrpStmnt.setString(SQLStatement.PARAM2, params[1]);
            updateBizDayPrpStmnt.setString(SQLStatement.PARAM3, params[2]);
            result = updateBizDayPrpStmnt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to set the Date Settings -  "
                    + "BusinessDate " + params[0]
                    + "EOD " + params[1]
                    + "Skips " + params[2]
                    + " : "  + ex.getMessage());
            throw new DaoException("SQLStatementException: @"
                    + functionName + " - Error update Business Date process",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to set the Date Settings -  "
                    + "BusinessDate " + params[0]
                    + "EOD " + params[1]
                    + "Skips " + params[2]
                    + " : "  + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error update Business Date process", ex);
        } finally {
            closeConnectionObjects(null, updateBizDayPrpStmnt, null);
            
            tp.methodExit(result);
        }
        return result;
    }

    @Override
    public final DateSetting getDateSetting(final String companyId, final String storeId) throws DaoException {
       
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        Connection conn = null;
        PreparedStatement selectBizDayPrpStmnt = null;
        ResultSet result = null;
        DateSetting retDateSetting = null;

        try {
            conn = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectBizDayPrpStmnt = conn.prepareStatement(
                    sqlStatement.getProperty("select-date-setting"));
            selectBizDayPrpStmnt.setString(SQLStatement.PARAM1, companyId);
            selectBizDayPrpStmnt.setString(SQLStatement.PARAM2, storeId);
            
            result = selectBizDayPrpStmnt.executeQuery();
            if (result.next()) {
                String businessdate = result.getString(
                        result.findColumn("TodayDate"));
                String skips = result.getString(result.findColumn("Skip"));
                if (skips == null) {
                    skips = "0";
                    tp.println("Skips set to 0");
                }
                String switchtime = result.getString(
                        result.findColumn("SwitchTime"));

                retDateSetting = new DateSetting();
                retDateSetting.setEod(switchtime);
                retDateSetting.setSkips(Integer.parseInt(skips));
                retDateSetting.setToday(businessdate);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the Date Settings: " + ex.getMessage());
            throw new DaoException("SQLStatementException: @"
                    + functionName + " - Error update Business Date process",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the Date Settings: " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error update Business Date process", ex);
        } finally {
            closeConnectionObjects(conn, selectBizDayPrpStmnt, result);
            
            if (null != retDateSetting) {
                tp.methodExit(retDateSetting.toString());
            } else {
                tp.methodExit("null");
            }
        }
        return retDateSetting;
    }
}
