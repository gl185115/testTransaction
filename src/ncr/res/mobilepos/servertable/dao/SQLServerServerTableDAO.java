/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerServerTableDAO
 *
 * DAO which handles information of AP Server
 *
 */
package ncr.res.mobilepos.servertable.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.servertable.model.APServerTypes;
import ncr.res.mobilepos.servertable.model.ServerTable;

/**
 * A Data Access Object implementation for ServerTable.
 *
 * @see IServerTableDAO
 */
public class SQLServerServerTableDAO extends AbstractDao implements IServerTableDAO {

    private static final String PROG_NAME = "ServrDAO";

    /** The database manager. */
    private DBManager dbManager;

    /** The prepared statement. */
    private PreparedStatement prepdStatement = null;

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
    public SQLServerServerTableDAO() throws DaoException {
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
     * Gets the list of web application server (IIS or Tomcat) with request Url.
     */
    @Override
    public List<ServerTable> getServerTables() throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        List<ServerTable> servers = new ArrayList<ServerTable>();
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-servertables"));
            resultSet = selectStmnt.executeQuery();
            while (resultSet.next()) {
                ServerTable serverTable = new ServerTable();
                serverTable.setName(resultSet.getString("Name"));
                serverTable.setType(resultSet.getString("Type"));
                if(serverTable.getType().equalsIgnoreCase(APServerTypes.RESTRANSACTION)){
                	serverTable.setUrl(resultSet.getString("Url"));
                }else if(serverTable.getType().equalsIgnoreCase(APServerTypes.WOAPI)){
                	serverTable.setIisUrl(resultSet.getString("Url"));
                }
                servers.add(serverTable);
            }
         } catch (Exception e) {
             LOGGER.logAlert(PROG_NAME,
                     functionName,
                     Logger.RES_EXCEP_GENERAL,
                     "Failed to get servertables.\n"
                              + e.getMessage());
             throw new DaoException("Exception: @"
                     + "SqlServerServerTableDAO:" + functionName
                     + " - Failed to get servertables.", e);
         } finally {
             closeConnectionObjects(connection, prepdStatement, resultSet);
             tp.methodExit(servers);
         }
        return servers;
    }

}
