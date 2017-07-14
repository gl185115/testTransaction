/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Tender
 *
 * Model Class for Tender
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tender Model Object.
 *
 * <P>A Tender Node in POSLog XML.
 *
 * <P>The Tender node is under LineItem Node.
 * And holds the tender type information from the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Tender")
public class Tender {

    /**
    * The private member variable that will hold the VoucherId of Tender.
    */
    @XmlElement(name = "VoucherId")
    private String voucherId;

    /**
    * The private member variable that will hold the VoucherName of Tender.
    */
    @XmlElement(name = "VoucherName")
    private String voucherName;

    /**
     * The private member variable that will hold the amount of Tender.
     */
    @XmlElement(name = "Amount")
    private String amount;

    /**
     * The private member variable that will hold the tender type of Tender.
     */
    @XmlAttribute(name = "TenderType")
    private String tenderType;

    /**
     * The private member variable that will hold the tender type of Tender.
     */
    @XmlAttribute(name = "TenderTiming")
    private String tenderTiming;

    /**
     * The private member variable that will hold the type code of Tender.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;

    /**
     * The private member variable that will hold the type class of Tender.
     */
    @XmlAttribute(name = "TenderClass")
    private String tenderClass;

    /**
     * The private member variable that will hold the Tax.
     */
    @XmlElement(name = "Tax")
    private Tax tax;
    /**
     * The private member variable that will hold the Voucher.
     */
    @XmlElement(name = "Voucher")
    private List<Voucher> voucher;
    /**
     * The private member variable that will hold the OriginalVoucher.
     */
    @XmlElement(name = "OriginalVoucher")
    private List<Voucher> originalVoucher;
    /**
     * The private member variable that will hold the Authorization.
     */
    @XmlElement(name = "Authorization")
    private Authorization authorization;

    /**
     * The private member variable that will hold the CreditDebit.
     */
    @XmlElement(name = "CreditDebit")
    private CreditDebit creditDebit;

    /**
     * The private member variable that will hold the TenderChange.
     */
    @XmlElement(name = "TenderChange")
    private TenderChange tenderChange;
    /**
     *
     */
    @XmlElement(name = "StoredValueInstrument")
    private StoredValueInstrument storedValueInstrument;

    /**
     * The private member variable that will hold the MonetaryKind.
     */
    @XmlElement(name = "MonetaryKind")
    private List<MonetaryKind> monetaryKind;

    /**
     * The private member variable that will hold text notes for this line item
     */
    @XmlElement(name = "Note")
    private String note;

    /**
     * The private member variable that will hold other for this line item
     */
    @XmlElement(name = "Other")
    private Other other;

    /***
     * The private member variable that will hold netTotal for this line item
     */
    @XmlElement(name = "NetTotal")
    private String netTotal;

    @XmlElement(name = "GrossTotal")
    private String grossTotal;

    @XmlElement(name = "Difference")
    private String difference;

    @XmlElement(name = "ChangeReserve")
    private String changeReserve;

    @XmlElement(name = "Devices")
    private Devices devices;

    /**
     * The private member variable that will hold the category of Tender.
     */
    @XmlElement(name = "Category")
    private String category;

    /**
     * The private member variable that will hold the Id of Tender.
     */
    @XmlElement(name = "TenderId")
    private String tenderId;

    @XmlAttribute(name = "Voidable")
    private String voidable;

    @XmlAttribute(name = "Returnable")
    private String returnable;

    @XmlAttribute(name = "Cancelable")
    private String cancelable;

    @XmlAttribute(name = "TenderName")
    private String tenderName;


    @XmlAttribute(name = "SubTenderType")
    private String subTenderType;

    @XmlAttribute(name = "TenderIdentification")
    private String tenderIdentification;

    /**
     * The default constructor for Tender class.
     */
    public Tender() {
        this.authorization = new Authorization();
        this.creditDebit = new CreditDebit();
        this.tenderChange = new TenderChange();
        this.voucher = new ArrayList<Voucher>();
        this.amount = null;
        this.tenderType = null;
        this.typeCode = null;
        this.monetaryKind = new ArrayList<MonetaryKind>();
        this.tenderClass = null;
    }

    /**
     * Gets the cash amount.
     *
     * @return        The amount of Tender.
     */
    public final String getAmount() {
        return (amount == null || amount.equals("")) ? "0" : amount;
    }

    /**
     * Sets the cash amount.
     *
     * @param amountToSet        The new value for the amount of Tender.
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * Gets the type of payment.
     *
     * @return        The tender type of Tender.
     */
    public final String getTenderType() {
        return tenderType;
    }

    /**
     * Sets the type of payment.
     *
     * @param tenderTypeToSet      The new value for the tender type of Tender.
     */
    public final void setTenderType(final String tenderTypeToSet) {
        this.tenderType = tenderTypeToSet;
    }

    /**
     * Gets the type code of Tender.
     *
     * @return        The type code of Tender.
     */
    public final String getTypeCode() {
        return typeCode;
    }

    /**
     * Sets the type code of Tender.
     *
     * @param typeCodeToSet        The new value for the type code of Tender.
     */
    public final void setTypeCode(final String typeCodeToSet) {
        this.typeCode = typeCodeToSet;
    }

    /**
     * Gets the type class of Tender.
     *
     * @return        The type class of Tender.
     */
    public final String getTenderClass() {
        return tenderClass;
    }

    /**
     * Sets the type class of Tender.
     *
     * @param tenderClassToSet        The new value for the type class of Tender.
     */
    public final void setTenderClass(final String tenderClassToSet) {
        this.tenderClass = tenderClassToSet;
    }

    /**
     * Gets the Authorization when purchasing.
     *
     * @return        The Authorization.
     */
    public final Authorization getAuthorization() {
        return authorization;
    }

    /**
     * Sets the Authorization when purchasing.
     *
     * @param authorizationToSet   The new value for the
     *                              Authorization when purchasing.
     */
    public final void setAuthorization(final Authorization authorizationToSet) {
        this.authorization = authorizationToSet;
    }

    /**
     * Sets the CreditDebit when purchasing.
     *
     * @param creditDebitToSet    The new value for the
     *                              CreditDebit when purchasing.
     */
    public final void setCreditDebit(final CreditDebit creditDebitToSet) {
        this.creditDebit = creditDebitToSet;
    }

    /**
     * Gets the CreditDebit when purchasing.
     *
     * @return        The CreditDebit.
     */
    public final CreditDebit getCreditDebit() {
        return creditDebit;
    }

    /**
     * Sets the tender change under LineItem.
     *
     * @param tenderChangeToSet
     *     The new value for the TenderChange under LineItem.
     */
    public final void setTenderChange(final TenderChange tenderChangeToSet) {
        this.tenderChange = tenderChangeToSet;
    }

    /**
     * Gets the TenderChange under LineItem.
     *
     * @return        The TenderChange under LineItem.
     */
    public final TenderChange getTenderChange() {
        return tenderChange;
    }

    /**
     * @return the voucher
     */
    public final List<Voucher> getVoucher() {
        return voucher;
    }

    /**
     * @param voucher the voucher to set
     */
    public final void setVoucher(List<Voucher> voucher) {
        this.voucher = voucher;
    }

    /**
     * @return the originalVoucher
     */
    public final List<Voucher> getOriginalVoucher() {
        return originalVoucher;
    }

    /**
     * @param originalVoucher the originalVoucher to set
     */
    public final void setOriginalVoucher(List<Voucher> originalVoucher) {
        this.originalVoucher = originalVoucher;
    }

    /**
     * @return the tax
     */
    public final Tax getTax() {
        return tax;
    }

    /**
     * @param tax the tax to set
     */
    public final void setTax(Tax tax) {
        this.tax = tax;
    }

    public StoredValueInstrument getStoredValueInstrument() {
        return storedValueInstrument;
    }

    public void setStoredValueInstrument(StoredValueInstrument storedValueInstrument) {
        this.storedValueInstrument = storedValueInstrument;
    }

    /**
     * @return the monetaryKind
     */
    public List<MonetaryKind> getMonetaryKind() {
        return monetaryKind;
    }

    /**
     * @param monetaryKind the monetaryKind to set
     */
    public void setMonetaryKind(List<MonetaryKind> monetaryKind) {
        this.monetaryKind = monetaryKind;
    }

    public final String getNote() {
		return note;
	}

	public final void setNote(final String note) {
		this.note = note;
	}

	/**
     * Overrides the toString() Method.
     * @return The string representation of tender.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Amount : ").append(this.amount).append(crlf)
           .append("TenderType : ").append(this.tenderType).append(crlf)
           .append("TypeCode : ").append(this.typeCode).append(crlf);

        if (null != this.authorization) {
           str.append("Authorization : ")
              .append(this.authorization.toString()).append(crlf);
        }

        if (null  != this.creditDebit) {
            str.append("CreditDebit : ")
               .append(this.creditDebit.toString()).append(crlf);
        }

        if (null != this.tenderChange) {
            str.append("tenderChange : ")
               .append(this.tenderChange.toString());
        }

        if (null != this.voucher) {
            str.append("Voucher : ")
               .append(this.voucher.toString());
        }

        if (null != this.tax) {
            str.append("Tax : ")
               .append(this.tax.toString());
        }

        if(null != this.monetaryKind){
            str.append("MonetaryKind:")
            .append(this.monetaryKind.toString()).append(crlf);
        }

        return str.toString();
    }

    public final void setNetTotal(String ntotal) {
    	this.netTotal = ntotal;
    }

    public final String getNetTotal() {
    	return this.netTotal;
    }

    public final void setGrossTotal(String grsTotal) {
    	this.grossTotal = grsTotal;
    }

    public final String getGrossTotal() {
    	return grossTotal;
    }

    public final void setDifference(String diff) {
    	this.difference = diff;
    }

    public final String getDifference() {
    	return this.difference;
    }

    public final void setChangeReserve(String changeReserve) {
    	this.changeReserve = changeReserve;
    }

    public final String getChangeReserve() {
    	return this.changeReserve;
    }

    public final void setDevices(Devices obj) {
    	this.devices = obj;
    }

    public final Devices getDevices() {
    	return devices;
    }

    /**
     * @return the other
     */
    public final Other getOther() {
        return other;
    }

    /**
     * @param other the other to set
     */
    public final void setOther(final Other other) {
        this.other = other;
    }

    /**
     * @return the voucherId
     */
    public final String getVoucherId() {
        return voucherId;
    }

    /**
     * @param voucherId the voucherId to set
     */
    public final void setVoucherId(String voucherId) {
        this.voucherId = voucherId;
    }

    /**
     * @return the voucherName
     */
    public final String getVoucherName() {
        return voucherName;
    }

    /**
     * @param voucherName the voucherName to set
     */
    public final void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }

	public final String getTenderTiming() {
		return tenderTiming;
	}

	public final void setTenderTiming(String tenderTiming) {
		this.tenderTiming = tenderTiming;
	}

    public final void setCategory(String category) {
    	this.category = category;
    }

    public final String getCategory() {
    	return this.category;
    }

    public final void setTenderId(String tenderId) {
    	this.tenderId = tenderId;
    }

    public final String getTenderId() {
    	return this.tenderId;
    }

    public String getVoidable() {
        return voidable;
    }

    public void setVoidable(String voidable) {
        this.voidable = voidable;
    }

    public String getReturnable() {
        return returnable;
    }

    public void setReturnable(String returnable) {
        this.returnable = returnable;
    }

    public String getCancelable() {
        return cancelable;
    }

    public void setCancelable(String cancelable) {
        this.cancelable = cancelable;
    }

    public String getTenderName() {
        return tenderName;
    }

    public void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }

    public String getSubTenderType() {
        return subTenderType;
    }

    public void setSubTenderType(String subTenderType) {
        this.subTenderType = subTenderType;
    }

    public String getTenderIdentification() {
        return tenderIdentification ;
    }

    public void setTenderIdentification(String tenderIdentification ) {
        this.tenderIdentification = tenderIdentification ;
    }

}
