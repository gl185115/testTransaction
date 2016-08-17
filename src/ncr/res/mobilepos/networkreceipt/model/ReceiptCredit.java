/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

/**
 * ReceiptCredit Class is a Model representation of the
 * information which identifies the Credit.
 */
public class ReceiptCredit {
    /**
     * credit card number.
     */
    private String pan;
    /**
     * credit card expiration date.
     */
    private String expirationdate;
    /**
     * @return pan
     */
    public final String getPan() {
        return pan;
    }
    /**
     * @param panToSet  pan
     */
    public final void setPan(final String panToSet) {
        this.pan = panToSet;
    }
    /**
     * @return expirationdate
     */
    public final String getExpirationdate() {
        return expirationdate;
    }
    /**
     * @param expirationdateToSet  expirationdate
     */
    public final void setExpirationdate(final String expirationdateToSet) {
        this.expirationdate = expirationdateToSet;
    }
}
