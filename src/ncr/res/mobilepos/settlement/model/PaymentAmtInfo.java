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
  
	/**
	 * @return the tenderId
	 */
	@ApiModelProperty(value="í•ÊƒR[ƒh", notes="í•ÊƒR[ƒh")
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
	@ApiModelProperty(value="í•Ê–¼Ì(Š¿š)", notes="í•Ê–¼Ì(Š¿š)")
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
	@ApiModelProperty(value="x•¥í•Ê", notes="x•¥í•Ê")
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
	@ApiModelProperty(value="x•¥¯•Ê", notes="x•¥¯•Ê")
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
	@ApiModelProperty(value="‹àíWŒv‹àŠz", notes="‹àíWŒv‹àŠz")
	public int getSumAmt() {
		return sumAmt;
	}

	/**
	 * @param sumAmt the sumAmt to set
	 */
	public void setSumAmt(int sumAmt) {
		this.sumAmt = sumAmt;
	}
}
