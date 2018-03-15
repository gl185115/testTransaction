package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * ñ∆ê≈ã‡äz
 *
 * TaxExempt Model Object.
 *
 * <P>A TaxExempt Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TaxExempt")
public class TaxExempt {

    /**
     * ñ∆ê≈ã‡äz.
     */
    @XmlElement(name = "Amount")
    private String amount;

    /**
     * Count
     */
    @XmlElement(name = "Count")
    private String count;
    
    /**
     * Count
     */
    @XmlElement(name = "TaxIncludeAmount")
    private String taxIncludeAmount;
    
    /**
     * Count
     */
    @XmlElement(name = "TaxExcludeAmount")
    private String taxExcludeAmount;

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

    /**
     * @return the taxIncludeAmount
     */
    public String getTaxIncludeAmount() {
        return taxIncludeAmount;
    }

    /**
     * @param taxIncludeAmount the taxIncludeAmount to set
     */
    public void setTaxIncludeAmount(String taxIncludeAmount) {
        this.taxIncludeAmount = taxIncludeAmount;
    }

    /**
     * @return the taxExcludeAmount
     */
    public String getTaxExcludeAmount() {
        return taxExcludeAmount;
    }

    /**
     * @param taxExcludeAmount the taxExcludeAmount to set
     */
    public void setTaxExcludeAmount(String taxExcludeAmount) {
        this.taxExcludeAmount = taxExcludeAmount;
    }

}
