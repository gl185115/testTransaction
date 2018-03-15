package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 *
 * ContainerDeposits Model Object.
 *
 * <P>A ContainerDeposits Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ContainerDeposits")
public class ContainerDeposits {
    /**
     * amount.
     */
    @XmlElement(name = "Amount")
    private String amount;
    /**
     * Count
     */
    @XmlElement(name = "Count")
    private String count;
    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }
    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
    /**
     * @return the count
     */
    public String getCount() {
        return count;
    }
    /**
     * @param count the count to set
     */
    public void setCount(String count) {
        this.count = count;
    }
    
}
