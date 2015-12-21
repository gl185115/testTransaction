package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PostPoint")
public class PostPoint {
    /**
     * 
     */
    @XmlElement(name = "Description")
    private String description;
    /**
     * 
     */
    @XmlElement(name = "CompanyId")
    private String companyId;
    /**
     * 
     */
    @XmlElement(name = "DivisionId")
    private String divisionId;
    /**
     * 
     */
    @XmlElement(name = "TransactionMode")
    private String transactionMode;
    /**
     * 
     */
    @XmlElement(name = "TransactionId")
    private String transactionId;
    /**
     * 
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;
    /**
     * 
     */
    @XmlElement(name = "EndDayDate")
    private String endDayDate;
    /**
     * 
     */
    @XmlElement(name = "EndTime")
    private String endTime;
    /**
     * 
     */
    @XmlElement(name = "CardClass")
    private String cardClass;
    /**
     * 
     */
    @XmlElement(name = "CardType")
    private String cardType;
    /**
     * 
     */
    @XmlElement(name = "StatusCode")
    private String statusCode;
    /**
     * 
     */
    @XmlElement(name = "BeforePointsTotal")
    private String beforePointsTotal;
    /**
     * 
     */
    @XmlElement(name = "CumulativePointsTotal")
    private String cumulativePointsTotal;
    /**
     * 
     */
    @XmlElement(name = "ProcessedTransaction")
    private ProcessedTransaction processedTransacton;
    /**
     * 
     */
    @XmlElement(name = "LastVisitedCompanyId")
    private String lastVisitedCompanyId;
    /**
     * 
     */
    @XmlElement(name = "UpdateProgramId")
    private String updateProgramId;
    /**
     * 
     */
    @XmlElement(name = "UpdateTerminalId")
    private String updateTerminalId;
    /**
     * 
     */
    @XmlElement(name = "UpdateCompanyId")
    private String updatecompanyId;
    /**
     * 
     */
    @XmlElement(name = "UpdateOperator")
    private String updateOperator;
    /**
     * 
     */
    @XmlElement(name = "MembershipId13")
    private String membershipId13;
    
    @XmlElement(name = "MembershipId16")
    private String membershipId16;
    
    @XmlElement(name = "InputtedMembershipId")
    private InputtedMembershipId inputtedMembershipId;
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
    public String getTransactionMode() {
    	return this.transactionMode;
    }
    /**
     * 
     * @param str
     */
    public void setTransactionMode(String str) {
    	this.transactionMode = str;
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
    public String getBeforePointsTotal() {
        return beforePointsTotal;
    }
    /**
     * 
     * @param beforePointsTotal
     */
    public void setBeforePointsTotal(String beforePointsTotal) {
        this.beforePointsTotal = beforePointsTotal;
    }
    /**
     * 
     * @return
     */
    public String getCumulativePointsTotal() {
        return cumulativePointsTotal;
    }
    /**
     * 
     * @param cumulativePointsTotal
     */
    public void setCumulativePointsTotal(String cumulativePointsTotal) {
        this.cumulativePointsTotal = cumulativePointsTotal;
    }
    /**
     * 
     * @return
     */
    public ProcessedTransaction getProcessedTransacton() {
        return processedTransacton;
    }
    /**
     * 
     * @param processedTransacton
     */
    public void setProcessedTransacton(ProcessedTransaction processedTransacton) {
        this.processedTransacton = processedTransacton;
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
     * @param membershipId16 the membershipId16 to set
     */
    public void setMembershipId16(String membershipId16) {
        this.membershipId16 = membershipId16;
    }
    @Override
    public String toString() {
        return "AddPointsLater [description=" + description + ", companyId=" + companyId + ", divisionId=" + divisionId
                + ", transactionId=" + transactionId + ", sequenceNumber=" + sequenceNumber + ", endDayDate="
                + endDayDate + ", endTime=" + endTime + ", cardClass=" + cardClass + ", cardType=" + cardType
                + ", statusCode=" + statusCode + ", beforePointsTotal=" + beforePointsTotal + ", cumulativePointsTotal="
                + cumulativePointsTotal + ", lastVisitedCompanyId=" + lastVisitedCompanyId + ", updateProgramId="
                + updateProgramId + ", updateTerminalId=" + updateTerminalId + ", updatecompanyId=" + updatecompanyId
                + ", updateOperator=" + updateOperator + "]";
    }
}
