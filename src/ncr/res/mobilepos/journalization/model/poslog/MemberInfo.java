package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Member Points Info Model Object.
 *
 * <P>A MemberPointsInfo Node in POSLog XML.
 *
 * <P>The Membership information Node is under CustomerOrderTransaction Node.
 * And holds the value of Member Points Info.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MemberInfo")
public class MemberInfo {
    /**
     * The private member variable that will hold the amount for points.
     */
    @XmlElement(name = "AmountForPoints")
    private String amountForPoints;
    /**
     * The private member variable that will hold AfterAddDate.
     */
    @XmlElement(name = "AfterAddDate")
    private String AfterAddDate;
    /**
     * The private member variable that will hold the point rate.
     */
    @XmlElement(name = "PointRate")
    private String pointRate;
    /**
     * The private member variable that will hold the correction points.
     */
    @XmlElement(name = "CorrectionPoints")
    private String correctionPoints;
    /**
     * The private member variable that will hold the expiration date.
     */
    @XmlElement(name = "ExpirationDate")
    private String expirationDate;
    /**
     * The private member variable that will hold the applyStart date.
     */
    @XmlElement(name = "ApplyStart")
    private String applyStart;
    /**
     * The private member variable that will hold the applyEnd date.
     */
    @XmlElement(name = "ApplyEnd")
    private String applyEnd;
    /**
     * The private member variable that will hold the membershipId13 Id.
     */
    @XmlElement(name = "MembershipId13")
    private String membershipId13;
    /**
     * The private member variable that will hold the membershipId16 Id.
     */
    @XmlElement(name = "MembershipId16")
    private String membershipId16;
    /**
     * The private member variable that will hold the inputtedMembershipId Id.
     */
    @XmlElement(name = "InputtedMembershipId")
    private InputtedMembershipId inputtedMembershipId;
    /**
     * The private member variable that will hold the paymentTypeName Id.
     */
    @XmlElement(name = "PaymentTypeName")
    private String paymentTypeName;
    /**
     * The private member variable that will hold the media Id.
     */
    @XmlElement(name = "MediaId")
    private String mediaId;
    /**
     * The private member variable that will hold the points acknowledge Id.
     */
    @XmlElement(name = "PointsAcknowledgeId")
    private String pointsAcknowledgeId;
    /**
     * The private member variable that will hold the points transaction Id.
     */
    @XmlElement(name = "PointsTransactionId")
    private String piontsTransactionId;
    /**
     * The private member variable that will hold the points method.
     */
    @XmlElement(name = "PointsMethod")
    private String pointsMethod;
    /**
     * The private member variable that will hold the status code.
     */
    @XmlElement(name = "StatusCode")
    private String statusCode;
    /**
     * The private member variable that will hold the Server status code.
     */
    @XmlElement(name = "ServerStatusCode")
    private String serverStatusCode;
    /**
     * The private member variable that will hold the points prior.
     */
    @XmlElement(name = "PointsPrior")
    private String pointsPrior;
    /**
     * The private member variable that will hold the total points.
     */
    @XmlElement(name = "TotalPoints")
    private String totalPoints;
    /**
     * The private member variable that will hold the lost points.
     */
    @XmlElement(name = "LostPoints")
    private String lostPoints;
    /**
     * The private member variable that will hold the value of bonus points. 
     */
    @XmlElement(name = "BonusPoints")
    private String bonusPoints;
    /**
     * The private member variable that will hold the value of term points.
     */
    @XmlElement(name = "TermPoints")
    private String termPoints;
    /**
     * The private member variable that will hold the value of basic points.
     */
    @XmlElement(name = "BasicPoints")
    private String basicPoints;
    /**
     * The private member variable that will hold the value of basic points.
     */
    @XmlElement(name = "PointCalcExp")
    private String pointCalcExp;
    
    /**
     * The private member variable that will hold the value of OfflineFlag.
     */
    @XmlElement(name = "OfflineFlag")
    private String offlineFlag;
    /**
     * The private member variable that will hold the xeb member info.
     */
    @XmlElement(name = "XebMemberInfo")
    private XebMemberInfo xebMemberInfo;
    /**
     * Gets the AmountForPoints under LineItem.
     *
     * @return        The AmountForPoints under LineItem.
     */
    public final String getAmountForPoints() {
        return amountForPoints;
    }
    /**
     * Sets the AmountForPoints under LineItem.
     *
     * @param amountForPointsSet       The new value for the AmountForPoints under LineItem.
     */
    public final void setAmountForPoints(final String amountForPointsSet) {
        this.amountForPoints = amountForPointsSet;
    }
    /**
     * Gets the PointRate under LineItem.
     *
     * @return        The PointRate under LineItem.
     */
    public final String getPointRate() {
        return pointRate;
    }
    /**
     * Sets the PointRate under LineItem.
     *
     * @param pointRateSet       The new value for the PointRate under LineItem.
     */
    public final void setPointRate(final String pointRateSet) {
        this.pointRate = pointRateSet;
    }
    /**
     * Gets the CorrectionPoints under LineItem.
     *
     * @return        The CorrectionPoints under LineItem.
     */
    public final String getCorrectionPoints() {
        return correctionPoints;
    }
    /**
     * Sets the CorrectionPoints under LineItem.
     *
     * @param correctionPointsSet       The new value for the CorrectionPoints under LineItem.
     */
    public final void setCorrectionPoints(final String correctionPointsSet) {
        this.correctionPoints = correctionPointsSet;
    }
    /**
     * Gets the ExpirationDate under LineItem.
     *
     * @return        The ExpirationDate under LineItem.
     */
    public final String getExpirationDate() {
        return expirationDate;
    }
    /**
     * Sets the ExpirationDate under LineItem.
     *
     * @param expirationDateSet       The new value for the ExpirationDate under LineItem.
     */
    public final void setExpirationDate(final String expirationDateSet) {
        this.expirationDate = expirationDateSet;
    }
    /**
     * Gets the InputtedMembershipId under LineItem.
     *
     * @return        The InputtedMembershipId under LineItem.
     */
    public final InputtedMembershipId getInputtedMembershipId() {
        return inputtedMembershipId;
    }
    /**
     * Sets the InputtedMembershipId under LineItem.
     *
     * @param expirationDateSet       The new value for the InputtedMembershipId under LineItem.
     */
    public final void setInputtedMembershipId(final InputtedMembershipId inputtedMembershipId) {
        this.inputtedMembershipId = inputtedMembershipId;
    }
    /**
     * Gets the tMediaId under LineItem.
     *
     * @return        The tMediaId under LineItem.
     */
    public final String getMediaId() {
        return mediaId;
    }
    /**
     * Sets the tMediaId under LineItem.
     *
     * @param mediaIdSet       The new value for the tMediaId under LineItem.
     */
    public final void setMediaId(final String mediaIdSet) {
        this.mediaId = mediaIdSet;
    }
    /**
     * Gets the PointsAcknowledgeId under LineItem.
     *
     * @return        The PointsAcknowledgeId under LineItem.
     */
    public final String getPointsAcknowledgeId() {
        return pointsAcknowledgeId;
    }
    /**
     * Sets the PointsAcknowledgeId under LineItem.
     *
     * @param pointsAcknowledgeIdSet       The new value for the PointsAcknowledgeId under LineItem.
     */
    public final void setPointsAcknowledgeId(final String pointsAcknowledgeIdSet) {
        this.pointsAcknowledgeId = pointsAcknowledgeIdSet;
    }
    /**
     * Gets the PiontsTransactionId under LineItem.
     *
     * @return        The PiontsTransactionIdId under LineItem.
     */
    public final String getPiontsTransactionId() {
        return piontsTransactionId;
    }
    /**
     * Sets the PiontsTransactionId under LineItem.
     *
     * @param piontsTransactionIdSet       The new value for the PiontsTransactionIdId under LineItem.
     */
    public final void setPiontsTransactionId(final String piontsTransactionIdSet) {
        this.piontsTransactionId = piontsTransactionIdSet;
    }
    /**
     * Gets the PointsMethod under LineItem.
     *
     * @return        The PointsMethod under LineItem.
     */
    public final String getPointsMethod() {
        return pointsMethod;
    }
    /**
     * Sets the PointsMethod under LineItem.
     *
     * @param pointsMethodSet       The new value for the PointsMethod under LineItem.
     */
    public final void setPointsMethod(final String pointsMethodSet) {
        this.pointsMethod = pointsMethodSet;
    }
    public String getAfterAddDate() {
		return AfterAddDate;
	}
	public void setAfterAddDate(String afterAddDate) {
		AfterAddDate = afterAddDate;
	}
	/**
     * Gets the StatusCode under LineItem.
     *
     * @return        The StatusCode under LineItem.
     */
    public final String getStatusCode() {
        return statusCode;
    }
    /**
     * Sets the StatusCode under LineItem.
     *
     * @param statusCodeSet       The new value for the StatusCode under LineItem.
     */
    public final void setStatusCode(final String statusCodeSet) {
        this.statusCode = statusCodeSet;
    }
    /**
     * Gets the ServerStatusCode under LineItem.
     *
     * @return        The ServerStatusCode under LineItem.
     */
    public final String getServerStatusCode() {
        return serverStatusCode;
    }
    /**
     * Sets the ServerStatusCode under LineItem.
     *
     * @param serverStatusCodeSet       The new value for the ServerStatusCode under LineItem.
     */
    public final void setServerStatusCode(final String serverStatusCodeSet) {
        this.serverStatusCode = serverStatusCodeSet;
    }
    /**
     * Gets the PointsPrior under LineItem.
     *
     * @return        The PointsPrior under LineItem.
     */
    public final String getPointsPrior() {
        return pointsPrior;
    }
    /**
     * Sets the PointsPrior under LineItem.
     *
     * @param pointsPriorSet       The new value for the PointsPrior under LineItem.
     */
    public final void setPointsPrior(final String pointsPriorSet) {
        this.pointsPrior = pointsPriorSet;
    }
    /**
     * Gets the TotalPoints under LineItem.
     *
     * @return        The TotalPoints under LineItem.
     */
    public final String getTotalPoints() {
        return totalPoints;
    }
    /**
     * Sets the TotalPoints under LineItem.
     *
     * @param totalPointsSet       The new value for the TotalPoints under LineItem.
     */
    public final void setTotalPoints(final String totalPointsSet) {
        this.totalPoints = totalPointsSet;
    }
    /**
     * Gets the LostPoints under LineItem.
     *
     * @return        The LostPoints under LineItem.
     */
    public final String getLostPoints() {
        return lostPoints;
    }
    /**
     * Sets the LostPoints under LineItem.
     *
     * @param lostPointsSet       The new value for the LostPoints under LineItem.
     */
    public final void setLostPoints(final String lostPointsSet) {
        this.lostPoints = lostPointsSet;
    }
    public final String getOfflineFlag() {
		return offlineFlag;
	}
	public final void setOfflineFlag(String offlineFlag) {
		this.offlineFlag = offlineFlag;
	}
	/**
     * 
     * @return
     */
    public XebMemberInfo getXebMemberInfo() {
        return xebMemberInfo;
    }
    /**
     * 
     * @param xebMemberInfo
     */
    public void setXebMemberInfo(XebMemberInfo xebMemberInfo) {
        this.xebMemberInfo = xebMemberInfo;
    }
    /**
     * 
     * @return
     */
    public String getBonusPoints() {
        return bonusPoints;
    }
    /**
     * 
     * @param bonusPoints
     */
    public void setBonusPoints(String bonusPoints) {
        this.bonusPoints = bonusPoints;
    }
    /**
     * 
     * @return
     */
    public String getTermPoints() {
        return termPoints;
    }
    /**
     * 
     * @param termPoints
     */
    public void setTermPoints(String termPoints) {
        this.termPoints = termPoints;
    }
    /**
     * 
     * @return
     */
    public String getBasicPoints() {
        return basicPoints;
    }
    /**
     * 
     * @param basicPoints
     */
    public void setBasicPoints(String basicPoints) {
        this.basicPoints = basicPoints;
    }
    /**
     * 
     * @return
     */
    public final String getMembershipId13() {
        return membershipId13;
    }
    /**
     * 
     * @param membershipId13
     */
    public final void setMembershipId13(String membershipId13) {
        this.membershipId13 = membershipId13;
    }
    /**
     * 
     * @return
     */
    public final String getMembershipId16() {
        return membershipId16;
    }
    /**
     * 
     * @param membershipId16
     */
    public final void setMembershipId16(String membershipId16) {
        this.membershipId16 = membershipId16;
    }
    /**
     * 
     * @return
     */
    public final String getPaymentTypeName() {
        return paymentTypeName;
    }
    /**
     * 
     * @param paymentTypeName
     */
    public final void setPaymentTypeName(String paymentTypeName) {
        this.paymentTypeName = paymentTypeName;
    }
    /**
	 * @return the pointCalcExp
	 */
	public String getPointCalcExp() {
		return pointCalcExp;
	}
	/**
	 * @param pointCalcExp the pointCalcExp to set
	 */
	public void setPointCalcExp(String pointCalcExp) {
		this.pointCalcExp = pointCalcExp;
	}
	public String getApplyStart() {
		return applyStart;
	}
	public void setApplyStart(String applyStart) {
		this.applyStart = applyStart;
	}
	public String getApplyEnd() {
		return applyEnd;
	}
	public void setApplyEnd(String applyEnd) {
		this.applyEnd = applyEnd;
	}
	@Override
    public String toString() {
        return "MemberInfo [amountForPoints=" + amountForPoints + ", pointRate=" + pointRate + ", correctionPoints="
                + correctionPoints + ", expirationDate=" + expirationDate + ", memberShipId=" + inputtedMembershipId
                + ", mediaId=" + mediaId + ", pointsAcknowledgeId=" + pointsAcknowledgeId + ", piontsTransactionId="
                + piontsTransactionId + ", pointsMethod=" + pointsMethod + ", statusCode=" + statusCode
                + ", serverStatusCode=" + serverStatusCode + ", pointsPrior=" + pointsPrior + ", totalPoints="
                + totalPoints + ", lostPoints=" + lostPoints + ", bonusPoints=" + bonusPoints + ", termPoints="
                + termPoints + ", basicPoints=" + basicPoints + ", pointCalcExp=" + pointCalcExp + ", xebMemberInfo=" + xebMemberInfo + "]";
    }
    
    
}
