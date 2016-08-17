package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * PosLog Model Object.
 *
 * <P>A Total Node in POSLog XML.
 *
 * <P>The Total node mainly holds the detail of the Subtotal amount
 * and the Total payable amounts.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Total")
public class Total {
	/**
     * The private member variable that will hold the total type.
     */
	@XmlAttribute(name = "TotalType")
    private String totalType;
	/**
     * The private member variable that will hold the total value.
     */
	@XmlValue
    private double amount;
	/**
     * @return the totalType to set
     */
	public final String getTotalType() {
		return totalType;
	}
	/**
     * @param the totalType
     */
	public final void setTotalType(String totalType) {
		this.totalType = totalType;
	}
	/**
     * @return the amount
     */
	public final double getAmount() {
		return amount;
	}
	/**
     * @param the amount to set
     */
	public final void setAmount(double amount) {
		this.amount = amount;
	}
	
	
}