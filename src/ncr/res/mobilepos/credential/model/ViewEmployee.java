package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Contains employee details and result code.
 */
@XmlRootElement(name = "ViewEmployee")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ViewEmployee")
public class ViewEmployee extends ResultBase {
    /**
     * Employee instance. Contains employee detail.
     */
    @XmlElement(name = "Employee")
    private Employee employee = null;

    /**
     * Sets employee.
     *
     * @param emp
     *            The new instance of an employee.
     */
    public final void setEmployee(final Employee emp) {
        this.employee = emp;
    }

    /**
     * Gets employee.
     *
     * @return Employee The employee instance.
     */
    @ApiModelProperty(value="è]ã∆àıèÓïÒ", notes="è]ã∆àıèÓïÒ")
    public final Employee getEmployee() {
        return employee;
    }
}
