package ncr.res.mobilepos.helper;

import java.math.BigDecimal;

/**
 * The Class RoundingUtility.
 */
public final class RoundingUtility {

    /** Default Constructor. */
    private RoundingUtility() {    	
    }

    /**
     * Rounds unrounded value to given precision and rounding mode.
     *
     * @param unrounded
     *            the unrounded
     * @param precision
     *            the precision
     * @param roundingMode
     *            the rounding mode
     * @return the double
     */
    public static double round(final double unrounded, final int precision,
            final int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
}
