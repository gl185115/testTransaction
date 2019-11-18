package ncr.res.mobilepos.poslogstatus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;
import ncr.res.mobilepos.poslogstatus.model.UnsendTransaction;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPoslogStatusDAO extends AbstractDao implements IPoslogStatusDAO {

	private final SQLStatement sqlStatement;
	/**
	 * database manager.
	 */
	private DBManager dbManager;
	/**
	 * logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();
	/** The Trace Printer. */
	private Trace.Printer tp;

	private String progName = "PoslogStatusDAO";

	/**
	 * The class constructor.
	 * 
	 * @throws DaoException
	 *             Exception thrown when construction fails.
	 */
	public SQLServerPoslogStatusDAO() throws DaoException {
		this.dbManager = JndiDBManagerMSSqlServer.getInstance();
		this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
		this.sqlStatement = SQLStatement.getInstance();
	}

	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param consolidation
	 * @param transfer
	 * @param columnName
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final PoslogStatusInfo checkPoslogStatus(boolean consolidation, boolean transfer, String companyId,
			String retailStoreId, String businessDayDate, String columnName) throws DaoException {
		tp.methodEnter("checkPoslogStatus");
		PoslogStatusInfo poslogStatus = new PoslogStatusInfo();
		try {
			if (consolidation) {
				poslogStatus
						.setConsolidationResult(countUnsendPoslog(companyId, retailStoreId, businessDayDate, "Status"));
			}
			if (transfer) {
				poslogStatus
						.setTransferResult(countUnsendPoslog(companyId, retailStoreId, businessDayDate, columnName));
				poslogStatus.setTransferFailedResult(
						countFailedPoslog(companyId, retailStoreId, businessDayDate, columnName));
				if (poslogStatus.getTransferFailedResult() > 0 || poslogStatus.getTransferResult() > 0) {
					poslogStatus.setUnsendTransactionList(
							getUnsendTransactionList(companyId, retailStoreId, businessDayDate, columnName));
				}
				if (poslogStatus.getTransferFailedResult() > 0) {
					retryFailedPoslog(companyId, retailStoreId, businessDayDate, columnName);
				}
			}
		} finally {
			tp.methodExit(poslogStatus.toString());
		}
		return poslogStatus;
	}

	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param checkColumnName
	 *            Column name to Check
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final int countUnsendPoslog(String companyId, String retailStoreId, String businessDayDate,
			String checkColumnName) throws DaoException {
		tp.methodEnter("countUnsendPoslog");
		int unsendCount = -1;
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dbManager.getConnection();
			statement = prepareStatementCountUnsendPoslog(companyId, retailStoreId, businessDayDate, connection,
					checkColumnName);
			result = statement.executeQuery();
			result.next();
			unsendCount = result.getInt("UnsendCount");
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.countUnsendPoslog()", Logger.RES_EXCEP_SQL,
					"Failed to get unsend poslog count." + sqlEx.getMessage());
			throw new DaoException("SQLException: @countUnsendPoslog ", sqlEx);
		} finally {
			closeConnectionObjects(connection, statement, result);
			tp.methodExit(unsendCount);
		}
		return unsendCount;
	}

	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param checkColumnName
	 *            Column name to Check
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final int countFailedPoslog(String companyId, String retailStoreId, String businessDayDate,
			String checkColumnName) throws DaoException {
		tp.methodEnter("countFailedPoslog");
		int failedCount = -1;

		ResultSet result = null;
		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dbManager.getConnection();
			statement = prepareStatementCountFailedPoslog(companyId, retailStoreId, businessDayDate, connection,
					checkColumnName);
			result = statement.executeQuery();
			result.next();
			failedCount = result.getInt("FailedCount");
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.countUnsendPoslog()", Logger.RES_EXCEP_SQL,
					"Failed to get unsend poslog count." + sqlEx.getMessage());
			throw new DaoException("SQLException: @countUnsendPoslog ", sqlEx);
		} finally {
			closeConnectionObjects(connection, statement, result);
			tp.methodExit(failedCount);
		}
		return failedCount;
	}

	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param checkColumnName
	 *            Column name to Check
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final void retryFailedPoslog(String companyId, String retailStoreId, String businessDayDate,
			String checkColumnName) throws DaoException {
		tp.methodEnter("retryFailedPoslog");

		Connection connection = null;
		PreparedStatement statement = null;
		try {
			connection = dbManager.getConnection();
			statement = prepareStatementRetryFailedPoslog(companyId, retailStoreId, businessDayDate, connection,
					checkColumnName);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.countUnsendPoslog()", Logger.RES_EXCEP_SQL,
					"Failed to get unsend poslog count." + sqlEx.getMessage());
			rollBack(connection, "retryFailedPoslog", sqlEx);
			throw new DaoException("SQLException: @countUnsendPoslog ", sqlEx);
		} finally {
			closeConnectionObjects(connection, statement, null);
			tp.methodExit();
		}
		return;
	}
	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param checkColumnName
	 *            Column name to Check
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
    @SuppressWarnings("null")
    @Override
    public final List<UnsendTransaction> getUnsendTransactionList(String companyId, String retailStoreId,
            String businessDayDate,
			String checkColumnName) throws DaoException {
		tp.methodEnter("getUnsendTransactionList");

		ResultSet result = null;
		Connection connection = null;
		PreparedStatement statement = null;
        List<UnsendTransaction> unsendTransactionList = new ArrayList<UnsendTransaction>();
		try {
			connection = dbManager.getConnection();
			statement = prepareStatementtUnsendTransactionPoslog(companyId, retailStoreId, businessDayDate, connection,
					checkColumnName);
			result = statement.executeQuery();
			while (result.next()) {
				UnsendTransaction unsendTransaction = new UnsendTransaction();
				unsendTransaction.setBusinessDayDate(result.getString("BusinessDayDate"));
				unsendTransaction.setSequenceNumber(result.getString("SequenceNumber"));
				unsendTransaction.setSendStatus(result.getString(checkColumnName));
				unsendTransactionList.add(unsendTransaction);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.getUnsendTransactionList()", Logger.RES_EXCEP_SQL,
					"Failed to get unsend poslog count." + sqlEx.getMessage());
			throw new DaoException("SQLException: @getUnsendTransactionList ", sqlEx);
		} finally {
			closeConnectionObjects(connection, statement, null);
			tp.methodExit();
		}
        return unsendTransactionList;
	}
	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param connection
	 * @param checkColumnName
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStatementCountUnsendPoslog(String companyId, String retailStoreId,
			String businessDayDate, Connection connection, String checkColumnName) throws SQLException {
		// Replace %s to check column name.
		String query = String.format(sqlStatement.getProperty("count-unsend-poslog"), checkColumnName);
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, retailStoreId);
		statement.setString(SQLStatement.PARAM3, businessDayDate);
		return statement;
	}

	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param connection
	 * @param checkColumnName
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStatementCountFailedPoslog(String companyId, String retailStoreId,
			String businessDayDate, Connection connection, String checkColumnName) throws SQLException {
		// Replace %s to check column name.
		String query = String.format(sqlStatement.getProperty("count-failed-poslog"), checkColumnName);
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, retailStoreId);
		statement.setString(SQLStatement.PARAM3, businessDayDate);
		return statement;
	}

	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param connection
	 * @param checkColumnName
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStatementRetryFailedPoslog(String companyId, String retailStoreId,
			String businessDayDate, Connection connection, String checkColumnName) throws SQLException {
		// Replace %s to check column name.
		String query = String.format(sqlStatement.getProperty("retry-failed-poslog"), checkColumnName, checkColumnName);
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, retailStoreId);
		statement.setString(SQLStatement.PARAM3, businessDayDate);
		return statement;
	}

	/**
	 * @param companyId
	 * @param retailStoreId
	 * @param businessDayDate
	 * @param connection
	 * @param checkColumnName
	 * @return
	 * @throws SQLException
	 */
	private PreparedStatement prepareStatementtUnsendTransactionPoslog(String companyId, String retailStoreId,
			String businessDayDate, Connection connection, String checkColumnName) throws SQLException {
		// Replace %s to check column name.
		String query = String.format(sqlStatement.getProperty("get-unsend-and-failed-poslog"), checkColumnName, checkColumnName,checkColumnName);
		PreparedStatement statement = connection.prepareStatement(query);
		statement.setString(SQLStatement.PARAM1, companyId);
		statement.setString(SQLStatement.PARAM2, retailStoreId);
		statement.setString(SQLStatement.PARAM3, businessDayDate);
		return statement;
	}
}
