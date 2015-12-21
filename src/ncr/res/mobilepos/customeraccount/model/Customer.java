/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* Customer
*
* Customer Class is a Model representation and mainly holds
* the Customer's Information.
*
* Campos, Carlos
*/
package ncr.res.mobilepos.customeraccount.model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

 /**
 * Customer Class is a Model representation and mainly holds
 * the Customer's Information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Customer")
public class Customer extends ResultBase {
    /**
     * The Customer ID.
     */
    @XmlElement(name = "CustomerID")
    private String customerid;
    /**
     * The Name of the Customer.
     */
    @XmlElement(name = "CustomerName")
    private String customername;
    /**
     * The Discount Rate for the Customer.
     */
    @XmlElement(name = "DiscountRate")
    private String discountrate;
    /**
     * The Destination Receipt of the Customer.
     */
    @XmlElement (name = "DestReceipt")
    private String destreceipt;
    /**
     * The EMail Address of the Customer.
     */
    @XmlElement (name = "MailAddress")
    private String mailaddress;
    /**
     * The date the Customer is registered in the database.
     */
    @XmlElement (name = "UpdDate")
    private String upddate;
    /**
     * The Destination Receipt of the Customer.
     */
    @XmlElement (name = "Grade")
    private String grade;
    /**
     * The EMail Address of the Customer.
     */
    @XmlElement (name = "Points")
    private int points;
    /**
     * The Customer's Address.
     */
    @XmlElement (name = "Address")
    private String address;

    /**
     * Get The Customer's Address.
     * @return The Customer's Address
     */
    public final String getAddress() {
        return address;
    }

    /**
     * Set the Customer's Address.
     * @param custAddress Customer's Address.
     */
    public final void setAddress(final String custAddress) {
        this.address = custAddress;
    }

    /**
     * Gets the Memberfs ID.
     *
     * @return      Returns Memberfs ID.
     */
    public final String getCustomerid() {
        return customerid;
    }

    /**
     * Sets the Memberfs ID.
     * @param customerID        The new value for Memberfs ID.
     */
    public final void setCustomerid(final String customerID) {
        this.customerid = customerID;
    }

    /**
     * Gets the Memberfs full name.
     * @return      Returns Memberfs full name.
     */
    public final String getCustomername() {
        return customername;
    }

    /**
     * Sets the Memberfs full name.
     *
     * @param customerName     The new value for Memberfs full name.
     */
    public final void setCustomername(final String customerName) {
        this.customername = customerName;
    }

    /**
     * Gets the Memberfs default value of discount rate.
     * @return      Returns Memberfs discount rate.
     */
    public final String getDiscountrate() {
        return discountrate;
    }

    /**
    * Sets the Memberfs value of discount rate.
    * @param discountRate   The new value for Memberfs discount rate.
    */
    public final void setDiscountrate(final String discountRate) {
        this.discountrate = discountRate;
    }

    /**
     * Gets the Memberfs choice for the destination of receipt.
     *
     * @return      Returns destination of receipt.
     */
    public final String getDestreceipt() {
        return destreceipt;
    }

    /**
     * Sets the Memberfs choice for the destination of receipt.
     * @param destReceipt   The new value for destination of receipt.
     */
    public final void setDestreceipt(final String destReceipt) {
        this.destreceipt = destReceipt;
    }

    /**
     * Gets the Memberfs Mail address which receipts are sent to.
     * @return      Returns Memberfs Mail address.
     */
    public final String getMailaddress() {
        return mailaddress;
    }

    /**
     * Sets the Memberfs Mail address which receipts are sent to.
     *
     * @param mailAddress   The new value for Memberfs Mail address.
     */
    public final void setMailaddress(final String mailAddress) {
        this.mailaddress = mailAddress;
    }

    /**
     * Gets the Updated date and time.
     *
     * @return      Returns the Updated date and time.
     */
    public final String getUpddate() {
        return upddate;
    }

    /**
     * Sets the Updated date and time.
     * @param updDate   The new value for the updated date and time.
     */
    public final void setUpddate(final String updDate) {
        this.upddate = updDate;
    }

    /**
     * Gets the Memberfs Grade.
     * @return      Returns Memberfs Grade.
     */
    public final String getGrade() {
         return grade;
    }

    /**
     * Sets the Memberfs Grade.
     * @param memberGrade     The new value for Memberfs Grade.
     */
    public final void setGrade(final String memberGrade) {
        this.grade = memberGrade;
    }

    /**
     * Gets the Memberfs Points.
     * @return      Returns Memberfs Points.
     */
    public final int getPoints() {
        return points;
    }

    /**
     * Sets the Memberfs Points.
     * @param custPoints    The new value for Memberfs Points.
     */
    public final void setPoints(final int custPoints) {
        this.points = custPoints;
    }
}
