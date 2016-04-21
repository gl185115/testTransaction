package ncr.res.mobilepos.barcode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.xebioapi.model.JSONData;

public class SQLServerBarCodeDAO extends AbstractDao implements IBarCodeDAO{
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
    private static final String PROG_NAME = "BarCodeDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    
    private static final int MEMBERCARD13 = 13;

    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerBarCodeDAO() throws Exception {
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
     * 
     * @param companyId
     * @param storeId
     * @param cardType
     * @param seqNo
     * @param discountType
     * @return barcodeInfo
     * @throws DaoException
     */
    @Override
    public JSONData getDiscountInfo(String companyId, String storeId, String cardType, String seqNo, String discountType)
 throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("CardType", cardType).println("SeqNo", seqNo).println("DiscountType", discountType);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData barcodeInfo = new JSONData();
        List<JSONObject> valueList = null;
        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-barcode-type"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, companyId);
            selectStmnt.setString(SQLStatement.PARAM3, cardType);
            selectStmnt.setString(SQLStatement.PARAM4, seqNo);
            selectStmnt.setString(SQLStatement.PARAM5, discountType);
            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                if (valueList == null) {
                    valueList = new ArrayList<JSONObject>();
                }
                JSONObject valueResult = new JSONObject();
                valueResult.put("companyId", resultSet.getString("CompanyId"));
                valueResult.put("storeId", resultSet.getString("StoreId"));
                valueResult.put("cardType", resultSet.getString("CardType"));
                valueResult.put("seqNo", resultSet.getString("SeqNo"));
                valueResult.put("subNo", resultSet.getString("SubNo"));
                valueResult.put("discountType", resultSet.getString("DiscountType"));
                valueResult.put("discountClass", resultSet.getString("DiscountClass"));
                valueResult.put("dpt", resultSet.getString("Dpt"));
                valueResult.put("mdInternal", resultSet.getString("MdInternal"));
                valueResult.put("discountRefType", resultSet.getString("DiscountRefType"));
                valueResult.put("discountValue", resultSet.getString("DiscountValue"));
                valueResult.put("startDate", resultSet.getString("StartDate"));
                valueResult.put("endDateRefType", resultSet.getString("EndDateRefType"));
                valueResult.put("endDate", resultSet.getString("EndDate"));
                valueResult.put("entryDate", resultSet.getString("EntryDate"));
                valueResult.put("discountName", resultSet.getString("DiscountName"));
                valueList.add(valueResult);
            }
            if (valueList == null) {
                barcodeInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                barcodeInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                barcodeInfo.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return barcodeInfo;
            }
            barcodeInfo.setJsonObject(valueList.toString());
            barcodeInfo.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            barcodeInfo.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            barcodeInfo.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get Discount infomation.", sqlStmtEx);
            throw new DaoException("SQLStatementException:" + " @SQLServerBarCodeDAO.getDiscountInfo", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Discount infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerBarCodeDAO.getDiscountInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Discount infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerBarCodeDAO.getDiscountInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(barcodeInfo);
        }
        return barcodeInfo;
    }

    @Override
    public JSONObject isMemberCard(String cardCode) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("cardCode", cardCode);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        JSONObject flagArray = new JSONObject();
        boolean flag = false;
        boolean newRegistFlag = false;
        try {
            connection = dbManager.getConnection();
           
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-isMemberCard-info"));
            selectStmnt.setString(SQLStatement.PARAM1, cardCode);

            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                if(!StringUtility.isNullOrEmpty(cardCode)) {
                    if(cardCode.length() == MEMBERCARD13) {
                        if(cardCode.startsWith(resultSet.getString("PrefixCode13"))){
                        	newRegistFlag = "1".equals(resultSet.getString("NewRegistFlag"));
                            flag = true;
                            flagArray.put("cardTypeId",resultSet.getString("CardTypeId"));
                            flagArray.put("subCode2",resultSet.getString("SubCode2"));
                            flagArray.put("subCode3",resultSet.getString("SubCode3"));
                            break;
                        }
                    } else {
                        if(cardCode.startsWith(resultSet.getString("PrefixCode16From")) 
                                || cardCode.startsWith(resultSet.getString("PrefixCode16To"))){
                        	newRegistFlag = "1".equals(resultSet.getString("NewRegistFlag"));
                            flag = true;
                            flagArray.put("cardTypeId",resultSet.getString("CardTypeId"));
                            flagArray.put("subCode2",resultSet.getString("SubCode2"));
                            flagArray.put("subCode3",resultSet.getString("SubCode3"));
                            break;
                        } 
                    }
              }
                                
            }
            flagArray.put("flag",flag);
            flagArray.put("newRegistFlag",newRegistFlag);
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get Discount infomation.", sqlStmtEx);
            throw new DaoException("SQLStatementException:" + " @SQLServerBarCodeDAO.getDiscountInfo", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Discount infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerBarCodeDAO.getDiscountInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get Discount infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerBarCodeDAO.getDiscountInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(flagArray);
        }
        return flagArray;
    }
}
