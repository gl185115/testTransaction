package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Member Points Info Model Object.
 *
 * <P>
 * A MemberPointsInfo Node in POSLog XML.
 *
 * <P>
 * The Membership information Node is under CustomerOrderTransaction Node. And
 * holds the value of Member Points Info.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MemberInfo")
public class MemberInfo {
    /**
     * The private variable that will hold the PointsMethod
     */
    @XmlElement(name = "PointsMethod")
    private Integer pointsMethod;

    /**
     * The private variable that will hold the AmountForPoints
     */
    @XmlElement(name = "AmountForPoints")
    private Integer amountForPoints;

    /**
     * The private variable that will hold the AmountForRank
     */
    @XmlElement(name = "AmountForRank")
    private Integer amountForRank;

    /**
     * The private variable that will hold the InputtedMembershipId
     */
    @XmlElement(name = "InputtedMembershipId")
    private String inputtedMembershipId;

    /**
     * The private variable that will hold the TransactionId
     */
    @XmlElement(name = "TransactionId")
    private String transactionId;

    /**
     * The private variable that will hold the StatusCode
     */
    @XmlElement(name = "StatusCode")
    private String statusCode;

    /**
     * The private variable that will hold the CorrectionPoints
     */
    @XmlElement(name = "CorrectionPoints")
    private Integer correctionPoints;

    /**
     * The private variable that will hold the BasicPoints
     */
    @XmlElement(name = "BasicPoints")
    private Integer basicPoints;

    /**
     * The private variable that will hold the AdditionalPoints
     */
    @XmlElement(name = "AdditionalPoints")
    private Integer additionalPoints;

    /**
     * The private variable that will hold the BonusPoints
     */
    @XmlElement(name = "BonusPoints")
    private Integer bonusPoints;

    /**
     * The private variable that will hold the CouponPoints
     */
    @XmlElement(name = "CouponPoints")
    private Integer couponPoints;

    /**
     * The private variable that will hold the PointsRedeemed
     */
    @XmlElement(name = "PointsRedeemed")
    private Integer pointsRedeemed;

    /**
     * The private variable that will hold the PointsPrior
     */
    @XmlElement(name = "PointsPrior")
    private Integer pointsPrior;

    /**
     * The private variable that will hold the TotalPoints
     */
    @XmlElement(name = "TotalPoints")
    private Integer totalPoints;

    /**
     * The private variable that will hold the LostPoints
     */
    @XmlElement(name = "LostPoints")
    private Integer lostPoints;

    /**
     * The private variable that will hold the MemberDetail
     */
    @XmlElement(name = "MemberDetail")
    private MemberDetail memberDetail;

    /**
     * The private variable that will hold the AmountByRank
     */
    @XmlElement(name = "AmountByRank")
    private Integer amountByRank;

    /**
     * The private variable that will hold the CardClass
     */
    @XmlElement(name = "CardClass")
    private String cardClass;

    /**
     * The private member variable that holds the CouponDetail.
     */
    @XmlElement(name = "CouponDetail")
    private List<CouponDetail> couponDetail;
    /**
     *
     * @return PointsMethod
     */
    public Integer getPointsMethod() {
        return pointsMethod;
    }

    /**
     *
     * @param PointsMethod
     */
    public void setPointsMethod(Integer pointsMethod) {
        this.pointsMethod = pointsMethod;
    }

    /**
     *
     * @return AmountForPoints
     */
    public Integer getAmountForPoints() {
        return amountForPoints;
    }

    /**
     *
     * @param AmountForPoints
     */
    public void setAmountForPoints(Integer amountForPoints) {
        this.amountForPoints = amountForPoints;
    }

    /**
     *
     * @return AmountForRank
     */
    public Integer getAmountForRank() {
        return amountForRank;
    }

    /**
     *
     * @param AmountForRank
     */
    public void setAmountForRank(Integer amountForRank) {
        this.amountForRank = amountForRank;
    }

    /**
    *
    * @return TransactionId
    */
   public String getTransactionId() {
       return transactionId;
   }

   /**
    *
    * @param transactionId
    */
   public void setTransactionId(String transactionId) {
       this.transactionId = transactionId;
   }

    /**
     *
     * @return InputtedMembershipId
     */
    public String getInputtedMembershipId() {
        return inputtedMembershipId;
    }

    /**
     *
     * @param InputtedMembershipId
     */
    public void setInputtedMembershipId(String inputtedMembershipId) {
        this.inputtedMembershipId = inputtedMembershipId;
    }

    /**
     *
     * @return StatusCode
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     *
     * @param StatusCode
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    /**
     *
     * @return CorrectionPoints
     */
    public Integer getCorrectionPoints() {
        return correctionPoints;
    }

    /**
     *
     * @param CorrectionPoints
     */
    public void setCorrectionPoints(Integer correctionPoints) {
        this.correctionPoints = correctionPoints;
    }

    /**
     *
     * @return BasicPoints
     */
    public Integer getBasicPoints() {
        return basicPoints;
    }

    /**
     *
     * @param BasicPoints
     */
    public void setBasicPoints(Integer basicPoints) {
        this.basicPoints = basicPoints;
    }

    /**
     *
     * @return AdditionalPoints
     */
    public Integer getAdditionalPoints() {
        return additionalPoints;
    }

    /**
     *
     * @param AdditionalPoints
     */
    public void setAdditionalPoints(Integer additionalPoints) {
        this.additionalPoints = additionalPoints;
    }

    /**
     *
     * @return BonusPoints
     */
    public Integer getBonusPoints() {
        return bonusPoints;
    }

    /**
     *
     * @param BonusPoints
     */
    public void setBonusPoints(Integer bonusPoints) {
        this.bonusPoints = bonusPoints;
    }

    /**
     *
     * @return CouponPoints
     */
    public Integer getCouponPoints() {
        return couponPoints;
    }

    /**
     *
     * @param CouponPoints
     */
    public void setCouponPoints(Integer couponPoints) {
        this.couponPoints = couponPoints;
    }

    /**
     *
     * @return PointsRedeemed
     */
    public Integer getPointsRedeemed() {
        return pointsRedeemed;
    }

    /**
     *
     * @param PointsRedeemed
     */
    public void setPointsRedeemed(Integer pointsRedeemed) {
        this.pointsRedeemed = pointsRedeemed;
    }

    /**
     *
     * @return PointsPrior
     */
    public Integer getPointsPrior() {
        return pointsPrior;
    }

    /**
     *
     * @param PointsPrior
     */
    public void setPointsPrior(Integer pointsPrior) {
        this.pointsPrior = pointsPrior;
    }

    /**
     *
     * @return TotalPoints
     */
    public Integer getTotalPoints() {
        return totalPoints;
    }

    /**
     *
     * @param TotalPoints
     */
    public void setTotalPoints(Integer totalPoints) {
        this.totalPoints = totalPoints;
    }

    /**
     *
     * @return LostPoints
     */
    public Integer getLostPoints() {
        return lostPoints;
    }

    /**
     *
     * @param LostPoints
     */
    public void setLostPoints(Integer lostPoints) {
        this.lostPoints = lostPoints;
    }

    /**
     *
     * @return MemberDetail
     */
    public MemberDetail getMemberDetail() {
        return memberDetail;
    }

    /**
     *
     * @param MemberDetail
     */
    public void setMemberDetail(MemberDetail memberDetail) {
        this.memberDetail = memberDetail;
    }

    /**
    *
    * @return AmountByRank
    */
   public Integer getAmountByRank() {
       return amountByRank;
   }

   /**
    *
    * @param AmountByRank
    */
   public void setAmountByRank(Integer amountByRank) {
       this.amountByRank = amountByRank;
   }
    /**
    *
    * @return CardClass
    */
   public String getCardClass() {
       return cardClass;
   }

   /**
    *
    * @param CardClass
    */
   public void setCardClass(String cardClass) {
       this.cardClass = cardClass;
   }

   /**
    * Gets the CouponDetail.
    *
    * @return        Returns the CouponDetail list.
    */
   public final List<CouponDetail> getCouponDetail() {
       return couponDetail;
   }

   /**
    * Sets the CouponDetail.
    *
    * @param CouponDetailToSet      The new value for the CouponDetail list.
    */
   public final void setCouponDetail(
           final List<CouponDetail> couponDetailToSet) {
       this.couponDetail = couponDetailToSet;
   }

    @Override
    public String toString() {
        return "MemberInfo [PointsMethod=" + pointsMethod + "AmountForPoints = " + amountForPoints
                + "InputtedMembershipId = " + inputtedMembershipId + "StatusCode = " + statusCode
                + "CorrectionPoints = " + correctionPoints + "BasicPoints = " + basicPoints + "AdditionalPoints = "
                + additionalPoints + "BonusPoints = " + bonusPoints + "CouponPoints = " + couponPoints
                + "PointsRedeemed = " + pointsRedeemed + "PointsPrior = " + pointsPrior + "TotalPoints = " + totalPoints
                + "LostPoints = " + lostPoints + "AmountByRank = " + amountByRank + "CardClass = " + cardClass + "MemberDetail = " + memberDetail + "]";
    }

}
