/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 * 
 * ServerTableResource
 * 
 * Resource which provides a list of AP Server for checking WebApp connection.
 * 
 */
package ncr.res.mobilepos.servertable.resource;

import java.sql.SQLException;
import java.util.List;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.servertable.dao.IServerTableDAO;
import ncr.res.mobilepos.servertable.model.ServerTable;
import ncr.res.mobilepos.servertable.model.ServerTables;

/**
 * ServerTableResource Web Resource Class. 
 * This gets server table (Tomcat or IIS web application request url).
 */
@Path("/servertable")
@Api(value = "/servertable", description = "サーバーテーブルAPI")
public class ServerTableResource {
	/**
	 * The program name.
	 */
	private static final String PROG_NAME = "SrvrTbl";
	/**
	 * the instance of the logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();

	/**
	 * Default Constructor for ServerTableResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public ServerTableResource() {
	}

	/**
	 * This method get the list of AP Server with request url.
	 * 
	 * @return ServerTables
	 */
	@Path("/get")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "ServerTables", response = ServerTables.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_OK, message="Success"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
	public final ServerTables getServerTables() {
		String functionName = DebugLogger.getCurrentMethodName();
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread()
				.getId(), getClass());
		if (tp != null) {
			tp.methodEnter(functionName);
		}
		ServerTables serverTables = new ServerTables();
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		try {
			IServerTableDAO serverDAO = daoFactory.getServerTableDAO();
			List<ServerTable> servers = serverDAO.getServerTables();
			if (!servers.isEmpty()) {
				serverTables.setServerTables(servers);
			}
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
					"Failed to get servertable:" + ex.getMessage());
			if (ex.getCause() instanceof SQLException) {
				serverTables.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			} else {
				serverTables.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
			}
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get servertable:" + ex.getMessage());
			serverTables.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(serverTables);
		}
		return serverTables;
	}
}
