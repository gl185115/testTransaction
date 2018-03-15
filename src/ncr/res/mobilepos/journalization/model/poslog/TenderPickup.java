package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TenderPickup Model Object.
 *
 * <P>A TenderPickup Node in POSLog XML.
 *
 * <P>The TenderPickup node is under TenderControlTransaction Node.
 * And holds the pickup transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderPickup")
public class TenderPickup {

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
     * An operation count of "Pickup"
     */
    @XmlElement(name = "Count")
    private String count;

    /**
     * Gets the tender type of the pickup transaction
     *
     * @return          	The tender type of the pickup transaction
     */
    public final String getTenderType() {
        return tenderType;
    }

    /**
     * Sets the tender type of the pickup transaction
     *
     * @param tenderType	The new tender type to set
     */
    public final void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    /**
     * Gets the amount of the pickup transaction
     *
     * @return          	The amount of the pickup transaction
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the amount of the pickup transaction
     *
     * @param amount		The new amount to set
     */
    public final void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count ƒZƒbƒg‚·‚é count
     */
    public void setCount(String count) {
        this.count = count;
    }
}
