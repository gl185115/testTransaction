package ncr.res.mobilepos.employee.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Employee information response model object.
 *
 * Encapsulates the employee information.
 */
@XmlRootElement(name = "EmployeeInfoResponse")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value = "EmployeeInfoResponse")
public class EmployeeInfoResponse extends ResultBase {

	/* This list is used to save employee information */
	@XmlElement(name = "EmpList")
	private List<EmployeeInfo> EmpList;

	/**
	 * Gets the employee list.
	 *
	 * @return the employee list
	 */
	@ApiModelProperty(value = "íSìñé“Åiè]ã∆àıÅjinfo", notes = "íSìñé“Åiè]ã∆àıÅjinfo")
	public final List<EmployeeInfo> getEmpList() {
		return EmpList;
	}

	/**
	 * Sets the employee information of the list.
	 *
	 * @param employeeList
	 *            information ToSet the new EmpList
	 */
	public final void setEmpList(List<EmployeeInfo> empList) {
		EmpList = empList;
	}

	@Override
	public final String toString() {

		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());

		if (null != this.EmpList) {
			sb.append(crlf).append("SubtotalDiscount: ").append(this.EmpList.toString());
		}

		return sb.toString();
	}
}
