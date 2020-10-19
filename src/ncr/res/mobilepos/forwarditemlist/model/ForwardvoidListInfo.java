package ncr.res.mobilepos.forwarditemlist.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "ForwardvoidListInfo")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardvoidListInfo")
public class ForwardvoidListInfo {

    @XmlElement(name = "CompanyId")
    private String CompanyId;

    @XmlElement(name = "RetailStoreId")
    private String RetailStoreId;

    @XmlElement(name = "WorkstationId")
    private String WorkstationId;

    @XmlElement(name = "Queue")
    private String Queue;

    @XmlElement(name = "TrainingFlag")
    private String TrainingFlag;

    @XmlElement(name = "BusinessDayDate")
    private String BusinessDayDate;

    @XmlElement(name = "Status")
    private String Status;

	@XmlElement(name = "SequenceNumber")
    private String SequenceNumber;

    @XmlElement(name = "BusinessDateTime")
    private String BusinessDateTime;

    @XmlElement(name = "OperatorId")
    private String OperatorId;

    @XmlElement(name = "SalesTotalAmt")
    private String SalesTotalAmt;

    @XmlElement(name = "SalesTotalQty")
    private String SalesTotalQty;

    @XmlElement(name = "Ext2")
    private String Ext2;

    @XmlElement(name = "DeviceName")
    private String DeviceName;

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

    public void setWorkstationId(String workstationId) {
        WorkstationId = workstationId;
    }

    @ApiModelProperty(value="POSコード", notes="POSコード")
    public String getWorkstationId() {
        return WorkstationId;
    }


    @ApiModelProperty(value="キュー番号", notes="キュー番号")
    public String getQueue() {
        return Queue;
    }

    public void setQueue(String queue) {
        Queue = queue;
    }

    @ApiModelProperty(value="トレーニングフラグ", notes="トレーニングフラグ")
    public String getTrainingFlag() {
        return TrainingFlag;
    }

    public void setTrainingFlag(String trainingFlag) {
        TrainingFlag = trainingFlag;
    }

    @ApiModelProperty(value="POS業務日付", notes="POS業務日付")
    public String getBusinessDayDate() {
        return BusinessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        BusinessDayDate = businessDayDate;
    }

    @ApiModelProperty(value="取引状態", notes="取引状態")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @ApiModelProperty(value="取引番号", notes="取引番号")
    public String getSequenceNumber() {
		return SequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		SequenceNumber = sequenceNumber;
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

    @ApiModelProperty(value="取引合計金額", notes="取引合計金額")
	public String getSalesTotalAmt() {
		return SalesTotalAmt;
	}

	public void setSalesTotalAmt(String salesTotalAmt) {
		SalesTotalAmt = salesTotalAmt;
	}

    @ApiModelProperty(value="取引合計点数", notes="取引合計点数")
	public String getSalesTotalQty() {
		return SalesTotalQty;
	}

	public void setSalesTotalQty(String salesTotalQty) {
		SalesTotalQty = salesTotalQty;
	}

    @ApiModelProperty(value="精算機の端末番号", notes="精算機の端末番号")
	public String getExt2() {
		return Ext2;
	}

	public void setExt2(String ext2) {
		Ext2 = ext2;
	}

    @ApiModelProperty(value="精算機の端末名称", notes="精算機の端末名称")
	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}
}
