/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLog
 *
 * Model Class for PosLog
 *
 * Carlos G. Campos
 */
package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;

/**
 * SearchedPosLog Model Object.
 *
 * <P>A POSLog object representing a PosLog XML.
 *
 * <P>SearchedPOSLog is a model class that represents a POSLog retrieved from
 * transaction search. Specially for Last Normal Transaction search.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLog")
@ApiModel(value="SearchedPosLog")
public class SearchedPosLog extends ResultBase {

    /**
     * The private member variable that will hold the Transaction information.
     */
    @XmlElement(name = "Transaction")
    private Transaction transaction;
    
    /**
     * The private member variable that will hold the Additional information.
     */
    @XmlElement(name = "AdditionalInformation")
    private AdditionalInformation additionalInformation;
    
    /**
     * The private member variable that will hold the MemberInfo information.
     */
    @XmlElement(name = "MemberInfo")
    private MemberInfo memberInfo;

    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

    /**
     * Default Constructor for PosLog.
     *
     * <P>Initializes Transaction object.
     */
    public SearchedPosLog() {
        transaction = new Transaction();
    }

    /**
     * Sets the PosLog.
     *
     * @param transactionToSet        The new value for the Transaction
     *                                   under POSLog.
     */
    public SearchedPosLog(final Transaction transactionToSet) {
        setTransaction(transactionToSet);
    }

    /**
     * Gets the Transaction.
     *
     * @return        Returns the Transaction under POSLog.
     */
    @ApiModelProperty(value="取引", notes="取引")
    public final Transaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the Transaction.
     *
     * @param transactionToSet        The new value for the Transaction
     *                                   under POSLog.
     */
    public final void setTransaction(final Transaction transactionToSet) {
        this.transaction = transactionToSet;
    }

    /**
     * @return the additionalInformation
     */
    @ApiModelProperty(value="エキストラの情報を得る", notes="エキストラの情報を得る")
    public AdditionalInformation getAdditionalInformation() {
        return additionalInformation;
    }

    /**
     * @param additionalInformation the additionalInformation to set
     */
    public void setAdditionalInformation(final AdditionalInformation additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    /**
     * Overrides the toString() method.
     * @return The String representration of POSLog.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        str.append(super.toString()).append("\r\n");

        if (null != transaction) {
            str.append("Transaction: ").append(transaction.toString());
        }

        return str.toString();
    }
}
