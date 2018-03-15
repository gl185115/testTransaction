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
 * Coupon Model Object.
 *
 * <P>A Coupon Node in POSLog XML.
 *
 * <P>The Coupon node is under Sale Node.
 * And mainly holds the information of the Coupon
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Coupon")
public class Coupon {
    /**
     * CouponFlag
     */
    @XmlElement(name="CouponFlag")
    private String couponFlag;
    /**
     * CouponPromotionCode
     */
    @XmlElement(name="CouponPromotionCode")
    private String couponPromotionCode;
    /**
     * CouponIssueCount
     */
    @XmlElement(name="CouponIssueCount")
    private int couponIssueCount;
    /**
     * CouponUnitPrice
     */
    @XmlElement(name="CouponUnitPrice")
    private int couponUnitPrice;
    /**
     * CouponIssueType
     */
    @XmlElement(name="CouponIssueType")
    private int couponIssueType;
    /**
     * CouponIssueType
     */
    @XmlElement(name="CouponCount")
    private int couponCount;
    /**
     * CouponReceiptName
     */
    @XmlElement(name="CouponReceiptName")
    private String couponReceiptName;
	/**
     * @return the couponFlag
     */
    public Coupon(){
    }
    public final String getCouponFlag() {
        return couponFlag;
    }
    /**
     * @param couponFlag the couponFlag to set
     */
    public final void setCouponFlag(final String couponFlag) {
        this.couponFlag = couponFlag;
    }
    /**
     * @return the couponPromotionCode
     */
    public final String getCouponPromotionCode() {
        return couponPromotionCode;
    }
    /**
     * @param couponPromotionCode the couponPromotionCode to set
     */
    public final void setCouponPromotionCode(final String couponPromotionCode) {
        this.couponPromotionCode = couponPromotionCode;
    }
    /**
     * @return the couponIssueCount
     */
    public final int getCouponIssueCount() {
        return couponIssueCount;
    }
    /**
     * @param couponIssueCount the couponIssueCount to set
     */
    public final void setCouponIssueCount(final int couponIssueCount) {
        this.couponIssueCount = couponIssueCount;
    }
    /**
     * @return the couponUnitPrice
     */
    public final int getCouponUnitPrice() {
        return couponUnitPrice;
    }
    /**
     * @param couponUnitPrice the couponUnitPrice to set
     */
    public final void setCouponUnitPrice(final int couponUnitPrice) {
        this.couponUnitPrice = couponUnitPrice;
    }
    /**
     * @return the couponIssueType
     */
    public final int getCouponIssueType() {
        return couponIssueType;
    }
    /**
     * @param couponIssueType the couponIssueType to set
     */
    public final void setCouponIssueType(final int couponIssueType) {
        this.couponIssueType = couponIssueType;
    }
    public final int getCouponCount() {
		return couponCount;
	}
	public final void setCouponCount(int couponCount) {
		this.couponCount = couponCount;
	}
	/**
     * @return the couponReceiptName
     */
    public final String getCouponReceiptName() {
        return couponReceiptName;
    }
    /**
     * @param couponReceiptName the couponReceiptName to set
     */
    public final void setCouponReceiptName(final String couponReceiptName) {
        this.couponReceiptName = couponReceiptName;
    }
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("CouponIssueCount: ").append(this.couponIssueCount).append(crlf)
           .append("CouponPromotionCode: ").append(this.couponPromotionCode).append(crlf)
           .append("CouponFlag: ").append(this.couponFlag);
        return str.toString();
    }
}
