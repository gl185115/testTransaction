package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * The Model class for Price Derivation Result.
 *
 * @author CC185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PriceDerivationResult")
public class PriceDerivationResult {
    /**
     * The Method Code.
     */
    @XmlAttribute(name = "MethodCode")
    private String methodCode;
    /** The Sequence Number. */
    @XmlElement(name = "SequenceNumber")
    private int sequenceNumber;
    /** The Amount. */
    @XmlElement(name = "Amount")
    private Amount amount;
    /** The Amount. */
    @XmlElement(name = "SetPercent")
    private double setPercent;
    /** The Amount. */
    @XmlElement(name = "SetAmount")
    private SetAmount setAmount;
    /**The Previous Price. */
    @XmlElement(name = "PreviousPrice")
    private double previousPrice;
    /** The Price Derivation Rule. */
    @XmlElement(name = "PriceDerivationRule")
    private PriceDerivationRule priceDerivationRule;
    /**
     * @return the methodCode
     */
    public final String getMethodCode() {
        return methodCode;
    }
    /**
     * @param methodCodeToSet the methodCode to set
     */
    public final void setMethodCode(final String methodCodeToSet) {
        this.methodCode = methodCodeToSet;
    }
    /**
     * @return the sequenceNumber
     */
    public final int getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * @param sequenceNumberToSet the sequenceNumber to set
     */
    public final void setSequenceNumber(final int sequenceNumberToSet) {
        this.sequenceNumber = sequenceNumberToSet;
    }
    /**
     * @return the amount
     */
    public final Amount getAmount() {
        return amount;
    }
    /**
     * @param amountToSet the amount to set
     */
    public final void setAmount(final Amount amountToSet) {
        this.amount = amountToSet;
    }
    /**
     * @return the setPercent
     */
    public double getSetPercent() {
        return setPercent;
    }
    /**
     * @param setPercentToSet the setPercent to set
     */
    public void setSetPercent(double setPercent) {
        this.setPercent = setPercent;
    }
    /**
     * @return the setAmount
     */
    public SetAmount getSetAmount() {
        return setAmount;
    }
    /**
     * @param setAmountToSet the setAmount to set
     */
    public void setSetAmount(SetAmount setAmount) {
        this.setAmount = setAmount;
    }
    /**
     * @return the previousPrice
     */
    public final double getPreviousPrice() {
        return previousPrice;
    }
    /**
     * @param previousPriceToSet the previousPrice to set
     */
    public final void setPreviousPrice(final double previousPriceToSet) {
        this.previousPrice = previousPriceToSet;
    }
    /**
     * @return the priceDerivationRule
     */
    public final PriceDerivationRule getPriceDerivationRule() {
        return priceDerivationRule;
    }
    /**
     * @param priceDerivationRuleToSet the priceDerivationRule to set
     */
    public final void setPriceDerivationRule(
            final PriceDerivationRule priceDerivationRuleToSet) {
        this.priceDerivationRule = priceDerivationRuleToSet;
    }
}
