package ncr.res.mobilepos.line.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.pricing.model.Description;

/**
 * Line Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "LineInfo")
@ApiModel(value="Line")
public class Line {
	
	@XmlElement(name = "CompanyID")
    private String companyId;
	
	@XmlElement(name = "RetailStoreID")
    private String retailStoreId;
	
	@XmlElement(name = "Line")
    private String line; 
	
	@XmlElement(name = "Description")
    private Description description;
    
    @XmlElement(name = "Department")
    private String department;
    
    @XmlElement(name = "TaxType")
    private String taxType;
    
    @XmlElement(name = "TaxRate")
    private String taxRate;  
    
    @XmlElement(name = "DiscountType")
    private String discountType;
    
    @XmlElement(name = "ExceptionFlag")
    private String exceptionFlag;
    
    @XmlElement(name = "DiscountFlag")
    private String discountFlag;
    
    @XmlElement(name = "DiscountAmount")
    private double discountAmount;
    
    @XmlElement(name = "DiscountRate")
    private double discountRate;
    
    @XmlElement(name = "AgeRestrictedFlag")
    private String ageRestrictedFlag;
    
    @XmlElement(name = "InheritFlag")
    private String inheritFlag;
    
    @XmlElement(name = "SubSmallInt5")
    private String subSmallInt5;
    
    private String updAppId;
    
    private String updOpeCode;
    
    // constructors
    public Line() {    	
    }
    
    public Line(final String line) {
        this.line = line;
    }
    
    public Line(final Line classInfo) { 
    	this.companyId = classInfo.getCompanyId();
    	this.retailStoreId = classInfo.getRetailStoreId();
    	this.line = classInfo.getLine();    	
    	this.description = classInfo.getDescription();
    	this.department = classInfo.getDepartment();    	
    	this.taxType = classInfo.getTaxType();
    	this.taxRate = classInfo.getTaxRate();
    	this.discountType = classInfo.getDiscountType();
    	this.exceptionFlag = classInfo.getExceptionFlag();
    	this.discountType = classInfo.getDiscountType();    	
    	this.discountAmount = classInfo.getDiscountAmount();
    	this.discountRate = classInfo.getDiscountRate();
    	this.ageRestrictedFlag = classInfo.getAgeRestrictedFlag();
    	this.inheritFlag = classInfo.getInheritFlag();
    	this.subSmallInt5 = classInfo.getSubSmallInt5();    
    	this.updAppId = classInfo.getUpdAppId();  
    	this.updOpeCode = classInfo.getUpdOpeCode();
    }
    
    public Line(final String line, final String retailStoreId, 
    		final Description description, final String department) {    	
        this.setDescription(description);
        this.setDepartment(department);
        this.setLine(line);        
        this.setRetailStoreId(retailStoreId);        
    } 	

	@Override
    public final String toString() {
      StringBuilder str = new StringBuilder();
      String clrf = "; ";
      str.append("CompanyID: ").append(companyId).append(clrf)
            .append("RetailStoreID: ").append(retailStoreId).append(clrf)
      		.append("Line: ").append(line).append(clrf)        	
      		.append("Description: ").append(description != null ? 
        		 description.toString() : "").append(clrf)
        	.append("Department: ").append(department).append(clrf)
        	.append("TaxType: ").append(taxType).append(clrf)        		 
        	.append("TaxRate: ").append(taxRate).append(clrf)        		 
        	.append("DiscountType: ").append(taxType).append(clrf)        		 
        	.append("ExceptionFlag: ").append(exceptionFlag).append(clrf)        		 
        	.append("DiscountFlag: ").append(discountFlag).append(clrf)        		 
        	.append("DiscountAmount: ").append(discountAmount).append(clrf)        		 
        	.append("DiscountRate: ").append(discountRate).append(clrf)        		 
        	.append("AgeRestrictedFlag: ").append(ageRestrictedFlag).append(clrf)        		 
        	.append("InheritFlag: ").append(inheritFlag).append(clrf)        		 
        	.append("SubSmallInt5: ").append(subSmallInt5).append(clrf);    
      return str.toString();
    }
	
	@ApiModelProperty( value="会社コード", notes="会社コード")
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty( value="店舗コード", notes="店舗コード")
	public final String getRetailStoreId() {
		return retailStoreId;
	}

	public final void setRetailStoreId(String retailStoreId) {
		this.retailStoreId = retailStoreId;
	}
   
	@ApiModelProperty( value="品種コード", notes="品種コード")
	public final String getLine() {
		return line;
	}

	public final void setLine(String line) {
		this.line = line;
	}	

	@ApiModelProperty( value="種類", notes="種類")
	public final Description getDescription() {
		return description;
	}

	public final void setDescription(Description description) {
		this.description = description;
	}

	@ApiModelProperty( value="部門コード", notes="部門コード")
	public final String getDepartment() {
		return department;
	}

	public final void setDepartment(String department) {
		this.department = department;
	}

	@ApiModelProperty( value="課税区分", notes="課税区分")
	public final String getTaxType() {
		return taxType;
	}

	public final void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	@ApiModelProperty( value="消費税率", notes="消費税率")
	public final String getTaxRate() {
		return taxRate;
	}

	public final void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	@ApiModelProperty( value="値引・割引除外区分", notes="値引・割引除外区分")
	public final String getDiscountType() {
		return discountType;
	}

	public final void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	@ApiModelProperty( value="売上外フラグ", notes="売上外フラグ")
	public final String getExceptionFlag() {
		return exceptionFlag;
	}

	public final void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	@ApiModelProperty( value="自動値引フラグ", notes="自動値引フラグ")
	public final String getDiscountFlag() {
		return discountFlag;
	}

	public final void setDiscountFlag(String discountFlag) {
		this.discountFlag = discountFlag;
	}

	@ApiModelProperty( value="自動値引金額", notes="自動値引金額")
	public final double getDiscountAmount() {
		return discountAmount;
	}

	public final void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	@ApiModelProperty( value="自動値引率", notes="自動値引率")
	public final double getDiscountRate() {
		return discountRate;
	}

	public final void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	@ApiModelProperty( value="年齢制限フラグ", notes="年齢制限フラグ")
	public final String getAgeRestrictedFlag() {
		return ageRestrictedFlag;
	}

	public final void setAgeRestrictedFlag(String ageRestrictedFlag) {
		this.ageRestrictedFlag = ageRestrictedFlag;
	}

	@ApiModelProperty( value="継承フラグ", notes="継承フラグ")
	public final String getInheritFlag() {
		return inheritFlag;
	}

	public final void setInheritFlag(String inheritFlag) {
		this.inheritFlag = inheritFlag;
	}

	@ApiModelProperty( value="予約", notes="予約")
	public final String getSubSmallInt5() {
		return subSmallInt5;
	}

	public final void setSubSmallInt5(String subSmallInt5) {
		this.subSmallInt5 = subSmallInt5;
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
}
