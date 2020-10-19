package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.NONE)
public class PayPassportRequest {
    
    @XmlElement(name = "authKey")
    private String authKey;
    @XmlElement(name = "memberID")
    private String memberID;
    @XmlElement(name = "paymentType")
    private String paymentType;
    @XmlElement(name = "paymentInfoCode")
    private String paymentInfoCode;
    @XmlElement(name = "strCd")
    private String strCd;
    @XmlElement(name = "paymentRequestCode")
    private String paymentRequestCode;
    @XmlElement(name = "amount")
    private int amount;
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
    @Override
    public String toString() {
        return "PayPassportRequest [authKey=" + authKey + ", memberID=" + memberID + ", paymentType=" + paymentType
                + ", paymentInfoCode=" + paymentInfoCode + ", strCd=" + strCd + ", paymentRequestCode="
                + paymentRequestCode + ", amount=" + amount + "]";
    }

}
