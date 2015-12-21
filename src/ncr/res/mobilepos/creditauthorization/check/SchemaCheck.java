package ncr.res.mobilepos.creditauthorization.check;

import java.util.regex.Pattern;
import atg.taglib.json.util.JSONException;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.pastelport.platform.CommonTx;

/**
 * The Class SchemaCheck.
 */
public final class SchemaCheck {
    /** Default Constructor. */
    private SchemaCheck() {    	
    }


    /**
     * key-required-number-length required 1 true 0 false number 1 true 0 false
     * length 0 no limit.
     */
    private static String[] checkFiles = {"corpid-1-1-0", "storeid-1-1-0",
            "terminalid-1-1-4", "txid-1-1-4", "guid-1-0-0", "service-1-0-0",
            "txdatetime-0-1-14", "amount-1-1-0", "txtype-1-1-0",
            "paymentmethod-1-1-0", "taxothers-1-1-0", "approvalcode-0-1-6" };

    /**
     * check the filed.
     *
     * @param tx
     *            the tx
     * @return result
     */
    public static boolean check(final CommonTx tx) {
        Trace.Printer tp = null;
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), SchemaCheck.class);
        tp.methodEnter("check");

        boolean result = false;
        String[] checkFiled = new String[4];
        String value = null;
        try {
            for (int i = 0; i < checkFiles.length; i++) {
                checkFiled = checkFiles[i].split("-");
                try {
                    value = tx.getFieldValue(checkFiled[0]);
                    result = true;
                } catch (JSONException e) {
                    if ("1".equals(checkFiled[1])) {
                        result = false;
                        break;
                    } else {
                        continue;
                    }
                }

                if (result) {
                    result = numberCheck(checkFiled[2], value);
                }

                if (result) {
                    result = lengthCheck(checkFiled[3], value);
                }

                if (!result) {
                    break;
                }
            }
        } catch (Exception e) {
            result = false;
        }
        tp.methodExit(result);
        return result;
    }

    /**
     * check the filed number.
     *
     * @param num
     *            the num
     * @param value
     *            the value
     * @return result
     */
    private static boolean numberCheck(final String num, final String value) {
        boolean result;
        Pattern p = Pattern.compile("\\d*");
        if ("1".equals(num)) {
            result = p.matcher(value).matches();
        } else {
            result = true;
        }
        return result;
    }

    /**
     * check the length of the filed.
     *
     * @param length
     *            the length
     * @param value
     *            the value
     * @return result
     */
    private static boolean lengthCheck(final String length,
            final String value) {
        boolean result = false;
        int lengthfiled = Integer.parseInt(length);

        boolean hasEqualLength = false;
        if (value.length() == lengthfiled) {
            hasEqualLength = true;
        }

        if (lengthfiled != 0) {
            result = hasEqualLength;
        } else {
            result = true;
        }

        return result;
    }
}
