package ncr.res.mobilepos.futurePay.constants;

/**
 * Constants
 */
public class FuturePayConstants {
    /** カード状態照会API */
    public static final String INQUIRY_CARD_INFO = "/inquiry_card_info";
    /** ポイント利用API  */
    public static final String WITHDRAW_POINT = "/withdraw_point";
    /** 現金利用API  */
    public static final String SALES_REWARD = "/sales_reward";
    /** ポイント付与API  */
    public static final String DEPOSIT_POINT = "/deposit_point";
    /** 履歴照会API  */
    public static final String INQUIRY_HISTORY = "/inquiry_history";

    /** the Category column of the table PRM_SYSTEM_CONFIG  */
    public static final String CATEGORY = "MemberServer";

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

    /** Dummy Xml - MemberInfo */
    public static final String MEMBERINFO_TEST = "memberInfo.json";
    
    /** Retry total times */
    public static final int RETRYTOTAL = 3;
}
