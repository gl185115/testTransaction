package ncr.res.mobilepos.department.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates list of departments.
 */
@XmlRootElement(name = "DepartmentList")
@XmlAccessorType(XmlAccessType.NONE)
@XmlSeeAlso(Department.class)
public class DepartmentList extends ResultBase {
    /**
     * List of Departments.
     */
    @XmlElementWrapper(name = "DepartmentList")
    @XmlElementRef
    private List<Department> departments;
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
    public final List<Department> getDepartments() {
        return departments;
    }

    /**
     * Sets list of departments.
     *
     * @param dptList list of departments.
     */
    public final void setDepartments(final List<Department> dptList) {
        this.departments = dptList;
    }

    /**
     * Gets the retail storeid.
     *
     * @return retailStoreID
     */
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the retails storeid.
     *
     * @param storeID The retailStoreID.
     */
    public final void setRetailStoreID(final String storeID) {
        this.retailStoreID = storeID;
    }

    @Override
    public final String toString() {
        int dptCount = 0;
        if (null != departments) {
            dptCount = departments.size();
        }
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Number of Departments: ").append(dptCount);
        return sb.toString();
    }
}
