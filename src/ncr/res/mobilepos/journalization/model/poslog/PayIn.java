package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PayIn Model Object.
 *
 * <P>A PayIn Node in POSLog XML.
 *
 * <P>The PayIn node is under TenderControlTransaction Node.
 * And holds the PayIn transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PayIn")
public class PayIn {
	
	/**
	 *   Attribute for TenderType
	 */
	@XmlAttribute(name = "TenderType")
	private String tenderType;
	
	/**
	 *   Element for Amount
	 */
	@XmlElement(name = "Amount")
	private Amount amount;
	
	/**
	 *   Element for Devices
	 */
	@XmlElement(name = "Devices")
	private Devices devices;
	
	/**
	 *   Gets the TenderType attribute value.
	 * @return   The TenderType attribute value.
	 */
	public final String getTenderType() {
		return tenderType;
	}
	
	/**
	 *   Sets the TenderType attribute value.
	 * @param tenderTypeToSet    The TenderType attribute value.
	 */
	public final void setTenderType(String tenderTypeToSet) {
		this.tenderType = tenderTypeToSet;
	}
	
	/**
	 *   Gets the Amount transaction details.
	 * @return   The Amount transaction details.
	 */
	public final Amount getAmount() {
		return amount;
	}
	
	/**
	 *   Sets the Amount transaction details.
	 * @param amountToSet    The Amount transaction details.
	 */
	public final void setAmount(Amount amountToSet) {
		this.amount = amountToSet;
	}
	
	/**
	 *   Gets the Devices transaction details.
	 * @return    The Devices transaction details.
	 */
	public final Devices getDevices() {
		return devices;
	}
	
	/**
	 *   Sets the Devices transaction details.
	 * @param devicesToSet   The Devices transaction details.
	 */
	public final void setDevices(Devices devicesToSet) {
		this.devices = devicesToSet;
	}
}