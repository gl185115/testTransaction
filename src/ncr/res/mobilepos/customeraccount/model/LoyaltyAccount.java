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
	 * 会社コード
	 * 
	 * @return CompanyId
	 */
	@ApiModelProperty(value = "会社コード", notes = "会社コード")
	public String getCompanyId() {
		return CompanyId;
	}

	/**
	 * 会社コード
	 * 
	 * @param companyId
	 */
	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	/**
	 * 店舗コード
	 * 
	 * @return StoreId
	 */
	@ApiModelProperty(value = "店舗コード", notes = "店舗コード")
	public String getStoreId() {
		return StoreId;
	}

	/**
	 * 店舗コード
	 * 
	 * @param storeId
	 */
	public void setStoreId(String storeId) {
		StoreId = storeId;
	}

	/**
	 * 顧客コード
	 * 
	 * @return ConnCode
	 */
	@ApiModelProperty(value = "顧客コード", notes = "顧客コード")
	public String getConnCode() {
		return ConnCode;
	}

	/**
	 * 顧客コード
	 * 
	 * @param connCode
	 */
	public void setConnCode(String connCode) {
		ConnCode = connCode;
	}

	/**
	 * 組織コード
	 * 
	 * @return OrgCode
	 */
	@ApiModelProperty(value = "組織コード", notes = "組織コード")
	public String getOrgCode() {
		return OrgCode;
	}

	/**
	 * 組織コード
	 * 
	 * @param orgCode
	 */
	public void setOrgCode(String orgCode) {
		OrgCode = orgCode;
	}

	/**
	 * 仕入先区分
	 * 
	 * @return ConnCat
	 */
	@ApiModelProperty(value = "仕入先区分", notes = "仕入先区分")
	public int getConnCat() {
		return ConnCat;
	}

	/**
	 * 仕入先区分
	 * 
	 * @param connCat
	 */
	public void setConnCat(int connCat) {
		ConnCat = connCat;
	}

	/**
	 * 代表仕入先コード
	 * 
	 * @return ConnGrp
	 */
	@ApiModelProperty(value = "代表仕入先コード", notes = "代表仕入先コード")
	public String getConnGrp() {
		return ConnGrp;
	}

	/**
	 * 代表仕入先コード
	 * 
	 * @param connGrp
	 */
	public void setConnGrp(String connGrp) {
		ConnGrp = connGrp;
	}

	/**
	 * 顧客名
	 * 
	 * @return ConnName
	 */
	@ApiModelProperty(value = "顧客名", notes = "顧客名")
	public String getConnName() {
		return ConnName;
	}

	/**
	 * 顧客名
	 * 
	 * @param connName
	 */
	public void setConnName(String connName) {
		ConnName = connName;
	}

	/**
	 * 顧客カナ名
	 * 
	 * @return ConnKanaName
	 */
	@ApiModelProperty(value = "顧客カナ名", notes = "顧客カナ名")
	public String getConnKanaName() {
		return ConnKanaName;
	}

	/**
	 * 顧客カナ名
	 * 
	 * @param connKanaName
	 */
	public void setConnKanaName(String connKanaName) {
		ConnKanaName = connKanaName;
	}

	/**
	 * 仕入先郵便番号
	 * 
	 * @return ConnZip
	 */
	@ApiModelProperty(value = "仕入先郵便番号", notes = "仕入先郵便番号")
	public String getConnZip() {
		return ConnZip;
	}

	/**
	 * 仕入先郵便番号
	 * 
	 * @param connZip
	 */
	public void setConnZip(String connZip) {
		ConnZip = connZip;
	}

	/**
	 * 仕入先住所
	 * 
	 * @return ConnAddr
	 */
	@ApiModelProperty(value = "仕入先住所", notes = "仕入先住所")
	public String getConnAddr() {
		return ConnAddr;
	}

	/**
	 * 仕入先住所
	 * 
	 * @param connAddr
	 */
	public void setConnAddr(String connAddr) {
		ConnAddr = connAddr;
	}

	/**
	 * 電話番号
	 * 
	 * @return ConnTel
	 */
	@ApiModelProperty(value = "電話番号", notes = "電話番号")
	public String getConnTel() {
		return ConnTel;
	}

	/**
	 * 電話番号
	 * 
	 * @param connTel
	 */
	public void setConnTel(String connTel) {
		ConnTel = connTel;
	}

	/**
	 * FAX番号
	 * 
	 * @return ConnFax
	 */
	@ApiModelProperty(value = "FAX番号", notes = "FAX番号")
	public String getConnFax() {
		return ConnFax;
	}

	/**
	 * FAX番号
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
	 * 仕入先決算日（mmdd）
	 * 
	 * @return ConnCloseDate
	 */
	@ApiModelProperty(value = "仕入先決算日（mmdd）", notes = "仕入先決算日（mmdd）")
	public String getConnCloseDate() {
		return ConnCloseDate;
	}

	/**
	 * 仕入先決算日（mmdd）
	 * 
	 * @param connCloseDate
	 */
	public void setConnCloseDate(String connCloseDate) {
		ConnCloseDate = connCloseDate;
	}

	/**
	 * 発注データ伝送区分
	 * 
	 * @return OnlineType
	 */
	@ApiModelProperty(value = "発注データ伝送区分", notes = "発注データ伝送区分")
	public int getOnlineType() {
		return OnlineType;
	}

	/**
	 * 発注データ伝送区分
	 * 
	 * @param onlineType
	 */
	public void setOnlineType(int onlineType) {
		OnlineType = onlineType;
	}

	/**
	 * 発注書発行区分
	 * 
	 * @return SlipType
	 */
	@ApiModelProperty(value = "発注書発行区分", notes = "発注書発行区分")
	public int getSlipType() {
		return SlipType;
	}

	/**
	 * 発注書発行区分
	 * 
	 * @param slipType
	 */
	public void setSlipType(int slipType) {
		SlipType = slipType;
	}

	/**
	 * 支払区分
	 * 
	 * @return PaymentType
	 */
	@ApiModelProperty(value = "支払区分", notes = "支払区分")
	public int getPaymentType() {
		return PaymentType;
	}

	/**
	 * 支払区分
	 * 
	 * @param paymentType
	 */
	public void setPaymentType(int paymentType) {
		PaymentType = paymentType;
	}

	/**
	 * 原価率
	 * 
	 * @return CostRate
	 */
	@ApiModelProperty(value = "原価率", notes = "原価率")
	public int getCostRate() {
		return CostRate;
	}

	/**
	 * 原価率
	 * 
	 * @param costRate
	 */
	public void setCostRate(int costRate) {
		CostRate = costRate;
	}

	/**
	 * 取引開始日付
	 * 
	 * @return ConnStartDate
	 */
	@ApiModelProperty(value = "取引開始日付", notes = "取引開始日付")
	public String getConnStartDate() {
		return ConnStartDate;
	}

	/**
	 * 取引開始日付
	 * 
	 * @param connStartDate
	 */
	public void setConnStartDate(String connStartDate) {
		ConnStartDate = connStartDate;
	}

	/**
	 * 取引終了日付
	 * 
	 * @return ConnEndDate
	 */
	@ApiModelProperty(value = "取引終了日付", notes = "取引終了日付")
	public String getConnEndDate() {
		return ConnEndDate;
	}

	/**
	 * 取引終了日付
	 * 
	 * @param connEndDate
	 */
	public void setConnEndDate(String connEndDate) {
		ConnEndDate = connEndDate;
	}

	/**
	 * 支払先コード
	 * 
	 * @return ConnSubCode
	 */
	@ApiModelProperty(value = "支払先コード", notes = "支払先コード")
	public String getConnSubCode() {
		return ConnSubCode;
	}

	/**
	 * 支払先コード
	 * 
	 * @param connSubCode
	 */
	public void setConnSubCode(String connSubCode) {
		ConnSubCode = connSubCode;
	}

	/**
	 * 顧客カナ名(元)
	 * 
	 * @return SubCode1
	 */
	@ApiModelProperty(value = "顧客カナ名(元)", notes = "顧客カナ名(元)")
	public String getSubCode1() {
		return SubCode1;
	}

	/**
	 * 顧客カナ名(元)
	 * 
	 * @param subCode1
	 */
	public void setSubCode1(String subCode1) {
		SubCode1 = subCode1;
	}

	/**
	 * 電話番号(元)
	 * 
	 * @return SubCode2
	 */
	@ApiModelProperty(value = "電話番号(元)", notes = "電話番号(元)")
	public String getSubCode2() {
		return SubCode2;
	}

	/**
	 * 電話番号(元)
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
