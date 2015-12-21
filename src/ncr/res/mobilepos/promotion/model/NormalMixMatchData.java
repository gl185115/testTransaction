package ncr.res.mobilepos.promotion.model;

/**
 * MixMatchData Model Object.
 *
 * Encapsulates the Mix and Match Data for Promotion information.
 * @author cc185102
 */
public class NormalMixMatchData extends MixMatchData {
    /** The Must Buy Flag. */
    private int mustBuyFlag;
    /** An array of Quantity. */
    private int[] quantity;
    /** An Array of Discount Price.*/
    private long[] discountprice;
    /** The Price for Employee type 1. */
    private long[] empprice1;
    /** The Price for Employee type 2. */
    private long[] empprice2;
    /** The Price for Employee type 3. */
    private long[] empprice3;
    /** The flag for Must buy Mix and Match. */
    public static final int MUST_BUY_MIX_MATCH = 1;
    /**
     * @return the mustBuyFlag
     */
    public final int getMustBuyFlag() {
        return mustBuyFlag;
    }
    /**
     * @param mustBuyFlagToSet the mustBuyFlag to set
     */
    public final void setMustBuyFlag(final int mustBuyFlagToSet) {
        this.mustBuyFlag = mustBuyFlagToSet;
    }
    /**
     * @return the quantity
     */
    public final int[] getQuantity() {
        return quantity.clone();
    }
    /**
     * @param quantityToSet the quantity to set
     */
    public final void setQuantity(final int[] quantityToSet) {
        this.quantity = quantityToSet.clone();
    }
    /**
     * @return the discountprice
     */
    public final long[] getDiscountprice() {
        return discountprice.clone();
    }
    /**
     * @param discountpriceToSet the discountprice to set
     */
    public final void setDiscountprice(final long[] discountpriceToSet) {
        this.discountprice = discountpriceToSet.clone();
    }
    /**
     * @return the empprice1
     */
    public final long[] getEmpprice1() {
        return empprice1.clone();
    }
    /**
     * @param empprice1ToSet the empprice1 to set
     */
    public final void setEmpprice1(final long[] empprice1ToSet) {
        this.empprice1 = empprice1ToSet.clone();
    }
    /**
     * @return the empprice2
     */
    public final long[] getEmpprice2() {
        return empprice2.clone();
    }
    /**
     * @param empprice2ToSet the empprice2 to set
     */
    public final void setEmpprice2(final long[] empprice2ToSet) {
        this.empprice2 = empprice2ToSet.clone();
    }
    /**
     * @return the empprice3
     */
    public final long[] getEmpprice3() {
        return empprice3.clone();
    }
    /**
     * @param empprice3ToSet the empprice3 to set
     */
    public final void setEmpprice3(final long[] empprice3ToSet) {
        this.empprice3 = empprice3ToSet.clone();
    }
}
