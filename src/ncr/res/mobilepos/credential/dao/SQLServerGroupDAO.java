/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCredentialDAO
 *
 * A Data Access Object implementation for Operator Sign ON/OFF.
 *
 * Menesses, Chris Niven
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.credential.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.credential.model.UserGroup;
import ncr.res.mobilepos.credential.model.UserGroupLabel;
import ncr.res.mobilepos.credential.model.ViewUserGroup;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;


/**
 * SQLServerGroupDAO is a Data Access Object implementation for Groups.
 *
 */
public class SQLServerGroupDAO extends AbstractDao implements
        IGroupDAO {
    /**
     * Logging handler.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * DeviceInfo data access object classname.
     */
    private String className = "SQLServerGroupDAO";
    /**
     * Progname assignment for SQLServerGroupDAO.
     */
    private String progName = "SGDao";
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;
    /**
     * class instance of trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * SQLServerGroupDAO default constructor.
     * @throws DaoException database exception
     */
    public SQLServerGroupDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }


    @Override
    public final ResultBase createGroup(
            final int groupCode, final UserGroup group)
        throws DaoException {
        ResultBase resultBase = new ResultBase();

        String functionName = className + "createGroup";

        tp.methodEnter("createGroup");
        tp.println("groupCode", groupCode)
            .println("Group", group.toString());

        int result = 0;
        Connection connection = null;
        PreparedStatement create = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            create = connection.prepareStatement(
                    sqlStatement.getProperty("create-Group"));
            create.setInt(SQLStatement.PARAM1, groupCode);
            create.setString(SQLStatement.PARAM2, group.getGroupName());
            create.setBoolean(SQLStatement.PARAM3, group.isTransaction());
            create.setBoolean(SQLStatement.PARAM4, group.isReports());
            create.setBoolean(SQLStatement.PARAM5, group.isSettings());
            create.setBoolean(SQLStatement.PARAM6, group.isMerchandise());
            create.setBoolean(SQLStatement.PARAM7, group.isAdministration());
            create.setBoolean(SQLStatement.PARAM8, group.isDrawer());

            result = create.executeUpdate();
            connection.commit();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_OK);
                resultBase.setMessage(
                        "Success Creation of Group.");
            } else {
                resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
                resultBase.setMessage(
                        "Failed to create group");
                tp.println("Failed to create group");
            }
        } catch (SQLException ex) {
            // Duplication of insertion
            if (ex.getErrorCode()
                    != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                        "Failed to create Group:"
                        + ex.getMessage());
                rollBack(connection, functionName, ex);
                throw new DaoException("SQLException: @"
                   + functionName, ex);
            }
            resultBase.setNCRWSSResultCode(
                    ResultBase.RES_GROUP_EXISTS);
            tp.println("Duplicate Entry of Group.");
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to create Group: " + ex.getMessage());
            throw new DaoException("Exception: @"
                    + functionName, ex);
        } finally {
            closeConnectionObjects(connection, create);
            
            tp.methodExit(resultBase);
        }

        return resultBase;
    }

    @Override
    public final List<UserGroupLabel> listGroups(final String key)
            throws DaoException {
        String functionName = className + "listGroups";
        tp.methodEnter("listGroups").println("key", key);

        List<UserGroupLabel> groups = new ArrayList<UserGroupLabel>();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            if (null == key || key.isEmpty()) {
                select = connection.prepareStatement(sqlStatement
                        .getProperty("get-GroupList"));
            } else {
                select = connection.prepareStatement(sqlStatement
                        .getProperty("get-GroupListWithKey"));
                select.setInt(SQLStatement.PARAM1,
                        GlobalConstant.getMaxSearchResults());
                select.setString(SQLStatement.PARAM2, "%" + key + "%");
            }

            result = select.executeQuery();

            while (result.next()) {
                UserGroupLabel userGroupLbl = new UserGroupLabel();
                userGroupLbl.setGroupCode(Integer.parseInt(
                        result.getString("groupcode").trim()));
                userGroupLbl.setGroupName(result.getString("groupname").trim());
                groups.add(userGroupLbl);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to retrieve Group List: "
                            + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + functionName, ex);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to retrieve Group List: "
                            + ex.getMessage());
            throw new DaoException("Exception: @"
                    + functionName, ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit("Count:" + Integer.toString(groups.size()));
        }
        return groups;
    }

    @Override
    public final ViewUserGroup viewGroupDetail(final int groupCode)
        throws DaoException {

        String functionName = className + "viewGroupDetail";

        tp.methodEnter("viewGroupDetail")
            .println("GroupCode", Integer.toString(groupCode));

        ViewUserGroup viewUserGrp = new ViewUserGroup();
        UserGroup userGroup = null;

        ResultSet resultSet = null;
        PreparedStatement view = null;
        Connection connection = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            view = connection.prepareStatement(
                    sqlStatement.getProperty("view-GroupDetail"));

            view.setInt(SQLStatement.PARAM1, groupCode);

            resultSet = view.executeQuery();

            if (resultSet.next()) {
                userGroup = new UserGroup();
                userGroup.setGroupCode(groupCode);
                userGroup.setGroupName(resultSet.getString(
                        resultSet.findColumn("GroupName")));
                userGroup.setTransaction(resultSet.getBoolean(
                        resultSet.findColumn("Transactions")));
                userGroup.setReports(resultSet.getBoolean(
                        resultSet.findColumn("Reports")));
                userGroup.setSettings(resultSet.getBoolean(
                        resultSet.findColumn("Settings")));
                userGroup.setMerchandise(resultSet.getBoolean(
                        resultSet.findColumn("Merchandise")));
                userGroup.setAdministration(resultSet.getBoolean(
                        resultSet.findColumn("Administration")));
                userGroup.setDrawer(resultSet.getBoolean(
                        resultSet.findColumn("Drawer")));
            } else {
                viewUserGrp.setNCRWSSResultCode(ResultBase.RES_GROUP_NOTFOUND);
                tp.println("Failed to retrieve Group Detail.");
            }
            viewUserGrp.setUserGroup(userGroup);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
              "Failed to retrieve Group Detail: " + sqlEx.getMessage());
             throw new DaoException("SQLException: @"
                          + "SQLServerGroupDAO.viewGroupDetail", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                  "Failed to retrieve Group Detail: " + ex.getMessage());
          throw new DaoException("SQLException: @"
                  + "SQLServerGroupDAO.viewGroupDetail", ex);
        } finally {
            closeConnectionObjects(connection, view, resultSet);
            
            tp.methodExit(viewUserGrp.toString());
        }
        return viewUserGrp;
    }

    @Override
    public final ViewUserGroup updateGroup(
            final int groupCode, final UserGroup groupDetails)
    throws DaoException {

        String functionName = className + "updateGroup";
        tp.methodEnter("updateGroup");
        tp.println("GroupCode", groupCode)
          .println("UserGroup", groupDetails.toString());

        Connection connection = null;
        PreparedStatement updateGroupStmnt = null;
        ResultSet resultSet = null;

        UserGroup userGroup = null;
        ViewUserGroup viewUserGrp = new ViewUserGroup();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateGroupStmnt = connection.prepareStatement(
                    sqlStatement.getProperty("update-Group"));

            updateGroupStmnt.setString(
                    SQLStatement.PARAM1, groupDetails.getGroupName());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM2, groupDetails.isTransaction());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM3, groupDetails.isReports());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM4, groupDetails.isSettings());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM5, groupDetails.isMerchandise());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM6, groupDetails.isAdministration());
            updateGroupStmnt.setBoolean(
                    SQLStatement.PARAM7, groupDetails.isDrawer());
            updateGroupStmnt.setInt(
                    SQLStatement.PARAM8, groupCode);

            resultSet = updateGroupStmnt.executeQuery();

            if (resultSet.next()) {
                userGroup = new UserGroup();
                userGroup.setGroupCode(groupCode);
                userGroup.setGroupName(resultSet.getString(
                        resultSet.findColumn("groupname")).trim());
                userGroup.setTransaction(resultSet.getBoolean(
                        resultSet.findColumn("transactions")));
                userGroup.setReports(resultSet.getBoolean(
                        resultSet.findColumn("reports")));
                userGroup.setSettings(resultSet.getBoolean(
                        resultSet.findColumn("settings")));
                userGroup.setMerchandise(resultSet.getBoolean(
                        resultSet.findColumn("merchandise")));
                userGroup.setAdministration(resultSet.getBoolean(
                        resultSet.findColumn("administration")));
                userGroup.setDrawer(resultSet.getBoolean(
                        resultSet.findColumn("drawer")));

                viewUserGrp.setUserGroup(userGroup);

                viewUserGrp.setNCRWSSResultCode(ResultBase.RES_OK);
                tp.println("Success");
            } else {
                viewUserGrp.setNCRWSSResultCode(ResultBase.RES_GROUP_NOTFOUND);
                tp.println("Failed to update Group.");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update Group : " + e.getMessage());
            rollBack(connection, functionName, e);
                throw new DaoException("SQLException:"
                        + " @SQLServerGroupDAO.updateGroup", e);
        } catch (Exception e) {
            rollBack(connection, functionName, e);
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update Group: " + e.getMessage());
            throw new DaoException("Exception:"
                    + " @SQLServerGroupDAO.updateGroup", e);
        } finally {
            closeConnectionObjects(connection, updateGroupStmnt, resultSet);
            
            tp.methodExit(viewUserGrp);
        }
        return viewUserGrp;
    }

    @Override
    public final ResultBase deleteGroup(final int groupCode)
            throws DaoException {
        tp.methodEnter("deleteGroup");
        tp.println("GroupCode", groupCode);

        Connection connection = null;
        PreparedStatement deletePrepStmnt = null;
        int result = 0;
        ResultBase resBase = new ResultBase();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            deletePrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("delete-user-group"));
            deletePrepStmnt.setInt(SQLStatement.PARAM1, groupCode);
            result = deletePrepStmnt.executeUpdate();

            if (result < SQLResultsConstants.ONE_ROW_AFFECTED) {
                resBase.setNCRWSSResultCode(ResultBase
                        .RES_GROUP_NOTFOUND);
                tp.println("User Group not found.");
            } else  {
                connection.commit();
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerCredentialDAO.deleteGroup",
                    Logger.RES_EXCEP_SQL,
                    "Failed to delete user group\n " + e.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO.deleteGroup", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerCredentialDAO.deleteGroup", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName,
                    "SQLServerCredentialDAO.deleteGroup",
                    Logger.RES_EXCEP_GENERAL, "Failed to delete user group\n "
                            + e.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO.deleteGroup", e);
            throw new DaoException("Exception:"
                    + "@SQLServerCredentialDAO.deleteGroup ", e);
        } finally {
            closeConnectionObjects(connection, deletePrepStmnt);
            
            tp.methodExit(resBase.toString());
        }
        return resBase;
    }
}
