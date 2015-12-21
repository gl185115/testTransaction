package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Holds the SummaryReceipt element.
 * @author RES
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ControlTransaction")
public class ControlTransaction {
	
	/**
	 * Version Number.
	 */
	@XmlAttribute(name = "Version")
    private String version;
	
	/**
	 * The SummaryReceipt element.
	 */
	@XmlElement(name = "SummaryReceipt")
	private SummaryReceipt summaryReceipt;
	
	 /**
     * The private member variable which holds the ReceiptReprint.
     */
    @XmlElement(name = "ReceiptReprint")
    private ReceiptReprint receiptReprint;

    /**
     * The private member variable which holds the ReceiptReprint.
     */
    @XmlElement(name = "StoredValueFund")
    private StoredValueFund storedValueFund;
    
    /**
     * The private member variable which holds the Operator signing on
     */
    @XmlElement(name = "OperatorSignOn")
    private OperatorSignOn operatorSignOn;
    
    /**
     * The private member variable which holds the Operator signing off
     */
    @XmlElement(name = "OperatorSignOff")
    private OperatorSignOff operatorSignOff;
    
    /**
     * The private member variable which holds the Operator auto sign off
     */
    @XmlElement(name = "AutoSignoff")
    private OperatorAutoSignOff autoSignOff;    
   
	/**
	 * Gets the version number attribute value.
	 * @return
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Sets the version number attribute value.
	 * @param version
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Gets the SummaryReceipt element.
	 * @return
	 */
	public SummaryReceipt getSummaryReceipt() {
		return summaryReceipt;
	}
	
	/**
	 * Sets the SummaryReceipt element.
	 * @param summaryReceipt
	 */
	public void setSummaryReceipt(SummaryReceipt summaryReceipt) {
		this.summaryReceipt = summaryReceipt;
	}
	
    /**
     * @return receiptReprint
     */
    public ReceiptReprint getReceiptReprint() {
        return receiptReprint;
    }

    /**
     * @param receiptReprint セットする receiptReprint
     */
    public void setReceiptReprint(ReceiptReprint receiptReprint) {
        this.receiptReprint = receiptReprint;
    }
    
    /**
     * @return storedValueFund
     */
    public StoredValueFund getStoredValueFund() {
        return storedValueFund;
    }

    /**
     * @param storedValueFund セットする storedValueFund
     */
    public void setStoredValueFund(StoredValueFund storedValueFund) {
        this.storedValueFund = storedValueFund;
    }
    
    /**
     * @return operatorSignOn
     */
    public OperatorSignOn getOperatorSignOn() {
    	return this.operatorSignOn;
    }
    
    /**
     * @param operatorSignOn
     */
    public void setOperatorSignOn(OperatorSignOn operatorSignOn) {
    	this.operatorSignOn = operatorSignOn;
    }
    
    /**
     * 
     * @return operatorSignOff
     */
    public OperatorSignOff getOperatorSignOff() {
    	return this.operatorSignOff;
    }
    
    /**
     * @param operatorSignOff
     */
    public void setOperatorSignOff(OperatorSignOff operatorSignOff) {
    	this.operatorSignOff = operatorSignOff;
    }
    
    /**
     * @return operatorAutoSignOff
     */
    public OperatorAutoSignOff getAutoSignOff() {
    	return this.autoSignOff;
    }
    
    /**
     * @param operatorAutoSignOff
     */
    public void setAutoSignOff(OperatorAutoSignOff operatorAutoSignOff) {
    	this.autoSignOff = operatorAutoSignOff;
    }    
}
