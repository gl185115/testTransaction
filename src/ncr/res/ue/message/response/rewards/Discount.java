package ncr.res.ue.message.response.rewards;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Dicount model.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Discount")
public class Discount extends RewardBase {
    /**
     * the total message length.
     * Header & terminator length + reward length
     */
    @XmlElement(name = "RewardLength")
    private int overallMessageLength;

    /**
     * gets the overall message length.
     * @return int - the length of the message
     */
    public final int getOverallMessageLength() {
        return overallMessageLength;
    }

    /**
     * the length of the reward flag.
     */
    private static final int REWARD_FLAG_LENGTH = 1;

    /**
     * the reward flag.
     */
    @XmlElement(name = "RewardFlag")
    private String rewardFlag;

    /**
     * gets the reward flag.
     * @return String - the reward flag
     */
    public final String getRewardFlag() {
        return rewardFlag;
    }

    /**
     * the length of the discount type.
     */
    private static final int DISCOUNT_TYPE_LENGTH = 1;

    /**
     * the discount type.
     */
    @XmlElement(name = "DiscountType")
    private String discountType;

    /**
     * gets the discount type.
     * @return String - the discount type
     */
    public final String getDiscountType() {
        return discountType;
    }

    /**
     * the length of the discount level.
     */
    private static final int DISCOUNT_LEVEL_LENGTH = 1;

    /**
     * the discount level.
     */
    @XmlElement(name = "DiscountLevel")
    private String discountLevel;

    /**
     * gets the discount level.
     * @return String - the discount level
     */
    public final String getDiscountLevel() {
        return discountLevel;
    }

    /**
     * the length of the calculation type.
     */
    private static final int CALCULATION_TYPE_LENGTH = 2;

    /**
     * the calculation type.
     */
    @XmlElement(name = "CalculationType")
    private String calculationType;

    /**
     * gets the calculation type.
     * @return String - the calculation type
     */
    public final String getCalculationType() {
        return calculationType;
    }

    /**
     * the length of the flex negative.
     */
    private static final int FLEX_NEGATIVE_LENGTH = 1;

    /**
     * the flex negative.
     */
    @XmlElement(name = "FlexNegative")
    private String flexNegative;

    /**
     * gets the flex negative.
     * @return String - the flex negative
     */
    public final String getFlexNegative() {
        return flexNegative;
    }

    /**
     * the length of the item entry id.
     */
    private static final int ITEM_ENTRY_ID_LENGTH = 6;

    /**
     * the item entry id.
     */
    @XmlElement(name = "ItemEntryID")
    private String itemEntryID;

    /**
     * gets the item entry id.
     * @return String - the item entry id
     */
    public final String getItemEntryId() {
        return itemEntryID;
    }

    /**
     * the length of the discount department.
     */
    private static final int DISCOUNT_DPT_LENGTH = 4;

    /**
     * the discount department.
     */
    @XmlElement(name = "DiscountDepartment")
    private String discountDepartment;

    /**
     * gets the discount department.
     * @return String - the discount department
     */
    public final String getDiscountDepartment() {
        return discountDepartment;
    }

    /**
     * the length of the MNUP precision.
     */
    private static final int MNUP_PRECISION_LENGTH = 1;

    /**
     * the mnup precision.
     */
    @XmlElement(name = "MNUPPrecision")
    private String mnupPrecision;

    /**
     * gets the mnup precision.
     * @return String - the mnup precision
     */
    public final String getMNUPPrecision() {
        return mnupPrecision;
    }

    /**
     * the length of the match net unit price.
     */
    private static final int MATCH_NET_UNIT_PRICE_LENGTH = 10;

    /**
     * the discount level.
     */
    @XmlElement(name = "MatchNetUnitPrice")
    private String matchNetUnitPrice;

    /**
     * gets the match net unit price.
     * @return String - the match net unit price
     */
    public final String getMatchNetUnitPrice() {
        return matchNetUnitPrice;
    }

    /**
     * the length of the unit discount precision.
     */
    private static final int UNIT_DISCOUNT_PRECISION_LENGTH = 1;

    /**
     * the unit discount precision.
     */
    @XmlElement(name = "UnitDiscountPrecision")
    private String unitDiscountPrecision;

    /**
     * gets the unit discount precision.
     * @return String - the unit discount precision
     */
    public final String getUnitDiscountPrecision() {
        return unitDiscountPrecision;
    }

    /**
     * the length of the unit discount amount.
     */
    private static final int UNIT_DISCOUNT_AMT_LENGTH = 10;
    /**
     * the unit discount amount.
     */
    @XmlElement(name = "UnitDiscountAmt")
    private String unitDiscountAmt;

    /**
     * gets the unit discount amount.
     * @return String - the unit discount amount
     */
    public final String getUnitDiscountAmt() {
        return unitDiscountAmt;
    }

    /**
     * the length of the returns discount precision.
     */
    private static final int RETURNS_DISCOUNT_PRECISION_LENGTH = 1;
    /**
     * the returns discount precision.
     */
    @XmlElement(name = "ReturnsDiscountPrecision")
    private String retDiscountPrecision;

    /**
     * gets the returns discount precision.
     * @return String - the returns discount precision
     */
    public final String getRetDiscountPrecision() {
        return retDiscountPrecision;
    }

    /**
     * the length of the returns discount amount.
     */
    private static final int RETURNS_DISCOUNT_AMT_LENGTH = 10;

    /**
     * the returns discount amount.
     */
    @XmlElement(name = "ReturnsDiscountAmt")
    private String retDiscountAmt;

    /**
     * gets the returns discount amount.
     * @return String - the returns discount amount
     */
    public final String getRetDiscountAmt() {
        return retDiscountAmt;
    }

    /**
     * the length of the calculate precision.
     */
    private static final int CALCULATE_PRECISION_LENGTH = 1;

    /**
     * the calculate precision.
     */
    @XmlElement(name = "CalculatePrecision")
    private String calculatePrecision;

    /**
     * gets the calculate precision.
     * @return String - the calculate precision
     */
    public final String getCalculatePrecision() {
        return calculatePrecision;
    }

    /**
     * the length of the unit calculate amount.
     */
    private static final int UNIT_CALCULATE_AMT_LENGTH = 10;

    /**
     * the unit calculate amount.
     */
    @XmlElement(name = "UnitCalculateAmt")
    private String unitCalculateAmount;

    /**
     * gets the unit calculate amount.
     * @return String - the calculate precision
     */
    public final String getUnitCalculateAmount() {
        return unitCalculateAmount;
    }

    /**
     * the length of the quantity type.
     */
    private static final int QUANTITY_TYPE_LENGTH = 1;

    /**
     * the quantity type.
     */
    @XmlElement(name = "QuantityType")
    private String quantityType;

    /**
     * gets the quantity type.
     * @return String - the quantity type
     */
    public final String getQuantityType() {
        return quantityType;
    }

    /**
     * the length of the quantity subtype.
     */
    private static final int QUANTITY_SUBTYPE_LENGTH = 2;

    /**
     * the quantity subtype.
     */
    @XmlElement(name = "QuantitySubType")
    private String quantitySubtype;

    /**
     * gets the quantity subtype.
     * @return String - the quantity subtype
     */
    public final String getQuantitySubtype() {
        return quantitySubtype;
    }

    /**
     * the length of the quantity precision.
     */
    private static final int QUANTITY_PRECISION_LENGTH = 1;

    /**
     * the quantity precision.
     */
    @XmlElement(name = "QuantityPrecision")
    private String quantityPrecision;

    /**
     * gets the quantity precision.
     * @return String - the quantity precision
     */
    public final String getQuantityPrecision() {
        return quantityPrecision;
    }

    /**
     * the length of the quantity.
     */
    private static final int QUANTITY_LENGTH = 6;

    /**
     * the quantity.
     */
    @XmlElement(name = "Quantity")
    private String quantity;

    /**
     * gets the quantity.
     * @return String - the quantity
     */
    public final String getQuantity() {
        return quantity;
    }

    /**
     * the length of the promotion code.
     */
    private static final int PROMOTION_CODE_LENGTH = 10;

    /**
     * the promotion code.
     */
    @XmlElement(name = "PromotionCode")
    private String promoCode;

    /**
     * gets the promotion code.
     * @return String - the promotion code
     */
    public final String getPromoCode() {
        return promoCode;
    }

    /**
     * the length of the discount data.
     */
    private static final int DISCOUNT_DATA_LENGTH = 4;

    /**
     * the discount data.
     */
    @XmlElement(name = "DiscountData")
    private String discountData;

    /**
     * gets the discount data.
     * @return String - the discount data
     */
    public final String getDiscountData() {
        return discountData;
    }

    /**
     * the length of the pos reference id.
     */
    private static final int POS_REFERENCE_ID_LENGTH = 6;

    /**
     * the pos reference id.
     */
    @XmlElement(name = "POSReferenceID")
    private String posReferenceID;

    /**
     * gets the pos reference id.
     * @return String - the pos reference id
     */
    public final String getPosReferenceID() {
        return posReferenceID;
    }

    /**
     * the length of the vendor coupon code.
     */
    private static final int VENDOR_COUPON_CODE_LENGTH = 20;

    /**
     * the vendor coupon code.
     */
    @XmlElement(name = "VendorCouponCode")
    private String vendorCouponCode;

    /**
     * gets the vendor coupon code.
     * @return String - the vendor coupon code
     */
    public final String getVendorCouponCode() {
        return vendorCouponCode;
    }

    /**
     * the length of the discount description.
     */
    private static final int DISCOUNT_DESCRIPTION_LENGTH = 18;

    /**
     * the collection of discount descriptions.
     */
    @XmlElement(name = "DiscountDescription")
    private List<String> discountDescriptions;

    /**
     * gets the collection of discount descriptions.
     * @return String - the discount descriptions
     */
    public final List<String> getDiscountDescriptions() {
        return discountDescriptions;
    }

    /**
     * the length of the description language id.
     */
    private static final int DESCRIPTION_LANGUAGE_ID_LENGTH = 2;

    /**
     * the collection of discount language ids.
     */
    @XmlElement(name = "DescriptionLanguageID")
    private List<String> descriptionLanguageIDs;

    /**
     * gets the collection of discount descriptions.
     * @return String - the discount descriptions
     */
    public final List<String> getDiscountLanguageIDs() {
        return descriptionLanguageIDs;
    }

    /**
     * Constructor.
     * @param discountMessage - the message corresponding to a certain
     * @param length - the length of the passed message
     * reward sent by UE.
     */
    public Discount(final String discountMessage, final int length) {
        discountDescriptions = new ArrayList<String>();
        descriptionLanguageIDs = new ArrayList<String>();
        if (length
                != (discountMessage.length())) {
            return;
        }

        overallMessageLength = length;

        int startIndex = 0;
        int endIndex = REWARD_ID_LENGTH;

        this.setRewardID(discountMessage.substring(startIndex, endIndex));

        startIndex = endIndex;
        endIndex = startIndex + EARNED_REWARD_ID_LENGTH;

        this.setEarnedRewardID(discountMessage.substring(startIndex, endIndex));

        startIndex = endIndex;
        endIndex = startIndex + REWARD_FLAG_LENGTH;

        rewardFlag = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + DISCOUNT_TYPE_LENGTH;

        discountType = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + DISCOUNT_LEVEL_LENGTH;

        discountLevel = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + CALCULATION_TYPE_LENGTH;

        calculationType = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + FLEX_NEGATIVE_LENGTH;

        flexNegative = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + ITEM_ENTRY_ID_LENGTH;

        itemEntryID = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + DISCOUNT_DPT_LENGTH;

        discountDepartment = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + MNUP_PRECISION_LENGTH;

        mnupPrecision = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + MATCH_NET_UNIT_PRICE_LENGTH;

        matchNetUnitPrice = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + UNIT_DISCOUNT_PRECISION_LENGTH;

        unitDiscountPrecision = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + UNIT_DISCOUNT_AMT_LENGTH;

        unitDiscountAmt = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + RETURNS_DISCOUNT_PRECISION_LENGTH;

        retDiscountPrecision = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + RETURNS_DISCOUNT_AMT_LENGTH;

        retDiscountAmt = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + CALCULATE_PRECISION_LENGTH;

        calculatePrecision = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + UNIT_CALCULATE_AMT_LENGTH;

        unitCalculateAmount = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + QUANTITY_TYPE_LENGTH;

        quantityType = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + QUANTITY_SUBTYPE_LENGTH;

        quantitySubtype = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + QUANTITY_PRECISION_LENGTH;

        quantityPrecision = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + QUANTITY_LENGTH;

        quantity = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + PROMOTION_CODE_LENGTH;

        promoCode = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + DISCOUNT_DATA_LENGTH;

        discountData = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + POS_REFERENCE_ID_LENGTH;

        posReferenceID = discountMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + VENDOR_COUPON_CODE_LENGTH;

        vendorCouponCode = discountMessage.substring(startIndex, endIndex);

        while (endIndex < length) {
            startIndex = endIndex;
            endIndex = startIndex + DISCOUNT_DESCRIPTION_LENGTH;

            discountDescriptions.add(
                    discountMessage.substring(startIndex, endIndex));

            startIndex = endIndex;
            endIndex = startIndex + DESCRIPTION_LANGUAGE_ID_LENGTH;

            descriptionLanguageIDs.add(
                    discountMessage.substring(startIndex, endIndex));
        }

    }
}
