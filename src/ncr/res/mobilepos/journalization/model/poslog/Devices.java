package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Devices Model Object.
 *
 * <P>A Devices Node in POSLog XML.
 *
 * <P>The Devices node is under PayOut Node.
 * And holds the Devices transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Devices")
public class Devices {
	
	/**
	 * Element for CashChanger.
	 */
	@XmlElement(name = "CashChanger")
	private CashChanger cashChanger;
	
	/**
	 * Element for CashDrawer.
	 */
	@XmlElement(name = "CashDrawer")
	private CashDrawer cashDrawer;
	
	/**
	 *  Gets the CashChanger transaction details.
	 * @return    The CashChanger transaction details.
	 */
	public final CashChanger getCashChanger() {
		return cashChanger;
	}
	
	/**
	 *  Sets the CashChanger transaction details.
	 * @param cashChangerToSet     The CashChanger transaction details.
	 */
	public final void setCashChanger(CashChanger cashChangerToSet) {
		this.cashChanger = cashChangerToSet;
	}
	
	/**
	 *  Gets the CashDrawer transaction details.
	 * @return    The CashDrawer transaction details.
	 */
	public final CashDrawer getCashDrawer() {
		return cashDrawer;
	}
	
	/**
	 *  Sets the CashDrawer transaction details..
	 * @param cashDrawerToSet    The CashDrawer transaction details.
	 */
	public final void setCashDrawer(CashDrawer cashDrawerToSet) {
		this.cashDrawer = cashDrawerToSet;
	}
}