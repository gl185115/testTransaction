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
	
	@ApiModelProperty( value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty( value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getRetailStoreId() {
		return retailStoreId;
	}

	public final void setRetailStoreId(String retailStoreId) {
		this.retailStoreId = retailStoreId;
	}
   
	@ApiModelProperty( value="�i��R�[�h", notes="�i��R�[�h")
	public final String getLine() {
		return line;
	}

	public final void setLine(String line) {
		this.line = line;
	}	

	@ApiModelProperty( value="���", notes="���")
	public final Description getDescription() {
		return description;
	}

	public final void setDescription(Description description) {
		this.description = description;
	}

	@ApiModelProperty( value="����R�[�h", notes="����R�[�h")
	public final String getDepartment() {
		return department;
	}

	public final void setDepartment(String department) {
		this.department = department;
	}

	@ApiModelProperty( value="�ېŋ敪", notes="�ېŋ敪")
	public final String getTaxType() {
		return taxType;
	}

	public final void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	@ApiModelProperty( value="����ŗ�", notes="����ŗ�")
	public final String getTaxRate() {
		return taxRate;
	}

	public final void setTaxRate(String taxRate) {
		this.taxRate = taxRate;
	}

	@ApiModelProperty( value="�l���E�������O�敪", notes="�l���E�������O�敪")
	public final String getDiscountType() {
		return discountType;
	}

	public final void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	@ApiModelProperty( value="����O�t���O", notes="����O�t���O")
	public final String getExceptionFlag() {
		return exceptionFlag;
	}

	public final void setExceptionFlag(String exceptionFlag) {
		this.exceptionFlag = exceptionFlag;
	}

	@ApiModelProperty( value="�����l���t���O", notes="�����l���t���O")
	public final String getDiscountFlag() {
		return discountFlag;
	}

	public final void setDiscountFlag(String discountFlag) {
		this.discountFlag = discountFlag;
	}

	@ApiModelProperty( value="�����l�����z", notes="�����l�����z")
	public final double getDiscountAmount() {
		return discountAmount;
	}

	public final void setDiscountAmount(double discountAmount) {
		this.discountAmount = discountAmount;
	}

	@ApiModelProperty( value="�����l����", notes="�����l����")
	public final double getDiscountRate() {
		return discountRate;
	}

	public final void setDiscountRate(double discountRate) {
		this.discountRate = discountRate;
	}

	@ApiModelProperty( value="�N����t���O", notes="�N����t���O")
	public final String getAgeRestrictedFlag() {
		return ageRestrictedFlag;
	}

	public final void setAgeRestrictedFlag(String ageRestrictedFlag) {
		this.ageRestrictedFlag = ageRestrictedFlag;
	}

	@ApiModelProperty( value="�p���t���O", notes="�p���t���O")
	public final String getInheritFlag() {
		return inheritFlag;
	}

	public final void setInheritFlag(String inheritFlag) {
		this.inheritFlag = inheritFlag;
	}

	@ApiModelProperty( value="�\��", notes="�\��")
	public final String getSubSmallInt5() {
		return subSmallInt5;
	}

	public final void setSubSmallInt5(String subSmallInt5) {
		this.subSmallInt5 = subSmallInt5;
	}

	@ApiModelProperty( value="�ŏI�X�V�v���O����ID", notes="�ŏI�X�V�v���O����ID")
	public final String getUpdAppId() {
		return updAppId;
	}

	public final void setUpdAppId(String updAppId) {
		this.updAppId = updAppId;
	}

	@ApiModelProperty( value="�ŏI�X�V���[�U�[ID", notes="�ŏI�X�V���[�U�[ID")
	public final String getUpdOpeCode() {
		return updOpeCode;
	}

	public final void setUpdOpeCode(String updOpeCode) {
		this.updOpeCode = updOpeCode;
	}
}
