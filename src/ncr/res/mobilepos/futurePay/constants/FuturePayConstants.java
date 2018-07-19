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

    /** Dummy Xml - MemberInfo */
    public static final String MEMBERINFO_TEST = "memberInfo.json";
    
    /** Coupon Server category from PRM_SYSTEM_CONFIG **/
    public static final String COUPONSERVER_CATEGORY = "CouponServer";
    
    /** 1@:@CouponServer Debug Mode 0: CouponServer Normal Mode **/
    public static final String KEYID_COUPONSERVER_DEBUG = "CouponServerDebug";
    
    /** User of Coupon system **/
    public static final String KEYID_COUPONSERVER_USER = "CouponServerUser";
    
    /** Password of Coupon system **/
    public static final String KEYID_COUPONSERVER_PASS = "CouponServerPass";
    
    /** Connection Timeout seconds of request to Coupon system **/
    public static final String KEYID_COUPONSERVER_CONNECT_TIMEOUT = "CouponServerConnectTimeout";
    
    /** Timeout seconds of request to Coupon system **/
    public static final String KEYID_COUPONSERVER_TIMEOUT = "CouponServerTimeout";
    
    /** URL of Coupon system **/
    public static final String KEYID_COUPONSERVER_URI = "CouponServerUri";
    
    public static final String COUPON_DELETE_TEST = "coupon_delete.json";

    /** Retry total times */
    public static final int RETRYTOTAL = 3;
}
