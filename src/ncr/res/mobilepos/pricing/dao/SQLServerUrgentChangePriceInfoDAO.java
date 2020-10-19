package ncr.res.mobilepos.pricing.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.model.UrgentChangeItemInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerUrgentChangePriceInfoDAO extends AbstractDao implements IUrgentChangePriceInfoDAO {
	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "UrgentChangePriceInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerUrgentChangePriceInfoDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerUrgentChangePriceInfoDAO() throws DaoException {
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
     * Get UrgentChangePriceInfo from MST_PLU,MST_PLU_URGENT,MST_PRICE_PROM_INFO,MST_PRICE_PROM_DETAIL and MST_PRICE_PROM_STORE.
     * @param  companyId,
	 *         storeId ,
	 *         mdInternal ,
	 *         bizDate ,
	 *         bizDateTime
     * @return UrgentChangeItemInfo
     * @throws DaoException Exception when error occurs.
     */
	@Override
	public UrgentChangeItemInfo getUrgentChangePriceInfo(String companyId,String storeId,String mdInternal,String bizDate,String bizDateTime)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
        tp.println("storeId", storeId);
        tp.println("mdInternal", mdInternal);
        tp.println("bizDate", bizDate);
        tp.println("bizDateTime", bizDateTime);

        UrgentChangeItemInfo urgentChangeItemInfo = null;
        try (Connection connection = dbManager.getConnection();
              PreparedStatement statement = prepareGetUrgentChangeItemInfo(companyId, storeId, mdInternal, bizDate, bizDateTime, connection);
              ResultSet result = statement.executeQuery();)
        {
            while(result.next()){
            	urgentChangeItemInfo = new UrgentChangeItemInfo();
            	
            	urgentChangeItemInfo.setmdName(result.getString(result.findColumn("MdNameLocal")));
            	urgentChangeItemInfo.setSalesPrice1(result.getDouble(result.findColumn("SalesPrice1")));
            	if(result.getObject("UrgentPrice") != null) {
            		urgentChangeItemInfo.setUrgentPrice(result.getDouble(result.findColumn("UrgentPrice")));
            	}else if(result.getObject("PriceFromSalesPrice") != null) {
            		urgentChangeItemInfo.setUrgentPrice(result.getDouble(result.findColumn("PriceFromSalesPrice")));
            	}else {
            		urgentChangeItemInfo.setUrgentPrice(result.getDouble(result.findColumn("SalesPrice1")));
            	}
            	urgentChangeItemInfo.setDptCode(result.getString(result.findColumn("DptMst")));
            	urgentChangeItemInfo.setClassCode(result.getString(result.findColumn("LineMst")));
            	urgentChangeItemInfo.setCategoryCode(result.getString(result.findColumn("ClassMst")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the UrgentChangeItem information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getUrgentChangePriceInfo ", sqlEx);
        } finally {
        	tp.methodExit(urgentChangeItemInfo);
        }
        return urgentChangeItemInfo;
	}

    protected PreparedStatement prepareGetUrgentChangeItemInfo(String companyId,String storeId,String mdInternal,String bizDate,String bizDateTime,Connection connection
                                                            ) throws SQLException {
        // Creates PreparedStatement .
    	SQLStatement sqlStatement = SQLStatement.getInstance();
        PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-price-urgentPrice-promPrice"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, mdInternal);
        statement.setString(SQLStatement.PARAM4, bizDate);
        statement.setString(SQLStatement.PARAM5, bizDateTime);
        return statement;
    }
}
