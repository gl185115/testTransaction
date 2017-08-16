package ncr.res.mobilepos.department.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.DepartmentName;
import ncr.res.mobilepos.department.model.DptConst;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.resource.ItemResource;
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
			final String retailStoreID, final String departmentID, String searchRetailStoreID)
			throws DaoException {
		tp.methodEnter("selectDepartmentDetail");
		tp.println("CompanyID", companyID)
          .println("RetailStoreID", retailStoreID)
          .println("DepartmentID", departmentID)
          .println("SearchRetailStoreID", searchRetailStoreID);

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
				dpt.setSubCode1(result.getString(DptConst.COL_SUBCODE1_FLAG));
				dpt.setSubNum1(result.getString(DptConst.COL_SUBNUM1_FLAG));
				dpt.setSubNum2(result.getString(DptConst.COL_SUBNUM2_FLAG));
				dpt.setSubNum3(result.getString(DptConst.COL_SUBNUM3_FLAG));
				dpt.setSubNum4(result.getString(DptConst.COL_SUBNUM4_FLAG));
				dptModel.setDepartment(dpt);
				dptModel.setRetailStoreID(searchRetailStoreID);
				setPricePromInfo(dptModel);
			} else {
				dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_DPTNOTFOUND);
				tp.println("Department not found.");
				dptModel.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			}
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

	private void setPricePromInfo(ViewDepartment dptModel) {
		tp.methodEnter(DebugLogger.getCurrentMethodName()).println("Dpt", dptModel.getDepartment().getDepartmentID());

		ItemResource itemResource = new ItemResource();
		PricePromInfo pricePromInfo ;
		pricePromInfo = itemResource.getPricePromInfo("", dptModel.getDepartment().getDepartmentID(), "");
		if (pricePromInfo != null){
			dptModel.setDiscountClass(pricePromInfo.getDiscountClass());
			dptModel.setDiscountAmt(pricePromInfo.getDiscountAmt());
			dptModel.setDiscountRate(pricePromInfo.getDiscountRate());
			dptModel.setPromotionNo(pricePromInfo.getPromotionNo());
			dptModel.setPromotionType(pricePromInfo.getPromotionType());
		}
        tp.methodExit(dptModel.toString());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final DepartmentList listDepartments(final String companyId, final String storeId,
			final String key, final String name, final int limit)
			throws DaoException {
		String functionName = "SQLServerDepartmentDAO.listDepartments";
		tp.methodEnter(DebugLogger.getCurrentMethodName())
				.println("CompanyID", companyId).println("RetailStoreID", storeId)
				.println("Key", key).println("Name", name).println("Limit", limit);

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
			selectStmt.setString(SQLStatement.PARAM2, companyId);
			selectStmt.setString(SQLStatement.PARAM3, storeId);

			String keyId = StringUtility.isNullOrEmpty(key) ? null : StringUtility.escapeCharatersForSQLqueries(key) + "%";
			String nameTemp = StringUtility.isNullOrEmpty(name) ? null : "%"
					+ StringUtility.escapeCharatersForSQLqueries(name) + "%";

			selectStmt.setString(SQLStatement.PARAM4, keyId);
			selectStmt.setString(SQLStatement.PARAM5, nameTemp);

			resultSet = selectStmt.executeQuery();

			while (resultSet.next()) {
				Department department = new Department();
				department
						.setDepartmentID(resultSet.getString(DptConst.COL_DPT));
				department.setRetailStoreID(storeId);
				DepartmentName departmentName = new DepartmentName();
				departmentName.setEn(resultSet.getString(DptConst.COL_DPT_NAME));
				departmentName.setJa(resultSet
						.getString(DptConst.COL_DPT_NAME_LOCAL));
				department.setDepartmentName(departmentName);
				department.setInheritFlag(resultSet.getString(DptConst.COL_INHERIT_FLAG));
				department.setNonSales(resultSet.getInt(DptConst.COL_EXCEPTION_FLAG));
				department.setDiscountType(resultSet.getString(DptConst.COL_DISCOUNT_TYPE));
				department.setDiscountFlag(resultSet.getString(DptConst.COL_DISCOUNT_FLAG));
				department.setDiscountAmt(resultSet.getDouble(DptConst.COL_DISCOUNT_AMT));
				department.setDiscountRate(resultSet.getDouble(DptConst.COL_DISCOUNT_RATE));
				department.setTaxRate(resultSet.getString(DptConst.COL_TAX_RATE));
				department.setTaxType(resultSet.getString(DptConst.COL_TAX_TYPE));
				department.setSubNum1(resultSet.getString(DptConst.COL_SUBNUM1_FLAG));
				department.setSubNum2(resultSet.getString(DptConst.COL_SUBNUM2_FLAG));
				department.setSubNum3(resultSet.getString(DptConst.COL_SUBNUM3_FLAG));
				department.setSubNum4(resultSet.getString(DptConst.COL_SUBNUM4_FLAG));
				department.setSubCode1(resultSet.getString(DptConst.COL_SUBCODE1_FLAG));
				
				ItemResource itemResource = new ItemResource();
				PricePromInfo pricePromInfo ;
				pricePromInfo = itemResource.getPricePromInfo("", department.getDepartmentID(), "");
				if (pricePromInfo != null){
					department.setPriceDiscountClass(pricePromInfo.getDiscountClass());
					department.setPriceDiscountAmt(pricePromInfo.getDiscountAmt());
					department.setPriceDiscountRate(pricePromInfo.getDiscountRate());
					department.setPricePromotionNo(pricePromInfo.getPromotionNo());
				}
				
				departments.add(department);
			}

			if (departments.size() == 0){
				dptList.setNCRWSSResultCode(ResultBase.RES_ERROR_DPTNOTFOUND);
				tp.println("Department not found.");
				dptList.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			} else {
				dptList.setRetailStoreID(storeId);
				dptList.setDepartments(departments);
			}

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
}
