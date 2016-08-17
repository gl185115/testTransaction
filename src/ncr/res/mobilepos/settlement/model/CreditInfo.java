package ncr.res.mobilepos.settlement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CreditInfo")
@ApiModel(value="CreditInfo")
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
    
    @ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCompanyId() {
    	return companyId;
    }
    
    public final void setStoreId(String storeId) {
    	this.storeId = storeId;
    }
    
    @ApiModelProperty(value="店舗番号", notes="店舗番号")
    public final String getStoreId() {
    	return storeId;
    }
    
    public final void setCreditCompanyId(String creditCompanyId) {
    	this.creditCompanyId = creditCompanyId;
    }
    
    @ApiModelProperty(value="クレジット会社コード", notes="クレジット会社コード")
    public final String getCreditCompanyId() {
    	return creditCompanyId;
    }
    
    public final void setPaymentMethod(String paymentMethod) {
    	this.paymentMethod = paymentMethod;
    }
    
    @ApiModelProperty(value="支払方法", notes="支払方法")
    public final String getPaymentMethod() {
    	return paymentMethod;
    }
    
    public final void setBusinessDayDate(String businessDayDate) {
    	this.businessDayDate = businessDayDate;
    }
    
    @ApiModelProperty(value="取引日付", notes="取引日付")
    public final String getBusinessDayDate() {
    	return businessDayDate;
    }
    
    public final void setTrainingFlag(int trainingFlag) {
    	this.trainingFlag = trainingFlag;
    }
    
    @ApiModelProperty(value="トレーニングフラグ", notes="トレーニングフラグ")
    public final int getTrainingFlag() {
    	return trainingFlag;
    }
    
    public final void setTxType(String txType) {
    	this.txType = txType;
    }
    
    @ApiModelProperty(value="取引種別", notes="取引種別")
    public final String getTxType() {
    	return txType;
    }
    
    public final void setSalesItemCnt(int salesItemCnt) {
    	this.salesItemCnt = salesItemCnt;
    }
    
    @ApiModelProperty(value="売上数量", notes="売上数量")
    public final int getSalesItemCnt() {
    	return salesItemCnt;
    }
    
    public final void setSalesItemAmt(double salesItemAmt) {
    	this.salesItemAmt = salesItemAmt;
    }
    
    @ApiModelProperty(value="売上金額", notes="売上金額")
    public final double getSalesItemAmt() {
    	return salesItemAmt;
    }
    
    public final void setSalesCntSum(int salesCntSum) {
    	this.salesCntSum = salesCntSum;
    }
    
    @ApiModelProperty(value="売上数量和", notes="売上数量和")
    public final int getSalesCntSum() {
    	return salesCntSum;
    }
    
    public final void setSalesAmtSum(double salesAmtSum) {
    	this.salesAmtSum = salesAmtSum;
    }
    
    @ApiModelProperty(value="売上金額和", notes="売上金額和")
    public final double getSalesAmtSum() {
    	return salesAmtSum;
    }
}
