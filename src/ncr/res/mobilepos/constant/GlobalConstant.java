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

import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.WebServerGlobals;

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
     * The Network Email Address for sending the Receipt.
     */
    public static final String EMAIL_ADDRESS_KEY = "EmailAddress";
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
     * The Key of SPM file writer.
     */
    public static final String SPM_FW = "SPMFile";
    /**
     * Key for Multiple SOD on the businessdaydate.
     */
    public static final String MULTIPLE_SOD = "MultiSOD";
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
    public static final String CREDENTIAL_DAY_LEFT_WARNING =
        "CredentialDayLeftWarning";
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
    public static final String PRICINGTYPE = "PricingType";
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
    /** The Search Limit.
     *  The default is 5.
     */
    private static int maxSearchResults = 5;

    /** 
     * System specific URL parameters.
     */
    /** Search API URL **/
    public static final String INVENTORYORDERSEARCHURL = "InventoryOrderSearchUrl";  

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
     * Reset the Global values saved from the system.
     * @param newGlobalValues   The new Global variables.
     */
    public static void reset(WebServerGlobals newGlobalValues) {
		if (!StringUtility.isNullOrEmpty(newGlobalValues.getCredentialexpiry())) {
			setCredentialExpiry(newGlobalValues.getCredentialcookiesexpiry());
		}

		if (!StringUtility.isNullOrEmpty(newGlobalValues.getStoreOpenTime())) {
			setStoreOpenTime(newGlobalValues.getStoreOpenTime());
		}

        setMaxSearchResults(newGlobalValues.getMaxSearchResults());
    }
}

