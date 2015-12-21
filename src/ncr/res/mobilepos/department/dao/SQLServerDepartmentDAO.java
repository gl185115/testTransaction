package ncr.res.mobilepos.department.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.DepartmentName;
import ncr.res.mobilepos.department.model.DptConst;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
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

	private static final int STATUS_UNKNOWN = 0;

	private static final int STATUS_ACTIVE = 1;

	private static final int STATUS_DELETED = 2;

	private static final int DEPARTMENT_NOT_FOUND = -1;

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

	/***
	 * Deletes an existing Active department If not existing, returns an error
	 * code.
	 * 
	 * @param storeId
	 *            - store to which the department belongs.
	 * @param department
	 *            - department object details.
	 * @return ResultBase - 0 for success deletion.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final ResultBase deleteDepartment(final String storeId,
			final Department department) throws DaoException {

		tp.methodEnter("deleteDepartment");
		tp.println("StoreID", storeId).println("Department", department);

		Connection connection = null;
		PreparedStatement delete = null;
		int result = 0;
		ResultBase resBase = new ResultBase();

		try {
			int departmentStatus = getDepartmentStatus(storeId,
					department.getDepartmentID());

			if (departmentStatus != STATUS_ACTIVE) {
				resBase.setNCRWSSResultCode(ResultBase.RES_DPTMT_NOT_EXIST);
				tp.println("Department not found.");
				return resBase;
			}

			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();

			delete = connection.prepareStatement(sqlStatement
					.getProperty("delete-department"));
			delete.setString(SQLStatement.PARAM1, storeId);
			delete.setString(SQLStatement.PARAM2, department.getDepartmentID());
			delete.setString(SQLStatement.PARAM3, department.getUpdAppId());
			delete.setString(SQLStatement.PARAM4, department.getUpdOpeCode());

			result = delete.executeUpdate();
			if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
				resBase.setNCRWSSResultCode(ResultBase.RES_DPTMT_OK);
			} else if (result == SQLResultsConstants.NO_ROW_AFFECTED) {
				resBase.setNCRWSSResultCode(ResultBase.RES_DPTMT_NOT_EXIST);
				tp.println("Department not found.");
			}
			connection.commit();
		} catch (SQLStatementException e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.deleteDepartment",
					Logger.RES_EXCEP_SQLSTATEMENT,
					"Failed to delete Department\n " + e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:deleteDepartment", e);
			throw new DaoException(
					"SQLServerDepartmentDAO: @deleteDepartment ", e);
		} catch (SQLException e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.deleteDepartment",
					Logger.RES_EXCEP_SQL,
					"Failed to delete Department\n " + e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:deleteDepartment", e);
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO. deleteDepartment", e);
		} catch (Exception e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.deleteDepartment",
					Logger.RES_EXCEP_GENERAL, "Failed to delete Department\n "
							+ e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:deleteDepartment", e);
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.deleteDepartment ", e);
		} finally {
			closeConnectionObjects(connection, delete);

			tp.methodExit(resBase);
		}
		return resBase;
	}

	/**
	 * Creates a department. <br/>
	 * If existing with not active status, the status will be active and may
	 * includes new information of department name.<br/>
	 * If existing with active status, an error code will be returned.
	 * 
	 * @param storeId
	 *            - store to which the department belongs.
	 * @param departmentId
	 *            - new department identifier.
	 * @param department
	 *            - new department object.
	 * @return ResultBase
	 * @throws DaoException
	 *             - if duplicate entry of active status.<br/>
	 *             - if database fails.
	 */
	public final ResultBase createDepartment(final String storeId,
			final String departmentId, final Department department)
			throws DaoException {

		tp.methodEnter("createDepartment");
		tp.println("StoreID", storeId).println("DepartmentID", departmentId)
				.println("department", department);

		Connection connection = null;
		PreparedStatement sqlStmt = null;
		int result = 0;
		ResultBase resBase = new ResultBase();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();

			int departmentStatus = this.getDepartmentStatus(storeId,
					departmentId);

			switch (departmentStatus) {
			case STATUS_ACTIVE:
				resBase.setNCRWSSResultCode(ResultBase.RES_DPTMT_EXISTS);
				tp.println("Department to be added is currently active.");
				return resBase;
			case DEPARTMENT_NOT_FOUND:
				sqlStmt = connection.prepareStatement(sqlStatement
						.getProperty("create-department"));
				sqlStmt = this.setSQLstmtForDptNotFound(sqlStmt, department);
				break;
			case STATUS_DELETED:
			case STATUS_UNKNOWN:
				sqlStmt = connection.prepareStatement(sqlStatement
						.getProperty("update-department-active"));
				sqlStmt = this.setSQLstmtForStatusUnknown(sqlStmt, department);
				break;
			default: 
			}
			sqlStmt.setString(SQLStatement.PARAM1, storeId);
			sqlStmt.setString(SQLStatement.PARAM2, departmentId);

			String en = "";
			String ja = "";
			DepartmentName departmentName = department.getDepartmentName();
			if (departmentName != null) {
				en = departmentName.getEn() != null ? departmentName.getEn()
						: "";
				ja = departmentName.getJa() != null ? departmentName.getJa()
						: "";
			}

			sqlStmt.setString(SQLStatement.PARAM3, en);
			sqlStmt.setString(SQLStatement.PARAM4, ja);

			result = sqlStmt.executeUpdate();
			if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
				resBase.setNCRWSSResultCode(ResultBase.RES_DPTMT_OK);
			}
			connection.commit();
		} catch (SQLStatementException e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.createDepartment",
					Logger.RES_EXCEP_SQLSTATEMENT,
					"Failed to add Department\n " + e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:createDepartment", e);
			throw new DaoException(
					"SQLServerDepartmentDAO: @createDepartment ", e);
		} catch (SQLException e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.createDepartment",
					Logger.RES_EXCEP_SQL,
					"Failed to add Department\n " + e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:createDepartment", e);
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.createDepartment ", e);

		} catch (Exception e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.createDepartment",
					Logger.RES_EXCEP_GENERAL,
					"Failed to add Department\n " + e.getMessage());
			rollBack(connection, "@SQLServerDepartmentDAO:createDepartment", e);
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.createDepartment ", e);
		} finally {
			closeConnectionObjects(connection, sqlStmt);

			tp.methodExit(resBase);
		}
		return resBase;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final DepartmentList listDepartments(final String storeId,
			final String key, final String name, final int limit)
			throws DaoException {
		String functionName = "SQLServerDepartmentDAO.listDepartments";
		tp.methodEnter(DebugLogger.getCurrentMethodName())
				.println("RetailStoreID", storeId).println("Key", key)
				.println("Limit", limit);

		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		DepartmentList dptList = new DepartmentList();
		List<Department> departments = new ArrayList<Department>();

		try {

			String strStmnt = "get-departments";
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();

			selectStmt = connection.prepareStatement(sqlStatement
					.getProperty(strStmnt));

			tp.println("searchlimit", GlobalConstant.getMaxSearchResults());
			int searchLimit = (limit == 0) ? GlobalConstant
					.getMaxSearchResults() : limit;

			selectStmt.setInt(SQLStatement.PARAM1, searchLimit);
			selectStmt.setString(SQLStatement.PARAM2, storeId);

			String keyId = StringUtility.isNullOrEmpty(key) ? null : StringUtility.escapeCharatersForSQLqueries(key) + "%";
			String nameTemp = StringUtility.isNullOrEmpty(name) ? null : "%"
					+ StringUtility.escapeCharatersForSQLqueries(name) + "%";

			selectStmt.setString(SQLStatement.PARAM3, keyId);
			selectStmt.setString(SQLStatement.PARAM4, nameTemp);
			resultSet = selectStmt.executeQuery();
			while (resultSet.next()) {
				Department department = new Department();
				department
						.setDepartmentID(resultSet.getString(DptConst.COL_DPT));
				department.setRetailStoreID(resultSet
						.getString(DptConst.COL_STORE_ID));
				DepartmentName departmentName = new DepartmentName();
				departmentName.setEn(resultSet.getString(DptConst.COL_DPT_NAME));
				departmentName.setJa(resultSet
						.getString(DptConst.COL_DPT_NAME_LOCAL));
				department.setDepartmentName(departmentName);
				department.setInheritFlag(resultSet.getString(DptConst.COL_INHERIT_FLAG));
				department.setNonSales(resultSet.getInt(DptConst.COL_EXCEPTION_FLAG));
				department.setSubSmallInt5(resultSet.getInt(DptConst.COL_SUB_SMALL_INT5));
				department.setDiscountType(resultSet.getString(DptConst.COL_DISCOUNT_TYPE));
				department.setDiscountFlag(resultSet.getString(DptConst.COL_DISCOUNT_FLAG));
				department.setDiscountAmt(resultSet.getDouble(DptConst.COL_DISCOUNT_AMT));
				department.setDiscounRate(resultSet.getDouble(DptConst.COL_DISCOUNT_RATE));
				department.setTaxRate(resultSet.getString(DptConst.COL_TAX_RATE));
				department.setTaxType(resultSet.getString(DptConst.COL_TAX_TYPE));
				departments.add(department);
			}

			dptList.setRetailStoreID(storeId);
			dptList.setDepartments(departments);

		} catch (SQLStatementException sqlStmtEx) {
			LOGGER.logAlert(progName, functionName,
					Logger.RES_EXCEP_SQLSTATEMENT,
					"Failed to list Departments\n " + sqlStmtEx.getMessage());
			throw new DaoException("SQLServerDepartmentDAO: @listDepartments",
					sqlStmtEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
					"Failed to list Departments\n " + sqlEx.getMessage());
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.listDepartments", sqlEx);
		} catch (Exception e) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to list Departments\n " + e.getMessage());
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.listDepartments ", e);
		} finally {
			closeConnectionObjects(connection, selectStmt, resultSet);

			tp.methodExit(dptList);
		}

		return dptList;
	}

	@Override
	public final ViewDepartment updateDepartment(final String oldStoreId,
			final String oldDepartmentId, final Department department)
			throws DaoException {

		tp.methodEnter("updateDepartment");
		tp.println("StoreID", oldStoreId)
				.println("DepartmentID", oldDepartmentId)
				.println("Department", department);

		String storeId = oldStoreId;
		String departmentId = oldDepartmentId;
		ViewDepartment dptModel = new ViewDepartment();
		try {
			int deptStatus = getDepartmentStatus(storeId, departmentId);
			switch (deptStatus) {
			case STATUS_DELETED:
			case DEPARTMENT_NOT_FOUND:
				dptModel.setNCRWSSResultCode(ResultBase.RES_DPTMT_NOT_EXIST);
				tp.println("Department was deleted.");
				return dptModel;
			case STATUS_UNKNOWN:
				dptModel.setNCRWSSResultCode(ResultBase.RES_DPTMT_NOTACTIVE);
				tp.println("Department is neither deleted nor active.");
				return dptModel;
			case STATUS_ACTIVE:
				return this.updateActiveDepartment(oldStoreId, oldDepartmentId,
						department);
			default: {
				//do nothing
			}
			}
		} catch (Exception e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.updateDepartment()",
					Logger.RES_EXCEP_GENERAL, "Failed to update Departments\n "
							+ e.getMessage());
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.updateDepartment ", e);
		} finally {
			tp.methodExit(dptModel);
		}
		return dptModel;
	}
	
	private final ViewDepartment updateActiveDepartment(final String oldStoreId,
			final String oldDepartmentId, final Department department)
			throws DaoException {

		tp.methodEnter("updateActiveDepartment");
		tp.println("StoreID", oldStoreId)
				.println("DepartmentID", oldDepartmentId)
				.println("Department", department);

		Connection connection = null;
		PreparedStatement update = null;
		ResultSet result = null;
		ViewDepartment dptModel = new ViewDepartment();
		Department dpt = null;
		String storeId = oldStoreId;
		String departmentId = oldDepartmentId;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			String newStoreId = department.getRetailStoreID();
			String newDepartmentId = department.getDepartmentID();
			boolean updateDeletedDepartment = false;
			int newDeptStatus = 0;

			if (!storeId.equals(newStoreId)
					|| !departmentId.equals(newDepartmentId)) {
				newDeptStatus = getDepartmentStatus(newStoreId, newDepartmentId);
				if (newDeptStatus == STATUS_DELETED) {
					updateDeletedDepartment = true;
					storeId = newStoreId;
					departmentId = newDepartmentId;
				} else if (newDeptStatus == STATUS_ACTIVE) {
					dptModel.setNCRWSSResultCode(ResultBase.RES_DPTMT_EXISTS);
					tp.println("Department already exist.");
					return dptModel;
				}
			} else {
				newStoreId = storeId;
			}
			update = connection.prepareStatement(sqlStatement
					.getProperty("update-department"));
			update.setString(SQLStatement.PARAM1, department.getDepartmentID());

			String en = null;
			String ja = null;
			if (department.getDepartmentName() != null) {
				en = department.getDepartmentName().getEn() != null ? department
						.getDepartmentName().getEn() : null;
				ja = department.getDepartmentName().getJa() != null ? department
						.getDepartmentName().getJa() : null;
			}

			update.setString(SQLStatement.PARAM2, en);
			update.setString(SQLStatement.PARAM3, ja);

			update.setString(SQLStatement.PARAM4, newStoreId);
			update.setString(SQLStatement.PARAM5, department.getTaxType());
			update.setString(SQLStatement.PARAM6, department.getTaxRate());
			update.setString(SQLStatement.PARAM7, department.getDiscountType());
			update.setInt(SQLStatement.PARAM8, department.getNonSales());
			update.setString(SQLStatement.PARAM9, department.getDiscountFlag());
			update.setDouble(SQLStatement.PARAM10, department.getDiscountAmt());
			update.setDouble(SQLStatement.PARAM11, department.getDiscounRate());
			update.setString(SQLStatement.PARAM12,
					department.getAgeRestrictedFlag());
			update.setString(SQLStatement.PARAM13, department.getInheritFlag());
			update.setInt(SQLStatement.PARAM14, department.getSubSmallInt5());
			update.setString(SQLStatement.PARAM15, department.getUpdAppId());
			update.setString(SQLStatement.PARAM16, department.getUpdOpeCode());
			update.setString(SQLStatement.PARAM17, storeId);
			update.setString(SQLStatement.PARAM18, departmentId);

			result = update.executeQuery();
			if (result.next()) {
				dpt = new Department();
				dpt.setDepartmentID(result.getString(DptConst.COL_DPT));

				// department name
				DepartmentName departmentName = new DepartmentName();
				departmentName.setEn(result.getString(DptConst.COL_DPT_NAME));
				departmentName
						.setJa(result.getString(DptConst.COL_DPT_NAME_LOCAL));

				dpt.setDepartmentName(departmentName);
				dpt.setInheritFlag(result.getString(DptConst.COL_INHERIT_FLAG));
				dpt.setNonSales(result.getInt(DptConst.COL_EXCEPTION_FLAG));
				dpt.setSubSmallInt5(result.getInt(DptConst.COL_SUB_SMALL_INT5));
				dpt.setDiscountType(result.getString(DptConst.COL_DISCOUNT_TYPE));
				dpt.setDiscountFlag(result.getString(DptConst.COL_DISCOUNT_FLAG));
				dpt.setDiscountAmt(result.getDouble(DptConst.COL_DISCOUNT_AMT));
				dpt.setDiscounRate(result.getDouble(DptConst.COL_DISCOUNT_RATE));
				dpt.setTaxRate(result.getString(DptConst.COL_TAX_RATE));
				dpt.setTaxType(result.getString(DptConst.COL_TAX_TYPE));

				dptModel.setDepartment(dpt);
			} else {
				dptModel.setNCRWSSResultCode(ResultBase.RES_DPTMT_NO_UPDATE);
				tp.println("Department to update does not exists.");
			}
			connection.commit();
			if (updateDeletedDepartment) {
				department.setRetailStoreID(oldStoreId);
				department.setDepartmentID(oldDepartmentId);
				deleteDepartment(oldStoreId, department);
			}

		} catch (SQLStatementException sqlStmtEx) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.updateActiveDepartment()",
					Logger.RES_EXCEP_SQLSTATEMENT, "Failed to update the "
							+ "Department Details.\n" + sqlStmtEx.getMessage());
			rollBack(connection,
					"@SQLServerDepartmentDAO:updateActiveDepartment", sqlStmtEx);
			throw new DaoException(
					"SQLStatementException: @updateActiveDepartment ",
					sqlStmtEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.updateActiveDepartment()",
					Logger.RES_EXCEP_SQL, "Failed to get the Department"
							+ "Details.\n" + sqlEx.getMessage());
			if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) == sqlEx
					.getErrorCode()) {
				dptModel.setNCRWSSResultCode(ResultBase.RES_DPTMT_EXISTS);
				tp.println("Department Entry is duplicated.");
			} else {
				rollBack(connection,
						"@SQLServerDepartmentDAO:updateActiveDepartment", sqlEx);
				throw new DaoException("SQLException: @updateActiveDepartment",
						sqlEx);
			}
		} catch (Exception e) {
			LOGGER.logAlert(progName,
					"SQLServerDepartmentDAO.updateActiveDepartment()",
					Logger.RES_EXCEP_GENERAL, "Failed to update Departments\n "
							+ e.getMessage());
			rollBack(connection,
					"@SQLServerDepartmentDAO:updateActiveDepartment", e);
			throw new DaoException("SQLException:"
					+ "@SQLServerDepartmentDAO.updateActiveDepartment ", e);
		} finally {
			closeConnectionObjects(connection, update, result);

			tp.methodExit(dptModel);
		}
		return dptModel;
	}

	/**
	 * This is to get the current status of the Department 1 - Active 2 -
	 * Deleted -1 - Not Found
	 * 
	 * @param storeId
	 * @param departmentId
	 * @return
	 * @throws DaoException
	 */
	public final int getDepartmentStatus(final String storeId,
			final String departmentId) throws DaoException {
		String functionName = "@SQL" + "getDepartmentDAO";
		tp.methodEnter("getDepartmentDAO");
		tp.println("storeId", storeId);
		tp.println("departmentId", departmentId);

		Connection connection = null;
		PreparedStatement select = null;
		PreparedStatement update = null;
		ResultSet result = null;
		int status = 0;

		try {
			status = STATUS_UNKNOWN;
			connection = dbManager.getConnection();

			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-department"));

			select.setString(SQLStatement.PARAM1, storeId);
			select.setString(SQLStatement.PARAM2, departmentId);
			result = select.executeQuery();

			if (result.next()) {
				String res = result.getString(result.findColumn("Status"));
				if (res != null && "Deleted".equalsIgnoreCase(res)) {
					status = STATUS_DELETED;
				} else if (res != null && "Active".equalsIgnoreCase(res)) {
					status = STATUS_ACTIVE;
				}

			} else {
				tp.println("Department not found.");
				return DEPARTMENT_NOT_FOUND;
			}

			connection.commit();
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(progName, functionName,
					Logger.RES_EXCEP_SQLSTATEMENT,
					"Failed to check Status of Operator#" + departmentId + ": "
							+ ex.getMessage());
			throw new DaoException(
					"SQLStatementException: @SQLServerDepartmentDAO"
							+ ".getDepartmentStatus", ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
					"Failed to check Status of Department" + departmentId
							+ ": " + ex.getMessage());
			throw new DaoException(
					"SQLException: @SQLServerDepartmentDAO.checkOperatorStatus",
					ex);
		} catch (Exception ex) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to check Status of Department" + departmentId
							+ ": " + ex.getMessage());
			throw new DaoException(
					"SQLException: @SQLServerDepartmentDAO.checkOperatorStatus",
					ex);
		} finally {
			closeConnectionObjects(null, select, result);
			closeConnectionObjects(connection, update);

			tp.methodExit(status);
		}

		return status;

	}

	/**
	 * 
	 * @param sqlStmt
	 * @param department
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement setSQLstmtForDptNotFound(
			PreparedStatement sqlStmt, Department department)
			throws SQLException {

		sqlStmt.setString(SQLStatement.PARAM5, DptConst.JIGYOBU_DEFAULT);
		sqlStmt.setString(SQLStatement.PARAM6, DptConst.GYOSHU_DEFAULT);
		sqlStmt.setString(SQLStatement.PARAM7, DptConst.BUMON_DEFAULT);
		sqlStmt.setString(SQLStatement.PARAM8, DptConst.URIBA_DEFAULT);
		sqlStmt.setString(SQLStatement.PARAM9, DptConst.CATEGORY_DEFAULT);
		sqlStmt.setString(SQLStatement.PARAM10, department.getTaxType());
		sqlStmt.setString(SQLStatement.PARAM11, department.getTaxRate());
		sqlStmt.setString(SQLStatement.PARAM12, department.getDiscountType());
		sqlStmt.setInt(SQLStatement.PARAM13, department.getNonSales());
		sqlStmt.setString(SQLStatement.PARAM14, department.getDiscountFlag());
		sqlStmt.setDouble(SQLStatement.PARAM15, department.getDiscountAmt());
		sqlStmt.setDouble(SQLStatement.PARAM16, department.getDiscounRate());
		sqlStmt.setString(SQLStatement.PARAM17,
				department.getAgeRestrictedFlag());
		sqlStmt.setString(SQLStatement.PARAM18, department.getInheritFlag());
		sqlStmt.setInt(SQLStatement.PARAM19, department.getSubSmallInt5());
		sqlStmt.setString(SQLStatement.PARAM20, department.getUpdAppId());
		sqlStmt.setString(SQLStatement.PARAM21, department.getUpdOpeCode());
		return sqlStmt;
	}

	/**
	 * 
	 * @param sqlStmt
	 * @param department
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement setSQLstmtForStatusUnknown(
			PreparedStatement sqlStmt, Department department)
			throws SQLException {
		sqlStmt.setString(SQLStatement.PARAM5, department.getUpdAppId());
		sqlStmt.setString(SQLStatement.PARAM6, department.getUpdOpeCode());
		return sqlStmt;
	}

    /**
     * Gets the Department info.
     *
     * @param retailStoreID
     *            - Path parameter for store identifier
     * @param departmentID
     *            - Path parameter for department identifier
     * @return a JSON string of the Department Info.
     *
     * @throws DaoException
     *             - if database fails.
     */
    public final ViewDepartment getDepartmentInfo(
            final String retailStoreID, final String departmentID)
            throws DaoException {
    	
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName);
        tp.println("RetailStoreID", retailStoreID).
           println("DepartmentID", departmentID);

        ViewDepartment dptInfo = null;
        Department dpt = null;

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("select-departmentdetails"));

            select.setString(SQLStatement.PARAM1, retailStoreID);
            select.setString(SQLStatement.PARAM2, departmentID);

            result = select.executeQuery();
            
            dptInfo = new ViewDepartment();
            if (result.next()) {
                dpt = new Department();
                dpt.setRetailStoreID(result.getString(DptConst.COL_STORE_ID));
                dpt.setDepartmentID(result.getString(DptConst.COL_DPT));

                // department name
                DepartmentName departmentName = new DepartmentName();
                departmentName.setEn(result.getString(DptConst.COL_DPT_NAME));
                departmentName
                        .setJa(result.getString(DptConst.COL_DPT_NAME_LOCAL));
                dpt.setDepartmentName(departmentName);

                dpt.setDptStatus(result.getString(DptConst.COL_STATUS));

                dptInfo.setDepartment(dpt);
                dptInfo.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                dptInfo.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                dptInfo.setMessage(ResultBase.RES_SUCCESS_MSG);
            } else {
                dptInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                dptInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                dptInfo.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName+ ": Failed to get the "
                            + "Department Info.", sqlStmtEx);
            throw new DaoException("SQLStmtException:"
                    + " @SQLServerDepartmentDAO.getDepartmentInfo", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_SQL,
                    functionName+ ": Failed to get the "
                            + "Department Info.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerDepartmentDAO.getDepartmentInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL,
                    functionName+ ": Failed to get the "
                            + "Department Info .", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerDepartmentDAO.getDepartmentInfo", ex);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(dptInfo);
        }

        return dptInfo;
    }
}
