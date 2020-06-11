package ncr.res.mobilepos.futurePay.dao;

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
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerFuturePayDAO extends AbstractDao implements
        IFuturePayDAO {

    private final String PROG_NAME = "CusSearchDao";
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
    public SQLServerFuturePayDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
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
    public Map<String ,String> getPrmSystemConfigValue(String Category)
            throws DaoException {
        String functionName = "getPrmSystemConfigValue";
        tp.methodEnter(functionName);

        Map<String ,String> mapReturn = new HashMap<String, String>();

        ResultSet resultSet = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            preparedStatement = connection.prepareStatement(sqlStatement
                    .getProperty("get-system-parameter-information"));
            preparedStatement.setString(SQLStatement.PARAM1, Category);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
            	mapReturn.put(resultSet.getString("KeyId").trim(), resultSet.getString("Value").trim());
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get Value.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerFuturePayDAO.getPrmSystemConfigValue",
                    sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get guestzone list.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerFuturePayDAO.getPrmSystemConfigValue",
                    ex);
        } finally {
            closeConnectionObjects(connection, preparedStatement, resultSet);
            tp.methodExit(mapReturn.size());
        }

        return mapReturn;
    }

}
