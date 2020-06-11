package ncr.res.mobilepos.department.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import ncr.res.mobilepos.pricing.model.ChangeableTaxRate;
import ncr.res.mobilepos.pricing.model.DefaultTaxRate;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.model.TaxRateInfo;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.factory.TaxRateInfoFactory;
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

	private static final String COLUMN_OF_DPT = "ColumnOfDpt";

	private String dptTaxId = "";

	private final List<TaxRateInfo> taxRateInfoList;

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
		taxRateInfoList = TaxRateInfoFactory.getInstance();
	}

	/**
	 * Gets the information of an active department.
	 * @param companyID
	 *
	 * @param retailStoreID
	 *            - store to which the department belongs.
	 * @param departmentID
	 *            - department id to be viewed.
	 * @param searchRetailStoreID
	 *
	 * @param mapTaxId
	 *
	 * @return Department details like name and code
	 *
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final ViewDepartment selectDepartmentDetail(final String companyID,
			final String retailStoreID, final String departmentID, String searchRetailStoreID, Map<String, String> mapTaxId)
			throws DaoException {
		tp.methodEnter("selectDepartmentDetail");
		tp.println("CompanyID", companyID)
          .println("RetailStoreID", retailStoreID)
          .println("DepartmentID", departmentID)
          .println("SearchRetailStoreID", searchRetailStoreID)
          .println("MapTaxId", mapTaxId);

		ViewDepartment dptModel = new ViewDepartment();

		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;

		// 部門の税率区分情報を取得する
		getSaleTaxIdInfo(mapTaxId);

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			String query = String.format(sqlStatement.getProperty("select-departmentdetails"), "dpt." + dptTaxId);
			select = connection.prepareStatement(query);
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
				dpt.setCompanyID(result.getString(DptConst.COL_COMPANY_ID));
				dpt.setRetailStoreID(result.getString(DptConst.COL_STORE_ID));
				dpt.setDepartmentName(departmentName);
				dpt.setTaxType(result.getString(DptConst.COL_TAX_TYPE));
				dpt.setDiscountType(result.getString(DptConst.COL_DISCOUNT_TYPE));
				dpt.setNonSales(result.getInt(DptConst.COL_EXCEPTION_FLAG));
				dpt.setSubCode1(result.getString(DptConst.COL_SUBCODE1_FLAG));
				dpt.setSubNum1(result.getString(DptConst.COL_SUBNUM1_FLAG));
				dpt.setSubNum2(result.getString(DptConst.COL_SUBNUM2_FLAG));
				dpt.setSubNum3(result.getString(DptConst.COL_SUBNUM3_FLAG));
				dpt.setSubNum4(result.getString(DptConst.COL_SUBNUM4_FLAG));
				if(!StringUtility.isNullOrEmpty(result.getString(dptTaxId))){
					dpt.setTaxId(result.getInt(dptTaxId));
				}
				dpt.setGroupID(result.getString(DptConst.COL_GROUPID));
				dpt.setGroupName(result.getString(DptConst.COL_GROUPNAME));

				List<TaxRateInfo> taxInfoList = new ArrayList<TaxRateInfo>();
				DefaultTaxRate defaultTaxRate = null;
				ChangeableTaxRate changeableTaxRate = null;

				// 非課税の場合、商品の税率情報を取得する
				if(("2").equals(dpt.getTaxType())){
					defaultTaxRate = new DefaultTaxRate();
					defaultTaxRate.setRate(0);
					dpt.setDefaultTaxRate(defaultTaxRate);
				}else{
					if (!getDptTaxInfo(taxInfoList, defaultTaxRate, changeableTaxRate, dpt)){
						LOGGER.logAlert(progName, "getDptTaxInfo", Logger.RES_TABLE_DATA_ERR,
								"The data in MST_TAXRATE is error.\n");
						dptModel.setDepartment(null);
						dptModel.setNCRWSSResultCode(ResultBase.RES_TABLE_DATA_ERR);
						dptModel.setMessage("The data in MST_TAXRATE is error.");
						return dptModel;
					}

					if(dpt.getChangeableTaxRate() == null && dpt.getDefaultTaxRate() == null){
						LOGGER.logAlert(progName, "getDptTaxInfo", Logger.RES_GET_DATA_ERR,
								"税率取得エラー。\n"+"Company="+ dpt.getCompanyID() +",Store="+ dpt.getRetailStoreID() + ",DPT=" + dpt.getDepartmentID());
						dptModel.setDepartment(null);
						dptModel.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
						dptModel.setMessage("Tax rate search error");
						return dptModel;
					}
				}

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
		tp.methodEnter("setPricePromInfo").println("Dpt", dptModel.getDepartment().getDepartmentID());

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
			final String key, final String name, final int limit, final Map<String, String> mapTaxId)
			throws DaoException {
		String functionName = "SQLServerDepartmentDAO.listDepartments";
		tp.methodEnter("listDepartments")
				.println("CompanyID", companyId).println("RetailStoreID", storeId)
				.println("Key", key).println("Name", name).println("Limit", limit)
				.println("MapTaxId", mapTaxId);

		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		DepartmentList dptList = new DepartmentList();
		List<Department> departments = new ArrayList<Department>();

		// 部門の税率区分情報を取得する
		getSaleTaxIdInfo(mapTaxId);

		try {
			String strStmnt = "get-departments";
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();

			String query = String.format(sqlStatement.getProperty(strStmnt), "TaxId", "DPT." + dptTaxId + " TaxId", "a." + dptTaxId + " TaxId");
			selectStmt = connection.prepareStatement(query);

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
				department.setCompanyID(resultSet.getString(DptConst.COL_COMPANY_ID));
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
				department.setTaxType(resultSet.getString(DptConst.COL_TAX_TYPE));
				department.setSubNum1(resultSet.getString(DptConst.COL_SUBNUM1_FLAG));
				department.setSubNum2(resultSet.getString(DptConst.COL_SUBNUM2_FLAG));
				department.setSubNum3(resultSet.getString(DptConst.COL_SUBNUM3_FLAG));
				department.setSubNum4(resultSet.getString(DptConst.COL_SUBNUM4_FLAG));
				if(!StringUtility.isNullOrEmpty(resultSet.getString("TaxId"))){
					department.setTaxId(resultSet.getInt("TaxId"));
				}
				department.setSubCode1(resultSet.getString(DptConst.COL_SUBCODE1_FLAG));
				department.setInputType(resultSet.getString(DptConst.COL_INPUTSUBCODE1_FLAG));

				ItemResource itemResource = new ItemResource();
				PricePromInfo pricePromInfo ;
				pricePromInfo = itemResource.getPricePromInfo("", department.getDepartmentID(), "");
				if (pricePromInfo != null){
					department.setPriceDiscountClass(pricePromInfo.getDiscountClass());
					department.setPriceDiscountAmt(pricePromInfo.getDiscountAmt());
					department.setPriceDiscountRate(pricePromInfo.getDiscountRate());
					department.setPricePromotionNo(pricePromInfo.getPromotionNo());
				}

				List<TaxRateInfo> taxInfoList = new ArrayList<TaxRateInfo>();
				DefaultTaxRate defaultTaxRate = null;
				ChangeableTaxRate changeableTaxRate = null;

				// 非課税の場合、商品の税率情報を取得する
				if(("2").equals(department.getTaxType())){
					defaultTaxRate = new DefaultTaxRate();
					defaultTaxRate.setRate(0);
					department.setDefaultTaxRate(defaultTaxRate);
					departments.add(department);
				}else{
					if (!getDptTaxInfo(taxInfoList, defaultTaxRate, changeableTaxRate, department)){
						LOGGER.logAlert(progName, "getDptTaxInfo", Logger.RES_TABLE_DATA_ERR,
								"The data in MST_TAXRATE is error.\n");
						dptList.setDepartments(null);
						dptList.setNCRWSSResultCode(ResultBase.RES_TABLE_DATA_ERR);
						dptList.setMessage("The data in MST_TAXRATE is error.");
						return dptList;
					}

					if(department.getChangeableTaxRate() != null || department.getDefaultTaxRate() != null){
						departments.add(department);
					}else{
						LOGGER.logAlert(progName, "getDptTaxInfo", Logger.RES_GET_DATA_ERR,
								"税率取得エラー。\n"+"Company="+ department.getCompanyID() +",Store="+ department.getRetailStoreID() + ",DPT=" + department.getDepartmentID());
						dptList.setDepartments(null);
						dptList.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
						dptList.setMessage("The data is not found.");
						return dptList;
					}
				}
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

	 /**
  	 * 部門の税率区分情報を取得する
  	 *
  	 * @param mapTaxId
  	 */
  	private void getSaleTaxIdInfo(Map<String, String> mapTaxId) {
  		if (mapTaxId != null ) {
  			for (String key : mapTaxId.keySet()) {
  				if ((COLUMN_OF_DPT).equals(key)) {
  					if (!StringUtility.isNullOrEmpty(mapTaxId.get(COLUMN_OF_DPT))) {
  						dptTaxId = mapTaxId.get(COLUMN_OF_DPT);
  						break;
  					}
  				}
  			}
  		}
  		if (StringUtility.isNullOrEmpty(dptTaxId)) {
  			dptTaxId = "SubNum5";
  		}
  	}

	 /**
  	 * 部門の税率情報を取得する
  	 *
  	 * @param taxInfoList
  	 * @param defaultTaxRate
  	 * @param changeableTaxRate
  	 * @param department
  	 */
  	private boolean getDptTaxInfo(List<TaxRateInfo> taxInfoList, DefaultTaxRate defaultTaxRate, ChangeableTaxRate changeableTaxRate,
  			Department department) {
  		String functionName = "getDptTaxInfo";
		tp.methodEnter(functionName).println("taxInfoList", taxInfoList).println("defaultTaxRate", defaultTaxRate)
			.println("changeableTaxRate", changeableTaxRate).println("department", department);

		if (taxRateInfoList != null) {
			for (TaxRateInfo TaxInfo : taxRateInfoList) {
				if (TaxInfo.getTaxId().equals(department.getTaxId())) {
					taxInfoList.add(TaxInfo);
				}
			}
		}
		if(taxInfoList.size() > 0){
			for (TaxRateInfo TaxInfo : taxInfoList) {
				if (TaxInfo.getSubNum1() == 0 && TaxInfo.getSubNum2() == 1) {
					if (defaultTaxRate != null) {
						return false;
					} else {
						defaultTaxRate = new DefaultTaxRate();
						defaultTaxRate.setRate(TaxInfo.getTaxRate());
					}
				}
				if (TaxInfo.getSubNum1() == 0 && TaxInfo.getSubNum2() == 0) {
					changeableTaxRate = new ChangeableTaxRate();
					changeableTaxRate.setRate(TaxInfo.getTaxRate());
				}
				if (TaxInfo.getSubNum1() == 1 && TaxInfo.getSubNum2() == 1) {
					if (defaultTaxRate != null) {
						return false;
					} else {
						defaultTaxRate = new DefaultTaxRate();
						defaultTaxRate.setRate(TaxInfo.getTaxRate());
						defaultTaxRate.setReceiptMark(TaxInfo.getSubCode1());
						defaultTaxRate.setReducedTaxRate(TaxInfo.getSubNum1());
					}
				}
				if (TaxInfo.getSubNum1() == 1 && TaxInfo.getSubNum2() == 0) {
					changeableTaxRate = new ChangeableTaxRate();
					changeableTaxRate.setRate(TaxInfo.getTaxRate());
					changeableTaxRate.setReceiptMark(TaxInfo.getSubCode1());
					changeableTaxRate.setReducedTaxRate(TaxInfo.getSubNum1());
				}
			}
		}
		if(changeableTaxRate != null || defaultTaxRate != null){
			department.setChangeableTaxRate(changeableTaxRate);
			department.setDefaultTaxRate(defaultTaxRate);
		}
		return true;
  	}
}
