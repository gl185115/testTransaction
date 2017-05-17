/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* GlobalConstant
*
* GlobalConstant is a class that enumerates all
*  Key names of the System configuration.
*
* Campos, Carlos (cc185102)
*/
package ncr.res.mobilepos.constant;

import java.util.HashMap;
import java.util.Map;

import ncr.res.mobilepos.promotion.helper.TerminalItem;

/**
 * GlobalConstant is a class that enumerates
 * all Key names of the System configuration.
 * System Configuration parameters behave in Key-Value Pair structure.
 */
public final class GlobalConstant {
    /** Default Constructor. */
    private GlobalConstant() {    	
    }
    /**
     * The key name for APIUrl.
     */
    public static final String API_SERVER_URL = "APIServerUri";
    /**
     * The key name for APIUrl timeout.
     */
    public static final String API_SERVER_TIMEOUT = "APIServerTimeout";
    /**
     * The key name for price include Tax.
     */
    public static final String PRICE_INCLUDE_TAX_KEY = "PriceIncludeTax";
    /**
     * The Key name for Tax Rate for Calculation the Consumption.
     * Tax in the WebUI side
     */
    public static final String TAX_RATE_KEY = "TaxRate";
    /**
     * The operator sign-on expiry.
     */
    public static final String CREDENTIAL_EXPIRY_KEY = "CredentialExpiry";
    /**
     * The SwitchTime of the BussinessDate.
     */
    public static final String SWITCHTIME_KEY = "SwitchTime";
    /**
     * The Store Open time.
     */
    public static final String STORE_OPEN_TIME_KEY = "StoreOpenTime";
    /**
     * TransactionInfo used for consolidation.
     */
    public static final String TRANSACTIONINFO = "TransactionInfo";
    /**
     * CurDbInfo.
     */
    public static final String CURDBINFO = "CurDbInfo";
    /**
     * The Default Language Setting.
     */
    public static final String DEFAULT_LANGUAGE = "DefaultLanguage";
   /**
     * The key of pastelPort Parameter.
     */
    public static final String PASTELPORTENV_PARAM_KEY =
        "PastelPortEnvParamKey";
    /**
     * The Key of JIS1 Parameters.
     */
    public static final String JIS1_PARAM_KEY = "JIS1PARAM";
    /**
     * The Key of JIS2 Parameters.
     */
    public static final String JIS2_PARAM_KEY = "JIS2PARAM";
    /**
     * The Key of Company ID Parameter.
     */
    public static final String COMPANY_ID = "CompanyId";
    /**
     * The Key of Threshold Parameter.
     */
    public static final String THRESHOLD = "SignlessThreshold";
    /**
     * Key for Multiple SOD on the businessdaydate.
     */
    public static final String MULTIPLE_SOD = "MultiSOD";
    /**
     * The Key of Documentary Tax Range1.
     */
    public static final String DOC_TAX_RANGE1_KEY = "Range1";
    /**
     * The Key of Flag.
     */
    public static final String FLAG = "Flag";
    /**
     * The Key of KeyValue.
     */
    public static final String KEYVALUE = "KeyValue";
    /**
     * The Key for UeIoServer address.
     */
    public static final String UE_IOSERVER_ADDRESS = "UeIoServerAddress";
    /**
     * The Key for UeIoServer port.
     */
    public static final String UE_IOSERVER_PORT = "UeIoServerPort";
    /**
     * The Key for Location Code assigned for the server.
     */
    public static final String UE_LOCATION_CODE = "UeLocationCode";
    /**
     * The Key for AMS Reward Engine Integration Protocol Version.
     */
    public static final String UE_PROTOCOL_VERSION = "UeProtocolVersion";
    /**
     * The Key for AMS Reward Engine Integration Protocol Build.
     */
    public static final String UE_PROTOCOL_BUILD = "UeProtocolBuild";
    /** The Key for Customer Tier. */
    public static final String CUSTOMER_TIER_LIST = "CustomerTierList";
    /** The Key for Search Limit. */
    public static final String MAX_SEARCH_RESULTS = "MaxSearchResults";
    /** The Key Promotion TerminalItem(s). */
    public static final String PROMOTION_TERMINAL_ITEMS =
        "PromotionTerminalItems";
    /** The Key for credential day left warning. */
    public static final String CREDENTIAL_DAY_LEFT_WARNING = "CredentialDayLeftWarning";
    /**
     * The key of receipt number 
     */
    public static final String NUMBER_OF_RECEIPT = "NumberOfReceipt";
    /**
     * The key of QueueBaster config.
     */
    public static final String QUEUEBASTER_MAXSUSPENDABLE = "MaxSuspendable";
    /** The enterprise storeid. */
    public static final String ENTERPRISE_STOREID = "0";
    /** The FULL Percent. */
    public static final int PERCENT = 100;
    /** The Pricing Type **/
    public static final String PRICING_TYPE = "PricingType";
    /** Enterprise Server Timeout */
    public static final String ENTERPRISE_SERVER_TIMEOUT = "EnterpriseServerTimeout";
    /** Enterprise Server Uri */
    public static final String ENTERPRISE_SERVER_URI = "EnterpriseServerUri";

    /** The Mix Match Target **/
    public static final String MIXMATCHTARGET = "MixMatchTarget";
    /** The Member serverUri **/
    public static final String POINTSERVERURI = "MemberServerUri";
    /** The MemberServer's CorpID **/
    public static final String POINTSERVERCORPID = "CorpId";
    /** The MemberServer (Fantamiliar point rate1) **/
    public static final String POINTSERVERPOINTRATE1 = "rate1";
    /** The MemberServer (Fantamiliar point rate2) **/
    public static final String POINTSERVERPOINTRATE2 = "rate2";
    /** The MemberServer (Fantamiliar point rate3) **/
    public static final String POINTSERVERPOINTRATE3 = "rate3";

    /** Key to retrieve TodUrl from SystemConfig **/
    public static final String KEY_TOD_URI = "TodUri";
    /** Key to retrieve TodConnectionTimeout from SystemConfig **/
    public static final String KEY_TOD_CONNECTION_TIMEOUT = "TodConnectionTimeout";
    /** Key to retrieve TodReadTimeout from SystemConfig **/
    public static final String KEY_TOD_READ_TIMEOUT = "TodReadTimeout";
    /** Keys to retrieve InStoreParams **/
    public static final String KEY_INSTORE_PARAM_1 = "InStoreParam1";
    public static final String KEY_INSTORE_PARAM_2 = "InStoreParam2";
    public static final String KEY_INSTORE_PARAM_3 = "InStoreParam3";
    public static final String KEY_INSTORE_PARAM_4 = "InStoreParam4";
    public static final String KEY_INSTORE_PARAM_5 = "InStoreParam5";
    public static final String KEY_INSTORE_PARAM_6 = "InStoreParam6";
    public static final String KEY_INSTORE_PARAM_7 = "InStoreParam7";
    public static final String KEY_INSTORE_PARAM_8 = "InStoreParam8";
    public static final String KEY_INSTORE_PARAM_9 = "InStoreParam9";
    public static final String KEY_INSTORE_PARAM_10 = "InStoreParam10";
    public static final String KEY_INSTORE_PARAM_11 = "InStoreParam11";
    /**
     * The key name for server ping timeout.
     */
    public static final String KEY_SERVER_PING_TIMEOUT = "PingWaitTimer";
    /**
     * Default value for server ping timeout in milliseconds.
     */
    private static final int DEFAULT_SERVER_PING_TIMEOUT = 3000;
    /**
     * Ping waiting time in milliseconds.
     */
    private static int pingWaitTimer = DEFAULT_SERVER_PING_TIMEOUT;
    /** The Search Limit.
     *  The default is 5.
     */
    private static int maxSearchResults = 5;

    // Tod params
    private static String todUri;
    private static int todConnectionTimeout = 1000;
    private static int todReadTimeout = 1000;

    /**  Instore params. **/
    private static String inStoreParam1;
    private static String inStoreParam2;
    private static String inStoreParam3;
    private static String inStoreParam4;
    private static String inStoreParam5;
    private static String inStoreParam6;
    private static String inStoreParam7;
    private static String inStoreParam8;
    private static String inStoreParam9;
    private static String inStoreParam10;
    private static String inStoreParam11;

    /** Terminal Item map. **/
    private static Map<String, TerminalItem> terminalItemsMap = new HashMap<>();

    /** SystemConfigMap **/
    private static Map<String, String> systemConfig = new HashMap<>();

    /**
     * System specific URL parameters.
     */
    /** Search API URL **/
    public static final String INVENTORYORDERSEARCHURL = "InventoryOrderSearchUrl";  

    private static String taxRate;
    private static String pricingType;
    private static String enterpriseServerTimeout;
    private static String enterpriseServerUri;
    private static String apiServerUrl;
    private static int apiServerTimeout = 3000;
    private static String priceIncludeTaxKey;
    private static String range1;
    private static String defaultLanguage;
    
    /**
     * The Company ID.
     */
    private static String corpID = "";

    /**
     * The Credential Expiry.
     */
    private static String credentialExpiry = "";

    private static String credentialDaysLeft = "";

    /**
     * The Store Open time.
     */
    private static String storeOpenTime = "";
    /**
     * The Switch Time.
     */
    private static String switchTime = "";
    /**
     * Allow or disallow multiple SOD on businessdaydate.
     */
    private static boolean multiSOD = false;

    /**
     * Gets Tax Rate.
     * @return
     */
    public static String getTaxRate() {
        return taxRate;
    }

    /**
     * Sets Tax Rate.
     * @param taxRate
     */
    public static void setTaxRate(String taxRate) {
        GlobalConstant.taxRate = taxRate;
    }

    /**
     * Set the CORP ID.
     * @param corpidToSet    Corp ID to set.
     */
    public static void setCorpid(final String corpidToSet) {
        GlobalConstant.corpID = corpidToSet;
    }

    /**
     * Get the Corp ID.
     * @return  The CORP ID.
     */
    public static String getCorpid() {
        return GlobalConstant.corpID;
    }

    /**
     * Set the Store Open Time.
     * @param storeOpenTimetoSet The Store Open Time.
     */
    public static void setStoreOpenTime(final String storeOpenTimetoSet) {
        GlobalConstant.storeOpenTime = storeOpenTimetoSet;
    }

    /**
     * Get the Store Open Time.
     * @return  The Store Open Time.
     */
    public static String getStoreOpenTime() {
        return storeOpenTime;
    }

    /**
     * Set the search limit.
     * @param limitToSet    The search limit to set.
     */
    public static void setMaxSearchResults(final int limitToSet) {
        if (0 < limitToSet) {
            GlobalConstant.maxSearchResults = limitToSet;
        }
    }

    /**
     * Get the Search Limit.
     * @return  The Search limit.
     */
    public static int getMaxSearchResults() {
        return GlobalConstant.maxSearchResults;
    }

    /**
     * Get the Credential Expiry.
     * @return  The Search limit.
     */
    public static String getCredentialExpiry() {
        return credentialExpiry;
    }

    /**
     * Set the Credential Expiry.
     * @param credentialExpiryToSet The Credential expiry to set.
     */
    public static void setCredentialExpiry(final String credentialExpiryToSet) {
        GlobalConstant.credentialExpiry = credentialExpiryToSet;
    }

    /**
     * Get the Switch Time.
     * @return  The Search limit.
     */
    public static String getSwitchTime() {
        return switchTime;
    }

    /**
     * Set the Switch Time.
     * @param switchTimeToSet The switch time to set.
     */
    public static void setSwitchTime(final String switchTimeToSet) {
        GlobalConstant.switchTime = switchTimeToSet;
    }
    
    /**
     * Get the day which starts the display of a login warning message. 
     * @return  Credential Days left before password expiration
     */
    public static String getCredentialDaysLeft() {
        return credentialDaysLeft;
    }

    /**
     * Set the day which starts the display of a login warning message. 
     * @param credentialDaysLeftToSet The Credential days left to set.
     */
    public static void setCredentialDaysLeft(final String credentialDaysLeftToSet) {
        GlobalConstant.credentialDaysLeft = credentialDaysLeftToSet;
    }
    
    public static void setMultiSOD(final boolean multiSODToSet) {
    	GlobalConstant.multiSOD = multiSODToSet;
    }
    
    public static boolean isMultiSOD() {
    	return GlobalConstant.multiSOD;    	
    }

    /**
     * Sets todUri
     * @param todUri
     */
    public static void setTodUri(String todUri) {
        GlobalConstant.todUri = todUri;
    }

    /**
     * Sets todConnectionTimeout
     * @param todConnectionTimeout
     */
    public static void setTodConnectionTimeout(int todConnectionTimeout) {
        GlobalConstant.todConnectionTimeout = todConnectionTimeout;
    }

    /**
     *
     * @param todReadTimeout
     */
    public static void setTodReadTimeout(int todReadTimeout) {
        GlobalConstant.todReadTimeout = todReadTimeout;
    }

    /**
     * Gets todUri
     * @return
     */
    public static String getTodUri() {
        return GlobalConstant.todUri;
    }

    /**
     * Gets todConnectionTimeout
     * @return
     */
    public static int getTodConnectionTimeout() {
        return GlobalConstant.todConnectionTimeout;
    }

    /**
     * Gets todReadTimeout
     * @return
     */
    public static int getTodReadTimeout() {
        return GlobalConstant.todReadTimeout;
    }

    public static String getInStoreParam1() {
        return inStoreParam1;
    }

    public static void setInStoreParam1(String inStoreParam1) {
        GlobalConstant.inStoreParam1 = inStoreParam1;
    }

    public static String getInStoreParam2() {
        return inStoreParam2;
    }

    public static void setInStoreParam2(String inStoreParam2) {
        GlobalConstant.inStoreParam2 = inStoreParam2;
    }

    public static String getInStoreParam3() {
        return inStoreParam3;
    }

    public static void setInStoreParam3(String inStoreParam3) {
        GlobalConstant.inStoreParam3 = inStoreParam3;
    }

    public static String getInStoreParam4() {
        return inStoreParam4;
    }

    public static void setInStoreParam4(String inStoreParam4) {
        GlobalConstant.inStoreParam4 = inStoreParam4;
    }

    public static String getInStoreParam5() {
        return inStoreParam5;
    }

    public static void setInStoreParam5(String inStoreParam5) {
        GlobalConstant.inStoreParam5 = inStoreParam5;
    }

    public static String getInStoreParam6() {
        return inStoreParam6;
    }

    public static void setInStoreParam6(String inStoreParam6) {
        GlobalConstant.inStoreParam6 = inStoreParam6;
    }

    public static String getInStoreParam7() {
        return inStoreParam7;
    }

    public static void setInStoreParam7(String inStoreParam7) {
        GlobalConstant.inStoreParam7 = inStoreParam7;
    }

    public static String getInStoreParam8() {
        return inStoreParam8;
    }

    public static void setInStoreParam8(String inStoreParam8) {
        GlobalConstant.inStoreParam8 = inStoreParam8;
    }

    public static String getInStoreParam9() {
        return inStoreParam9;
    }

    public static void setInStoreParam9(String inStoreParam9) {
        GlobalConstant.inStoreParam9 = inStoreParam9;
    }

    public static String getInStoreParam10() {
        return inStoreParam10;
    }

    public static void setInStoreParam10(String inStoreParam10) {
        GlobalConstant.inStoreParam10 = inStoreParam10;
    }

    public static String getInStoreParam11() {
        return inStoreParam11;
    }

    public static void setInStoreParam11(String inStoreParam11) {
        GlobalConstant.inStoreParam11 = inStoreParam11;
    }

    public static Map<String, TerminalItem> getTerminalItemsMap() {
        return terminalItemsMap;
    }

    public static void setTerminalItemsMap(Map<String, TerminalItem> terminalItemsMap) {
        GlobalConstant.terminalItemsMap = terminalItemsMap;
    }

    public static Map<String, String> getSystemConfig() {
        return systemConfig;
    }

    public static void setSystemConfig(Map<String, String> systemConfig) {
        GlobalConstant.systemConfig = systemConfig;
    }

	public static String getPricingType() {
        return pricingType;
    }
	
	public static void setPricingType(String pricingType) {
        GlobalConstant.pricingType = pricingType;
    }

    public static String getEnterpriseServerTimeout() {
        return enterpriseServerTimeout;
    }

    public static void setEnterpriseServerTimeout(String enterpriseServerTimeout) {
        GlobalConstant.enterpriseServerTimeout = enterpriseServerTimeout;
    }
    public static String getEnterpriseServerUri() {
        return enterpriseServerUri;
    }

    public static void setEnterpriseServerUri(String enterpriseServerUri) {
        GlobalConstant.enterpriseServerUri = enterpriseServerUri;
    }

    public static String getApiServerUrl() {
        return apiServerUrl;
    }

    public static void setApiServerUrl(String apiServerUrl) {
        GlobalConstant.apiServerUrl = apiServerUrl;
    }

    public static int getApiServerTimeout() {
        return apiServerTimeout;
    }

    public static void setApiServerTimeout(int apiServerTimeout) {
        GlobalConstant.apiServerTimeout = apiServerTimeout;
    }

    public static String getPriceIncludeTaxKey() {
        return priceIncludeTaxKey;
    }

    public static void setPriceIncludeTaxKey(String priceIncludeTaxKey) {
        GlobalConstant.priceIncludeTaxKey = priceIncludeTaxKey;
    }

    public static String getRange1() {
        return range1;
    }

    public static void setRange1(String range1) {
        GlobalConstant.range1 = range1;
    }

    public static String getDefaultLanguage() {
        return defaultLanguage;
    }

    public static void setDefaultLanguage(String defaultLanguage) {
        GlobalConstant.defaultLanguage = defaultLanguage;
    }

	public static int getPingWaitTimer() {
		return pingWaitTimer;
	}

	public static void setPingWaitTimer(int pingWaitTimer) {
		GlobalConstant.pingWaitTimer = pingWaitTimer;
	}
}

