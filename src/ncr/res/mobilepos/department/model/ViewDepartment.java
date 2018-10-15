package ncr.res.mobilepos.department.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates list of departments.
 */
@XmlRootElement(name = "ViewDepartment")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ViewDepartment")
public class ViewDepartment extends ResultBase {
    /**
     * List of Departments.
     */
    @XmlElement(name = "Department")
    private Department department;
    /**
     * Store id.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

   /**
     * Promotion No.
     */
    @XmlElement(name = "promotionNo")
    private String promotionNo;

    /**
     * Discount Rate.
     */
    @XmlElement(name = "DiscountRate")
    private Double discountRate;

    /**
     * Discount Amt.
     */
    @XmlElement(name = "DiscountAmt")
    private Long discountAmt;

    /**
     * Discount Class.
     */
    @XmlElement(name = "DiscountClass")
    private String discountClass;
    
    /**
     * Promotion Type.
     */
    @XmlElement(name = "PromotionType")
    private String promotionType;
    /**
     * Gets the list of departments.
     *
     * @return an array list of department.
     */
    @ApiModelProperty( value="������", notes="������")
    public final Department getDepartment() {
        return department;
    }

    /**
     * Sets list of departments.
     *
     * @param departmentToSet list of departments.
     */
    public final void setDepartment(final Department departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Gets the retail storeid.
     *
     * @return retailStoreID
     */
    @ApiModelProperty( value="�X�܃R�[�h", notes="�X�܃R�[�h")
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the retails storeid.
     *
     * @param retailStoreIDToSet storeid.
     */
    public final void setRetailStoreID(final String retailStoreIDToSet) {
        this.retailStoreID = retailStoreIDToSet;
    }

   /**
     * Gets the promotionNo .
     *
     * @return promotionNo
     */
    @ApiModelProperty( value="���No", notes="���No")
    public final String getPromotionNo() {
        return promotionNo;
    }

    /**
     * Sets the promotionNo.
     *
     * @param promotionNoToSet promotionNo.
     */
    public final void setPromotionNo(final String promotionNoToSet) {
        this.promotionNo = promotionNoToSet;
    }

	/**
     * Gets the DiscountRate.
     *
     * @return discountRate
     */
    @ApiModelProperty( value="������", notes="������")
    public final Double getDiscountRate() {
        return discountRate;
    }

    /**
     * Sets the DiscountRate.
     *
     * @param discountRateToSet discountRate.
     */
    public final void setDiscountRate(final Double discountRateToSet) {
        this.discountRate = discountRateToSet;
    }

    /**
     * Gets the DiscountAmt.
     *
     * @return discountAmt
     */
    @ApiModelProperty( value="�l���z", notes="�l���z")
    public final Long getDiscountAmt() {
        return discountAmt;
    }

    /**
     * Sets the discountAmt.
     *
     * @param discountAmtToSet discountAmt.
     */
    public final void setDiscountAmt(final long discountAmtToSet) {
        this.discountAmt = discountAmtToSet;
    }

	/**
     * Gets the discountClass.
     *
     * @return discountClass
     */
    @ApiModelProperty( value="�����敪", notes="�����敪")
    public final String getDiscountClass() {
        return discountClass;
    }

    /**
     * Sets the discountClass.
     *
     * @param discountClassToSet discountClass.
     */
    public final void setDiscountClass(final String discountClassToSet) {
        this.discountClass = discountClassToSet;
    }
    
	/**
     * Gets the promotionType.
     *
     * @return promotionType
     */
    @ApiModelProperty( value="�ݒ�敪", notes="�ݒ�敪")
    public final String getPromotionType() {
        return promotionType;
    }

    /**
     * Sets the promotionType.
     *
     * @param promotionType promotionType.
     */
    public final void setPromotionType(final String promotionType) {
        this.promotionType = promotionType;
    }
    
    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
