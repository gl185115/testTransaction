package ncr.res.mobilepos.employee.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Employee information Model for Employee information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "EmployeeInfo")
@ApiModel(value = "EmployeeInfo")
public class EmployeeInfo {

	/* �S���҃R�[�h */
	@XmlElement(name = "EmpCode")
	private String EmpCode;

	/* ���O�C��ID */
	@XmlElement(name = "OpeCode")
	private String OpeCode;

	/* �X�܃R�[�h */
	@XmlElement(name = "StoreId")
	private String StoreId;

	/* ���� */
	@XmlElement(name = "OpeName")
	private String OpeName;

	/**
	 * Gets the EmpCode.
	 *
	 * @return the EmpCode
	 */
	@ApiModelProperty(value = "�S���҃R�[�h", notes = "�S���҃R�[�h")
	public String getEmpCode() {
		return EmpCode;
	}

	/**
	 * Sets the empCode of the column.
	 *
	 * @param empCodeToSet
	 *            the new empCode
	 */
	public void setEmpCode(String empCode) {
		EmpCode = empCode;
	}

	/**
	 * Gets the OpeCode.
	 *
	 * @return the OpeCode
	 */
	@ApiModelProperty(value = "���O�C��ID", notes = "���O�C��ID")
	public String getOpeCode() {
		return OpeCode;
	}

	/**
	 * Sets the opeCode of the column.
	 *
	 * @param opeCodeToSet
	 *            the new opeCode
	 */
	public void setOpeCode(String opeCode) {
		OpeCode = opeCode;
	}

	/**
	 * Gets the StoreId.
	 *
	 * @return the StoreId
	 */
	@ApiModelProperty(value = "�X�܃R�[�h", notes = "�X�܃R�[�h")
	public String getStoreId() {
		return StoreId;
	}

	/**
	 * Sets the storeId of the column.
	 *
	 * @param storeIdToSet
	 *            the new storeId
	 */
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	/**
	 * Gets the OpeName.
	 *
	 * @return the OpeName
	 */
	@ApiModelProperty(value = "����", notes = "����")
	public String getOpeName() {
		return OpeName;
	}

	/**
	 * Sets the opeName of the column.
	 *
	 * @param opeNameToSet
	 *            the new opeName
	 */
	public void setOpeName(String opeName) {
		OpeName = opeName;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";

		if (null != this.EmpCode) {
			sb.append("EmpCode: ").append(this.EmpCode.toString());
		}

		if (null != this.OpeCode) {
			sb.append(crlf).append("OpeCode: ").append(this.OpeCode.toString());
		}

		if (null != this.StoreId) {
			sb.append(crlf).append("StoreId: ").append(this.StoreId.toString());
		}

		if (null != this.OpeName) {
			sb.append(crlf).append("OpeName: ").append(this.OpeName.toString());
		}

		return sb.toString();
	}
}
