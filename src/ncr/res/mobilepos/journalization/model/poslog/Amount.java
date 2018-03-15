package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Amount Model Object.
 *
 * <P>An Amount Node in POSLog XML.
 *
 * @author CC185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Amount")
public class Amount {
    /** The Action. */
    @XmlAttribute(name = "Action")
    private String action;
    /** The SetFlag. */
    @XmlAttribute(name = "SetFlag")
    private String setFlag;
    /** The Amount value. */
    @XmlValue
    private double amount;
    /**
     * @return the setFlag
     */
    public String getSetFlag() {
        return setFlag;
    }
    /**
     * @param setSetFlagToSet the setSetFlag to set
     */
    public void setSetFlag(String setFlag) {
        this.setFlag = setFlag;
    }
    /**
     * @return the action
     */
    public final String getAction() {
        return action;
    }
    /**
     * @param actionToSet the action to set
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
     * @param amountToSet the amount to set
     */
    public final void setAmount(final double amountToSet) {
        this.amount = amountToSet;
    }
}
