package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Discount Model Object.
 *
 * Encapsulates the Discount Promotion information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Discount")
public class Discount {
    /**
     * The RewardID.
     */
    @XmlElement(name = "RewardID")
    private int rewardID;
    /**
     * The Earned Reward ID.
     */
    @XmlElement(name = "EarnedRewardID")
    private String earnedRewardID;
    /**
     * The Promotion Code.
     */
    @XmlElement(name = "PromotionCode")
    private String promotionCode;
    /**
     * The Discount Description.
     */
    @XmlElement(name = "DiscountDescription")
    private String discountDescription;
    /**
     * The Item Entry ID.
     */
    @XmlElement(name = "ItemEntryID")
    private String itemEntryID;
    /**
     * The Unit Discount Amount.
     */
    @XmlElement(name = "UnitDiscountAmt")
    private long unitDiscountAmount;
    /**
     * The Quantity of the Sale from a bundle.
     */
    @XmlElement(name = "Quantity")
    private int quantity;
    /**
     * @return the rewardID
     */
    public final int getRewardID() {
        return rewardID;
    }
    /**
     * @param rewardIDToSet the rewardID to set
     */
    public final void setRewardID(final int rewardIDToSet) {
        this.rewardID = rewardIDToSet;
    }
    /**
     * @return the earnedRewardID
     */
    public final String getEarnedRewardID() {
        return earnedRewardID;
    }
    /**
     * @param earnedRewardIDToSet the earnedRewardID to set
     */
    public final void setEarnedRewardID(final String earnedRewardIDToSet) {
        this.earnedRewardID = earnedRewardIDToSet;
    }
    /**
     * @return the promotionCode
     */
    public final String getPromotionCode() {
        return promotionCode;
    }
    /**
     * @param promotionCodeToSet the promotionCode to set
     */
    public final void setPromotionCode(final String promotionCodeToSet) {
        this.promotionCode = promotionCodeToSet;
    }
    /**
     * @return the discountDescription
     */
    public final String getDiscountDescription() {
        return discountDescription;
    }
    /**
     * @param discountDescriptionToSet the discountDescription to set
     */
    public final void setDiscountDescription(
            final String discountDescriptionToSet) {
        this.discountDescription = discountDescriptionToSet;
    }
    /**
     * @return the itemEntryID
     */
    public final String getItemEntryID() {
        return itemEntryID;
    }
    /**
     * @param itemEntryIDToSet the itemEntryID to set
     */
    public final void setItemEntryID(final String itemEntryIDToSet) {
        this.itemEntryID = itemEntryIDToSet;
    }
    /**
     * @return the unitDiscountAmount
     */
    public final long getUnitDiscountAmount() {
        return unitDiscountAmount;
    }
    /**
     * @param unitDiscountAmountToSet the unitDiscountAmount to set
     */
    public final void setUnitDiscountAmount(final long unitDiscountAmountToSet) {
        this.unitDiscountAmount = unitDiscountAmountToSet;
    }

    /**
     * The capacity for accepting overflow discounts.
     * This is used to keep track of how much more discount
     * can be placed in the item before it exceeds the limit.
     */
    private int amountRemaining = 0;
    /**
     * Convert to string.
     * @return String
     */
    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
    /**
     * sets the amount remaining.
     * @param amountRemainingToSet
     *  capacity for accepting overflow discounts
     */
    public final void setAmountRemaining(final int amountRemainingToSet) {
        this.amountRemaining = amountRemainingToSet;
    }
    /**
     * gets the amount remaining.
     * @return the capacity for accepting overflow discounts
     */
    public final int getAmountRemaining() {
        return amountRemaining;
    }
    /**
     * @return the quantity
     */
    public final int getQuantity() {
        return quantity;
    }
    /**
     * @param quantityToSet the quantity to set
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
    }
}
