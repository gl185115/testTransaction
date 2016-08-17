package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Beginning Model Object.
 *
 * <P>A Beginning Node in POSLog XML.
 *
 * <P>The Beginning node is under TenderSummary Node.
 * And holds the start of day transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Beginning")
public class Beginning {
	
	/**
	 * Attribute for the tender type
	 */
	@XmlAttribute(name = "TenderType")
	private String tenderType;
	
	/**
	 * Element for the amount
	 */
    @XmlElement(name = "Amount")
    private String amount;
    
    /**
     * Gets the tender type of the start of day transaction
     * 
     * @return          	The tender type of the start of day transaction
     */
	public final String getTenderType() {
		return tenderType;
	}
	
    /**
     * Sets the tender type of the start of day transaction
     * 
     * @param tenderType	The new tender type to set
     */
	public final void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
    
    /**
     * Gets the amount of the start of day transaction
     * 
     * @return          	The amount of the start of day transaction
     */
	public final String getAmount() {
		return amount;
	}
	
    /**
     * Sets the amount of the start of day transaction
     * 
     * @param amount		The new amount to set
     */
	public final void setAmount(String amount) {
		this.amount = amount;
	}

}
