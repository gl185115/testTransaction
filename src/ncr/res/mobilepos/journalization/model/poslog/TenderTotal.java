package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderTotal")
public class TenderTotal {

    /** The total of value. */
    @XmlValue
    private String total;

    /**
     * The private member variable that will hold the TenderType.
     */
    @XmlAttribute(name = "TenderType")
    private String tenderType;

    /**
     * Gets the Tender Type of the returned item.
     *
     * @return        Returns the Tender Type information of Return.
     */
    public final String getTenderType() {
        return tenderType;
    }

    /**
     * Sets the Item Tender Type information of the returned item.
     *
     * @param tenderType        The new value for Item Tender Type information
     */
    public final void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    /**
     * Gets the Total of the returned item.
     *
     * @return        Returns the Total information of Return.
     */
    public final String getTotal() {
        return total;
    }

    /**
     * Sets the Item Total information of the returned item.
     *
     * @param total        The new value for Item Total information
     */
    public final void setTotal(String total) {
        this.total = total;
    }

    public TenderTotal(){
        total = null;
        tenderType = null;
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        String crlf = "\r\n";
        str.append("total : ").append(this.total).append(crlf)
        .append("TenderType : ").append(this.tenderType).append(crlf);
        return str.toString();
    }

}
