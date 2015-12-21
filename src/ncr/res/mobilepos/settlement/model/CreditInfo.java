package ncr.res.mobilepos.settlement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CreditInfo")
public class CreditInfo {
    @XmlElement(name = "CompanyId")
    private String companyId;
    @XmlElement(name = "StoreId")
    private String storeId;
    @XmlElement(name = "CreditCompanyId")
    private String creditCompanyId;
    @XmlElement(name = "PaymentMethod")
    private String paymentMethod;
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;
    @XmlElement(name = "TrainingFlag")
    private int trainingFlag;
    @XmlElement(name = "TxType")
    private String txType;
    @XmlElement(name = "SalesItemCnt")
    private int salesItemCnt;
    @XmlElement(name = "SalesItemAmt")
    private double salesItemAmt;
    @XmlElement(name = "SalesCntSum")
    private int salesCntSum;
    @XmlElement(name = "SalesAmtSum")
    private double salesAmtSum;
    
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
    
    public final void setCreditCompanyId(String creditCompanyId) {
    	this.creditCompanyId = creditCompanyId;
    }
    
    public final String getCreditCompanyId() {
    	return creditCompanyId;
    }
    
    public final void setPaymentMethod(String paymentMethod) {
    	this.paymentMethod = paymentMethod;
    }
    
    public final String getPaymentMethod() {
    	return paymentMethod;
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
    
    public final void setTxType(String txType) {
    	this.txType = txType;
    }
    
    public final String getTxType() {
    	return txType;
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
    
    public final void setSalesCntSum(int salesCntSum) {
    	this.salesCntSum = salesCntSum;
    }
    
    public final int getSalesCntSum() {
    	return salesCntSum;
    }
    
    public final void setSalesAmtSum(double salesAmtSum) {
    	this.salesAmtSum = salesAmtSum;
    }
    
    public final double getSalesAmtSum() {
    	return salesAmtSum;
    }
}
