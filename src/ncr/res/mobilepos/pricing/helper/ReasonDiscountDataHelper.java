package ncr.res.mobilepos.pricing.helper;

import ncr.res.mobilepos.helper.StringUtility;

/**
 * Helper Class for Reason Discount Data.
 * @author VR185019
 *
 */
public class ReasonDiscountDataHelper {
    /** The Default Constructor. */
    public ReasonDiscountDataHelper() {
    }
    /**
     * Check data entry if valid.
     * @param amount, percentage
     * @return true/false
     */
    public static boolean isDataError(final String amount, final String percentage) {
    	if (amount == null || percentage == null) {
            return false;
        }
    	if (!StringUtility.isNumberFormatted(amount)
                              || !StringUtility.isNumberFormatted(percentage)) {
            return false;
        }
        if (amount.trim().length() > 0 && percentage.trim().length() > 0) {
            return false;
        }        
        return true;
    }
}
