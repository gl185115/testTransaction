package ncr.res.mobilepos.ej.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PosLogInfo")
@ApiModel(value="PosLogInfo")
public class PosLogInfo extends ResultBase{
	@XmlElement(name = "CompanyId")
	private String companyId;

	@XmlElement(name = "RetailStoreId")
	private String retailStoreId;

	@XmlElement(name = "WorkstationId")
	private String workstationId;

	@XmlElement(name = "SequenceNumber")
	private String sequenceNumber;

	@XmlElement(name = "BusinessDate")
	private String businessDate;

	@XmlElement(name = "TrainingFlag")
	private String trainingFlag;

	@XmlElement(name = "POSLog")
	private String posLog;

	public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRetailStoreId() {
        return retailStoreId;
    }

    public void setRetailStoreId(String retailStoreId) {
        this.retailStoreId = retailStoreId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getBusinessDate() {
        return businessDate;
    }

    public void setBusinessDate(String businessDate) {
        this.businessDate = businessDate;
    }

    public String getTrainingFlag() {
        return trainingFlag;
    }

    public void setTrainingFlag(String trainingFlag) {
        this.trainingFlag = trainingFlag;
    }

    public String getPOSLog() {
        return posLog;
    }

    public void setPOSLog(String posLog) {
        this.posLog = posLog;
    }

	@Override
	public final String toString() {
		ObjectMapper mapper = new ObjectMapper();
		String ret = "";
		try {
			ret += mapper.writeValueAsString(this);
		} catch (Exception ex) {
			ret = super.toString();
		}
		return ret;
	}
}
