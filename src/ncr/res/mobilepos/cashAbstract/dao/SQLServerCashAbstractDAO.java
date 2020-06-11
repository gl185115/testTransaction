package ncr.res.mobilepos.cashAbstract.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class SQLServerCashAbstractDAO extends AbstractDao implements ICashAbstractDAO {
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
    private static final String PROG_NAME = "CashAbstractDAO";
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
    public SQLServerCashAbstractDAO() throws DaoException {
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
     * @param companyId,storeId
     *
     * @return Tender
     *
     * @throws DaoException
     */
    @Override
    public JSONData getcashAbstract(String companyId, String storeId, String cashFlowDirection, String tenderId, String tenderType) throws DaoException {
        String functionName = "getcashAbstract";
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("CashFlowDirection", cashFlowDirection).println("TenderId", tenderId).println("TenderType", tenderType);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData tender = new JSONData();
        HashMap<String, JSONArray> tenderList = new HashMap<String, JSONArray>();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-cashAbstract-type"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, companyId);
            selectStmnt.setString(SQLStatement.PARAM3, cashFlowDirection);
            selectStmnt.setString(SQLStatement.PARAM4, tenderId);
            selectStmnt.setString(SQLStatement.PARAM5, tenderType);
            resultSet = selectStmnt.executeQuery();
            JSONObject tenderInfo = null;
            while (resultSet.next()) {
                tenderInfo = new JSONObject();

                tenderInfo.put("companyId", resultSet.getString("CompanyId"));
                tenderInfo.put("storeId", resultSet.getString("StoreId"));
                tenderInfo.put("cashFlowDirection", resultSet.getString("CashFlowDirection"));
                tenderInfo.put("cashFlowType", resultSet.getString("CashFlowType"));
                JSONObject labelValue = new JSONObject();
                labelValue.put("ja", resultSet.getString("RemarksCol"));
                labelValue.put("en", resultSet.getString("RemarksKanaCol"));
                tenderInfo.put("label", labelValue);
                tenderInfo.put("remarksId", resultSet.getString("RemarksId"));

                if (tenderList.containsKey(resultSet.getString("CashFlowDirection"))) {
                    tenderList.get(resultSet.getString("CashFlowDirection")).add(tenderInfo);
                } else {
                    JSONArray erlectronicList = new JSONArray();
                    erlectronicList.add(tenderInfo);
                    tenderList.put(resultSet.getString("CashFlowDirection"), erlectronicList);
                }
            }
            if (tenderList.isEmpty()) {
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return tender;
            } else {
                JSONObject valueResult = new JSONObject();
                for (Iterator<Entry<String, JSONArray>> it = tenderList.entrySet().iterator(); it.hasNext();) {
                    Entry<String, JSONArray> e = it.next();
                    valueResult.put(e.getKey().toString(), e.getValue());
                }
                tender.setJsonObject(valueResult.toString());
                tender.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                tender.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                tender.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get cashAbstract infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerCashAbstractDAO.getcashAbstract", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get cashAbstract infomation.", ex);
            throw new DaoException("Exception:" + " @SQLServerCashAbstractDAO.getcashAbstract", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(tender);
        }
        return tender;
    }
}