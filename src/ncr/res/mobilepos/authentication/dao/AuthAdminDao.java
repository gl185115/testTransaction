package ncr.res.mobilepos.authentication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * AuthAdminDao handles database access for administration tasks
 * of the RES Authentication service.
 *
 */
public class AuthAdminDao extends AuthDBManager implements IAuthAdminDao {
    /***
     * the class instance of iowriter.
     */
    private static final Logger  LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /***
     * holds the the short class name used for logging.
     */
    private String progName = "AutAdDao";
    /***
     * holds the the full class name.
     */
    private String className = "AuthAdminDao";
    /**
     * instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * DB Access Handler.
     */
    private DBManager dbManager;

    /**
     * Constructor.
     * @throws DaoException - the exception that was thrown
     */
    public AuthAdminDao() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * validatePasscode
     * Check if passcode matches to that of the database.
     *
     * @param passcode - passcode to validate
     * @return int value determining the validity of the passcode data.
     * @throws DaoException - the exception that was thrown
     */
    public final int validatePasscode(final String passcode)
        throws DaoException {

        String functionName = className + "validatePasscode";

        tp.methodEnter("validatePasscode");
        tp.println("passcode", passcode);

        int ret = ResultBase.RESREG_OK;

        ResultSet result = null;

        String dbpasscode = "";
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();

            result = executeQuery(sqlStatement
                    .getProperty("get-system-parameter-value"),
                        "Authentication",
                        "RegistrationPasscodeExpiry");

            if (result.next()) {
                String expiryTime =
                    result.getString(result.findColumn("Value"));
                Timestamp expiry = Timestamp.valueOf(expiryTime);
                Calendar c = Calendar.getInstance();
                if (expiry.before(c.getTime())) {
                    ret = ResultBase.RESREG_PASSCODEEXPIRED;
                    tp.println("Passcode has expired.");
                } else {
                    result = executeQuery(sqlStatement
                                .getProperty("get-system-parameter-value"),
                                "Authentication",
                                "RegistrationPasscode");

                    if (result.next()) {
                        dbpasscode =
                            result.getString(result.findColumn("Value"));

                        if (dbpasscode.equals(passcode)) {
                            ret = ResultBase.RESREG_OK;
                        } else {
                            ret = ResultBase.RESREG_INVALIDPASSCODE;
                        }
                    } else {
                        ret = ResultBase.RES_ERROR_GENERAL;
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    e.getMessage());
            throw new DaoException("SQL Error", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            connectionClose();
            tp.methodExit(ret);
        }

        return ret;
    }

    /**
     * setPasscode
     * Set passcode for registetring iOS devices to RES Authentication service.
     * Registration fails if adminKey is invalid.
     *
     * @param passcode - passcode to set
     * @param adminKey - admin key to verify
     * @param expiry - length of time (in number of hours)
     *                 it takes for the passcode to become invalid.
     * @return ResultBase structure containing the information of the execution.
     * @throws DaoException - the exception that was thrown
     */
    public final int setPasscode(final String passcode,
            final String adminKey, final int expiry)
    throws DaoException {

        String functionName = className + "setPasscode";

        tp.methodEnter("setPasscode");
        tp.println("passcode", passcode).println("adminKey", adminKey)
            .println("expiry", expiry);

        int ret = ResultBase.RESREG_OK;
        ResultSet result = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();

            result = executeQuery(sqlStatement
                    .getProperty("get-system-parameter-validate-value"),
                        "Authentication", "AdminKey", adminKey);

            if  (result.next()) {

				if (1 != executeUpdate(
						sqlStatement.getProperty("set-system-parameter-value"),
						passcode, "Authentication", "RegistrationPasscode")) {
					tp.println("Failed to retrieve registration passcode.");
					ret = ResultBase.RES_ERROR_GENERAL;
				}

                Calendar c = Calendar.getInstance();
                c.add(Calendar.HOUR, (int) expiry);
                Timestamp  sqlDate = new Timestamp(c.getTime().getTime());

                if (1 != executeUpdate(
                        sqlStatement.getProperty("set-system-parameter-value"),
                        sqlDate.toString(), "Authentication",
                        "RegistrationPasscodeExpiry")) {
                    tp.println("Failed to set registration passcode expiry.");
                    ret = ResultBase.RES_ERROR_GENERAL;
                }
            } else {
                tp.println("AdminKey was invalid.");
                ret = ResultBase.RESREG_INVALIDADMINKEY;
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQL,
                    e.getMessage());
            throw new DaoException("SQL Error", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            connectionClose();
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * setAdminKey
     * Set adminKey credential to restrict access to setting
     * the registration passcode.
     * Modifying the adminKey will fail if the currentkey parameter
     * does not match to
     * currently set in the Authentication service administration data.
     *
     * @param currentkey - current admin key that is set to the database.
     * @param newkey - new key to replace the currently set admin key.
     * @return ResultBase structure containing the information of the execution.
     * @throws DaoException - the exception that was thrown
     */
    public final int setAdminKey(final String currentkey, final String newkey)
    throws DaoException {

        String functionName = className + "setAdminKey";

        tp.methodEnter("setAdminKey");
        tp.println("currentkey", currentkey).println("newkey", newkey);

        int ret = ResultBase.RESREG_OK;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();

            if (1 != executeUpdate(
                sqlStatement.getProperty("set-system-parameter-validate-value"),
                newkey, "Authentication", "AdminKey", currentkey)) {
                tp.println("AdminKey is invalid.");
                ret = ResultBase.RESREG_INVALIDADMINKEY;
            }
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                                e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        }  finally {
            connectionClose();
            tp.methodExit(ret);
        }

        return ret;
    }

    /**
     * getActiveValidityDuration
     * Retrieves the activevalidity duration
     * value from the Admin database table.
     *
     * @return int value representing the amount of time
     * that the devices remains
     * in active status.
     * @throws DaoException - the exception that was thrown
     */
    public final int getActiveValidityDuration() throws DaoException {

        String functionName = className + "getActiveValidityDuration";

        tp.methodEnter("getActiveValidityDuration");

        ResultSet result = null;
        int retVal = -1;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();

            result = executeQuery(
                        sqlStatement.getProperty("get-system-parameter-value"),
                        "Authentication", "AuthExpiry");

            if (!result.next()) {
                result.close();
                connectionClose();
                tp.println("Failed to find AuthExpiry.");
                return 0;
            }

            retVal = result.getInt(result.findColumn("Value"));
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    e.getMessage());
            throw new DaoException("SQL Error", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            connectionClose();
            tp.methodExit(retVal);
        }
        return retVal;
    }

    /**
     * removeDevice
     * Administration interface to removing device registration
     * data from database
     * using admin credential.
     * removeDevice will fail if adminKey does not
     * match to currently set adminKey in database.
     *
     * @param deviceid - ID of device to be removed from database.
     * @param adminKey - admin credential identifier to allow deletion
     * of device using device ID only.
     * @return ResultBase structure containing the information of the execution.
     * @throws DaoException - the exception that was thrown
     */
    public final int removeDevice(final String deviceid, final String adminKey)
    throws DaoException {

        String functionName = className + "removeDevice";

        tp.methodEnter("removeDevice");
        tp.println("deviceid", deviceid).println("adminKey", adminKey);

        int ret = ResultBase.RESREG_OK;

        PreparedStatement deleteStmnt = null;
        Connection connection = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            result = executeQuery(
                    sqlStatement.getProperty("get-system-parameter-value"),
                    "Authentication", "AdminKey");
            if (!result.next()) {
                tp.println("Failed to find AdminKey.");
                return 0;
            }

            if (adminKey.equals(result.getString(result.findColumn("Value")))) {

                deleteStmnt = connection.prepareStatement(
                        sqlStatement.getProperty("delete-autdevices"));
                deleteStmnt.setString(SQLStatement.PARAM1, deviceid);

                int deleteResult = deleteStmnt.executeUpdate();

                if (deleteResult == SQLResultsConstants.NO_ROW_AFFECTED) {
                    tp.println("The device does not exist.");
                    ret = ResultBase.RESREG_DEVICENOTEXIST;
                }
                connection.commit();
            } else {
                tp.println("AdminKey is invalid.");
                ret = ResultBase.RESREG_INVALIDADMINKEY;
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    e.getMessage());
            throw new DaoException("SQL Error", e);
        } catch (Exception e) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO,
                    e.getMessage());
            throw new DaoException("Abnormal operation!", e);
        } finally {
            closeConnectionObjects(connection, deleteStmnt, result);
            
            tp.methodExit(ret);
        }
        return ret;
    }
}
