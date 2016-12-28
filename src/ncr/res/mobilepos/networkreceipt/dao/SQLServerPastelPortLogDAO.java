package ncr.res.mobilepos.networkreceipt.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPastelPortLogDAO extends AbstractDao implements IPastelPortLogDAO{
	/**
	 * class instance of the DBManager.
     */
    private DBManager dbManager;
    /**
     * class instance of the Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * class instance of the debug log printer.
     */
    private Trace.Printer tp;
    /**
     * constructor.
     * 
     * @throws DaoException - thrown when any exception occurs
     */
    public SQLServerPastelPortLogDAO() throws DaoException {

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
	
	@Override
	public ResultSet getPastelPortLogData(final String storeId,
			final String txId, final String txDate) throws DaoException {
		
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("storeid", storeId)
				.println("txid", txId).println("txdate", txDate);
		
		Connection connection = null;
		PreparedStatement getPaymentStmt = null;
		ResultSet rs = null;
		try {

			SQLStatement sqlStatement = SQLStatement.getInstance();
			connection = dbManager.getConnection();

			getPaymentStmt = connection.prepareStatement(sqlStatement
					.getProperty("get-pastelportlog"));
			getPaymentStmt.setString(SQLStatement.PARAM1, storeId);
			getPaymentStmt.setString(SQLStatement.PARAM2, txDate);
			getPaymentStmt.setInt(SQLStatement.PARAM3, Integer.valueOf(txId));

			rs = getPaymentStmt.executeQuery();
		} catch (SQLException sqlEx) {
			LOGGER.logAlert("RecptDAO", Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get pastelport log data.", sqlEx);
		}
		return rs;
	}
}
