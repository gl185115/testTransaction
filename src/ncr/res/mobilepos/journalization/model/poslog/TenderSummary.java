package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TenderSummary Model Object.
 *
 * <P>A TenderSummary Node in POSLog XML.
 *
 * <P>The TenderSummary node is under TillSettle Node.
 * And holds the start of day and end of day transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderSummary")
public class TenderSummary {
	
	/**
	 * Element for start of day
	 */
    @XmlElement(name = "Beginning")
    private Beginning beginning;

	
	/**
	 * Element for end of day
	 */
    @XmlElement(name = "Ending")
    private Ending ending;
    
    /**
     * Gets the start of day transaction details
     * 
     * @return          	The start of day transaction details
     */
	public final Beginning getBeginning() {
		return beginning;
	}
	
    /**
     * Sets the start of day transaction details
     * 
     * @param beginning		The new start of day transaction details to set
     */
	public final void setBeginning(Beginning beginning) {
		this.beginning = beginning;
	}
    
    /**
     * Gets the end of day transaction details
     * 
     * @return          	The end of day transaction details
     */
	public final Ending getEnding() {
		return ending;
	}
	
    /**
     * Sets the end of day transaction details
     * 
     * @param ending		The new end of day transaction details to set
     */
	public final void setEnding(Ending ending) {
		this.ending = ending;
	}

}
