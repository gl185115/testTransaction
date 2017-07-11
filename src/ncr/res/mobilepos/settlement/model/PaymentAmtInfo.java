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
	@XmlElement(name = "SumAmt")
	private int sumAmt;
  
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
}
