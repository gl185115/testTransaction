package ncr.res.mobilepos.tenderinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.ResourceBundle;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class SQLServerTenderInfoDAO extends AbstractDao implements ITenderInfoDAO {
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
    private static final String PROG_NAME = "TenderInfoDAO";
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
    public SQLServerTenderInfoDAO() throws DaoException {
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
     * /**
     *
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     * @throws DaoException
     */
    @Override
    public JSONData getTenderInfo(String companyId, String storeId, String tenderType) throws DaoException {
        String functionName = "getTenderInfo";
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId).println("TenderType",
                tenderType);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData tender = new JSONData();
        HashMap<String, JSONArray> tenderList = new HashMap<String, JSONArray>();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            StringBuilder exceptTIAfterTrim = new StringBuilder();
            Boolean flagList = false;
            Boolean flagOfTwo = false;
            Boolean flagOfFive = false;
            Boolean flagOfSeven = false;
            if (!StringUtility.isNullOrEmpty(tenderType)) {
                if (tenderType.indexOf(",") != -1) {
                    // trim all elements
                    String exceptTypeIdArray[] = tenderType.split(",");
                    for (int i = 0; i < exceptTypeIdArray.length; i++) {
                        exceptTypeIdArray[i] = exceptTypeIdArray[i].trim();
                        if (exceptTypeIdArray[i].indexOf("-1") > -1) {
                            flagList = true;
                            continue;
                        }
                        if (exceptTypeIdArray[i].indexOf("2") > -1)
                            flagOfTwo = true;
                        if (exceptTypeIdArray[i].indexOf("5") > -1)
                            flagOfFive = true;
                        if (exceptTypeIdArray[i].indexOf("7") > -1)
                            flagOfSeven = true;
                        if (i > 0)
                            exceptTIAfterTrim.append(",");
                        exceptTIAfterTrim.append(exceptTypeIdArray[i]);
                    }
                } else {
                    exceptTIAfterTrim.append(tenderType.trim());
                }
            }
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-tenderinfo-type"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, companyId);
            selectStmnt.setString(SQLStatement.PARAM3, exceptTIAfterTrim.toString());
            resultSet = selectStmnt.executeQuery();

            JSONObject tenderInfo = null;
            JSONObject labelValue = null;
            JSONArray erlectronicList = null;
            while (resultSet.next()) {
                tenderInfo = new JSONObject();
                tenderInfo.put("companyId", resultSet.getString("CompanyId"));
                tenderInfo.put("storeId", storeId);
                tenderInfo.put("tenderType", resultSet.getString("TenderType"));
                tenderInfo.put("tenderId", resultSet.getString("TenderId"));
                tenderInfo.put("code", resultSet.getString("TenderId"));
                labelValue = new JSONObject();
                labelValue.put("ja", resultSet.getString("TenderName"));
                labelValue.put("en", resultSet.getString("TenderKanaName"));

                tenderInfo.put("label", labelValue);
                tenderInfo.put("stampType", resultSet.getString("StampType"));
                tenderInfo.put("pointType", resultSet.getString("PointType"));
                tenderInfo.put("changeType", resultSet.getString("ChangeType"));
                tenderInfo.put("unitPrice", resultSet.getString("UnitPrice"));
                tenderInfo.put("displayOrder", resultSet.getString("DisplayOrder"));

                if (tenderList.containsKey(resultSet.getString("TenderType"))) {
                    tenderList.get(resultSet.getString("TenderType")).add(tenderInfo);
                } else {
                    erlectronicList = new JSONArray();
                    erlectronicList.add(tenderInfo);
                    tenderList.put(resultSet.getString("TenderType"), erlectronicList);
                }
            }
            if (tenderList.isEmpty()) {
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return tender;
            } else {
                JSONObject valueResult = new JSONObject();

                ResourceBundle jaStr = ResourceBundle.getBundle("label", Locale.JAPANESE);
                ResourceBundle enStr = ResourceBundle.getBundle("label", Locale.ENGLISH);

                if (flagList && flagOfTwo && flagOfFive) {
                    JSONObject elec = new JSONObject();
                    JSONArray elecList = new JSONArray();
                    labelValue = new JSONObject();
                    labelValue.put("ja", jaStr.getString("tenderCtrlElectronicCash"));
                    labelValue.put("en", enStr.getString("tenderCtrlElectronicCash"));

                    elec.put("label", labelValue);
                    elec.put("list", tenderList.get("5"));
                    elecList.add(elec);
                    tenderList.put("5", elecList);
                }
                if (flagList && flagOfTwo && flagOfSeven) {
                    JSONObject elec = new JSONObject();
                    JSONArray elecList = new JSONArray();
                    labelValue = new JSONObject();

                    if (tenderList.get("7").length() > 1) {
                        labelValue.put("ja", jaStr.getString("tenderCtrlShoppingLoan"));
                        labelValue.put("en", enStr.getString("tenderCtrlShoppingLoan"));
                        elec.put("label", labelValue);
                        elec.put("list", tenderList.get("7"));
                        elecList.add(elec);
                        tenderList.put("7", elecList);
                    }
                }
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
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get tender infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerTenderInfoDAO.getTenderInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get tender infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerTenderInfoDAO.getTenderInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(tender);
        }
        return tender;
    }
    /**
     * /**
     *
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     * @throws DaoException
     */
    @Override
    public JSONData getAllTenderInfo(String companyId, String storeId) throws DaoException {
        String functionName = "getAllTenderInfo";
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData tender = new JSONData();
        HashMap<String, JSONArray> tenderList = new HashMap<String, JSONArray>();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-tenderinfo-alltype"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, companyId);
            resultSet = selectStmnt.executeQuery();

            JSONObject tenderInfo = null;
            JSONObject labelValue = null;
            JSONArray erlectronicList = null;
            while (resultSet.next()) {
                tenderInfo = new JSONObject();
                tenderInfo.put("companyId", resultSet.getString("CompanyId"));
                tenderInfo.put("storeId", storeId);
                tenderInfo.put("tenderType", resultSet.getString("TenderType"));
                tenderInfo.put("tenderId", resultSet.getString("TenderId"));
                tenderInfo.put("code", resultSet.getString("TenderId"));
                labelValue = new JSONObject();
                labelValue.put("ja", resultSet.getString("TenderName"));
                labelValue.put("en", resultSet.getString("TenderKanaName"));
                tenderInfo.put("label", labelValue);
                tenderInfo.put("stampType", resultSet.getString("StampType"));
                tenderInfo.put("pointType", resultSet.getString("PointType"));
                tenderInfo.put("changeType", resultSet.getString("ChangeType"));
                tenderInfo.put("tenderIdentification", resultSet.getString("TenderIdentification"));
                tenderInfo.put("unitPrice", resultSet.getString("UnitPrice"));
                tenderInfo.put("tranVoid", resultSet.getString("SubNum1"));
                tenderInfo.put("tranReturn", resultSet.getString("SubNum2"));
                tenderInfo.put("tenderVoid", resultSet.getString("SubNum3"));
                tenderInfo.put("displayOrder", resultSet.getString("DisplayOrder"));

                getCouponPriceInfo(storeId, tenderInfo, companyId);
                if (tenderList.containsKey(resultSet.getString("TenderType"))) {
                    tenderList.get(resultSet.getString("TenderType")).add(tenderInfo);
                } else {
                    erlectronicList = new JSONArray();
                    erlectronicList.add(tenderInfo);
                    tenderList.put(resultSet.getString("TenderType"), erlectronicList);
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
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get tender infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerTenderInfoDAO.getTenderInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get tender infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerTenderInfoDAO.getTenderInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(tender);
        }
        return tender;
    }

    /**
     * åîóﬁ(ã‡éÌï )ÇÃéÌóﬁÅ^íPâøèÓïÒÇéÊìæÇ∑ÇÈ
     * @param storeid
     * @param tenderInfo
     * @param companyId The ID of the companyId
     * @throws DaoException   Exception thrown when the failed.
     */
	private void getCouponPriceInfo(final String storeId, final JSONObject tenderInfo, final String companyId)
			throws DaoException {
		tp.methodEnter("getCouponPriceInfo").println("StoreID", storeId)
				.println("tenderInfo", tenderInfo).println("CompanyId", companyId);

		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		String functionName = "SQLServerTenderInfoDAO.getCouponPriceInfo";
		try {
			String tenderId = tenderInfo.getString("tenderId").toString();
			String tenderType = tenderInfo.getString("tenderType").toString();
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("get-coupon-price-info"));
			select.setString(SQLStatement.PARAM1, companyId);
			select.setString(SQLStatement.PARAM2, storeId);
			select.setString(SQLStatement.PARAM3, tenderId);
			select.setString(SQLStatement.PARAM4, tenderType);
			result = select.executeQuery();

			JSONArray couponPriceList = null;
			JSONObject couponPriceInfo = null;
			while(result.next()){
				if (couponPriceList == null) {
					couponPriceList = new JSONArray();
				}
				couponPriceInfo = new JSONObject();
				couponPriceInfo.put("nameText", result.getString("NameText"));
				couponPriceInfo.put("unitPrice", result.getString("UnitPrice"));
				couponPriceList.add(couponPriceInfo);
			}
			tenderInfo.put("couponPriceList", couponPriceList);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the prom information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getCouponPriceInfo ", sqlEx);
		} catch (NumberFormatException nuEx) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_PARSE,
					"Failed to get the prom information.\n" + nuEx.getMessage());
			throw new DaoException("NumberFormatException: @getCouponPriceInfo ", nuEx);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get the prom information.\n" + e.getMessage());
			throw new DaoException("Exception: @getCouponPriceInfo ", e);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(tenderInfo);
		}
	}

    @Override
    public JSONData getTenderInfoByType(String companyId, String storeId, String tenderType, String tenderId)
            throws DaoException {
        String functionName = "getTenderInfoByType";
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId).println("TenderType",
                tenderType).println("TenderId", tenderId);
        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        JSONObject tenderInfo = null;
        JSONData tender = new JSONData();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-tender-info"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, companyId);
            selectStmnt.setString(SQLStatement.PARAM3, tenderType);
            selectStmnt.setString(SQLStatement.PARAM4, tenderId);
            resultSet = selectStmnt.executeQuery();
            if (resultSet.next()) {
                tenderInfo = new JSONObject();
                tenderInfo.put("companyId", resultSet.getString("CompanyId"));
                tenderInfo.put("storeId", resultSet.getString("StoreId"));
                tenderInfo.put("tenderType", resultSet.getString("TenderType"));
                tenderInfo.put("tenderId", resultSet.getString("TenderId"));
                tenderInfo.put("tenderName", resultSet.getString("TenderName"));
                tenderInfo.put("tenderKanaName", resultSet.getString("TenderKanaName"));

                tenderInfo.put("stampType", resultSet.getString("StampType"));
                tenderInfo.put("pointType", resultSet.getString("PointType"));
                tenderInfo.put("changeType", resultSet.getString("ChangeType"));
                tenderInfo.put("unitPrice", resultSet.getString("UnitPrice"));
                tenderInfo.put("displayOrder", resultSet.getString("DisplayOrder"));

            }else{
                tender.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                tender.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return tender;
            }
            tender.setJsonObject(tenderInfo.toString());
            tender.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            tender.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            tender.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get tender infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerTenderInfoDAO.getTenderInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get tender infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerTenderInfoDAO.getTenderInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(tender);
        }
        return tender;
    }
}