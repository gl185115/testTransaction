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
import ncr.res.mobilepos.pricing.model.PriceUrgentInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPriceUrgentInfoDAO extends AbstractDao implements IPriceUrgentInfoDAO {
	/**
	 * The Database Manager of the class.
	 */
	private DBManager dbManager;
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
	/**
	 * The Program name.
	 */
	private String progname = "PriceUrgentInfoDAO";
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	/**
	 * Default Constructor for SQLServerPricePromInfoDAO
	 *
	 * <P>
	 * Sets DBManager for database connection, and Logger for logging.
	 *
	 * @throws DaoException The exception thrown when the constructor fails.
	 */
	public SQLServerPriceUrgentInfoDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
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
	 * Get PriceUrgentInfo from MST_PLU_URGENT.
	 * 
	 * @param companyId, storeId , mdInternal
	 * @return priceUrgentList
	 * @throws DaoException Exception when error occurs.
	 */
	@Override
	public List<PriceUrgentInfo> getPriceUrgentInfoList(String companyId, String storeId, String mdInternal) throws DaoException {
		String functionName = "getPriceUrgentInfoList";
		tp.println("CompanyId", companyId);
		tp.println("storeId", storeId);
		tp.println("dayDate", mdInternal);

		List<PriceUrgentInfo> priceUrgentList = new ArrayList<PriceUrgentInfo>();
		try (Connection connection = dbManager.getConnection();
				PreparedStatement statement = prepareGetPriceUrgentInfoList(companyId, storeId, mdInternal, connection);
				ResultSet result = statement.executeQuery();) {
			PriceUrgentInfo priceUrgentInfo = null;
			while (result.next()) {
				priceUrgentInfo = new PriceUrgentInfo();
				priceUrgentInfo.setDpt(result.getString(result.findColumn("Dpt")));
				priceUrgentInfo.setLine(result.getString(result.findColumn("Line")));
				priceUrgentInfo.setClas(result.getString(result.findColumn("Class")));
				priceUrgentInfo.setSku(result.getString(result.findColumn("Sku")));
				priceUrgentInfo.setUrgentPrice(result.getLong(result.findColumn("UrgentPrice")));
				priceUrgentList.add(priceUrgentInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get the riceUrgent information.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPriceUrgentInfoList", sqlEx);
		} finally {
			tp.methodExit(priceUrgentList);
		}
		return priceUrgentList;
	}

	protected PreparedStatement prepareGetPriceUrgentInfoList(String companyId, String storeId, String mdInternal, Connection connection) throws SQLException {
		// Creates PreparedStatement .
		SQLStatement sqlStatement = SQLStatement.getInstance();
		PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-price-urgent-info"));
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, storeId);
		statement.setString(SQLStatement.PARAM3, mdInternal);
		return statement;
	}
}
