/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for RetailTransaction
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Other Model Object.
 *
 * <P>A Other Node in POSLog XML.
 *
 * <P>The Other node is under Tender Node.
 * And mainly holds the information of the Other
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Other")
public class Other {
    /**
     * The private member variable that will hold the tenderFlag.
     */
    @XmlElement(name = "TenderFlag")
    private String tenderFlag;
    /**
     * The private member variable that will hold the tenderId.
     */
    @XmlElement(name = "TenderId")
    private String tenderId;
    
    /**
     * The private member variable that will hold the tenderName.
     */
    @XmlElement(name = "TenderName")
    private String tenderName;
    
    /**
     * The private member variable that will hold the memberNo.
     */
    @XmlElement(name = "MemberNo")
    private String memberNo;
    
    /**
     * The private member variable that will hold the AuthorizationCode.
     */
    @XmlElement(name = "AuthorizationCode")
    private String authorizationCode;
    
    /**
     * The private member variable that will hold the slipNo.
     */
    @XmlElement(name = "SlipNo")
    private String slipNo;
    
    /**
     * The private member variable that will hold the changeType.
     */
    @XmlElement(name = "ChangeType")
    private String changeType;
    
    /**
     * The private member variable that will hold the stampType.
     */
    @XmlElement(name = "StampType")
    private String stampType;
    
    /**
     * The private member variable that will hold the pointType.
     */
    @XmlElement(name = "PointType")
    private String pointType;
    
    @XmlElement(name = "Count")
    private String count;
    
    @XmlElement(name = "Amount")
    private Amount amount;
    
    public final Amount getAmount() {
    	return this.amount;
    }
    public final void setAmount(Amount amt) {
    	this.amount = amt;
    }
    
    public final String getCount() {
    	return this.count;
    }
    public final void setCount(String cnt) {
    	this.count = cnt;
    }

    /**
     * @return the tenderFlag
     */
    public final String getTenderFlag() {
        return tenderFlag;
    }

    /**
     * @param tenderFlag the tenderFlag to set
     */
    public final void setTenderFlag(final String tenderFlag) {
        this.tenderFlag = tenderFlag;
    }

    /**
     * @return the tenderId
     */
    public final String getTenderId() {
        return tenderId;
    }

    /**
     * @param tenderId the tenderId to set
     */
    public final void setTenderId(final String tenderId) {
        this.tenderId = tenderId;
    }

    /**
     * @return the tenderName
     */
    public final String getTenderName() {
        return tenderName;
    }

    /**
     * @param tenderName the tenderName to set
     */
    public final void setTenderName(final String tenderName) {
        this.tenderName = tenderName;
    }

    /**
     * @return the memberNo
     */
    public final String getMemberNo() {
        return memberNo;
    }

    /**
     * @param memberNo the memberNo to set
     */
    public final void setMemberNo(final String memberNo) {
        this.memberNo = memberNo;
    }

    /**
     * @return the authorizationCode
     */
    public final String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * @param authorizationCode the authorizationCode to set
     */
    public final void setAuthorizationCode(final String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    /**
     * @return the slipNo
     */
    public final String getSlipNo() {
        return slipNo;
    }

    /**
     * @param slipNo the slipNo to set
     */
    public final void setSlipNo(final String slipNo) {
        this.slipNo = slipNo;
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
