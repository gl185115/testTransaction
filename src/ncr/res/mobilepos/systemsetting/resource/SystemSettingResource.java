package ncr.res.mobilepos.systemsetting.resource;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.property.WebContextListener;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.model.DateTime;
import ncr.res.mobilepos.systemsetting.model.SystemSetting;
import ncr.res.mobilepos.systemsetting.model.TerminalInfo;

/**
 * SystemSettingResource class is a web resource which provides support
 * for System Setting.
 */
@Path("/SystemSettings")
@Api(value="/SystemSettings", description="システムの設定API")
public class SystemSettingResource {

    /**
     * ServletContext.
     */
    @Context
    private ServletContext context;

    /**
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }

    /**
     * @param contextToSet
     *            the context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }
    
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * DAO Factory for system setting.
     */
    private DAOFactory daoFactory;
    /**
     * Program name.
     */
    private String PROG_NAME = "SysSet";

    /**
     * DAO Factory for System Setting.
     */
    public SystemSettingResource() {
        this.daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }    

    /**
     * A Web Method Call used to get the System's Date Settings.
     *
     * @return The JSON object represenation of the Date Settings
     */
    @POST
    @Path("/DateSettings/get")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="日付設定取得", response=SystemSetting.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RESSYS_ERROR_NO_SETTINGS_FOUND, message="システム設定データが見つからない"),
            @ApiResponse(code=ResultBase.RES_ERROR_DAO, message="DAOエラー"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final SystemSetting getDateSetting(
    		@ApiParam(name="companyid", value="会社コード")@FormParam("companyid") final String companyId,
    		@ApiParam(name="storeid", value="店舗番号")@FormParam("storeid") final String storeId) {
        String functionname = "SystemSettingResource.getDateSetting";
        DateSetting dateSetting;
        SystemSetting systemSetting = new SystemSetting();

        tp.methodEnter("getDateSetting")
            .println("companyid", companyId)
            .println("storeid", storeId);

        try {
            String qStoreId = StringUtility.isNullOrEmpty(storeId) ? "0" : storeId;

            ISystemSettingDAO systemSetDao = daoFactory.getSystemSettingDAO();

            dateSetting = systemSetDao.getDateSetting(companyId, qStoreId);
            if (dateSetting == null) {
                if (!"0".equals(qStoreId)) {
                    qStoreId = "0";
                    dateSetting = systemSetDao.getDateSetting(companyId, qStoreId);
                }
            }

            //Are there date being reetrieved?
            //If yes, set the DateSetting.
            if (null != dateSetting) {
                systemSetting.setDateSetting(dateSetting);
            } else {
                systemSetting.setNCRWSSResultCode(
                        ResultBase.RESSYS_ERROR_NO_SETTINGS_FOUND);
                tp.println("No settings found.");
            }
        } catch (DaoException e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_DAO,
                    "Failed to get the System Settings: \n" + e.getMessage());
            systemSetting.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the System Settings: \n" + e.getMessage());
            systemSetting.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(systemSetting.toString());
        }

        return systemSetting;
    }
    
    /**
     * Web Method Call used to get the Server Machine's current Date
     * and Time.
     *
     * @return DateTime
     */
    @GET
    @Path("/DateSettings/getcurrentdatetime")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="現在日時取得", response=DateTime.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final DateTime getCurrentDateTime() {
        String functionname = "SystemSettingResource.getCurrentDateTime";
        tp.methodEnter("getCurrentDateTime");
        DateTime dateTime = new DateTime();

        try {
            String today = DateFormatUtility.
                               getCurrentDateTimeFormatted("yyyyMMddHHmmss");

            if (null != today && !today.isEmpty()) {
                dateTime.setCurrentDateTime(today);
                dateTime.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
                dateTime.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                tp.println("Unabled to retrieve current date and time.");
            }
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the server's current date and time. \n"
                               + e.getMessage());
            dateTime.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
        } finally {
            tp.methodExit(dateTime.toString());
        }
        return dateTime;
    }
    
    /**
     * Ping check connection to IpAddress.
     * @param ipAddress IP to ping
     * @return ResultBase
     */
	@POST
	@Path("/ping")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "PingIPAddress", response = ResultBase.class)
	@ApiResponses(value = {
			@ApiResponse(code = ResultBase.RES_OK, message = "IPAddress is reachable"),
			@ApiResponse(code = ResultBase.RES_ERROR_PING, message = "IpAddress is not reachable"),
			@ApiResponse(code = ResultBase.RES_ERROR_IOEXCEPTION, message = "Network error"),
			@ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message="汎用エラー")})
	public final ResultBase ping(
			@ApiParam(name = "ipaddress", value = "ipaddress") @FormParam("ipaddress") final String ipAddress) {
		String functionName = DebugLogger.getCurrentMethodName();
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread()
				.getId(), getClass());
		if (tp != null) {
			tp.methodEnter(functionName);
		}
		ResultBase result = new ResultBase();
		Process pingProc = null;
		try {
		    int pingTimeout = 3000;
		    if (GlobalConstant.getServerPingTimeout() > 0) {
		        pingTimeout = GlobalConstant.getServerPingTimeout();
		    }
		    // 送信バイト数　9999バイト
		    String cmd = "ping -n 1 -l 9999 -w " + pingTimeout + " " + ipAddress;
		    
		    pingProc = Runtime.getRuntime().exec(cmd);
		    pingProc.waitFor();
		    
		    BufferedReader in = new BufferedReader(new InputStreamReader(pingProc.getInputStream()));
            String inputLine = "";
            String tmp;
            while ((tmp = in.readLine()) != null) {
                inputLine += tmp;
            }
		    
		    if (pingProc.exitValue() == 0 && inputLine.contains("=9999")){//応答バイト数　9999バイト
		        result.setNCRWSSResultCode(ResultBase.RES_OK);
		    } else {
		        result.setNCRWSSResultCode(ResultBase.RES_ERROR_PING);
                result.setMessage(ipAddress + " ping failed.");
		    }
		    
		    if (pingProc != null) {
		        pingProc.destroy();
		    }
		}
		catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to ping ipaddress.\n" + e.getMessage());
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
		    if (pingProc != null) {
                pingProc.destroy();
            }
			tp.methodExit(result);
		}
		return result;
	}
	
    /**
     * get mex host terminal info
     * @return TerminalInfo
     */
	@GET
	@Path("/getMeXHostTerminalInfo")
	@Produces({ MediaType.APPLICATION_JSON })
	@ApiOperation(value = "MeXHostTerminalInfo", response = TerminalInfo.class)
	@ApiResponses(value = {
			@ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message="汎用エラー")})
	public final TerminalInfo getMeXHostTerminalInfo() {
		String functionName = DebugLogger.getCurrentMethodName();
		Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread()
				.getId(), getClass());
		if (tp != null) {
			tp.methodEnter(functionName);
		}
		TerminalInfo terminalInfo = new TerminalInfo();
		SystemFileConfig systemFileConfig = SystemFileConfig.getInstance();
		try {
			String companyId = systemFileConfig.getCompanyId();
			String storeId = systemFileConfig.getStoreId();
			String terminalId = systemFileConfig.getTerminalId();
			
			terminalInfo.setCompanyId(companyId);
			terminalInfo.setStoreId(storeId);
			terminalInfo.setTerminalId(terminalId);
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
					"Failed to get mex host terminalInfo\n" + e.getMessage());
			terminalInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			terminalInfo.setMessage(e.getMessage());
		} finally {
			tp.methodExit(terminalInfo);
		}
		return terminalInfo;
	}
	
    /**
     * get reloadMasterTables
     * @return ResultBase
     */
    @GET
    @Path("/ReloadMasterTables")
    @Produces({MediaType.APPLICATION_JSON })
    @ApiOperation(value="メモリー再展開チェック", response=DateTime.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final ResultBase reloadMasterTables() {
        String functionname = "SystemSettingResource.reloadMasterTables";
        tp.methodEnter("reloadMasterTables");
        ResultBase result = new ResultBase();
        WebContextListener listener = new WebContextListener();
        String logName = "";
        try {
        	logName = "initializeEnvironmentVariables";
            listener.initializeEnvironmentVariables();
            logName = "initializeLoggers";
            listener.initializeLoggers();
            logName = "initializeDBInstances";
            listener.initializeDBInstances();
            logName = "preloadDBRecord";
            listener.preloadDBRecord();
            logName = "initializeBusinessLogicFactories";
            listener.initializeBusinessLogicFactories();

            result.setNCRWSSResultCode(ResultBase.RES_OK);
            result.setMessage("Success");
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionname, Logger.RES_EXCEP_GENERAL,
            		"\n" + logName 
            		+ " error has happend.\nFailed to get the Master table data. \n"
                    + e.getMessage());
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(logName + " error has happend. " + e.getMessage());
        } finally {
            tp.methodExit(result.toString());
        }
        return result;
    }
}
