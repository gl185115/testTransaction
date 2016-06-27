package ncr.res.mobilepos.cashaccount.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Represents the Cash Balance information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CashBalance")
@ApiModel(value="CashBalance")
public class CashBalance {

    /**
     * till id / drawer id
     */
	@XmlElement(name = "TillId")
	private String tillId;

    /**
     * cash on hand
     */
	@XmlElement(name = "CashOnHand")
	private String cashOnHand;

    /**
     *Gets the till id.
     *
     * @return tillId
     */
	@ApiModelProperty(value="�h���AID", notes="�h���AID")
	public final String getTillId() {
		return tillId;
	}

    /**
     * Sets the till id.
     *
     * @param tillId		new tillId to set
     */
	public final void setTillId(String tillId) {
		this.tillId = tillId;
	}

    /**
     *Gets the cash on hand.
     *
     * @return cashOnHand
     */
	@ApiModelProperty(value="�p���z", notes="�p���z")
	public final String getCashOnHand() {
		return cashOnHand;
	}

    /**
     * Sets the cash on hand.
     *
     * @param cashOnHand		new cashOnHand to set
     */
	public final void setCashOnHand(String cashOnHand) {
		this.cashOnHand = cashOnHand;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TillId:" + this.tillId + "; CashOnHand:" + this.cashOnHand);
		return sb.toString();
	}


}
