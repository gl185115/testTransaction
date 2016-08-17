/**
 * 
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * @author pb185094
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Eligibility")
public class Eligibility {
	
	 /** The type of eligibility */
	@XmlAttribute(name = "Type")
	private String type;
	
	 /** The number of items to be eligible */
	@XmlElement(name = "Quantity")
	private int quantity;
	
	
	/**
	 * @return the type
	 */
	public final String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public final void setType(final String type) {
		this.type = type;
	}

	/**
	 * @return the quantity
	 */
	public final int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity to set
	 */
	public final void setQuantity(final int quantity) {
		this.quantity = quantity;
	}

	

}
