package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ExchangeDetail")
public class ExchangeDetail {

    /**
     * The private member variable that will hold the tender total.
     * of the returned item.
     */
    @XmlElement(name = "TenderTotal")
    private TenderTotal tenderTotal;

    /**
     * The private member variable that will hold the reason.
     * of the returned item.
     */
    @XmlElement(name = "Reason")
    private String reason;

    /**
     * Gets the Item Tender Total information of the returned item.
     *
     * @return        Returns the Item Tender Total information of returned item.
     */
    public final TenderTotal getTenderTotal() {
        return tenderTotal;
    }

    /**
     * Sets the Item Tender Total information of the returned item.
     *
     * @param tenderTotal        The new value for Item Tender Total information
     */
    public final void setTenderTotal(TenderTotal tenderTotal) {
        this.tenderTotal = tenderTotal;
    }

    /**
     * Gets the Item Reason information of the returned item.
     *
     * @return        Returns the Item Reason information of returned item.
     */
    public final String getReason() {
        return reason;
    }

    /**
     * Sets the Item Reason information of the returned item.
     *
     * @param reason        The new value for Item Reason information
     */
    public final void setReason(String reason) {
        this.reason = reason;
    }

    public ExchangeDetail(){
        this.tenderTotal = new TenderTotal();
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Reason : ").append(this.reason).append(crlf);
        if (null != this.tenderTotal) {
            str.append("TenderTotal : ").append(this.tenderTotal.toString());
        }
     return str.toString();
    }

}
