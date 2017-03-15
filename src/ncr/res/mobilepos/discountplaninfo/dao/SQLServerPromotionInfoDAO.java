package ncr.res.mobilepos.discountplaninfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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

public class SQLServerPromotionInfoDAO extends AbstractDao implements IPromotionInfoDAO {
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
    private static final String PROG_NAME = "PromotionInfoDAO";
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
    public SQLServerPromotionInfoDAO() throws DaoException {
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
     * @param discountReason
     * @param discountBarcodeType
     * @param partialFlag
     * @param priceDiscountFlag
     * @param rateDiscountFlag
     * @return promotion
     * @throws DaoException
     */
    @Override
    public JSONData getPromotionInfo(String companyId, String storeId, String discountReason,
            String discountBarcodeType, String partialFlag, String priceDiscountFlag, String rateDiscountFlag)
                    throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId)
                .println("DiscountReason", discountReason).println("DiscountBarcodeType", discountBarcodeType)
                .println("PartialFlag", partialFlag).println("PriceDiscountFlag", priceDiscountFlag)
                .println("RateDiscountFlag", rateDiscountFlag);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;

        JSONData promotion = new JSONData();
        JSONObject promotioninfo = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-promotioninfo-type"));
            selectStmnt.setString(SQLStatement.PARAM1, companyId);
            selectStmnt.setString(SQLStatement.PARAM2, storeId);
            selectStmnt.setString(SQLStatement.PARAM3, discountReason);
            selectStmnt.setString(SQLStatement.PARAM4, discountBarcodeType);
            selectStmnt.setString(SQLStatement.PARAM5, partialFlag);
            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                if ((priceDiscountFlag.indexOf(resultSet.getString("PriceDiscountFlag")) > -1)
                        && (rateDiscountFlag.indexOf(resultSet.getString("RateDiscountFlag")) > -1)) {
                    promotioninfo = new JSONObject();
                    promotioninfo.put("companyId", resultSet.getString("CompanyId"));
                    promotioninfo.put("storeId", resultSet.getString("StoreId"));
                    promotioninfo.put("promotionId", resultSet.getString("PromotionId"));
                    promotioninfo.put("promotionName", resultSet.getString("PromotionName"));
                    promotioninfo.put("receiptName", resultSet.getString("ReceiptName"));
                    promotioninfo.put("discountReason", resultSet.getString("DiscountReason"));
                    promotioninfo.put("discountReasonName", resultSet.getString("DiscountReasonName"));
                    promotioninfo.put("promotionType", resultSet.getString("PromotionType"));
                    promotioninfo.put("discountType", resultSet.getString("DiscountType"));
                    promotioninfo.put("inputFlag", resultSet.getString("InputFlag"));
                    promotioninfo.put("partialFlag", resultSet.getString("PartialFlag"));
                    promotioninfo.put("rateDiscountFlag", resultSet.getString("RateDiscountFlag"));
                    promotioninfo.put("priceDiscountFlag", resultSet.getString("PriceDiscountFlag"));
                    promotioninfo.put("buttonFlag", resultSet.getString("ButtonFlag"));
                    promotioninfo.put("barcodeFlag", resultSet.getString("BarcodeFlag"));
                    promotioninfo.put("barcodeDigit", resultSet.getString("BarcodeDigit"));
                    promotioninfo.put("barcodeStartCode", resultSet.getString("BarcodeStartCode"));
                    promotioninfo.put("barcodeEndCode", resultSet.getString("BarcodeEndCode"));
                    promotioninfo.put("barcodeIdCode1", resultSet.getString("BarcodeIdCode1"));
                    promotioninfo.put("barcodeIdStartIndex1", resultSet.getString("BarcodeIdStartIndex1"));
                    promotioninfo.put("barcodeIdStartDigit1", resultSet.getString("BarcodeIdStartDigit1"));
                    promotioninfo.put("barcodeIdCode2", resultSet.getString("BarcodeIdCode2"));
                    promotioninfo.put("barcodeIdStartIndex2", resultSet.getString("BarcodeIdStartIndex2"));
                    promotioninfo.put("barcodeIdStartDigit2", resultSet.getString("BarcodeIdStartDigit2"));
                    promotioninfo.put("pointCombinationUseFlag", resultSet.getString("PointCombinationUseFlag"));
                    promotioninfo.put("pointAddFlag", resultSet.getString("PointAddFlag"));
                    promotioninfo.put("discountBarcodeType", resultSet.getString("DiscountBarcodeType"));
                    break;
                }
            }
            if (promotioninfo == null) {
                promotion.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                promotion.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                promotion.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return promotion;
            }
            promotion.setJsonObject(promotioninfo.toString());
            promotion.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            promotion.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            promotion.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get promotion infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerPromotionDAO.getpromotionInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get promotion infomation.",
                    ex);
            throw new DaoException("Exception:" + " @SQLServerPromotionDAO.getpromotionInfo", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(promotion);
        }
        return promotion;
    }
}