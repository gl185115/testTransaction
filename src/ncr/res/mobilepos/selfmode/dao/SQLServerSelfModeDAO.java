/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.selfmode.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.selfmode.model.SelfMode;
import ncr.res.mobilepos.selfmode.model.SelfModeInfo;

/**
 * A Data Access Object implementation for Transfer transactions between smart
 * phone and POS.
 */
public class SQLServerSelfModeDAO extends AbstractDao implements ISelfModeDAO {
	private static final String PROG_NAME = "SelfModeDAO";
	/**
	 * The Database manager.
	 */
	private DBManager dbManager;
	/**
	 * The IOWriter to log.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
	/**
	 * The Trace Printer.
	 */
	private Trace.Printer tp;

	/**
	 * constructor.
	 * 
	 * @throws DaoException The exception thrown when error occur.
	 */
	public SQLServerSelfModeDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	}

	/**
	 * Gets the Database Manager for the Class.
	 *
	 * @return The Database Manager Object
	 */
	public final DBManager getDbManager() {
		return dbManager;
	}

	@Override
	public final ResultBase updateStatus(final SelfMode selfMode) throws DaoException {

		String functionName = "SQLServerSelfModeDAO.updateStatus";
		tp.methodEnter(functionName);
		tp.println("SelfMode", selfMode);

		ResultBase result = new ResultBase();
		Connection connection = null;
		PreparedStatement upadteStmt = null;
		SQLStatement sqlStatement = null;
		int updateCount = 0;
		try {

			connection = dbManager.getConnection();
			sqlStatement = SQLStatement.getInstance();

			upadteStmt = connection.prepareStatement(sqlStatement.getProperty("update-self-mode-status"));
			upadteStmt.setString(SQLStatement.PARAM1, selfMode.getCompanyId());
			upadteStmt.setString(SQLStatement.PARAM2, selfMode.getRetailStoreId());
			upadteStmt.setString(SQLStatement.PARAM3, selfMode.getWorkstationId());
			upadteStmt.setInt(SQLStatement.PARAM4, selfMode.getTraining());
			upadteStmt.setInt(SQLStatement.PARAM5, selfMode.getStatus());
			upadteStmt.setInt(SQLStatement.PARAM6, selfMode.getDetail());
			upadteStmt.setInt(SQLStatement.PARAM7, selfMode.getPrinter());
			upadteStmt.setInt(SQLStatement.PARAM8, selfMode.getCashChanger());
			upadteStmt.setString(SQLStatement.PARAM9, selfMode.getCashChangerCount());
			upadteStmt.setString(SQLStatement.PARAM10, selfMode.getCashChangerCountStatus());
			upadteStmt.setString(SQLStatement.PARAM11, selfMode.getMessage());
			upadteStmt.setInt(SQLStatement.PARAM12, selfMode.getAlert());
			upadteStmt.setString(SQLStatement.PARAM13, selfMode.getUpdateDateTime());
						
			
			updateCount = upadteStmt.executeUpdate();
			if (updateCount >= SQLResultsConstants.ONE_ROW_AFFECTED) {
				connection.commit();
				result.setMessage(ResultBase.RES_SUCCESS_MSG);
				result.setNCRWSSResultCode(ResultBase.RES_OK);
			} else {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
				tp.println("No row(s)( affected. Check TXL_SELFMODE_STATE table structure.");
			}
		} catch (SQLException sqlEx) {
			result.setMessage(sqlEx.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
					"SQL Exception Error occured." + " Failed to update status. \n" + sqlEx.getMessage());
		} catch (Exception ex) {
			result.setMessage(ex.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"SQL Exception Error occured." + " Failed to update status. \n" + ex.getMessage());
		} finally {
			closeConnectionObjects(connection, upadteStmt, null);
			tp.methodExit(result);
		}
		return result;
	}

	@Override
	public SelfModeInfo getStatus(String companyId, String retailStoreId, String workstationId) throws DaoException {

		String functionName = "SQLServerSelfModeDAO.getStatus";
		tp.methodEnter(functionName);
		tp.println("CompanyId", companyId).println("RetailStoreId", retailStoreId).println("WorkstationId",
				workstationId);

		SelfModeInfo result = new SelfModeInfo();
		Connection connection = null;
		PreparedStatement getStmt = null;
		SQLStatement sqlStatement = null;
		ResultSet resultset = null;

		JSONArray selfModeResults = new JSONArray();

		try {

			connection = dbManager.getConnection();
			sqlStatement = SQLStatement.getInstance();

			getStmt = connection.prepareStatement(sqlStatement.getProperty("get-self-mode-status"));
			getStmt.setString(SQLStatement.PARAM1, companyId);
			getStmt.setString(SQLStatement.PARAM2, retailStoreId);
			getStmt.setString(SQLStatement.PARAM3, workstationId);
			resultset = getStmt.executeQuery();

			while (resultset.next()) {
				JSONObject row = new JSONObject();
				row.put("CompanyId", resultset.getString("CompanyId"));
				row.put("RetailStoreId", resultset.getString("RetailStoreId"));
				row.put("WorkstationId", resultset.getString("WorkstationId"));
				row.put("Training", resultset.getString("Training"));
				row.put("Status", resultset.getString("Status"));
				row.put("Detail", resultset.getString("Detail"));
				row.put("Printer", resultset.getString("Printer"));
				row.put("CashChanger", resultset.getString("CashChanger"));
				row.put("CashChangerCount", resultset.getString("CashChangerCount"));
				row.put("CashChangerCountStatus", resultset.getString("CashChangerCountStatus"));
				row.put("Message", resultset.getString("Message"));
				row.put("Alert", resultset.getString("Alert"));
				row.put("UpdateDateTime", resultset.getString("UpdateDateTime"));
				selfModeResults.put(row);
			}
			result.setNCRWSSResultCode(ResultBase.RES_OK);
			result.setMessage(ResultBase.RES_SUCCESS_MSG);
			result.setSelfModeInfo(selfModeResults.toString());
		} catch (SQLException sqlEx) {
			result.setMessage(sqlEx.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
					"SQL Exception Error occured." + " Failed to get status. \n" + sqlEx.getMessage());
		} catch (Exception ex) {
			result.setMessage(ex.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"SQL Exception Error occured." + " Failed to get status. \n" + ex.getMessage());
		} finally {
			closeConnectionObjects(connection, getStmt, resultset);
			tp.methodExit(result);
		}
		return result;
	}

    // add history table
/*
	@Override
	public final ResultBase updateHistory(final SelfMode selfMode) throws DaoException {

		String functionName = "SQLServerSelfModeDAO.updateHistory";
		tp.methodEnter(functionName);
		tp.println("SelfMode", selfMode);

		ResultBase result = new ResultBase();
		Connection connection = null;
		PreparedStatement upadteStmt = null;
		SQLStatement sqlStatement = null;
		int updateCount = 0;
		try {

			connection = dbManager.getConnection();
			sqlStatement = SQLStatement.getInstance();

			upadteStmt = connection.prepareStatement(sqlStatement.getProperty("update-self-mode-status-history"));
			upadteStmt.setString(SQLStatement.PARAM1, selfMode.getCompanyId());
			upadteStmt.setString(SQLStatement.PARAM2, selfMode.getRetailStoreId());
			upadteStmt.setString(SQLStatement.PARAM3, selfMode.getWorkstationId());
			upadteStmt.setInt(SQLStatement.PARAM4, selfMode.getTraining());
			upadteStmt.setInt(SQLStatement.PARAM5, selfMode.getStatus());
			upadteStmt.setInt(SQLStatement.PARAM6, selfMode.getDetail());
			upadteStmt.setInt(SQLStatement.PARAM7, selfMode.getPrinter());
			upadteStmt.setInt(SQLStatement.PARAM8, selfMode.getCashChanger());
			upadteStmt.setString(SQLStatement.PARAM9, selfMode.getCashChangerCount());
			upadteStmt.setString(SQLStatement.PARAM10, selfMode.getCashChangerCountStatus());
			upadteStmt.setString(SQLStatement.PARAM11, selfMode.getMessage());
			upadteStmt.setInt(SQLStatement.PARAM12, selfMode.getAlert());
			upadteStmt.setString(SQLStatement.PARAM13, selfMode.getUpdateDateTime());
			upadteStmt.setString(SQLStatement.PARAM14, selfMode.getTotal());
			upadteStmt.setString(SQLStatement.PARAM15, selfMode.getItemCount());
			
			updateCount = upadteStmt.executeUpdate();
			if (updateCount >= SQLResultsConstants.ONE_ROW_AFFECTED) {
				connection.commit();
				result.setMessage(ResultBase.RES_SUCCESS_MSG);
				result.setNCRWSSResultCode(ResultBase.RES_OK);
			} else {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
				tp.println("No row(s)( affected. Check TXL_SELFMODE_STATE_HISTORY table structure.");
			}
		} catch (SQLException sqlEx) {
			result.setMessage(sqlEx.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_SQL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
					"SQL Exception Error occured." + " Failed to update status history. \n" + sqlEx.getMessage());
		} catch (Exception ex) {
			result.setMessage(ex.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"SQL Exception Error occured." + " Failed to update status history. \n" + ex.getMessage());
		} finally {
			closeConnectionObjects(connection, upadteStmt, null);
			tp.methodExit(result);
		}
		return result;
	}
*/	
}
