/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditDebit
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
 * CreditDebit Model Object.
 *
 * <P>A CreditDebit Node in POSLog XML.
 *
 * <P>The CreditDebit node is under Tender Node.
 * And mainly holds the Customer's Credit Information
 *
 * @author     Jessel De La Cerna     <jd185128 @ ncr.com>
 * @author     Carlos Campos          <cc185102 @ ncr.com>
 * @author     mlwang                 <mlwangi @ isoftstone.com>
 * @see           Tender
 */
 /**
 * 改定履歴
 * バージョン         改定日付               担当者名           改定内容
 * 1.01      2014.12.12    mlwang
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CreditDebit")
public class CreditDebit {

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
     * The private member variable which holds the Card Type
     * information that the Customer owned.
     */
    @XmlAttribute(name = "PaymentMethodName")
    private String PaymentMethodName;
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
     * 1.01      2014.12.12    mlwang
     * amount.
     */
    @XmlElement(name = "Amount")
    private String amount;
    /**
     * 1.01      2014.12.12    mlwang
     * Count
     */
    @XmlElement(name = "Count")
    private String count;
    /***
     * registeredCount;
     */
    @XmlElement(name = "RegisteredCount")
    private String registeredCount;
    /***
     * currentCount
     */
    @XmlElement(name = "CurrentCount")
    private String currentCount;
    /***
     * currentAmount;
     */
    @XmlElement(name = "CurrentAmount")
    private String currentAmount;
    /***
     * difference
     */
    @XmlElement(name = "Difference")
    private String difference;
    /**
     *
     * barExpirationDate
     */
    @XmlElement(name = "xebExpirationDate")
    private String xebExpirationDate;
    /**
    *
    * xebExpirationDate
    */
   @XmlElement(name = "barExpirationDate")
   private String barExpirationDate;
    /**
     * ChangeType
     */
    @XmlElement(name = "ChangeType")
    private String changeType;
   
    /**
     * StampType
     */
    @XmlElement(name = "StampType")
    private String stampType;
    
    /**
     * PointType
     */
    @XmlElement(name = "PointType")
    private String pointType;
   
    /**
     * Default Constructor for CreditDebit.
     *
     * <P>Initializes member variable.
     */
    public CreditDebit() {
        cardType = null;
        creditCardCompanyCode = null;
        expirationDate = null;
        issuerName = null;
        accountNumber = null;
        cardIssuerID = null;
        paymentMethod = null;
        amount = null;
        count = null;
        cardIssuerID = null;
        barExpirationDate = null;
        changeType = null;
        stampType = null;
        pointType = null;
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
    public CreditDebit(final String cardTypeToSet,
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
    public CreditDebit(final String issuerName,
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
	 * @return the paymentMethodName
	 */
	public String getPaymentMethodName() {
		return PaymentMethodName;
	}
	/**
	 * @param paymentMethodName the paymentMethodName to set
	 */
	public void setPaymentMethodName(String paymentMethodName) {
		PaymentMethodName = paymentMethodName;
	}
	/**
	 * @return the xebExpirationDate
	 */
	public String getXebExpirationDate() {
		return xebExpirationDate;
	}
	/**
	 * @param xebExpirationDate the xebExpirationDate to set
	 */
	public void setXebExpirationDate(String xebExpirationDate) {
		this.xebExpirationDate = xebExpirationDate;
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
           .append("PaymentMethod : ").append(this.paymentMethod)
           .append("CardIssuerID : ").append(this.cardIssuerID)
           .append(crlf)
           .append("BarExpirationDate : ").append(this.barExpirationDate)
           .append(crlf)
           .append("ChangeType : ").append(this.changeType)
           .append(crlf)
           .append("StampType : ").append(this.stampType)
           .append(crlf)
           .append("PointType : ").append(this.pointType);
        return str.toString();
    }
    /**
     * Set registeredCount.
     * @param regCount
     */
    public final void setRegisteredCount(String regCount) {
    	this.registeredCount = regCount;
    }
    /***
     * Return registeredCount
     * @return
     */
    public final String getRegisteredCount() {
    	return this.registeredCount;
    }
    /***
     * Set currentCount
     * @param currCount
     */
    public final void setCurrentCount(String currCount) {
    	this.currentCount = currCount;
    }
    /***
     * Returnn currenctCount
     * @return
     */
    public final String getCurrentCount() {
    	return this.currentCount;
    }
    /**
     * set currentAmount
     */
    public final void setCurrentAmount(String currAmt) {
    	this.currentAmount = currAmt;
    }
    /***
     * get currAmount
     */
    public final String getCurrentAmount() {
    	return this.currentAmount;
    }
    /***
     * set difference
     */
    public final void setDifference(String diff) {
    	this.difference = diff;
    }
    /***
     * return difference
     */
    public final String getDifference() {
    	return this.difference;
    }
    /**
     * @return the changeType
     */
    public final String getChangeType() {
        return changeType;
    }
    /**
     * @param changeType the changeType to set
     */
    public final void setChangeType(final String changeType) {
        this.changeType = changeType;
    }
    /**
     * @return the stampType
     */
    public final String getStampType() {
        return stampType;
    }
    /**
     * @param stampType the stampType to set
     */
    public final void setStampType(final String stampType) {
        this.stampType = stampType;
    }
    /**
     * @return the pointType
     */
    public final String getPointType() {
        return pointType;
    }
    /**
     * @param pointType the pointType to set
     */
    public final void setPointType(final String pointType) {
        this.pointType = pointType;
    }
}
