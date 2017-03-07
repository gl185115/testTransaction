/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerStoreDAO
 *
 * Is a DAO Class for Store maintenance database manipulation.
 *
 */

package ncr.res.mobilepos.store.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

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
import ncr.res.mobilepos.store.model.CMPresetInfo;
import ncr.res.mobilepos.store.model.PresetSroreInfo;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.ViewStore;

/**
 * Access database for store CRUD manipulations.
 * 
 */
public class SQLServerStoreDAO extends AbstractDao implements IStoreDAO {
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
    private String progName = "StrDao";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    private static final int STATUS_UNKNOWN = 0;

    private static final int STATUS_ACTIVE = 1;

    private static final int STATUS_DELETED = 2;

    private static final int STORE_NOT_FOUND = -1;

    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerStoreDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves DBManager.
     * 
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    /*
     * view store details of storeid.
     * 
     * @see ncr.res.mobilepos.store.dao.IStoreDAO#viewStore(java.lang.String)
     */
    @Override
    public final ViewStore viewStore(final String retailStoreID)
            throws DaoException {

        String functionName = "SQLServerStoreDAO.viewStore";

        tp.methodEnter("viewStore");
        tp.println("RetailStoreID", retailStoreID);

        ViewStore storeData = new ViewStore();
        Store store = new Store();
        store.setRetailStoreID(retailStoreID);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-store-bystoreid"));

            select.setString(SQLStatement.PARAM1, retailStoreID);

            result = select.executeQuery();

            if (result.next()) {
                store.setRetailStoreID(result.getString("StoreId"));
                store.setStoreName(result.getString("StoreName"));
                store.setAddress(result.getString("StoreAddr"));
                store.setTel(result.getString("StoreTel"));
                store.setUrl(result.getString("StoreUrl"));
                store.setSalesSpaceName(result.getString("SalesSpaceName"));
                store.setEventName(result.getString("EventName"));
                store.setAds(result.getString("Ads"));
                store.setElectroFilePath(result.getString("ElectroFilePath"));
                store.setStampTaxFilePath(result.getString("StampTaxFilePath"));
            } else {
                storeData.setNCRWSSResultCode(ResultBase.RES_STORE_NOT_EXIST);
                tp.println("Store not found.");
            }

            storeData.setStore(store);

        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to View Store#" + retailStoreID + " : "
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @SQLServerStoreDAO"
                    + ".viewStore - Error view store", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to View Store#" + retailStoreID + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerStoreDAO"
                    + ".viewStore - Error view store", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(storeData);
        }

        return storeData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Store> listStores(final String companyId, final String key, 
    		final String name, final int limit) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("listStores")
        	.println("companyId", companyId)
        	.println("key", key)
			.println("name", name)
			.println("limit", limit);

        List<Store> storeList = new ArrayList<Store>();
        Connection conn = null;
        PreparedStatement selectStoresStmt = null;
        ResultSet resultset = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            selectStoresStmt = conn.prepareStatement(sqlStatement
                    .getProperty("get-stores"));
            tp.println("searchlimit", GlobalConstant.getMaxSearchResults());
            int searchLimit = (limit == 0) ? 
            		GlobalConstant.getMaxSearchResults() : limit;             
            selectStoresStmt.setString(SQLStatement.PARAM1, companyId);
            selectStoresStmt.setString(SQLStatement.PARAM2, key);
            selectStoresStmt.setString(SQLStatement.PARAM3, name);
            selectStoresStmt.setInt(SQLStatement.PARAM4, searchLimit);  
            resultset = selectStoresStmt.executeQuery();
            
            while (resultset.next()) {
                Store store = new Store();
                store.setCompanyId(resultset.getString("CompanyId"));
                store.setRetailStoreID(resultset.getString("StoreId"));
                store.setStoreName(resultset.getString("StoreName"));
                store.setAddress(resultset.getString("StoreAddr"));
                store.setTel(resultset.getString("StoreTel"));
                storeList.add(store);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to List Stores" + " : " + ex.getMessage());
            throw new DaoException("SQLException: @" + functionName
                    + " - Error list store", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to List Stores" + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error list store", ex);
        } finally {
            closeConnectionObjects(conn, selectStoresStmt, resultset);
            tp.methodExit(storeList.size());
        }
        return storeList;
    }

    /**
     * This is to get the current status of the Store 1 - Active 2 - Deleted -1
     * - Not Found
     * 
     * @param storeId
     * @return
     * @throws DaoException
     */
    public final int getStoreStatus(final String storeId) throws DaoException {
        String functionName = "@SQL" + "getStoreStatus";
        tp.methodEnter("getStoreStatus");
        tp.println("storeId", storeId);

        Connection connection = null;
        PreparedStatement select = null;
        PreparedStatement update = null;
        ResultSet result = null;
        int status = 0;

        try {
            status = STATUS_UNKNOWN;
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-store"));

            select.setString(SQLStatement.PARAM1, storeId);
            result = select.executeQuery();

            if (result.next()) {
                String res = result.getString(result.findColumn("Status"));
                if (res != null && "Deleted".equals(res)) {
                    status = STATUS_DELETED;
                } else if (res != null && "Active".equalsIgnoreCase(res)) {
                    status = STATUS_ACTIVE;
                }

            } else {
                tp.println("Store not found.");
                return STORE_NOT_FOUND;
            }

            connection.commit();
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to check Status of Department" + storeId + ": "
                            + ex.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerStoreDAO.getStoreStatus", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to check Status of Department" + storeId + ": "
                            + ex.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerStoreDAO.getStoreStatus", ex);
        } finally {
            closeConnectionObjects(null, select, result);
            closeConnectionObjects(connection, update);
            
            tp.methodExit(status);
        }

        return status;

    }    
    /**
     * {@inheritDoc}
     */
    @Override
    public final List<CMPresetInfo> listCMPresetInfo(final String companyId, final String storeId, 
            final String terminalId, final String businessDayDate) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("listCMPresetInfo")
            .println("companyId", companyId)
            .println("storeId", storeId)
            .println("terminalId", terminalId)
            .println("businessDayDate", businessDayDate);

        List<CMPresetInfo> cmPresetInfoList = new ArrayList<CMPresetInfo>();
        Connection conn = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            selectStmt = conn.prepareStatement(sqlStatement
                    .getProperty("get-preset-cm-info"));
           
            selectStmt.setString(SQLStatement.PARAM1, companyId);
            selectStmt.setString(SQLStatement.PARAM2, storeId);
            selectStmt.setString(SQLStatement.PARAM3, terminalId);
            selectStmt.setString(SQLStatement.PARAM4, businessDayDate);  
            resultSet = selectStmt.executeQuery();

            while (resultSet.next()) {
                CMPresetInfo cmPresetInfo = new CMPresetInfo();

                cmPresetInfo.setCompanyId(resultSet.getString("CompanyId"));
                cmPresetInfo.setCMId(resultSet.getInt("CMId"));
                cmPresetInfo.setCMName(resultSet.getString("CMName"));
                cmPresetInfo.setCMType(resultSet.getString("CMType"));
                cmPresetInfo.setBizCatId(resultSet.getString("BizCatId"));
                cmPresetInfo.setStoreId(resultSet.getString("StoreId"));
                cmPresetInfo.setTerminalId(resultSet.getString("TerminalId"));
                cmPresetInfo.setTop1Message(resultSet.getString("Top1Message"));
                cmPresetInfo.setTop2Message(resultSet.getString("Top2Message"));
                cmPresetInfo.setTop3Message(resultSet.getString("Top3Message"));
                cmPresetInfo.setTop4Message(resultSet.getString("Top4Message"));
                cmPresetInfo.setTop5Message(resultSet.getString("Top5Message"));
                cmPresetInfo.setTop6Message(resultSet.getString("Top6Message"));
                cmPresetInfo.setTop7Message(resultSet.getString("Top7Message"));
                cmPresetInfo.setTop8Message(resultSet.getString("Top8Message"));
                cmPresetInfo.setTop9Message(resultSet.getString("Top9Message"));
                cmPresetInfo.setTop10Message(resultSet.getString("Top10Message"));
                cmPresetInfo.setBottom1Message(resultSet.getString("Bottom1Message"));
                cmPresetInfo.setBottom2Message(resultSet.getString("Bottom2Message"));
                cmPresetInfo.setBottom3Message(resultSet.getString("Bottom3Message"));
                cmPresetInfo.setBottom4Message(resultSet.getString("Bottom4Message"));
                cmPresetInfo.setBottom5Message(resultSet.getString("Bottom5Message"));
                cmPresetInfo.setBottom6Message(resultSet.getString("Bottom6Message"));
                cmPresetInfo.setBottom7Message(resultSet.getString("Bottom7Message"));
                cmPresetInfo.setBottom8Message(resultSet.getString("Bottom8Message"));
                cmPresetInfo.setBottom9Message(resultSet.getString("Bottom9Message"));
                cmPresetInfo.setBottom10Message(resultSet.getString("Bottom10Message"));

                cmPresetInfoList.add(cmPresetInfo);
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to List PresetInfo" + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error list cm preset info", ex);
        } finally {
            closeConnectionObjects(conn, selectStmt, resultSet);
            tp.methodExit(cmPresetInfoList.size());
        }
        return cmPresetInfoList;
    }

    /**
     * get the PresetSroreInfo
     * @param companyId The Id of company
     * @param storeId The Id of Store
     * @param workStactionId  The Id of workStaction
     * @return PresetSroreInfo
     * @throws DaoException The Exception of Sql
     */
    public PresetSroreInfo getPresetSroreInfo(String companyId, String storeId, String workStactionId)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getPresetSroreInfo").println("companyId", companyId).println("storeId", storeId)
                .println("workStactionId", workStactionId);

        Connection conn = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        PresetSroreInfo presetSroreInfo = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            selectStmt = conn.prepareStatement(sqlStatement.getProperty("get-preset-storeinfo"));

            selectStmt.setString(SQLStatement.PARAM1, companyId);
            selectStmt.setString(SQLStatement.PARAM2, storeId);
            selectStmt.setString(SQLStatement.PARAM3, workStactionId);
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                presetSroreInfo = new PresetSroreInfo();
                if(null != resultSet.getString(resultSet.findColumn("ReceiptStoreName"))){
                    presetSroreInfo.setReceiptStoreName(resultSet.getString(resultSet.findColumn("ReceiptStoreName")));
                } else{
                    presetSroreInfo.setReceiptStoreName("");
                }
                presetSroreInfo.setReceiptTelNo(resultSet.getString(resultSet.findColumn("ReceiptTelNo")));
                presetSroreInfo
                        .setFormalReceiptStoreName(resultSet.getString(resultSet.findColumn("FormalReceiptStoreName")));
                presetSroreInfo.setFormalReceiptTelNo(resultSet.getString(resultSet.findColumn("FormalReceiptTelNo")));
                presetSroreInfo.setStoreName(resultSet.getString(resultSet.findColumn("StoreName")));
                presetSroreInfo.setStoreShortName(resultSet.getString(resultSet.findColumn("StoreShortName")));
                presetSroreInfo.setStoreKubun(resultSet.getString(resultSet.findColumn("StoreKubun")));
                presetSroreInfo.setStoreZip(resultSet.getString(resultSet.findColumn("StoreZip")));
                presetSroreInfo.setStoreAddr(resultSet.getString(resultSet.findColumn("StoreAddr")));
                presetSroreInfo.setStoreAddr1(resultSet.getString(resultSet.findColumn("StoreAddr1")));
                presetSroreInfo.setStoreAddr2(resultSet.getString(resultSet.findColumn("StoreAddr2")));
                presetSroreInfo.setStoreFax(resultSet.getString(resultSet.findColumn("StoreFax")));
                presetSroreInfo.setAds(resultSet.getString(resultSet.findColumn("Ads")));
                presetSroreInfo.setCdMsg(resultSet.getString(resultSet.findColumn("CdMsg")));
                presetSroreInfo.setElectroFilePath(resultSet.getString(resultSet.findColumn("ElectroFilePath")));
                presetSroreInfo.setStampTaxFilePath(resultSet.getString(resultSet.findColumn("StampTaxFilePath")));
                presetSroreInfo.setStoreCompCode(resultSet.getString(resultSet.findColumn("StoreCompCode")));
                presetSroreInfo.setSubCode1(resultSet.getString(resultSet.findColumn("SubCode1")));
                presetSroreInfo.setSubCode2(resultSet.getString(resultSet.findColumn("SubCode2")));
                presetSroreInfo.setSubCode3(resultSet.getString(resultSet.findColumn("SubCode3")));
                presetSroreInfo.setSubCode4(resultSet.getString(resultSet.findColumn("SubCode4")));
                presetSroreInfo.setSubCode5(resultSet.getString(resultSet.findColumn("SubCode5")));
                presetSroreInfo.setSubCode6(resultSet.getString(resultSet.findColumn("SubCode6")));
                presetSroreInfo.setSubCode7(resultSet.getString(resultSet.findColumn("SubCode7")));
                presetSroreInfo.setSubCode8(resultSet.getString(resultSet.findColumn("SubCode8")));
                presetSroreInfo.setSubCode9(resultSet.getString(resultSet.findColumn("SubCode9")));
                presetSroreInfo.setSubCode10(resultSet.getString(resultSet.findColumn("SubCode10")));
                presetSroreInfo.setSubCode11(resultSet.getString(resultSet.findColumn("SubCode11")));
                presetSroreInfo.setSubCode12(resultSet.getString(resultSet.findColumn("SubCode12")));
                presetSroreInfo.setSubCode13(resultSet.getString(resultSet.findColumn("SubCode13")));
                presetSroreInfo.setSubCode14(resultSet.getString(resultSet.findColumn("SubCode14")));
                presetSroreInfo.setSubCode16(resultSet.getString(resultSet.findColumn("SubCode16")));
                presetSroreInfo.setSubCode17(resultSet.getString(resultSet.findColumn("SubCode17")));
                presetSroreInfo.setSubNum1(resultSet.getString(resultSet.findColumn("SubNum1")));
                presetSroreInfo.setHostUpdDate(resultSet.getString(resultSet.findColumn("HostUpdDate")));
                presetSroreInfo.setStatus(resultSet.getString(resultSet.findColumn("Status")));
                presetSroreInfo.setCompanyName(resultSet.getString(resultSet.findColumn("CompanyName")));
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get PresetSroreInfo" + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName + " - Error get PresetSroreInfo ", ex);
        } finally {
            closeConnectionObjects(conn, selectStmt, resultSet);
            tp.methodExit(presetSroreInfo);
        }
        return presetSroreInfo;
    }
    @Override
    public String getSummaryReceiptNo(String companyId, String storeId, String workStactionId, String traning)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("getSummaryReceiptNo").println("companyId", companyId).println("storeId", storeId)
                .println("workStactionId", workStactionId).println("traning", traning);

        Connection conn = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        String subNum1 = "";

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            selectStmt = conn.prepareStatement(sqlStatement.getProperty("get-subNum1"));
           
            selectStmt.setString(SQLStatement.PARAM1, storeId);
            selectStmt.setString(SQLStatement.PARAM2, workStactionId);
            selectStmt.setString(SQLStatement.PARAM3, companyId);
            selectStmt.setString(SQLStatement.PARAM4, traning);
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                subNum1= resultSet.getString(resultSet.findColumn("SubNum1"));
            }
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get subnum1" + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName + " - Error get subnum1 ", ex);
        } finally {
            closeConnectionObjects(conn, selectStmt, resultSet);
            tp.methodExit(subNum1);
        }
        return subNum1;
    }

    @Override
    public int updateSummaryReceiptNo(int SubNum1, String companyId, String storeId, String workStactionId,
            String traning) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter("updateSummaryReceiptNo").println("SubNum1",SubNum1).println("companyId", companyId).println("storeId", storeId)
                .println("workStactionId", workStactionId).println("traning", traning);

        Connection conn = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        int result = 1;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            selectStmt = conn.prepareStatement(sqlStatement.getProperty("update-subNum1"));
            selectStmt.setInt(SQLStatement.PARAM1, SubNum1);
            selectStmt.setString(SQLStatement.PARAM2, storeId);
            selectStmt.setString(SQLStatement.PARAM3, workStactionId);
            selectStmt.setString(SQLStatement.PARAM4, companyId);
            selectStmt.setString(SQLStatement.PARAM5, traning);
            if (selectStmt.executeUpdate() != 1) {
                result = ResultBase.RESSYS_ERROR_QB_QUEUEFULL;
            }
            conn.commit();
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get subnum1" + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName + " - Error get subnum1 ", ex);
        } finally {
            closeConnectionObjects(conn, selectStmt, resultSet);
            tp.methodExit(result);
        }
        return result;
    }

    @Override
    public ViewStore getStoreDetaiInfo(String retailStoreID, String companyId) throws DaoException {
        String functionName = "SQLServerStoreDAO.getStoreDetaiInfo";

        tp.methodEnter("getStoreDetaiInfo");
        tp.println("RetailStoreID", retailStoreID);

        ViewStore storeData = new ViewStore();
        Store store = new Store();
        store.setRetailStoreID(retailStoreID);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-store-bystoreIdandcomnpanyId"));

            select.setString(SQLStatement.PARAM1, retailStoreID);
            select.setString(SQLStatement.PARAM2, companyId);

            result = select.executeQuery();

            if (result.next()) {
                store.setRetailStoreID(result.getString("StoreId"));
                store.setStoreName(result.getString("StoreName"));
                store.setAddress(result.getString("StoreAddr"));
                store.setTel(result.getString("StoreTel"));
                store.setUrl(result.getString("StoreUrl"));
                store.setSalesSpaceName(result.getString("SalesSpaceName"));
                store.setEventName(result.getString("EventName"));
                store.setAds(result.getString("Ads"));
            } else {
                storeData.setNCRWSSResultCode(ResultBase.RES_STORE_NOT_EXIST);
                tp.println("Store not found.");
            }

            storeData.setStore(store);

        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to View Store#" + retailStoreID + " : "
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @SQLServerStoreDAO"
                    + ".getStoreDetaiInfo - Error view store", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to View Store#" + retailStoreID + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerStoreDAO"
                    + ".getStoreDetaiInfo - Error view store", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(storeData);
        }

        return storeData;
    }
}
