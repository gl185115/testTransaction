package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * barTaxExempt Model Object.
 *
 * <P>An barTaxExempt Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barTaxExempt")
public class BarTaxExempt {
    /**
     * barAmount
     */
    @XmlElement(name = "barAmount")
    private double barAmount;

    /**
     * @return the barAmount
     */
    public double getBarAmount() {
        return barAmount;
    }

    /**
     * @param barAmount the barAmount to set
     */
    public void setBarAmount(double barAmount) {
        this.barAmount = barAmount;
    }
}
