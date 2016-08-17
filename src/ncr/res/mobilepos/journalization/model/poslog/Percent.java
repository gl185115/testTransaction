/**
 * 
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author pb185094
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Percent")
public class Percent {
	
	@XmlAttribute(name = "Action")
	private String action;
	
	@XmlValue
	private String percent;

	/**
	 * @return the action
	 */
	public final String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public final void setAction(
			final String action) {
		this.action = action;
	}

	/**
	 * @return the value
	 */
	public final String getPercent() {
		return percent;
	}

	/**
	 * @param value the value to set
	 */
	public final void setPercent(final String percent) {
		this.percent = percent;
	}
}
