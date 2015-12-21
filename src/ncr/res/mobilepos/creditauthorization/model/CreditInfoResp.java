package ncr.res.mobilepos.creditauthorization.model;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Class CreditInfoResp.
 */
public class CreditInfoResp extends ResultBase {

    /** The credit card's number in pan4. */
    private String pan4;

    /** The credit card's expiration date. */
    private String expiration;

    /**
     * Gets the pan4.
     *
     * @return pan4
     */
    public final String getPan4() {
        return pan4;
    }

    /**
     * Sets the pan4.
     *
     * @param pan4ToSet
     *            the new pan4
     */
    public final void setPan4(final String pan4ToSet) {
        this.pan4 = pan4ToSet;
    }

    /**
     * Gets the expiration.
     *
     * @return expiration
     */
    public final String getExpiration() {
        return expiration;
    }

    /**
     * Sets the expiration.
     *
     * @param exp
     *            the new expiration
     */
    public final void setExpiration(final String exp) {
        this.expiration = exp;
    }

}
