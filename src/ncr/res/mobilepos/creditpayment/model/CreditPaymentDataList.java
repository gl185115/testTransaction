package ncr.res.mobilepos.creditpayment.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates list of CreditPayment.
 * @author vr185019
 *
 */
@XmlAccessorType(XmlAccessType.NONE)

@XmlRootElement(name = "CreditPayment")
public class CreditPaymentDataList extends ResultBase {
    /** The result base. */
    private ResultBase result;
    /**
    /**
     * List of creditPayment.
    */
    @XmlElement(name = "CreditPayment")
    private List<CreditPayment> creditPayment;
    /**
     * Gets the result.
     *
     * @return the result
     */
    public final ResultBase getResult() {
        return result;
    }

    /**
     * Sets the result.
     *
     * @param value
     *            the new result
     */
    public final void setResult(final ResultBase value) {
        this.result = value;
    }
    /**
     * Setter for list of CreditPayment.
     * @param creditPaymentToSet   CreditPayment to set.
     */
    public final void setCreditPayment(
                   final List<CreditPayment> creditPaymentToSet) {
        this.creditPayment = creditPaymentToSet;
    }
    /**
     * Getter for list of CreditPayment.
     * @return   creditPayment
     */
    public final List<CreditPayment> getCreditPayment() {
        return creditPayment;
    }
    
    @Override
    public String toString() {
    	 StringBuilder sb = new StringBuilder();
         sb.append(super.toString()).append("; ");
         sb.append("Credit Payment: " + creditPayment).append("; ");
         return sb.toString();
    }
}
