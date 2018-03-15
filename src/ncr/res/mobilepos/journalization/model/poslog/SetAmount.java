package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * SetAmount Model Object.
 *
 * <P>
 * An SetAmount Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SetAmount")
public class SetAmount {
    /** The Action. */
    @XmlAttribute(name = "Action")
    private String action;

    /** The Amount value. */
    @XmlValue
    private double amount;

    /**
     * @return the action
     */
    public final String getAction() {
        return action;
    }

    /**
     * @param actionToSet
     *            the action to set
     */
    public final void setAction(final String actionToSet) {
        this.action = actionToSet;
    }

    /**
     * @return the amount
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * @param amountToSet
     *            the amount to set
     */
    public final void setAmount(final double amountToSet) {
        this.amount = amountToSet;
    }
}
