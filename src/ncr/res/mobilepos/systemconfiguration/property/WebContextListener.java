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

import java.io.IOException;
import java.util.Map;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.giftcard.factory.ToppanGiftCardConfigFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JrnSpm;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;


/**
 * WebContextListener is a Listener class that listens each
 * StartUp/Restart of the Web API.
 */
public class WebContextListener implements ServletContextListener {
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
			try {
                SnapLogger.closeInstance();
                DebugLogger.closeAllDbgPrinter();
                SpmFileWriter.closeInstance();
			} catch (IOException e) {
                Logger.getInstance().logWarning("SysCnfg",
						"WebContextListener.contextDestroyed",
						Logger.RES_EXCEP_IO,
						"SPM file close error.\n" + StringUtility.printStackTrace(e));
			}
	}

    /**
     * Initializes and loads environment variable containers.
     * @throws NamingException
     */
	public final void initializeEnvironmentVariables() throws NamingException {
        // Loads from WindowsEnvironmentVariables.
        WindowsEnvironmentVariables.initInstance();
        // Loads from web.xml.
        EnvironmentEntries.initInstance(new InitialContext());
    }

    /**
     * Initializes Loggers.
     * @throws NamingException
     * @throws IOException
     */
    public final void initializeLoggers() throws NamingException, IOException {
        EnvironmentEntries environmentEntries = EnvironmentEntries.getInstance();

        // Initializes IowLogger.
        Logger.initInstance(environmentEntries.getIowPath(), environmentEntries.getServerId());
        // Initializes SnapLogger.
        SnapLogger.initInstance(environmentEntries.getSnapPath());

        // Initializes DebugLogger.
        DebugLogger.initInstance(environmentEntries.getTracePath(), environmentEntries.getDebugLevel());
        // Initializes SpmFileWriter and writes a header line.
        SpmFileWriter spmFileWriter = SpmFileWriter.initInstance(environmentEntries.getSpmPath(),
                environmentEntries.getServerId(), true);
        spmFileWriter.write(JrnSpm.HEADER);

        Trace.Printer tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        String functionName = DebugLogger.getCurrentMethodName();
        if(tp != null) {
            tp.methodEnter(functionName);
            tp.println(" - The WebAPI StartApp called. ");
            tp.println("Preparing to retrieve the System Parameters");
            tp.methodExit();
        }
    }

    /**
     * Initializes database related classes.
     * @throws DaoException
     * @throws SQLStatementException
     */
    public final void initializeDBInstances() throws DaoException, SQLStatementException {
        // Checks if DataSource is present in context.xml, if it fails with invalid config, DaoException is thrown.
        JndiDBManagerMSSqlServer.initialize();
        // Initializes SQLStatement.
        SQLStatement.initInstance();
    }

    /**
     * Preload and cache system parameters from DB.
     * @throws DaoException
     */
    public final void preloadDBRecord() throws DaoException {
        DAOFactory dao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        //Get System parameters from PRM_SYSTEM_CONFIG and set to GlobalConstant.
        SQLServerSystemConfigDAO systemDao = dao.getSystemConfigDAO();
        Map<String, String> systemConfig = systemDao.getSystemParameters();
        GlobalConstant.setSystemConfig(systemConfig);
        // Copies some params which are used inside resTransaction to GlobalConstants from SystemConfiguration.
        copySystemConfigToGlobalConstant(systemConfig);
    }

    /**
     * Initializes business logic factories.
     * @throws Exception
     */
    public final void initializeBusinessLogicFactories() throws Exception {
        EnvironmentEntries environmentEntries = EnvironmentEntries.getInstance();
        // Loads ItemCode.xml file
        BarcodeAssignmentFactory.initialize(environmentEntries.getParaBasePath());
        // Loads config file for Toppan Giftcard feature.
        ToppanGiftCardConfigFactory.initialize(environmentEntries.getCustomParamBasePath());
        // Loads QrCodeInfo  Information
        QrCodeInfoFactory.initialize(WindowsEnvironmentVariables.getInstance().getSystemPath());
    }

    /**
     * Method called when during servlet StartUp.
     * @param event     Servlet Context Event
     */
    @SuppressWarnings("unchecked")
    @Override
    public final void contextInitialized(final ServletContextEvent event) {
        String functionName = "";
        Trace.Printer tp = null;
        try {
            initializeEnvironmentVariables();
            initializeLoggers();

            functionName = DebugLogger.getCurrentMethodName();
            tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
            tp.methodEnter(functionName);
            tp.println("WebContextListener.contextInitialized").println("System Parameter successfully retrieved.");

            initializeDBInstances();
            preloadDBRecord();
            initializeBusinessLogicFactories();
		} catch (Exception e) {
            // In case Logger is failed to initialize.
            System.out.println("resTransaction failed to initialize caused by:" + e.getMessage());
            e.printStackTrace();
            if(Logger.getInstance() != null) {
                Logger.getInstance().logSnapException(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                        functionName + ": Failed to context initialized.", e);
            }
		} finally {
            if(tp != null) {
                tp.methodExit();
            }
        }
    }

    /**
     * Copies to cache system config parameters.
     * @param sysParams
     */
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
        if(!StringUtility.isNullOrEmpty(apiServerTimeout)) {
            GlobalConstant.setApiServerTimeout(Integer.parseInt(apiServerTimeout));
        }

        String priceIncludeTax = sysParams.get(GlobalConstant.PRICE_INCLUDE_TAX_KEY);
        if(StringUtility.isNullOrEmpty(priceIncludeTax)) {
            priceIncludeTax = String.valueOf(Item.ROUND_DOWN);
        }
        GlobalConstant.setPriceIncludeTaxKey(priceIncludeTax);

        GlobalConstant.setPricingType(sysParams.get(GlobalConstant.PRICING_TYPE));

        GlobalConstant.setEnterpriseServerTimeout(sysParams.get(GlobalConstant.ENTERPRISE_SERVER_TIMEOUT));
        GlobalConstant.setEnterpriseServerUri(sysParams.get(GlobalConstant.ENTERPRISE_SERVER_URI));
        
        String pingWaitTimer = sysParams.get(GlobalConstant.PING_WAIT_TIMER);
        if(!StringUtility.isNullOrEmpty(pingWaitTimer)) {
        	GlobalConstant.setPingWaitTimer(Integer.parseInt(pingWaitTimer));
        }
    }
}
