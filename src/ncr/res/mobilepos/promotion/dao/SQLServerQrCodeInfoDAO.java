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
	public final List<QrCodeInfo> getkinokuniyaQrCodeInfo(String companyId, String storeId, String dayDate) throws DaoException {
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
            select = connection.prepareStatement(sqlStatement.getProperty("get-kinokuniya-qrcode-info"));
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
                codeInfo.setPromotionId(result.getString(result.findColumn("QRPromotionId")));
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
}
