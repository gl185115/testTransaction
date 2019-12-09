/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 * 
 * AppServerResource
 * 
 * Resource which provides a list of Application Server for checking WebApp connection.
 * 
 */
package ncr.res.mobilepos.appserver.resource;

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
import ncr.res.mobilepos.appserver.dao.IAppServerDAO;
import ncr.res.mobilepos.appserver.model.AppServer;
import ncr.res.mobilepos.appserver.model.AppServers;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * AppServerResource Web Resource Class. 
 * This gets application servers (Tomcat or IIS web application request url).
 */
@Path("/servertable")
@Api(value = "/servertable", description = "通信状況照会の為のサーバー（TOMCAT/IIS等の）リスト取得API")
public class AppServerResource {
	/**
	 * The program name.
	 */
	private static final String PROG_NAME = "SrvrTbl";
	/**
	 * the instance of the logger.
	 */
	private static final Logger LOGGER = (Logger) Logger.getInstance();

	/**
	 * Default Constructor for AppServerResource.
	 *
	 * <P>
	 * Initializes the logger object.
	 */
	public AppServerResource() {
	}

	/**
	 * This method get the list of AP Server with request url.
	 * 
	 * @return AppServers
	 */
	@Path("/get")
	@GET
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "サーバー(TOMCAT/IIS等の)リスト", response = AppServers.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_OK, message="Success"),
            @ApiResponse(code=ResultBase.RES_ERROR_DB, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="データベースエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
	public final AppServers getAppServers() {
		String functionName = DebugLogger.getCurrentMethodName();
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread()
				.getId(), getClass());
		if (tp != null) {
			tp.methodEnter(functionName);
		}
		AppServers appServers = new AppServers();
		DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
		try {
			IAppServerDAO serverDAO = daoFactory.getAppServerDAO();
			List<AppServer> servers = serverDAO.getAppServers();
			if (!servers.isEmpty()) {
				appServers.setAppServers(servers);
			}
		} catch (DaoException ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
					"Failed to get app servers:" + ex.getMessage());
			appServers.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
		} catch (Exception ex) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get app servers:" + ex.getMessage());
			appServers.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
		} finally {
			tp.methodExit(appServers);
		}
		return appServers;
	}
}
