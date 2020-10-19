package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class AccountInfoResponse extends ResultBase {

    @XmlElement(name = "resultCode")
    private int resultCode;

    @XmlElement(name = "mppId")
    private String mppId;

    @XmlElement(name = "couponAmount")
    private int couponAmount;

    @XmlElement(name = "totalMile")
    private int totalMile;

    @XmlElement(name = "nextMileAmount")
    private int nextMileAmount;

    @XmlElement(name = "mileLimit")
    private String mileLimit;

    @XmlElement(name = "couponLimitDate")
    private String couponLimitDate;

    @XmlElement(name = "couponLimitAmount")
    private int couponLimitAmount;

    @XmlElement(name = "selectableCouponList")
    private String selectableCouponList;

    @XmlElement(name = "errorMessage")
    private String errorMessage;

    /**
     * @return the resultCode
     */
    public final int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public final void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the mppId
     */
    public final String getMppId() {
        return mppId;
    }

    /**
     * @param mppId the mppId to set
     */
    public final void setMppId(String mppId) {
        this.mppId = mppId;
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
     * @return the totalMile
     */
    public final int getTotalMile() {
        return totalMile;
    }

    /**
     * @param totalMile the totalMile to set
     */
    public final void setTotalMile(int totalMile) {
        this.totalMile = totalMile;
    }

    /**
     * @return the nextMileAmount
     */
    public final int getNextMileAmount() {
        return nextMileAmount;
    }

    /**
     * @param nextMileAmount the nextMileAmount to set
     */
    public final void setNextMileAmount(int nextMileAmount) {
        this.nextMileAmount = nextMileAmount;
    }

    /**
     * @return the mileLimit
     */
    public final String getMileLimit() {
        return mileLimit;
    }

    /**
     * @param mileLimit the mileLimit to set
     */
    public final void setMileLimit(String mileLimit) {
        this.mileLimit = mileLimit;
    }

    /**
     * @return the couponLimitDate
     */
    public final String getCouponLimitDate() {
        return couponLimitDate;
    }

    /**
     * @param couponLimitDate the couponLimitDate to set
     */
    public final void setCouponLimitDate(String couponLimitDate) {
        this.couponLimitDate = couponLimitDate;
    }

    /**
     * @return the couponLimitAmount
     */
    public final int getCouponLimitAmount() {
        return couponLimitAmount;
    }

    /**
     * @param couponLimitAmount the couponLimitAmount to set
     */
    public final void setCouponLimitAmount(int couponLimitAmount) {
        this.couponLimitAmount = couponLimitAmount;
    }

    /**
     * @return the selectableCouponList
     */
    public final String getSelectableCouponList() {
        return selectableCouponList;
    }

    /**
     * @param selectableCouponList the selectableCouponList to set
     */
    public final void setSelectableCouponList(String selectableCouponList) {
        this.selectableCouponList = selectableCouponList;
    }

    /**
     * @return the errorMessage
     */
    public final String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public final void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "AccountInfoResponse [resultCode=" + resultCode + ", " + (mppId != null ? "mppId=" + mppId + ", " : "")
                + "couponAmount=" + couponAmount + "totalMile=" + totalMile
                + ", nextMileAmount=" + nextMileAmount + ", "
                + (mileLimit != null ? "mileLimit=" + mileLimit + ", " : "")
                + (couponLimitDate != null ? "couponLimitDate=" + couponLimitDate + ", " : "") + "couponLimitAmount="
                + couponLimitAmount + ", "
                + (selectableCouponList != null ? "selectableCouponList=" + selectableCouponList + ", " : "")
                + (errorMessage != null ? "errorMessage=" + errorMessage : "") + "]";
    }

}
