package ncr.res.mobilepos.department.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * 改定履歴
 * バージョン         改定日付       担当者名           改定内容
 * 1.01               2014.12.11     LiQian             DIV存在チェックを対応
 */

/**
 * Represents the Department information.
 */
@XmlRootElement(name = "Department")
@XmlAccessorType(XmlAccessType.NONE)
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
    
    private String updAppId;
    
    private String updOpeCode;

    /**
     *Gets the retail store number.
     *
     * @return retailStoreID
     */
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
    
    
    
    public String getTaxType() {
        return taxType;
    }
    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }
    public String getTaxRate() {
        return taxRate;
    }
    public void setTaxRate(String taxRate) {
        this.taxRate = taxRate;
    }
    public String getDiscountType() {
        return discountType;
    }
    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }
    public int getNonSales() {
        return nonSales;
    }
    public void setNonSales(int nonSales) {
        this.nonSales = nonSales;
    }
    public String getDiscountFlag() {
        return discountFlag;
    }
    public void setDiscountFlag(String discountFlag) {
        this.discountFlag = discountFlag;
    }
    public double getDiscountAmt() {
        return discountAmt;
    }
    public void setDiscountAmt(double discountAmt) {
        this.discountAmt = discountAmt;
    }
    public double getDiscounRate() {
        return discounRate;
    }
    public void setDiscounRate(double discounRate) {
        this.discounRate = discounRate;
    }
    public String getAgeRestrictedFlag() {
        return ageRestrictedFlag;
    }
    public void setAgeRestrictedFlag(String ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }
    public String getInheritFlag() {
        return inheritFlag;
    }
    public void setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    public int getSubSmallInt5() {
        return subSmallInt5;
    }
    public void setSubSmallInt5(int subSmallInt5) {
        this.subSmallInt5 = subSmallInt5;
    }
    public String getSubNum1() {
        return subNum1;
    }
    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }
    public String getSubNum2() {
        return subNum2;
    }
    public void setSubNum2(String subNum2) {
        this.subNum2 = subNum2;
    }
    public String getSubNum3() {
        return subNum3;
    }
    public void setSubNum3(String subNum3) {
        this.subNum3 = subNum3;
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
            .append("discounRate: ").append(discounRate).append(clrf)
            .append("taxRate: ").append(taxRate).append(clrf)
            .append("taxtype: ").append(taxType).append(clrf)
            .append("updAppId: ").append(updAppId).append(clrf)
            .append("updOpeCode: ").append(updOpeCode).append(clrf);
        return str.toString();
    }
}
