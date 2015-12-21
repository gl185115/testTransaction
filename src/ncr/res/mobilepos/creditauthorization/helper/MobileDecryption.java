// Copyright(c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.creditauthorization.helper;

import java.io.ByteArrayInputStream;

import java.io.IOException;
import java.io.InputStream;
import java.security.Key;
import java.security.Security;
import java.util.Base64;
import java.util.List;
import java.util.regex.Pattern;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.creditauthorization.model.Jis1;
import ncr.res.mobilepos.creditauthorization.model.Jis1Param;
import ncr.res.mobilepos.creditauthorization.model.Jis2;
import ncr.res.mobilepos.creditauthorization.model.Jis2Param;
import ncr.res.mobilepos.creditauthorization.resource
            .CreditAuthorizationResource;

/**
 * The Class MobileDecryption.
 */
/**
 * The Class MobileDecryption.
 */
public class MobileDecryption {

    /** The context. */
    private ServletContext context;

    /** The card input type. */
    private String cardInputType;

    /** The credit card's pan. */
    private String pan;

    /** The credit card's number in pan4. */
    private String pan4;

    /** The credit card's number in pan6. */
    private String pan6;

    /** The credit card's expiration date. */
    private String expirationDate;

    /** The jis1data. */
    private String jis1data;

    /** The jis2data. */
    private String jis2data;

    /** The jis2data information. */
    private String jis2dataInformation;

    /** The credit company name. */
    private String creditCompanyName;

    /** The credit company code. */
    private String creditCompanyCode;

    /** Card Input Type Jis1. */
    private static final String CARDINPUTTYPE_JIS1 = "1";

    /** The digits of Expiration. */
    private static final int EXPIRATION_DIGITS = 4;

    /** position of jis1 data. */
    private static final int JIS1DATA_POSITION = 1;

    /** position of jis2 data. */
    private static final int JIS2DATA_POSITION = 1;

    /** digits of jis2 data. */
    private static final int JIS2DATA_DIGITS = 69;

    /** position of ID mark in jis2 data. */
    private static final int JIS2DATA_IDMARK_POSITION = 1;

    /** digits of ID mark in jis2 data. */
    private static final int JIS2DATA_IDMARK_DIGITS = 1;

    /** position of category code in jis2 data. */
    private static final int JIS2DATA_CATEGORYCODE_POSITION = 2;

    /** digits of category code in jis2 data. */
    private static final int JIS2DATA_CATEGORYCODE_DIGITS = 1;

    /** position of corp code in jis2 data. */
    private static final int JIS2DATA_CORPCODE_POSITION = 7;

    /** digits of corp code in jis2 data. */
    private static final int JIS2DATA_CORPCODE_DIGITS = 4;

    /**
     * The maximum track2.
     */
    private static final int MAX_TRACK2 = 39;
    /**
     * The mark of start of track2.
     */
    private static final int START_TRACK2 = 0xF2;
    /**
     * The end index of track2.
     */
    private static final int END_TRACK2 = 0x3F;
    /**
     * The start index of track2 checking.
     */
    private static final int START_TRACK2_CHECKING = 20;
    /**
     * The 512 bytes value.
     */
    private static final int BYTES_512 = 512;

    /**
     * Constructor.
     *
     * @param cardData
     *            the new card data
     * @param contxt
     *            the new context
     * @param companyId
     *            the company id
     * @param storeId
     *            the store id
     * @throws Exception
     *            thrown when process fails.
     */
    public MobileDecryption(final String cardData,
            final ServletContext contxt,
            final String companyId,
            final String storeId) throws MobileDecryptException {
        // Set the ServletContext
        this.context = contxt;
        // decode by base64 and decrypt by AES256
        byte[] decrypteddata;
		try {
			decrypteddata = decrypt(cardData, companyId, storeId);
			// get track2 data of jis1
	        byte[] track2 = getTrack2OfJis1(decrypteddata);
	        if (track2 == null) {
	            throw new MobileDecryptException("Can not get Track2 data.");
	        }
	     // initialize the variable
	        init();

	        // this time ,only jis1
	        this.cardInputType = CARDINPUTTYPE_JIS1;
	        String decryptedStr = new String(track2);

	        // Get the parameter file for JIS data
	        if ((decryptedStr != null) && (!decryptedStr.isEmpty())
	                && (!setJis1Data(decryptedStr))) {            
	                throw new MobileDecryptException("This card is not supported.");            
	        }

		} catch (Exception e) {
			throw new MobileDecryptException("This card is not supported.", e); 
		}
        

            }

    /**
     * decode by base64 and decrypt by AES256.
     *
     * @param cardData
     *            the card data
     * @param companyId
     *            the company id
     * @param storeId
     *            the store id
     * @return the byte[]
     * @throws Exception
     *             the exception
     */
    private byte[] decrypt(final String cardData, final String companyId, 
            final String storeId) throws MobileDecryptException {
    	byte[] decrypteddata = new byte[BYTES_512];
    	InputStream is = null;
    	try{
        // decode the card date by base64
        byte[] encryptedData = (Base64.getDecoder()).decode(cardData);
        is = new ByteArrayInputStream(encryptedData);

        Key key = getAESKey(context, companyId, storeId);
        Security.addProvider(
                new org.bouncycastle.jce.provider.BouncyCastleProvider());
        Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding", "BC");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(
                new byte[cipher.getBlockSize()]));

        byte[] cipherBlock = new byte[cipher.getOutputSize(cipher
                .getBlockSize() * 100)];
        byte[] buffer = new byte[16];
        int noBytes = 0;
        int cipherBytes = 0;
        
        int index = 0;
        while ((noBytes = is.read(buffer)) != -1) {
            cipherBytes = cipher.update(buffer, 0, noBytes, cipherBlock);
            System.arraycopy(cipherBlock, 0, decrypteddata, index, cipherBytes);
            index = index + cipherBytes;
        }
        cipher.doFinal(cipherBlock, 0);
        System.arraycopy(cipherBlock, 0, decrypteddata, index, cipherBytes);
        is.close();
    	}catch (IOException e) {
			throw new MobileDecryptException("Error on data decryption.", e); 
		}catch(Exception e){
    		throw new MobileDecryptException("Error on data decryption.", e); 
    	}finally{
    		//do nothing
    	}
        return decrypteddata;
    }

    /**
     * Gets track2 data of jis1.
     *
     * @param data
     *            the data
     * @return the track2 of jis1
     */
    private byte[] getTrack2OfJis1(final byte[] data) {
        int idxStart = 0;
        int idxEnd = 0;
        for (int i = START_TRACK2_CHECKING; i < data.length; i++) {
            // mark of start of track2 is 0xF2
            if (data[i] == (byte) START_TRACK2) {
                idxStart = i;
                break;
            }
        }
        // If there is no 0xF2, assert that this data is wrong
        if (idxStart == 0) {
            return new byte[0];
        } else {
            // get the end index of track2 data
            for (int i = idxStart; i < data.length; i++) {
                if (data[i] == (byte) END_TRACK2) {
                    idxEnd = i;
                    break;
                }
            }
        }
        // If there is no 0x3F, assert that this data is wrong
        if (idxEnd == 0) {
            return new byte[0];
        }
        // Max of track2 data is 39 (from ";" to "?")
        if (idxEnd - idxStart > MAX_TRACK2) {
            return new byte[0];
        }

        byte[] track2 = new byte[idxEnd - idxStart];

        // get the track2 data
        System.arraycopy(data, idxStart + 1, track2, 0, track2.length);

        return track2;
    }

    /**
     * Initialize class instance variables.
     */
    private void init() {
        // decode the card date by base64
        this.cardInputType = "";
        this.pan = "";
        this.pan4 = "";
        this.pan6 = "";
        this.expirationDate = "";
        this.jis1data = "";
        this.jis2data = "";
        this.jis2dataInformation = "";
        this.creditCompanyName = "";
        this.creditCompanyCode = "";
    }

    /**
     * Checks the decrypted data.
     *
     * @param str
     *            the string to check
     * @return true, if successful
     */
    @SuppressWarnings("unused")
    private boolean checkNoAlpha(final String str) {
        Pattern p = Pattern.compile("[^a-zA-Z0-9]");
        return p.matcher(str).find();
    }

    /**
     * Check number only.
     *
     * @param str
     *            the string number to check
     * @return true, if successful
     */
    @SuppressWarnings("unused")
    private boolean checkNumOnly(final String str) {
        Pattern p = Pattern.compile("[^0-9]");
        return p.matcher(str).find();
    }

    /**
     * Sets jis1 data.
     *
     * @param track2
     *            the track2
     * @return true, if successful
     */
    private boolean setJis1Data(final String track2) {
        Jis1 jis1Info = null;
        Jis1Param jis1Param = (Jis1Param) context
                .getAttribute(GlobalConstant.JIS1_PARAM_KEY);
        List<Jis1> jis1s = jis1Param.getJis1s();
        for (Jis1 jis1 : jis1s) {
            int position = Integer.valueOf(jis1.getPosition());
            int digits = Integer.valueOf(jis1.getDigits());
            long companycodebegin = Long.valueOf(subString(
                    jis1.getCompanycodebegin(), 0, digits));
            long companycodeend = Long.valueOf(subString(
                    jis1.getCompanycodeend(), 0, digits));
            long cardInfo = Long.valueOf(subString(track2, position, digits));

            if (cardInfo >= companycodebegin && cardInfo <= companycodeend) {
                jis1Info = jis1;
                break;
            }
        }
        if (jis1Info == null) {
            return false;
        }
        // set the pan number
        int position1 = Integer.valueOf(jis1Info.getPosition1());
        int digits1 = Integer.valueOf(jis1Info.getDigits1());
        this.pan = subString(track2, position1, digits1);
        this.pan4 = getLast4digits(this.pan);
        this.pan6 = getBegin6Last4Digits(this.pan);
        // set the expiration
        int expirationposition = Integer.valueOf(jis1Info
                .getExpirationposition());
        this.expirationDate = subString(track2, expirationposition,
                EXPIRATION_DIGITS);
        // set the company name
        this.creditCompanyName = jis1Info.getCompanyinfo();
        // set the company code
        this.creditCompanyCode = jis1Info.getCrcompanycode();
        // set the jis1 data (37digits)
        this.jis1data = subString(track2, JIS1DATA_POSITION,
                track2.length() - 2);
        return true;
    }

    /**
     * Sets jis2 data. this time, do not use jis2.
     *
     * @param decryptedData
     *            the decrypted data
     * @return true, if successful
     */
    @SuppressWarnings("unused")
    private boolean setJis2Data(final String decryptedData) {
        // get id mark from jis2 data
        String idmark = subString(decryptedData, JIS2DATA_IDMARK_POSITION,
                JIS2DATA_IDMARK_DIGITS);
        // get category code from jis2 data
        String categorycode = subString(decryptedData,
                JIS2DATA_CATEGORYCODE_POSITION, JIS2DATA_CATEGORYCODE_DIGITS);
        // get copr code from jis2 data
        String corpcode = subString(decryptedData, JIS2DATA_CORPCODE_POSITION,
                JIS2DATA_CORPCODE_DIGITS);

        Jis2 jis2Info = null;
        Jis2Param jis2Param = (Jis2Param) context
                .getAttribute(GlobalConstant.JIS2_PARAM_KEY);
        List<Jis2> jis2s = jis2Param.getJis2s();
        for (Jis2 jis2 : jis2s) {
            // get id mark from jis2 parameter
            String idmarkParam = jis2.getIdmark();
            // get category code from jis2 parameter
            String categorycodeParam = jis2.getCategorycode();
            // get corp code from jis2 parameter
            String corpcodeParam = jis2.getCorpcode();
            // get position from jis2 parameter
            int position = Integer.valueOf(jis2.getPosition());
            // get digits from jis2 parameter
            int digits = Integer.valueOf(jis2.getDigits());
            // get company code from jis2 parameter
            String companycodeParam = jis2.getCompanycode()
                    .substring(0, digits);

            if (idmark.equals(idmarkParam)
                    && categorycode.equals(categorycodeParam)
                    && corpcode.equals(corpcodeParam)) {
                if (digits == 0) {
                    jis2Info = jis2;
                    break;
                } else {
                    String companycode = subString(decryptedData, position,
                            digits);
                    if (companycode.equals(companycodeParam)) {
                        jis2Info = jis2;
                        break;
                    }
                }
            }
        }

        if (jis2Info == null) {
            return false;
        }
        // set the pan number
        int position1 = Integer.valueOf(jis2Info.getPosition1());
        int digits1 = Integer.valueOf(jis2Info.getDigits1());
        this.pan = subString(decryptedData, position1, digits1);
        this.pan4 = getLast4digits(this.pan);
        this.pan6 = getBegin6Last4Digits(this.pan);
        // set the expiration
        int expirationposition = Integer.valueOf(jis2Info
                .getExpirationposition());
        this.expirationDate = subString(decryptedData, expirationposition,
                EXPIRATION_DIGITS);
        // set the company name
        this.creditCompanyName = jis2Info.getCompanyinfo();
        // set the company code
        this.creditCompanyCode = jis2Info.getCrcompanycode();
        // set jis2 data
        this.jis2data = subString(decryptedData, JIS2DATA_POSITION,
                JIS2DATA_DIGITS);
        this.jis2dataInformation = idmark + categorycode + corpcode;
        return true;
    }

    /**
     * Sub string.
     *
     * @param data
     *            the data
     * @param beginIndex
     *            the begin index
     * @param digits
     *            the digits
     * @return the string
     */
    private String subString(final String data, final int beginIndex,
            final int digits) {
        if (beginIndex > data.length() || beginIndex + digits > data.length()) {
            return data;
        }
        return data.substring(beginIndex, beginIndex + digits);
    }

    /**
     * Gets the last 4 digits.
     *
     * @param data
     *            the data
     * @return the last4digits
     */
    private String getLast4digits(final String data) {
        if (data == null || "".equals(data) || data.length() < 4) {
            return "";
        }

        int len = data.length();
        String last4 = data.substring(len - 4);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len - 4; i++) {
            sb.append("*");
        }
        sb.append(last4);
        return sb.toString();
    }

    /**
     * Gets the begin 6 digits and last 4 digits.
     *
     * @param data
     *            the data
     * @return the begin6 last4 digits
     */
    private String getBegin6Last4Digits(final String data) {
        if (data == null || "".equals(data) || data.length() < 4 + 6) {
            return "";
        }
        int len = data.length();
        String last4 = data.substring(len - 4);
        String begin6 = data.substring(0, 6);
        StringBuilder sb = new StringBuilder();
        sb.append(begin6);
        for (int i = 0; i < len - 4 - 6; i++) {
            sb.append("*");
        }
        sb.append(last4);
        return sb.toString();
    }

    /**
     * Gets the cipher key.
     *
     * @param context
     *            the context
     * @param companyId
     *            the company id
     * @param storeId
     *            the store id
     * @return the aES key
     * @throws IOException
     *             Signals that an I/O exception has occurred.
     */
    public static Key getAESKey(final ServletContext context, final String companyId, final String storeId)
            throws IOException {
        CreditAuthorizationResource creditAuthRes =
            new CreditAuthorizationResource();
        creditAuthRes.setContext(context);
        ncr.res.mobilepos.creditauthorization.model.Key aesKey = creditAuthRes
                .getAesKey(companyId, storeId);

        return new SecretKeySpec(Base64.getDecoder().decode(aesKey.getKey64()), "AES");
    }

    /**
     * Clear aes key.
     *
     * @return true, if successful
     */
    public final boolean clearAESKey() {
        return true;
    }

    /**
     * Gets the card input type.
     *
     * @return cardInputType
     */
    public final String getCardInputType() {
        return cardInputType;
    }

    /**
     * Gets the pan.
     *
     * @return pan
     */
    public final String getPan() {
        return pan;
    }

    /**
     * Gets the pan4.
     *
     * @return pan4
     */
    public final String getPan4() {
        return pan4;
    }

    /**
     * Gets the pan6.
     *
     * @return pan6
     */
    public final String getPan6() {
        return pan6;
    }

    /**
     * Gets the expiration date.
     *
     * @return expirationDate
     */
    public final String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Gets the jis1data.
     *
     * @return jis1data
     */
    public final String getJis1data() {
        return jis1data;
    }

    /**
     * Gets the jis2data.
     *
     * @return jis2data
     */
    public final String getJis2data() {
        return jis2data;
    }

    /**
     * Gets the jis2data information.
     *
     * @return jis2dataInformation
     */
    public final String getJis2dataInformation() {
        return jis2dataInformation;
    }

    /**
     * Gets the credit company name.
     *
     * @return creditCompanyName
     */
    public final String getCreditCompanyName() {
        return creditCompanyName;
    }

    /**
     * Gets the credit company code.
     *
     * @return creditCompanyCode
     */
    public final String getCreditCompanyCode() {
        return creditCompanyCode;
    }
}
