package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Retail Price Modifier Model Object.
 *
 * @author CC185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RetailPriceModifier")
public class RetailPriceModifier {
    /**
     * The Method Code.
     */
    @XmlAttribute(name = "MethodCode")
    private String methodCode;
    /** The Sequence Number. */
    @XmlElement(name = "SequenceNumber")
    private int sequenceNumber;
    /** The InputType. */
    @XmlElement(name = "InputType")
    private String inputType;
    /** The Bar code. */
    @XmlElement(name = "Barcode")
    private String barcode;
    /** The QRcode. */
    @XmlElement(name = "QRcode")
    private String qRcode;
    /** The Amount. */
    @XmlElement(name = "Amount")
    private Amount amount;
    /** The Percent. */
    @XmlElement(name = "Percent")
    private Percent percent;
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
	 * @return the percent
	 */
	public final Percent getPercent() {
		return percent;
	}
	/**
	 * @param percent the percent to set
	 */
	public final void setPercent(final Percent percent) {
		this.percent = percent;
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
    /**
     * @return the inputType
     */
    public final String getInputType() {
        return inputType;
    }
    /**
     * @param inputType the inputType to set
     */
    public final void setInputType(final String inputType) {
        this.inputType = inputType;
    }
    /**
     * @return the barcode
     */
    public final String getBarcode() {
        return barcode;
    }
    /**
     * @param barcode the barcode to set
     */
    public final void setBarcode(final String barcode) {
        this.barcode = barcode;
    }
    /**
     * @return the qRcode
     */
    public final String getqRcode() {
        return qRcode;
    }
    /**
     * @param qRcode the qRcode to set
     */
    public final void setqRcode(final String qRcode) {
        this.qRcode = qRcode;
    }
}
