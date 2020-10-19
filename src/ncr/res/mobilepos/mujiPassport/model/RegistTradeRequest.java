package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.NONE)
public class RegistTradeRequest {

    @XmlElement(name = "authKey")
    private String authKey;

    @XmlElement(name = "memberID")
    private String memberID;

    @XmlElement(name = "strCd")
    private String strCd;
    
    @XmlElement(name = "registarNo")
    private String registarNo;
    
    @XmlElement(name = "tradeDate")
    private String tradeDate;
    
    @XmlElement(name = "tradeNo")
    private String tradeNo;
    
    @XmlElement(name = "tradeType")
    private int tradeType;
    
    @XmlElement(name = "beforeTradeDate")
    private String beforeTradeDate;
    
    @XmlElement(name = "beforeTradeNo")
    private String beforeTradeNo;
    
    @XmlElement(name = "couponAmount")
    private int couponAmount;
    
    @XmlElement(name = "additionalMile")
    private int additionalMile;
    
    @XmlElement(name = "useCouponList")
    private String useCouponList;
    
    @XmlElement(name = "mybag")
    private int mybag;

    /**
     * @return the authKey
     */
    public final String getAuthKey() {
        return authKey;
    }

    /**
     * @param authKey the authKey to set
     */
    public final void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    /**
     * @return the memberID
     */
    public final String getMemberID() {
        return memberID;
    }

    /**
     * @param memberID the memberID to set
     */
    public final void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    /**
     * @return the strCd
     */
    public final String getStrCd() {
        return strCd;
    }

    /**
     * @param strCd the strCd to set
     */
    public final void setStrCd(String strCd) {
        this.strCd = strCd;
    }

    /**
     * @return the registarNo
     */
    public final String getRegistarNo() {
        return registarNo;
    }

    /**
     * @param registarNo the registarNo to set
     */
    public final void setRegistarNo(String registarNo) {
        this.registarNo = registarNo;
    }

    /**
     * @return the tradeDate
     */
    public final String getTradeDate() {
        return tradeDate;
    }

    /**
     * @param tradeDate the tradeDate to set
     */
    public final void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    /**
     * @return the tradeNo
     */
    public final String getTradeNo() {
        return tradeNo;
    }

    /**
     * @param tradeNo the tradeNo to set
     */
    public final void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    /**
     * @return the tradeType
     */
    public final int getTradeType() {
        return tradeType;
    }

    /**
     * @param tradeType the tradeType to set
     */
    public final void setTradeType(int tradeType) {
        this.tradeType = tradeType;
    }

    /**
     * @return the beforeTradeDate
     */
    public final String getBeforeTradeDate() {
        return beforeTradeDate;
    }

    /**
     * @param beforeTradeDate the beforeTradeDate to set
     */
    public final void setBeforeTradeDate(String beforeTradeDate) {
        this.beforeTradeDate = beforeTradeDate;
    }

    /**
     * @return the beforeTradeNo
     */
    public final String getBeforeTradeNo() {
        return beforeTradeNo;
    }

    /**
     * @param beforeTradeNo the beforeTradeNo to set
     */
    public final void setBeforeTradeNo(String beforeTradeNo) {
        this.beforeTradeNo = beforeTradeNo;
    }

    /**
     * @return the couponAmount
     */
    public final int getCouponAmount() {
        return couponAmount;
    }

    /**
     * @param couponAmount the couponAmount to set
     */
    public final void setCouponAmount(int couponAmount) {
        this.couponAmount = couponAmount;
    }

    /**
     * @return the additionalMile
     */
    public final int getAdditionalMile() {
        return additionalMile;
    }

    /**
     * @param additionalMile the additionalMile to set
     */
    public final void setAdditionalMile(int additionalMile) {
        this.additionalMile = additionalMile;
    }

    /**
     * @return the useCouponList
     */
    public final String getUseCouponList() {
        return useCouponList;
    }

    /**
     * @param useCouponList the useCouponList to set
     */
    public final void setUseCouponList(String useCouponList) {
        this.useCouponList = useCouponList;
    }

    /**
     * @return the mybag
     */
    public final int getMybag() {
        return mybag;
    }

    /**
     * @param mybag the mybag to set
     */
    public final void setMybag(int mybag) {
        this.mybag = mybag;
    }

    @Override
    public String toString() {
        return "RegistTradeRequest [" + (authKey != null ? "authKey=" + authKey + ", " : "")
                + (memberID != null ? "memberID=" + memberID + ", " : "")
                + (strCd != null ? "strCd=" + strCd + ", " : "")
                + (registarNo != null ? "registarNo=" + registarNo + ", " : "")
                + (tradeDate != null ? "tradeDate=" + tradeDate + ", " : "")
                + (tradeNo != null ? "tradeNo=" + tradeNo + ", " : "") + "tradeType=" + tradeType + ", "
                + (beforeTradeDate != null ? "beforeTradeDate=" + beforeTradeDate + ", " : "")
                + (beforeTradeNo != null ? "beforeTradeNo=" + beforeTradeNo + ", " : "") + "couponAmount="
                + couponAmount + ", additionalMile=" + additionalMile + ", "
                + (useCouponList != null ? "useCouponList=" + useCouponList + ", " : "") + "mybag=" + mybag + "]";
    }

}
