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

    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public String getCompanyId() {
        return CompanyId;
    }

    public void setCompanyId(String companyId) {
        CompanyId = companyId;
    }

    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
    public String getRetailStoreId() {
        return RetailStoreId;
    }

    public void setRetailStoreId(String retailStoreId) {
        RetailStoreId = retailStoreId;
    }

    public void setWorkstationId(String workstationId) {
        WorkstationId = workstationId;
    }

    @ApiModelProperty(value="POS�R�[�h", notes="POS�R�[�h")
    public String getWorkstationId() {
        return WorkstationId;
    }


    @ApiModelProperty(value="�L���[�ԍ�", notes="�L���[�ԍ�")
    public String getQueue() {
        return Queue;
    }

    public void setQueue(String queue) {
        Queue = queue;
    }

    @ApiModelProperty(value="�g���[�j���O�t���O", notes="�g���[�j���O�t���O")
    public String getTrainingFlag() {
        return TrainingFlag;
    }

    public void setTrainingFlag(String trainingFlag) {
        TrainingFlag = trainingFlag;
    }

    @ApiModelProperty(value="POS�Ɩ����t", notes="POS�Ɩ����t")
    public String getBusinessDayDate() {
        return BusinessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        BusinessDayDate = businessDayDate;
    }

    @ApiModelProperty(value="������", notes="������")
    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    @ApiModelProperty(value="����ԍ�", notes="����ԍ�")
    public String getSequenceNumber() {
		return SequenceNumber;
	}

	public void setSequenceNumber(String sequenceNumber) {
		SequenceNumber = sequenceNumber;
	}

    @ApiModelProperty(value="�������", notes="�������")
	public String getBusinessDateTime() {
		return BusinessDateTime;
	}

	public void setBusinessDateTime(String businessDateTime) {
		BusinessDateTime = businessDateTime;
	}

    @ApiModelProperty(value="�S���҃R�[�h", notes="�S���҃R�[�h")
	public String getOperatorId() {
		return OperatorId;
	}

	public void setOperatorId(String operatorId) {
		OperatorId = operatorId;
	}

    @ApiModelProperty(value="������v���z", notes="������v���z")
	public String getSalesTotalAmt() {
		return SalesTotalAmt;
	}

	public void setSalesTotalAmt(String salesTotalAmt) {
		SalesTotalAmt = salesTotalAmt;
	}

    @ApiModelProperty(value="������v�_��", notes="������v�_��")
	public String getSalesTotalQty() {
		return SalesTotalQty;
	}

	public void setSalesTotalQty(String salesTotalQty) {
		SalesTotalQty = salesTotalQty;
	}

    @ApiModelProperty(value="���Z�@�̒[���ԍ�", notes="���Z�@�̒[���ԍ�")
	public String getExt2() {
		return Ext2;
	}

	public void setExt2(String ext2) {
		Ext2 = ext2;
	}

    @ApiModelProperty(value="���Z�@�̒[������", notes="���Z�@�̒[������")
	public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}
}
