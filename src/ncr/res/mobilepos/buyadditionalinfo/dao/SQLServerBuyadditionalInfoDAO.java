package ncr.res.mobilepos.buyadditionalinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.buyadditionalinfo.model.BuyadditionalInfo;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerBuyadditionalInfoDAO extends AbstractDao implements IBuyadditionalInfoDAO {
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
	private static final String PROG_NAME = "BuyadditionalInfoDAO";
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
	public SQLServerBuyadditionalInfoDAO() throws Exception {
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
	 * get buy additional info
	 *
	 * @return buy additional info
	 * @throws DaoException
	 *             Exception when the method fail.
	 */
	@Override
	public List<BuyadditionalInfo> getBuyadditionalInfo(String companyId, String storeId) throws DaoException {
		String functionName = "getBuyadditionalInfo";
		tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		List<BuyadditionalInfo> buyadditionalInfoList = new ArrayList<BuyadditionalInfo>();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			statement = connection.prepareStatement(sqlStatement.getProperty("get-buyadditional-info-all"));

			statement.setString(SQLStatement.PARAM1, companyId);
			statement.setString(SQLStatement.PARAM2, storeId);
			result = statement.executeQuery();

			while (result.next()) {
				BuyadditionalInfo buyadditionalInfo = new BuyadditionalInfo();
				buyadditionalInfo.setCompanyId(result.getString("CompanyId"));
				buyadditionalInfo.setStoreId(result.getString("StoreId"));
				buyadditionalInfo.setId(result.getString("BuyAdditionalInfoId"));
				buyadditionalInfo.setName(result.getString("BuyAdditionalInfoName"));
				buyadditionalInfo.setKanaName(result.getString("BuyAdditionalInfoKanaName"));
				buyadditionalInfo.setShortName(result.getString("BuyAdditionalInfoShortName"));
				buyadditionalInfo.setShortKanaName(result.getString("BuyAdditionalInfoShortKanaName"));
				buyadditionalInfoList.add(buyadditionalInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get buyadditional info.",
					sqlEx);
			throw new DaoException("SQLException: @SQLServerBuyadditionalInfoDAO.getBuyadditionalInfo", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get buyadditional info.",
					ex);
			throw new DaoException("Exception: @SQLServerBuyadditionalInfoDAO.getBuyadditionalInfo", ex);
		} finally {
			closeConnectionObjects(connection, statement, result);
			tp.methodExit(buyadditionalInfoList);
		}
		return buyadditionalInfoList;
	}

}
