package ncr.res.mobilepos.ej.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "EjInfo")
@ApiModel(value="EjInfo")
public class EjInfo {
	@XmlElement(name = "CompanyId")
    private String companyId;

	@XmlElement(name = "StoreId")
    private String storeId;

	@XmlElement(name = "WorkstationId")
    private String workstationId;

    @XmlElement(name = "TxType")
    private String txType;

    @XmlElement(name = "NameCategory")
    private String nameCategory;

    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;

    @XmlElement(name = "TxTypeName")
    private String txTypeName;

    @XmlElement(name = "BillingAmt")
    private String billingAmt;

    @XmlElement(name = "BusinessDateTimeStart")
    private String businessDateTimeStart;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getWorkstationId() {
        return workstationId;
    }

    public void setWorkstationId(String workstationId) {
        this.workstationId = workstationId;
    }

    public String getTxType() {
        return txType;
    }

    public void setTxType(String txType) {
        this.txType = txType;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getTxTypeName() {
        return txTypeName;
    }

    public void setTxTypeName(String txTypeName) {
        this.txTypeName = txTypeName;
    }

    public String getBillingAmt() {
        return billingAmt;
    }

    public void setBillingAmt(String billingAmt) {
        this.billingAmt = billingAmt;
    }

    public String getBusinessDateTimeStart() {
        return businessDateTimeStart;
    }

    public void setBusinessDateTimeStart(String businessDateTimeStart) {
        this.businessDateTimeStart = businessDateTimeStart;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }
}
