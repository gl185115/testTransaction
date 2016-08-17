package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/**
 * PendingPayment Model Object.
 *
 * <P>A PendingPayment Node in POSLog XML.
 *
 * <P>The PendingPayment node is under LineItem Node.
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PendingPayment")
public class PendingPayment {
    /**
     * The private member variable that will hold the DepositAmount.
     */
    @XmlElement(name = "DepositAmount")
    private int depositAmount;

    public final int getDepositAmount() {
        return depositAmount;
    }

    public final void setDepositAmount(int depositAmount) {
        this.depositAmount = depositAmount;
    }

}
