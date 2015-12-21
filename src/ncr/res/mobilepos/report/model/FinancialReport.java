/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * FinancialReport
 *
 * Model for the Financial Report
 *
 * jd185128
 */
package ncr.res.mobilepos.report.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * FinancialReport Class is a Model representation of the Financial Report.
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "FinancialReport")
public class FinancialReport extends ResultBase {

    /** The device no. */
    @XmlElement(name = "DeviceNo")
    private String deviceNo;

    /** The business date. */
    @XmlElement(name = "BusinessDate")
    private String businessDate;

    /** The gross sales revenue. */
    @XmlElement(name = "GrossSalesRevenue")
    private BigDecimal grossSalesRevenue;

    /** The discount sale. */
    @XmlElement(name = "DiscountSale")
    private BigDecimal discountSale;

    /** The return sale. */
    @XmlElement(name = "ReturnSale")
    private BigDecimal returnSale;

    /** The void sale. */
    @XmlElement(name = "VoidSale")
    private BigDecimal voidSale;

    /** The cash. */
    @XmlElement(name = "Cash")
    private BigDecimal cash;

    /** The credit card. */
    @XmlElement(name = "CreditCard")
    private BigDecimal creditCard;

    /** The miscellaneous. */
    @XmlElement(name = "Miscellaneous")
    private BigDecimal miscellaneous;

    /** The storeid. */
    @XmlElement(name = "StoreNo")
    private String storeid;

    /** The store name. */
    @XmlElement(name = "StoreName")
    private String storeName;

    /**
     * FinancialReport constructor.
     */
    public FinancialReport() {
        super();
        BigDecimal initValue = new BigDecimal("0");
        setGrossSalesRevenue(initValue);
        setDiscountSale(initValue);
        setReturnSale(initValue);
        setVoidSale(initValue);
        setCash(initValue);
        setCreditCard(initValue);
        setMiscellaneous(initValue);
    }

    /**
     * Get device number.
     *
     * @return String Device Number
     */
    public final String getDeviceNo() {
        return deviceNo;
    }

    /**
     * Set miscellaneous.
     *
     * @param miscellaneousToSet
     *            the new miscellaneous
     */
    public final void setMiscellaneous(final BigDecimal miscellaneousToSet) {
        this.miscellaneous = miscellaneousToSet;
    }

    /**
     * Get device number.
     *
     * @return String Device Number
     */
    public final BigDecimal getMiscellaneous() {
        return miscellaneous;
    }

    /**
     * Set device number.
     *
     * @param deviceNoToSet
     *            the new device no
     */
    public final void setDeviceNo(final String deviceNoToSet) {
        this.deviceNo = deviceNoToSet;
    }

    /**
     * Get Business Day Date.
     *
     * @return String Business Day Date
     */
    public final String getBusinessDate() {
        return businessDate;
    }

    /**
     * * Set Business Day Date.
     *
     * @param businessDateToSet
     *            the new business date
     */
    public final void setBusinessDate(final String businessDateToSet) {
        this.businessDate = businessDateToSet;
    }

    /**
     * * Get Gross Sales Revenue.
     *
     * @return BigDecimal Gross Sales Revenue
     */
    public final BigDecimal getGrossSalesRevenue() {
        return grossSalesRevenue;
    }

    /**
     * * Set Gross Sales Revenue.
     *
     * @param grossSalesRevenueToSet
     *            the new gross sales revenue
     */
    public final void setGrossSalesRevenue(
            final BigDecimal grossSalesRevenueToSet) {
        this.grossSalesRevenue = grossSalesRevenueToSet;
    }

    /**
     * * Get Discount.
     *
     * @return BigDecimal Discount
     */
    public final BigDecimal getDiscountSale() {
        return discountSale;
    }

    /**
     * Set Discount.
     *
     * @param discountSaleToSet
     *            the new discount sale
     */
    public final void setDiscountSale(final BigDecimal discountSaleToSet) {
        if (null == this.discountSale) {
            this.discountSale = discountSaleToSet;
        } else {
            this.discountSale = this.discountSale.add(discountSaleToSet);
        }
    }

    /**
     * Get Return.
     *
     * @return BigDecimal Return
     */
    public final BigDecimal getReturnSale() {
        return returnSale;
    }

    /**
     * Set Return.
     *
     * @param returnSaleToSet
     *            the new return sale
     */
    public final void setReturnSale(final BigDecimal returnSaleToSet) {
        this.returnSale = returnSaleToSet;
    }

    /**
     * Get Void.
     *
     * @return BigDecimal Void
     */
    public final BigDecimal getVoidSale() {
        return voidSale;
    }

    /**
     * Set Void.
     *
     * @param voidSaleToSet
     *            the new void sale
     */
    public final void setVoidSale(final BigDecimal voidSaleToSet) {
        this.voidSale = voidSaleToSet;
    }

    /**
     * Get Total Amt of Cash.
     *
     * @return BigDecimal Cash
     */
    public final BigDecimal getCash() {
        return cash;
    }

    /**
     * Set Total Amt of Cash.
     *
     * @param cashToSet
     *            the new cash
     */
    public final void setCash(final BigDecimal cashToSet) {
        this.cash = cashToSet;
    }

    /**
     * Get Total Amt. of CreditCard
     *
     * @return BigDecimal CreditCard
     */
    public final BigDecimal getCreditCard() {
        return creditCard;
    }

    /**
     * Set Total Amt of CreditCard.
     *
     * @param creditCardToSet
     *            the new credit card
     */
    public final void setCreditCard(final BigDecimal creditCardToSet) {
        this.creditCard = creditCardToSet;
    }

    /**
     * Get TotalSales.
     *
     * @return BigDecimal TotalSale
     */
    public final BigDecimal getTotalSale() {
        return cash.add(creditCard);
    }

    /**
     * Get Net Revenue.
     *
     * @return BigDecimal Net Revenue
     */
    public final BigDecimal getNetRevenue() {
        return grossSalesRevenue.subtract(discountSale).subtract(returnSale)
                .subtract(voidSale);
    }

    /**
     * Gets the storeid.
     *
     * @return storeid
     */
    public final String getStoreid() {
        return storeid;
    }

    /**
     * Sets the storeid.
     *
     * @param storeidToSet
     *            the new storeid
     */
    public final void setStoreid(final String storeidToSet) {
        this.storeid = storeidToSet;
    }

    /**
     * Gets the store name.
     *
     * @return storeName
     */
    public final String getStoreName() {
        return storeName;
    }

    /**
     * Sets the store name.
     *
     * @param storeNameToSet
     *            the new store name
     */
    public final void setStoreName(final String storeNameToSet) {
        this.storeName = storeNameToSet;
    }

}
