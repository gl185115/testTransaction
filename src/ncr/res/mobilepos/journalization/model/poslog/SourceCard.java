package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SourceCard")
public class SourceCard {


    /**
     * The private member variable that holds
     * the CardCompanyCode of DestinationCard.
     */
    @XmlElement(name = "CreditCardCompanyCodeFrom")
    private String creditCardCompanyCodeFrom;

    // @XmlElement(name = "InputtedMembershipId")
    // private InputtedMembershipId inputtedMembershipId;
    /**
     * The private member variable that holds
     * the MemberCode1 of DestinationCard.
     */
    @XmlElement(name = "MembershipId16From")
    private String membershipId16From;

    /**
     * The private member variable that holds
     * the MemberCode2 of DestinationCard.
     */
    @XmlElement(name = "MembershipId13From")
    private String membershipId13From;
    /**
     * The private variable that holds the unifiedmembershipid
     */
    @XmlElement(name = "UnifiedMembershipIdFrom")
    private String unifiedMembershipIdFrom;
    /**
     * 
     */
    @XmlElement(name = "InputType")
    private String inputType;
    /**
     * 
     */
    @XmlElement(name = "Rank")
    private int rank;
    /**
     * 
     */
    @XmlElement(name = "Points")
    private int points;
    
    /**
     * 
     * @return
     */
    public String getCreditCardCompanyCodeFrom() {
        return creditCardCompanyCodeFrom;
    }
    /**
     * 
     * @param creditCardCompanyCodeFrom
     */
    public void setCreditCardCompanyCodeFrom(String creditCardCompanyCodeFrom) {
        this.creditCardCompanyCodeFrom = creditCardCompanyCodeFrom;
    }
    // /**
    //  * @param inputtedMembershipId the inputtedMembershipId to set
    //  */
    // public void setInputtedMembershipId(InputtedMembershipId inputtedMembershipId) {
    //     this.inputtedMembershipId = inputtedMembershipId;
    // }
    // /**
    //  * @return the inputtedMembershipId
    //  */
    // public InputtedMembershipId getInputtedMembershipId() {
    //     return inputtedMembershipId;
    // }
    /**
     * 
     * @return
     */
    public String getMembershipId16From() {
        return membershipId16From;
    }
    /**
     * 
     * @param membershipId16From
     */
    public void setMembershipId16From(String membershipId16From) {
        this.membershipId16From = membershipId16From;
    }
    /**
     * 
     * @return
     */
    public String getMembershipId13From() {
        return membershipId13From;
    }
    /**
     * 
     * @param membershipId13From
     */
    public void setMembershipId13From(String membershipId13From) {
        this.membershipId13From = membershipId13From;
    }
    /**
     * 
     * @return
     */
    public String getUnifiedMembershipIdFrom() {
        return unifiedMembershipIdFrom;
    }
    /**
     * 
     * @param unifiedMembershipIdFrom
     */
    public void setUnifiedMembershipIdFrom(String unifiedMembershipIdFrom) {
        this.unifiedMembershipIdFrom = unifiedMembershipIdFrom;
    }
    
    /**
     * @return the inputType
     */
    public String getInputType() {
        return inputType;
    }
    /**
     * @param inputType the inputType to set
     */
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    /**
     * @return the rank
     */
    public int getRank() {
        return rank;
    }
    /**
     * @param rank the rank to set
     */
    public void setRank(int rank) {
        this.rank = rank;
    }
    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }
    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "SourceCard [creditCardCompanyCodeFrom=" + creditCardCompanyCodeFrom + ", membershipId16From="
                + membershipId16From + ", membershipId13From=" + membershipId13From + ", unifiedMembershipIdFrom="
                + unifiedMembershipIdFrom + ", inputType=" + inputType + ", rank=" + rank + ", points=" + points + "]";
    }
   
}
