/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerPOSMailInfoDAO
 *
 * DAO which handles database information of POS Mail Info.
 *
 */
/** ADD BGN 情報伝達機能 **/
package ncr.res.mobilepos.posmailinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;

import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
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
	private static SimpleDateFormat inSDF = new SimpleDateFormat("yyyy-mm-dd");
	private static SimpleDateFormat outSDF = new SimpleDateFormat("yyyy/mm/dd");

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

		PreparedStatement selectStmnt = null;
		ResultSet resultSet = null;
		Connection connection = null;
		
		JSONData posMailInfo = new JSONData();
		JSONArray infoData = new JSONArray();
		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			
			selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-pos-mail-info"));
			selectStmnt.setString(SQLStatement.PARAM1, companyId);
			selectStmnt.setString(SQLStatement.PARAM2, retailStoreId);
			selectStmnt.setString(SQLStatement.PARAM3, workstationId);
			selectStmnt.setString(SQLStatement.PARAM4, businessDate);
			resultSet = selectStmnt.executeQuery();

			JSONObject rowData = null;
			String outputDate = "";
			while(resultSet.next()) {
				rowData = new JSONObject();
				rowData.put("RecordId", resultSet.getString("RecordId"));				
				rowData.put("MailSubject", resultSet.getString("MailSubject"));
				rowData.put("MailBody", resultSet.getString("MailBody"));
				rowData.put("RegOpeCode", resultSet.getString("RegOpeCode"));
				rowData.put("RegOpeName",  resultSet.getString("RegOpeName"));

				Date date = inSDF.parse(resultSet.getString("StartDate"));
				outputDate = outSDF.format(date);
				rowData.put("StartDate", outputDate);

				date = inSDF.parse(resultSet.getString("EndDate"));
				outputDate = outSDF.format(date);
				rowData.put("EndDate", outputDate);				

				infoData.add(rowData);
			}
			posMailInfo.setInfoData(infoData.toString());
			posMailInfo.setNCRWSSResultCode(ResultBase.RES_POSMAIL_INFO_OK);
			posMailInfo.setNCRWSSExtendedResultCode(ResultBase.RES_POSMAIL_INFO_OK);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get list of messages.",
                    e);
            throw new DaoException("Exception:" + " @SQLServerPOSMailInfoDAO.getPOSMailInfo", e);
		} finally {
			closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(posMailInfo);
		}
		return posMailInfo;
	}
}
/** ADD END 情報伝達機能 **/