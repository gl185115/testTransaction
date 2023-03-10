package ncr.res.mobilepos.settlement.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PaymentAmtInfo")
@ApiModel(value="PaymentAmtInfo")
public class PaymentAmtInfo {
	@XmlElement(name = "TenderId")
	private String tenderId;
	@XmlElement(name = "TenderName")
	private String tenderName;
	@XmlElement(name = "TenderType")
	private String tenderType;
	@XmlElement(name = "TenderIdentification")
	private String tenderIdentification;
	@XmlElement(name = "SumAmt")
	private int sumAmt;
	@XmlElement(name = "SumBalancingRegisterAmt")
	private int sumBalancingRegisterAmt;
	@XmlElement(name = "SumBalancingCurrentAmt")
	private int sumBalancingCurrentAmt;
	@XmlElement(name = "SumBalancingDifferenceAmt")
	private int sumBalancingDifferenceAmt;

	/**
	 * @return the tenderId
	 */
	@ApiModelProperty(value="種別コード", notes="種別コード")
	public String getTenderId() {
		return tenderId;
	}

	/**
	 * @param tenderId the tenderId to set
	 */
	public void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}
	
	/**
	 * @return the tenderName
	 */
	@ApiModelProperty(value="種別名称(漢字)", notes="種別名称(漢字)")
	public String getTenderName() {
		return tenderName;
	}

	/**
	 * @param tenderName the tenderName to set
	 */
	public void setTenderName(String tenderName) {
		this.tenderName = tenderName;
	}
	
	/**
	 * @return the tenderType
	 */
	@ApiModelProperty(value="支払種別", notes="支払種別")
	public String getTenderType() {
		return tenderType;
	}

	/**
	 * @param tenderType the tenderType to set
	 */
	public void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	
	/**
	 * @return the tenderIdentification
	 */
	@ApiModelProperty(value="支払識別", notes="支払識別")
	public String getTenderIdentification() {
		return tenderIdentification;
	}

	/**
	 * @param tenderIdentification the tenderIdentification to set
	 */
	public void setTenderIdentification(String tenderIdentification) {
		this.tenderIdentification = tenderIdentification;
	}
	
	/**
	 * @return the sumAmt
	 */
	@ApiModelProperty(value="金種集計金額", notes="金種集計金額")
	public int getSumAmt() {
		return sumAmt;
	}

	/**
	 * @param sumAmt the sumAmt to set
	 */
	public void setSumAmt(int sumAmt) {
		this.sumAmt = sumAmt;
	}
	
	/**
	 * @return the sumBalancingRegisterAmt
	 */
	@ApiModelProperty(value="実在高金額", notes="実在高金額")
	public int getSumBalancingRegisterAmt() {
		return sumBalancingRegisterAmt;
	}

	/**
	 * @param sumBalancingRegisterAmt the sumBalancingRegisterAmt to set
	 */
	public void setSumBalancingRegisterAmt(int sumBalancingRegisterAmt) {
		this.sumBalancingRegisterAmt = sumBalancingRegisterAmt;
	}
	
	/**
	 * @return the sumBalancingCurrentAmt
	 */
	@ApiModelProperty(value="計算在高金額", notes="計算在高金額")
	public int getSumBalancingCurrentAmt() {
		return sumBalancingCurrentAmt;
	}

	/**
	 * @param sumBalancingCurrentAmt the sumBalancingCurrentAmt to set
	 */
	public void setSumBalancingCurrentAmt(int sumBalancingCurrentAmt) {
		this.sumBalancingCurrentAmt = sumBalancingCurrentAmt;
	}
	
	/**
	 * @return the sumBalancingDifferenceAmt
	 */
	@ApiModelProperty(value="過不足金額", notes="過不足金額")
	public int getSumBalancingDifferenceAmt() {
		return sumBalancingDifferenceAmt;
	}

	/**
	 * @param sumBalancingDifferenceAmt the sumBalancingDifferenceAmt to set
	 */
	public void setSumBalancingDifferenceAmt(int sumBalancingDifferenceAmt) {
		this.sumBalancingDifferenceAmt = sumBalancingDifferenceAmt;
	}
}
