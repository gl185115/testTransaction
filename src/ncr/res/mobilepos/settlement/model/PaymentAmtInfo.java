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
	@ApiModelProperty(value="éÌï ÉRÅ[Éh", notes="éÌï ÉRÅ[Éh")
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
	@ApiModelProperty(value="éÌï ñºèÃ(äøéö)", notes="éÌï ñºèÃ(äøéö)")
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
	@ApiModelProperty(value="éxï•éÌï ", notes="éxï•éÌï ")
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
	@ApiModelProperty(value="éxï•éØï ", notes="éxï•éØï ")
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
	@ApiModelProperty(value="ã‡éÌèWåvã‡äz", notes="ã‡éÌèWåvã‡äz")
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
	@ApiModelProperty(value="é¿ç›çÇã‡äz", notes="é¿ç›çÇã‡äz")
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
	@ApiModelProperty(value="åvéZç›çÇã‡äz", notes="åvéZç›çÇã‡äz")
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
	@ApiModelProperty(value="âﬂïsë´ã‡äz", notes="âﬂïsë´ã‡äz")
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
