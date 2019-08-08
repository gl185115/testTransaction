/*
* Copyright (c) 2019 NCR/JAPAN Corporation SW-R&D
*
* SQLServerSystemConfigDAO
*
* SQLServerSystemConfigDAO Class is
* A Data Access Object implementation for System Configuration.
*
* Campos, Carlos
*/
package ncr.res.mobilepos.systemconfiguration.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemconfiguration.model.SystemConfigInfo;

/**
 * A Data Access Object implementation for System Configuration.
 *
 */
public class SQLServerSystemConfigDAO extends AbstractDao {
    /**
     * Database Manager.
     */
    private DBManager dbManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer for Debug.
     */
    private Trace.Printer tp;
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "SysCfDao";
    /**
     * The class constructor.
     *
     * @throws DaoException        Exception thrown when construction fails.
     */
    public SQLServerSystemConfigDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Get All Informations from PRM_SYSTEM_CONFIG.
     * @return systemConfigInfoList
     * @throws DaoException  The Exception thrown when getting the System
     *                        Parameters fails
     */
    public List<SystemConfigInfo> getSystemConfigInfo() throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);

		List<SystemConfigInfo> systemConfigInfoList = new ArrayList<SystemConfigInfo>();
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		SystemConfigInfo systemConfigInfo = null;
		try {
			connection = dbManager.getConnection();

			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("get-system-parameters"));

			result = select.executeQuery();
			while (result.next()) {
				systemConfigInfo = new SystemConfigInfo();
				systemConfigInfo.setCategory(result.getString("Category").trim());
				systemConfigInfo.setKeyId(result.getString("KeyId").trim());
				systemConfigInfo.setValue(result.getString("Value") == null ? null : result.getString("Value").trim());
				systemConfigInfoList.add(systemConfigInfo);
			}
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get the System Configuration Informations.",
					e);
			throw new DaoException("SQLException:@" + "SQLServerSystemConfigDAO." + functionName, e);
		} finally {
			closeConnectionObjects(connection, select, result);
		}
		return systemConfigInfoList;
	}

	/**
     * Get the Value From PRM_SYSTEM_CONFIG by keyId And category.
     * @param key The key of the System Parameter
     * @param category The Category where the System Parameter belongs
     * @return value
	 */
	public final String getParameterValue(final String keyId, final String category){
		List<SystemConfigInfo> systemConfigInfoList = GlobalConstant.getSystemConfigInfoList();
		String value = null;
		if(systemConfigInfoList != null && !systemConfigInfoList.isEmpty()){
			for (SystemConfigInfo systemConfigInfo : systemConfigInfoList) {
				if(keyId.equals(systemConfigInfo.getKeyId()) && category.equals(systemConfigInfo.getCategory())){
					value = systemConfigInfo.getValue();
					break;
				}
			}
		}
		return value;
	}

    /**
     * Get the System parameters from PRM_SYSTEM_CONFIG by category.
     * @param  List<SystemConfigInfo> systemConfigInfoList
     * @return The key-value pair of each System parameter
     */
	public final Map<String, String> getPrmSystemConfigValue(String category){
		List<SystemConfigInfo> systemConfigInfoList = GlobalConstant.getSystemConfigInfoList();
		Map<String, String> mapReturn = new HashMap<String, String>();
		if(systemConfigInfoList != null && !systemConfigInfoList.isEmpty()){
			for (SystemConfigInfo systemConfigInfo : systemConfigInfoList) {
				if(category.equals(systemConfigInfo.getCategory())){
					mapReturn.put(systemConfigInfo.getKeyId(), systemConfigInfo.getValue());
				}
			}
		}
		return mapReturn;
	}

    /**
     * Get the value of a System parameter.
     * @param key            The key of the System Parameter
     * @param category        The Category where the System Parameter belongs
     * @return                The value of the System Parameter in String Format
     * @throws DaoException    The Exception thrown when the method fails
     */
	public final String getParameterString(final String key,
			final String category) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("Key", key)
				.println("Category", category);

        String value = "";
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-system-parameter-value"));
            select.setString(1, category);
            select.setString(2, key);

            result = select.executeQuery();
            if (result.next()) {
                value = result.getString(result.findColumn("Value"));
            }
        } catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get the System parameter of " + key, e);
			throw new DaoException("SQLException:@"
					+ "SQLServerSystemConfigDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(value);
        }

        return value;
    }

    /**
     * Get the value of a System parameter.
     * @param key                The key of the System Parameter
     * @param category            The Category where the
     *                               System Parameter belongs
     * @return                    The value of the System Parameter
     *                               in Boolean Format
     * @throws DaoException        The Exception thrown when the method fails
     */
	public final boolean getParameterBoolean(final String key,
			final String category) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("Key", key)
				.println("Category", category);

        boolean value = false;
        try {
            value = Boolean.parseBoolean(getParameterString(key, category));
        } catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to get the System Parameter", e);
			throw new DaoException("Exception:@" + "SQLServerSystemConfigDAO."
					+ functionName + "  - Cannot convert value to boolean.");
        } finally {
            tp.methodExit(value);
        }
        return value;
    }

    /**
     * Set the System Parameters.
     * @param params            The Parameters to be set
     * @return                    The number of rows affected
     * @throws DaoException        The Exception thrown when the method fials
     */
	public final int setParameterString(final String... params)
			throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        int result = 0;
        Connection connection = null;
        PreparedStatement select = null;
        try {
            connection = dbManager.getConnection();

            // Retrieves sql query statement
            SQLStatement sqlStatement = SQLStatement.getInstance();
            String statement =
                sqlStatement.getProperty("set-system-parameter-value");
            select = connection.prepareStatement(statement);

            for (int i = 0; i < params.length; i++) {
                select.setString(i + 1, params[i]);
            }

            result = select.executeUpdate();
            connection.commit();
		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to set the System Parameters.", e);
			throw new DaoException("SQLException:@SQLServerSystemConfigDAO."
					+ functionName, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to set the System Parameters.", e);
			throw new DaoException("Exception: @SQLServerSystemConfigDAO."
					+ functionName, e);
		} finally {
			closeConnectionObjects(connection, select);

			tp.methodExit(result);
		}
        return result;
    }

    /**
     * Set the System Parameters.
     * @param params            The Parameters to be set
     * @return                    The number of rows affected
     * @throws DaoException        The Exception thrown when the method fials
     */
	public final int addParameterString(final String... params)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);

        int result = 0;
        Connection connection = null;
        PreparedStatement addStmnt = null;
        try {
            connection = dbManager.getConnection();

            // Retrieves sql query statement
            SQLStatement sqlStatement = SQLStatement.getInstance();
            String statement =
                sqlStatement.getProperty("add-system-parameter-value");
            addStmnt = connection.prepareStatement(statement);

            for (int i = 0; i < params.length; i++) {
                addStmnt.setString(i + 1, params[i]);
            }

            result = addStmnt.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
        	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
					functionName + ": Failed to add the System Parameter.", e);
			throw new DaoException("SQLException:@SQLServerSystemConfigDAO." + functionName, e);
        } catch (Exception e) {
        	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to add the System Parameter.", e);
			throw new DaoException("Exception:@SQLServerSystemConfigDAO."
					+ functionName, e);
        } finally {
            closeConnectionObjects(connection, addStmnt);

            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Set the System Parameters.
     * @param keyPairParam        List of Keys with value.
     * @return                    The number of rows affected
     * @throws DaoException       The Exception thrown when the method fials
     */
	public final int setParameterValue(final Map<String, String> keyPairParam)
			throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);

        int result = 0;
        Connection connection = null;
        PreparedStatement update = null;
        try {
            connection = dbManager.getConnection();

            // Retrieves sql query statement
            SQLStatement sqlStatement = SQLStatement.getInstance();
            String statement =
                sqlStatement.getProperty("set-system-parameter-value-by-key");
            update = connection.prepareStatement(statement);

            for (Entry<String, String> param : keyPairParam.entrySet()) {
                String key = param.getKey();
                String value = param.getValue();
                if (StringUtility.isNullOrEmpty(value)) {
                    break;
                }
                update.setString(SQLStatement.PARAM1, value);
                update.setString(SQLStatement.PARAM2, key);
                result += update.executeUpdate();
            }

            connection.commit();

		} catch (SQLException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to set the System Parameters.", e);
			throw new DaoException("SQLException: @SQLServerSystemConfigDAO."
					+ functionName, e);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
					+ ": Failed to set the System Parameters.", e);
			throw new DaoException("Exception: @SQLServerSystemConfigDAO."
					+ functionName, e);
		} finally {
			closeConnectionObjects(connection, update);
			tp.methodExit(result);
		}

		return result;
	}
}

