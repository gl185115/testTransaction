/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * UnionPay
 *
 * Model Class for CreditDebit
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 *
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * UnionPay Model Object.
 *
 * <P>A UnionPay Node in POSLog XML.
 *
 * <P>The UnionPay node is under TillSettle Node.
 * And mainly holds the Customer's UnionPay Information
 *
 * @author     yuanchi                 <yuanchia@isoftstone.com>
 * @see        TillSettle
 */
 /**
 * 改定履歴
 * バージョン         改定日付               担当者名           改定内容
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "UnionPay")
public class UnionPay {

    /**
     * The private member variable which holds the Card Type
     * information that the Customer owned.
     */
    @XmlAttribute(name = "CardType")
    private String cardType;
    /**
     * The private member variable which holds the Card Type
     * information that the Customer owned.
     */
    @XmlAttribute(name = "PaymentMethodCode")
    private String paymentMethodCode;
    /**
     * The private member variable which holds the Expiration Date
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "ExpirationDate")
    private String expirationDate;
    /**
     * The private member variable which holds the Company Code
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "CreditCardCompanyCode")
    private String creditCardCompanyCode;

    /**
     * The private member variable which holds the CardIssuerID
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "CardIssuerID")
    private String cardIssuerID;
    /**
     * The private member variable which holds the IssueSequence
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "IssueSequence")
    private String issueSequence;

    /**
     * The private member variable which holds the IssuerName
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "IssuerName")
    private String issuerName;
    /**
     * The private member variable which holds the Account Number
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "AccountNumber")
    private String accountNumber;
    /**
     * The private member variable which holds the Payment Method
     * information of the Card that the Customer owned.
     */
    @XmlElement(name = "PaymentMethod")
    private PaymentMethod paymentMethod;
    /**
     * Amount.
     */
    @XmlElement(name = "Amount")
    private String amount;
    /**
     * Count
     */
    @XmlElement(name = "Count")
    private String count;
    /**
     *
     * barExpirationDate
     */
    @XmlElement(name = "barExpirationDate")
    private String barExpirationDate;
    /**
     * Default Constructor for CreditDebit.
     *
     * <P>Initializes member variable.
     */
    public UnionPay() {
        cardType = null;
        creditCardCompanyCode = null;
        expirationDate = null;
        issuerName = null;
        accountNumber = null;
        cardIssuerID = null;
        paymentMethod = null;
        amount = null;
        count = null;
    }
    /**
     * @return the barExpirationDate
     */
    public final String getBarExpirationDate() {
        return barExpirationDate;
    }
    /**
     *
     * @param barExpirationDate
     */
    public final void setBarExpirationDate(String barExpirationDate) {
        this.barExpirationDate = barExpirationDate;
    }

    /**
     * The custom constructor for CreditDebit Class.
     *
     * Accepts new values for Card's type, company code,
     * and expiration date.
     *
     * @param cardTypeToSet                The new value for the Card Type
     * @param creditCardCompanyCodeToSet       The new value for Credit
     *                                      card company code
     * @param expirationDateToSet              The Card's expiration date
     */
    public UnionPay(final String cardTypeToSet,
            final String creditCardCompanyCodeToSet,
            final String expirationDateToSet) {
        super();
        setCardType(cardTypeToSet);
        setCreditCardCompanyCode(creditCardCompanyCodeToSet);
        setExpirationDate(expirationDateToSet);
    }

    /**
     * The custom constructor for CreditDebit Class.
     *
     * Accepts new values for Card's type, company code,
     * and expiration date.
     *
     * @param issuerName          The new value for the IssuerName
     * @param issueSequence       The new value for Credit Issue Sequence
     *
     * @param cardIssuerID      The CardIssuerID
     * @param paymentMethod	    The card's payment method
     */
    public UnionPay(final String issuerName,
            final String issueSequence,
            final String cardIssuerID,
            final PaymentMethod paymentMethod) {
        super();
        setIssuerName(issuerName);
        setIssueSequence(issueSequence);
        setCardIssuerID(cardIssuerID);
        setPaymentMethod(paymentMethod);
    }
    /**
     * Gets the Card's type information.
     *
     * @return        Returns the Card's type information
     */
    public final String getCardType() {
        return cardType;
    }

    /**
     * Sets the Card's type information.
     *
     * @param cardTypeToSet        The new value for the Card's Type
     */
    public final void setCardType(final String cardTypeToSet) {
        this.cardType = cardTypeToSet;
    }
    /**
     * Gets the payment method code information.
     *
     * @return        Returns the paymentMethodCode information
     */
    public final String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    /**
     * Sets the payment method code information.
     *
     * @param paymentMethodCodeToSet        The new value for the payment method code.
     */
    public final void setPaymentMethodCode(final String paymentMethodCodeToSet) {
        this.paymentMethodCode = paymentMethodCodeToSet;
    }
    /**
     * Gets the Card's company code.
     *
     * @return        The Card's company code
     */
    public final String getCreditCardCompanyCode() {
        return creditCardCompanyCode;
    }

    /**
     * Sets the Card's company code.
     *
     * @param creditCardCompanyCodeToSet   The new value for Card's company code
     */
    public final void setCreditCardCompanyCode(
            final String creditCardCompanyCodeToSet) {
        this.creditCardCompanyCode = creditCardCompanyCodeToSet;
    }

    /**
     * Gets the Card's expiration date information.
     *
     * @return        The Card's company code
     */
    public final String getExpirationDate() {
        return expirationDate;
    }

    /**
     * Sets the Card's expiration date information.
     *
     * @param expirationDateToSet    The new value for expiration date.
     */
    public final void setExpirationDate(final String expirationDateToSet) {
        this.expirationDate = expirationDateToSet;
    }

    /**
     * @return the cardIssuerID
     */
    public final String getCardIssuerID() {
        return cardIssuerID;
    }

    /**
     * @param cardIssuerID the cardIssuerID to set
     */
    public final void setCardIssuerID(String cardIssuerID) {
        this.cardIssuerID = cardIssuerID;
    }

    /**
     * @return the issueSequence
     */
    public final String getIssueSequence() {
        return issueSequence;
    }

    /**
     * @param issueSequence the issueSequence to set
     */
    public final void setIssueSequence(String issueSequence) {
        this.issueSequence = issueSequence;
    }

    /**
     * @return the issuerName
     */
    public final String getIssuerName() {
        return issuerName;
    }

    /**
     * @param issuerName the issuerName to set
     */
    public final void setIssuerName(String issuerName) {
        this.issuerName = issuerName;
    }

    /**
     * @return the accountNumber
     */
    public final String getAccountNumber() {
        return accountNumber;
    }

    /**
     * @param accountNumber the accountNumber to set
     */
    public final void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * @return the paymentMethod
     */
    public final PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod the paymentMethod to set
     */
    public final void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    /**
     * @return amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount セットする amount
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * @return count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count セットする count
     */
    public void setCount(String count) {
        this.count = count;
    }

    /**
     * Overrides the toString() Method.
     * @return the String representation of CreditDebit.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        String crlf = "\r\n";
        str.append("CardType : ").append(this.cardType).append(crlf)
           .append("CreditCompanyCode : ").append(this.creditCardCompanyCode)
           .append(crlf)
           .append("ExpirationDate : ").append(this.expirationDate)
           .append(crlf)
           .append("CardIssuerID : ").append(this.cardIssuerID)
           .append(crlf)
           .append("IssuerName : ").append(this.issuerName)
           .append(crlf)
           .append("IssueSequence : ").append(this.issueSequence)
           .append(crlf)
           .append("AccountNumber : ").append(this.accountNumber)
           .append(crlf)
           .append("PaymentMethod : ").append(this.paymentMethod);
        return str.toString();
    }
}
