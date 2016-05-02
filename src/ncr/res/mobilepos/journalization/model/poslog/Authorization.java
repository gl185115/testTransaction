/*
 *  Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Authorization
 *
 * Model Class for Authorization
 *
 * De la Cerna, Jessel G.
 * Campos, Carlos G.
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * Authorization Model Object.
 *
 * <P>An Authorization Node in POSLog XML.
 *
 * <P>The Authorization node is under Tender Node.
 * And mainly holds the approval code information during Credit Authorization.
 *
 * @see Tender
 *
 * @author Jessel Dela Cerna     <jd185128 @ ncr.com>
 * @author Carlos Campos         <cc185102 @ ncr.com>
 * @author mlwang                    <mlwangi @ isoftstone.com>
 */
 /**
 * 改定履歴
 * バージョン         改定日付               担当者名           改定内容
 * 1.01      2014.12.12    mlwang
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Authorization")
public class Authorization {
    @XmlAttribute(name = "SignatureRequiredFlag")
    private String signatureRequiredFlag;
    /**
     * The private member variable that will hold the RequestingTerminalID information.
     */
    @XmlElement(name = "RequestingTerminalID")
    private String requestingTerminalID;
    /**
     * The private member variable that will hold the approval code information.
     */
    @XmlElement(name = "AuthorizationCode")
    private String authorizationCode;
    /**
     * The private member variable that will hold the AuthorizationDateTime information.
     */
    @XmlElement(name = "AuthorizationDateTime")
    private String authorizationDateTime;
    /**
     * The private member variable that will hold the ReferenceNumber information.
     */
    @XmlElement(name = "ReferenceNumber")
    private String referenceNumber;
    /**
     * 1.01      2014.12.12    mlwang
     * barApplicationIdentifier
     * Application Identifier (AID) ※ ICクレジットの場合のみ
     */
    @XmlElement(name = "xebApplicationIdentifier")
    private String xebApplicationIdentifier;
    /**
     * xebApplicationIdentifier
     * Application Identifier (AID) ※ ICクレジットの場合のみ
     */
    @XmlElement(name = "xebApplicationTransactionCounter")
    private String xebApplicationTransactionCounter;
    /**
     * xebApplicationTransactionCounter
     * Application Identifier (AID) ※ ICクレジットの場合のみ
     */
    @XmlElement(name = "xebNPCSeqNo")
    private String xebNPCSeqNo;
    /**
     * NPC処理通番（伝票番号）
     */
    @XmlElement(name = "xebCardseqno")
    private String xebCardseqno;
    /**
     * カードシーケンス番号
     */
    @XmlElement(name = "xebApplicationname")
    private String xebApplicationname;
    /**
     * アプリケーション名
     */
    
    @XmlElement(name = "barApplicationIdentifier")
    private String barApplicationIdentifier;
    /**
     * 1.01      2014.12.12    mlwang
     * barApplicationTransactionCounter
     * Application Transaction Counter (ATC) ※ ICクレジットの場合のみ
     */
    @XmlElement(name = "barApplicationTransactionCounter")
    private String barApplicationTransactionCounter;
    /**
     * 1.01      2014.12.12    mlwang
     * 銀聯番号
     */
    @XmlElement(name = "ChinaUnionPayNumber")
    private String chinaUnionPayNumber;
    
    /**
     * 伝票番号　
     */
    @XmlElement(name = "SlipNo")
    private String slipNo;
    
    /**
     * ApplicationIdentifier　
     */
    @XmlElement(name = "ApplicationIdentifier")
    private String applicationIdentifier;
    
    /**
     * ApplicationTransactionCounter　
     */
    @XmlElement(name = "ApplicationTransactionCounter")
    private String applicationTransactionCounter;
    
    /**
     * NPCSeqNo　
     */
    @XmlElement(name = "NPCSeqNo")
    private String nPCSeqNo;
    
    /**
     * CardseqNo　
     */
    @XmlElement(name = "CardseqNo")
    private String cardseqNo;
    
    /**
     * ApplicationName　
     */
    @XmlElement(name = "ApplicationName")
    private String applicationName;
    
    /**
     * PreReferenceNumber　
     */
    @XmlElement(name = "PreReferenceNumber")
    private String preReferenceNumber;
    
    /**
     * PreNPCSeqNo　
     */
    @XmlElement(name = "PreNPCSeqNo")
    private String preNPCSeqNo;
    
    /**
     * Default Constructor for Authorization Class.
     */
    public Authorization() {
        authorizationCode = null;
        authorizationDateTime = null;
        requestingTerminalID = null;
        referenceNumber = null;
        barApplicationIdentifier = null;
        barApplicationTransactionCounter = null;
        chinaUnionPayNumber = null;
    }

    /**
     * Custom Constructor for Authorization Class that accepts new
     * value for authorization code.
     *
     * @param authorizationCodeToSet     The new value for authorization code
     */
    public Authorization(final String authorizationCodeToSet) {
        super();
        setAuthorizationCode(authorizationCodeToSet);
    }
    /**
     * Custom Constructor for Authorization Class that accepts new
     * value for authorization code.
     *
     * @param authorizationCodeToSet     The new value for authorization code
     * @param authorizationDateTimeToSet     The new value for authorization date time
     * @param requestingTerminalID     The new value for requesting terminal id
     * @param referenceNumber     The new value for reference number
     */
    public Authorization(final String authorizationCodeToSet,
            final String authorizationDateTimeToSet,
            final String requestingTerminalID,
            final String referenceNumber) {
        super();
        setAuthorizationCode(authorizationCodeToSet);
        setAuthorizationDateTime(authorizationDateTimeToSet);
        setRequestingTerminalID(requestingTerminalID);
        setReferenceNumber(referenceNumber);
    }

    public String getSignatureRequiredFlag() {
        return signatureRequiredFlag;
    }

    public void setSignatureRequiredFlag(String signatureRequiredFlag) {
        this.signatureRequiredFlag = signatureRequiredFlag;
    }

    /**
     * Gets the Authorization Code.
     *
     * @return         The value of the Authorization Code
     */
    public final String getAuthorizationCode() {
        return authorizationCode;
    }

    /**
     * Sets the Authorization Code.
     *
     * @param authorizationCodeToSet     The new value for authorization code
     */
    public final void setAuthorizationCode(
            final String authorizationCodeToSet) {
        this.authorizationCode = authorizationCodeToSet;
    }

    /**
     * @return the requestingTerminalID
     */
    public final String getRequestingTerminalID() {
        return requestingTerminalID;
    }

    /**
     * @param requestingTerminalID the requestingTerminalID to set
     */
    public final void setRequestingTerminalID(String requestingTerminalID) {
        this.requestingTerminalID = requestingTerminalID;
    }

    /**
     * @return the authorizationDateTime
     */
    public final String getAuthorizationDateTime() {
        return authorizationDateTime;
    }

    /**
     * @param authorizationDateTime the authorizationDateTime to set
     */
    public final void setAuthorizationDateTime(String authorizationDateTime) {
        this.authorizationDateTime = authorizationDateTime;
    }

    /**
     * @return the referenceNumber
     */
    public final String getReferenceNumber() {
        return referenceNumber;
    }

    /**
     * @param referenceNumber the referenceNumber to set
     */
    public final void setReferenceNumber(String referenceNumber) {
        this.referenceNumber = referenceNumber;
    }


    /**
     * @return barApplicationIdentifier
     */
    public String getBarApplicationIdentifier() {
        return barApplicationIdentifier;
    }

    /**
     * @param barApplicationIdentifier セットする barApplicationIdentifier
     */
    public void setBarApplicationIdentifier(String barApplicationIdentifier) {
        this.barApplicationIdentifier = barApplicationIdentifier;
    }

    /**
     * @return barApplicationTransactionCounter
     */
    public String getBarApplicationTransactionCounter() {
        return barApplicationTransactionCounter;
    }

    /**
     * @param barApplicationTransactionCounter セットする barApplicationTransactionCounter
     */
    public void setBarApplicationTransactionCounter(
            String barApplicationTransactionCounter) {
        this.barApplicationTransactionCounter = barApplicationTransactionCounter;
    }

    /**
	 * @return the xebApplicationIdentifier
	 */
	public String getXebApplicationIdentifier() {
		return xebApplicationIdentifier;
	}

	/**
	 * @param xebApplicationIdentifier the xebApplicationIdentifier to set
	 */
	public void setXebApplicationIdentifier(String xebApplicationIdentifier) {
		this.xebApplicationIdentifier = xebApplicationIdentifier;
	}

	/**
	 * @return the xebApplicationTransactionCounter
	 */
	public String getXebApplicationTransactionCounter() {
		return xebApplicationTransactionCounter;
	}

	/**
	 * @param xebApplicationTransactionCounter the xebApplicationTransactionCounter to set
	 */
	public void setXebApplicationTransactionCounter(String xebApplicationTransactionCounter) {
		this.xebApplicationTransactionCounter = xebApplicationTransactionCounter;
	}

	/**
	 * @return the xebNPCSeqNo
	 */
	public String getXebNPCSeqNo() {
		return xebNPCSeqNo;
	}

	/**
	 * @param xebNPCSeqNo the xebNPCSeqNo to set
	 */
	public void setXebNPCSeqNo(String xebNPCSeqNo) {
		this.xebNPCSeqNo = xebNPCSeqNo;
	}

	/**
	 * @return the xebCardseqno
	 */
	public String getXebCardseqno() {
		return xebCardseqno;
	}

	/**
	 * @param xebCardseqno the xebCardseqno to set
	 */
	public void setXebCardseqno(String xebCardseqno) {
		this.xebCardseqno = xebCardseqno;
	}

	/**
	 * @return the xebApplicationname
	 */
	public String getXebApplicationname() {
		return xebApplicationname;
	}

	/**
	 * @param xebApplicationname the xebApplicationname to set
	 */
	public void setXebApplicationname(String xebApplicationname) {
		this.xebApplicationname = xebApplicationname;
	}

	/**
     * @return ChinaUnionPayNumber
     */
    public String getChinaUnionPayNumber() {
		return chinaUnionPayNumber;
	}
    /**
     * @param ChinaUnionPayNumber セットする ChinaUnionPayNumber
     */
	public void setChinaUnionPayNumber(String chinaUnionPayNumber) {
		this.chinaUnionPayNumber = chinaUnionPayNumber;
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
     * Overrides the toString() Method.
     * @return The String Representation of Authorization.
     */
    public final String toString() {
        String authorization =  "AuthorizationCode : " + this.authorizationCode + "\r\n"
                + "RequestingTerminalID : " + this.requestingTerminalID + "\r\n"
                + "AuthorizationDateTime: " + this.authorizationDateTime + "\r\n"
                + "ReferenceNumber: " + this.referenceNumber + "\r\n"
                + "barApplicationIdentifier： " + this.barApplicationIdentifier + "\r\n"
                + "barApplicationTransactionCounter： " + this.barApplicationTransactionCounter + "\r\n"
                + "barChinaUnionPayNumber： " + this.chinaUnionPayNumber + "\r\n"
                + "SlipNo: " + this.slipNo;
        return authorization;
    }

    /**
     * @return the applicationIdentifier
     */
    public final String getApplicationIdentifier() {
        return applicationIdentifier;
    }

    /**
     * @param applicationIdentifier the applicationIdentifier to set
     */
    public final void setApplicationIdentifier(final String applicationIdentifier) {
        this.applicationIdentifier = applicationIdentifier;
    }

    /**
     * @return the applicationTransactionCounter
     */
    public final String getApplicationTransactionCounter() {
        return applicationTransactionCounter;
    }

    /**
     * @param applicationTransactionCounter the applicationTransactionCounter to set
     */
    public final void setApplicationTransactionCounter(final String applicationTransactionCounter) {
        this.applicationTransactionCounter = applicationTransactionCounter;
    }

    /**
     * @return the nPCSeqNo
     */
    public final String getNPCSeqNo() {
        return nPCSeqNo;
    }

    /**
     * @param nPCSeqNo the nPCSeqNo to set
     */
    public final void setNPCSeqNo(final String nPCSeqNo) {
        this.nPCSeqNo = nPCSeqNo;
    }

    /**
     * @return the cardseqNo
     */
    public final String getCardseqNo() {
        return cardseqNo;
    }

    /**
     * @param cardseqNo the cardseqNo to set
     */
    public final void setCardseqNo(final String cardseqNo) {
        this.cardseqNo = cardseqNo;
    }

    /**
     * @return the applicationName
     */
    public final String getApplicationName() {
        return applicationName;
    }

    /**
     * @param applicationName the applicationName to set
     */
    public final void setApplicationName(final String applicationName) {
        this.applicationName = applicationName;
    }

    /**
     * @return the preReferenceNumber
     */
    public final String getPreReferenceNumber() {
        return preReferenceNumber;
    }

    /**
     * @param preReferenceNumber the preReferenceNumber to set
     */
    public final void setPreReferenceNumber(final String preReferenceNumber) {
        this.preReferenceNumber = preReferenceNumber;
    }

    /**
     * @return the preNPCSeqNo
     */
    public final String getPreNPCSeqNo() {
        return preNPCSeqNo;
    }

    /**
     * @param preNPCSeqNo the preNPCSeqNo to set
     */
    public final void setPreNPCSeqNo(final String preNPCSeqNo) {
        this.preNPCSeqNo = preNPCSeqNo;
    }
}
