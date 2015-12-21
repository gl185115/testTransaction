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

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.model.WebServerGlobals;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;
import ncr.res.mobilepos.systemconfiguration.model.MemberServer;
import ncr.res.mobilepos.systemconfiguration.model.UrlConfig;

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
    @SuppressWarnings("unchecked")
	@GET
    @Produces ({MediaType.APPLICATION_JSON })
    @Path("")
    public final WebServerGlobals getSystemConfiguration() {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter(functionName);

        WebServerGlobals globals = new WebServerGlobals();
        Class<?> fieldType  = null;
    	String fieldName = null;
    	String attributeValue = null;

        //Set the System configuration for Web Server's Global variable
        try {  	
        	for (Field f: WebServerGlobals.class.getDeclaredFields()) {
        		f.setAccessible(true);
        		fieldType = f.getType();
        		fieldName = f.getName();
        		attributeValue = String.valueOf(context.getAttribute(StringUtility.toUpperCamel(fieldName)));
        		attributeValue = StringUtility.convNullStringToNull(attributeValue);
        		
        		if (f.getModifiers() == PRIVATE_FIELD_CONSTANT_VALUE) {
        			if (fieldType.equals(String.class)) {
        				if (!StringUtility.isNullOrEmpty(attributeValue)) {
        					f.set(globals, attributeValue);
                    	} 
                    } else if (fieldType.equals(boolean.class)) {
                    	if (!StringUtility.isNullOrEmpty(attributeValue)) {
                    		f.set(globals, Boolean.parseBoolean(attributeValue));
                    	}
                    } else if (fieldType.equals(int.class)) {
                    	if (!StringUtility.isNullOrEmpty(attributeValue)) {
                    		f.set(globals, Integer.parseInt(attributeValue));
                    	}
                    }
        		}     			
        	}     	
        	globals.setMemberInfoConfig(createMemberServerInfo());
        	
            UrlConfig urlConfig = new UrlConfig();
            for (Field f: UrlConfig.class.getDeclaredFields()) {
        		f.setAccessible(true);
        		f.set(urlConfig, String.valueOf(context.getAttribute(StringUtility.toUpperCamel(f.getName()))));
        	}     	     
            globals.setUrlConfig(urlConfig);
        } catch (Exception e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to get system configurations.", e);
	    } finally {
            tp.methodExit(globals.toString());
        }
        return globals;
    }
    
	/**
	 * Parse KeyId's value to Integer.
	 * 
	 * @param keyId
	 * @return int
	 * @throws NumberFormatException
	 *             if keyvalue is null.
	 */
	private int parseInt(final String keyId)  {
		int keyValue = 0;
		String contextValue = String.valueOf(context.getAttribute(keyId));
		try {
			keyValue = Integer.parseInt(contextValue);
		} catch (NumberFormatException e) {
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE,
					"@parseInt: Failed to parse (" + contextValue + ") "
							+ keyId + " KeyId.", e);
		} finally {
			//do nothing
		}
		return keyValue;
	}

    /**
     * A Web Method Call to Set the Web Store Server designated Company ID.
     * @param corpid The Web Store Server Company ID
     * @return The ResultBase telling if the Setting
     *              of Company ID passed or fail.
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/setcompanyid")
    public final ResultBase setStoreCompanyID(
            @FormParam("corpid") final String corpid) {
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("corpid", corpid);

        ResultBase resultBase = new ResultBase();

        //Are there value for Company ID already?
        //If yes, don't set the Company ID anymore.
        if (0 < GlobalConstant.getCorpid().length()) {
            tp.println("New CorpId with value " + corpid + " has not been set.")
              .println("A CorpId with value " + GlobalConstant.getCorpid()
                      + " has been set already.");
            tp.methodExit(resultBase.toString());
            return resultBase;
        }

        try {

            String corpID = corpid;
            //Dont allow CorpID of NULL
            if (null == corpID) {
                tp.println("CorpID is invalid.");
                throw new Exception("Company ID being set is to NULL");
            }

            corpID = corpID.trim();

            //Dont allow CorpID of spaces
            if (0 == corpID.length()) {
                tp.println("CorpID is invalid.");
                throw new Exception("Company ID being set is to EMPTY String");
            }

            //Is the Company ID contains Non Numeric Character
            if (!StringUtility.isNumberFormatted(corpid)) {
                tp.println("CorpID must be Numeric.");
                throw
                    new Exception("Company ID being set is with"
                            + " Non Numeric String as " + corpID);
            }
            SQLServerSystemConfigDAO systemDao = dao.getSystemConfigDAO();

            GlobalConstant.setCorpid(corpid);

            //Add the Company ID in the System configuration
            //Category is fixed as "Store"
            //Description is Fixed
            systemDao.addParameterString(corpid, "Store",
                    GlobalConstant.COMPANY_ID, "The Store Company ID");
        } catch (Exception e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set companyid.", e);
			resultBase.setNCRWSSResultCode(ResultBase.RES_ERROR_SETCORPIDFAIL);
			resultBase = new ResultBase(ResultBase.RES_ERROR_SETCORPIDFAIL,
					ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }

    /**
     * A Web Method Call to set the search limit result.
     * @param systemconfigurationJson The Search Limit.
     *              If limit is lesser than 1,
     *              the default value of 5 is set as Search Limit instead.
     * @return The ResultBase telling if the Setting
     *              of Search Limit passed or fail.
     */
    @POST
    @Produces({MediaType.APPLICATION_JSON })
    @Path("/maintenance")
	public final ResultBase setSystemConfiguration(
			@FormParam("systemconfiguration") final String systemconfigurationJson) {
    	String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName).println("systemconfiguration",
				systemconfigurationJson);

        ResultBase resultBase = new ResultBase();
        try {
            //Is the limit invalid?
            //If yes, Do not set the Search Result limit.
            if (StringUtility.isNullOrEmpty(systemconfigurationJson)) {
                resultBase.setNCRWSSResultCode(
                        ResultBase.RES_ERROR_INVALIDPARAMETER);
                return resultBase;
            }

           JsonMarshaller<WebServerGlobals> webServerGlobalMarshaller =
               new JsonMarshaller<WebServerGlobals>();

           WebServerGlobals newGlobals =
               webServerGlobalMarshaller.unMarshall(systemconfigurationJson,
                       WebServerGlobals.class);

           SQLServerSystemConfigDAO systemConfigDAO = dao.getSystemConfigDAO();
           Map<String, String> systemParamsUpdate
               = systemConfigDAO.getSystemParameters();

           Class<WebServerGlobals> webServerReflection = WebServerGlobals.class;
           //Update the database.
           for (Entry<String, String> param : systemParamsUpdate.entrySet()) {
               Field field = null;
               String key = param.getKey();
               try {
                   field = webServerReflection.getDeclaredField(
                           StringUtility.toCamelCase(param.getKey()));
                   field.setAccessible(true);
               } catch (NoSuchFieldException ex) {
                   continue;
               }

               //Only update system parameter with values of String or Integer
               if (field.getType().equals(List.class)) {
                   break;
               } else if (field.get(newGlobals) == null) {
                   continue;
               } else if (field.getType().equals(int.class)
                       && (Integer) field.get(newGlobals) == -1) {
                   continue;
               } else if (field.getType().equals(boolean.class)
                   && !systemconfigurationJson.contains(key)) {
                   continue;
               }

               param.setValue(String.valueOf(field.get(newGlobals)));
           }

           //update the System Parameter(s)
           int result =
               systemConfigDAO.setParameterValue(systemParamsUpdate);

           //Is the setting of the new parameter(s)
           //in the database are successful?
           //If yes, it is necessary to reset
           //Global Variables in the Servlet context.
           if (result > SQLResultsConstants.NO_ROW_AFFECTED) {
               for (String key : systemParamsUpdate.keySet()) {
                   Field field = null;
                   try {
                       field = webServerReflection.getDeclaredField(
                    		   StringUtility.toCamelCase(key));
                       field.setAccessible(true);
                   } catch (NoSuchFieldException ex) {
                       continue;
                   }

                 //Only update system parameter with values of String or Integer
                   if (field.getType().equals(List.class)) {
                       break;
                   } else if (field.get(newGlobals) == null) {
                       continue;
                   } else if (field.getType().equals(int.class)
                           && (Integer) field.get(newGlobals) < 0) {
                       continue;
                   } else if (field.getType().equals(boolean.class)
                       && !systemconfigurationJson.contains(key)) {
                       continue;
                   }

                   context.setAttribute(key, field.get(newGlobals));
               }
               GlobalConstant.reset(newGlobals);
           } else {
               resultBase.setNCRWSSResultCode(
                       ResultBase.RES_ERROR_SETSYSTEMCONFIGFAIL);
           }
        } catch (DaoException exDao) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to set system configurations.",
					exDao);
			resultBase = new ResultBase(
					ResultBase.RES_ERROR_SETSYSTEMCONFIGFAIL,
					ResultBase.RES_ERROR_DAO, exDao);
        } catch (Exception e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to set system configurations.", e);
			resultBase = new ResultBase(
					ResultBase.RES_ERROR_SETSYSTEMCONFIGFAIL,
					ResultBase.RES_ERROR_GENERAL, e);
        } finally {
            tp.methodExit(resultBase.toString());
        }
        return resultBase;
    }
    
    

    /**
     * create Member Server configuration.
     *
     * it can be overridden by the customer specific method.
     */
    protected MemberServer createMemberServerInfo() {
        MemberServer infoConfig = new MemberServer();
        infoConfig.setServerUri(String.valueOf(context.getAttribute(
                    		GlobalConstant.POINTSERVERURI)).toString());
        return infoConfig;
        /*
         * sample: Disney Store's Fantamiliar
         *
        FantamiliarConfig infoConfig = new FantamiliarConfig();
        infoConfig.setServerUri(String.valueOf(context.getAttribute(
                    		GlobalConstant.POINTSERVERURI)).toString());
        infoConfig.setCorpId(String.valueOf(context.getAttribute(
            		GlobalConstant.POINTSERVERCORPID)).toString());
        infoConfig.setRate1(String.valueOf(context.getAttribute(
            		GlobalConstant.POINTSERVERPOINTRATE1)).toString());
        infoConfig.setRate2(String.valueOf(context.getAttribute(
            		GlobalConstant.POINTSERVERPOINTRATE2)).toString());
        infoConfig.setRate3(String.valueOf(context.getAttribute(
            		GlobalConstant.POINTSERVERPOINTRATE3)).toString());
        return infoConfig;
        */
    }
}