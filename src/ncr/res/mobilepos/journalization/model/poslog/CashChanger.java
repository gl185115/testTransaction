package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * CashChanger Model Object.
 *
 * <P>A CashChanger Node in POSLog XML.
 *
 * <P>The CashChanger node is under Devices Node.
 * And holds the CashChanger transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CashChanger")
public class CashChanger {
	
	/**
	 * Element for amount.
	 */
	@XmlElement(name = "Amount")
	private Amount amount;
	
	/**
	 *  Element for monetaryKind;
	 */
	@XmlElement(name = "MonetaryKind")
	private List<MonetaryKind> monetaryKind;
	
	/**
	 *  Gets the amount details.
	 * @return       The amount details.
	 */
	public final Amount getAmount() {
		return amount;
	}
	
	/**
	 *  Sets the amount details.
	 * @param amountToSet       The amount details to set.
	 */
	public final void setAmount(Amount amountToSet) {
		this.amount = amountToSet;
	}
	
	/**
	 *  Gets the monetaryKind transaction details.
	 * @return      The monetaryKind transaction details.
	 */
	public final List<MonetaryKind> getMonetaryKind() {
		return monetaryKind;
	}
	
	/**
	 *  Sets the monetaryKind transaction details.
	 * @param monetaryKindToSet        The monetaryKind transaction details to set.
	 */
	public final void setMonetaryKind(List<MonetaryKind> monetaryKindToSet) {
		this.monetaryKind = monetaryKindToSet;
	}
	
}