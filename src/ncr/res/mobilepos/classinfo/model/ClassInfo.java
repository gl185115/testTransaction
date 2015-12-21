package ncr.res.mobilepos.classinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.pricing.model.Description;

/**
 * ClassInfo Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ClassInfo")
public class ClassInfo {
	
	@XmlElement(name = "CompanyId")
    private String companyId;
	
	@XmlElement(name = "RetailStoreID")
    private String retailStoreId;
	
	@XmlElement(name = "Class")
	private String itemClass;
	
	@XmlElement(name = "Description")
    private Description description;
    
    @XmlElement(name = "Department")
    private String department;
    
    @XmlElement(name = "Line")
    private String line;      
    
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
    public ClassInfo() {
    }
    
    public ClassInfo(final String itemClass) {
        this.itemClass = itemClass;
    }
    
    public ClassInfo(final ClassInfo classInfo) { 
    	this.companyId = classInfo.getCompanyId();
    	this.retailStoreId = classInfo.getRetailStoreId();
    	this.itemClass = classInfo.getItemClass();    	
    	this.description = classInfo.getDescription();
    	this.department = classInfo.getDepartment();
    	this.line = classInfo.getLine();
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
    
    public ClassInfo(final String itemClass, final String retailStoreId, final Description description,
           final String department, final String line) {    	
    	this.setItemClass(itemClass);       
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
      		.append("Class: ").append(itemClass).append(clrf)        	
      		.append("Description: ").append(description != null ? 
        		 description.toString() : "").append(clrf)
        	.append("Department: ").append(department).append(clrf)        		 
        	.append("Line: ").append(line).append(clrf)        		 
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
	
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public final String getRetailStoreId() {
		return retailStoreId;
	}

	public final void setRetailStoreId(String retailStoreId) {
		this.retailStoreId = retailStoreId;
	}

	public final String getItemClass() {
		return itemClass;
	}

	public final void setItemClass(String itemClass) {
		this.itemClass = itemClass;
	}	

	public final Description getDescription() {
		return description;
	}

	public final void setDescription(Description description) {
		this.description = description;
	}

	public final String getDepartment() {
		return department;
	}

	public final void setDepartment(String department) {
		this.department = department;
	}

	public final String getLine() {
		return line;
	}

	public final void setLine(String line) {
		this.line = line;
	}

	public final String getTaxType() {
		return taxType;
	}

	public final void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public final String getTaxRate() {
		return taxRate;
	}

	public final void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	public final String getDiscountType() {
		return discountType;
	}

	public final void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public final String getExceptionFlag() {
		return exceptionFlag;
	}

	public final void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	public final String getDiscountFlag() {
		return discountFlag;
	}

	public final void setDiscountFlag(String discountFlag) {
		this.discountFlag = discountFlag;
	}

	public final double getDiscountAmount() {
		return discountAmount;
	}

	public final void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	public final double getDiscountRate() {
		return discountRate;
	}

	public final void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	public final String getAgeRestrictedFlag() {
		return ageRestrictedFlag;
	}

	public final void setAgeRestrictedFlag(String ageRestrictedFlag) {
		this.ageRestrictedFlag = ageRestrictedFlag;
	}

	public final String getInheritFlag() {
		return inheritFlag;
	}

	public final void setInheritFlag(String inheritFlag) {
		this.inheritFlag = inheritFlag;
	}

	public final String getSubSmallInt5() {
		return subSmallInt5;
	}

	public final void setSubSmallInt5(String subSmallInt5) {
		this.subSmallInt5 = subSmallInt5;
	}
	
	public final String getUpdAppId() {
		return updAppId;
	}

	public final void setUpdAppId(String updAppId) {
		this.updAppId = updAppId;
	}

	public final String getUpdOpeCode() {
		return updOpeCode;
	}

	public final void setUpdOpeCode(String updOpeCode) {
		this.updOpeCode = updOpeCode;
	}    
}
