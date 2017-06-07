package ncr.res.mobilepos.promotion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerQrCodeInfoDAO extends AbstractDao implements IQrCodeInfoDAO {
	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "QrCodeInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    
    /**
     * Default Constructor for SQLServerMixMatchDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerQrCodeInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for SQLServerItemDAO.
     *
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Get PromotionId from MST_QRCODE_INFO and MST_QRCODE_STORE
     * @param companyId,
	 *        storeId,
	 *        dayDate   
     * @return QrCodeList
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final List<QrCodeInfo> getQrCodeInfoList(String companyId, String storeId, String dayDate) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
        tp.println("storeId", storeId);
        tp.println("dayDate", dayDate);
        
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        
        List<QrCodeInfo> QrCodeList = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-qrcode-info-list"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, dayDate);
            result = select.executeQuery();
            
            QrCodeInfo codeInfo = null;
            while(result.next()){
                if (QrCodeList == null){
                    QrCodeList = new ArrayList<QrCodeInfo>();
                }
                codeInfo = new QrCodeInfo();
                codeInfo.setPromotionId(result.getString(result.findColumn("PromotionId")));
                codeInfo.setPromotionName(result.getString(result.findColumn("PromotionName")));
                if(null != result.getObject(result.findColumn("MinimumPrice"))){
                    codeInfo.setMinimumPrice(result.getDouble(result.findColumn("MinimumPrice")));
                }else{
                    codeInfo.setMinimumPrice(null);
                }
                codeInfo.setOutputTargetValue(result.getString(result.findColumn("OutputTargetValue")));
                codeInfo.setBmpFileName(result.getString(result.findColumn("BMPFileName")));
                codeInfo.setBmpFileFlag(result.getString(result.findColumn("BmpFileFlag")));
                codeInfo.setBmpFileCount(result.getString(result.findColumn("BmpFileCount")));
                codeInfo.setOutputType(result.getString(result.findColumn("OutputType")));
                codeInfo.setDisplayOrder(result.getString(result.findColumn("DisplayOrder")));
                codeInfo.setPromotionType(result.getString(result.findColumn("promotionType")));
                codeInfo.setDpt(result.getString(result.findColumn("Dpt")));
                codeInfo.setLine(result.getString(result.findColumn("Line")));
                codeInfo.setClassCode(result.getString(result.findColumn("Class")));
                codeInfo.setSku(result.getString(result.findColumn("Sku")));
                codeInfo.setConnCode(result.getString(result.findColumn("ConnCode")));
                codeInfo.setBrandId(result.getString(result.findColumn("BrandId")));
                codeInfo.setMemberRank(result.getString(result.findColumn("MemberRank")));
                codeInfo.setMemberTargetType(result.getString(result.findColumn("MemberTargetType")));
                codeInfo.setSexType(result.getString(result.findColumn("SexType")));
                codeInfo.setBirthMonth(result.getString(result.findColumn("BirthMonth")));
                QrCodeList.add(codeInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the qrcode information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getQrCodeInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the qrcode information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getQrCodeInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the qrcode information.\n" + e.getMessage());
            throw new DaoException("Exception: @getQrCodeInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(QrCodeList);
        }
        
        return QrCodeList;
    }
    
    /**
     * Get CustomerId from MST_QRCODE_MEMBERID
     * @param promotionId,
	 *        customerId
     * @return rightCustomerId
     * @throws DaoException Exception when error occurs.
     */
    public final String getCustomerQrCodeInfoList(String companyId, String promotionId, String customerId) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
    	tp.println("PromotionId", promotionId);
        tp.println("CustomerId", customerId);
        
    	Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        
        String rightCustomerId = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("is-member-on-promotion"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, promotionId);
            select.setString(SQLStatement.PARAM3, customerId);
            result = select.executeQuery();
            
            while(result.next()){
                rightCustomerId = result.getString(result.findColumn("MemberId"));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the customerId.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getCustomerQrCodeInfo ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the customerId.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getCustomerQrCodeInfo ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the customerId.\n" + e.getMessage());
            throw new DaoException("Exception: @getCustomerQrCodeInfo ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(rightCustomerId);
        }
    	return rightCustomerId;
    }
}
