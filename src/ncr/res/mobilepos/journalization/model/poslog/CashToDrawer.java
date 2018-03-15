package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * CashToDrawer Model Object.
 *
 * <P>A CashToDrawer Node in POSLog XML.
 *
 * <P>The CashToDrawer node is under TenderControlTransaction Node.
 * And holds the CashToDrawer transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CashToDrawer")
public class CashToDrawer {
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
     * An operation count of "CashToDrawer"
     */
    @XmlElement(name = "Count")
    private String count;
    /**
     * Gets the tender type of the cashToDrawer transaction
     *
     * @return          	The tender type of the cashToDrawer transaction
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
     * @param count �Z�b�g���� count
     */
    public void setCount(String count) {
        this.count = count;
    }
    @Override
    public String toString() {
        return "CashToDrawer [tenderType=" + tenderType + ", amount=" + amount + ", count=" + count + "]";
    }
    
    
}
