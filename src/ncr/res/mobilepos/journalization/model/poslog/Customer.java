package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Customer Model Object.
 *
 * <P>A Customer Node in POSLog XML.
 *
 * <P>The Customer Node is under CustomerOrderTransaction Node.
 * And holds the value of Customer's Demographic.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Customer")
public class Customer {
    /** The Customer Demographic. */
    @XmlElement(name = "CustomerDemographic")
    private String customerDemographic;
    
    /** The Customer ID. */
    @XmlElement(name = "CustomerID")
    private String customerId;
    
    /** The Customer Rank. */
    @XmlElement(name = "CustomerRank")
    private String customerRank;
    
    /** The Customer Input Type. */
    @XmlElement(name = "CustomerInputType")
    private String customerInputType;
    
    /** The Card Status Type. */
    @XmlElement(name = "CardStatusType")
    private String cardStatusType;
    
    /** The Card Type Id. */
    @XmlElement(name = "CardTypeId")
    private String cardTypeId;
    
    /** The Card Class Id. */
    @XmlElement(name = "CardClassId")
    private String cardClassId;
    
    /** The Customer Name. */
    @XmlElement(name = "Name")
    private String name;
    
    /** The Customer Telephone Number. */
    @XmlElement(name = "TelephoneNumber")
    private String telephoneNumber;
    
    /** The Customer eMail. */
    @XmlElement(name = "eMail")
    private String eMail;

    /**
     * The Getter for Customer Demographic.
     * @return 							The Customer Demographic.
     */
    public final String getCustomerDemographic() {
        return customerDemographic;
    }
    
    /**
     * The Setter for Customer Demographic.
     * @param customerDemographic 		The new Customer Demographic.
     */
    public final void setCustomerDemographic(
            final String customerDemographic) {
        this.customerDemographic = customerDemographic;
    }

    /**
     * The Getter for Customer ID.
     * @return 							The Customer ID.
     */
	public final String getCustomerId() {
		return customerId;
	}
    
    /**
     * The Setter for Customer ID.
     * @param customerId				The new Customer ID.
     */
	public final void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

    /**
     * The Getter for Customer Name.
     * @return 							The Customer Name.
     */
	public final String getName() {
		return name;
	}
    
    /**
     * The Setter for Customer Name.
     * @param name						The new Customer Name.
     */
	public final void setName(String name) {
		this.name = name;
	}

    /**
     * The Getter for Customer Telephone Number.
     * @return 							The Customer Telephone Number.
     */
	public final String getTelephoneNumber() {
		return telephoneNumber;
	}
    
    /**
     * The Setter for Customer Telephone Number.
     * @param telephoneNumber			The new Customer Telephone Number.
     */
	public final void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

    /**
     * The Getter for Customer eMail.
     * @return 							The Customer eMail.
     */
	public final String geteMail() {
		return eMail;
	}
    
    /**
     * The Setter for Customer eMail.
     * @param eMail			The new Customer eMail.
     */
	public final void seteMail(String eMail) {
		this.eMail = eMail;
	}

    /**
     * The Getter for CustomerRank.
     * @return                          The CustomerRank.
     */
    public final String getCustomerRank() {
        return customerRank;
    }

    /**
     * The Setter for CustomerRank.
     * @param customerRank         The new CustomerRank.
     */
    public final void setCustomerRank(String customerRank) {
        this.customerRank = customerRank;
    }

    /**
     * @return the customerInputType
     */
    public final String getCustomerInputType() {
        return customerInputType;
    }

    /**
     * @param customerInputType the customerInputType to set
     */
    public final void setCustomerInputType(String customerInputType) {
        this.customerInputType = customerInputType;
    }

    /**
     * @return the cardStatusType
     */
    public final String getCardStatusType() {
        return cardStatusType;
    }

    /**
     * @param cardStatusType the cardStatusType to set
     */
    public final void setCardStatusType(String cardStatusType) {
        this.cardStatusType = cardStatusType;
    }

    /**
     * @return the cardTypeId
     */
    public final String getCardTypeId() {
        return cardTypeId;
    }

    /**
     * @param cardTypeId the cardTypeId to set
     */
    public final void setCardTypeId(String cardTypeId) {
        this.cardTypeId = cardTypeId;
    }

    /**
     * @return the cardClassId
     */
    public final String getCardClassId() {
        return cardClassId;
    }

    /**
     * @param cardClassId the cardClassId to set
     */
    public final void setCardClassId(String cardClassId) {
        this.cardClassId = cardClassId;
    }
}
