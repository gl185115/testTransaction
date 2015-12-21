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

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

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
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("CouponIssueCount: ").append(this.couponIssueCount).append(crlf)
           .append("CouponPromotionCode: ").append(this.couponPromotionCode).append(crlf)
           .append("CouponFlag: ").append(this.couponFlag);
        return str.toString();
    }
}
