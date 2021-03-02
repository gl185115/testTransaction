package ncr.res.mobilepos.intaPay.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;
import ncr.res.mobilepos.intaPay.model.IntaPayStoreConfig;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerIntaPayDAO extends AbstractDao implements IIntaPayDAO {

    private final String PROG_NAME = "IIntaPayDAO";
    /** The database manager. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * constructor
     *
     * @throws DaoException
     */
    public SQLServerIntaPayDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * get field Value in the table PRM_SYSTEM_CONFIG
     *
     * @throws DaoException
     */
    @Override
    public Map<String, String> getPrmSystemConfigValue(String Category) throws DaoException {
        String functionName = "getPrmSystemConfigValue";
        tp.methodEnter(functionName);

        Map<String, String> mapReturn = new HashMap<String, String>();

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            preparedStatement = connection
                    .prepareStatement(sqlStatement.getProperty("get-system-parameter-information"));
            preparedStatement.setString(SQLStatement.PARAM1, Category);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                mapReturn.put(resultSet.getString("KeyId").trim(), resultSet.getString("Value").trim());
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Value.", sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerIntaPayDAO.getPrmSystemConfigValue", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get guestzone list.", ex);
            throw new DaoException("Exception:" + " @SQLServerIntaPayDAO.getPrmSystemConfigValue", ex);
        } finally {
            closeConnectionObjects(connection, preparedStatement, resultSet);
            tp.methodExit(mapReturn.size());
        }

        return mapReturn;
    }

    /**
     * get field Value in the table PRM_SYSTEM_CONFIG
     *
     * @throws DaoException
     */
    @SuppressWarnings("null")
    @Override
    public IntaPayStoreConfig getStoreParam(String companyId, String storeId) throws DaoException {
        String functionName = "getStoreParam";
        tp.methodEnter(functionName);

        IntaPayStoreConfig intaPayStoreConfig = null;

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            String query = String.format(sqlStatement.getProperty("get-store-parameter"),
                    IntaPayConstants.KEYID_API_KEY_STORE_PARAM + "," + IntaPayConstants.KEYID_MCH_CODE_STORE_PARAM);
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(SQLStatement.PARAM1, companyId);
            preparedStatement.setString(SQLStatement.PARAM2, storeId);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String apiKey = resultSet.getString(IntaPayConstants.KEYID_API_KEY_STORE_PARAM);
                String mchCode = resultSet.getString(IntaPayConstants.KEYID_MCH_CODE_STORE_PARAM);
                if(!StringUtility.isNullOrEmpty(apiKey) && !StringUtility.isNullOrEmpty(mchCode)){
                    intaPayStoreConfig = new IntaPayStoreConfig();
                    intaPayStoreConfig.setAPIKey(apiKey.trim());
                    intaPayStoreConfig.setMchCode(mchCode.trim());
                }
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get Value.", sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerIntaPayDAO.getStoreParam", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get guestzone list.", ex);
            throw new DaoException("Exception:" + " @SQLServerIntaPayDAO.getStoreParam", ex);
        } finally {
            closeConnectionObjects(connection, preparedStatement, resultSet);
            tp.methodExit(intaPayStoreConfig);
        }

        return intaPayStoreConfig;
    }
}
