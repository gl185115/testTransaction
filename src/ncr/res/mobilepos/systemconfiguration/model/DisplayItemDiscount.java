package ncr.res.mobilepos.systemconfiguration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  DiscountItemDiscount is a model class representing
 *  the display of Inputs for ItemDiscount.
 *  Please see: http://jira.ncr.com/browse/RES-4306
 * @author CC185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemDiscount")
public class DisplayItemDiscount {
    /**
     * The list of rates.
     */
    @XmlElement(name = "Rates")
    private String[] rates;
    /**
     * The list of amounts.
     */
    @XmlElement(name = "Amounts")
    private String[] amounts;
    /**
     * @return the rates
     */
    public final String[] getRates() {
        return rates;
    }
    /**
     * @param ratesToSet the rates to set
     */
    public final void setRates(final String[] ratesToSet) {
        this.rates = ratesToSet;
    }
    /**
     * @return the amounts
     */
    public final String[] getAmounts() {
        return amounts;
    }
    /**
     * @param amountsToSet the amounts to set
     */
    public final void setAmounts(final String[] amountsToSet) {
        this.amounts = amountsToSet;
    }
}
