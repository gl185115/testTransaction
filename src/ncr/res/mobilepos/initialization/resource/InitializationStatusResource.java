/*
 * Copyright (c) 2016 NCR/JAPAN Corporation SW-R&D
 * InitializationStatusResource
 * Resource which provides resTransaction is up and going through proper initialization.
 * This checks if all the configure and loggers are properly initialized during tomcat startup.
 */
package ncr.res.mobilepos.initialization.resource;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.model.ResultBase;

/**
 * InitializationStatus Web Resource Class.
 * This checks if resTransation is properly initialized.
 * Actual initialization is done by WebContextListener.
 */
@Path("/initialization")
public class InitializationStatusResource {

    /**
     * The program name.
     */
    private static final String PROG_NAME = "InitializationStatus";

    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public InitializationStatusResource() {
    }

    /**
     * <P>Init method of InitializationStatus servlet.<br/>
     * This invokes after the constructor was called.<br/>
     * This initializes Spm File used in journalize method.
     */
    @PostConstruct
    public void init() {

    }

    /**
     * This method checks if resTransaction is properly initialized during tomcat startup.
     * @return OK
     */
    @Path("/status")
    @GET
    @Produces({MediaType.APPLICATION_XML + ";charset=UTF-8"})
    @ApiOperation(value="resTransactionèâä˙âªèÛë‘éÊìæ", response=String.class)
    @ApiResponses(value={
            @ApiResponse(code=ResultBase.RES_OK_INITIALIZATION, message="èâä˙âªê¨å˜"),
            @ApiResponse(code=ResultBase.RES_ERROR_INITIALIZATION, message="èâä˙âªé∏îs"),
            @ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="îƒópÉGÉâÅ["),
    })
    public final ResultBase getInitializationStatus() {

        String functionName = DebugLogger.getCurrentMethodName();
        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        if(tp != null) {
            tp.methodEnter(functionName).println("POSLog xml");
        }
        ResultBase result = new ResultBase();

        // Failed to read parameters from web.xml.
        boolean initFailed = false;
        initFailed |= EnvironmentEntries.getInstance() == null;
        initFailed |= Logger.getInstance() == null;
        initFailed |= SnapLogger.getInstance() == null;
        initFailed |= DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass()) == null;
        initFailed |= SpmFileWriter.getInstance() == null;

        if(initFailed) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_INITIALIZATION);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INITIALIZATION);
            result.setMessage(ResultBase.RES_FAILED_MSG);
        } else {
            result.setNCRWSSResultCode(ResultBase.RES_OK_INITIALIZATION);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_OK_INITIALIZATION);
            result.setMessage(ResultBase.RES_SUCCESS_MSG);
        }
        if(tp != null) {
            tp.methodExit(result);
        }
        return result;
    }

}
