package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PaymentOnAccount")
public class PaymentOnAccount {

    @XmlAttribute(name = "AccountCode")
    private String accountCode;

    @XmlElement(name = "AccountNumber")
    private String accountNumber;

    @XmlElement(name = "Amount")
    private double amount;

    @XmlElement(name = "Note")
    private String note;

    /**
     * InventoryReservationID
     */
    @XmlElement(name = "InventoryReservationID")
    private String inventoryReservationID;

    /**
     * TenderChange
     * 前受金のお釣り
     */
    @XmlElement(name = "TenderChange")
    private TenderChange tenderChange;

    @XmlElement(name = "PreviousAmount")
    private double previousAmount;

    /**
     * Default Constructor for Sale.
     *
     * Sets the member variable to default value.
     */
    public PaymentOnAccount() {
        accountCode = null;
        accountNumber = null;
        amount = 0;
    }

    public final String getAccountCode() {
        return accountCode;
    }

    public final void setAccountCode(final String accountCode) {
        this.accountCode = accountCode;
    }

    public final String getAccountNumber() {
        return accountNumber;
    }

    public final void setAccountNumber(final String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public final double getAmount() {
        return amount;
    }

    public final void setAmount(final double amount) {
        this.amount = amount;
    }

    public final String getNote() {
        return note;
    }

    public final void setNote(final String note) {
        this.note = note;
    }

    /**
     * @return inventoryReservationID
     */
    public String getInventoryReservationID() {
        return inventoryReservationID;
    }

    /**
     * @param inventoryReservationID セットする inventoryReservationID
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
    }

    /**
     * @return tenderChange
     */
    public TenderChange getTenderChange() {
        return tenderChange;
    }

    /**
     * @param tenderChange セットする tenderChange
     */
    public void setTenderChange(TenderChange tenderChange) {
        this.tenderChange = tenderChange;
    }

    /**
     * @return previousAmount
     */
    public double getPreviousAmount() {
        return previousAmount;
    }

    /**
     * @param previousAmount セットする previousAmount
     */
    public void setPreviousAmount(double previousAmount) {
        this.previousAmount = previousAmount;
    }

    /**
     * Overrides the toString() Method.
     * @return The string representation of paymentOnAccount.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("AccountCode : ").append(this.accountCode).append(crlf)
           .append("AccountNumber : ").append(this.accountNumber).append(crlf)
           .append("Amount : ").append(this.amount).append("");

        return str.toString();
    }

}
