package ncr.res.mobilepos.networkreceipt.model;

/**
 * class that handles settlement management.
 *
 */
public class SettlementManagement {
    /**
     * the date of the transaction.
     */
    private String date;
    /**
     * the store id.
     */
    private String storeId;
    /**
     * the terminal id.
     */
    private String terminalId;
    /**
     * the sequence number.
     */
    private String seqNo;
    /**
     * the brand.
     */
    private String brand;
    /**
     * credit company code.
     */
    private String crCompanyCode;
    /**
     * credit amount.
     */
    private String amount;
    /**
     * credit card number.
     */
    private String pan;
    /**
     * expiry date of the credit card.
     */
    private String expiry;
    /**
     * the method of payment.
     */
    private String paymentMethod;
    /**
     * settlement number.
     */
    private String settlementNum;

    /**
     * class instance of weight1.
     */
    private static final int[] WEIGHT1 = {2, 3, 4, 5, 6, 7,
                                    2, 3, 4, 5, 6, 7,
                                    2, 3, 4, 5, 6, 7,
                                    2, 3, 4, 5, 6, 7,
                                    2, 3, 4, 5, 6, 7,
                                    2, 3, 4, 5, 6, 7
                                    };
    /**
     * class instance of weight2.
     */
    private static final int[] WEIGHT2 = {1, 2, 1, 2, 1, 2,
                                    1, 2, 1, 2, 1, 2,
                                    1, 2, 1, 2, 1, 2,
                                    1, 2, 1, 2, 1, 2,
                                    1, 2, 1, 2, 1, 2,
                                    1, 2, 1, 2, 1, 2
                                    };
    /**
     * class instance of weight 3.
     */
    private static final int[] WEIGHT3 = {1, 3, 1, 3, 1, 3,
                                    1, 3, 1, 3, 1, 3,
                                    1, 3, 1, 3, 1, 3,
                                    1, 3, 1, 3, 1, 3,
                                    1, 3, 1, 3, 1, 3,
                                    1, 3, 1, 3, 1, 3
                                    };
    /**
     * class instance of modulus1.
     */
    private int modulus1 = 11;
    /**
     * class instance of modulus2.
     */
    private int modulus2 = 10;
    /**
     * class instance of modulus3.
     */
    private int modulus3 = 10;

    /**
     * Constructor.
     * @param dateToSet YYMMDD
     * @param storeIdToSet SSSS
     * @param terminalIdToSet TTTT
     * @param seqNoToSet XXXXX
     * @param brandToSet PP
     * @param crCompanyCodeToSet credit card company code
     * @param amountToSet credit amount
     * @param panToSet credit card number
     * @param expiryToSet credit card expiry date
     * @param paymentMethodToSet payment method
     */
    public SettlementManagement(final String dateToSet,
            final String storeIdToSet, final String terminalIdToSet,
            final String seqNoToSet, final String brandToSet,
            final String crCompanyCodeToSet, final String amountToSet,
            final String panToSet, final String expiryToSet,
            final String paymentMethodToSet) {
        this.date = dateToSet;
        this.storeId = chageStrToFixDigit(storeIdToSet, 4);
        this.terminalId = chageStrToFixDigit(terminalIdToSet, 4);
        this.seqNo = chageStrToFixDigit(seqNoToSet, 5);
        this.brand = brandToSet;
        this.crCompanyCode = chageStrToFixDigit(crCompanyCodeToSet, 2);
        this.amount = chageStrToFixDigit(amountToSet, 9);
        this.pan = panToSet;
        this.expiry = chageStrToFixDigit(expiryToSet, 4);
        this.paymentMethod = chageStrToFixDigit(paymentMethodToSet, 2);
    }

    /**
     * @return settlementNum
     */
    public final String getSettlementNum() {
        StringBuilder sb = new StringBuilder();
        sb.append(date);
        sb.append(storeId);
        sb.append(terminalId);
        sb.append(seqNo);
        sb.append(brand);

        //Get check digit 1
        String checkDigitTarget1 =
            crCompanyCode + amount + pan + expiry + paymentMethod;
        String checkDigit1 =
            getCheckDigit(checkDigitTarget1, WEIGHT1, modulus1);

        //Get check digit 2
        String checkDigitTarget2 = checkDigitTarget1;
        String checkDigit2 =
            getCheckDigit(checkDigitTarget2, WEIGHT2, modulus2);

        //Get check digit 3
        String checkDigitTarget3 =
            date + storeId + terminalId + seqNo
            + brand + checkDigit1 + checkDigit2;
        String checkDigit3 =
            getCheckDigit(checkDigitTarget3, WEIGHT3, modulus3);

        if ("".equals(checkDigit1)
                || "".equals(checkDigit2) || "".equals(checkDigit3)) {
            settlementNum = "";
        } else {
            sb.append(checkDigit1).append(checkDigit2).append(checkDigit3);
            settlementNum = sb.toString();
        }

        return settlementNum;
    }

    /**
     * @param controlNo
     * @param weightArray
     * @param modulus
     * @return result
     */
    private int[] processControlNo(final String controlNo,
            final int[] weightArray, final int modulus) {
        int[] result = new int[]{0};
        //Check every digit of controlNo is number
        char[] controlNoCharArray = controlNo.toCharArray();
        int[] controlNoIntArray = new int[controlNoCharArray.length];          
        //Check controlNo if null or ""
        if (controlNo == null || "".equals(controlNo)) {
        	result[0] = -1;
        } else {
	        for (int i = 0; i < controlNoCharArray.length; i++) {
	            int tempInt = controlNoCharArray[i] - '0';
	            if (tempInt < 0 || tempInt > 9) {
	            	result[0] = -1;
	            	break;
	            } else {
	                controlNoIntArray[i] = tempInt;
	            }
	        }
        }

        //If the length of controlNo is long than
        //weightArray's length, return ""
        if (controlNoIntArray.length > weightArray.length) {
        	result[0] = -1;
        }
        
        //If modulus is 0 or minus, return ""
        if (modulus <= 0) {
        	result[0] = -1;
        }
        
        // save control number
        if (result[0] != -1) {
        	result =  new int[controlNoIntArray.length];
        	result = controlNoIntArray;
        }
        	
        return result;
    }
    
    /**
     * gets the check digit.
     * @param controlNo - control number
     * @param weightArray - weight array
     * @param modulus - modulus
     * @return the check digit
     */
    private String getCheckDigit(final String controlNo,
            final int[] weightArray, final int modulus) {
        String checkDigit = "";

        //Check every digit of controlNo is number
        char[] controlNoCharArray = controlNo.toCharArray();
        int[] controlNoIntArray = new int[controlNoCharArray.length];
        
        controlNoIntArray = processControlNo(controlNo,
                weightArray, modulus);
        // check if control number has a problem.
        if (controlNoIntArray[0] < 0) {
        	return "";
        }
        
        int sum = 0;

        //get sum
        for (int i = 0; i < controlNoIntArray.length; i++) {
            sum += controlNoIntArray[i] * weightArray[i];
        }
        //get remainder
        int remainder = sum % modulus;
        //get check digit
        int checkDigitInt = modulus - remainder;

        //if 2 digits, get the last 1 digit
        checkDigit = String.valueOf(checkDigitInt % 10);

        return checkDigit;
    }

    /**
     * changes the string to fixed digit.
     * @param data - the string to convert
     * @param digits - number of digits
     * @return the converted string
     */
    private String chageStrToFixDigit(final String data, final int digits) {
        if (data == null) {
            return "";
        }
        String result = "";
        int len = data.length();

        if (len > digits) {
            //if length of data is longer than digits, get last digits
            result = data.substring(len - digits);
        } else if (len < digits) {
            //if length of data is shorter than digits,
            //put the data to digits with 0
            StringBuilder sbStoreId = new StringBuilder();
            for (int i = 0; i < digits - len; i++) {
                sbStoreId.append("0");
            }
            result = sbStoreId.append(data).toString();
        } else {
            result = data;
        }

        return result;
    }
}
