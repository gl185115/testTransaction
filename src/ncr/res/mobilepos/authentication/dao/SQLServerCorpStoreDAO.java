/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerStoreDAO
 *
 * Is a DAO Class for Store maintenance database manipulation.
 *
 */

package ncr.res.mobilepos.authentication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.authentication.model.CorpStore;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * Access database for corpstore CRUD manipulations.
 * 
 */
public class SQLServerCorpStoreDAO extends AbstractDao implements ICorpStoreDAO {
    /**
     * DBManager instance, provides database connection object.
     */
    private DBManager dbManager;
    /**
     * Logger instance, logs error and information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * program name of the class.
     */
    private String progName = "CStDao";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerCorpStoreDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves DBManager.
     * 
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    @Override
    public final ResultBase createCorpStore(final String companyId,
            final String storeId, final CorpStore corpStore)
            throws DaoException {

        String functionName = "SQLServerCorpStoreDAO.createCorpStore";
        tp.methodEnter("createCorpStore");
        tp.println("CompanyID", companyId).println("StoreID", storeId);

        Connection connection = null;
        PreparedStatement insert = null;
        int result = 0;
        ResultBase res = new ResultBase();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            insert = connection.prepareStatement(sqlStatement
                    .getProperty("create-corpstore"));

            insert.setString(SQLStatement.PARAM1, storeId);
            insert.setString(SQLStatement.PARAM2, storeId);
            insert.setString(SQLStatement.PARAM3, corpStore.getStorename());
            insert.setString(SQLStatement.PARAM4, corpStore.getPasscode());
            insert.setInt(SQLStatement.PARAM5, corpStore.getPermission());

            result = insert.executeUpdate();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                res.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
                res.setNCRWSSResultCode(ResultBase.RES_CORPSTORE_EXISTS);
                tp.println("CorpStore already exists.");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to add CorpStore\n " + e.getMessage());
            rollBack(connection, "@SQLServerCorpStoreDAO.createCorpStore ", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerCorpStoreDAO.createCorpStore ", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to add CorpStore\n " + e.getMessage());
            rollBack(connection, "@SQLServerCorpStoreDAO.createCorpStore ", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerCorpStoreDAO.createCorpStore ", e);
        } finally {
            closeConnectionObjects(connection, insert);
            
            tp.methodExit(res);
        }
        return res;
    }

    @Override
    public final ViewCorpStore viewCorpStore(final String companyId,
            final String storeId) throws DaoException {

        String functionName = "SQLServerCorpStoreDAO.viewCorpStore";

        tp.methodEnter("viewCorpStore");
        tp.println("RetailStoreID", storeId).println("CompanyID", companyId);

        ViewCorpStore storeData = new ViewCorpStore();
        CorpStore corpstore = new CorpStore();

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-corpstore"));

            select.setString(SQLStatement.PARAM1, storeId);
            result = select.executeQuery();

            if (result.next()) {
                corpstore.setStoreid(storeId);
                corpstore.setStorename(result.getString("StoreName"));
                corpstore.setPermission(result.getInt("Permission"));

                corpstore.setPasscode(StringUtility.mask(
                        result.getString("PassCode"), 'x'));

            } else {
                storeData
                        .setNCRWSSResultCode(ResultBase.RES_CORPSTORE_NOT_EXIST);
                tp.println("CorpStore not found.");
            }

            storeData.setCorpstore(corpstore);

        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to View CorpStore#" + storeId + " : "
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @SQLServerCorpStoreDAO"
                    + ".viewCorpStore - Error view corpstore", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to View CorpStore#" + storeId + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerCorpStoreDAO"
                    + ".viewCorpStore - Error view corpstore", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(storeData);
        }

        return storeData;
    }

    @Override
    public final ResultBase deleteCorpStore(final String companyId,
            final String storeId) throws DaoException {

        String functionName = "SQLServerCorpStoreDAO.deleteCorpStore";
        tp.methodEnter("deleteCorpStore");
        tp.println("RetailStoreID", storeId).println("CompanyID", companyId);

        ResultBase resultBase = new ResultBase();
        Connection connection = null;
        PreparedStatement delete = null;
        int result = 0;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            delete = connection.prepareStatement(sqlStatement
                    .getProperty("delete-corpstore"));

            delete.setString(SQLStatement.PARAM1, storeId);

            result = delete.executeUpdate();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
            } else if (result == SQLResultsConstants.NO_ROW_AFFECTED) {
                resultBase
                        .setNCRWSSResultCode(ResultBase.RES_CORPSTORE_NOT_EXIST);
                tp.println("CorpStore not found.");
            }
            connection.commit();
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to Delete CorpStore#" + storeId + " : "
                            + sqlEx.getMessage());
            rollBack(connection, "@SQLServerCorpStoreDAO:deleteCorpStore",
                    sqlEx);
            throw new DaoException("SQLException: @SQLServerCorpStoreDAO"
                    + ".deleteCorpStore - Error delete corpstore", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to Delete CorpStore#" + storeId + " : "
                            + ex.getMessage());
            rollBack(connection, "@SQLServerCorpStoreDAO:deleteCorpStore", ex);
            throw new DaoException("Exception: @SQLServerCorpStoreDAO"
                    + ".deleteCorpStore - Error delete corpstore", ex);
        } finally {
            closeConnectionObjects(connection, delete);
            
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    @Override
    public final ViewCorpStore updateCorpStore(final String companyId,
            final String storeId, final CorpStore store) throws DaoException {
        String functionName = "SQLServerCorpStoreDAO.updateCorpStore";

        tp.methodEnter("updateCorpStore");
        tp.println("StoreID", storeId).println("CompanyID", companyId);

        Connection conn = null;
        PreparedStatement updateStoreStmnt = null;
        ViewCorpStore viewStore = null;
        ResultSet result = null;
        try {
            viewStore = this.viewCorpStore(companyId, storeId);
            if (ResultBase.RES_CORPSTORE_NOT_EXIST == viewStore
                    .getNCRWSSResultCode()) {
                tp.println("Store not found.");
                viewStore.setCorpstore(new CorpStore());
                return viewStore;
            }

            SQLStatement sqlStatement = SQLStatement.getInstance();
            conn = dbManager.getConnection();
            updateStoreStmnt = conn.prepareStatement(sqlStatement
                    .getProperty("update-corpstore"));
            updateStoreStmnt.setString(SQLStatement.PARAM1, store.getStoreid());
            updateStoreStmnt.setString(SQLStatement.PARAM2,
                    store.getStorename());
            updateStoreStmnt
                    .setString(SQLStatement.PARAM3, store.getPasscode());
            updateStoreStmnt.setInt(SQLStatement.PARAM4, store.getPermission());
            updateStoreStmnt.setString(SQLStatement.PARAM5, storeId);

            result = updateStoreStmnt.executeQuery();

            CorpStore newStore = new CorpStore();
            if (result.next()) {
                newStore.setStorename(result.getString(result
                        .findColumn("StoreName")));
                newStore.setStoreid(result.getString(result
                        .findColumn("StoreId")));
                newStore.setPermission(result.getInt(result
                        .findColumn("Permission")));

                newStore.setPasscode(StringUtility.mask(
                        result.getString("PassCode"), 'x'));
            } else {
                viewStore
                        .setNCRWSSResultCode(ResultBase.RES_CORPSTORE_NO_UPDATE);
                newStore = null;
                tp.println("CorpStore update was not successful.");
            }
            conn.commit();
            viewStore.setCorpstore(newStore);
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to Update CorpStore with StoreID#" + storeId
                            + " : " + ex.getMessage());
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) == ex
                    .getErrorCode()) {
                viewStore = new ViewCorpStore();
                CorpStore errStore = new CorpStore();
                viewStore.setCorpstore(errStore);
                viewStore.setNCRWSSResultCode(ResultBase.RES_CORPSTORE_EXISTS);
                tp.println("CorpStore entry is duplicated.");
            } else {
                rollBack(conn, functionName, ex);
                throw new DaoException("SQLException: @" + functionName
                        + " - Error update store", ex);
            }
        } catch (Exception ex) {
            rollBack(conn, functionName, ex);
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to Update CorpStore with StoreID#" + storeId
                            + " : " + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error update corpstore", ex);
        } finally {
            closeConnectionObjects(conn, updateStoreStmnt, result);
            
            tp.methodExit(viewStore);
        }
        return viewStore;
    }
}
