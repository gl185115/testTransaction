/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerPOSMailInfoDAO
 *
 * DAO which handles database information of POS Mail Info.
 *
 */
package ncr.res.mobilepos.posmailinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONException;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.webserviceif.model.JSONData;

/**
 * A data access obbject implementation for POS Mail notification messages.
 * 
 * @see IPOSMailInfoDAO
 */
public class SQLServerPOSMailInfoDAO extends AbstractDao implements IPOSMailInfoDAO {
	private DBManager dbManager;
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	private static final String PROG_NAME = "POSMailInfoDAO";
	private Trace.Printer tp;	
	private static final String parse(Date date) throws ParseException {
        return new SimpleDateFormat("yyyy/mm/dd").format(date);
    }

	public SQLServerPOSMailInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

	public final DBManager getDBManager() {
        return dbManager;
    }
	/**
	 * Gets the list of POS Mail notification messages.
	 * 
     * @param companyId
     * @param storeId
     * @param workstationId
     * @param businessDate
     * @return posMailInfo
     * @throws DaoException
     */
	@Override
	public JSONData getPOSMailInfo(String companyId, String retailStoreId, String workstationId, String businessDate)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName)
			.println("companyId", companyId)
			.println("retailStoreId", retailStoreId)
			.println("workstationId", workstationId)
			.println("businessDate", businessDate);

		JSONData posMailInfo = new JSONData();
		JSONArray infoData = new JSONArray();
		SQLStatement sqlStatement = SQLStatement.getInstance();
		
		try (Connection connection = dbManager.getConnection();
				PreparedStatement selectStmnt = getPOSMailInfoPreparedStatement(
						companyId, retailStoreId, workstationId, businessDate,
						sqlStatement, connection);
				ResultSet resultSet = selectStmnt.executeQuery();) {
			while (resultSet.next()) {
				JSONObject rowData = new JSONObject();
				rowData.put("RecordId", resultSet.getString("RecordId"));
				rowData.put("MailSubject", resultSet.getString("MailSubject"));
				rowData.put("MailBody", resultSet.getString("MailBody"));
				rowData.put("RegOpeCode", resultSet.getString("RegOpeCode"));
				rowData.put("RegOpeName", resultSet.getString("RegOpeName"));

				Date date = DateFormatUtility.parse(resultSet.getString("StartDate"),"yyyy-mm-dd");
				String outputDate = SQLServerPOSMailInfoDAO.parse(date);
				rowData.put("StartDate", outputDate);

				date = DateFormatUtility.parse(resultSet.getString("EndDate"),"yyyy-mm-dd");
				outputDate = SQLServerPOSMailInfoDAO.parse(date);
				rowData.put("EndDate", outputDate);

				infoData.add(rowData);
			}
			posMailInfo.setInfoData(infoData.toString());
		} catch (JSONException jsonEx) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_JSON,
					"Failed to get list of messages.\n" + jsonEx.getMessage());
			throw new DaoException("JSONException: @getPOSMailInfo ", jsonEx);
		} catch (ParseException parseEx) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_PARSE,
					"Failed to get list of messages.\n" + parseEx.getMessage());
			throw new DaoException("ParseException: @getPOSMailInfo ", parseEx);
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
					"Failed to get list of messages.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @getPOSMailInfo ", sqlEx);
		} finally {
			tp.methodExit(posMailInfo);
		}
		return posMailInfo;
	}
	/**
	 * Sets sql parameter of "get-pos-mail-info"
	 * @param companyId
	 * @param retailStoreId
	 * @param workstationId
	 * @param businessDate
	 * @param sqlStmt
	 * @param connection
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement getPOSMailInfoPreparedStatement(String companyId,
			String retailStoreId, String workstationId, String businessDate,
			SQLStatement sqlStmt, Connection connection) throws SQLException {
		PreparedStatement selectStmnt = connection.prepareStatement(sqlStmt
				.getProperty("get-pos-mail-info"));
		selectStmnt.setString(SQLStatement.PARAM1, companyId);
		selectStmnt.setString(SQLStatement.PARAM2, retailStoreId);
		selectStmnt.setString(SQLStatement.PARAM3, workstationId);
		selectStmnt.setString(SQLStatement.PARAM4, businessDate);
		return selectStmnt;
	}
}