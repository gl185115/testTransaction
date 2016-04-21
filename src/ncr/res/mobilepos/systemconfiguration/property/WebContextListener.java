/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* WebContextListener
*
* A Listener class that listens each StartUp/Restart of the Web API.
*
* Campos, Carlos  (cc185102)
*/
package ncr.res.mobilepos.systemconfiguration.property;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * WebContextListener is a Listener class that listens each
 * StartUp/Restart of the Web API.
 */
public class WebContextListener implements ServletContextListener {
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * The Trace Printer for Debug.
     */
    private Trace.Printer tp;
	/**
	 * Abbreviation program name of the class.
	 */
	private static final String PROG_NAME = "WebCtx";

    /**
     * Method to destroy servlet context.
     * @param servletContext  Servlet Context Event
     */
    @Override
	public void contextDestroyed(final ServletContextEvent servletContext) {
		Object spmFw = servletContext.getServletContext().getAttribute(
				GlobalConstant.SPM_FW);
		if (spmFw != null) {
			try {
				((SpmFileWriter) spmFw).close();
			} catch (IOException e) {
				LOGGER.logWarning("SysCnfg",
						"WebContextListener.contextDestroyed",
						Logger.RES_EXCEP_IO,
						"SPM file close error.\n" + StringUtility.printStackTrace(e));
			}
		}
	}

    /**
     * Method called when during servlet StartUp.
     * @param event     Servlet Context Event
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void contextInitialized(final ServletContextEvent event) {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println(" - The WebAPI StartApp called. ");
        tp.println("Preparing to retrieve the System Parameters");

        try {
            //Instantiate the DAO Factory for System Configuration
            DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            SQLServerSystemConfigDAO systemDao = dao.getSystemConfigDAO();
            ServletContext servletContext = event.getServletContext();

            //Get the System parameters from PRM_SYSTEM_CONFIG
            Map<String, String> sysParams = systemDao.getSystemParameters();

            //Set the all the System parameters
            //in the context of Servlet. This will be used throughout
            //the lifetime of
            //the system
            for (Entry<String, String> param : sysParams.entrySet()) {
                servletContext.setAttribute(param.getKey(), param.getValue());
            }

            String multiSOD = (String) servletContext.getAttribute(GlobalConstant.MULTIPLE_SOD);
    		if(!StringUtility.isNullOrEmpty(multiSOD) && "1".equalsIgnoreCase(multiSOD)){
    			GlobalConstant.setMultiSOD(true);					
    		} else {
    			GlobalConstant.setMultiSOD(false);	
    		}

            //global variable for credential day left warning
            GlobalConstant.setCredentialDaysLeft((String) servletContext
                    .getAttribute(GlobalConstant.CREDENTIAL_DAY_LEFT_WARNING));            
            //global variable for credential expiry
            GlobalConstant.setCredentialExpiry((String) servletContext
                .getAttribute(GlobalConstant.CREDENTIAL_EXPIRY_KEY));
            //global variable for store open time
            GlobalConstant.setStoreOpenTime((String) servletContext
                .getAttribute(GlobalConstant.STORE_OPEN_TIME_KEY));
            GlobalConstant.setSwitchTime((String) servletContext
                .getAttribute(GlobalConstant.SWITCHTIME_KEY));

            String companyid = (String) servletContext
                .getAttribute(GlobalConstant.COMPANY_ID);
            // Are there Company ID set in PRM_SYSTEM_CONFIG?
            // Set the CompanyID automatically at Start Up
            if (!StringUtility.isNullOrEmpty(companyid)) {
                GlobalConstant.setCorpid(companyid);
            }

            // Set the Search Limit.
            String searchLimit = (String) servletContext
                    .getAttribute(GlobalConstant.MAX_SEARCH_RESULTS);
            if (!StringUtility.isNullOrEmpty(searchLimit)) {
                GlobalConstant.setMaxSearchResults(
                        Integer.parseInt(searchLimit));
            }

            //Set an Empty Prmotion TerminalItem HashMap
            Map<String, TerminalItem> terminalItemsMap =
                new HashMap<String, TerminalItem>();
            servletContext.setAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS,
                    terminalItemsMap);

            tp.println("WebContextListener.contextInitialized").println(
                    "System Parameter successfully retrieved.");

		} catch (DaoException e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_DAO,
					functionName + ": Failed to context initialized.", e);
		} catch (Exception e) {
			LOGGER.logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
					functionName + ": Failed to context initialized.", e);
		} finally {
            tp.methodExit();
        }
    }

}
