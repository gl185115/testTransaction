package ncr.res.mobilepos.discountplaninfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class SQLServerPremiumFlagDAO extends AbstractDao implements IPremiumFlagDAO {
    /**
     * DBManager instance, provides database connection object.
     */
    private DBManager dbManager;
    /**
     * Logger instance, logs error and information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * program name of the class.
     */
    private static final String PROG_NAME = "PremiumFlagDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Initializes DBManager.
     *
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerPremiumFlagDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Retrieves DBManager.
     *
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    /**
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param dptIdList
     * @return premiumFlag(only return the dptList where the premiumFlag is true)
     * @throws DaoException
     */
    @Override
    public JSONData getPremiumFlag(String companyId, String storeId, String terminalId, String dptIdList)
            throws DaoException {
        String functionName = "getPremiumFlags";
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("TerminalId", terminalId).println("DptIdList", dptIdList);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData premiumFlag = new JSONData();
        JSONArray dptList = new JSONArray();
        JSONObject list = new JSONObject();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(String.format(sqlStatement.getProperty("get-premium-flag") ,GlobalConstant.getBizCatIdColumnOfStoreInfo()));
            selectStmnt.setString(SQLStatement.PARAM1, companyId);
            selectStmnt.setString(SQLStatement.PARAM2, storeId);
            selectStmnt.setString(SQLStatement.PARAM3, terminalId);
            selectStmnt.setString(SQLStatement.PARAM4, dptIdList);
            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                dptList.add(resultSet.getString("DptId"));
            }
            if (dptList.isEmpty()) {
                premiumFlag.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                premiumFlag.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                premiumFlag.setMessage(ResultBase.RES_NODATAFOUND_MSG);
            } else {
                list.put("dptList", dptList);
                premiumFlag.setJsonObject(list.toString());
                premiumFlag.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                premiumFlag.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                premiumFlag.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get premium flag.", sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerPremiumFlagDAO.getPremiumFlag", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get premium flag.", ex);
            throw new DaoException("Exception:" + " @SQLServerPremiumFlagDAO.getPremiumFlag", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(premiumFlag);
        }
        return premiumFlag;
    }
}