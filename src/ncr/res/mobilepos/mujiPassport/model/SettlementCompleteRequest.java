package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class SettlementCompleteRequest {
    
    @XmlElement(name = "authKey")
    private String authKey;
    
    @XmlElement(name = "memberID")
    private String memberID;
    
    @XmlElement(name = "settlementId")
    private String settlementId;
    
    @XmlElement(name = "journalId")
    private String journalId;
    
    @XmlElement(name = "tradeDate")
    private String tradeDate;
    
    @XmlElement(name = "strCd")
    private String strCd;
    
    @XmlElement(name = "amount")
    private int amount;
    
    @XmlElement(name = "paymentType")
    private String paymentType;
    
    @XmlElement(name = "paymentRequestCode")
    private String paymentRequestCode;
    
    @XmlElement(name = "paymentInfoCode")
    private String paymentInfoCode;
    
    @XmlElement(name = "prepaidBalance")
    private int prepaidBalance;
    
    @XmlElement(name = "prepaidExpirationDate")
    private String prepaidExpirationDate;

    /**
     * @return the authKey
     */
    public String getAuthKey() {
        return authKey;
    }

    /**
     * @param authKey the authKey to set
     */
    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    /**
     * @return the memberID
     */
    public String getMemberID() {
        return memberID;
    }

    /**
     * @param memberID the memberID to set
     */
    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    /**
     * @return the settlementId
     */
    public String getSettlementId() {
        return settlementId;
    }

    /**
     * @param settlementId the settlementId to set
     */
    public void setSettlementId(String settlementId) {
        this.settlementId = settlementId;
    }

    /**
     * @return the journalId
     */
    public String getJournalId() {
        return journalId;
    }

    /**
     * @param journalId the journalId to set
     */
    public void setJournalId(String journalId) {
        this.journalId = journalId;
    }

    /**
     * @return the tradeDate
     */
    public String getTradeDate() {
        return tradeDate;
    }

    /**
     * @param tradeDate the tradeDate to set
     */
    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * @return the strCd
     */
    public String getStrCd() {
        return strCd;
    }

    /**
     * @param strCd the strCd to set
     */
    public void setStrCd(String strCd) {
        this.strCd = strCd;
    }

    /**
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * @return the paymentType
     */
    public String getPaymentType() {
        return paymentType;
    }

    /**
     * @param paymentType the paymentType to set
     */
    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    /**
     * @return the paymentRequestCode
     */
    public String getPaymentRequestCode() {
        return paymentRequestCode;
    }

    /**
     * @param paymentRequestCode the paymentRequestCode to set
     */
    public void setPaymentRequestCode(String paymentRequestCode) {
        this.paymentRequestCode = paymentRequestCode;
    }

    /**
     * @return the paymentInfoCode
     */
    public String getPaymentInfoCode() {
        return paymentInfoCode;
    }

    /**
     * @param paymentInfoCode the paymentInfoCode to set
     */
    public void setPaymentInfoCode(String paymentInfoCode) {
        this.paymentInfoCode = paymentInfoCode;
    }

    /**
     * @return the prepaidBalance
     */
    public int getPrepaidBalance() {
        return prepaidBalance;
    }

    /**
     * @param prepaidBalance the prepaidBalance to set
     */
    public void setPrepaidBalance(int prepaidBalance) {
        this.prepaidBalance = prepaidBalance;
    }

    /**
     * @return the prepaidExpirationDate
     */
    public String getPrepaidExpirationDate() {
        return prepaidExpirationDate;
    }

    /**
     * @param prepaidExpirationDate the prepaidExpirationDate to set
     */
    public void setPrepaidExpirationDate(String prepaidExpirationDate) {
        this.prepaidExpirationDate = prepaidExpirationDate;
    }

    @Override
    public String toString() {
        return "SettlementCompleteRequest [authKey=" + authKey + ", memberID=" + memberID + ", settlementId="
                + settlementId + ", journalId=" + journalId + ", tradeDate=" + tradeDate + ", strCd=" + strCd
                + ", amount=" + amount + ", paymentType=" + paymentType + ", paymentRequestCode=" + paymentRequestCode
                + ", paymentInfoCode=" + paymentInfoCode + ", prepaidBalance=" + prepaidBalance
                + ", prepaidExpirationDate=" + prepaidExpirationDate + "]";
    }

}
