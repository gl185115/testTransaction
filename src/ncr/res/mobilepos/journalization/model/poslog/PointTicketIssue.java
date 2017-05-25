package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PointTicketIssue")
public class PointTicketIssue {
    /**
     * private variable that holds the description
     */
    @XmlElement(name = "Description")
    private String description;
    /**
     *  private variable that holds the company id
     */
    @XmlElement(name = "CompanyId")
    private String companyId;
    /**
     *  private variable that holds the division id
     */
    @XmlElement(name = "DivisionId")
    private String divisionId;
    /**
     *  private variable that holds the transaction Id
     */
    @XmlElement(name = "TransactionId")
    private String transactionId;
    /**
     *  private variable that holds the sequence number
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;
    /**
     *  private variable that holds the end day date
     */
    @XmlElement(name = "EndDayDate")
    private String endDayDate;
    /**
     *  private variable that holds the end time
     */
    @XmlElement(name = "EndTime")
    private String endTime;
    /**
     *  private variable that holds the card class
     */
    @XmlElement(name = "CardClass")
    private String cardClass;
    /**
     *  private variable that holds the card type
     */
    @XmlElement(name = "CardType")
    private String cardType;
    /**
     *  private variable that holds the status code
     */
    @XmlElement(name = "StatusCode")
    private String statusCode;
    /**
     *  private variable that holds the points prior
     */
    @XmlElement(name = "PointsPrior")
    private String pointsPrior;
    /**
     *  private variable that holds the points redeemed
     */
    @XmlElement(name = "PointsRedeemed")
    private String pointsRedeemed;
    /**
     *  private variable that holds the unified memberhsip id
     */
    @XmlElement(name = "UnifiedMembershipId")
    private String unifiedMembershipId;
    /**
     *  private variable that holds the credit card company code
     */
    @XmlElement(name = "CreditCardCompanyCode")
    private String creditCardCompanyCode;
    /**
     *  private variable that holds the membership id 13
     */
    @XmlElement(name = "MembershipId13")
    private String membershipId13;
    /**
     *  private variable that holds the membership id 16
     */
    @XmlElement(name = "MembershipId16")
    private String membershipId16;
    
    // @XmlElement(name = "InputtedMembershipId")
    // private InputtedMembershipId inputtedMembershipId;
    /**
     *  private variable that holds the last visited company
     */
    @XmlElement(name = "LastVisitedCompanyId")
    private String lastVisitedCompanyId;
    /**
     *  private variable that holds the program id
     */
    @XmlElement(name = "UpdateProgramId")
    private String updateProgramId;
    /**
     *  private variable that holds the terminal id
     */
    @XmlElement(name = "UpdateTerminalId")
    private String updateTerminalId;
    /**
     *  private variable that holds the company id that executes update
     */
    @XmlElement(name = "UpdateCompanyId")
    private String updatecompanyId;
    /** private variable that holds the operator that executes update
     * 
     */
    @XmlElement(name = "UpdateOperator")
    private String updateOperator;
    
    @XmlElement(name = "TicketCount")
    private String ticketCounter;
    /**
     * 
     * @return
     */
    public String getDescription() {
        return description;
    }
    /**
     * 
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }
    /**
     * 
     * @return
     */
    public String getCompanyId() {
        return companyId;
    }
    /**
     * 
     * @param companyId
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    /**
     * 
     * @return
     */
    public String getDivisionId() {
        return divisionId;
    }
    /**
     * 
     * @param divisionId
     */
    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }
    /**
     * 
     * @return
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
     * @return
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * 
     * @param sequenceNumber
     */
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    /**
     * 
     * @return
     */
    public String getEndDayDate() {
        return endDayDate;
    }
    /**
     * 
     * @param endDayDate
     */
    public void setEndDayDate(String endDayDate) {
        this.endDayDate = endDayDate;
    }
    /**
     * 
     * @return
     */
    public String getEndTime() {
        return endTime;
    }
    /**
     * 
     * @param endTime
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
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
     * @param cardClass
     */
    public void setCardClass(String cardClass) {
        this.cardClass = cardClass;
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
    public String getStatusCode() {
        return statusCode;
    }
    /**
     * 
     * @param statusCode
     */
    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }
    /**
     * 
     * @return
     */
    public String getLastVisitedCompanyId() {
        return lastVisitedCompanyId;
    }
    /**
     * 
     * @param lastVisitedCompanyId
     */
    public void setLastVisitedCompanyId(String lastVisitedCompanyId) {
        this.lastVisitedCompanyId = lastVisitedCompanyId;
    }
    /**
     * 
     * @return
     */
    public String getUpdateProgramId() {
        return updateProgramId;
    }
    /**
     * 
     * @param updateProgramId
     */
    public void setUpdateProgramId(String updateProgramId) {
        this.updateProgramId = updateProgramId;
    }
    /**
     * 
     * @return
     */
    public String getUpdateTerminalId() {
        return updateTerminalId;
    }
    /**
     * 
     * @param updateTerminalId
     */
    public void setUpdateTerminalId(String updateTerminalId) {
        this.updateTerminalId = updateTerminalId;
    }
    /**
     * 
     * @return
     */
    public String getUpdatecompanyId() {
        return updatecompanyId;
    }
    /**
     * 
     * @param updatecompanyId
     */
    public void setUpdatecompanyId(String updatecompanyId) {
        this.updatecompanyId = updatecompanyId;
    }
    /**
     * 
     * @return
     */
    public String getUpdateOperator() {
        return updateOperator;
    }
    /**
     * 
     * @param updateOperator
     */
    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }
    /**
     * @return the pointsPrior
     */
    public String getPointsPrior() {
        return pointsPrior;
    }
    /**
     * @param pointsPrior the pointsPrior to set
     */
    public void setPointsPrior(String pointsPrior) {
        this.pointsPrior = pointsPrior;
    }
    /**
     * @return the pointsRedeemed
     */
    public String getPointsRedeemed() {
        return pointsRedeemed;
    }
    /**
     * @param pointsRedeemed the pointsRedeemed to set
     */
    public void setPointsRedeemed(String pointsRedeemed) {
        this.pointsRedeemed = pointsRedeemed;
    }
    /**
     * @return the unifiedMembershipId
     */
    public String getUnifiedMembershipId() {
        return unifiedMembershipId;
    }
    /**
     * @param unifiedMembershipId the unifiedMembershipId to set
     */
    public void setUnifiedMembershipId(String unifiedMembershipId) {
        this.unifiedMembershipId = unifiedMembershipId;
    }
    /**
     * @return the creditCardCompanyCode
     */
    public String getCreditCardCompanyCode() {
        return creditCardCompanyCode;
    }
    /**
     * @param creditCardCompanyCode the creditCardCompanyCode to set
     */
    public void setCreditCardCompanyCode(String creditCardCompanyCode) {
        this.creditCardCompanyCode = creditCardCompanyCode;
    }
    /**
     * @return the membershipId13
     */
    public String getMembershipId13() {
        return membershipId13;
    }
    /**
     * @param membershipId13 the membershipId13 to set
     */
    public void setMembershipId13(String membershipId13) {
        this.membershipId13 = membershipId13;
    }
    /**
     * @return the membershipId16
     */
    public String getMembershipId16() {
        return membershipId16;
    }
    
    /**
     * @param membershipId16 the membershipId16 to set
     */
    public void setMembershipId16(String membershipId16) {
        this.membershipId16 = membershipId16;
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
     * @return the ticketCounter
     */
    public String getTicketCounter() {
        return ticketCounter;
    }
    /**
     * @param ticketCounter the ticketCounter to set
     */
    public void setTicketCounter(String ticketCounter) {
        this.ticketCounter = ticketCounter;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "PointTicketIssue [description=" + description + ", companyId=" + companyId + ", divisionId="
                + divisionId + ", transactionId=" + transactionId + ", sequenceNumber=" + sequenceNumber
                + ", endDayDate=" + endDayDate + ", endTime=" + endTime + ", cardClass=" + cardClass + ", cardType="
                + cardType + ", statusCode=" + statusCode + ", pointsPrior=" + pointsPrior + ", pointsRedeemed="
                + pointsRedeemed + ", unifiedMembershipId=" + unifiedMembershipId + ", creditCardCompanyCode="
                + creditCardCompanyCode + ", membershipId13=" + membershipId13 + ", membershipId16=" + membershipId16
                + ", lastVisitedCompanyId=" + lastVisitedCompanyId + ", updateProgramId=" + updateProgramId
                + ", updateTerminalId=" + updateTerminalId + ", updatecompanyId=" + updatecompanyId
                + ", updateOperator=" + updateOperator + "]";
    }
    
  
}
