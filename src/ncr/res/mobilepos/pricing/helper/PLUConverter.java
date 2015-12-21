/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.pricing.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.WebServerGlobals;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.systemconfiguration
          .resource.SystemConfigurationResource;

import org.jsoup.helper.StringUtil;


/**
 * PLUConverter is the class which convert the plucode and the item.
 */
public class PLUConverter {
	
	/*
	 *  Logging handler.
	 */
   private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger

   private static final String PROG_NAME = "PLUConverter";

    /**
     * PLU length.
     */
    private static final int PLU_BASE_LEN = 13;
    /**
     * Velocity maximum length.
     */
    private static final int VELOCITY_MAX_LEN = 5;

    /**
     * Three.
     */
    private static final int THREE = 3;
    /**
     * Four.
     */
    private static final int FOUR = 4;
    /**
     * Five.
     */
    private static final int FIVE = 5;
    /**
     * Six.
     */
    private static final int SIX = 6;
    /**
     * Ten.
     */
    private static final int TEN = 10;
    /**
     * Twelve.
     */
    private static final int TWELEVE = 12;
    /**
     * Thirteen.
     */
    private static final int THIRTEEN = 13;

    /**
     * Store tag list.
     */
    @SuppressWarnings("serial")
    private final Map<String, Integer> inStoreTagList =
        new HashMap<String, Integer>() { {
			put("02", TEN);
			put("20", TEN);
			put("21", TEN);
			put("22", TEN);
			put("23", TEN);
			put("24", TEN);
			put("25", TEN);
			put("26", TEN);
			put("27", TEN);
			put("28", SIX);
			put("29", FIVE);
    } };
    /**
     * Odd Weight.
     */
    private static final int ODD_WEIGHT = 1;
    /**
     * Even Weight.
     */
    private static final int EVEN_WEIGHT = 3;
    /**
     * Modulus.
     */
    private static final int MODULUS = 10;
    /**
     * Store Flag.
     */
    private boolean isInStoreFlg = false;
    /**
     * SKU Digits.
     */
    private int skuDigits = 0;

    /**
     *  ServletContext.
     */
    private ServletContext context;

    /**
     * Constructor.
     */
    public PLUConverter() {
    }

    /**
     * Set ServletContext.
     *
     * @param contextToSet   ServletContext
     */
    public final void setContext(final ServletContext contextToSet) {
        this.context = contextToSet;
    }

    /**
     * Convert plucode.
     *
     * @param pluCode       Item's plu code
     * @return Converted plucode
     */
    public final String convertPluCode(final String pluCode) {

        StringBuilder sb = new StringBuilder(pluCode);
        int len = pluCode.length();
        int velocityZeroNum = PLU_BASE_LEN - VELOCITY_MAX_LEN - 1;

        // nothing is done
        if (!StringUtil.isNumeric(pluCode) || PLU_BASE_LEN < len) {
            return pluCode;
        }

        // velocity for manual-input
        if (VELOCITY_MAX_LEN >= len) {
            // zero-padding to 12 digits
            sb.insert(0, getZero(PLU_BASE_LEN - len - 1));
            // add check-digit
            sb.append(getCD(sb.toString()));
        } else if (PLU_BASE_LEN > len) {
            // zero-padding to 13 digits
            sb.insert(0, getZero(PLU_BASE_LEN - len));
        }

        // velocity
        if (getZero(velocityZeroNum).equals(sb.substring(0, velocityZeroNum))) {            
        } else if (getZero(SIX).equals(sb.substring(0, SIX))) {
            // UPC-E (1th-6th digit is zero)
            // convert for UPC-E (7th-12th digit)
            sb.replace(0, sb.length(), convertUPCE(sb.substring(SIX, TWELEVE)));
        } else if (isInStore(pluCode)) {
            // in-store
            isInStoreFlg = true;
            skuDigits = getSkuDigits(pluCode);
            // price-on
            if (skuDigits < TEN) {
                // replace price with zero
                sb = sb.replace(2 + skuDigits , TWELEVE,
                        getZero(TEN - skuDigits));
                // reset check-digit
                sb = sb.replace(TWELEVE, THIRTEEN,
                        getCD(sb.substring(0, TWELEVE)));
            }
        }

        return sb.toString();
    }

    /**
     * Convert Item.
     *
     * @param pluCode   Item's plucode
     * @param item      Item
     */
    public final void convertItem(final String pluCode, final Item item) {

        int len = pluCode.length();
        double price = 0;

        // nothing is done
        if (!StringUtil.isNumeric(pluCode) || PLU_BASE_LEN < len) {
            return;
        }

        // in-store
        if (isInStoreFlg) {
            // reset plucode
            item.setItemId(pluCode);
            // price-on
            if (skuDigits < TEN) {
                // set price on label
                price = Double.parseDouble(
                        pluCode.substring(2 + skuDigits, TWELEVE));
                item.setRegularSalesUnitPrice(price);
                item.setActualSalesUnitPrice(price);
            }
        }

        // reset plucode (temporary processing)
        item.setItemId(pluCode);

        return;
    }

    /**
     * Convert UPC-E.
     *
     * @param pluCode (6 digits)
     * @return Converted plucode
     */
    private String convertUPCE(final String pluCode) {

        StringBuilder sb = new StringBuilder();
        String lastPluCode = pluCode.substring(FIVE, SIX);

        // 6th digit is 0-2
        if ("0".equals(lastPluCode)
                || "1".equals(lastPluCode)
                || "2".equals(lastPluCode)) {
            // 00x1x2x60000x3x4x5C
            sb.append(getZero(2));
            sb.append(pluCode.substring(0, 2));
            sb.append(pluCode.substring(FIVE, SIX));
            sb.append(getZero(FOUR));
            sb.append(pluCode.substring(2, FIVE));
            sb.append(getCD(sb.toString()));
        } else if ("3".equals(lastPluCode)) {     // 6th digit is 3
            // 00x1x2x300000x4x5C
            sb.append(getZero(2));
            sb.append(pluCode.substring(0, THREE));
            sb.append(getZero(FIVE));
            sb.append(pluCode.substring(THREE, FIVE));
            sb.append(getCD(sb.toString()));
        } else if ("4".equals(lastPluCode)) {       // 6th digit is 4
            // 00x1x2x3x400000x5C
            sb.append(getZero(2));
            sb.append(pluCode.substring(0, FOUR));
            sb.append(getZero(FIVE));
            sb.append(pluCode.substring(FOUR, FIVE));
            sb.append(getCD(sb.toString()));
        } else {  // 6th digit is 5-9
            // 00x1x2x3x4x50000x6C
            sb.append(getZero(2));
            sb.append(pluCode.substring(0, FIVE));
            sb.append(getZero(FOUR));
            sb.append(pluCode.substring(FIVE, SIX));
            sb.append(getCD(sb.toString()));
        }

        return sb.toString();
    }

    /**
     * Check whether the plucode is in-store.
     *
     * @param pluCode Item's plu code
     * @return Whether the plucode is in-store
     */
    private boolean isInStore(final String pluCode) {

        if (PLU_BASE_LEN != pluCode.length()) {
            return false;
        }

        Set<Map.Entry<String, Integer>> entrySet =
            inStoreTagList.entrySet();
        Iterator<Map.Entry<String, Integer>> it = entrySet.iterator();
        while (it.hasNext()) {
              Map.Entry<String, Integer> entry = it.next();
              String inStoreTag = entry.getKey();
              if (inStoreTag.equals(pluCode.substring(0,
                      inStoreTag.length()))) {
                  return true;
              }
        }
        return false;
    }

    /**
     * Get sku digits.
     * @param pluCode       Item's plucode
     * @return sku digits num
     */
    private int getSkuDigits(final String pluCode) {

        String[] valueArray = getInStoreParam();
        String inStoreTag = pluCode.substring(0, 2);
        int defaultSkuDigits = inStoreTagList.get(inStoreTag);
        skuDigits = defaultSkuDigits;

        for (String value : valueArray) {
            if (value == null) {
                continue;
            }
            String[] array = value.split(",");
            if (array.length < 2) {
                continue;
            }
            if (array[0].equals(inStoreTag)) {
                try {
                    skuDigits = Integer.parseInt(array[1]);
                    if (skuDigits < 0 || skuDigits > TEN) {
                        skuDigits = defaultSkuDigits;
                    }
                } catch (NumberFormatException e) {
                	LOGGER.logAlert(PROG_NAME, PROG_NAME+".getSkuDigits", Logger.RES_EXCEP_GENERAL, "Incorrect number format: "+value+". "+e.getMessage());
                }
                break;
            }
        }
        return skuDigits;
    }

    /**
     * Get in-store parameter.
     * @return in-store parameter
     */
    private String[] getInStoreParam() {

        SystemConfigurationResource systemConfigurationResource =
            new SystemConfigurationResource();
        systemConfigurationResource.setContext(context);
        WebServerGlobals webServerGlobals =
            systemConfigurationResource.getSystemConfiguration();
        List<String> valueList = new ArrayList<String>();
        valueList.add(webServerGlobals.getInStoreParam1());
        valueList.add(webServerGlobals.getInStoreParam2());
        valueList.add(webServerGlobals.getInStoreParam3());
        valueList.add(webServerGlobals.getInStoreParam4());
        valueList.add(webServerGlobals.getInStoreParam5());
        valueList.add(webServerGlobals.getInStoreParam6());
        valueList.add(webServerGlobals.getInStoreParam7());
        valueList.add(webServerGlobals.getInStoreParam8());
        valueList.add(webServerGlobals.getInStoreParam9());
        valueList.add(webServerGlobals.getInStoreParam10());
        valueList.add(webServerGlobals.getInStoreParam11());
        return (String[]) valueList.toArray(new String[valueList.size() + 1]);
    }

    /**
     * Get num digits zero.
     * @param num   Number
     * @return String which have designated digits
     */
    private String getZero(final int num) {

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < num; i++) {
            sb.append("0");
        }
        return sb.toString();
    }

    /**
     * Get Check-Digit.
     * @param pluCode (12 digits)
     * @return Check-Digit
     */
    private String getCD(final String pluCode) {

        int sum = 0;

        for (int i = 0; i < pluCode.length(); i++) {
            if (((i + 1) & 1) == 1) {
                /* odd digit */
                sum += Integer.parseInt(pluCode.substring(i, i + 1))
                        * ODD_WEIGHT;
            } else {
                /* even digit */
                sum += Integer.parseInt(pluCode.substring(i, i + 1))
                        * EVEN_WEIGHT;
            }
        }

        if (sum % MODULUS == 0) {
            return "0";
        } else {
            return Integer.toString(MODULUS - (sum % MODULUS));
        }
    }
}
