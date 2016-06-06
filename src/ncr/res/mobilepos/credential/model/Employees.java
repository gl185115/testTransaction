package ncr.res.mobilepos.credential.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

/**
 * EmployeeList is a model class that represents the list of Employee.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Employees")
@XmlSeeAlso({ Employee.class })
@ApiModel(value="Employees")
public class Employees extends ResultBase {
    /**
     * The retail store identifier string.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailtStoreID;

    /**
     * List of employees.
     */
    @XmlElementWrapper(name = "EmployeeList")
    @XmlElementRef()
    private List<Employee> employeeList;

    /**
     * Gets the retail store number.
     *
     * @return retailStoreID.
     */
    public final String getRetailtStoreID() {
        return retailtStoreID;
    }

    /**
     * Sets the retail store number.
     *
     * @param retailtStoreIDToSet
     *            retailStoreID to set.
     */
    public final void setRetailtStoreID(final String retailtStoreIDToSet) {
        this.retailtStoreID = retailtStoreIDToSet;
    }

    /**
     * Gets the list of employee.
     *
     * @return List of Employee.
     */
    public final List<Employee> getEmployeeList() {
        return employeeList;
    }

    /**
     * Sets the list of employee.
     *
     * @param employeeListToSet
     *            List of Employee to set.
     */
    public final void setEmployeeList(final List<Employee> employeeListToSet) {
        this.employeeList = employeeListToSet;
    }
}
