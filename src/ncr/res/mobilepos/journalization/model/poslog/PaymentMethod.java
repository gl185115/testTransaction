package ncr.res.mobilepos.journalization.model.poslog;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tender Model Object.
 *
 * <P>A Tender Node in POSLog XML.
 *
 * <P>The Tender node is under LineItem Node.
 * And holds the tender type information from the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PaymentMethod")
public class PaymentMethod {
     /**
     * The private member variable that will hold the PaymentMethodCode.
     */
    @XmlAttribute(name = "PaymentMethodCode")
    private String paymentMethodCode;
    /**
     * The private member variable that will hold the PaymentMethodName.
     */
    @XmlAttribute(name = "PaymentMethodName")
    private String paymentMethodName;
    /**
     * The private member variable that will hold the Ndivided.
     */
    @XmlElement(name = "Ndivided")
    private String Ndivided;
    /**
     * The private member variable that will hold the BonusCount.
     */
    @XmlElement(name = "BonusCount")
    private String bonusCount;
    /**
     * The private member variable that will hold the BonusPayAmount.
     */
    @XmlElement(name = "BonusPayAmount")
    private String bonusPayAmount;
    /**
     * The private member variable that will hold the BonusMonthDay.
     */
    @XmlElement(name = "BonusMonthDay")
    private List<String> bonusMonthDay;

    /**
     * @return the paymentMethodCode
     */
    public final String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    /**
     * @param paymentMethodCode the paymentMethodCode to set
     */
    public final void setPaymentMethodCode(String paymentMethodCode) {
        this.paymentMethodCode = paymentMethodCode;
    }

    /**
     * @return the paymentMethodName
     */
    public final String getPaymentMethodName() {
        return paymentMethodName;
    }

    /**
     * @param paymentMethodName the paymentMethodName to set
     */
    public final void setPaymentMethodName(String paymentMethodName) {
        this.paymentMethodName = paymentMethodName;
    }

    /**
     * @return the Ndivided
     */
    public final String getNdivided() {
        return Ndivided;
    }

    /**
     * @param nDivided the nDivided to set
     */
    public final void setNdivided(String Ndivided) {
        this.Ndivided = Ndivided;
    }

    /**
     * @return the bonusCount
     */
    public final String getBonusCount() {
        return bonusCount;
    }

    /**
     * @param bonusCount the bonusCount to set
     */
    public final void setBonusCount(String bonusCount) {
        this.bonusCount = bonusCount;
    }

    /**
     * @return the bonusPayAmount
     */
    public final String getBonusPayAmount() {
        return bonusPayAmount;
    }

    /**
     * @param bonusPayAmount the bonusPayAmount to set
     */
    public final void setBonusPayAmount(String bonusPayAmount) {
        this.bonusPayAmount = bonusPayAmount;
    }

    /**
     * @return the bonusMonthDay
     */
    public final List<String> getBonusMonthDay() {
        return bonusMonthDay;
    }

    /**
     * @param bonusMonthDay the bonusMonthDay to set
     */
    public final void setBonusMonthDay(List<String> bonusMonthDay) {
        this.bonusMonthDay = bonusMonthDay;
    }

    /**
     * Overrides the toString() Method.
     * @return The string representation of tender.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        List<String> bonusMonths = (this.bonusMonthDay != null)
                ? this.bonusMonthDay
                : new ArrayList<String>();

        str.append("PaymentMethodCode : ").append(this.paymentMethodCode)
           .append(crlf)
           .append("PaymentMethodName : ")
           .append(this.paymentMethodName).append(crlf)
           .append("BonusCount : ").append(this.bonusCount).append(crlf)
           .append(crlf)
           .append("Ndivide : ").append(this.Ndivided).append(crlf)
           .append("BonusPayAmount : ").append(this.bonusPayAmount)
           .append(crlf);

        for (String bonusmonth : bonusMonths) {
            str.append("BonusMonthDay: ").append(bonusmonth).append(crlf);
        }
        return str.toString();
    }
}