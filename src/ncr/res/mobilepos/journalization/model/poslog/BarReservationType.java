package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author liukai
 * 
 *         BarReservationType Model Object.
 * 
 *         <P>
 *         A BarReservationType Node in POSLog XML.
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barReservationType")
public class BarReservationType {

	/**
	 * barReservationStatus
	 */
	@XmlElement(name = "barReservationStatus")
	private String barReservationStatus;
	/**
	 * ReasonCode
	 */
	@XmlElement(name = "ReasonCode")
	private String reasonCode;
	/**
	 * ReasonName
	 */
	@XmlElement(name = "ReasonName")
	private String reasonName;

	/**
	 * Gets the barReservationStatus of RainCheck.
	 * 
	 * @return The barReservationStatus of RainCheck.
	 */
	public final String getBarReservationStatus() {
		return barReservationStatus;
	}

	/**
	 * Sets the barReservationStatus of RainCheck.
	 * 
	 * @param barReservationStatus
	 *            The new value for barReservationStatus of RainCheck.
	 */
	public final void setBarReservationStatus(String barReservationStatus) {
		this.barReservationStatus = barReservationStatus;
	}

	/**
	 * Gets the ReasonCode of RainCheck.
	 * 
	 * @return The ReasonCode of RainCheck.
	 */
	public final String getReasonCode() {
		return reasonCode;
	}

	/**
	 * Sets the ReasonCode of RainCheck.
	 * 
	 * @param ReasonCode
	 *            The new value for ReasonCode of RainCheck.
	 */
	public final void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	/**
	 * Gets the reasonName of RainCheck.
	 * 
	 * @return The reasonName of RainCheck.
	 */
	public final String getReasonName() {
		return reasonName;
	}

	/**
	 * Sets the reasonName of RainCheck.
	 * 
	 * @param reasonName
	 *            The new value for reasonName of RainCheck.
	 */
	public final void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
}
