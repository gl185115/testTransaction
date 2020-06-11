/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerAppServerDAO
 *
 * DAO which handles information of AP Server
 *
 */
package ncr.res.mobilepos.appserver.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.appserver.model.AppServer;
import ncr.res.mobilepos.appserver.model.AppServerTypes;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for AppServer.
 *
 * @see IAppServerDAO
 */
public class SQLServerAppServerDAO extends AbstractDao implements IAppServerDAO {

	private static final String PROG_NAME = "ServrDAO";

	/** The database manager. */
	private DBManager dbManager;

	/** A private member variable used for logging the class implementations. */
	private static final Logger LOGGER = (Logger) Logger.getInstance();

	/** The instance of the trace debug printer. */
	private Trace.Printer tp = null;

	/**
	 * The Constructor of the Class.
	 *
	 * @throws DaoException
	 *             thrown when process fails.
	 */
	public SQLServerAppServerDAO() throws DaoException {
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
	 * Gets the list of web application server (IIS or Tomcat) with request Url.
	 */
	@Override
	public List<AppServer> getAppServers() throws DaoException {
		String functionName = "getAppServers";
		tp.methodEnter(functionName);
		List<AppServer> servers = new ArrayList<AppServer>();
		try (Connection connection = dbManager.getConnection();
				PreparedStatement selectStmnt = connection
						.prepareStatement(SQLStatement.getInstance()
								.getProperty("get-server-list"));
				ResultSet resultSet = selectStmnt.executeQuery()) {
			while (resultSet.next()) {
				AppServer appServer = new AppServer();
				appServer.setName(resultSet.getString("Name"));
				appServer.setType(resultSet.getString("Type"));
				if (appServer.getType().equalsIgnoreCase(
						AppServerTypes.RESTRANSACTION)) {
					appServer.setUrl(resultSet.getString("Url"));
				} else if (appServer.getType().equalsIgnoreCase(
						AppServerTypes.WOAPI)) {
					appServer.setIisUrl(resultSet.getString("Url"));
				}
				servers.add(appServer);
			}
		} catch (SQLException sqlEx) {
			LOGGER.logAlert(
					PROG_NAME,
					functionName,
					Logger.RES_EXCEP_SQL,
					"Failed to get the list of appserver.\n"
							+ sqlEx.getMessage());
			throw new DaoException("SQLException: @getAppServers ", sqlEx);
		} finally {
			tp.methodExit(servers);
		}
		return servers;
	}
}