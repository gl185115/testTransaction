package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Xeb Member Points Info Model Object.
 *
 * <P>A XebMemberInfo Node in POSLog XML.
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "XebMemberInfo")
public class XebMemberInfo {
    /**
     * The private variable that will hold the unified membership id.
     */
    @XmlElement(name = "UnifiedMembershipId")
    private String unifiedMembershipId;
    /**
     * The private variable that will hold quantity for points.
     */
    @XmlElement(name = "QuantityForPoints")
    private Integer quantityForPoints;
    /**
     * The private variable that will hold amount for cash.
     */
    @XmlElement(name = "AmountForCash")
    private Integer amountForCash;
    /**
     * The private variable that will hold amount for affiliate.
     */
    @XmlElement(name = "AmountForAffiliate")
    private Integer amountForAffiliate;
    /**
     * The private variable that will hold amount for nonAffiliate.
     */
    @XmlElement(name = "AmountForNonAffiliate")
    private Integer amountForNonAffiliate;
    /**
     * The private variable that will hold amount for additional.
     */
    @XmlElement(name = "AmountForAdditional")
    private Integer amountForAdditional;
    /**
     * The private variable that will hold quantity for additional.
     */
    @XmlElement(name = "QuantityForAdditional")
    private Integer quantityForAdditional;
    /**
     * 
     */
    @XmlElement(name = "PointsRedeemed")
    private String pointsRedeemed;
    /**
     * The private variable that will hold the value of rank
     */
    @XmlElement(name = "Rank")
    private Integer rank;
    /**
     * The private variable that will hold the value of commodity sales 
     */
    @XmlElement(name = "QuantityForRank")
    private String quantityForRank;
    /**
     * Amount of money of commodity sales
     */
    @XmlElement(name = "SalesQuantityByRank")
    private String salesQuantityByRank;
    /**
     * 
     */
    @XmlElement(name = "SalesAmountByRank")
    private String salesAmountByRank;
    /**
     * The private variable that will hold the value of card type.
     */
    @XmlElement(name = "CardType")
    private String cardType;
    /**
     * The private variable that will hold the value of card type name.
     */
    @XmlElement(name = "CardTypeName")
    private String cardTypeName;
    /**
     * The private variable that will hold the value of card div.
     */
    @XmlElement(name = "CardClass")
    private String cardClass;
    /**
     * The QuantityPerYear.
     */
    @XmlElement(name = "QuantityPerYear")
    private String quantityPerYear;
    /**
     * The AmountPerYear.
     */
    @XmlElement(name = "AmountPerYear")
    private String amountPerYear;
    /**
     * The ExtendExpirationMonth.
     */
    @XmlElement(name = "ExtendExpirationMonth")
    private String extendExpirationMonth;
    /**
     * The ExtendExpirationPrintFlag.
     */
    @XmlElement(name = "ExtendExpirationPrintFlag")
    private String extendExpirationPrintFlag;
    /**
     * The OwnCreditFlag.
     */
    @XmlElement(name = "OwnCreditFlag")
    private String ownCreditFlag;
    /**
     * The AddingFlag.
     */
    @XmlElement(name = "AddingFlag")
    private String addingFlag;
    /**
     * the private variable that will hold the value of original transaction.
     */
    @XmlElement(name =  "OriginalTransaction")
    private OriginalTransaction originalTransaction;
    /**
     * 
     * @return
     */
    public String getUnifiedMembershipId() {
        return unifiedMembershipId;
    }
    /**
     * 
     * @param unifiedMembershipId
     */
    public void setUnifiedMembershipId(String unifiedMembershipId) {
        this.unifiedMembershipId = unifiedMembershipId;
    }
    /**
     * 
     * @return
     */
    public Integer getQuantityForPoints() {
        return quantityForPoints;
    }
    /**
     * 
     * @param quantityForPoints
     */
    public void setQuantityForPoints(Integer quantityForPoints) {
        this.quantityForPoints = quantityForPoints;
    }
    /**
     * 
     * @return
     */
    public Integer getRank() {
        return rank;
    }
    /**
     * 
     * @param rank
     */
    public void setRank(Integer rank) {
        this.rank = rank;
    }
    /**
     * 
     * @return
     */
    public String getCardType() {
        return cardType;
    }
    /**
     * 
     * @param cardType
     */
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    /**
     * 
     * @return
     */
    public String getCardClass() {
        return cardClass;
    }
    /**
     * 
     * @param cardDiv
     */
    public void setCardDiv(String cardClass) {
        this.cardClass = cardClass;
    }
    /**
     * 
     * @return
     */
    public OriginalTransaction getOriginalTransaction() {
        return originalTransaction;
    }
    /**
     * 
     * @param originalTransaction
     */
    public void setOriginalTransaction(OriginalTransaction originalTransaction) {
        this.originalTransaction = originalTransaction;
    }
    /**
     * 
     * @return
     */
    public String getQuantityForRank() {
        return quantityForRank;
    }
    /**
     * 
     * @param quantityForRank
     */
    public void setQuantityForRank(String quantityForRank) {
        this.quantityForRank = quantityForRank;
    }
    /**
     * 
     * @return
     */
    public String getPointsRedeemed() {
        return pointsRedeemed;
    }
    /**
     * 
     * @param pointsRedeemed
     */
    public void setPointsRedeemed(String pointsRedeemed) {
        this.pointsRedeemed = pointsRedeemed;
    }
    /**
     * 
     * @return
     */
    public String getSalesQuantityByRank() {
        return salesQuantityByRank;
    }
    /**
     * 
     * @param salesQuantityByRank
     */
    public void setSalesQuantityByRank(String salesQuantityByRank) {
        this.salesQuantityByRank = salesQuantityByRank;
    }
    /**
     * 
     * @return
     */
    public String getSalesAmountByRank() {
        return salesAmountByRank;
    }
    /**
     * 
     * @param salesAmountByRank
     */
    public void setSalesAmountByRank(String salesAmountByRank) {
        this.salesAmountByRank = salesAmountByRank;
    }
    /**
     * 
     * @return
     */
    public String getQuantityPerYear() {
        return quantityPerYear;
    }
    /**
     * 
     * @param quantityPerYear
     */
    public void setQuantityPerYear(String quantityPerYear) {
        this.quantityPerYear = quantityPerYear;
    }
    /**
     * 
     * @return
     */
    public String getAmountPerYear() {
        return amountPerYear;
    }
    /**
     * 
     * @param amountPerYear
     */
    public void setAmountPerYear(String amountPerYear) {
        this.amountPerYear = amountPerYear;
    }
    /**
     * 
     * @return
     */
    public String getExtendExpirationMonth() {
        return extendExpirationMonth;
    }
    /**
     * 
     * @param extendExpirationMonth
     */
    public void setExtendExpirationMonth(String extendExpirationMonth) {
        this.extendExpirationMonth = extendExpirationMonth;
    }
    /**
     * 
     * @return
     */
    public String getExtendExpirationPrintFlag() {
        return extendExpirationPrintFlag;
    }
    /**
     * 
     * @param extendExpirationPrintFlag
     */
    public void setExtendExpirationPrintFlag(String extendExpirationPrintFlag) {
        this.extendExpirationPrintFlag = extendExpirationPrintFlag;
    }
    /**
     * 
     * @return
     */
    public String getOwnCreditFlag() {
        return ownCreditFlag;
    }
    /**
     * 
     * @param ownCreditFlag
     */
    public void setOwnCreditFlag(String ownCreditFlag) {
        this.ownCreditFlag = ownCreditFlag;
    }
    /**
     * 
     * @return
     */
    public String getAddingFlag() {
        return addingFlag;
    }
    /**
     * 
     * @param addingFlag
     */
    public void setAddingFlag(String addingFlag) {
        this.addingFlag = addingFlag;
    }
    /**
     * 
     * @return
     */
    public Integer getAmountForCash() {
        return amountForCash;
    }
    /**
     * 
     * @param amountForCash
     */
    public void setAmountForCash(Integer amountForCash) {
        this.amountForCash = amountForCash;
    }
    /**
     * 
     * @return
     */
    public Integer getAmountForAffiliate() {
        return amountForAffiliate;
    }
    /**
     * 
     * @param amountForAffiliate
     */
    public void setAmountForAffiliate(Integer amountForAffiliate) {
        this.amountForAffiliate = amountForAffiliate;
    }
    /**
     * 
     * @return
     */
    public Integer getAmountForNonAffiliate() {
        return amountForNonAffiliate;
    }
    /**
     * 
     * @param amountForNonAffiliate
     */
    public void setAmountForNonAffiliate(Integer amountForNonAffiliate) {
        this.amountForNonAffiliate = amountForNonAffiliate;
    }
    /**
     * 
     * @return
     */
    public Integer getAmountForAdditional() {
        return amountForAdditional;
    }
    /**
     * 
     * @param amountForAdditional
     */
    public void setAmountForAdditional(Integer amountForAdditional) {
        this.amountForAdditional = amountForAdditional;
    }
    /**
     * 
     * @return
     */
    public Integer getQuantityForAdditional() {
        return quantityForAdditional;
    }
    /**
     * 
     * @param quantityForAdditional
     */
    public void setQuantityForAdditional(Integer quantityForAdditional) {
        this.quantityForAdditional = quantityForAdditional;
    }
    /**
     * 
     * @return
     */
    public String getCardTypeName() {
        return cardTypeName;
    }
    /**
     * 
     * @param cardTypeName
     */
    public void setCardTypeName(String cardTypeName) {
        this.cardTypeName = cardTypeName;
    }
    /**
     * 
     * 
     * @param cardClass
     */
    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
    }
    @Override
    public String toString() {
        return "XebMemberInfo [unifiedMembershipId=" + unifiedMembershipId + ", quantityForPoints=" + quantityForPoints
                + ", pointsRedeemed=" + pointsRedeemed + ", rank=" + rank + ", quantityForRank=" + quantityForRank
                + ", salesQuantityByRank=" + salesQuantityByRank + ", salesAmountByRank=" + salesAmountByRank
                + ", cardType=" + cardType + ", cardClass=" + cardClass + ", originalTransaction=" + originalTransaction
                + "]";
    }


}
