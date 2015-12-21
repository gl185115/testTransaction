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
@XmlRootElement(name = "Points")
public class MemberPointsInfo {
    /**
     * The private member variable that will hold the amount for points.
     */
    @XmlElement(name = "AmountForPoints")
    private String amountForPoints;
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
     * The private member variable that will hold the membership Id.
     */
    @XmlElement(name = "MembershipId")
    private String memberShipId;
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
     * Gets the MemberShipId under LineItem.
     *
     * @return        The MemberShipId under LineItem.
     */
    public final String getMemberShipId() {
        return memberShipId;
    }
    /**
     * Sets the MemberShipId under LineItem.
     *
     * @param expirationDateSet       The new value for the MemberShipId under LineItem.
     */
    public final void setMemberShipId(final String memberShipIdSet) {
        this.memberShipId = memberShipIdSet;
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
}
