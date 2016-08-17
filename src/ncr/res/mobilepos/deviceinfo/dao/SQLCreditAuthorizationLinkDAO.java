package ncr.res.mobilepos.deviceinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
/**
 * SQLCreditAuthorizationLinkDAO data access object.
 * @see ICreditAuthorizationLinkDAO
 */
public class SQLCreditAuthorizationLinkDAO
    extends AbstractDao implements ILinkDAO {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * Program name 
     */
    private static final String PROG_NAME = "CALDao";
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;
    /**
     * class instance of trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * SQLCreditAuthorizationLinkDAO default constructor.
     * @throws DaoException database exception
     */
    public SQLCreditAuthorizationLinkDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }


    @Override
	public final ResultBase createLink(final String storeId,
			final String posLinkId, final POSLinkInfo posLinkInfo)
			throws SQLException, DaoException {
        
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
            create = connection.prepareStatement(
                    sqlStatement.getProperty("create-authorizationlink"));
            create.setString(SQLStatement.PARAM1, storeId);
            create.setString(SQLStatement.PARAM2, posLinkId);
            create.setString(SQLStatement.PARAM3, posLinkInfo.getLinkName());
            create.setString(SQLStatement.PARAM4, posLinkInfo.getUpdAppId());
            create.setString(SQLStatement.PARAM5, posLinkInfo.getUpdOpeCode());

            result = create.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage(
                        "Success Creation of Authorization Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultBase.setMessage(
                        "Failed to create authorization link");
                tp.println("Failed to create authorization link");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName
							+ ": Failed to create credit authorization link.",
					ex);
			throw new DaoException(ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to create credit authorization link.", ex);
			if (ex.getErrorCode() != Math
					.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				rollBack(connection, functionName, ex);
				throw new DaoException(ex);
			}
			resultBase = new ResultBase(ResultBase.RES_LINK_ALREADYEXISTS,
					ResultBase.RES_ERROR_SQL, ex);
			tp.println("Duplicate Entry of Authorization Link.");
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to create credit authorization link.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, create);
			
			tp.methodExit(resultBase);
		}

		return resultBase;
	}

    @Override
	public final POSLinkInfo getLinkItem(final String retailstoreid,
			final String poslinkid) throws SQLException, DaoException {
        
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("RetailStoreID", retailstoreid)
				.println("POSLinkID", poslinkid);

        POSLinkInfo posLinkInfo = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-authorizationlink"));
            select.setString(SQLStatement.PARAM1, retailstoreid);
            select.setString(SQLStatement.PARAM2, poslinkid);

            result = select.executeQuery();

            if (result.next()) {
                posLinkInfo = new POSLinkInfo();
                posLinkInfo.setRetailStoreId(retailstoreid);
                posLinkInfo.setPosLinkId(poslinkid);
                posLinkInfo.setLinkName(result.getString(
                        result.findColumn("DisplayName")).trim());
                posLinkInfo.setStatus(result.getString(
                        result.findColumn("Status")).trim());
            } else {
                tp.println("Failed to retrieve Authorization Link.");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(
					PROG_NAME,
					Logger.RES_EXCEP_SQLSTATEMENT,
					functionName + ": Failed to get credit authorization link.",
					ex);
			throw new DaoException(ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get credit authorization link.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get credit authorization link.", ex);
			throw new DaoException(ex);
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
	public final ViewPosLinkInfo updateLink(final String storeID,
			final String posLinkID, final POSLinkInfo posLinkInfo,
			Connection connection) throws DaoException {
        
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("storeID", storeID)
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
                    .getProperty("update-authorizationlink"));
            update.setString(SQLStatement.PARAM1,
                    posLinkInfo.getRetailStoreId());
            update.setString(SQLStatement.PARAM2, posLinkInfo.getPosLinkId());
            update.setString(SQLStatement.PARAM3, posLinkInfo.getLinkName());
            update.setString(SQLStatement.PARAM4, posLinkInfo.getUpdAppId());
            update.setString(SQLStatement.PARAM5, posLinkInfo.getUpdOpeCode());
            update.setString(SQLStatement.PARAM6, storeID);
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
                tp.println("AuthorizationLink information not found.");
            }

            connection.commit();

		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName
							+ ": Failed to update credit authorization link.",
					ex);
			throw new DaoException(ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to update credit authorization link.", ex);
			if (ex.getErrorCode() == Math
					.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				newPosLinkInfo = new ViewPosLinkInfo(
						ResultBase.RES_LINK_ALREADYEXISTS,
						ResultBase.RES_ERROR_SQL, ex);
			} else {
				rollBack(connection, functionName, ex);
				throw new DaoException(ex);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to update credit authorization link.", ex);
			throw new DaoException(ex);
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
				.println("Limit", limit);

        List<POSLinkInfo> links = new ArrayList<POSLinkInfo>();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            
            boolean selectAll = false;
            
            if(storeId != null && storeId.trim().isEmpty()){
                select = connection.prepareStatement(sqlStatement
                        .getProperty("get-all-authorizationlinks"));
                selectAll = true;
            }else{
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-authorizationlinks"));
            select.setString(SQLStatement.PARAM1, storeId);
            }
            result = select.executeQuery();

            while (result.next()) {
                POSLinkInfo posLinkInfo = new POSLinkInfo();
                posLinkInfo.setPosLinkId(result.getString("Id").trim());
                posLinkInfo.setLinkName(result.getString("DisplayName").trim());
                if(selectAll){
                    posLinkInfo.setRetailStoreId(result.getString("StoreId").trim());
                }
                links.add(posLinkInfo);
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName
							+ ": Failed to get credit authorization links.", ex);
			throw new DaoException(ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get credit authorization links.", ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get credit authorization links.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, select, result);
			
			tp.methodExit("Count:" + Integer.toString(links.size()));
		}
		return links;
	}

    @Override
	public final ResultBase deleteLink(final String storeId,
			final String posLinkId, final String appId, final String opeCode)
			throws SQLException, DaoException {
        
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("StoreId", storeId)
				.println("PosLinkId", posLinkId).println("UpdAppId", appId)
				.println("UpdOpeCode", opeCode);

		ResultBase resultBase = new ResultBase();
        int result = 0;
        Connection connection = null;
        PreparedStatement delete = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            delete = connection.prepareStatement(
                    sqlStatement.getProperty("delete-authorizationlink"));
            delete.setString(SQLStatement.PARAM1, appId);
            delete.setString(SQLStatement.PARAM2, opeCode);
            delete.setString(SQLStatement.PARAM3, storeId);
            delete.setString(SQLStatement.PARAM4, posLinkId);
            
            result = delete.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage(
                        "Success Delete of Credit Authorization Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                resultBase.setMessage(
                        "Failed to delete credit authorization link");
                tp.println("Failed to delete credit authorization link");
            }
		} catch (SQLStatementException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
					functionName
							+ ": Failed to delete credit authorization link.",
					ex);
			throw new DaoException(ex);
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to delete credit authorization link.", ex);
			rollBack(connection, functionName, ex);
			throw new DaoException(ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to delete credit authorization link.", ex);
			throw new DaoException(ex);
		} finally {
			closeConnectionObjects(connection, delete);
			
			tp.methodExit(resultBase);
		}

		return resultBase;
	}
}
