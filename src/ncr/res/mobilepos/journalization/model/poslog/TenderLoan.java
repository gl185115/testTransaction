package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TenderLoan Model Object.
 *
 * <P>A TenderLoan Node in POSLog XML.
 *
 * <P>The TenderLoan node is under TenderControlTransaction Node.
 * And holds the loan transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderLoan")
public class TenderLoan {
	
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
     * Gets the tender type of the loan transaction
     * 
     * @return          	The tender type of the loan transaction
     */
	public final String getTenderType() {
		return tenderType;
	}
	
    /**
     * Sets the tender type of the loan transaction
     * 
     * @param tenderType	The new tender type to set
     */
	public final void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
    
    /**
     * Gets the amount of the loan transaction
     * 
     * @return          	The amount of the loan transaction
     */
	public final String getAmount() {
		return amount;
	}
	
    /**
     * Sets the amount of the loan transaction
     * 
     * @param amount		The new amount to set
     */
	public final void setAmount(String amount) {
		this.amount = amount;
	}

}
