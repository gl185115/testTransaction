package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Xeb Member Points Info Model Object.
 *
 * <P>
 * A MemberInfoDetail Node in POSLog XML.
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CouponDetail")
public class CouponDetail {
    /**
     * The private variable that will hold the SequenceNumber
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;

    /**
     * The private variable that will hold the CouponBarcode
     */
    @XmlElement(name = "CouponBarcode")
    private String couponBarcode;

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getCouponBarcode() {
        return couponBarcode;
    }

    public void setCouponBarcode(String couponBarcode) {
        this.couponBarcode = couponBarcode;
    }

    @Override
    public String toString() {
        return "CouponDetail [SequenceNumber=" + sequenceNumber + "CouponBarcode=" + couponBarcode +"]";
    }

}
