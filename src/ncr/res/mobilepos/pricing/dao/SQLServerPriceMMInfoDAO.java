package ncr.res.mobilepos.pricing.dao;

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
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPriceMMInfoDAO extends AbstractDao implements IPriceMMInfoDAO {
	/**
	 * The Database Manager of the class.
	 */
	private DBManager dbManager;
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
	/**
	 * The Program name.
	 */
	private String progname = "PriceMMInfoDAO";
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	/**
	 * Default Constructor for SQLServerPriceMMInfoDAO
	 *
	 * <P>
	 * Sets DBManager for database connection, and Logger for logging.
	 *
	 * @throws DaoException
	 *			 The exception thrown when the constructor fails.
	 */
	public SQLServerPriceMMInfoDAO() throws DaoException {
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
	 * Get PriceMMInfo from MST_PRICE_MM_INFO,MST_PRICE_MM_DETAIL and MST_PRICE_MM_STORE.
	 * @param companyId,
	 *		storeId,
	 *		dayDate
	 * @return PriceMMList
	 * @throws DaoException Exception when error occurs.
	 */
	@Override
	public List<PriceMMInfo> getPriceMMInfoList(String companyId, String storeId, String dayDate)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.println("CompanyId", companyId);
		tp.println("storeId", storeId);
		tp.println("dayDate", dayDate);

		List<PriceMMInfo> pricePromList = new ArrayList<PriceMMInfo>();
		try (Connection connection = dbManager.getConnection();
				PreparedStatement statement = prepareGetPriceMMInfoList(companyId, storeId, dayDate, connection);
				ResultSet result = statement.executeQuery();)
		{
			PriceMMInfo priceMMInfo = null;
			while(result.next()){
				priceMMInfo = new PriceMMInfo();
				priceMMInfo.setMMNo(result.getString(result.findColumn("MMNo")));
				priceMMInfo.setConditionCount1(result.getInt(result.findColumn("ConditionCount1")));
				priceMMInfo.setConditionCount2(result.getInt(result.findColumn("ConditionCount2")));
				priceMMInfo.setConditionCount3(result.getInt(result.findColumn("ConditionCount3")));
				
				priceMMInfo.setConditionPrice3(result.getDouble(result.findColumn("ConditionPrice3")));
				priceMMInfo.setConditionPrice2(result.getDouble(result.findColumn("ConditionPrice2")));
				priceMMInfo.setConditionPrice1(result.getDouble(result.findColumn("ConditionPrice1")));
				
				if(result.getObject(result.findColumn("DecisionPrice1")) != null ){
					priceMMInfo.setDecisionPrice1(result.getDouble(result.findColumn("DecisionPrice1")));
				}
				
				if(result.getObject(result.findColumn("DecisionPrice2")) != null ){
					priceMMInfo.setDecisionPrice2(result.getDouble(result.findColumn("DecisionPrice2")));
				}
				
				if(result.getObject(result.findColumn("DecisionPrice3")) != null ){
					priceMMInfo.setDecisionPrice3(result.getDouble(result.findColumn("DecisionPrice3")));
				}

				priceMMInfo.setAveragePrice1(result.getDouble(result.findColumn("AveragePrice1")));
				priceMMInfo.setAveragePrice2(result.getDouble(result.findColumn("AveragePrice2")));
				priceMMInfo.setAveragePrice3(result.getDouble(result.findColumn("AveragePrice3")));
				
				priceMMInfo.setNote(result.getString(result.findColumn("Note")));
				priceMMInfo.setSku(result.getString(result.findColumn("Sku")));
				pricePromList.add(priceMMInfo);

			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the PriceMM information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPriceMMInfo ", sqlEx);
		} finally {
			tp.methodExit(pricePromList);
		}
		return pricePromList;
	}

	protected PreparedStatement prepareGetPriceMMInfoList(String companyId,String storeId,String businessDate,Connection connection
															) throws SQLException {
		// Creates PreparedStatement .
		SQLStatement sqlStatement = SQLStatement.getInstance();
		PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-price-mm-info-list"));
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, storeId);
		statement.setString(SQLStatement.PARAM3, businessDate);
		return statement;
	}
}
