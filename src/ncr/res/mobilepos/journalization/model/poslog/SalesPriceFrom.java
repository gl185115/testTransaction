package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * PosLog Model Object.
 *
 * <P>
 * A Sales Price From Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SalesPriceFrom")
public class SalesPriceFrom {
    /**
    * The value of SalesPriceFrom.
    */
    @XmlValue
    private String value;

    /**
     * The private member variable that will hold the urgent old price.
     */
    @XmlAttribute(name = "UrgentOldPrice")
    private int urgentOldPrice;

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param value
     *            the value to set
     */
    public final void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the urgentOldPrice to set
     */
    public final int getUrgentOldPrice() {
        return urgentOldPrice;
    }

    /**
     * @param the
     *            urgentOldPrice
     */
    public final void setUrgentOldPrice(int urgentOldPrice) {
        this.urgentOldPrice = urgentOldPrice;
    }
}