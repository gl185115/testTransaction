package ncr.res.mobilepos.deviceinfo.dao;

import java.io.IOException;
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
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * SQLQueueBusterLinkDAO data access object.
 *
 * @see IQueueBusterLinkDAO
 */
public class SQLQueueBusterLinkDAO extends AbstractDao implements ILinkDAO {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * Progname assignment for PeripheralDeviceControlDAO.
     */
    private static final String PROG_NAME = "DQBLDao";
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;
    /**
     * class instance of trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * SQLQueueBusterLinkDAO default constructor.
     *
     * @throws DaoException
     *             database exception
     */
    public SQLQueueBusterLinkDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    @Override
	public final ResultBase createLink(final String storeid,
			final String poslinkid, final POSLinkInfo poslinkinfo)
			throws SQLException, DaoException, IOException {

		String functionName = "createLink";
		tp.methodEnter(functionName).println("StoreId", storeid)
				.println("PosLinkId", poslinkid)
				.println("POSLinkInfo", poslinkinfo.toString());

		ResultBase resultBase = new ResultBase();
        int result = 0;
        Connection connection = null;
        PreparedStatement create = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            create = connection.prepareStatement(sqlStatement.getProperty("create-queuebusterlink"));
            create.setString(SQLStatement.PARAM1, storeid);
            create.setString(SQLStatement.PARAM2, poslinkid);
            create.setString(SQLStatement.PARAM3, poslinkinfo.getLinkName());
            create.setString(SQLStatement.PARAM4, poslinkinfo.getUpdAppId());
            create.setString(SQLStatement.PARAM5, poslinkinfo.getUpdOpeCode());

            result = create.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage("Success Creation of QueueBuster Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultBase.setMessage("Failed to create queue buster link");
                tp.println("Failed to create queue buster link");
            }
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to create queuebusterlink.", ex);
			if (ex.getErrorCode() != Math
					.abs(SQLResultsConstants.ROW_DUPLICATE)) {
				throw new DaoException("SQLException: @" + functionName, ex);
			}
			resultBase.setNCRWSSResultCode(ResultBase.RES_LINK_ALREADYEXISTS);
			tp.println("Duplicate Entry of QueueBuster Link.");

			POSLinkInfo linkInfo = getLinkItem(storeid, poslinkid);
			if ("Deleted".equalsIgnoreCase(linkInfo.getStatus())) {
				closeConnectionObjects(connection, create);
				connection = dbManager.getConnection();
				ResultBase activateResult = activateLink(storeid, poslinkid,
						connection);
				if (activateResult.getNCRWSSResultCode() == 0) {
					ViewPosLinkInfo viewLinkInfo = updateLink(storeid,
							poslinkid, poslinkinfo, connection);
					if (viewLinkInfo.getNCRWSSResultCode() == 0) {
						resultBase.setNCRWSSResultCode(viewLinkInfo
								.getNCRWSSResultCode());
					} else {
						rollBack(connection, "updateDevice", null);
					}

				}
				closeConnectionObjects(connection, create);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to create queuebusterlink.", ex);
			throw new DaoException("Exception: @" + functionName, ex);
		} finally {
			closeConnectionObjects(connection, create);

			tp.methodExit(resultBase);
		}

		return resultBase;
	}

        public final ViewPosLinkInfo updateLink(final String storeID,
                final String posLinkID, final POSLinkInfo posLinkInfo, Connection connection)
                throws DaoException, SQLException {
            String functionName = "updateLink";
            tp.methodEnter(functionName).println("storeID", storeID)
                    .println("posLinkID", posLinkID)
                    .println("posLinkInfo", posLinkInfo);

            ViewPosLinkInfo newPosLinkInfo = new ViewPosLinkInfo();
            ResultSet result = null;
            PreparedStatement update = null;
            boolean isNewConnection = false;
            try {

            	if(connection == null){
            		connection = dbManager.getConnection();
            		isNewConnection = true;
            	}

                SQLStatement sqlStatement = SQLStatement.getInstance();
                update = connection.prepareStatement(sqlStatement
                        .getProperty("update-queuebusterlink"));
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
                    final String linkName = result.getString("DisplayName");
                    posLink.setLinkName((linkName != null) ? linkName.trim() : "");
                    newPosLinkInfo.setPosLinkInfo(posLink);
                } else {
                    newPosLinkInfo
                            .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                    tp.println("QueuebusterLink information not found.");
                }

                connection.commit();

            } catch (SQLException ex) {
                LOGGER.logAlert(
                        PROG_NAME,
                        functionName,
                        Logger.RES_EXCEP_SQL,
                        "Failed to update queuebuster information: "
                                + ex.getMessage());
                if (ex.getErrorCode() == Math
                        .abs(SQLResultsConstants.ROW_DUPLICATE)) {
                    newPosLinkInfo
                            .setNCRWSSResultCode(ResultBase.RES_LINK_ALREADYEXISTS);

                    POSLinkInfo prevLink = getLinkItem(storeID, posLinkID);
                    String storeIdToSet = posLinkInfo.getRetailStoreId()!= null? posLinkInfo.getRetailStoreId():storeID;
                	String linkIdToSet = posLinkInfo.getPosLinkId()!= null? posLinkInfo.getPosLinkId():posLinkID;
                    POSLinkInfo linkInfo = getLinkItem(storeIdToSet, linkIdToSet);
                    if("Deleted".equalsIgnoreCase(linkInfo.getStatus()) ){
                    	closeConnectionObjects(connection, update);
                    	connection = dbManager.getConnection();
                    	if(posLinkInfo.getLinkName() == null){
                    		posLinkInfo.setLinkName(prevLink.getLinkName());
                    	}
                        ResultBase activateResult = activateLink(storeIdToSet, linkIdToSet, connection);
                        if(activateResult.getNCRWSSResultCode() == 0){
                        	ViewPosLinkInfo viewPosLinkInfo = updateLink(storeIdToSet, linkIdToSet, posLinkInfo, connection);
                        	if(viewPosLinkInfo.getNCRWSSResultCode() == 0){
                        		ResultBase deleteResult = deleteLink(storeID, posLinkID, posLinkInfo.getUpdAppId(), posLinkInfo.getUpdOpeCode());
                        		if(deleteResult.getNCRWSSResultCode() == 0){
                        			newPosLinkInfo = viewPosLinkInfo;
                        		}else{
                        			rollBack(connection, "deleteLink", null);
                        		}
                        	}else{
                        		rollBack(connection, "updateLink", null);
                        	}

                        }

                    }
                } else {

                    throw new DaoException("SQLException:"
                            + " @SQLQueueBusterLinkDAO.updateLink", ex);
                }
            } catch (Exception ex) {
            	rollBack(connection, functionName, ex);
                LOGGER.logAlert(
                        PROG_NAME,
                        functionName,
                        Logger.RES_EXCEP_GENERAL,
                        "Failed to update queuebuster information: "
                                + ex.getMessage());
                throw new DaoException("SQLException:"
                        + " @SQLQueueBusterLinkDAO.updateLink", ex);
            } finally {
				if(isNewConnection){
            		closeConnectionObjects(connection, update, result);
            	}else{
            		closeConnectionObjects(null, update, result);
            	}

                tp.methodExit(newPosLinkInfo);
            }

            return newPosLinkInfo;
        }


    @Override
	public final ResultBase deleteLink(final String storeid,
			final String poslinkid, final String appId, final String opeCode)
			throws SQLException, DaoException {

    	String functionName = "deleteLink";
		tp.methodEnter(functionName).println("StoreId", storeid)
				.println("PosLinkId", poslinkid).println("UpdAppId", appId)
				.println("UpdOpeCode", opeCode);

		ResultBase resultBase = new ResultBase();
        int result = 0;
        Connection connection = null;
        PreparedStatement delete = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            delete = connection.prepareStatement(sqlStatement
                    .getProperty("delete-queuebusterlink"));
            delete.setString(SQLStatement.PARAM1, appId);
            delete.setString(SQLStatement.PARAM2, opeCode);
            delete.setString(SQLStatement.PARAM3, storeid);
            delete.setString(SQLStatement.PARAM4, poslinkid);

            result = delete.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage("Success Delete of QueueBuster Link.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                resultBase.setMessage("Failed to delete queue buster link");
                tp.println("Failed to delete queue buster link");
            }
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to delete queuebusterlink.", ex);
			rollBack(connection, functionName, ex);
			throw new DaoException("SQLException: @SQLQueueBusterLinkDAO."
					+ functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to delete queuebusterlink.", ex);
			throw new DaoException("SQLException: @SQLQueueBusterLinkDAO."
					+ functionName, ex);
		} finally {
			closeConnectionObjects(connection, delete);

			tp.methodExit(resultBase);
		}

		return resultBase;
	}

    @Override
	public final POSLinkInfo getLinkItem(final String retailstoreid,
			final String poslinkid) throws SQLException, DaoException {

		String functionName = "getLinkItem";
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
                    .getProperty("get-queuebusterlinkinfo"));
            select.setString(SQLStatement.PARAM1, retailstoreid);
            select.setString(SQLStatement.PARAM2, poslinkid);

            result = select.executeQuery();
            connection.commit();

            if (result.next()) {
                posLinkInfo = new POSLinkInfo();
                posLinkInfo.setRetailStoreId(retailstoreid);
                posLinkInfo.setPosLinkId(poslinkid);
                posLinkInfo.setLinkName(result.getString(
                        result.findColumn("DisplayName")).trim());
                posLinkInfo.setStatus(result.getString(
                        result.findColumn("Status")).trim());
            } else {
                tp.println("Failed to retrieve QueueBuster Link.");
            }
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get queuebusterlink.", ex);
			throw new DaoException("SQLException: @SQLQueueBusterLinkDAO."
					+ functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get queuebusterlink.", ex);
			throw new DaoException("Exception: @SQLQueueBusterLinkDAO."
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


    private ResultBase activateLink(final String storeID,
            final String posLinkID, Connection connection)
            throws DaoException {
        String functionName = "activatePosLink";
        tp.methodEnter(functionName).println("storeID", storeID)
                .println("posLinkID", posLinkID);
        ResultBase resultBase = new ResultBase();
        ResultSet result = null;
        PreparedStatement activate = null;

        try {

            SQLStatement sqlStatement = SQLStatement.getInstance();
            activate = connection.prepareStatement(sqlStatement
                    .getProperty("activate-queuebusterlink"));

            activate.setString(SQLStatement.PARAM1, storeID);
            activate.setString(SQLStatement.PARAM2, posLinkID);

            result = activate.executeQuery();

            if (result.next()) {
            	resultBase
                .setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
            	resultBase
                        .setNCRWSSResultCode(ResultBase.RES_LINK_NOTFOUND);
                tp.println("QueuebusterLink information not found.");
            }

            connection.commit();
            activate.close();
        } catch (SQLException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to activate queuebuster information: "
                            + ex.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to activate queuebuster information: "
                            + ex.getMessage());
            throw new DaoException("SQLException:"
                    + " @SQLQueueBusterLinkDAO.activateLink", ex);
        } finally {
               tp.methodExit(resultBase);

        }

        return resultBase;
    }

    @Override
	public final List<POSLinkInfo> getLinks(final String storeId,
			final String key, final String name, final int limit)
			throws SQLException, DaoException {

		String functionName = "getLinks";
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
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-all-queueubusterlinks"));
            select.setInt(SQLStatement.PARAM1, (limit < 0)? 10000:limit);
            select.setString(SQLStatement.PARAM2, (StringUtility.isNullOrEmpty(storeId))?"%":storeId);
            select.setString(SQLStatement.PARAM3, (StringUtility.isNullOrEmpty(key))?"%":StringUtility.escapeCharatersForSQLqueries(key.trim())+"%");
            select.setString(SQLStatement.PARAM4, (StringUtility.isNullOrEmpty(name))?"%":"%"+StringUtility.escapeCharatersForSQLqueries(name.trim())+"%");

            result = select.executeQuery();

            while (result.next()) {
                POSLinkInfo posLinkInfo = new POSLinkInfo();
                posLinkInfo.setPosLinkId(result.getString(
                        result.findColumn("Id")).trim());
                posLinkInfo.setLinkName(result.getString(
                        result.findColumn("DisplayName")).trim());
                posLinkInfo.setRetailStoreId(result.getString(
                            result.findColumn("StoreId")).trim());
                links.add(posLinkInfo);
            }
		} catch (SQLException ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get list of queuebusterlinks.", ex);
			throw new DaoException("SQLException: @SQLQueueBusterLinkDAO."
					+ functionName, ex);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get list of queuebusterlinks.", ex);
			throw new DaoException("Exception: @SQLQueueBusterLinkDAO."
					+ functionName, ex);
		} finally {
			closeConnectionObjects(connection, select, result);

			tp.methodExit("Count:" + Integer.toString(links.size()));
		}
		return links;
	}
}
