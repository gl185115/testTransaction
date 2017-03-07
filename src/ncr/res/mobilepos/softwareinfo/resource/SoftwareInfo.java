/*
* Copyright (c) 2011-2012,2015 NCR/JAPAN Corporation SW-R&D
*
* SoftwareInfo
*
* SoftwareInfo Class is a Web Resource which support
* Software Version.
*
* del Rio, Rica Marie M.
*/

package ncr.res.mobilepos.softwareinfo.resource;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.softwareinfo.model.OperatingSystemInfo;
import ncr.res.mobilepos.softwareinfo.model.SoftwareComponents;
import ncr.res.mobilepos.softwareinfo.model.SoftwareVersion;
import ncr.res.mobilepos.softwareinfo.model.JavaFramework;
import ncr.res.mobilepos.softwareinfo.model.TomCatServer;
import ncr.res.mobilepos.softwareinfo.model.WebApiSw;

/**
 * SoftwareInfo class that encapsulates application's software version
 * and software information.
 *
 */
@Path("/softwareinfo")
@Api(value="/softwareinfo", description="ソフトウェア情報API")
public class SoftwareInfo {
    private static final String PROG_NAME = "SofInf";

	/**
     * Application's software version.
     */
    private SoftwareVersion software;

    /**
     * the instance of trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * Servlet Context.
     */
    @Context
    private ServletContext context;

    /**
     * the class instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * Gets the resource servlet context.
     *
     * @return the context
     */
    public final ServletContext getContext() {
        return context;
    }
    /**
     * constructor.
     */
    public SoftwareInfo() {
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
    /**
     * Setting of Servlet Context.
     * @param contextToSet  Servlet Context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Web service for getting the Web API Version of the application software.
     * @return  Web API Software version
     */
    @Path("/service")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="apiのウェブ版を得る", response=SoftwareVersion.class)
    @ApiResponses(value={})
    public final SoftwareVersion getWebAPIVersion() {
        String functionName = "SoftwareInfo.getWebAPIVersion";
		tp.methodEnter(functionName);

        WebApiSw webApiSw = new WebApiSw();
        try {
            webApiSw.setInputStream(context
                    .getResourceAsStream("/META-INF/MANIFEST.MF"));
            webApiSw.loadProperties();

        } catch (IOException e) {
            tp.println("Cannot read file.");
            LOGGER.logAlert(PROG_NAME, 
                    Logger.RES_EXCEP_IO, "Failed to read MANIFEST.MF", e);
        } catch (Exception e) {
            tp.println("MANIFEST.MF file does not exist in the META-INF.");
            LOGGER.logAlert(PROG_NAME, 
                    Logger.RES_EXCEP_GENERAL, 
                    "MANIFEST.MF file does not exist in the META-INF.", e);
        }
        return (SoftwareVersion)tp.methodExit(webApiSw);
    }

    /**
     * Web service for getting the TomCat Version of the application software.
     * @return  TomCat Version
     */
    @Path("/server")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="トムキャットバージョンを取得する", response=SoftwareVersion.class)
    @ApiResponses(value={})
    public final SoftwareVersion getTomCatVersion() {
        tp.methodEnter("getTomCatVersion");

        TomCatServer tomcatVersion = new TomCatServer();
        String serverInfo = context.getServerInfo();
        tomcatVersion.setVersion(serverInfo);

        return (SoftwareVersion)tp.methodExit(tomcatVersion);
    }

    /**
     * Web service for getting the Java Version of the application software.
     * @return  Java version
     */
    @Path("/java")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="javaのバージョンを取得する", response=SoftwareVersion.class)
    @ApiResponses(value={})
    public final SoftwareVersion getJavaVersion() {
      tp.methodEnter("getJavaVersion");

        JavaFramework javaVersion = new JavaFramework();
        Properties properties = System.getProperties();

        javaVersion.setVersion(properties.getProperty("java.version"));

        return (SoftwareVersion)tp.methodExit(javaVersion);
    }
    
    /**
     * Web service for getting the Operating System of the application software.
     * @return  OperatingSystemInfo
     */
	@Path("/operatingsystem")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value="オペレーティングシステム情報を取得", response=OperatingSystemInfo.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
	public final OperatingSystemInfo getOperatingSystemInfo() {
		tp.methodEnter("getOperatingSystemInfo");
		OperatingSystemInfo operatingSystemInfo = null;
		try {
			operatingSystemInfo = new OperatingSystemInfo();
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME,
                  Logger.RES_EXCEP_GENERAL, "getOperatingSystemInfo", e);
			operatingSystemInfo
					.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			operatingSystemInfo.setMessage(e.getMessage());
        }
        return (OperatingSystemInfo)tp.methodExit(operatingSystemInfo);
	}
    
    /**
     * Web service for getting all Server components version of the application software.
     * @return  SoftwareComponents
     */
    @Path("/getallsoftwareinfo")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value="全ソフトウェア情報取得", response=SoftwareComponents.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
        })
    public final SoftwareComponents getAllSoftwareInfo() {
    	tp.methodEnter("getAllSoftwareInfo");
    	
		SoftwareComponents softwareComponents = new SoftwareComponents();
		try {
			softwareComponents.setServiceVersionInfo(this.getWebAPIVersion());
			softwareComponents.setServerVersionInfo(this.getTomCatVersion());
			softwareComponents.setJavaVersionInfo(this.getJavaVersion());
			softwareComponents.setOperatingSystemVersionInfo(this
					.getOperatingSystemInfo());
		} catch (Exception e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                            "getAllSoftwareInfo", e);
			softwareComponents.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			softwareComponents.setMessage(e.getMessage());
		}
        return (SoftwareComponents)tp.methodExit(softwareComponents);
    }
}
