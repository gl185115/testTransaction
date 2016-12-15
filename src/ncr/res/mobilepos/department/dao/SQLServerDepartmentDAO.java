package ncr.res.mobilepos.department.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentName;
import ncr.res.mobilepos.department.model.DptConst;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
/**
 * 
 * @author AP185142
 * @author RD185102
 * @author cc185102 - (July 30, 2012) Updated List Department to have partial
 *         search on Department ID and Department Name.
 * 
 */
public class SQLServerDepartmentDAO extends AbstractDao implements
		IDepartmentDAO {

	/**
	 * database manager.
	 */
	private DBManager dbManager;
	/**
	 * logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/** The Trace Printer. */
	private Trace.Printer tp;

	private String progName = "DptDao";

	/**
	 * The class constructor.
	 * 
	 * @throws DaoException
	 *             Exception thrown when construction fails.
	 */
	public SQLServerDepartmentDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
				getClass());
	}

	/**
	 * Gets the information of an active department.
	 * 
	 * @param retailStoreID
	 *            - store to which the department belongs.
	 * @param departmentID
	 *            - department id to be viewed.
	 * @return Department details like name and code.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final ViewDepartment selectDepartmentDetail(final String companyID, 
			final String retailStoreID, final String departmentID)
			throws DaoException {
		tp.methodEnter("selectDepartmentDetail");
		tp.println("CompanyID", companyID)
          .println("RetailStoreID", retailStoreID)
          .println("DepartmentID", departmentID);

		ViewDepartment dptModel = new ViewDepartment();

		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("select-departmentdetails"));
			select.setString(SQLStatement.PARAM1, companyID);
			select.setString(SQLStatement.PARAM2, retailStoreID);
			select.setString(SQLStatement.PARAM3, departmentID);

			result = select.executeQuery();
			if (result.next()) {
				Department dpt = new Department();
				dpt.setDepartmentID(result.getString(DptConst.COL_DPT));

				// department name
				DepartmentName departmentName = new DepartmentName();
				departmentName.setEn(result.getString(DptConst.COL_DPT_NAME));
				departmentName.setJa(result
						.getString(DptConst.COL_DPT_NAME_LOCAL));
				dpt.setDepartmentName(departmentName);
				dpt.setTaxRate(result.getString(DptConst.COL_TAX_RATE));
				dpt.setTaxType(result.getString(DptConst.COL_TAX_TYPE));
				dpt.setDiscountType(result.getString(DptConst.COL_DISCOUNT_TYPE));
				dpt.setNonSales(result.getInt(DptConst.COL_EXCEPTION_FLAG));
				dpt.setSubNum1(result.getString(DptConst.COL_SUBNUM1_FLAG));
				dpt.setSubNum2(result.getString(DptConst.COL_SUBNUM2_FLAG));
				dpt.setSubNum3(result.getString(DptConst.COL_SUBNUM3_FLAG));
				dptModel.setDepartment(dpt);
				dptModel.setRetailStoreID(result.getString(DptConst.COL_STORE_ID));
			} else {
				dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DPTNOTFOUND);
				tp.println("Department not found.");
				dptModel.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			}
		} catch (SQLStatementException sqlStmtEx) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.selectDepartmentDetail()",
					Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get the "
							+ "Department Details.\n" + sqlStmtEx.getMessage());
			throw new DaoException("SQLStatementException:"
					+ " @selectDepartmentDetail ", sqlStmtEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.selectDepartmentDetail()",
					Logger.RES_EXCEP_SQL, "Failed to get the Department"
							+ "Details.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @selectDepartmentDetail ",
					sqlEx);
		} finally {
			closeConnectionObjects(connection, select, result);

			tp.methodExit(dptModel.toString());
		}

		return dptModel;
	}
    
}
