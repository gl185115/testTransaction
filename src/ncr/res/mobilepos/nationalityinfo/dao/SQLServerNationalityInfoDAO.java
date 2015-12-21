package ncr.res.mobilepos.nationalityinfo.dao;

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
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.nationalityinfo.model.NationalityInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerNationalityInfoDAO extends AbstractDao implements INationalityInfoDAO {
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
	private static final String PROG_NAME = "NationalityInfoDAO";
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
	public SQLServerNationalityInfoDAO() throws Exception {
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
	 * get nationality info
	 * 
	 * @return nationality info
	 * @throws DaoException
	 *             Exception when the method fail.
	 */
	@Override
	public List<NationalityInfo> getNationalityInfo(String companyId, String storeId) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		List<NationalityInfo> nationalityInfoList = new ArrayList<NationalityInfo>();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			statement = connection.prepareStatement(sqlStatement.getProperty("get-nationality-info-all"));

			statement.setString(SQLStatement.PARAM1, companyId);
			statement.setString(SQLStatement.PARAM2, storeId);
			result = statement.executeQuery();

			while (result.next()) {
				NationalityInfo nationalityInfo = new NationalityInfo();
				nationalityInfo.setCompanyId(result.getString("CompanyId"));
				nationalityInfo.setStoreId(result.getString("StoreId"));
				nationalityInfo.setId(result.getString("NationalityId"));
				nationalityInfo.setName(result.getString("NationalityName"));
				nationalityInfo.setKanaName(result.getString("NationalityKanaName"));
				nationalityInfo.setShortName(result.getString("NationalityShortName"));
				nationalityInfo.setShortKanaName(result.getString("NationalityShortKanaName"));
				nationalityInfoList.add(nationalityInfo);
			}
		} catch (SQLStatementException sqlStmtEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to get nationality info.", sqlStmtEx);
			throw new DaoException("SQLStatementException: @SQLServerNationalityInfoDAO.getNationalityInfo", sqlStmtEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get nationality info.", sqlEx);
			throw new DaoException("SQLException: @SQLServerNationalityInfoDAO.getNationalityInfo", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get nationality info.",
					ex);
			throw new DaoException("Exception: @SQLServerNationalityInfoDAO.getNationalityInfo", ex);
		} finally {
			closeConnectionObjects(connection, statement, result);
			tp.methodExit(nationalityInfoList);
		}
		return nationalityInfoList;
	}

}
