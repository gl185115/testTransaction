package ncr.res.mobilepos.uiconfig.dao;

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
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;

public class SQLServerUiConfigCommonDAO extends AbstractDao implements IUiConfigCommonDAO {
	private final String PROG_NAME = "UcfCmnDAO";

	/** The database manager. */
	private DBManager dbManager;
	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/**
	 * The Constructor of the Class.
	 *
	 * @throws DaoException
	 *             thrown when process fails.
	 */
	public SQLServerUiConfigCommonDAO() throws DaoException {
		dbManager = JndiDBManagerMSSqlServer.getInstance();
		tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * Gets the Database Manager for the Class.
	 *
	 * @return The Database Manager Object
	 */
	public final DBManager getDbManager() {
		return dbManager;
	}

	/**
	 * @return return the guest zone information
	 *
	 * @throws DaoException
	 *             Thrown when process fails.
	 */
	@Override
	public List<CompanyInfo> getCompanyInfo() throws DaoException {
		// TODO Auto-generated method stub
		String functionName = "getCompanyInfo";
		tp.methodEnter(functionName);

		List<CompanyInfo> companylist = null;
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement select = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("get-company-info"));
			result = select.executeQuery();
			while (result.next()) {
				if (companylist == null) {
					companylist = new ArrayList<CompanyInfo>();
				}
				CompanyInfo companyInfo = new CompanyInfo();
				companyInfo.setCompanyId(result.getString("CompanyId"));
				companyInfo.setCompanyName(result.getString("CompanyName"));
				companyInfo.setCompanyName(result.getString("CompanyKanaName"));
				companyInfo.setCompanyName(result.getString("CompanyShortName"));
				companyInfo.setCompanyName(result.getString("CompanyShortKanaName"));
				companylist.add(companyInfo);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get CompanyInfo list.", sqlEx);
			throw new DaoException("SQLException:" + " @SQLServerUiConfigCommonDAO.getCompanyInfo", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get CompanyInfo list.",
					ex);
			throw new DaoException("Exception:" + " @SQLServerUiConfigCommonDAO.getCompanyInfo", ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(companylist);
		}

		return companylist;
	}

	/**
	 * @return return the TableStore information
	 *
	 * @throws DaoException
	 *             Thrown when process fails.
	 */
	@Override
	public List<StoreEntry> getStoreEntryList(String companyId) throws DaoException {
		// TODO Auto-generated method stub
		String functionName = "getStoreEntryList";
		tp.methodEnter(functionName);
		tp.println("companyId", companyId);

		List<StoreEntry> storeEntryList = null;
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement select = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("get-store-info"));
			select.setString(SQLStatement.PARAM1, companyId);
			result = select.executeQuery();
			while (result.next()) {
				if (storeEntryList == null) {
					storeEntryList = new ArrayList<StoreEntry>();
				}
				StoreEntry storeEntry = new StoreEntry();
				storeEntry.setCompanyId(result.getString("CompanyId"));
				storeEntry.setAds(result.getString("Ads"));
				storeEntry.setCdMsg(result.getString("CdMsg"));
				storeEntry.setElectroFilePath(result.getString("ElectroFilePath"));
				storeEntry.setEventName(result.getString("EventName"));
				// storeEntry.setMessage(result.getString("Message"));
				storeEntry.setSalesSpaceName(result.getString("SalesSpaceName"));
				// storeEntry.setSostUpdDate(result.getString("SostUpdDate"));
				storeEntry.setStatus(result.getString("Status"));
				storeEntry.setStampTaxFilePath(result.getString("StampTaxFilePath"));
				storeEntry.setStoreAddr(result.getString("StoreAddr"));
				storeEntry.setStoreAddr1(result.getString("StoreAddr1"));
				storeEntry.setStoreAddr2(result.getString("StoreAddr2"));
				storeEntry.setStoreCompCode(result.getString("StoreCompCode"));
				storeEntry.setStoreFax(result.getString("StoreFax"));
				storeEntry.setStoreId(result.getString("StoreId"));
				storeEntry.setStoreKubun(result.getString("StoreKubun"));
				storeEntry.setStoreName(result.getString("StoreName"));
				storeEntry.setStoreShortName(result.getString("StoreShortName"));
				storeEntry.setStoreTel(result.getString("StoreTel"));
				storeEntry.setStoreType(result.getString("StoreType"));
				storeEntry.setStoreUrl(result.getString("StoreUrl"));
				storeEntry.setStoreZip(result.getString("StoreZip"));
				storeEntry.setSubCode1(result.getString("SubCode1"));
				storeEntry.setSubCode2(result.getString("SubCode2"));
				storeEntry.setSubCode3(result.getString("SubCode3"));
				storeEntry.setSubCode4(result.getString("SubCode4"));
				storeEntry.setSubCode5(result.getString("SubCode5"));
				storeEntry.setSubCode6(result.getString("SubCode6"));
				storeEntry.setSubCode7(result.getString("SubCode7"));
				storeEntry.setSubCode8(result.getString("SubCode8"));
				storeEntry.setSubCode9(result.getString("SubCode9"));
				storeEntry.setSubCode10(result.getString("SubCode10"));
				storeEntry.setSubCode11(result.getString("SubCode11"));
				storeEntry.setSubCode12(result.getString("SubCode12"));
				storeEntry.setSubCode13(result.getString("SubCode13"));
				storeEntry.setSubCode14(result.getString("SubCode14"));
				storeEntry.setSubCode15(result.getString("SubCode15"));
				storeEntry.setSubCode16(result.getString("SubCode16"));
				storeEntry.setSubCode17(result.getString("SubCode17"));
				storeEntry.setSubCode18(result.getString("SubCode18"));
				storeEntry.setSubCode19(result.getString("SubCode19"));
				storeEntry.setSubCode20(result.getString("SubCode20"));
				storeEntry.setSubCode21(result.getString("SubCode21"));
				storeEntry.setSubCode22(result.getString("SubCode22"));
				storeEntry.setSubCode23(result.getString("SubCode23"));
				storeEntry.setSubCode24(result.getString("SubCode24"));
				storeEntry.setSubCode25(result.getString("SubCode25"));
				storeEntry.setSubNum1(result.getString("SubNum1"));
				storeEntry.setSubNum2(result.getString("SubNum2"));
				storeEntry.setSubNum3(result.getString("SubNum3"));
				storeEntry.setSubNum4(result.getString("SubNum4"));
				storeEntry.setSubNum5(result.getString("SubNum5"));
				storeEntry.setSubNum6(result.getString("SubNum6"));
				storeEntry.setSubNum7(result.getString("SubNum7"));
				storeEntry.setSubNum8(result.getString("SubNum8"));
				storeEntry.setSubNum9(result.getString("SubNum9"));
				storeEntry.setSubNum10(result.getString("SubNum10"));
				storeEntry.setSubNum11(result.getString("SubNum11"));
				storeEntry.setSubNum12(result.getString("SubNum12"));
				storeEntry.setSubNum13(result.getString("SubNum13"));
				storeEntry.setSubNum14(result.getString("SubNum14"));
				storeEntry.setSubNum15(result.getString("SubNum15"));
				storeEntryList.add(storeEntry);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get storeEntry list.", sqlEx);
			throw new DaoException("SQLException:" + " @SQLServerUiConfigCommonDAO.getStoreEntryList", sqlEx);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get storeEntry list.",
					ex);
			throw new DaoException("Exception:" + " @SQLServerUiConfigCommonDAO.getStoreEntryList", ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			tp.methodExit(storeEntryList);
		}

		return storeEntryList;
	}

}
