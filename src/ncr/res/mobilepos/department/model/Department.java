package ncr.res.mobilepos.department.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 改定履歴
 * バージョン         改定日付       担当者名           改定内容
 * 1.01               2014.12.11     LiQian             DIV存在チェックを対応
 */

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.pricing.model.ChangeableTaxRate;
import ncr.res.mobilepos.pricing.model.DefaultTaxRate;
import ncr.res.mobilepos.pricing.model.PricePromInfo;

/**
 * Represents the Department information.
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="Department")
public class Department {
	/**
     * Company number.
     */
    @XmlElement(name = "CompanyID")
    private String companyID;
	/**
     * Store number.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;
    /**
     * Department id.
     */
    @XmlElement(name = "DepartmentID")
    private String departmentID;
    /**
     * Department name.
     */
    @XmlElement(name = "DepartmentName")
    private DepartmentName departmentName;
    /**
     * Status of Department
     */
    @XmlElement(name = "Status")
    private String dptStatus;
    /**
     * Gets the Status of Department .
     *
     * @return strStatus
     */
    @ApiModelProperty( value="部門状態", notes="部門状態")
    public final String getDptStatus() {
        return dptStatus;
    }
    /**
     * Sets the Status of Department .
     *
     * @param dptStatus
     */
    public final void setDptStatus(String dptStatus) {
        this.dptStatus = dptStatus;
    }
    
    @XmlElement(name = "TaxType")
    private String taxType;

    @XmlElement(name = "TaxRate")
    private String taxRate;
    
    @XmlElement(name = "DiscountType")
    private String discountType;
    
    @XmlElement(name = "NonSales")
    private int nonSales;
    
    @XmlElement(name = "DiscountFlag")
    private String discountFlag;
    
    @XmlElement(name = "DiscountAmt")
    private double discountAmt;
    
    @XmlElement(name = "DiscountRate")
    private double discountRate;
    
    @XmlElement(name = "AgeRestrictedFlag")
    private String ageRestrictedFlag;
    
    @XmlElement(name = "InheritFlag")
    private String inheritFlag;

    @XmlElement(name = "SubSmallInt5")
    private int subSmallInt5;
    
    @XmlElement(name = "SubNum1")
    private String subNum1;
    
    @XmlElement(name = "SubNum2")
    private String subNum2;
    
    @XmlElement(name = "SubNum3")
    private String subNum3;
    
    @XmlElement(name = "SubNum4")
    private String subNum4;

    @XmlElement(name = "TaxId")
    private Integer taxId;

    private String updAppId;
    
    private String updOpeCode;

    @XmlElement(name = "SubCode1")
    private String subCode1;
    
    @XmlElement(name = "PriceDiscountClass")
    private String priceDiscountClass;
    
    @XmlElement(name = "PricePromotionNo")
    private String pricePromotionNo;
	
	@XmlElement(name = "PriceDiscountAmt")
    private double priceDiscountAmt;
    
    @XmlElement(name = "PriceDiscountRate")
    private double priceDiscountRate;
    
    @XmlElement(name = "InputType")
    private String inputType;
    
    @XmlElement(name = "GroupName")
    private String groupName;
    
    @XmlElement(name = "GroupID")
    private String groupID;

    @XmlElement(name = "ChangeableTaxRate")
    private ChangeableTaxRate changeableTaxRate;

    @XmlElement(name = "DefaultTaxRate")
    private DefaultTaxRate defaultTaxRate;
    
    @XmlElement(name = "PricePromList")
    private List<PricePromInfo> pricePromList; 
    
    @ApiModelProperty(value="特売", notes="特売")
    public List<PricePromInfo> getPricePromList() {
		return pricePromList;
	}

	public void setPricePromList(List<PricePromInfo> pricePromList) {
		this.pricePromList = pricePromList;
	}

    /**
     *Gets the retail store number.
     *
     * @return retailStoreID
     */
    @ApiModelProperty( value="店舗コード", notes="店舗コード")
    public final String getRetailStoreID() {
         return retailStoreID;
    }
    /**
     * Sets the retail store number.
     *
     * @param retailStoreId - store number
     */
    public final void setRetailStoreID(final String retailStoreId) {
        this.retailStoreID = retailStoreId;
    }

    /**
     *Gets the companyID.
     *
     * @return companyID
     */
    @ApiModelProperty( value="会社コード", notes="会社コード")
    public final String getCompanyID() {
         return companyID;
    }
    /**
     * Sets the companyID.
     *
     * @param companyID
     */
    public final void setCompanyID(final String companyID) {
        this.companyID = companyID;
    }


    /**
     * Gets the department id.
     *
     * @return departmentID
     */
    @ApiModelProperty( value="部門コード", notes="部門コード")
    public final String getDepartmentID() {
        return departmentID;
    }

    /**
     * Sets the department id.
     *
     * @param dptID
     *            department id.
     */
    public final void setDepartmentID(final String dptID) {
        this.departmentID = dptID;
    }

    /**
     * Gets the department name.
     *
     * @return department name.
     */
    @ApiModelProperty( value="部門名称", notes="部門名称")
    public final DepartmentName getDepartmentName() {
        return departmentName;
    }

    /**
     * Sets the department name.
     *
     * @param dptName
     *            department name.
     */
    public final void setDepartmentName(final DepartmentName dptName) {
        this.departmentName = dptName;
    }
    
    
    @ApiModelProperty( value="売上税区分", notes="売上税区分")
    public String getTaxType() {
        return taxType;
    }
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }
    @ApiModelProperty( value="消費税率", notes="消費税率")
    public String getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
    @ApiModelProperty( value="値引許可フラグ", notes="値引許可フラグ")
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    @ApiModelProperty( value="非売品", notes="非売品")
    public int getNonSales() {
        return nonSales;
    }
    public void setNonSales(int nonSales) {
        this.nonSales = nonSales;
    }
    @ApiModelProperty( value="自動値引フラグ", notes="自動値引フラグ")
    public String getDiscountFlag() {
        return discountFlag;
    }
    public void setDiscountFlag(String discountFlag) {
        this.discountFlag = discountFlag;
    }
    @ApiModelProperty( value="自動値引金額", notes="自動値引金額")
    public double getDiscountAmt() {
        return discountAmt;
    }
    public void setDiscountAmt(double discountAmt) {
        this.discountAmt = discountAmt;
    }
    @ApiModelProperty( value="自動値引率", notes="自動値引率")
    public double getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    @ApiModelProperty( value="年齢制限フラグ", notes="年齢制限フラグ")
    public String getAgeRestrictedFlag() {
        return ageRestrictedFlag;
    }
    public void setAgeRestrictedFlag(String ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }
    @ApiModelProperty( value="継承フラグ", notes="継承フラグ")
    public String getInheritFlag() {
        return inheritFlag;
    }
    public void setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    @ApiModelProperty( value="予約", notes="予約")
    public int getSubSmallInt5() {
        return subSmallInt5;
    }
    public void setSubSmallInt5(int subSmallInt5) {
        this.subSmallInt5 = subSmallInt5;
    }
    @ApiModelProperty( value="割引対象フラグ", notes="割引対象フラグ")
    public String getSubNum1() {
        return subNum1;
    }
    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }
    @ApiModelProperty( value="ポイント対象フラグ", notes="ポイント対象フラグ")
    public String getSubNum2() {
        return subNum2;
    }
    public void setSubNum2(String subNum2) {
        this.subNum2 = subNum2;
    }
    @ApiModelProperty( value="テイクアウトフラグ", notes="テイクアウトフラグ")
    public String getSubNum3() {
        return subNum3;
    }
    public void setSubNum3(String subNum3) {
        this.subNum3 = subNum3;
    }
    @ApiModelProperty( value="Point累計購入金額連携対象フラグ", notes="Point累計購入金額連携対象フラグ")
    public String getSubNum4() {
        return subNum4;
    }
    public void setSubNum4(String subNum4) {
        this.subNum4 = subNum4;
    }
    @ApiModelProperty( value="税率区分", notes="税率区分")
    public Integer getTaxId() {
        return taxId;
    }
    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }
    @ApiModelProperty( value="最終更新プログラムID", notes="最終更新プログラムID")
    public final String getUpdAppId() {
	return updAppId;
    }
    
    public final void setUpdAppId(String updAppId) {
	this.updAppId = updAppId;
    }
	
    @ApiModelProperty( value="最終更新ユーザーID", notes="最終更新ユーザーID")
    public final String getUpdOpeCode() {
	return updOpeCode;
    }
	
    public final void setUpdOpeCode(String updOpeCode) {
	this.updOpeCode = updOpeCode;
    }
    @ApiModelProperty( value="グループコード", notes="グループコード")
    public String getSubCode1() {
        return subCode1;
    }
    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }
    @ApiModelProperty( value="企画No", notes="企画No")
    public String getPricePromotionNo() {
        return pricePromotionNo;
    }
    public void setPricePromotionNo(String pricePromotionNo) {
        this.pricePromotionNo = pricePromotionNo;
    }
    @ApiModelProperty( value="割引区分", notes="割引区分")
    public String getPriceDiscountClass() {
        return priceDiscountClass;
    }
    public void setPriceDiscountClass(String priceDiscountClass) {
        this.priceDiscountClass = priceDiscountClass;
    }
	@ApiModelProperty( value="値引額", notes="値引額")
    public double getPriceDiscountAmt() {
        return priceDiscountAmt;
    }
    public void setPriceDiscountAmt(double priceDiscountAmt) {
        this.priceDiscountAmt = priceDiscountAmt;
    }
    @ApiModelProperty( value="割引率", notes="割引率")
    public double getPriceDiscountRate() {
        return priceDiscountRate;
    }
    public void setPriceDiscountRate(double priceDiscountRate) {
        this.priceDiscountRate = priceDiscountRate;
    }
    @ApiModelProperty( value="inputType", notes="inputType")
    public String getInputType() {
        return inputType;
    }
    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
    @ApiModelProperty(value="グループコード", notes="グループコード")
    public String getGroupID() {
        return groupID;
    }
    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }
    @ApiModelProperty(value="グループ名称", notes="グループ名称")
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @ApiModelProperty(value="変更可能な税率", notes="変更可能な税率")
    public final ChangeableTaxRate getChangeableTaxRate() {
        return this.changeableTaxRate;
    }
    public final void setChangeableTaxRate(final ChangeableTaxRate changeableTaxRate) {
        this.changeableTaxRate = changeableTaxRate;
    }
    @ApiModelProperty(value="デフォルトで使う税率", notes="デフォルトで使う税率")
    public final DefaultTaxRate getDefaultTaxRate() {
        return this.defaultTaxRate;
    }
    public final void setDefaultTaxRate(final DefaultTaxRate defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("retailStoreID: ").append(retailStoreID).append(clrf)
        	.append("departmentID: ").append(departmentID).append(clrf)
        	.append("departmentName: ").append(departmentName == null ? 
        			"" : departmentName.toString()).append(clrf)
            // 1.01  2014.12.11 LiQian DIV存在チェックを対応   ADD START
            .append("dptStatus: ").append(dptStatus).append(clrf)
            // 1.01  2014.12.11 LiQian DIV存在チェックを対応   ADD END
            .append("inheritFlag: ").append(inheritFlag).append(clrf)
            .append("nonsales: ").append(nonSales).append(clrf)
            .append("subSmallInt5: ").append(subSmallInt5).append(clrf)
            .append("discountType: ").append(discountType).append(clrf)
            .append("discountFlag: ").append(discountFlag).append(clrf)
            .append("discountAmt: ").append(discountAmt).append(clrf)
            .append("discounRate: ").append(discountRate).append(clrf)
            .append("taxRate: ").append(taxRate).append(clrf)
            .append("taxtype: ").append(taxType).append(clrf)
            .append("updAppId: ").append(updAppId).append(clrf)
            .append("updOpeCode: ").append(updOpeCode).append(clrf);
        return str.toString();
    }
}
