/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* SystemConfigurationResource
*
* Web Resource which support MobilePOS System Configuration properties.
*
* Campos, Carlos  (cc185102)
*/

package ncr.res.mobilepos.systemconfiguration.resource;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;

import javax.servlet.ServletContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * SystemConfigurationResource Class is a Web Resource which support.
 * MobilePOS System Configuration properties.
 *
 */
@Path("/systemconfiguration")
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
    /** The DaoFactory. */
    private DAOFactory dao;
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
       dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
       tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
               getClass());
    }

    /**
     * A Web Method call to get the Global Variables for
     * the Web Server.7
     * @return System configuration for web servers
     */
    /**
     * API to returns all the SytemConfiguration variables.
     * @return SystemConfiguration
     */
    @SuppressWarnings("unchecked")
    @GET
    @Produces ({MediaType.APPLICATION_JSON })
    @Path("")
    public final Map<String, Object> getSystemConfiguration() {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        // Maps to return.
        Map<String, Object> returnMap = new HashMap<String,Object>();
        Map<String, String> configUrl = new HashMap<String, String>();

        // Checks if SystemConfiguration exists on ServletContext.
        Map<String, String> systemConfigMap = GlobalConstant.getSystemConfig();
        if(systemConfigMap == null) {
            returnMap.put("NCRWSSResultCode", ResultBase.RES_ERROR_NODATAFOUND);
            returnMap.put("NCRWSSExtendedResultCode", ResultBase.RES_ERROR_NODATAFOUND);
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "No SystemConfiguration found in ServletContext" );
            tp.methodExit(returnMap);
            return returnMap;
        }

        // Moves SystemConfigurations to Return Map.
        for(Entry<String, String> entry : systemConfigMap.entrySet()) {
            String key = entry.getKey();
            String attributeValue = StringUtility.convNullStringToNull(entry.getValue());
            if(isInUrlConfig(key)) {
                // Puts the entry to ConfigUrl map.
                configUrl.put(key, attributeValue);
            } else {
                returnMap.put(key, attributeValue);
            }
        }
        // Puts ConfigUrl map to return.
        returnMap.put("Url",  configUrl);

        // Adds result codes as successful return.
        returnMap.put("NCRWSSResultCode", ResultBase.RES_OK);
        returnMap.put("NCRWSSExtendedResultCode", ResultBase.RES_OK);
        return returnMap;
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