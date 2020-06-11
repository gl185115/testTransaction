/*
* Copyright (c) 2011-2019 NCR/JAPAN Corporation SW-R&D
*
* SystemConfigurationResource
*
* Web Resource which support MobilePOS System Configuration properties.
*
* Campos, Carlos  (cc185102)
*/

package ncr.res.mobilepos.systemconfiguration.resource;

import java.util.List;
import java.util.Map;

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
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.model.SystemConfigInfo;
import ncr.res.mobilepos.systemconfiguration.model.SystemParameter;

/**
 * SystemConfigurationResource Class is a Web Resource which support.
 * MobilePOS System Configuration properties.
 *
 */
@Path("/systemconfiguration")
@Api(value="/SystemConfiguration", description="システム設定取得")
public class SystemConfigurationResource {
    /**
     * Servlet Context.
     */
    @Context
    private ServletContext context;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The Trace Printer for Debug. */
    private Trace.Printer tp;
    /**
     * Abbreviation program name of the class.
     */
    private static final String PROG_NAME = "SysCnfg";
    /**
     * The constant value for a field with a private access modifier.
     */
    private static final int PRIVATE_FIELD_CONSTANT_VALUE = 2;
    /**
     * Setting of Servlet Context.
     * @param contextToSet      Servlet Context to set
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * SystemConfigurationResource default
     * constructor.
     */
    public SystemConfigurationResource() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
               getClass());
    }


    /**
     * Get Category, KeyId, Value From PRM_SYSTEM_CONFIG
     * @return SystemConfiguration
     */
    @SuppressWarnings("unchecked")
    @GET
    @Path("/getsystemconfig")
    @Produces ({MediaType.APPLICATION_JSON })
    @ApiOperation(value="システムパラメータ取得", response=Map.class)
    @ApiResponses(value={
    		@ApiResponse(code=ResultBase.RES_ERROR_NODATAFOUND, message="データ未検出"),
        })
    public final SystemParameter getSystemConfigs(){
    	String functionName = "getSystemConfigs";
        tp.methodEnter(functionName);
        SystemParameter systemParameter = new SystemParameter();
        List<SystemConfigInfo> listSystemConfigInfos = GlobalConstant.getSystemConfigInfoList();

        if(listSystemConfigInfos.isEmpty()){
        	systemParameter.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_SYSTEM_CONFIG_NOFOUND);
        	systemParameter.setNCRWSSResultCode(ResultBase.RES_ERROR_SYSTEM_CONFIG_NOFOUND);
        	LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "System Config Informations not get" );
            tp.methodExit(systemParameter);
        }else{
        	for(SystemConfigInfo sysConfigInfo:listSystemConfigInfos){
        		if(StringUtility.isNullOrEmpty(sysConfigInfo.getValue())){
        			sysConfigInfo.setValue("");
        		}
        	}
        	systemParameter.setSystemparameter(listSystemConfigInfos);
        }
        return systemParameter;
    }

    /**
     * Checks if a key of SystemConfiguration should be included in UrlConfig.
     * This exists to maintain protocol compatibility between UI.
     * @param key a key of ServletContext to check.
     * @return true: key is included in UrlConfig. false otherwise.
     */
    public static boolean isInUrlConfig(String key) {
        switch(key) {
            case "EnterpriseServerUri":
                return true;
            default:
                return false;
        }
    };

}