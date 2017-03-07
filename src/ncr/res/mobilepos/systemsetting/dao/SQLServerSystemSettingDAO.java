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
