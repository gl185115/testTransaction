package ncr.res.mobilepos.employee.dao;

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
import ncr.res.mobilepos.employee.model.EmployeeInfo;
import ncr.res.mobilepos.employee.model.EmployeeInfoResponse;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerEmployeeDAO extends AbstractDao implements IEmployeeDao {

	/**
	 * DBManager that manages the database.
	 */
	private DBManager dbManager;

	private static final String PROG_NAME = "SQLServerEmployeeDAO";
	/**
	 * Logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;
	private final SQLStatement sqlStatement;

	/**
	 * The class constructor.
	 * 
	 * @throws DaoException
	 *             Exception thrown when construction fails.
	 */
	public SQLServerEmployeeDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();

		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
		this.sqlStatement = SQLStatement.getInstance();
	}

	@Override
	public EmployeeInfoResponse EmpList(String companyID) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("EmpList");
		tp.println("companyID", companyID);

		EmployeeInfoResponse employeeInfoResponse = new EmployeeInfoResponse();
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;

		if (StringUtility.isEmpty(companyID)) {
			employeeInfoResponse.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			employeeInfoResponse.setMessage("companyID is empty.");
			tp.methodExit("companyID is empty.");
			return employeeInfoResponse;
		}

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			statement = connection.prepareStatement(sqlStatement.getProperty("get-employee-list-by-companyId"));

			statement.setString(SQLStatement.PARAM1, companyID);
			result = statement.executeQuery();

			List<EmployeeInfo> employeeInfoList = new ArrayList<EmployeeInfo>();
			while (result.next()) {
				EmployeeInfo employeeInfo = new EmployeeInfo();
				employeeInfo.setEmpCode(result.getString("EmpCode"));
				employeeInfo.setOpeCode(result.getString("OpeCode"));
				employeeInfo.setStoreId(result.getString("StoreId"));
				employeeInfo.setOpeName(result.getString("OpeName"));
				employeeInfoList.add(employeeInfo);
			}
			employeeInfoResponse.setEmpList(employeeInfoList);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get EmpList info.",
					sqlEx);
			throw new DaoException("SQLException: @SQLServerEmployeeDAO.EmpList", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get EmpList info.",
					ex);
			throw new DaoException("Exception: @SQLServerEmployeeDAO.EmpList", ex);
		} finally {
			closeConnectionObjects(connection, statement, result);
		}

		return employeeInfoResponse;
	}

}
