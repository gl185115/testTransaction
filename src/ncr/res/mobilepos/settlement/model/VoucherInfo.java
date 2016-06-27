package ncr.res.mobilepos.settlement.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "VoucherInfo")
@ApiModel(value="VoucherInfo")
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
    @XmlElement(name="VoucherDetails")
    private List<VoucherDetails> details;


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

    public final void setVoucherCompanyId(String voucherCompanyId) {
    	this.voucherCompanyId = voucherCompanyId;
    }

    @ApiModelProperty(value="ギフト券会社コード", notes="ギフト券会社コード")
    public final String getVoucherCompanyId() {
    	return voucherCompanyId;
    }

    public final void setVoucherType(String voucherType) {
    	this.voucherType = voucherType;
    }

    @ApiModelProperty(value="商品券タイプ", notes="商品券タイプ")
    public final String getVoucherType() {
    	return voucherType;
    }

    public final void setBusinessDayDate(String businessDayDate) {
    	this.businessDayDate = businessDayDate;
    }

    @ApiModelProperty(value="営業日", notes="営業日")
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

    public final void setVoucherName(String voucherName) {
    	this.voucherName = voucherName;
    }

    @ApiModelProperty(value="商品券名前", notes="商品券名前")
    public final String getVoucherName() {
    	return voucherName;
    }

    public final void setVoucherKanaName(String voucherKanaName) {
    	this.voucherKanaName = voucherKanaName;
    }

    @ApiModelProperty(value="商品券のカナ名", notes="商品券のカナ名")
    public final String getVoucherKanaName() {
    	return voucherKanaName;
    }

    /**
     * @return the dailyReport
     */
    @ApiModelProperty(value="商品券詳細", notes="商品券詳細")
    public List<VoucherDetails> getVoucherDetails() {
        return details;
    }

    /**
     * @param dailyReport the dailyReport to set
     */
    public void setVoucherDetails(List<VoucherDetails> details) {
        this.details = details;
    }

    public void addVoucherDetails(VoucherDetails details) {
        if(this.details == null) {
            this.details = new ArrayList<VoucherDetails>();
        }
        this.details.add(details);
    }

}
