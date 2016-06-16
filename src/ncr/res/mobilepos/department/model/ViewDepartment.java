package ncr.res.mobilepos.department.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

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
     * Gets the list of departments.
     *
     * @return an array list of department.
     */
    @ApiModelProperty( value="部門情報", notes="部門情報")
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
    @ApiModelProperty( value="店舗コード", notes="店舗コード")
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

}
