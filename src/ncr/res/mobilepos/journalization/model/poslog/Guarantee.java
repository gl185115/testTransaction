package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Guarantee")
public class Guarantee {

    /**
     * The private member variable that will hold the GuaranteeDetail.
     */
    @XmlElement(name = "GuaranteeDetail")
    private GuaranteeDetail guaranteeDetail;

    /**
     * Gets the Guarantee Detail information of the returned item.
     *
     * @return        Returns the Guarantee Detail information of returned item.
     */
    public final GuaranteeDetail getGuaranteeDetail() {
        return guaranteeDetail;
    }

    /**
     * Sets the Guarantee Detail information of the returned item.
     *
     * @param guaranteeDetail        The new value for Guarantee Detail information
     */
    public final void setGuaranteeDetail(GuaranteeDetail guaranteeDetail) {
        this.guaranteeDetail = guaranteeDetail;
    }
    
    public Guarantee(){
        this.guaranteeDetail = new GuaranteeDetail();
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        if (null != this.getGuaranteeDetail()) {
            str.append("GuaranteeDetail : ").append(this.guaranteeDetail.toString());
        }

        return str.toString();
    }
}
