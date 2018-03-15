package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GuaranteeDetail")
public class GuaranteeDetail {

    /**
     * The private member variable that will hold the reason.
     * of the returned item.
     */
    @XmlElement(name = "Reason")
    private String reason;

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

    public GuaranteeDetail(){}

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Reason : ").append(this.reason).append(crlf);
 
     return str.toString();
    }

}
