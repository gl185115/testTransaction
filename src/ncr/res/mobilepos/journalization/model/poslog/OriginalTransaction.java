package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Original Transaction Model Object.
 *
 * <P>A OrignalTranaction Node in POSLog XML.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OriginalTransaction")
public class OriginalTransaction {
    /**
     * 
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreId;
    /**
     * 
     */
    @XmlElement(name = "WorkstationID")
    private String workstationId;
    /**
     * 
     */
    @XmlElement(name = "OrganizationHierarchy")
    private OrganizationHierarchy organizationHierarchy;
    /**
     * 
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;
    /**
     * 
     */
    @XmlElement(name = "BusinessDayDate")
    private String BusinessDayDate;
    /**
     * 
     */
    @XmlElement(name = "OperatorID")
    private OperatorID operatorId;
    /**
     * 
     */
    @XmlElement(name = "ReceiptDateTime")
    private String receiptDateTime;
    /**
     * 
     */
    @XmlElement(name = "TillID")
    private String tillId;
    /**
     * 
     * @return
     */
    public String getRetailStoreId() {
        return retailStoreId;
    }
    /**
     * 
     * @param retailStoreId
     */
    public void setRetailStoreId(String retailStoreId) {
        this.retailStoreId = retailStoreId;
    }
    /**
     * 
     * @return
     */
    public String getWorkstationId() {
        return workstationId;
    }
    /**
     * 
     * @param workstationId
     */
    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }
    /**
     * 
     * @return
     */
    public OrganizationHierarchy getOrganizationHierarchy() {
        return organizationHierarchy;
    }
    /**
     * 
     * @param organizationHierarchy
     */
    public void setOrganizationHierarchy(OrganizationHierarchy organizationHierarchy) {
        this.organizationHierarchy = organizationHierarchy;
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
    public String getBusinessDayDate() {
        return BusinessDayDate;
    }
    /**
     * 
     * @param businessDayDate
     */
    public void setBusinessDayDate(String businessDayDate) {
        BusinessDayDate = businessDayDate;
    }
    /**
     * 
     * @return
     */
    public OperatorID getOperatorId() {
        return operatorId;
    }
    /**
     * 
     * @param operatorId
     */
    public void setOperatorId(OperatorID operatorId) {
        this.operatorId = operatorId;
    }
    /**
     * 
     * @return
     */
    public String getReceiptDateTime() {
        return receiptDateTime;
    }
    /**
     * 
     * @param receiptDateTime
     */
    public void setReceiptDateTime(String receiptDateTime) {
        this.receiptDateTime = receiptDateTime;
    }
    /**
     * 
     * @return
     */
    public String getTillId() {
        return tillId;
    }
    /**
     * 
     * @param tillId
     */
    public void setTillId(String tillId) {
        this.tillId = tillId;
    }
    /**
     * 
     */
    @Override
    public String toString() {
        return "OriginalTransaction [retailStoreId=" + retailStoreId + ", workstationId=" + workstationId
                + ", organizationHierarchy=" + organizationHierarchy + ", sequenceNumber=" + sequenceNumber
                + ", BusinessDayDate=" + BusinessDayDate + ", operatorId=" + operatorId + ", receiptDateTime="
                + receiptDateTime + ", tillId=" + tillId + "]";
    }
    

}
