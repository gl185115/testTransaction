package ncr.res.mobilepos.intaPay.helper;

import java.security.MessageDigest;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;

import ncr.res.mobilepos.intaPay.constants.IntaPayConstants;


/**
 * 
 * 
 * @author
 */
public class EncryptionUtil {

    private static final String[] HEX_DIGITS = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
    public static final char CHAR_ZERO = '0';

    public static String encode(String origin, String encryptionType, String apiKey) {
        String resultString = SignUtil.ENCRYPTION_TYPE_HMACSHA256.equals(encryptionType) ? doHmacSha256(origin, apiKey)
                : doEncode(origin, encryptionType, true);
        if (!Util.isEmptyString(resultString)) {
            resultString = resultString.toUpperCase();
        }
        return resultString;
    }

    public static String encodeSHA1(String str, boolean toUpperCase) {
        return doEncode(str, SignUtil.ENCRYPTION_TYPE_SHA1, toUpperCase);
    }

    private static String doEncode(String origin, String encryptionType, boolean toUpperCase) {

        String resultString = null;

        try {
            resultString = origin;
            MessageDigest md = MessageDigest.getInstance(encryptionType);
            resultString = byteArrayToHexString(md.digest(resultString.getBytes(IntaPayConstants.ENCODE_UTF8)));
            if (toUpperCase) {
                resultString = resultString.toUpperCase();
            }
        } catch (Exception e) {
            resultString = "";
        }

        return resultString;
    }

    private static String doHmacSha256(String origin, String skey) {

        String resultString = null;

        try {
            SecretKeySpec signingKey = new SecretKeySpec(skey.getBytes(IntaPayConstants.ENCODE_UTF8), SignUtil.ENCRYPTION_TYPE_HMACSHA256);
            Mac mac = Mac.getInstance(SignUtil.ENCRYPTION_TYPE_HMACSHA256);
            mac.init(signingKey);
            resultString = byte2hex(mac.doFinal(origin.getBytes(IntaPayConstants.ENCODE_UTF8)));
        } catch (Exception e) {
            resultString = "";
        }

        return resultString;
    }

    private static String byte2hex(byte[] bytes) {
        StringBuilder hs = new StringBuilder();
        for (int n = 0; bytes != null && n < bytes.length; n++) {
            String tmp = Integer.toHexString(bytes[n] & 0XFF);
            if (tmp.length() == 1) {
                hs.append(CHAR_ZERO);
            }
            hs.append(tmp);
        }
        return hs.toString();
    }

    private static String byteArrayToHexString(byte[] bytes) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aByte : bytes) {
            resultSb.append(byteToHexString(aByte));
        }

        return resultSb.toString();
    }

    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n += 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    public static String decriptMchId(String str, String pw) {

        String returnStr = null;

        try {
            Cipher cipher = Cipher.getInstance(SignUtil.ENCRYPTION_TYPE_RC4);
            SecretKeySpec key = new SecretKeySpec(pw.getBytes(IntaPayConstants.ENCODE_UTF8), SignUtil.ENCRYPTION_TYPE_RC4);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] data = cipher.update(DatatypeConverter.parseHexBinary(str));
            returnStr = new String(data, IntaPayConstants.ENCODE_UTF8);
        } catch (Exception e) {
            returnStr = "";
        }

        return returnStr;
    }

    public static String encriptMchId(String str, String pw) {

        String returnStr = null;

        try {
            Cipher cipher = Cipher.getInstance(SignUtil.ENCRYPTION_TYPE_RC4);
            SecretKeySpec key = new SecretKeySpec(pw.getBytes(IntaPayConstants.ENCODE_UTF8), SignUtil.ENCRYPTION_TYPE_RC4);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] data = cipher.update(str.getBytes(IntaPayConstants.ENCODE_UTF8));
            returnStr = DatatypeConverter.printHexBinary(data);
        } catch (Exception e) {
            returnStr = "";
        }

        return returnStr;
    }

}
