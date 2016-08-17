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
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;
import java.util.Map;

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

            //Get the System parameters from PRM_SYSTEM_CONFIG
            SQLServerSystemConfigDAO systemDao = dao.getSystemConfigDAO();
            Map<String, String> systemConfig = systemDao.getSystemParameters();

            // Set SystemConfiguration to GlobalConstant.
            GlobalConstant.setSystemConfig(systemConfig);

            // Copies some params which are used inside resTransaction to GlobalConstants from SystemConfiguration.
            copySystemConfigToGlobalConstant(systemConfig);

            tp.println("WebContextListener.contextInitialized").println("System Parameter successfully retrieved.");
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

    private static void copySystemConfigToGlobalConstant(Map<String, String> sysParams) {
        // Gets MultiSOD flag from SystemConfiguration.
        String multiSOD = (String)sysParams.get(GlobalConstant.MULTIPLE_SOD);
        if(!StringUtility.isNullOrEmpty(multiSOD) && "1".equalsIgnoreCase(multiSOD)){
            GlobalConstant.setMultiSOD(true);
        } else {
            GlobalConstant.setMultiSOD(false);
        }

        //global variable for credential day left warning
        GlobalConstant.setCredentialDaysLeft(sysParams.get(GlobalConstant.CREDENTIAL_DAY_LEFT_WARNING));
        //global variable for credential expiry
        GlobalConstant.setCredentialExpiry(sysParams.get(GlobalConstant.CREDENTIAL_EXPIRY_KEY));
        //global variable for store open time
        GlobalConstant.setStoreOpenTime(sysParams.get(GlobalConstant.STORE_OPEN_TIME_KEY));
        // global variable for switch time
        GlobalConstant.setSwitchTime(sysParams.get(GlobalConstant.SWITCHTIME_KEY));
        // global variable for MaxSearchResults
        String searchLimit = sysParams.get(GlobalConstant.MAX_SEARCH_RESULTS);
        if (!StringUtility.isNullOrEmpty(searchLimit)) {
            GlobalConstant.setMaxSearchResults(Integer.parseInt(searchLimit));
        }

        // global variable for switch time
        GlobalConstant.setTodUri(sysParams.get(GlobalConstant.KEY_TOD_URI));
        // global variable for MaxSearchResults
        String todConnectionTimeout = sysParams.get(GlobalConstant.KEY_TOD_CONNECTION_TIMEOUT);
        if (!StringUtility.isNullOrEmpty(todConnectionTimeout)) {
            GlobalConstant.setTodConnectionTimeout(Integer.parseInt(todConnectionTimeout));
        }
        // global variable for MaxSearchResults
        String todReadTimeout = sysParams.get(GlobalConstant.KEY_TOD_READ_TIMEOUT);
        if (!StringUtility.isNullOrEmpty(todReadTimeout)) {
            GlobalConstant.setTodReadTimeout(Integer.parseInt(todReadTimeout));
        }

        GlobalConstant.setInStoreParam1(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_1));
        GlobalConstant.setInStoreParam2(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_2));
        GlobalConstant.setInStoreParam3(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_3));
        GlobalConstant.setInStoreParam4(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_4));
        GlobalConstant.setInStoreParam5(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_5));
        GlobalConstant.setInStoreParam6(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_6));
        GlobalConstant.setInStoreParam7(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_7));
        GlobalConstant.setInStoreParam8(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_8));
        GlobalConstant.setInStoreParam9(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_9));
        GlobalConstant.setInStoreParam10(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_10));
        GlobalConstant.setInStoreParam11(sysParams.get(GlobalConstant.KEY_INSTORE_PARAM_11));


        String taxRate = sysParams.get(GlobalConstant.TAX_RATE_KEY);
        if (!StringUtility.isNullOrEmpty(taxRate)) {
            taxRate = "0";
        }
        GlobalConstant.setTaxRate(taxRate);

        GlobalConstant.setRange1(sysParams.get(GlobalConstant.DOC_TAX_RANGE1_KEY));

        GlobalConstant.setDefaultLanguage(sysParams.get(GlobalConstant.DEFAULT_LANGUAGE));

        GlobalConstant.setApiServerUrl(sysParams.get(GlobalConstant.API_SERVER_URL));
        String apiServerTimeout = sysParams.get(GlobalConstant.API_SERVER_TIMEOUT);
        if(!StringUtility.isNullOrEmpty()) {
            GlobalConstant.setApiServerTimeout(Integer.parseInt(apiServerTimeout));
        }

        String priceIncludeTax = sysParams.get(GlobalConstant.PRICE_INCLUDE_TAX_KEY);
        if(StringUtility.isNullOrEmpty()) {
            priceIncludeTax = String.valueOf(Item.ROUND_DOWN);
        }
        GlobalConstant.setPriceIncludeTaxKey(priceIncludeTax);

        GlobalConstant.setPricingType(sysParams.get(GlobalConstant.PRICING_TYPE));

        GlobalConstant.setEnterpriseServerTimeout(sysParams.get(GlobalConstant.ENTERPRISE_SERVER_TIMEOUT));
        GlobalConstant.setEnterpriseServerUri(sysParams.get(GlobalConstant.ENTERPRISE_SERVER_URI));
    }

}
