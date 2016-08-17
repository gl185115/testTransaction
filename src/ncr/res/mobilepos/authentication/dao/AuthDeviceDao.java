package ncr.res.mobilepos.authentication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.authentication.model.CorpStore;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.model.SignDetails;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * class that handles authentication and registration of devices.
 *
 */
public class AuthDeviceDao extends AuthDBManager implements IAuthDeviceDao {
    /**
     * The class instance of the database manager.
     */
    private DBManager dbManager;
    /**
     * The class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Holds the full name of the class.
     */
    private String className = "AuthDeviceDao";
    /**
     * Holds the shortened name of the class for use in logging.
     */
    private String progName = "AutDvDao";
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * Default constructor.
     * @throws DaoException - holds the exception that was thrown
     */
    public AuthDeviceDao() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());

    }

	@Override
	public final int registerTerminal(final String companyid, final String storeid,
			final String terminalid, final String devicename,
			final String udid, final String uuid,
			final int signstatus, final String signtid,
			final String signactivationkey)
			throws DaoException {

		String functionName = className + "registerUser";
		tp.methodEnter("registerUser")
		        .println("companyid", companyid)
		        .println("storeid", storeid)
				.println("terminalid", terminalid)
				.println("devicename", devicename)
				.println("udid", udid)
				.println("uuid", uuid)
				.println("signstatus", signstatus)
				.println("signtid", signtid)
				.println("signactivationkey", signactivationkey);

        Connection connection = null;
        PreparedStatement insertStmt = null;
        int result = ResultBase.RES_ERROR_GENERAL;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertStmt = connection
                .prepareStatement(sqlStatement.getProperty("save-device"));

            insertStmt.setString(SQLStatement.PARAM1, companyid);
            insertStmt.setString(SQLStatement.PARAM2, storeid);
            insertStmt.setString(SQLStatement.PARAM3, terminalid);
            insertStmt.setString(SQLStatement.PARAM4, uuid);
            insertStmt.setString(SQLStatement.PARAM5, udid);
            insertStmt.setInt(SQLStatement.PARAM6, signstatus);
            insertStmt.setString(SQLStatement.PARAM7, signtid);
            insertStmt.setString(SQLStatement.PARAM8, signactivationkey);
            if(insertStmt.executeUpdate() == SQLResultsConstants.ONE_ROW_AFFECTED){
            	result = ResultBase.RESREG_OK;
            }
            connection.commit();
        } catch (SQLException ex) {
        	LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to save device.\n" + ex.getMessage());
            throw new DaoException("SQLException: @" + functionName, ex);
        } catch (SQLStatementException ex) {
        	LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to save device.\n" + ex.getMessage());
            throw new DaoException("SQLStatementException: @" + functionName, ex);
        } catch (Exception ex) {
        	LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to save device.\n" + ex.getMessage());
            throw new DaoException("Exception: @" + functionName, ex);
        }finally {
            closeConnectionObjects(connection, insertStmt);

            tp.methodExit(result);
        }
        return result;
    }

    @Override
	public final int deregisterTerminal(final String storeid,
			final String terminalid) throws DaoException {
        String functionName = className + "deregisterUser";
        tp.methodEnter("deregisterUser");
        tp.println("storeid", storeid)
            .println("terminalid", terminalid);

        Connection connection = null;
        PreparedStatement deleteStmt = null;
        int result = 0;
        int ret = ResultBase.RESREG_OK;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteStmt = connection.prepareStatement(
                    sqlStatement.getProperty("delete-device"));
            deleteStmt.setString(SQLStatement.PARAM1, storeid);
            deleteStmt.setString(SQLStatement.PARAM2, terminalid);

            result = deleteStmt.executeUpdate();

            if (SQLResultsConstants.ONE_ROW_AFFECTED == result) {
                ret = ResultBase.RESREG_OK;
            } else {
                ret = ResultBase.RESREG_DEVICENOTEXIST;
                tp.println("The device does not exist.");
            }
            connection.commit();
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Failed to delete device.", e);
        } finally {
            closeConnectionObjects(connection, deleteStmt);

            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Deletes the user with the given udid.
     * @param udid - the unique device number of the device
     * @return boolean - true if successful, false if not
     * @throws DaoException - holds the exception that was thrown
     */
    public final boolean deleteDeviceWithUdid(final String udid)
    throws DaoException {

        String functionName = className + "deleteDeviceWithUdid";
        LOGGER.logFunctionEntry(progName, functionName, udid);

        tp.methodEnter("deleteDeviceWithUdid");
        tp.println("udid", udid);

        Connection connection = null;
        PreparedStatement deleteStmt = null;
        boolean result = false;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteStmt = connection.prepareStatement(
                    sqlStatement.getProperty("delete-device-with-udid"));

            deleteStmt.setString(SQLStatement.PARAM1, udid);

            int res = deleteStmt.executeUpdate();

            result = (SQLResultsConstants.ONE_ROW_AFFECTED == res);

            connection.commit();

        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            rollBack(connection,
                    "SQLException:@AuthDeviceDao.deleteDeviceWithUdid", e);
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, deleteStmt);

            tp.methodExit(result);
        }
        return result;
    }

    @Override
    public final int authenticateUser(final String storeid,
    		final String terminalid) throws DaoException {
        String functionName = className + "authenticateUser";

        tp.methodEnter("authenticateUser");
        tp.println("storeid", storeid)
        	.println("terminalid", terminalid);

        int ret = ResultBase.RESAUTH_OK;

        try {
            ret = validateDevice(storeid, terminalid);

            if (ret != ResultBase.RESAUTH_OK) {
                return ret;
            }

            int userState = getUserStatus(storeid, terminalid);

            if (userState == DeviceStatus.STATUS_DEVICENOTFOUND) {
                tp.println("Device was not found.");
                ret = userState;
            } else if (userState == DeviceStatus.STATUS_DEVICEONLINE) {
                tp.println("Device was already online.");
                ret = DeviceStatus.STATUS_DEVICESTATUSNOCHANGE;
                setState(storeid, terminalid, 1); // update last active time
            } else {
            	setState(storeid, terminalid, 1);
            }
        } catch (Exception e) {
           LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            tp.methodExit(ret);
        }

        return ret;
    }

    @Override
    public final int deauthenticateUser(
            final String storeid, final String terminalid, final String uuid,
            final String udid) throws DaoException {

        String functionName = className + "deauthenticateUser";

        tp.methodEnter("deauthenticateUser");
        tp.println("storeid", storeid)
            .println("terminalid", terminalid).println("uuid", uuid)
            .println("udid", udid);

        int ret = ResultBase.RESAUTH_OK;
        try {
            ret = validateDevice(storeid,
                    terminalid);
            if (ResultBase.RESAUTH_OK != ret) {
                return ret;
            }

            int userState = getUserStatus(storeid, terminalid);

            if (DeviceStatus.STATUS_DEVICENOTFOUND == userState) {
                tp.println("Device was not found.");
                ret = userState;
            } else if (DeviceStatus.STATUS_DEVICEOFFLINE == userState) {
                tp.println("Device was already offline.");
                ret = DeviceStatus.STATUS_DEVICESTATUSNOCHANGE;
            } else {
                if (SQLResultsConstants.ONE_ROW_AFFECTED
                        != setState(storeid, terminalid, 0)) {
                    tp.println("An error occured in setting the status.");
                    ret = ResultBase.RES_ERROR_GENERAL;
                }
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Get user's status directly from database.
     *
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device
     *  to request the status of
     *
     * @return DeviceStatus - a class which holds the status
     *  of the device
     *
     * @throws DaoException - holds the exception that was thrown
     */
    private DeviceStatus getUserStatusFromDB(final String storeid,
    		final String terminalid) throws DaoException {

        String functionName = className + "getUserStatusFromDB";

        tp.methodEnter("getUserStatusFromDB");
        tp.println("storeid", storeid)
            .println("terminalid", terminalid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        DeviceStatus retVal = new DeviceStatus();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(sqlStatement
                    .getProperty("select-device-by-storeId-terminalId"));

            selectStmt.setString(SQLStatement.PARAM1, storeid);
            selectStmt.setString(SQLStatement.PARAM2, terminalid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                retVal = new DeviceStatus(terminalid,
                		resultSet.getInt(resultSet.findColumn("State")), "");
            } else {
                retVal = new DeviceStatus(DeviceStatus.STATUS_DEVICENOTFOUND,
                        "Device was not found");
                retVal.setTerminalStatus(DeviceStatus.STATUS_DEVICENOTFOUND);
                tp.println("The device was not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(retVal);
        }
        return retVal;
    }

    /**
     * Get user's status directly from database.
     * @param terminalid - the terminal id of the device
     *  to request the status of
     * @return DeviceStatus - a class which holds the status
     *  of the device
     * @throws DaoException - holds the exception that was thrown
     */
    private DeviceStatus getUserStatusFromDB(final String terminalid)
    throws DaoException {

        String functionName = className + "getUserStatusFromDB";

        tp.methodEnter("getUserStatusFromDB");
        tp.println("terminalid", terminalid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        DeviceStatus retVal = new DeviceStatus();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-device-by-terminalid"));

            selectStmt.setString(1, terminalid);
            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                retVal = new DeviceStatus(terminalid,
                            resultSet.getInt(resultSet.findColumn("State")),
                            "");
            } else {
                retVal = new DeviceStatus(DeviceStatus.STATUS_DEVICENOTFOUND,
                        "Device was not found");
                retVal.setTerminalStatus(DeviceStatus.STATUS_DEVICENOTFOUND);
                tp.println("The device was not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(retVal);
        }
        return retVal;
    }

    @Override
    public final int getUserStatus(final String storeid,
            final String terminalid) throws DaoException {

        String functionName = className + "getUserStatus";

        tp.methodEnter("getUserStatus");
        tp.println("storeid", storeid)
        	.println("terminalid", terminalid);

        int status = ResultBase.RESREG_OK;

        try {
            DeviceStatus deviceStatus =
            		getUserStatusFromDB(storeid, terminalid);

            status = deviceStatus.getTerminalStatus();
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            tp.methodExit(status);
        }
        return status;
    }

    @Override
    public final int getUserStatus(final String terminalid)
    throws DaoException {

        String functionName = className + "getUserStatus";

        tp.methodEnter("getUserStatus");
        tp.println("terminalid", terminalid);

        int status = ResultBase.RESREG_OK;
        try {
            DeviceStatus stat = getUserStatusFromDB(terminalid);
            status = stat.getTerminalStatus();
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            tp.methodExit(status);
        }
        return status;
    }

    /**
     * Get the validy duration for authentication.
     * @return int - the number of miliseconds that the
     *  status of the device is valid
     * @throws DaoException - holds the exception that was thrown
     * @deprecated
     */
    @Deprecated
    private int getActiveValidityDuration() throws DaoException {
        IAuthAdminDao admin =
            DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getAuthAdminDAO();
        return admin.getActiveValidityDuration();
    }

    /**
     * Sets the state of the device.
     *
     * @param storeid - the store id
     * @param terminalid - the terminal id of the device to set
     * @param state - the new state
     *
     * @return int - result code of the request
     *
     * @throws DaoException - holds the exception that was thrown
     */
    private int setState(final String storeid, final String terminalid,
    		final int state) throws DaoException {

        tp.methodEnter("setState");
        tp.println("storeid", storeid)
            .println("terminalid", terminalid)
            .println("state", state);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        int result = 0;

        java.util.Calendar currentDate = java.util.Calendar.getInstance();
        Timestamp currenttime = new Timestamp(currentDate.getTimeInMillis());

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(
                    sqlStatement.getProperty("update-device-state"));

            updateStmt.setInt(SQLStatement.PARAM1, state);
            updateStmt.setString(SQLStatement.PARAM2, storeid);
            updateStmt.setString(SQLStatement.PARAM3, terminalid);
            tp.println("currenttime", currenttime.toString());
            updateStmt.setString(SQLStatement.PARAM4, currenttime.toString());

            result = updateStmt.executeUpdate();
            connection.commit();

        } catch (Exception e) {
            LOGGER.logAlert("AutDvDao", "setState", Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            rollBack(connection, "SQLException:@AuthDeviceDao.setState", e);
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, updateStmt);

            tp.methodExit(result);
        }
        return result;
    }

    /**
     * Validates the device.
     *
     * @param storeId - the store id of the device
     * @param terminalId - the terminal id of the device
     *
     * @return int - result code of the query
     *
     * @throws DaoException - holds the exception that was thrown
     */
    public final int validateDevice(final String storeId,
    		final String terminalId) throws DaoException {
        String functionName = className + "validateDevice";

        tp.methodEnter("validateDevice");
        tp.println("storeid", storeId)
        	.println("terminalid", terminalId);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        int ret = ResultBase.RES_ERROR_GENERAL;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(sqlStatement.
            		getProperty("select-device-by-storeId-terminalId"));

            selectStmt.setString(SQLStatement.PARAM1, storeId);
            selectStmt.setString(SQLStatement.PARAM2, terminalId);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                ret = ResultBase.RESAUTH_OK;
            } else {
                ret = ResultBase.RESREG_DEVICENOTEXIST;
                tp.println("The device does not exist.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(ret);
        }

        return ret;
    }

	@Override
	public final boolean isDeviceExisting(final String companyId, final String storeId,
			final String terminalId, final boolean isMatchAll)
			throws DaoException {
		String functionName = className + "isDeviceExisting";
		tp.methodEnter(DebugLogger.getCurrentMethodName())
				.println("storeid", storeId).println("terminalid", terminalId)
				.println("matchall", isMatchAll);

		Connection connection = null;
		PreparedStatement selectStmt = null;
		ResultSet resultSet = null;
		boolean ret = false;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			if (isMatchAll) {
				selectStmt = connection.prepareStatement(sqlStatement
						.getProperty("select-device-strict"));
				selectStmt.setString(SQLStatement.PARAM1, companyId);
				selectStmt.setString(SQLStatement.PARAM2, storeId);
				selectStmt.setString(SQLStatement.PARAM3, terminalId);
			} else {
				selectStmt = connection.prepareStatement(sqlStatement
						.getProperty("select-device-terminalid"));
				selectStmt.setString(SQLStatement.PARAM1, terminalId);
			}

			resultSet = selectStmt.executeQuery();
			if (resultSet.next()) {
				ret = true;
			}

		} catch (Exception e) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					e.getMessage());
			throw new DaoException("Failed to check device existent.", e);
		} finally {
			closeConnectionObjects(connection, selectStmt, resultSet);

			tp.methodExit(ret);
		}
		return ret;
	}
    @Override
    public final boolean isDeviceExisting(final String uuid,
            final String udid) throws DaoException {
        tp.methodEnter("isDeviceExisting");
        tp.println("uuid", uuid).println("udid", udid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        boolean ret = false;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty(
                            "select-device-by-uuid-udid"));
            selectStmt.setString(SQLStatement.PARAM1, uuid);
            selectStmt.setString(SQLStatement.PARAM2, udid);
            resultSet = selectStmt.executeQuery();
            if (resultSet.next()) {
                ret = true;
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName, "AuthDeviceDao.isDeviceExisting()",
                    Logger.RES_EXCEP_SQL, "SQLException:" + e.getMessage());
            tp.methodExit("SQLException occurred.");
            throw new DaoException("SQLException", e);
        } catch (SQLStatementException e) {
            LOGGER.logAlert(progName, "AuthDeviceDao.isDeviceExisting()",
                    Logger.RES_EXCEP_SQL,
                    "SQLStatementException:" + e.getMessage());
            tp.methodExit("SQLStatementException occurred.");
            throw new DaoException("SQLStatementException", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(ret);
        }
        return ret;
    }

    @Override
    public final boolean updateExistingDeviceToken(
            final String storeid, final String terminalid,
            final String udid, final String uuid,
            final String devicename) throws DaoException {

        String functionName = className + "updateExistingDeviceToken";

        tp.methodEnter("updateExistingDeviceToken");
        tp.println("storeid", storeid)
            .println("terminalid", terminalid).println("udid", udid)
            .println("uuid", uuid).println("devicename", devicename);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        int result = 0;
        boolean ret = false;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(
                    sqlStatement.getProperty("update-device-uuidtoken"));

            updateStmt.setString(SQLStatement.PARAM1, uuid);
            updateStmt.setString(SQLStatement.PARAM2, devicename);
            updateStmt.setString(SQLStatement.PARAM3, storeid);
            updateStmt.setString(SQLStatement.PARAM4, terminalid);

            result = updateStmt.executeUpdate();

            if (result == SQLResultsConstants.ONE_ROW_AFFECTED) {
                ret = true;
            }

            connection.commit();

        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, updateStmt);

            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Check if parameter is a valid udid.
     * @param udid - the udid to check for validity
     * @return boolean - true if valid, false if not
     */
    public static boolean isValidUDID(final String udid) {

        boolean isValid = false;

        String expression = "[A-Fa-f0-9]{40}";

        CharSequence inputStr = udid;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    /**
     * Check if parameter is a valid uuid.
     * @param uuid - the uuid to check for validity
     * @return boolean - true if valid, false if not
     */
    public static boolean isValidUUID(final String uuid) {

        boolean isValid = false;

        String expression = "([A-Fa-f0-9]{8})?[-]?([A-Fa-f0-9]{4})?[-]?"
            + "([A-Fa-f0-9]{4})?[-]?([A-Fa-f0-9]{4})?[-]?([A-Fa-f0-9]{12})";

        CharSequence inputStr = uuid;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }

        return isValid;
    }

    @Override
    public final ResultBase getWebApiUrl(final String corpid,
            final String passcode) throws DaoException {

        String functionName = className + "getWebApiUrl";

        tp.methodEnter("getWebApiUrl");
        tp.println("corpid", corpid).println("passcode", passcode);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        String webAppUrl = null;
        ResultBase result = new ResultBase();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-passcode-url-by-corpid"));
            selectStmt.setString(1, corpid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString(
                        resultSet.findColumn("Passcode"))
                        .trim().equals(passcode)) {
                     webAppUrl =
                         resultSet.getString(resultSet.findColumn("Url"));
                     result.setMessage(webAppUrl);
                     result.setNCRWSSResultCode(ResultBase.RESAUTH_OK);

                } else {
                    tp.println("Passcode is invalid.");
                    result.setNCRWSSResultCode(
                            ResultBase.RESAUTH_PASSCODE_INVALID);
                }
            } else {
                result.setNCRWSSResultCode(ResultBase.RESAUTH_CORPID_NOTEXIST);
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(result);
        }
        return result;
    }

    @Override
    public final String getWebApiUrl(final String corpid)
    throws DaoException {

        String functionName = className + "getWebApiUrl";

        tp.methodEnter("getWebApiUrl");
        tp.println("corpid", corpid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        String webAppUrl = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-url-by-corpid"));
            selectStmt.setString(1, corpid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                webAppUrl = resultSet.getString(resultSet.findColumn("Url"));
            } else {
                tp.println("Corp url not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(webAppUrl);
        }
        return webAppUrl;
    }

    /**Deprecated*/
    @Override
    public final String getCorpName(final String corpid)
    throws DaoException {

        String functionName = className + "getWebApiUrl";

        tp.methodEnter("getCorpName");
        tp.println("corpid", corpid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        String corpname = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-corpname-by-corpid"));
            selectStmt.setString(1, corpid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                corpname = resultSet
                    .getString(resultSet.findColumn("CorpName"));
            } else {
                tp.println("Corp identity not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(corpname);
        }
        return corpname;
    }

    @Override
    public final String getStoreName(final String corpid,
            final String storeid) throws DaoException {

        String functionName = className + "getStoreName";

        tp.methodEnter("getStoreName");
        tp.println("storeid", storeid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        String storename = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt = connection.prepareStatement(
                    sqlStatement.getProperty("select-storename-"
                            + "by-corpid-storeid"));
            selectStmt.setString(1, storeid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                storename =
                    resultSet.getString(resultSet.findColumn("StoreName"));
            } else {
                tp.println("Store identity not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(storename);
        }
        return storename;
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public final int setSignatureActivationStatus(final String storeId, final String terminalId, final String udid,
            final String uuid, final int signStatus, final String signTid,
            final String signActivationKey) throws DaoException {

        String functionName = className + "setSignatureActivationStatus";

        tp.methodEnter("setSignatureActivationStatus");
        tp.println("storeId", storeId)
            .println("terminalId", terminalId).println("udid", udid)
            .println("uuid", uuid).println("signStatus", signStatus)
            .println("signTid", signTid)
            .println("signActivationKey", signActivationKey);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        int result = ResultBase.RES_ERROR_GENERAL;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(
                    sqlStatement.getProperty("update-device-signature-"
                            + "activation-status"));
            updateStmt.setInt(SQLStatement.PARAM1, signStatus);
            updateStmt.setString(SQLStatement.PARAM2, signTid);
            updateStmt.setString(SQLStatement.PARAM3, signActivationKey);
            updateStmt.setString(SQLStatement.PARAM4, storeId);
            updateStmt.setString(SQLStatement.PARAM5, terminalId);
            updateStmt.setString(SQLStatement.PARAM6, udid);
            updateStmt.setString(SQLStatement.PARAM7, uuid);

            if (SQLResultsConstants.ONE_ROW_AFFECTED
                    == updateStmt.executeUpdate()) {
                result = ResultBase.RESAUTH_OK;
            } else {
                tp.println("Failed to udpate signature activation status.");
            }
            connection.commit();
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            rollBack(connection,
                    "SQLException:@AuthDeviceDao.setSignatureActivationStatus",
                    e);
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, updateStmt);

            tp.methodExit(result);
        }
        return result;

    }

    @Override
    public final SignDetails getSignDetails(final String companyId, final String storeId, final String terminalId,
            final String udid, final String uuid) throws DaoException {

        String functionName = className + "getSignDetails";

        tp.methodEnter("getSignDetails");
        tp.println("storeId", storeId)
            .println("terminalId", terminalId).println("udid", udid)
            .println("uuid", uuid);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        SignDetails signDetails = new SignDetails();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmt =
                connection.prepareStatement(
                        sqlStatement.getProperty("get-signdetails"));
            selectStmt.setString(SQLStatement.PARAM1, companyId);
            selectStmt.setString(SQLStatement.PARAM2, storeId);
            selectStmt.setString(SQLStatement.PARAM3, terminalId);
            selectStmt.setString(SQLStatement.PARAM4, udid);
            selectStmt.setString(SQLStatement.PARAM5, uuid);

            resultSet = selectStmt.executeQuery();

            if (resultSet.next()) {
                signDetails.setSignStatus(
                        resultSet.getInt(resultSet.findColumn("SignStatus")));

                String signTId = resultSet.getString(resultSet.findColumn("SignTid"));
                signDetails.setSignTid(signTId == null ? " " : signTId.trim());

                String signActivationKey = resultSet.getString(resultSet.findColumn("SignActivationKey"));
                signDetails.setSignActivationKey(signActivationKey == null ? " " : signActivationKey.trim());
            } else{
                tp.println("Sign details not found.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(signDetails);
        }
        return signDetails;
    }

    @Override
	public final ViewCorpStore validateCorpStore(final String companyId, final String storeId,
			final String passCode) throws DaoException {
        String functionName = className + ".validateCorpStore";
		tp.methodEnter(DebugLogger.getCurrentMethodName())
				.println("storeid", storeId).println("passcode", passCode)
				.println("companyid", companyId);

        Connection connection = null;
        PreparedStatement selectStmt = null;
        ResultSet resultSet = null;
        ViewCorpStore result = new ViewCorpStore();

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			selectStmt = connection.prepareStatement(sqlStatement
					.getProperty("select-passcode-permission-from-corp-store"));
			selectStmt.setString(SQLStatement.PARAM1, storeId);
			selectStmt.setString(SQLStatement.PARAM2, companyId);
			resultSet = selectStmt.executeQuery();
			if (resultSet.next()) {
				String storePasscode = resultSet.getString("PassCode");
				if (storePasscode == null || storePasscode.isEmpty()) {
					result.setNCRWSSResultCode(ResultBase.RESAUTH_OK);
				} else {
					if (storePasscode.equals(passCode)) {
						if (resultSet.getInt("Permission") == 1) {
							result.setNCRWSSResultCode(ResultBase.RESAUTH_OK);
						} else {
							tp.println("Permission to register is denied.");
							result.setNCRWSSResultCode(ResultBase.RESAUTH_REG_DENIED);
						}
					} else {
						tp.println("Passcode is wrong.");
						result.setNCRWSSResultCode(ResultBase.RESAUTH_PASSCODE_INVALID);
					}
				}
			} else {
				tp.println("CompanyId/StoreId does not exist in MST_CORP_STORE.");
				result.setNCRWSSResultCode(ResultBase.RESAUTH_STOREID_NOTEXIST);
			}

			if (result.getNCRWSSResultCode() == ResultBase.RESAUTH_OK) {
				CorpStore corpStore = new CorpStore();
				corpStore.setCompanyName(resultSet.getString("CompanyName"));
				corpStore.setStorename(resultSet.getString("StoreName"));
				result.setCorpstore(corpStore);
			}
		} catch (SQLStatementException e) {
			LOGGER.logAlert(progName, functionName,
					Logger.RES_EXCEP_SQLSTATEMENT,
					"Failed to validate corp-store.\n " + e.getMessage());
			throw new DaoException(
					"SQLStatementException: @AuthDeviceDao.validateCorpStore ",
					e);
        } catch (SQLException e) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
					"Failed to validate corp-store.\n " + e.getMessage());
			throw new DaoException(
					"SQLException: @AuthDeviceDao.validateCorpStore ", e);
        } catch (Exception e) {
			LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to validate corp-store.\n " + e.getMessage());
			throw new DaoException(
					"Exception: @AuthDeviceDao.validateCorpStore ", e);
        } finally {
            closeConnectionObjects(connection, selectStmt, resultSet);

            tp.methodExit(result);
        }
        return result;
    }
}
