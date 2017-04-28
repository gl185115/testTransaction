package ncr.res.mobilepos.poslogstatus.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPoslogStatusDAO extends AbstractDao implements IPoslogStatusDAO {

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
	}

	/**
	 * check poslog status and return count of poslog not deal with.
	 * 
	 * @param consolidation
	 * @param transfer
	 * @return PoslogStatusInfo.
	 * @throws DaoException
	 *             - if database fails.
	 */
	@Override
	public final PoslogStatusInfo checkPoslogStatus(boolean consolidation, boolean transfer)
			throws DaoException {
		tp.methodEnter("checkPoslogStatus");

		PoslogStatusInfo poslogStatus = new PoslogStatusInfo();
		String statusTemp = EnvironmentEntries.getInstance().getPoslogTransferStatusColumn();
		
		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;

		try {
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement.getProperty("poslog-status-check"));
			select.setString(SQLStatement.PARAM1, statusTemp);

			result = select.executeQuery();
			if (result.next()) {
				if (consolidation){
					poslogStatus.setConsolidationResult(result.getLong("C_NotExcuteCount"));
				}
				if (transfer){
					poslogStatus.setTransferResult(result.getLong("T_NotExcuteCount"));
				}
			}

		} catch (SQLException sqlEx) {
			LOGGER.logAlert(progName, "SQLServerPoslogStatusDAO.checkPoslogStatus()", Logger.RES_EXCEP_SQL,
					"Failed to get the PoslogStatus.\n" + sqlEx.getMessage());
			throw new DaoException("SQLException: @checkPoslogStatus ", sqlEx);
		} finally {
			closeConnectionObjects(connection, select, result);

			tp.methodExit(poslogStatus.toString());
		}
		return poslogStatus;
	}
}
