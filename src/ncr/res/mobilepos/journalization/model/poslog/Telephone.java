package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Telephone Model Object.
 *
 * <P>An Telephone Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Telephone")
public class Telephone {
    
    /** The FullTelephoneNumber. */
    @XmlElement(name = "FullTelephoneNumber")
    private String fullTelephoneNumber;
    
    /** The VoucherTelephoneNumber. */
    @XmlElement(name = "VoucherTelephoneNumber")
    private String voucherTelephoneNumber;
    
    /**
     * @return the fullTelephoneNumber
     */
    public final String getFullTelephoneNumber() {
        return fullTelephoneNumber;
    }
    /**
     * @param fullTelephoneNumber the fullTelephoneNumber to set
     */
    public final void setFullTelephoneNumber(String fullTelephoneNumber) {
        this.fullTelephoneNumber = fullTelephoneNumber;
    }
    
    /**
     * @return the voucherTelephoneNumber
     */
    public final String getVoucherTelephoneNumber() {
        return voucherTelephoneNumber;
    }
    /**
     * @param voucherTelephoneNumber the voucherTelephoneNumber to set
     */
    public final void setVoucherTelephoneNumber(String voucherTelephoneNumber) {
        this.voucherTelephoneNumber = voucherTelephoneNumber;
    }
}
