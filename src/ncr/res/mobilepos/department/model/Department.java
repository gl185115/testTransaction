package ncr.res.mobilepos.department.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * ���藚��
 * �o�[�W����         ������t       �S���Җ�           ������e
 * 1.01               2014.12.11     LiQian             DIV���݃`�F�b�N��Ή�
 */

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Represents the Department information.
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="Department")
public class Department {
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
    @ApiModelProperty( value="������", notes="������")
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
    private double discounRate;
    
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
    
    private String updAppId;
    
    private String updOpeCode;

    @XmlElement(name = "SubCode1")
    private String subCode1;
    /**
     *Gets the retail store number.
     *
     * @return retailStoreID
     */
    @ApiModelProperty( value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
     * Gets the department id.
     *
     * @return departmentID
     */
    @ApiModelProperty( value="����R�[�h", notes="����R�[�h")
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
    @ApiModelProperty( value="���喼��", notes="���喼��")
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
    
    
    @ApiModelProperty( value="����ŋ敪", notes="����ŋ敪")
    public String getTaxType() {
        return taxType;
    }
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }
    @ApiModelProperty( value="����ŗ�", notes="����ŗ�")
    public String getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
    @ApiModelProperty( value="�l�����t���O", notes="�l�����t���O")
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    @ApiModelProperty( value="�񔄕i", notes="�񔄕i")
    public int getNonSales() {
        return nonSales;
    }
    public void setNonSales(int nonSales) {
        this.nonSales = nonSales;
    }
    @ApiModelProperty( value="�����l���t���O", notes="�����l���t���O")
    public String getDiscountFlag() {
        return discountFlag;
    }
    public void setDiscountFlag(String discountFlag) {
        this.discountFlag = discountFlag;
    }
    @ApiModelProperty( value="�����l�����z", notes="�����l�����z")
    public double getDiscountAmt() {
        return discountAmt;
    }
    public void setDiscountAmt(double discountAmt) {
        this.discountAmt = discountAmt;
    }
    @ApiModelProperty( value="�����l����", notes="�����l����")
    public double getDiscounRate() {
        return discounRate;
    }
    public void setDiscounRate(double discounRate) {
        this.discounRate = discounRate;
    }
    @ApiModelProperty( value="�N����t���O", notes="�N����t���O")
    public String getAgeRestrictedFlag() {
        return ageRestrictedFlag;
    }
    public void setAgeRestrictedFlag(String ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }
    @ApiModelProperty( value="�p���t���O", notes="�p���t���O")
    public String getInheritFlag() {
        return inheritFlag;
    }
    public void setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    @ApiModelProperty( value="�\��", notes="�\��")
    public int getSubSmallInt5() {
        return subSmallInt5;
    }
    public void setSubSmallInt5(int subSmallInt5) {
        this.subSmallInt5 = subSmallInt5;
    }
    @ApiModelProperty( value="�����Ώۃt���O", notes="�����Ώۃt���O")
    public String getSubNum1() {
        return subNum1;
    }
    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }
    @ApiModelProperty( value="�|�C���g�Ώۃt���O", notes="�|�C���g�Ώۃt���O")
    public String getSubNum2() {
        return subNum2;
    }
    public void setSubNum2(String subNum2) {
        this.subNum2 = subNum2;
    }
    @ApiModelProperty( value="�e�C�N�A�E�g�t���O", notes="�e�C�N�A�E�g�t���O")
    public String getSubNum3() {
        return subNum3;
    }
    public void setSubNum3(String subNum3) {
        this.subNum3 = subNum3;
    }
    @ApiModelProperty( value="KPC�݌v�w�����z�A�g�Ώۃt���O", notes="KPC�݌v�w�����z�A�g�Ώۃt���O")
    public String getSubNum4() {
        return subNum4;
    }
    public void setSubNum4(String subNum4) {
        this.subNum4 = subNum4;
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
    @ApiModelProperty( value="�O���[�v�R�[�h", notes="�O���[�v�R�[�h")
    public String getSubCode1() {
        return subCode1;
    }
    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }
    
    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("retailStoreID: ").append(retailStoreID).append(clrf)
        	.append("departmentID: ").append(departmentID).append(clrf)
        	.append("departmentName: ").append(departmentName == null ? 
        			"" : departmentName.toString()).append(clrf)
            // 1.01  2014.12.11 LiQian DIV���݃`�F�b�N��Ή�   ADD START
            .append("dptStatus: ").append(dptStatus).append(clrf)
            // 1.01  2014.12.11 LiQian DIV���݃`�F�b�N��Ή�   ADD END
            .append("inheritFlag: ").append(inheritFlag).append(clrf)
            .append("nonsales: ").append(nonSales).append(clrf)
            .append("subSmallInt5: ").append(subSmallInt5).append(clrf)
            .append("discountType: ").append(discountType).append(clrf)
            .append("discountFlag: ").append(discountFlag).append(clrf)
            .append("discountAmt: ").append(discountAmt).append(clrf)
            .append("discounRate: ").append(discounRate).append(clrf)
            .append("taxRate: ").append(taxRate).append(clrf)
            .append("taxtype: ").append(taxType).append(clrf)
            .append("updAppId: ").append(updAppId).append(clrf)
            .append("updOpeCode: ").append(updOpeCode).append(clrf);
        return str.toString();
    }
}
