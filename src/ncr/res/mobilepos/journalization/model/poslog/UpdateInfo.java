package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "UpdateInfo")
public class UpdateInfo {

    /**
     * The private variable that holds the value of updateType
     */
    @XmlAttribute(name = "UpdateType")
    private String updateType;
	/**
     * The private member variable that holds
     * the Description of UpdateInfo.
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * The private member variable that holds
     * the CompanyId of UpdateInfo.
     */
    @XmlElement(name = "CompanyId")
    private String companyId;

    /**
     * The private member variable that holds
     * the DivisionId of UpdateInfo.
     */
    @XmlElement(name = "DivisionId")
    private String divisionId;
        
    /**
     * The private member variable that holds
     * the TransactionId of UpdateInfo.
     */
    @XmlElement(name = "TransactionId")
    private String transactionId;
    
    /**
     * The private member variable that holds
     * the SequenceNumber of UpdateInfo.
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;
    
    /**
     * The private member variable that holds
     * the EndDayDate of UpdateInfo.
     */
    @XmlElement(name = "EndDayDate")
    private String endDayDate;
    
    /**
     * The private member variable that holds
     * the EndTime of UpdateInfo.
     */
    @XmlElement(name = "EndTime")
    private String endTime;
    
    @XmlElement(name = "CurrentPoint")
    private String currentPoint;
    
    /**
     * The private member variable that holds
     * the LastVisitedCompanyId of UpdateInfo.
     */
    @XmlElement(name = "LastVisitedCompanyId")
    private String lastVisitedCompanyId;
    
    /**
     * The private member variable that holds
     * the UpdateProgramId of UpdateInfo.
     */
    @XmlElement(name = "UpdateProgramId")
    private String updateProgramId;
    
    /**
     * The private member variable that holds
     * the UpdateTerminalId of UpdateInfo.
     */
    @XmlElement(name = "UpdateTerminalId")
    private String updateTerminalId;
    
    /**
     * The private member variable that holds
     * the UpdateCompanyId of UpdateInfo.
     */
    @XmlElement(name = "UpdateCompanyId")
    private String updateCompanyId;
        
    /**
     * The private member variable that holds
     * the UpdateOperator of UpdateInfo.
     */
    @XmlElement(name = "UpdateOperator")
    private String updateOperator;
    /**
     * The private member SourceCard
     */
    @XmlElement(name = "SourceCard")
    private SourceCard sourceCard;
    
	/**
	 * Element for DestinationCard.
	 */
	@XmlElement(name = "DestinationCard")
	private DestinationCard destinationCard;
	/**
	 * return member sourcecard
	 * @return
	 */
	public SourceCard getSourceCard() {
		return this.sourceCard;
	}
	/**
	 * set member sourcecard
	 * @param sc
	 */
	public void setSourceCard(SourceCard sc) {
		this.sourceCard = sc;
	}
	
    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }
    
    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    
    /**
     * @return the divisionId
     */
    public String getDivisionId() {
        return divisionId;
    }

    /**
     * @param divisionId the divisionId to set
     */
    public void setDivisionId(String divisionId) {
        this.divisionId = divisionId;
    }
    
    /**
     * @return the transactionId
     */
    public String getTransactionId() {
        return transactionId;
    }

    /**
     * @param transactionId the transactionId to set
     */
    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    
    /**
     * @return the sequenceNumber
     */
    public String getSequenceNumber() {
        return sequenceNumber;
    }

    /**
     * @param sequenceNumber the sequenceNumber to set
     */
    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }
    
    /**
     * @return the endDayDate
     */
    public String getEndDayDate() {
        return endDayDate;
    }

    /**
     * @param endDayDate the endDayDate to set
     */
    public void setEndDayDate(String endDayDate) {
        this.endDayDate = endDayDate;
    }
    
    /**
     * @return the endTime
     */
    public String getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    
    /**
     * @return the lastVisitedCompanyId
     */
    public String getLastVisitedCompanyId() {
        return lastVisitedCompanyId;
    }

    /**
     * @param lastVisitedCompanyId the lastVisitedCompanyId to set
     */
    public void setLastVisitedCompanyId(String lastVisitedCompanyId) {
        this.lastVisitedCompanyId = lastVisitedCompanyId;
    }
    
    /**
     * @return the updateProgramId
     */
    public String getUpdateProgramId() {
        return updateProgramId;
    }

    /**
     * @param updateProgramId the updateProgramId to set
     */
    public void setUpdateProgramId(String updateProgramId) {
        this.updateProgramId = updateProgramId;
    }
    
    /**
     * @return the updateTerminalId
     */
    public String getUpdateTerminalId() {
        return updateTerminalId;
    }

    /**
     * @param updateTerminalId the updateTerminalId to set
     */
    public void setUpdateTerminalId(String updateTerminalId) {
        this.updateTerminalId = updateTerminalId;
    }
    /**
     * @return the updateCompanyId
     */
    public String getUpdateCompanyId() {
        return updateCompanyId;
    }

    /**
     * @param updateCompanyId the updateCompanyId to set
     */
    public void setUpdateCompanyId(String updateCompanyId) {
        this.updateCompanyId = updateCompanyId;
    }
    /**
     * @return the updateOperator
     */
    public String getUpdateOperator() {
        return updateOperator;
    }

    /**
     * @param updateOperator the updateOperator to set
     */
    public void setUpdateOperator(String updateOperator) {
        this.updateOperator = updateOperator;
    }
        
    /**
	 * @return    The DestinationCard transaction details.
	 */
	public final DestinationCard getDestinationCard() {
		return destinationCard;
	}
	
	/**
	 *  Sets the DestinationCard details..
	 * @param destinationCard    The DestinationCard details.
	 */
	public final void setDestinationCard(DestinationCard destinationCardToSet) {
		this.destinationCard = destinationCardToSet;
	}
	/**
	 * 
	 * @return
	 */
    public String getUpdateType() {
        return updateType;
    }
    /**
     * 
     * @param updateType
     */
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
    /**
     * 
     * @return
     */
    public String getCurrentPoint() {
        return currentPoint;
    }
    /**
     * 
     * @param currentPoint
     */
    public void setCurrentPoint(String currentPoint) {
        this.currentPoint = currentPoint;
    }
	
}
