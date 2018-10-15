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

	/* 担当者コード */
	@XmlElement(name = "EmpCode")
	private String EmpCode;

	/* ログインID */
	@XmlElement(name = "OpeCode")
	private String OpeCode;

	/* 店舗コード */
	@XmlElement(name = "StoreId")
	private String StoreId;

	/* 氏名 */
	@XmlElement(name = "OpeName")
	private String OpeName;

	/**
	 * Gets the EmpCode.
	 *
	 * @return the EmpCode
	 */
	@ApiModelProperty(value = "担当者コード", notes = "担当者コード")
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
	@ApiModelProperty(value = "ログインID", notes = "ログインID")
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
	@ApiModelProperty(value = "店舗コード", notes = "店舗コード")
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
	@ApiModelProperty(value = "氏名", notes = "氏名")
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
