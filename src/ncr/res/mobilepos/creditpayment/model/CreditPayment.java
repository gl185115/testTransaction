package ncr.res.mobilepos.creditpayment.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class CreditPaymentRequestResult.
 */
@XmlRootElement(name = "CreditPayment")
@XmlAccessorType(XmlAccessType.NONE)
public class CreditPayment {
     /** 
      * The ButtonNo.
     */
    @XmlElement(name = "buttonNo")
    private String buttonNo;
    /**
     * The code.
     */
    @XmlElement(name = "code")
    private String code;
    /**
     * The DisplayName.
     */
    @XmlElement(name = "displayName")
    private String displayName;
       
    /**
     * Gets the button number.
     *
     * @return the buttonNo
     */
    public final String getButtonNo() {
        return buttonNo;
    }

    /**
     * Sets the button number.
     *
     * @param value
     *            the new button number
     */
    public final void setButtonNo(final String value) {
        this.buttonNo = value;
    }
    
    /**
     * Gets the code.
     *
     * @return the code
     */
    public final String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param value
     *            the new code
     */
    public final void setCode(final String value) {
        this.code = value;
    }
    
    /**
     * Gets the Display Name.
     *
     * @return the displayName
     */
    public final String getDisplayName() {
        return displayName;
    }

    /**
     * Sets the button number.
     *
     * @param value
     *            the new result
     */
    public final void setDisplayName(final String value) {
        this.displayName = value;
    }
    @Override
    public String toString() {
    	 StringBuilder sb = new StringBuilder();
         sb.append(super.toString()).append("; ");
         sb.append("Button Num: " + buttonNo).append("; ");
         sb.append("Code: " + code).append("; ");
         sb.append("Display Name: " + displayName).append("; ");
         return sb.toString();
    }
}
