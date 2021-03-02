package ncr.res.mobilepos.intaPay.helper;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;


public class SignUtil {

    /**
     * ÉTÉCÉìéÌóﬁ
     */
    public static final String SIGN_TYPE_SHA256 = "HMAC-SHA256";
    public static final String ENCRYPTION_TYPE_SHA1 = "SHA-1";

    /**
     * à√çÜâªéÌóﬁ
     */
    public static final String ENCRYPTION_TYPE_HMACSHA256 = "HmacSHA256";
    public static final String ENCRYPTION_TYPE_MD5 = "MD5";
    public static final String ENCRYPTION_TYPE_RC4 = "RC4";
    

    @SuppressWarnings("rawtypes")
    public static String getSign(SortedMap<String, String> signParams, String apiKey, String encryptionType) {
        // 1. key=value ASCII sort
        StringBuffer sb = new StringBuffer();
        Set<Entry<String, String>> es = signParams.entrySet();
        Iterator<Entry<String, String>> it = es.iterator();
        Map.Entry entry;
        String key;
        String value;
        while (it.hasNext()) {
            entry = (Map.Entry) it.next();
            key = (String) entry.getKey();
            if ("sign".equals(key)) {
                continue;
            }
            value = (String) entry.getValue();
            if (!Util.isEmptyString(value)) {
                sb.append(key).append("=").append(value).append("&");
            }
        }

        // 2. API keyÇÃí«â¡
        String stringSignTemp = null;
        if (!"SHA-1".equals(encryptionType)) {
            sb.append("key=").append(apiKey);
            stringSignTemp = sb.toString();
        } else {
            stringSignTemp = sb.substring(0, sb.length() - 1);
        }

        // 3. default : HMAC-SHA256
        String sign = "";

        String signType;
        if (SIGN_TYPE_SHA256.equals(encryptionType)) {
            signType = ENCRYPTION_TYPE_HMACSHA256;
        } else if (ENCRYPTION_TYPE_SHA1.equals(encryptionType)) {
            signType = ENCRYPTION_TYPE_SHA1;
        } else {
            signType = ENCRYPTION_TYPE_MD5;
        }
        sign = EncryptionUtil.encode(stringSignTemp, signType, apiKey);
        return sign;
    }

}