package ncr.res.mobilepos.intaPay.constants;

/**
 * Constants
 */
public class IntaPayConstants {

    /**
     * 加盟店情報
     */
    public static final String DEFAULT_MCH_CODE = "D1000010006";

    public static final String DEFAULT_API_KEY = "38c80ac8b730";

    public static final String DEFAULT_CERTFILEPATH = "D:\\software\\ncr\\res\\link\\catalina\\conf\\100001.p12";

    public static final String DEFAULT_CERTPW = "100001";

    public static final Boolean DEFAULT_CERTUSE = true;

    public static final Boolean DEFAULT_DEBUG_FLAG = false;

    public static final String DEFAULT_BODY = "Test";

    public static final String DEFAULT_DEVICE_INFO = "127.0.0.1";

    public static final String ENCODE_UTF8 = "UTF-8";

    /**
     * アクセスサーバー情報
     */
    public static final String DEFAULT_GATEWAY_SERVER_URL = "https://wechatpay-staging.intasect.com"; // "http://localhost:8080";

    /**
     * the Category column of the table PRM_SYSTEM_CONFIG
     */
    public static final String INTAPAY_CATEGORY = "IntaPayAPI";

    public static final String KEYID_CERTUSE = "CertUse";
    public static final String KEYID_GATEWAY_SERVER_URL = "GatewayServerUrl";
    public static final String KEYID_CERTPW = "CertPW";
    public static final String KEYID_CERTFILEPATH = "CertFilePath";
    public static final String KEYID_BODY = "Body";
    public static final String KEYID_DEVICE_INFO = "DeviceInfo";
    public static final String KEYID_API_KEY = "APIKey";
    public static final String KEYID_MCH_CODE = "MchCode";

    public static final String KEYID_API_KEY_STORE_PARAM = "SubCode7";
    public static final String KEYID_MCH_CODE_STORE_PARAM = "SubCode8";
    
    public static final String KEYID_DEBUG = "IntapayDebug";
    public static final String INTAPAYINFO_TEST = "IntapayInfo";
    public static final String INTAPAYINFO_TESTDATATYPE = ".json";

    public static final String DEBUG_TYPE_PAY = "pay";
    public static final String DEBUG_TYPE_PAYQUERY = "payquery";
    public static final String DEBUG_TYPE_REFUND = "refund";
    public static final String DEBUG_TYPE_REFUNDQUERY = "refundquery";
    /**
     * 支払
     */
    public static final String PAY_URL = "/BusinessTransationTSL/ws/transaction/payment.do";
    /**
     * 支払検索
     */
    public static final String PAYQUERY_URL = "/BusinessTransationTSL/ws/transactionquery/payment.do";

    /**
     * 返金
     */
    public static final String REFUND_URL = "/BusinessTransationTSL/ws/transaction/refund.do";

    /**
     * 返金検索
     */
    public static final String REFUNDQUERY_URL = "/BusinessTransationTSL/ws/transactionquery/refund.do";

    /** Retry total times */
    public static final int RETRYTOTAL = 3;

    /**
     * サインの種類 wx param
     */
    public static final String SIGN_TYPE_SHA256 = "HMAC-SHA256";

    /**
     * 変量名
     */
    public static final String TOTAL_FEE = "totalFee";

    /**
     * 変量名
     */
    public static final String TRANSACTION_ID = "transactionId";

    /**
     * エラーコード
     */
    public static final String W_ERR_CODE = "err_code";

    /**
     * エラーコード説明
     */
    public static final String W_ERR_CODE_DES = "err_code_des";

    /**
     * 業務結果
     */
    public static final String W_RESULT_CODE = "result_code";

    /**
     * リターンステータスコード
     */
    public static final String W_RETURN_CODE = "return_code";

    /**
     * リターンメッセージ
     */
    public static final String W_RETURN_MSG = "return_msg";


}
