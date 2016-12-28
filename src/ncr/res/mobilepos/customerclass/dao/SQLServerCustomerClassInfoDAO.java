package ncr.res.mobilepos.customerclass.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customerclass.model.CustomerClassInfo;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCustomerClassInfoDAO extends AbstractDao implements ICustomerClassInfoDAO {
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
	private static final String PROG_NAME = "CustomerInfo";
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
	public SQLServerCustomerClassInfoDAO() throws Exception {
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
	 * get customer class info
	 * 
	 * @return customer class info
	 * @throws DaoException
	 *             Exception when the method fail.
	 */
	@Override
	public List<CustomerClassInfo> getCustomerClassInfo(String companyId, String storeId) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("companyId", companyId).println("storeId", storeId);

		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		List<CustomerClassInfo> customerClassInfoList = new ArrayList<CustomerClassInfo>();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			statement = connection.prepareStatement(sqlStatement.getProperty("get-customer-class-info-all"));

			statement.setString(SQLStatement.PARAM1, companyId);
			statement.setString(SQLStatement.PARAM2, storeId);
			result = statement.executeQuery();

			while (result.next()) {
				CustomerClassInfo customeClassInfo = new CustomerClassInfo();
				customeClassInfo.setCompanyId(result.getString("CompanyId"));
				customeClassInfo.setStoreId(result.getString("StoreId"));
				customeClassInfo.setId(result.getString("CustomerClassId"));
				customeClassInfo.setName(result.getString("CustomerClassName"));
				customeClassInfo.setKanaName(result.getString("CustomerClassKanaName"));
				customeClassInfo.setGenerationType(result.getString("GenerationType"));
				customeClassInfo.setSexType(result.getString("SexType"));
				customeClassInfo.setOtherType(result.getString("OtherType"));
				customerClassInfoList.add(customeClassInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get customer class info.",
					sqlEx);
			throw new DaoException("SQLException: @SQLServerCustomerInfoDAO.getCustomerClassInfo", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get customer class info.",
					ex);
			throw new DaoException("Exception: @SQLServerCustomerInfoDAO.getCustomerClassInfo", ex);
		} finally {
			closeConnectionObjects(connection, statement, result);
			tp.methodExit(customerClassInfoList);
		}
		return customerClassInfoList;
	}

}
