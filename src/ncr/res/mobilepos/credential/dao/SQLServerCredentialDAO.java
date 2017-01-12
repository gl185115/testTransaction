/*
 * Copyright (c) 2011-2012,2015 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCredentialDAO
 *
 * A Data Access Object implementation for Operator Sign ON/OFF.
 *
 */
package ncr.res.mobilepos.credential.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.credential.model.Authorization;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.NameMasterInfo;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.model.Permissions;
import ncr.res.mobilepos.credential.model.ViewEmployee;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * SQLServerCredentialDAO is a Data Access Object implementation for Operator
 * Sign ON/OFF.
 *
 */
public class SQLServerCredentialDAO extends AbstractDao implements
        ICredentialDAO {
    /**
     * Instance of Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Class name use in logging.
     */
    private String className = "SQLServerCredentialDAO.";
    /**
     * Program name use in logging.
     */
    private String progName = "Credntl";

    /**
     * DBMananger provides connection object.
     */
    private DBManager dbManager;

    /**
     * Gets the dbManager.
     *
     * @return the dbManager
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Sets the dbManager.
     *
     * @param dbMgr
     *            the new DBManager
     */
    public final void setDbManager(final DBManager dbMgr) {
        this.dbManager = dbMgr;
    }

    /** The Trace Printer. */
    private Trace.Printer tp;
    /**
     * Default constructor. Initializes dbManager and iowriter.
     *
     * @throws DaoException
     *             thrown when error occurs.
     */
    public SQLServerCredentialDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves dbManager.
     *
     * @return dbManager object.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    /**
     * Signs on an operator.
     *
     * @param operatorNumber
     *            Operator's number
     * @param passCode
     *            Pass code of the operator
     * @param terminalID
     *            ID of the device where the operator account is signed on
     * @return The number of rows affected in the database during the Signing On
     * @throws DaoException
     *             Exception thrown when method fails.
     */
    public final Operator signOnOperator(final String companyId, final String operatorNumber,
            final String passCode, final String terminalID) throws DaoException {

        String functionName = className + "signOnOperator";
        tp.methodEnter("signOnOperator");
        tp.println("operatorNumber", operatorNumber)
                .println("passCode", passCode)
                .println("terminalID", terminalID);

        Operator operator = new Operator();
        Connection connection = null;
        PreparedStatement update = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String passcodeDB = "";
        String operatorName = "";
        String opeNameKana = "";

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-operator-by-operatorno"));
            select.setString(SQLStatement.PARAM1, operatorNumber);
            select.setString(SQLStatement.PARAM2, companyId);
            result = select.executeQuery();

            if (result.next()) {
                int operatorType = result.getInt(result
                        .findColumn("OperatorType"));
                Permissions permissions = getUserGroupPermission(operatorType,
                        connection);

                // Validate Permissions
                if (null == permissions) {
                    operator.setNCRWSSResultCode(ResultBase.RES_GROUP_NOTFOUND);
                    tp.println("No User Group Permission with GroupCode of "
                            + operatorType);
                    return operator;
                }

                passcodeDB = result.getString(result.findColumn("PassCode"));
                operatorName = result.getString(result
                        .findColumn("OperatorName"));
                
                opeNameKana = result.getString(result.findColumn("OpeKanaName"));

                String status = result.getString(result.findColumn("Status"));

                if(status != null && "Deleted".equalsIgnoreCase(status)){
                    operator.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                    tp.println("Operator not found");
                    return operator;
                } 

                if (passcodeDB == null || passcodeDB.isEmpty()) {
                    update = connection.prepareStatement(sqlStatement
                            .getProperty("credential-first-log-in"));
                    update.setString(SQLStatement.PARAM1, passCode);
                    update.setString(SQLStatement.PARAM2, terminalID);
                    update.setString(SQLStatement.PARAM3, operatorNumber);
                    update.executeUpdate();

                } else if (passcodeDB.equals(passCode)) {
                    update = connection.prepareStatement(sqlStatement
                            .getProperty("update-operator-terminalid"));
                    update.setString(SQLStatement.PARAM1, terminalID);
                    update.setString(SQLStatement.PARAM2, operatorNumber);
                    update.setString(SQLStatement.PARAM3, passCode);
                    update.executeUpdate();
                } else {
                    tp.println("Invalid passcode");
                    operator.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_PASSCODE_INVALID);
                    return operator;
                }

                // Set Up the necessary information during Operator SignOn
                DateFormat today = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                operator.setNCRWSSResultCode(ResultBase.RESCREDL_OK);
                operator.setSignOnAt(today.format(Calendar.getInstance()
                        .getTime()));
                ((SimpleDateFormat) today).applyPattern("MMM dd, ''yy");
                operator.setDate(today.format(Calendar.getInstance().getTime()));
                operator.setOperatorNo(operatorNumber);
                operator.setName(operatorName);
                operator.setOperatorType(operatorType);
                operator.setPermissions(permissions);
                operator.setOpeNameKana(opeNameKana);
                connection.commit();
            } else {
                operator.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                tp.println("Operator not found");
            }

        } catch (SQLException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to Sign on Operator#" + operatorNumber + " : "
                            + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @SQLServerCredentialDAO"
                    + ".signOnOperator - Error SignOn process", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to Sign on Operator#" + operatorNumber + " : "
                            + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("Exception: @SQLServerCredentialDAO"
                    + ".signOnOperator - Error SignOn process", ex);
        } finally {
            closeConnectionObjects(null, select, null);
            closeConnectionObjects(connection, update, result);

            tp.methodExit(operator.toString());
        }

        return operator;
    }   

    /**
     * Signs Off an operator by specifying the operator number.
     *
     * @param operatorNumber
     *            The Operator Number
     * @return The number of rows affected in the database during the Signing
     *         Off
     * @throws DaoException
     *             Exception thrown when Signing off of an Operator fails
     */
    public final int signOffOperator(final String operatorNumber)
            throws DaoException {

        String functionName = className + "signOffOperator";
        tp.methodEnter("signOffOperator");
        tp.println("operatorNumber", operatorNumber);

        int resultCode = 0;
        Connection connection = null;
        PreparedStatement update = null;

        // set Status to Inactive
        String operatorStatus = "Inactive";

        if (checkOperatorStatus(operatorNumber) == Operator.STATUS_DELETED){
            operatorStatus = "Deleted";
        }

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();

            update = connection.prepareStatement(sqlStatement
                    .getProperty("sign-off-operator"));

            update.setString(1, operatorStatus);
            update.setString(2, operatorNumber);
            resultCode = update.executeUpdate();

            // commit codes here!
            connection.commit();

        } catch (SQLException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to Sign off Operator#" + operatorNumber + ": "
                            + ex.getMessage());
            throw new DaoException("SQLException: @SQLServerCredentialDAO"
                    + ".signOffOperator - Error Sign Off process", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to Sign off Operator#" + operatorNumber + ": "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerCredentialDAO"
                    + ".signOffOperator - Error Sign Off process", ex);
        } finally {
            closeConnectionObjects(connection, update, null);

            tp.methodExit(resultCode);
        }

        return resultCode;
    }

    /**
     * Checks for operator status (Automatically updates status if validity has
     * expired).
     *
     * @param operatorNumber
     *            Operator's number
     * @return Current status of Operator. 0: offline || 1: online || 2 :
     *         deleted
     * @throws DaoException
     *             Exception when the method fail.
     */
    public final int checkOperatorStatus(final String operatorNumber)
            throws DaoException {

        String functionName = className + "checkOperatorStatus";
        tp.methodEnter("checkOperatorStatus");
        tp.println("operatorNumber", operatorNumber);

        Connection connection = null;
        PreparedStatement select = null;
        PreparedStatement update = null;
        ResultSet result = null;
        int status = 0;

        try {
            // temporary
            // operatorStatus has been removed
            status = Operator.STATUS_INACTIVE;
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-operator-status"));

            select.setString(SQLStatement.PARAM1, operatorNumber);
            result = select.executeQuery();

            if (result.next()) {
                String res = result.getString(result.findColumn("Status"));
                if (res != null && "Deleted".equalsIgnoreCase(res)) {
                    status = Operator.STATUS_DELETED;
                } else if (res != null && "Active".equalsIgnoreCase(res)) {
                    status = Operator.STATUS_ACTIVE;
                }

            } else {
                tp.println("Operator not found.");
                //return -1; // no operator found
                status = Operator.OPERATOR_NOT_FOUND;
            }

            connection.commit();
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to check Status of Operator#" + operatorNumber
                            + ": " + ex.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerCredentialDAO.checkOperatorStatus",
                    ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to check Status of Operator#" + operatorNumber
                            + ": " + ex.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerCredentialDAO.checkOperatorStatus",
                    ex);
        } finally {
            closeConnectionObjects(null, select, result);
            closeConnectionObjects(connection, update);

            tp.methodExit(status);
        }

        return status;
    }

    /**
     * Get status of Operator
     *
     * @param empCode
     *            Employee Code
     *
     * @return status of an operator
     */
    @Override
    public final Operator getStatusOfOperator(final String companyId, final String empCode)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyId);
        tp.println("EmpCode", empCode);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        Operator operator = new Operator();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-status-of-operator"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, empCode);

            result = select.executeQuery();
            // If there are no data then return -1 else return operator's name, kana name and permission
            if (!result.next()) {
                operator.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                operator.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                operator.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                tp.println(ResultBase.RES_NODATAFOUND_MSG);
            } else {
                operator.setName(result.getString("OperatorName"));
                operator.setOpeKananame(result.getString("OpeKanaName"));
                operator.setSecuritylevel(result.getString("SecLevel2"));
                operator.setOperatorType(result.getInt("OpeType"));
                
                Permissions permissions = getUserGroupPermission(operator.getOperatorType(), connection);
                operator.setPermissions(permissions);
                
                operator.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                operator.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                operator.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to to get Status of Operator.", sqlEx);
            throw new DaoException(
                    "SQLException: @SQLServerCredentialDAO.getStatusOfOperator",
                    sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to to get Status of Operator.", ex);
            throw new DaoException(
                    "Exception: @SQLServerCredentialDAO.getStatusOfOperator",
                    ex);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(operator);
        }

        return operator;
    }

    /**
     * get system name master
     * @return system name master list
     * @throws DaoException
     *             Exception when the method fail.
     */
    @Override
    public final List<NameMasterInfo> getSystemNameMaster(
            final String companyId, final String StoreId,
            final String nameCategory) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("StoreId", StoreId)
          .println("nameCategory", nameCategory);

        List<NameMasterInfo> nmMstInfoLst = new ArrayList<NameMasterInfo>();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-systemnames"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, StoreId);
            select.setString(SQLStatement.PARAM3, nameCategory);
            result = select.executeQuery();
            while (result.next()) {
                NameMasterInfo namemasterinfo = new NameMasterInfo();
                namemasterinfo.setCompanyId(result.getString("CompanyId"));
                namemasterinfo.setStoreId(result.getString("StoreId"));
                namemasterinfo.setNameCategory(result.getString("NameCategory"));
                namemasterinfo.setNameId(result.getString("NameId"));
                namemasterinfo.setNameText(result.getString("NameText"));
                namemasterinfo.setNameIdName(result.getString("NameIdName"));
                nmMstInfoLst.add(namemasterinfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get system name list.", sqlEx);
            throw new DaoException(
                    "SQLException: @SQLServerCredentialDAO.getSystemNameMaster",
                    sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get system name list.", ex);
            throw new DaoException(
                    "Exception: @SQLServerCredentialDAO.getSystemNameMaster",
                    ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(nmMstInfoLst);
        }
        return nmMstInfoLst;
    }

    /**
     * FOR GUEST ACCOUNT. Signs on the operator if existing, if not create a new
     * operator
     *
     * @param operatorNumber
     *            Operator's number
     * @param terminalID
     *            Terminal number
     * @return Current status of Operator. 0: offline || 1: online
     * @throws DaoException
     *             Exception when the method fail.
     */
    public final Operator guestSignOnOperator(final String operatorNumber,
            final String terminalID) throws DaoException {

        String functionName = className + "guestSignOnOperator";
        tp.methodEnter("guestSignOnOperator");
        tp.println("operatorNumber", operatorNumber).println("terminalID",
                terminalID);

        Operator operator = new Operator();
        Connection connection = null;
        PreparedStatement update = null;
        PreparedStatement select = null;
        ResultSet resultSelect = null;
        ResultSet resultUpdate = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-operator-by-operatorno"));
            select.setString(SQLStatement.PARAM1, operatorNumber);
            resultSelect = select.executeQuery();

            String passcode = null;
            String operatorName = null;
           
            if (!resultSelect.next()) {
                passcode = "111";
                createOperator(operatorNumber, passcode);
            } else {
                passcode = resultSelect.getString(resultSelect
                        .findColumn("PassCode"));
                if (passcode == null) {
                    tp.println("the operator's passcode is null."
                            + " do not allow guest log-in");
                    operator.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_PASSCODE_INVALID);
                    return operator;
                }
                operatorName = resultSelect.getString(resultSelect
                        .findColumn("OperatorName"));
            }

            update = connection.prepareStatement(sqlStatement
                    .getProperty("update-operator-terminalid-guest"));
            update.setString(SQLStatement.PARAM1, terminalID);
            update.setString(SQLStatement.PARAM2, operatorNumber);
            resultUpdate = update.executeQuery();
            // Call result.next() directly since Operator is existing.
            resultUpdate.next();

            int operatorType = resultUpdate.getInt(resultUpdate
                    .findColumn("OperatorType"));
            Permissions permission = getUserGroupPermission(operatorType,
                    connection);

            if (null == permission) {
                tp.println("No User Group Permission with GroupCode of "
                        + operatorType);
                operator.setNCRWSSResultCode(ResultBase.RES_GROUP_NOTFOUND);
                rollBack(connection, functionName, null);
                return operator;
            }

            // Set Up the necessary information during Operator SignOn
            SimpleDateFormat today = new SimpleDateFormat("HH:mm",
                    Locale.ENGLISH);
            operator.setNCRWSSResultCode(ResultBase.RESCREDL_OK);
            operator.setSignOnAt(today.format(Calendar.getInstance().getTime()));
            today.applyPattern("MMM dd, ''yy");
            operator.setDate(today.format(Calendar.getInstance().getTime()));
            operator.setOperatorNo(operatorNumber);
            operator.setPasscode(passcode);
            operator.setName(operatorName);
            operator.setOperatorType(operatorType);
            operator.setPermissions(permission);
            connection.commit();
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to Sign on Guest Operator#" + operatorNumber
                            + " : " + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @SQLServerCredentialDAO"
                    + ".signOnOperator - Error SignOn process", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to Sign on Guest Operator#" + operatorNumber
                            + " : " + ex.getMessage());
            rollBack(connection, functionName, ex);
            throw new DaoException(
                    "Exception: @SQLServerCredentialDAO.signOnOperator "
                            + "- Error SignOn process", ex);
        } finally {
            closeConnectionObjects(null, select, resultUpdate);
            closeConnectionObjects(connection, update, resultSelect);

            tp.methodExit(operator.toString());
        }

        return operator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final List<Employee> listOperators(final String retailStoreID,
            final String key, final String name, final int limit) throws DaoException {

        String functionName = "SQLServerCredentialDAO.listOperators";
        tp.methodEnter("listOperators").println("RetailStoreID", retailStoreID)
                .println("Key", key)
                .println("name", name)
                .println("limit", limit);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet rs = null;
        List<Employee> operators = new ArrayList<Employee>();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-all-operators"));

            tp.println("searchlimit", GlobalConstant.getMaxSearchResults());
            int searchLimit = (limit == 0) ? GlobalConstant
                    .getMaxSearchResults() : limit;
            selectStmt.setString(SQLStatement.PARAM1, (StringUtility.isNullOrEmpty(retailStoreID)) ? null : retailStoreID);
            selectStmt.setString(SQLStatement.PARAM2, (StringUtility.isNullOrEmpty(key)) ? null : StringUtility.escapeCharatersForSQLqueries(key.trim()) + "%");
            selectStmt.setString(SQLStatement.PARAM3, (StringUtility.isNullOrEmpty(name)) ? null : "%" + StringUtility.escapeCharatersForSQLqueries(name.trim()) + "%"); 
            selectStmt.setInt(SQLStatement.PARAM4, searchLimit);
            rs = selectStmt.executeQuery();
            while (rs.next()) {
                Employee operator = new Employee();
                String operatorNo = rs.getString(rs.findColumn("OperatorNo"));
                String operatorName = rs.getString(rs
                        .findColumn("OperatorName"));
                String operatorType = rs.getString(rs
                        .findColumn("OperatorType"));
                String workstationID = rs
                        .getString(rs.findColumn("TerminalId"));
                String status = rs
                        .getString(rs.findColumn("Status"));

                String companyId = null;
                String storeId = null;
                String tempStore = null;
                
                companyId = rs.getString(rs.findColumn("CompanyId"));
                tempStore = rs.getString(rs.findColumn("StoreId"));
                storeId = (tempStore == null) ? "" : tempStore;

                operator.setNumber(operatorNo.trim());
                operator.setName(operatorName.trim());
                operator.setWorkStationID(workstationID.trim());
                operator.setOperatorType(operatorType);
                operator.setCompanyId(companyId.trim());
                operator.setRetailStoreID(storeId);
                operator.setStatus(status);

                String strPasscode = rs.getString(rs.findColumn("PassCode"));
                if(strPasscode != null){
                    operator.setPasscode(strPasscode.replaceAll(".", "*"));
                }

                // Add the operator in the list
                operators.add(operator);
            }
        } catch (SQLException ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "List Operators: " + ex.getMessage());
            throw new DaoException("SQLException: @" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, selectStmt, rs);

            tp.methodExit(operators.size());
        }

        return operators;
    }
   
    @Override
    public final ResultBase createEmployee(final String retailStoreID,
            final String operatorID, final Employee employee)
            throws DaoException {

        String functionName = className + "createEmployee";
        tp.methodEnter("createEmployee");
        tp.println("retailStoreID", retailStoreID)
                .println("operatorID", operatorID)
                .println("name", employee.getName())
                .println("type", employee.getOperatorType())
                .println("terminalid", employee.getWorkStationID())
                .println("passcode", employee.getPasscode());

        ResultBase result = new ResultBase();
        Connection connection = null;
        PreparedStatement create = null;
        PreparedStatement select = null;
        ResultSet rs = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            // check if employee already existing

            int status = checkOperatorStatus(operatorID);

            if (status != -1 && status != Operator.STATUS_DELETED) {
                tp.methodExit("Entry is a duplicate");
                result.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_EXISTS);
                return result;
            }

            create = connection.prepareStatement(sqlStatement
                    .getProperty("create-employee"));
            create.setString(SQLStatement.PARAM1, retailStoreID);
            create.setString(SQLStatement.PARAM2, operatorID);
            create.setString(SQLStatement.PARAM3, employee.getName());
            create.setString(SQLStatement.PARAM4, employee.getPasscode());
            create.setInt(SQLStatement.PARAM5,
                    Integer.parseInt(employee.getOperatorType()));
            create.setString(SQLStatement.PARAM6, employee.getUpdAppId());
            create.setString(SQLStatement.PARAM7, employee.getUpdOpeCode());
            create.executeUpdate();
            connection.commit();
            result.setNCRWSSResultCode(ResultBase.RESCREDL_OK);

        } catch (SQLException sqlEx) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    sqlEx.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:createEmployee",
                    sqlEx);
            throw new DaoException("Exception: @SQLServerCredentialDAO"
                    + ".createEmployee - Error creating employee", sqlEx);
        } catch (DaoException daoEx) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    daoEx.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:createEmployee",
                    daoEx);
            throw new DaoException("Exception: @SQLServerCredentialDAO"
                    + ".createEmployee - Error creating employee", daoEx);
        } catch (Exception ex) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:createEmployee", ex);
            throw new DaoException("Exception: @SQLServerCredentialDAO"
                    + ".createEmployee - Error creating employee", ex);
        } finally {
            closeConnectionObjects(null, select, null);
            closeConnectionObjects(connection, create, rs);

            tp.methodExit(result.toString());
        }

        return result;
    }

    /***
     * deletes an employee.
     *
     * @param retailStoreID
     *            - id of the store
     * @param operatorID
     *            - id of the operator to delete
     * @return ResultBase
     * @throws DaoException
     *             - exception
     */
    @Override
    public final ResultBase deleteEmployee(final String retailStoreID,
            final String operatorID, final String updOpeCode, final String updAppId) throws DaoException {

        tp.methodEnter("deleteEmployee");
        tp.println("retailStoreID", retailStoreID).println("operatorID",
                operatorID);

        Connection connection = null;
        PreparedStatement delete = null;
        int result = 0;
        ResultBase resBase = new ResultBase();
        int currentOperatorStatus = checkOperatorStatus(operatorID);

        // if operator is currently logged in - do not allow to delete
        if ( currentOperatorStatus == Operator.STATUS_ACTIVE) {
            tp.methodExit("Employee " + operatorID
                    + " is currently logged on. Delete operation aborted.");
            resBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE);
            return resBase;
        } else if ( currentOperatorStatus == Operator.STATUS_DELETED || currentOperatorStatus == Operator.OPERATOR_NOT_FOUND) {
            tp.methodExit("Operator not found.");
            resBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
            return resBase;
        }

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            delete = connection.prepareStatement(sqlStatement
                    .getProperty("delete-employee"));
            delete.setString(SQLStatement.PARAM1, updOpeCode);
            delete.setString(SQLStatement.PARAM2, updAppId);
            delete.setString(SQLStatement.PARAM3, retailStoreID);
            delete.setString(SQLStatement.PARAM4, operatorID);


            result = delete.executeUpdate();
            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                resBase.setNCRWSSResultCode(ResultBase.RESCREDL_OK);
            } else if (result == SQLResultsConstants.NO_ROW_AFFECTED) {
                resBase.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                tp.println("Operator not found.");
            }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerCredentialDAO.deleteEmployee",
                    Logger.RES_EXCEP_SQL,
                    "Failed to delete Employee\n " + e.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO.deleteEmployee", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerCredentialDAO.deleteEmployee", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName,
                    "SQLServerCredentialDAO.deleteEmployee",
                    Logger.RES_EXCEP_GENERAL, "Failed to delete Employee\n "
                            + e.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO.deleteEmployee", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerCredentialDAO.deleteEmployee ", e);
        } finally {
            closeConnectionObjects(connection, delete);

            tp.methodExit(resBase.toString());
        }

        return resBase;
    }

    @Override
    public final ViewEmployee updateEmployee(final String retailStoreID,
            final String currentOperatorID, final String operatorID,
            final Employee employee) throws DaoException {

        String functionName = className + "updateEmployee";
        tp.methodEnter("updateEmployee");
        tp.println("retailStoreID", retailStoreID)
                .println("currentOperatorID", currentOperatorID)
                .println("operatorID", operatorID)
                .println("name", employee.getName())
                .println("number", employee.getNumber())
                .println("type", employee.getOperatorType())
                .println("passcode", employee.getPasscode())
                .println("storeid", employee.getRetailStoreID())
                .println("terminalid", employee.getWorkStationID());

        // check if operator is online
        if (currentOperatorID.equals(operatorID) || (Operator.STATUS_ACTIVE == checkOperatorStatus(operatorID))) {
                Employee errEmployee = new Employee();
                ViewEmployee errViewEmp = new ViewEmployee();
                errViewEmp.setEmployee(errEmployee);
                errViewEmp
                        .setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_ONLINE);
                tp.methodExit("Update is aborted. Operator is online");
                return errViewEmp;
        }

        ViewEmployee newEmpInfo = new ViewEmployee();
        Employee newEmp = new Employee();
        Employee oldEmp = new Employee();
        Connection connection = null;
        PreparedStatement updateStmt = null;
        String strOperatorNoToBeSaved = null;
        boolean isValidForUpdate = true;

        if (dbManager == null) {
            dbManager = JndiDBManagerMSSqlServer.getInstance();
        }
        ResultSet res = null;
        Employee newEmployeeToBeUpdated = employee;
        try {
            int oldOperatorStatus = this.checkOperatorStatus(operatorID);
            if(oldOperatorStatus == Operator.OPERATOR_NOT_FOUND || oldOperatorStatus == Operator.STATUS_DELETED){//not existing ||  existing but deleted
                newEmpInfo.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                tp.println("Operator not found.");
                isValidForUpdate = false;
            }else {
                int newOperatorStatus = this.checkOperatorStatus(employee.getNumber());
                if(!operatorID.equalsIgnoreCase(employee.getNumber()) && (newOperatorStatus == Operator.STATUS_ACTIVE || newOperatorStatus == Operator.STATUS_INACTIVE || newOperatorStatus == Operator.STATUS_OFFLINE || newOperatorStatus == Operator.STATUS_ONLINE  ) ){
                    newEmpInfo.setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_EXISTS);
                    isValidForUpdate = false;
                }else if(!operatorID.equalsIgnoreCase(employee.getNumber()) && newOperatorStatus == Operator.STATUS_DELETED){
                    strOperatorNoToBeSaved = employee.getNumber();
                    oldEmp = this.getEmployeeDetails(operatorID);
                    if(oldEmp != null){
                        newEmployeeToBeUpdated = this.mergeEmployeeDetails(newEmployeeToBeUpdated, oldEmp);
                    }
                    //delete old employee
                    ResultBase resultBaseDelete = this.deleteEmployee(retailStoreID, operatorID, employee.getUpdOpeCode(), employee.getUpdAppId());
                    if(resultBaseDelete.getNCRWSSResultCode() != ResultBase.RESCREDL_OK){
                        newEmpInfo.setNCRWSSResultCode(resultBaseDelete.getNCRWSSResultCode());
                        isValidForUpdate = false;
                    }
                }
            }
            if(isValidForUpdate){
                connection = dbManager.getConnection();
                SQLStatement sqlStatement = SQLStatement.getInstance();
                updateStmt = connection.prepareStatement(sqlStatement
                        .getProperty("update-employee"));

                setValues(updateStmt, newEmployeeToBeUpdated.getNumber(), newEmployeeToBeUpdated.getPasscode(),
                        newEmployeeToBeUpdated.getName(), newEmployeeToBeUpdated.getOperatorType(),
                        newEmployeeToBeUpdated.getRetailStoreID(), newEmployeeToBeUpdated.getUpdAppId(),
                        newEmployeeToBeUpdated.getUpdOpeCode(),newEmployeeToBeUpdated.getStatus(),
                        newEmployeeToBeUpdated.getWorkStationID(), newEmployeeToBeUpdated.getRole(),
                        (strOperatorNoToBeSaved != null)? strOperatorNoToBeSaved: operatorID);

                res = updateStmt.executeQuery();

                if (res.next()) {
                    newEmp.setName(res.getString(res.findColumn("OperatorName")));
                    newEmp.setNumber(res.getString(res.findColumn("OperatorNo")));
                    newEmp.setOperatorType(res.getString(res
                            .findColumn("OperatorType")));

                    String strPasscode = res.getString(res.findColumn("PassCode"));
                    if(strPasscode != null){
                        newEmp.setPasscode(strPasscode.replaceAll(".", "*"));
                    }
                } else {
                    newEmpInfo
                            .setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND);
                    tp.println("Operator not found.");
                }

                connection.commit();
            }
            newEmpInfo.setEmployee(newEmp);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    sqlEx.getMessage());
            // Is the error was caused by row duplicate?
            // If yes, return result code that item already exist.
            // There is no need to rollback since there is no update.
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) == sqlEx
                    .getErrorCode()) {
                newEmpInfo
                        .setNCRWSSResultCode(ResultBase.RESCREDL_ERROR_EXISTS);
            } else {
                rollBack(connection, "@SQLServerCredentialDAO:updateEmployee",
                        sqlEx);
                throw new DaoException("SQLException: @updateEmployee ", sqlEx);
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    daoEx.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:updateEmployee",
                    daoEx);
            throw new DaoException("DaoException: @updateEmployee ", daoEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:updateEmployee", ex);
            throw new DaoException("Exception: @updateEmployee ", ex);
        } finally {
            closeConnectionObjects(connection, updateStmt, res);

            tp.methodExit(newEmpInfo.toString());
        }

        return newEmpInfo;
    }

    /**
     * Private Helper Method that gets User Group Permission based on groupcode.
     *
     * @param connection
     *            The Connection Object.
     * @param groupcode
     *            The Group Code.
     * @return The {@link Permissions}
     */
    private Permissions getUserGroupPermission(final int groupcode,
            final Connection connection) throws DaoException {
        tp.methodEnter("getUserGroupPermission");
        Permissions permission = null;
        PreparedStatement selectPermission = null;
        ResultSet result = null;
        String functionName = className + "getUserGroupPermission";

        try {
            SQLStatement sqlStatement;
            sqlStatement = SQLStatement.getInstance();
            selectPermission = connection.prepareStatement(sqlStatement
                    .getProperty("view-GroupDetail"));
            selectPermission.setInt(SQLStatement.PARAM1, groupcode);
            result = selectPermission.executeQuery();
            if (result.next()) {
                permission = new Permissions();
                permission.setTransactions(result.getBoolean(result
                        .findColumn("transactions")));
                permission.setDrawer(result.getBoolean(result
                        .findColumn("drawer")));
                permission.setReports(result.getBoolean(result
                        .findColumn("reports")));
                permission.setSettings(result.getBoolean(result
                        .findColumn("settings")));
                permission.setMerchandise(result.getBoolean(result
                        .findColumn("merchandise")));
                permission.setAdministration(result.getBoolean(result
                        .findColumn("administration")));
            } else {
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                        "No User Group Permission with GroupCode of "
                                + groupcode);
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to retrieve User Group Permission"
                            + " with GroupCode of " + groupcode);
        } finally {
            boolean hasErrorOccur = closeConnectionObjects(null,
                    selectPermission, result);
            if (hasErrorOccur) {
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                        "Failed to close SQL related objects "
                                + "on User Group Permission"
                                + " with GroupCode of " + groupcode);
            }
            tp.methodExit();
        }
        return permission;
    }

    private Employee getEmployeeDetails(String strOperatorNo) throws DaoException {
        String functionName = className + "getEmployeeDetails";
        tp.methodEnter("getEmployeeDetails").println("strOperatorNo", strOperatorNo);

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement check = null;
        Employee employee = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            check = connection.prepareStatement(
                    sqlStatement.getProperty("get-employee-info"));
            check.setString(SQLStatement.PARAM1, strOperatorNo);

            resultSet = check.executeQuery();

            if (resultSet.next()) {
                employee = new Employee();
                employee.setNumber(resultSet.getString("OperatorNo"));
                employee.setPasscode(resultSet.getString("PassCode"));
                employee.setRole(resultSet.getString("Role"));
                employee.setName(resultSet.getString("OperatorName"));
                employee.setOperatorType(resultSet.getString("OperatorType"));
                employee.setRetailStoreID(resultSet.getString("StoreId"));
                employee.setStatus(resultSet.getString("Status"));
                employee.setWorkStationID(resultSet.getString("TerminalId"));

            }

        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get employee details: " + ex.getMessage());
            throw new DaoException("SQLException: @"
                    + functionName, ex);
        } finally {
            closeConnectionObjects(connection, check,resultSet);

            tp.methodExit(employee);
        }
        return employee;
    }

    private Employee mergeEmployeeDetails(Employee newEmployee, Employee oldEmployee){

        if(newEmployee.getName() == null){
            newEmployee.setName(oldEmployee.getName());
        }
        if(newEmployee.getOperatorType() == null){
            newEmployee.setOperatorType(oldEmployee.getOperatorType());
        }
        if(newEmployee.getWorkStationID() == null){
            newEmployee.setWorkStationID(oldEmployee.getWorkStationID());
        }
        if(newEmployee.getPasscode() == null){
            newEmployee.setPasscode(oldEmployee.getPasscode());
        }
        if(newEmployee.getStatus() == null){
            newEmployee.setStatus(oldEmployee.getStatus());
        }
        if(newEmployee.getRole() == null){
            newEmployee.setRole(oldEmployee.getRole());
        }
        if(newEmployee.getRetailStoreID() == null){
            newEmployee.setRetailStoreID(oldEmployee.getRetailStoreID());
        }

        return newEmployee;
    }
    
    /**
     * get authorization level of an operator
     * 
     * @param companyId
     * @param opeCode
     * @return Authorization
     */
    public Authorization getOperatorAuthorization(String companyId, String opeCode) throws DaoException {
        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement authorizationStmt = null;
        Authorization authorization = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            authorizationStmt = connection.prepareStatement(
                    sqlStatement.getProperty("view-authorization"));
            authorizationStmt.setString(SQLStatement.PARAM1, companyId);
            authorizationStmt.setString(SQLStatement.PARAM2, opeCode);
            resultSet = authorizationStmt.executeQuery();
            if (resultSet.next()) {
                authorization = new Authorization();
                authorization.setSecLevel1(resultSet.getBoolean("SecLevel1"));
                authorization.setSecLevel2(resultSet.getBoolean("SecLevel2"));
                authorization.setSecLevel3(resultSet.getBoolean("SecLevel3"));
                authorization.setSecLevel4(resultSet.getBoolean("SecLevel4"));
                authorization.setSecLevel5(resultSet.getBoolean("SecLevel5"));
                authorization.setSecLevel6(resultSet.getBoolean("SecLevel6"));
                authorization.setSecLevel7(resultSet.getBoolean("SecLevel7"));
                authorization.setSecLevel8(resultSet.getBoolean("SecLevel8"));
                authorization.setSecLevel9(resultSet.getBoolean("SecLevel9"));
                authorization.setSecLevel10(resultSet.getBoolean("SecLevel10"));
            }

        } catch (Exception ex) {
            LOGGER.logAlert(progName, "getOperatorAuthorization", Logger.RES_EXCEP_GENERAL,
                    "Failed to get employee details: " + ex.getMessage());
            throw new DaoException("SQLException: @getOperatorAuthorization", ex);
        } finally {
            closeConnectionObjects(connection, authorizationStmt, resultSet);
            tp.methodExit(authorization);
        }
        return authorization;
    }
    
    /**
     * Creates a new operator.
     *
     * @param operatorNumber
     *            The Number of the new Operator
     * @param passCode
     *            The Operator's Passcode
     * @return Error code when error occurred otherwise 0
     * @throws DaoException
     *             The Exception thrown when the method fails
     */
    public final int createOperator(final String operatorNumber,
            final String passCode) throws DaoException {

        String functionName = className + "createOperator";
        tp.methodEnter("createOperator");
        tp.println("operatorNumber", operatorNumber).println("passCode",
                passCode);

        int resultCode = 0;
        Connection connection = null;
        PreparedStatement insertInto = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertInto = connection.prepareStatement(sqlStatement
                    .getProperty("create-operator"));
            insertInto.setString(SQLStatement.PARAM1, operatorNumber);
            insertInto.setString(SQLStatement.PARAM2, passCode);
            insertInto.setString(SQLStatement.PARAM3, "NCR");
            insertInto.setString(SQLStatement.PARAM4, "");
            insertInto.setString(SQLStatement.PARAM5, "");
            insertInto.executeUpdate();
            connection.commit();
        } catch (SQLException sqlEx) {
            resultCode = 1;
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    sqlEx.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:createOperator",
                    sqlEx);

        } catch (Exception ex) {
            resultCode = 1;
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    ex.getMessage());
            rollBack(connection, "@SQLServerCredentialDAO:createOperator", ex);
        } finally {
            closeConnectionObjects(connection, insertInto, null);

            tp.methodExit(resultCode);
        }

        return resultCode;
    }
}
