package ncr.res.mobilepos.cashaccount.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * GetCashBalance service response
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GetCashBalance")
@ApiModel(value="GetCashBalance")
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
	@ApiModelProperty(value="現金残額情報", notes="現金残額情報")
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
