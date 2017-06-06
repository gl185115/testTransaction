package ncr.res.mobilepos.customerSearch.constants;

/**
 * Constants
 */
public class CustomerSearchConstants {

    /** search member use url */
    public static final String API_B0110 = "/member/MemberSearchApi/exec.do";

    /** search buying history use url */
    public static final String API_B0407 = "/member/MemberTradeListApi/exec.do";
    
    /** get changed login key use url */
    public static final String API_B0001 = "/login/LoginKeyConvertApi/exec.do";
    
    /** get rank info use url */
    public static final String API_B2001 = "/rank/GetRankDetailApi/exec.do";
    
    /** get member info use url */
    public static final String API_B0102 = "/member/MemberDetailApi/exec.do";

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
    
    /** Dummy Xml - LoginKey */
    public static final String LOGINKEYXMLPATH = "D:/DummyXml/loginKey_success.xml";
    
    /** Dummy Xml - LoginKey */
    public static final String RANKXMLPATH = "D:/DummyXml/rank_success.xml";
    
    /** Dummy Xml - LoginKey */
    public static final String MEMBERINFOXMLPATH = "D:/DummyXml/memberInfo_success.xml";
    
    /** Dummy Xml - Error */
    public static final String ERRORXMLPATH = "D:/DummyXml/error.xml";
}
