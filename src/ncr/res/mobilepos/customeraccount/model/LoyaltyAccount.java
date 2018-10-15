/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * LoyaltyAccount
 *
 * Model Class for LoyaltyAccount
 *
 */
package ncr.res.mobilepos.customeraccount.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * LoyaltyAccount Model Object.
 *
 * Encapsulates the MST_CONNINFO information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "LoyaltyAccount")
@ApiModel(value = "LoyaltyAccount")
public class LoyaltyAccount {

	/**
	 * The private member variable that will hold the CompanyId of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "CompanyId")
	private String CompanyId;

	/**
	 * The private member variable that will hold the StoreId of LoyaltyAccount.
	 */
	@XmlElement(name = "StoreId")
	private String StoreId;

	/**
	 * The private member variable that will hold the ConnCode of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnCode")
	private String ConnCode;

	/**
	 * The private member variable that will hold the OrgCode of LoyaltyAccount.
	 */
	@XmlElement(name = "OrgCode")
	private String OrgCode;

	/**
	 * The private member variable that will hold the ConnCat of LoyaltyAccount.
	 */
	@XmlElement(name = "ConnCat")
	private int ConnCat;

	/**
	 * The private member variable that will hold the ConnGrp of LoyaltyAccount.
	 */
	@XmlElement(name = "ConnGrp")
	private String ConnGrp;

	/**
	 * The private member variable that will hold the ConnName of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnName")
	private String ConnName;

	/**
	 * The private member variable that will hold the ConnKanaName of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnKanaName")
	private String ConnKanaName;

	/**
	 * The private member variable that will hold the ConnZip of LoyaltyAccount.
	 */
	@XmlElement(name = "ConnZip")
	private String ConnZip;

	/**
	 * The private member variable that will hold the ConnAddr of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnAddr")
	private String ConnAddr;

	/**
	 * The private member variable that will hold the ConnTel of LoyaltyAccount.
	 */
	@XmlElement(name = "ConnTel")
	private String ConnTel;

	/**
	 * The private member variable that will hold the ConnFax of LoyaltyAccount.
	 */
	@XmlElement(name = "ConnFax")
	private String ConnFax;

	/**
	 * The private member variable that will hold the ConnOwner of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnOwner")
	private String ConnOwner;

	/**
	 * The private member variable that will hold the ConnLimit of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnLimit")
	private String ConnLimit;

	/**
	 * The private member variable that will hold the ConnCloseDate of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnCloseDate")
	private String ConnCloseDate;

	/**
	 * The private member variable that will hold the OnlineType of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "OnlineType")
	private int OnlineType;

	/**
	 * The private member variable that will hold the SlipType of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SlipType")
	private int SlipType;

	/**
	 * The private member variable that will hold the PaymentType of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "PaymentType")
	private int PaymentType;

	/**
	 * The private member variable that will hold the CostRate of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "CostRate")
	private int CostRate;

	/**
	 * The private member variable that will hold the ConnStartDate of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnStartDate")
	private String ConnStartDate;

	/**
	 * The private member variable that will hold the ConnEndDate of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnEndDate")
	private String ConnEndDate;

	/**
	 * The private member variable that will hold the ConnSubCode of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "ConnSubCode")
	private String ConnSubCode;

	/**
	 * The private member variable that will hold the SubCode1 of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SubCode1")
	private String SubCode1;

	/**
	 * The private member variable that will hold the SubCode2 of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SubCode2")
	private String SubCode2;

	/**
	 * The private member variable that will hold the SubCode3 of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SubCode3")
	private String SubCode3;

	/**
	 * The private member variable that will hold the SubCode4 of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SubCode4")
	private String SubCode4;

	/**
	 * The private member variable that will hold the SubCode5 of
	 * LoyaltyAccount.
	 */
	@XmlElement(name = "SubCode5")
	private String SubCode5;

	/**
	 * The private member variable that will hold the SubNum1 of LoyaltyAccount.
	 */
	@XmlElement(name = "SubNum1")
	private double SubNum1;

	/**
	 * The private member variable that will hold the SubNum2 of LoyaltyAccount.
	 */
	@XmlElement(name = "SubNum2")
	private double SubNum2;

	/**
	 * The private member variable that will hold the SubNum3 of LoyaltyAccount.
	 */
	@XmlElement(name = "SubNum3")
	private double SubNum3;

	/**
	 * The private member variable that will hold the SubNum4 of LoyaltyAccount.
	 */
	@XmlElement(name = "SubNum4")
	private double SubNum4;

	/**
	 * The private member variable that will hold the SubNum5 of LoyaltyAccount.
	 */
	@XmlElement(name = "SubNum5")
	private double SubNum5;

	/**
	 * ��ЃR�[�h
	 * 
	 * @return CompanyId
	 */
	@ApiModelProperty(value = "��ЃR�[�h", notes = "��ЃR�[�h")
	public String getCompanyId() {
		return CompanyId;
	}

	/**
	 * ��ЃR�[�h
	 * 
	 * @param companyId
	 */
	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	/**
	 * �X�܃R�[�h
	 * 
	 * @return StoreId
	 */
	@ApiModelProperty(value = "�X�܃R�[�h", notes = "�X�܃R�[�h")
	public String getStoreId() {
		return StoreId;
	}

	/**
	 * �X�܃R�[�h
	 * 
	 * @param storeId
	 */
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	/**
	 * �ڋq�R�[�h
	 * 
	 * @return ConnCode
	 */
	@ApiModelProperty(value = "�ڋq�R�[�h", notes = "�ڋq�R�[�h")
	public String getConnCode() {
		return ConnCode;
	}

	/**
	 * �ڋq�R�[�h
	 * 
	 * @param connCode
	 */
	public void setConnCode(String connCode) {
		ConnCode = connCode;
	}

	/**
	 * �g�D�R�[�h
	 * 
	 * @return OrgCode
	 */
	@ApiModelProperty(value = "�g�D�R�[�h", notes = "�g�D�R�[�h")
	public String getOrgCode() {
		return OrgCode;
	}

	/**
	 * �g�D�R�[�h
	 * 
	 * @param orgCode
	 */
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}

	/**
	 * �d����敪
	 * 
	 * @return ConnCat
	 */
	@ApiModelProperty(value = "�d����敪", notes = "�d����敪")
	public int getConnCat() {
		return ConnCat;
	}

	/**
	 * �d����敪
	 * 
	 * @param connCat
	 */
	public void setConnCat(int connCat) {
		ConnCat = connCat;
	}

	/**
	 * ��\�d����R�[�h
	 * 
	 * @return ConnGrp
	 */
	@ApiModelProperty(value = "��\�d����R�[�h", notes = "��\�d����R�[�h")
	public String getConnGrp() {
		return ConnGrp;
	}

	/**
	 * ��\�d����R�[�h
	 * 
	 * @param connGrp
	 */
	public void setConnGrp(String connGrp) {
		ConnGrp = connGrp;
	}

	/**
	 * �ڋq��
	 * 
	 * @return ConnName
	 */
	@ApiModelProperty(value = "�ڋq��", notes = "�ڋq��")
	public String getConnName() {
		return ConnName;
	}

	/**
	 * �ڋq��
	 * 
	 * @param connName
	 */
	public void setConnName(String connName) {
		ConnName = connName;
	}

	/**
	 * �ڋq�J�i��
	 * 
	 * @return ConnKanaName
	 */
	@ApiModelProperty(value = "�ڋq�J�i��", notes = "�ڋq�J�i��")
	public String getConnKanaName() {
		return ConnKanaName;
	}

	/**
	 * �ڋq�J�i��
	 * 
	 * @param connKanaName
	 */
	public void setConnKanaName(String connKanaName) {
		ConnKanaName = connKanaName;
	}

	/**
	 * �d����X�֔ԍ�
	 * 
	 * @return ConnZip
	 */
	@ApiModelProperty(value = "�d����X�֔ԍ�", notes = "�d����X�֔ԍ�")
	public String getConnZip() {
		return ConnZip;
	}

	/**
	 * �d����X�֔ԍ�
	 * 
	 * @param connZip
	 */
	public void setConnZip(String connZip) {
		ConnZip = connZip;
	}

	/**
	 * �d����Z��
	 * 
	 * @return ConnAddr
	 */
	@ApiModelProperty(value = "�d����Z��", notes = "�d����Z��")
	public String getConnAddr() {
		return ConnAddr;
	}

	/**
	 * �d����Z��
	 * 
	 * @param connAddr
	 */
	public void setConnAddr(String connAddr) {
		ConnAddr = connAddr;
	}

	/**
	 * �d�b�ԍ�
	 * 
	 * @return ConnTel
	 */
	@ApiModelProperty(value = "�d�b�ԍ�", notes = "�d�b�ԍ�")
	public String getConnTel() {
		return ConnTel;
	}

	/**
	 * �d�b�ԍ�
	 * 
	 * @param connTel
	 */
	public void setConnTel(String connTel) {
		ConnTel = connTel;
	}

	/**
	 * FAX�ԍ�
	 * 
	 * @return ConnFax
	 */
	@ApiModelProperty(value = "FAX�ԍ�", notes = "FAX�ԍ�")
	public String getConnFax() {
		return ConnFax;
	}

	/**
	 * FAX�ԍ�
	 * 
	 * @param connFax
	 */
	public void setConnFax(String connFax) {
		ConnFax = connFax;
	}

	/**
	 * 
	 * @return ConnOwner
	 */
	@ApiModelProperty(value = "", notes = "")
	public String getConnOwner() {
		return ConnOwner;
	}

	/**
	 * 
	 * @param connOwner
	 */
	public void setConnOwner(String connOwner) {
		ConnOwner = connOwner;
	}

	/**
	 * 
	 * @return ConnLimit
	 */
	@ApiModelProperty(value = "", notes = "")
	public String getConnLimit() {
		return ConnLimit;
	}

	/**
	 * 
	 * @param connLimit
	 */
	public void setConnLimit(String connLimit) {
		ConnLimit = connLimit;
	}

	/**
	 * �d���挈�Z���immdd�j
	 * 
	 * @return ConnCloseDate
	 */
	@ApiModelProperty(value = "�d���挈�Z���immdd�j", notes = "�d���挈�Z���immdd�j")
	public String getConnCloseDate() {
		return ConnCloseDate;
	}

	/**
	 * �d���挈�Z���immdd�j
	 * 
	 * @param connCloseDate
	 */
	public void setConnCloseDate(String connCloseDate) {
		ConnCloseDate = connCloseDate;
	}

	/**
	 * �����f�[�^�`���敪
	 * 
	 * @return OnlineType
	 */
	@ApiModelProperty(value = "�����f�[�^�`���敪", notes = "�����f�[�^�`���敪")
	public int getOnlineType() {
		return OnlineType;
	}

	/**
	 * �����f�[�^�`���敪
	 * 
	 * @param onlineType
	 */
	public void setOnlineType(int onlineType) {
		OnlineType = onlineType;
	}

	/**
	 * ���������s�敪
	 * 
	 * @return SlipType
	 */
	@ApiModelProperty(value = "���������s�敪", notes = "���������s�敪")
	public int getSlipType() {
		return SlipType;
	}

	/**
	 * ���������s�敪
	 * 
	 * @param slipType
	 */
	public void setSlipType(int slipType) {
		SlipType = slipType;
	}

	/**
	 * �x���敪
	 * 
	 * @return PaymentType
	 */
	@ApiModelProperty(value = "�x���敪", notes = "�x���敪")
	public int getPaymentType() {
		return PaymentType;
	}

	/**
	 * �x���敪
	 * 
	 * @param paymentType
	 */
	public void setPaymentType(int paymentType) {
		PaymentType = paymentType;
	}

	/**
	 * ������
	 * 
	 * @return CostRate
	 */
	@ApiModelProperty(value = "������", notes = "������")
	public int getCostRate() {
		return CostRate;
	}

	/**
	 * ������
	 * 
	 * @param costRate
	 */
	public void setCostRate(int costRate) {
		CostRate = costRate;
	}

	/**
	 * ����J�n���t
	 * 
	 * @return ConnStartDate
	 */
	@ApiModelProperty(value = "����J�n���t", notes = "����J�n���t")
	public String getConnStartDate() {
		return ConnStartDate;
	}

	/**
	 * ����J�n���t
	 * 
	 * @param connStartDate
	 */
	public void setConnStartDate(String connStartDate) {
		ConnStartDate = connStartDate;
	}

	/**
	 * ����I�����t
	 * 
	 * @return ConnEndDate
	 */
	@ApiModelProperty(value = "����I�����t", notes = "����I�����t")
	public String getConnEndDate() {
		return ConnEndDate;
	}

	/**
	 * ����I�����t
	 * 
	 * @param connEndDate
	 */
	public void setConnEndDate(String connEndDate) {
		ConnEndDate = connEndDate;
	}

	/**
	 * �x����R�[�h
	 * 
	 * @return ConnSubCode
	 */
	@ApiModelProperty(value = "�x����R�[�h", notes = "�x����R�[�h")
	public String getConnSubCode() {
		return ConnSubCode;
	}

	/**
	 * �x����R�[�h
	 * 
	 * @param connSubCode
	 */
	public void setConnSubCode(String connSubCode) {
		ConnSubCode = connSubCode;
	}

	/**
	 * �ڋq�J�i��(��)
	 * 
	 * @return SubCode1
	 */
	@ApiModelProperty(value = "�ڋq�J�i��(��)", notes = "�ڋq�J�i��(��)")
	public String getSubCode1() {
		return SubCode1;
	}

	/**
	 * �ڋq�J�i��(��)
	 * 
	 * @param subCode1
	 */
	public void setSubCode1(String subCode1) {
		SubCode1 = subCode1;
	}

	/**
	 * �d�b�ԍ�(��)
	 * 
	 * @return SubCode2
	 */
	@ApiModelProperty(value = "�d�b�ԍ�(��)", notes = "�d�b�ԍ�(��)")
	public String getSubCode2() {
		return SubCode2;
	}

	/**
	 * �d�b�ԍ�(��)
	 * 
	 * @param subCode2
	 */
	public void setSubCode2(String subCode2) {
		SubCode2 = subCode2;
	}

	/**
	 * 
	 * @return SubCode3
	 */
	@ApiModelProperty(value = "SubCode3", notes = "SubCode3")
	public String getSubCode3() {
		return SubCode3;
	}

	/**
	 * 
	 * @param subCode3
	 */
	public void setSubCode3(String subCode3) {
		SubCode3 = subCode3;
	}

	/**
	 * 
	 * @return SubCode4
	 */
	@ApiModelProperty(value = "SubCode4", notes = "SubCode4")
	public String getSubCode4() {
		return SubCode4;
	}

	/**
	 * 
	 * @param subCode4
	 */
	public void setSubCode4(String subCode4) {
		SubCode4 = subCode4;
	}

	/**
	 * 
	 * @return SubCode5
	 */
	@ApiModelProperty(value = "SubCode5", notes = "SubCode5")
	public String getSubCode5() {
		return SubCode5;
	}

	/**
	 * 
	 * @param subCode5
	 */
	public void setSubCode5(String subCode5) {
		SubCode5 = subCode5;
	}

	/**
	 * 
	 * @return SubNum1
	 */
	@ApiModelProperty(value = "SubNum1", notes = "SubNum1")
	public double getSubNum1() {
		return SubNum1;
	}

	/**
	 * 
	 * @param subNum1
	 */
	public void setSubNum1(double subNum1) {
		SubNum1 = subNum1;
	}

	/**
	 * 
	 * @return SubNum2
	 */
	@ApiModelProperty(value = "SubNum2", notes = "SubNum2")
	public double getSubNum2() {
		return SubNum2;
	}

	/**
	 * 
	 * @param subNum2
	 */
	public void setSubNum2(double subNum2) {
		SubNum2 = subNum2;
	}

	/**
	 * 
	 * @return SubNum3
	 */
	@ApiModelProperty(value = "SubNum3", notes = "SubNum3")
	public double getSubNum3() {
		return SubNum3;
	}

	/**
	 * 
	 * @param subNum3
	 */
	public void setSubNum3(double subNum3) {
		SubNum3 = subNum3;
	}

	/**
	 * 
	 * @return SubNum4
	 */
	@ApiModelProperty(value = "SubNum4", notes = "SubNum4")
	public double getSubNum4() {
		return SubNum4;
	}

	/**
	 * 
	 * @param subNum4
	 */
	public void setSubNum4(double subNum4) {
		SubNum4 = subNum4;
	}

	/**
	 * 
	 * @return SubNum5
	 */
	@ApiModelProperty(value = "SubNum5", notes = "SubNum5")
	public double getSubNum5() {
		return SubNum5;
	}

	/**
	 * 
	 * @param subNum5
	 */
	public void setSubNum5(double subNum5) {
		SubNum5 = subNum5;
	}

	@Override
	public String toString() {
		return "LoyaltyAccount [CompanyId=" + CompanyId + ", StoreId=" + StoreId + ", ConnCode=" + ConnCode
				+ ", OrgCode=" + OrgCode + ", ConnCat=" + ConnCat + ", ConnGrp=" + ConnGrp + ", ConnName=" + ConnName
				+ ", ConnKanaName=" + ConnKanaName + ", ConnZip=" + ConnZip + ", ConnAddr=" + ConnAddr + ", ConnTel="
				+ ConnTel + ", ConnFax=" + ConnFax + ", ConnOwner=" + ConnOwner + ", ConnLimit=" + ConnLimit
				+ ", ConnCloseDate=" + ConnCloseDate + ", OnlineType=" + OnlineType + ", SlipType=" + SlipType
				+ ", PaymentType=" + PaymentType + ", CostRate=" + CostRate + ", ConnStartDate=" + ConnStartDate
				+ ", ConnEndDate=" + ConnEndDate + ", ConnSubCode=" + ConnSubCode + ", SubCode1=" + SubCode1
				+ ", SubCode2=" + SubCode2 + ", SubCode3=" + SubCode3 + ", SubCode4=" + SubCode4 + ", SubCode5="
				+ SubCode5 + ", SubNum1=" + SubNum1 + ", SubNum2=" + SubNum2 + ", SubNum3=" + SubNum3 + ", SubNum4="
				+ SubNum4 + ", SubNum5=" + SubNum5 + "]";
	}
}
