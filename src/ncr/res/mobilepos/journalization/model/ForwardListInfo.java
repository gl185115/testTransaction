package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
@XmlRootElement(name = "ForwardListInfo")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardListInfo")
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
    @ApiModelProperty(value="会社コード", notes="会社コード")
    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public String getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(String retailStoreId) {
        RetailStoreId = retailStoreId;
    }
    @ApiModelProperty(value="POSコード", notes="POSコード")
    public String getWorkstationId() {
        return WorkstationId;
    }

    public void setWorkstationId(String workstationId) {
        WorkstationId = workstationId;
    }
    @ApiModelProperty(value="取引番号", notes="取引番号")
    public String getSequenceNumber() {
        return SequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        SequenceNumber = sequenceNumber;
    }
    @ApiModelProperty(value="キュー番号", notes="キュー番号")
    public String getQueue() {
        return Queue;
    }

    public void setQueue(String queue) {
        Queue = queue;
    }
    @ApiModelProperty(value="POS業務日付", notes="POS業務日付")
    public String getBusinessDayDate() {
        return BusinessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        BusinessDayDate = businessDayDate;
    }
    @ApiModelProperty(value="トレーニングフラグ", notes="トレーニングフラグ")
    public String getTrainingFlag() {
        return TrainingFlag;
    }

    public void setTrainingFlag(String trainingFlag) {
        TrainingFlag = trainingFlag;
    }
    @ApiModelProperty(value="取引時刻", notes="取引時刻")
    public String getBusinessDateTime() {
        return BusinessDateTime;
    }

    public void setBusinessDateTime(String businessDateTime) {
        BusinessDateTime = businessDateTime;
    }
    @ApiModelProperty(value="担当者コード", notes="担当者コード")
    public String getOperatorId() {
        return OperatorId;
    }

    public void setOperatorId(String operatorId) {
        OperatorId = operatorId;
    }
    @ApiModelProperty(value="担当者名", notes="担当者名")
    public String getOperatorName() {
        return OperatorName;
    }

    public void setOperatorName(String operatorName) {
        OperatorName = operatorName;
    }
    @ApiModelProperty(value="取引合計金額", notes="取引合計金額")
    public String getSalesTotalAmt() {
        return SalesTotalAmt;
    }

    public void setSalesTotalAmt(String salesTotalAmt) {
        SalesTotalAmt = salesTotalAmt;
    }
    @ApiModelProperty(value="取引状態", notes="取引状態")
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
