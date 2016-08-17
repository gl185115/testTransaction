package ncr.res.mobilepos.deviceinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * SQL SignatureLink Data Access Object.
 * @see ISignatureLinkDAO.
 * @author RD185102
 */
public class SQLSignatureLinkDAO extends AbstractDao implements ILinkDAO {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * ProgName assignment for PeripheralDeviceControlDAO.
     */
    private static final String PROG_NAME = "SignDAO";
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;
    /**
     * Class instance of trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * SQLSignatureLinkDAO default constructor.
     * @throws DaoException database exception
     */
    public SQLSignatureLinkDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
    @Override
    
    public final POSLinkInfo getLinkItem(final String storeId,
            final String posLinkId) throws SQLException, DaoException {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", storeId)
				.println("POSLinkID", posLinkId);

        POSLinkInfo posLinkInfo = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-signaturelinkinfo"));
            select.setString(SQLStatement.PARAM1, storeId);
            select.setString(SQLStatement.PARAM2, posLinkId);

            result = select.executeQuery();

            if (result.next()) {
                posLinkInfo = new POSLinkInfo();
                posLinkInfo.setRetailStoreId(storeId);
                posLinkInfo.setPosLinkId(posLinkId);
                posLinkInfo.setLinkName(result.getString(
                        result.findColumn("DisplayName")).trim());
                posLinkInfo.setStatus(result.getString(
                        result.findColumn("Status")).trim());
            } else {
                tp.println("Failed to retrieve Signature Link.");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to get signature link.", ex);
			throw new DaoException("SQLStatementException: @"
					+ "SQLSignatureLinkDAO." + functionName, ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get signature link.", ex);
			throw new DaoException("SQLException: @" + "SQLSignatureLinkDAO."
					+ functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get signature link.", ex);
			throw new DaoException("Exception: @" + "SQLSignatureLinkDAO."
					+ functionName, ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			
			if (posLinkInfo != null) {
				tp.methodExit(posLinkInfo.toString());
			} else {
				tp.methodExit("null");
			}
		}
		return posLinkInfo;
	}

    @Override
	public final ResultBase createLink(final String storeId,
			final String posLinkId, final POSLinkInfo posLinkInfo)
			throws Exception {
        
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("StoreId", storeId)
				.println("PosLinkId", posLinkId)
				.println("POSLinkInfo", posLinkInfo.toString());
        
        ResultBase resultBase = new ResultBase();
        int result = 0;
        Connection connection = null;
        PreparedStatement create = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            create = connection.prepareStatement(sqlStatement
                    .getProperty("create-signaturelink"));
            create.setString(SQLStatement.PARAM1, storeId);
            create.setString(SQLStatement.PARAM2, posLinkId);
            create.setString(SQLStatement.PARAM3, posLinkInfo.getLinkName());
            create.setString(SQLStatement.PARAM4, posLinkInfo.getUpdAppId());
            create.setString(SQLStatement.PARAM5, posLinkInfo.getUpdOpeCode());

            result = create.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage("Success Creation of Signature Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultBase.setMessage("Failed to create Signature link");
                tp.println("Failed to create Signature link");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to create signature link.", ex);
			throw new DaoException("SQLStatementException:"
					+ " @SQLSignatureLinkDAO." + functionName, ex);
		} catch (SQLException ex) {
			if (ex.getErrorCode() != Math
					.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
						+ ": Failed to create signature link.", ex);
				rollBack(connection, functionName, ex);
				throw new DaoException("SQLException: @" + functionName, ex);
			}
			resultBase = new ResultBase(ResultBase.RES_LINK_ALREADYEXISTS,
					ResultBase.RES_ERROR_SQL, ex);
			tp.println("Duplicate Entry of Signature Link.");
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to create signature link.", ex);
			throw new DaoException("Exception: @" + functionName, ex);
		} finally {
			closeConnectionObjects(connection, create);
			
			tp.methodExit(resultBase);
		}

		return resultBase;
	}

    @Override
	public final ViewPosLinkInfo updateLink(final String storeId,
			final String posLinkID, final POSLinkInfo posLinkInfo,
			Connection connection) throws DaoException {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeID", storeId)
				.println("posLinkID", posLinkID)
				.println("posLinkInfo", posLinkInfo);
        
        ViewPosLinkInfo newPosLinkInfo = new ViewPosLinkInfo();
        ResultSet result = null;
        PreparedStatement update = null;

        try {
        	if(connection == null){
        		connection = dbManager.getConnection();
        	}

            SQLStatement sqlStatement = SQLStatement.getInstance();
            update = connection.prepareStatement(sqlStatement
                    .getProperty("update-signaturelink"));
            update.setString(SQLStatement.PARAM1,
                    posLinkInfo.getRetailStoreId());
            update.setString(SQLStatement.PARAM2, posLinkInfo.getPosLinkId());
            update.setString(SQLStatement.PARAM3, posLinkInfo.getLinkName());
            update.setString(SQLStatement.PARAM4, posLinkInfo.getUpdAppId());
            update.setString(SQLStatement.PARAM5, posLinkInfo.getUpdOpeCode());
            update.setString(SQLStatement.PARAM6, storeId);
            update.setString(SQLStatement.PARAM7, posLinkID);

            result = update.executeQuery();

            if (result.next()) {
                POSLinkInfo posLink = new POSLinkInfo();
                posLink.setRetailStoreId(result.getString("StoreId").trim());
                posLink.setPosLinkId(result.getString("Id").trim());
                posLink.setLinkName(result.getString("DisplayName").trim());
                newPosLinkInfo.setPosLinkInfo(posLink);
            } else {
                newPosLinkInfo
                        .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                tp.println("Signature Link information not found.");
            }

            connection.commit();

		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to update signature link.", ex);
			throw new DaoException(
					"SQLStatementException: @SQLSignatureLinkDAO."
							+ functionName, ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update signature link.", ex);

			if (ex.getErrorCode() == Math
					.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				newPosLinkInfo = new ViewPosLinkInfo(
						ResultBase.RES_LINK_ALREADYEXISTS,
						ResultBase.RES_ERROR_SQL, ex);
			} else {
				rollBack(connection, functionName, ex);
				throw new DaoException("SQLException: @SQLSignatureLinkDAO."
						+ functionName, ex);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update signature link.", ex);
			throw new DaoException("Exception: @SQLSignatureLinkDAO."
					+ functionName, ex);
		} finally {
			closeConnectionObjects(connection, update, result);
			
			tp.methodExit(newPosLinkInfo);
		}
		return newPosLinkInfo;
	}

    @Override
	public final List<POSLinkInfo> getLinks(final String storeId,
			final String key, final String name, final int limit)
			throws SQLException, DaoException {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", storeId)
				.println("Key", key).println("Name", name)
				.println("limit", limit);

		List<POSLinkInfo> links = new ArrayList<POSLinkInfo>();
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement select = null;
		
        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            boolean selectAll = false;
            
            if(storeId != null && storeId.trim().isEmpty()){
                select = connection.prepareStatement(sqlStatement.getProperty("get-all-signaturelinks"));
                selectAll = true;
            }else{
                select = connection.prepareStatement(sqlStatement
                    .getProperty("get-SignatureLinks"));
                select.setString(SQLStatement.PARAM1, storeId);
            }
            result = select.executeQuery();

            while (result.next()) {
                POSLinkInfo posLinkInfo = new POSLinkInfo();
                posLinkInfo.setPosLinkId(result.getString(
                        result.findColumn("Id")).trim());
                posLinkInfo.setLinkName(result.getString(
                        result.findColumn("Displayname")).trim());
                if(selectAll){
                    posLinkInfo.setRetailStoreId(result.getString(
                            result.findColumn("StoreId")).trim());
                }
                links.add(posLinkInfo);
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to get list of signature links.",
					ex);
			throw new DaoException(
					"SQLStatementException: @SQLSignatureLinkDAO."
							+ functionName, ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get list of signature links.", ex);
			throw new DaoException("SQLException: @SQLSignatureLinkDAO."
					+ functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get list of signature links.", ex);
			throw new DaoException("Exception: @SQLSignatureLinkDAO."
					+ functionName, ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			
			tp.methodExit("Count:" + links.size());
		}
		return links;
	}

    @Override
	public final ResultBase deleteLink(final String storeId,
			final String posLinkId, final String appId, final String opeCode)
			throws Exception {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", storeId)
				.println("POSLinkID", posLinkId).println("AppId", appId)
				.println("OpeCode", opeCode);

		ResultBase resultBase = new ResultBase();
		int result = 0;
		Connection connection = null;
		PreparedStatement delete = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            delete = connection.prepareStatement(
                    sqlStatement.getProperty("delete-signaturelink"));
            delete.setString(SQLStatement.PARAM1, appId);
            delete.setString(SQLStatement.PARAM2, opeCode);
            delete.setString(SQLStatement.PARAM3, storeId);
            delete.setString(SQLStatement.PARAM4, posLinkId);

            result = delete.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage("Success Delete of Signature Link.");
                tp.println("Success Delete of Signature Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                resultBase.setMessage(
                        "Failed to delete Signature Link");
                tp.println("Failed to delete Signature Link");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to delete signature link.", ex);
			throw new DaoException("SQLStatementException:"
					+ " @SQLSignatureLinkDAO." + functionName, ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to delete signature link.", ex);
			rollBack(connection, functionName, ex);
			throw new DaoException("SQLException: @" + functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to delete signature link.", ex);
			throw new Exception("Exception: @" + functionName, ex);
		} finally {
			closeConnectionObjects(connection, delete);
			
			tp.methodExit(resultBase);
		}
		return resultBase;
	}
}
