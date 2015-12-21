package ncr.res.mobilepos.cashaccount.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * GetCashBalance service response
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GetCashBalance")
public class GetCashBalance extends ResultBase {
	
    /**
     * Cash Balance information
     */
	@XmlElement(name = "CashBalance")
	private CashBalance cashBalance;

    /**
     *Gets the cash balance information
     *
     * @return cashBalance
     */
	public final CashBalance getCashBalance() {
		return cashBalance;
	}
	
    /**
     * Sets the cash balance information
     *
     * @param cashBalance		new cash balance information to set
     */

	public final void setCashBalance(CashBalance cashBalance) {
		this.cashBalance = cashBalance;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("cashBalance is: " + ((cashBalance == null) ? "" : 
			cashBalance.toString()));
		return sb.toString();
	}
	
	

}
