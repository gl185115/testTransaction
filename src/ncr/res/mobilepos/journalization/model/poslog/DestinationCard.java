package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DestinationCard")
public class DestinationCard {

	/**
     * The private member variable that holds
     * the creditCardCompanyCodeTo of DestinationCard.
     */
    @XmlElement(name = "CreditCardCompanyCodeTo")
    private String creditCardCompanyCodeTo;

    @XmlElement(name = "UnifiedMembershipIdTo")
    private String unifiedMembershipIdTo;

    @XmlElement(name = "InputtedMembershipId")
    private InputtedMembershipId inputtedMembershipId;
    /**
     * The private member variable that holds
     * the membershipId16To of DestinationCard.
     */
    @XmlElement(name = "MembershipId16To")
    private String membershipId16To;

    /**
     * The private member variable that holds
     * the membershipId13To of DestinationCard.
     */
    @XmlElement(name = "MembershipId13To")
    private String membershipId13To;
    
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
     * @return the cardCompanyCode
     */
    public String getCreditCardCompanyCodeTo() {
        return creditCardCompanyCodeTo;
    }

    /**
     * @param creditCardCompanyCodeTo the creditCardCompanyCodeTo to set
     */
    public void setCreditCardCompanyCodeTo(String creditCardCompanyCodeTo) {
        this.creditCardCompanyCodeTo = creditCardCompanyCodeTo;
    }

	    /**
     * @return the unifiedMembershipIdTo
     */
    public String getUnifiedMembershipIdTo() {
        return unifiedMembershipIdTo;
    }

    /**
     * @param unifiedMembershipIdTo the unifiedMembershipIdTo to set
     */
    public void setUnifiedMembershipIdTo(String unifiedMembershipIdTo) {
        this.unifiedMembershipIdTo = unifiedMembershipIdTo;
    }

    /**
     * @param inputtedMembershipId the inputtedMembershipId to set
     */
    public void setInputtedMembershipId(InputtedMembershipId inputtedMembershipId) {
        this.inputtedMembershipId = inputtedMembershipId;
    }
    /**
     * @return the inputtedMembershipId
     */
    public InputtedMembershipId getInputtedMembershipId() {
        return inputtedMembershipId;
    }

    /**
     * @return the membershipId16To
     */
    public String getMembershipId16To() {
        return membershipId16To;
    }

    /**
     * @param membershipId16To the membershipId16To to set
     */
    public void setMembershipId16To(String membershipId16To) {
        this.membershipId16To = membershipId16To;
    }
    
    /**
     * @return the membershipId13To
     */
    public String getMembershipId13To() {
        return membershipId13To;
    }

    /**
     * @param membershipId13To the membershipId13To to set
     */
    public void setMembershipId13To(String membershipId13To) {
        this.membershipId13To = membershipId13To;
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
    public void setRand(int rank) {
        this.rank = rank;
    }

	
    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

    /**
     * @param rank the rank to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "DestinationCard [creditCardCompanyCodeTo=" + creditCardCompanyCodeTo + ", membershipId16To="
                + membershipId16To + ", membershipId13To=" + membershipId13To + ", inputType=" + inputType + ", rank="
                + rank + ", points=" + points + "]";
    }
    
    
}
