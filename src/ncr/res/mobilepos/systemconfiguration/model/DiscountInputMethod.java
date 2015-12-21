package ncr.res.mobilepos.systemconfiguration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  DiscountInputMethod is a model class representing
 *  the display of discount Inputs.
 *  Please see: http://jira.ncr.com/browse/RES-4306
 * @author CC185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DiscountInputMethod")
public class DiscountInputMethod {
    /**
     * The Input Method.
     */
    @XmlElement(name = "InputMethod")
    private int inputMethod;
    /**
     * The Item Discount.
     */
    @XmlElement(name = "ItemDiscount")
    private DisplayItemDiscount displayItemDiscount;
    /**
     * The Transaction Discount.
     */
    @XmlElement(name = "TransactionDiscount")
    private DisplayTransactionDiscount displayTransactionDiscount;
    /**
     * @return the inputMethod
     */
    public final int getInputMethod() {
        return inputMethod;
    }
    /**
     * @param inputMethodToSet the inputMethod to set
     */
    public final void setInputMethod(final int inputMethodToSet) {
        this.inputMethod = inputMethodToSet;
    }
    /**
     * @return the displayItemDiscount
     */
    public final DisplayItemDiscount getDisplayItemDiscount() {
        return displayItemDiscount;
    }
    /**
     * @param displayItemDiscountToSet the displayItemDiscount to set
     */
    public final void setDisplayItemDiscount(
            final DisplayItemDiscount displayItemDiscountToSet) {
        this.displayItemDiscount = displayItemDiscountToSet;
    }
    /**
     * @return the displayTransactionDiscount
     */
    public final DisplayTransactionDiscount getDisplayTransactionDiscount() {
        return displayTransactionDiscount;
    }
    /**
     * @param displayTransactionDiscountToSet
     *  the displayTransactionDiscount to set
     */
    public final void setDisplayTransactionDiscount(
            final DisplayTransactionDiscount displayTransactionDiscountToSet) {
        this.displayTransactionDiscount = displayTransactionDiscountToSet;
    }
}
