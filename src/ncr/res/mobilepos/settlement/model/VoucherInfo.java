package ncr.res.mobilepos.settlement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "VoucherInfo")
public class VoucherInfo {
	@XmlElement(name = "CompanyId")
    private String companyId;
    @XmlElement(name = "StoreId")
    private String storeId;
    @XmlElement(name = "VoucherCompanyId")
    private String voucherCompanyId;
    @XmlElement(name = "VoucherType")
    private String voucherType;
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;
    @XmlElement(name = "TrainingFlag")
    private int trainingFlag;
    @XmlElement(name = "SalesItemCnt")
    private int salesItemCnt;
    @XmlElement(name = "SalesItemAmt")
    private double salesItemAmt;
    @XmlElement(name = "VoucherName")
    private String voucherName;
    @XmlElement(name = "VoucherKanaName")
    private String voucherKanaName;
    
    public final void setCompanyId(String companyId) {
    	this.companyId = companyId;
    }
    
    public final String getCompanyId() {
    	return companyId;
    }
    
    public final void setStoreId(String storeId) {
    	this.storeId = storeId;
    }
    
    public final String getStoreId() {
    	return storeId;
    }
    
    public final void setVoucherCompanyId(String voucherCompanyId) {
    	this.voucherCompanyId = voucherCompanyId;
    }
    
    public final String getVoucherCompanyId() {
    	return voucherCompanyId;
    }
    
    public final void setVoucherType(String voucherType) {
    	this.voucherType = voucherType;
    }
    
    public final String getVoucherType() {
    	return voucherType;
    }
    
    public final void setBusinessDayDate(String businessDayDate) {
    	this.businessDayDate = businessDayDate;
    }
    
    public final String getBusinessDayDate() {
    	return businessDayDate;
    }
    
    public final void setTrainingFlag(int trainingFlag) {
    	this.trainingFlag = trainingFlag;
    }
    
    public final int getTrainingFlag() {
    	return trainingFlag;
    }
    
    public final void setSalesItemCnt(int salesItemCnt) {
    	this.salesItemCnt = salesItemCnt;
    }
    
    public final int getSalesItemCnt() {
    	return salesItemCnt;
    }
    
    public final void setSalesItemAmt(double salesItemAmt) {
    	this.salesItemAmt = salesItemAmt;
    }
    
    public final double getSalesItemAmt() {
    	return salesItemAmt;
    }
    
    public final void setVoucherName(String voucherName) {
    	this.voucherName = voucherName;
    }
    
    public final String getVoucherName() {
    	return voucherName;
    }
    
    public final void setVoucherKanaName(String voucherKanaName) {
    	this.voucherKanaName = voucherKanaName;
    }
    
    public final String getVoucherKanaName() {
    	return voucherKanaName;
    }
}
