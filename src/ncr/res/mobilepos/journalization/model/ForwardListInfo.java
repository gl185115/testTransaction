package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement(name = "ForwardListInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class ForwardListInfo {

    @XmlElement(name = "CompanyId")
    private String CompanyId;

    @XmlElement(name = "RetailStoreId")
    private String RetailStoreId;

    @XmlElement(name = "WorkstationId")
    private String WorkstationId;

    @XmlElement(name = "SequenceNumber")
    private String SequenceNumber;

    @XmlElement(name = "Queue")
    private String Queue;

    @XmlElement(name = "BusinessDayDate")
    private String BusinessDayDate;

    @XmlElement(name = "TrainingFlag")
    private String TrainingFlag;

    @XmlElement(name = "BusinessDateTime")
    private String BusinessDateTime;

    @XmlElement(name = "OperatorId")
    private String OperatorId;

    @XmlElement(name = "OperatorName")
    private String OperatorName;
    
    @XmlElement(name = "SalesTotalAmt")
    private String SalesTotalAmt;

    @XmlElement(name = "Status")
    private String Status;

    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    public String getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(String retailStoreId) {
        RetailStoreId = retailStoreId;
    }

    public String getWorkstationId() {
        return WorkstationId;
    }

    public void setWorkstationId(String workstationId) {
        WorkstationId = workstationId;
    }

    public String getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }

    public String getQueue() {
        return Queue;
    }

    public void setQueue(String queue) {
        Queue = queue;
    }

    public String getBusinessDayDate() {
        return BusinessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        BusinessDayDate = businessDayDate;
    }

    public String getTrainingFlag() {
        return TrainingFlag;
    }

    public void setTrainingFlag(String trainingFlag) {
        TrainingFlag = trainingFlag;
    }

    public String getBusinessDateTime() {
        return BusinessDateTime;
    }

    public void setBusinessDateTime(String businessDateTime) {
        BusinessDateTime = businessDateTime;
    }

    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        OperatorId = operatorId;
    }

    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }

    public String getSalesTotalAmt() {
        return SalesTotalAmt;
    }

    public void setSalesTotalAmt(String salesTotalAmt) {
        SalesTotalAmt = salesTotalAmt;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        String crlf = "\r\n";

        sb.append(crlf).append("CompanyId: ").append(this.CompanyId).append(crlf)
                .append("RetailStoreId: ").append(this.RetailStoreId).append(crlf)
                .append("WorkstationId: ").append(this.WorkstationId).append(crlf)
                .append("SequenceNumber: ").append(this.SequenceNumber).append(crlf)
                .append("Queue: ").append(this.Queue).append(crlf)
                .append("BusinessDayDate: ").append(this.BusinessDayDate).append(crlf)
                .append("TrainingFlag: ").append(this.TrainingFlag).append(crlf)
                .append("BusinessDateTime: ").append(this.BusinessDateTime).append(crlf)
                .append("Status: ").append(this.Status).append(crlf)
                .append("OperatorId: ").append(this.OperatorId).append(crlf)
                .append("OperatorName: ").append(this.OperatorName).append(crlf)
                .append("SalesTotalAmt: ").append(this.SalesTotalAmt);

        return sb.toString();
    }
}
