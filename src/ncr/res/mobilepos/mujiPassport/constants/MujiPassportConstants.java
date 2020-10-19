package ncr.res.mobilepos.mujiPassport.constants;

/**
 * Constants
 */
public class MujiPassportConstants {
    public static final String GETACCOUNTINFO = "/getAccountInfo";
    public static final String REGISTTRADE = "/registTrade";
    public static final String GETPASSPORTPAYMENTINFO = "/getPassportPaymentInfo";
    public static final String PAYPASSPORT = "/payPassport";
    public static final String SETTLEMENTCOMPLETE = "/settlementComplete";

    /** the Category column of the table PRM_SYSTEM_CONFIG */
    public static final String MEMBER_CATEGORY = "MemberServer";

    /** Url the KeyId column of the table PRM_SYSTEM_CONFIG */
    public static final String KEYID_MEMBERSERVERURI = "MemberServerUri";

    /** Username the KeyId column of the table PRM_SYSTEM_CONFIG */
    public static final String KEYID_MEMBERSERVERUSER = "MemberServerUser";

    /** Password the KeyId column of the table PRM_SYSTEM_CONFIG */
    public static final String KEYID_MEMBERSERVERPASS = "MemberServerPass";

    /** Timeout the KeyId column of the table PRM_SYSTEM_CONFIG */
    public static final String KEYID_MEMBERSERVERTIMEOUT = "MemberServerTimeout";

    /** Timeout the KeyId column of the table PRM_SYSTEM_CONFIG */
    public static final String KEYID_MEMBERSERVERCONNECTTIMEOUT = "MemberServerConnectTimeout";

    /** Dummy Xml - AccountInfo */
    public static final String ACCOUNTINFO_TEST = "AccountInfo.xml";
    
    /** Dummy Xml - RegistTrade */
    public static final String REGISTTRADE_TEST = "RegistTrade.xml";
    
    /** Dummy Xml - RegistTrade */
    public static final String PASSPORTPAYMENTINFO_TEST = "PassportPaymentInfo.xml";
    
    /** Dummy Xml - payPassport */
    public static final String PAYPASSPORT_TEST = "PayPassport.xml";
    
    
    /** Dummy Xml - settlementComplete */
    public static final String SETTLEMENTCOMPLETE_TEST = "SettlementComplete.xml";

    /** Retry total times */
    public static final int RETRYTOTAL = 3;
}
