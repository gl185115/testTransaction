/*
* Copyright (c) 2011-2019 NCR/JAPAN Corporation SW-R&D
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
import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.ej.Factory.NameCategoryFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.giftcard.factory.ToppanGiftCardConfigFactory;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.JrnSpm;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.SpmFileWriter;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.intaPay.factory.IntaPayFactory;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.point.factory.PointRateFactory;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.promotion.factory.PromotionMsgInfoFactory;
import ncr.res.mobilepos.promotion.factory.QrCodeInfoFactory;
import ncr.res.mobilepos.promotion.factory.TaxRateInfoFactory;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;
import ncr.res.mobilepos.helper.DataBinding;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.uiconfig.model.schedule.Schedule;



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
     * @throws IOException
     */
	public final void initializeEnvironmentVariables() throws NamingException, IOException {
        // Loads from WindowsEnvironmentVariables.
        WindowsEnvironmentVariables winEnv = WindowsEnvironmentVariables.initInstance();
        // Loads from web.xml.
        EnvironmentEntries.initInstance(new InitialContext());
        // Loads from system config files.
        if(!winEnv.isServerTypeEnterprise()){
            SystemFileConfig.initInstance(winEnv.getSystemPath());
        }
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
        String functionName = "initializeLoggers";
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
        GlobalConstant.setSystemConfigInfoList(systemDao.getSystemConfigInfo());
        // Copies some params which are used inside resTransaction to GlobalConstants from SystemConfiguration.
        copySystemConfigToGlobalConstant(systemDao);
    }

    /**
     * Initializes business logic factories.
     * @throws Exception
     */
    public final void initializeBusinessLogicFactories() throws Exception {
        EnvironmentEntries environmentEntries = EnvironmentEntries.getInstance();
        WindowsEnvironmentVariables windowsEnvironmentVariables = WindowsEnvironmentVariables.getInstance();
        SystemFileConfig systemFileConfig = SystemFileConfig.getInstance();

        // Loads ItemCode.xml file
        BarcodeAssignmentFactory.initialize(environmentEntries.getParaBasePath());
        // Loads config file for Toppan Giftcard feature.
        ToppanGiftCardConfigFactory.initialize(environmentEntries.getCustomParamBasePath());
        // Loads config form systemconfig table for intapay
        IntaPayFactory.initialize();

        // Loads System Name Information
    	NameCategoryFactory.initialize();

    	// POSLog.xmlのパーシング用のオブジェクト (DataBinding)の生成
        GlobalConstant.poslogDataBinding = new DataBinding<PosLog>(PosLog.class);

        // ForwardCountDataオブジェクトをxml変換するDataBindingオブジェクトの生成
        GlobalConstant.forwardcountdataDataBinding = new DataBinding<ForwardCountData>(ForwardCountData.class);

        // SearchedPosLog(POSLog.xml)のパーシング用のオブジェクト (DataBinding)の生成
        GlobalConstant.searchedposlogDataBinding = new DataBinding<SearchedPosLog>(SearchedPosLog.class);

        // schedule.xmlのパーシング用のオブジェクト (DataBinding)の生成
        GlobalConstant.scheduleDataBinding = new DataBinding<Schedule>(Schedule.class);



        if(!windowsEnvironmentVariables.isServerTypeEnterprise()) {
            String companyId =  systemFileConfig.getCompanyId();
            String storeId = systemFileConfig.getStoreId();

            // Only HOST loads QrCodeInfo  Information
            QrCodeInfoFactory.initialize(companyId, storeId);
            // Only HOST loads PointRate  Information
        	PointRateFactory.initialize(companyId, storeId);
            // Only HOST loads PricePromInfo Information
        	// PricePromInfoFactory.initialize(companyId, storeId);
        	// Only HOST loads PriceMMInfo Information
        	// PriceMMInfoFactory.initialize(companyId, storeId);
        	// Only HOST loads PromotionMsg Information
        	PromotionMsgInfoFactory.initialize(companyId, storeId);
        	// Only HOST loads TaxRate Information
        	TaxRateInfoFactory.initialize(companyId, storeId);
        }else{
        	// Only Enterprise loads TaxRate Information
        	TaxRateInfoFactory.initialize("0", "0");
        }
    }

    /**
     * Method called when during servlet StartUp.
     * @param event     Servlet Context Event
     */
    @Override
    public final void contextInitialized(final ServletContextEvent event) {
        String functionName = "";
        Trace.Printer tp = null;
        try {
            initializeEnvironmentVariables();
            initializeLoggers();

            functionName = "contextInitialized";
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
     * @param systemDao
     */
    private static void copySystemConfigToGlobalConstant(SQLServerSystemConfigDAO systemDao) {
        // Gets MultiSOD flag from SystemConfiguration.
        String multiSOD = systemDao.getParameterValue(GlobalConstant.MULTIPLE_SOD, GlobalConstant.CATE_TILL);
        if(!StringUtility.isNullOrEmpty(multiSOD) && "1".equalsIgnoreCase(multiSOD)){
            GlobalConstant.setMultiSOD(true);
        } else {
            GlobalConstant.setMultiSOD(false);
        }
        // global variable for DebugSmartP
        String debugSmartP = systemDao.getParameterValue(GlobalConstant.KEY_TOD_SMARTP_DEBUG, GlobalConstant.CATE_MEMBER_SERVER);
        if (!StringUtility.isNullOrEmpty(debugSmartP) && "1".equalsIgnoreCase(debugSmartP)) {
            GlobalConstant.setMemberServerDebug(true);
        } else {
            GlobalConstant.setMemberServerDebug(false);
        }
        String debugCouponServer = systemDao.getParameterValue(GlobalConstant.KEY_COUPON_SERVER_DEBUG, GlobalConstant.CATE_COUPON_SERVER);
        if (!StringUtility.isNullOrEmpty(debugCouponServer) && "1".equalsIgnoreCase(debugCouponServer)) {
            GlobalConstant.setCouponServerDebug(true);
        } else {
            GlobalConstant.setCouponServerDebug(false);
        }
        //global variable for credential day left warning
        GlobalConstant.setCredentialDaysLeft(systemDao.getParameterValue(GlobalConstant.CREDENTIAL_DAY_LEFT_WARNING, GlobalConstant.CATE_CREDENTIAL));
        //global variable for credential expiry
        GlobalConstant.setCredentialExpiry(systemDao.getParameterValue(GlobalConstant.CREDENTIAL_EXPIRY_KEY, GlobalConstant.CATE_CREDENTIAL));
        //global variable for store open time
        GlobalConstant.setStoreOpenTime(systemDao.getParameterValue(GlobalConstant.STORE_OPEN_TIME_KEY, GlobalConstant.CATE_REPORT));
        // global variable for MaxSearchResults
        String searchLimit = systemDao.getParameterValue(GlobalConstant.MAX_SEARCH_RESULTS, GlobalConstant.CATE_SEARCH);
        if (!StringUtility.isNullOrEmpty(searchLimit)) {
            GlobalConstant.setMaxSearchResults(Integer.parseInt(searchLimit));
        }

        // global variable for switch time
        GlobalConstant.setTodUri(systemDao.getParameterValue(GlobalConstant.KEY_TOD_URI, GlobalConstant.CATE_TOD_REQUEST));
        // global variable for MaxSearchResults
        String todConnectionTimeout = systemDao.getParameterValue(GlobalConstant.KEY_TOD_CONNECTION_TIMEOUT, GlobalConstant.CATE_TOD_REQUEST);
        if (!StringUtility.isNullOrEmpty(todConnectionTimeout)) {
            GlobalConstant.setTodConnectionTimeout(Integer.parseInt(todConnectionTimeout));
        }
        // global variable for MaxSearchResults
        String todReadTimeout = systemDao.getParameterValue(GlobalConstant.KEY_TOD_READ_TIMEOUT, GlobalConstant.CATE_TOD_REQUEST);
        if (!StringUtility.isNullOrEmpty(todReadTimeout)) {
            GlobalConstant.setTodReadTimeout(Integer.parseInt(todReadTimeout));
        }
        GlobalConstant.setInStoreParam1(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_1, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam2(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_2, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam3(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_3, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam4(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_4, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam5(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_5, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam6(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_6, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam7(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_7, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam8(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_8, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam9(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_9, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam10(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_10, GlobalConstant.CATE_PRICING));
        GlobalConstant.setInStoreParam11(systemDao.getParameterValue(GlobalConstant.KEY_INSTORE_PARAM_11, GlobalConstant.CATE_PRICING));

        String bizCatIdColumnOfStoreInfo = systemDao.getParameterValue(GlobalConstant.BIZCATID_COLUMN_OF_STOREINFO, GlobalConstant.CATE_CM_INFORMATION);
        GlobalConstant.setBizCatIdColumnOfStoreInfo(
        		 (!StringUtility.isNullOrEmpty(bizCatIdColumnOfStoreInfo)) ? bizCatIdColumnOfStoreInfo : GlobalConstant.DEFAULT_BIZCATID_COLUMN_OF_STOREINFO);

        String taxRate = systemDao.getParameterValue(GlobalConstant.TAX_RATE_KEY, GlobalConstant.CATE_PRICING);
        if (!StringUtility.isNullOrEmpty(taxRate)) {
            taxRate = "0";
        }
        GlobalConstant.setTaxRate(taxRate);

        GlobalConstant.setRange1(systemDao.getParameterValue(GlobalConstant.DOC_TAX_RANGE1_KEY, GlobalConstant.CATE_DOCUMENTARY_TAX));

        GlobalConstant.setDefaultLanguage(systemDao.getParameterValue(GlobalConstant.DEFAULT_LANGUAGE, GlobalConstant.CATE_LANGUAGE));

        GlobalConstant.setApiServerUrl(systemDao.getParameterValue(GlobalConstant.API_SERVER_URL, GlobalConstant.CATE_API_SERVER));
        String apiServerTimeout = systemDao.getParameterValue(GlobalConstant.API_SERVER_TIMEOUT, GlobalConstant.CATE_API_SERVER);
        if(!StringUtility.isNullOrEmpty(apiServerTimeout)) {
            GlobalConstant.setApiServerTimeout(Integer.parseInt(apiServerTimeout));
        }

        String priceIncludeTax = systemDao.getParameterValue(GlobalConstant.PRICE_INCLUDE_TAX_KEY, GlobalConstant.CATE_PRICING);
        if(StringUtility.isNullOrEmpty(priceIncludeTax)) {
            priceIncludeTax = String.valueOf(Item.ROUND_DOWN);
        }
        GlobalConstant.setPriceIncludeTaxKey(priceIncludeTax);

        GlobalConstant.setPricingType(systemDao.getParameterValue(GlobalConstant.PRICING_TYPE, GlobalConstant.CATE_PRICING));

        GlobalConstant.setEnterpriseServerTimeout(systemDao.getParameterValue(GlobalConstant.ENTERPRISE_SERVER_TIMEOUT, GlobalConstant.CATE_ENTERPRISE_SERVER));
        GlobalConstant.setEnterpriseServerUri(systemDao.getParameterValue(GlobalConstant.ENTERPRISE_SERVER_URI, GlobalConstant.CATE_ENTERPRISE_SERVER));
        GlobalConstant.setAuthenticationUid(systemDao.getParameterValue(GlobalConstant.AUTHENTICATION_UID, GlobalConstant.CATE_ENTERPRISE_SERVER));
        GlobalConstant.setAuthenticationPassword(systemDao.getParameterValue(GlobalConstant.AUTHENTICATION_PASSWORD, GlobalConstant.CATE_ENTERPRISE_SERVER));

        String serverPingTimeout = systemDao.getParameterValue(GlobalConstant.KEY_SERVER_PING_TIMEOUT, GlobalConstant.CATE_SYSTEM);
        if(!StringUtility.isNullOrEmpty(serverPingTimeout)) {
        	try{
        		int serverPingTimeoutInt = Integer.parseInt(serverPingTimeout);
        		if(serverPingTimeoutInt >= 0) {
        			GlobalConstant.setServerPingTimeout(serverPingTimeoutInt);
        		}
        	} catch(NumberFormatException ex) {
				Logger.getInstance().logAlert(
						"SysCnfg",
						"copySystemConfigToGlobalConstant",
						Logger.RES_PARA_ERR,
						"Invalid param format: "
								+ GlobalConstant.KEY_SERVER_PING_TIMEOUT + ","
								+ serverPingTimeout);
        	}
        }

        GlobalConstant.setMaxQRCodePrintNum(systemDao.getParameterValue(GlobalConstant.MAXQRCODEPRINTNUM, GlobalConstant.CATE_PROMOTION));

        // for Self RAP
        GlobalConstant.setSelfRapAuthenticationUid(systemDao.getParameterValue(GlobalConstant.AUTHENTICATION_UID, GlobalConstant.CATE_SELF_RAP));
        if (StringUtility.isNullOrEmpty(GlobalConstant.getSelfRapAuthenticationUid())) {
        	GlobalConstant.setSelfRapAuthenticationUid(GlobalConstant.DEF_SELF_RAP_AUTHENTICATION_UID);
        }
        GlobalConstant.setSelfRapAuthenticationPassword(systemDao.getParameterValue(GlobalConstant.AUTHENTICATION_PASSWORD, GlobalConstant.CATE_SELF_RAP));
        if (StringUtility.isNullOrEmpty(GlobalConstant.getSelfRapAuthenticationPassword())) {
        	GlobalConstant.setSelfRapAuthenticationPassword(GlobalConstant.DEF_SELF_RAP_AUTHENTICATION_PWD);
        }
    }

    /**
        GlobalConstant.setPingWaitTimer(
    parsePositiveInt(GlobalConstant.KEY_SERVER_PING_TIMEOUT,
                     sysParams, GlobalConstant.DEFAULT_SERVER_PING_TIMEOUT));
     **/

    /**
     * Gets one value from given key-value map, then parse into int.
     * If there is no value matched, the value has invalid format as Number or
     *    the parsed value is not positive int, then default is returned.
     * @param key
     * @param kvMap
     * @param defaultValue
     * @return found value or default for not-found, invalid format or negative int.
     */
    public static int parsePositiveInt(String key, Map<String, String> kvMap, int defaultValue) {
        String strValue = kvMap.get(key);
        if(!StringUtility.isNullOrEmpty(strValue)) {
            try {
                int intValue = Integer.parseInt(strValue);
                if (intValue >= 0) {
                    return intValue;
                }
            } catch (NumberFormatException e) {
                // Invalid format.
                Logger.getInstance().logAlert("GlobalConstant",
                        "parsePositiveInt",
                        Logger.RES_EXCEP_NODATAFOUND,
                        "Invalid format for: " + key + " in PRM_SYSTEM_CONFIG");
            }
        }
        return defaultValue;
    }
}
