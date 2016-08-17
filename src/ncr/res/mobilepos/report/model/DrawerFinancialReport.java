/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * FinancialReport
 *
 * Model for the Financial Report
 *
 * cm185093
 */
package ncr.res.mobilepos.report.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.helper.ReceiptFormatter;
import ncr.res.mobilepos.model.ResultBase;

/**
 * DrawerFinancialReport Class is a Model representation of the FinancialReport.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DrawerFinancialReport")
@ApiModel(value="DrawerFinancialReport")
public class DrawerFinancialReport extends ResultBase {

    /** The printer id. */
    @XmlElement(name = "PrinterId")
    private String printerID;

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

    /** The financial report data. */
    @XmlElement(name = "financialRptData")
    private String financialRptData;

    /** The storeid. */
    @XmlElement(name = "StoreNo")
    private String storeid;

    /** The store name. */
    @XmlElement(name = "StoreName")
    private String storeName;

    /**
     * FinancialReport constructor.
     */
    public DrawerFinancialReport() {
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
    @ApiModelProperty(value="プリンターID", notes="プリンターID")
    public final String getPrinterID() {
        return printerID;
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
    @ApiModelProperty(value="雑多", notes="雑多")
    public final BigDecimal getMiscellaneous() {
        return miscellaneous;
    }

    /**
     * Set device number.
     *
     * @param printerIDToSet
     *            the new printer id
     */
    public final void setPrinterID(final String printerIDToSet) {
        this.printerID = printerIDToSet;
    }

    /**
     * Get Business Day Date.
     *
     * @return String Business Day Date
     */
    @ApiModelProperty(value="営業日付", notes="営業日付")
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
    @ApiModelProperty(value="販売収益", notes="販売収益")
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
    @ApiModelProperty(value="ディスカウントセール", notes="ディスカウントセール")
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
    @ApiModelProperty(value="返品売価", notes="返品売価")
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
    @ApiModelProperty(value="取消売価", notes="取消売価")
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
    @ApiModelProperty(value="現金", notes="現金")
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
    @ApiModelProperty(value="クレジットカード", notes="クレジットカード")
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
    @ApiModelProperty(value="商品売上", notes="商品売上")
    public final BigDecimal getTotalSale() {
        return cash.add(creditCard).add(miscellaneous);
    }

    /**
     * Get Net Revenue.
     *
     * @return BigDecimal Net Revenue
     */
    @ApiModelProperty(value="純収益", notes="純収益")
    public final BigDecimal getNetRevenue() {
        return grossSalesRevenue.subtract(discountSale).subtract(returnSale)
                .subtract(voidSale);
    }

    /**
     * Set data on financial report.
     *
     * @param financialRptDataToSet
     *            the new financial rpt data
     */
    public final void setFinancialRptData(final String financialRptDataToSet) {
        this.financialRptData = financialRptDataToSet;
    }

    /**
     * Get data on financial report.
     *
     * @return String data of financial report
     */
    @ApiModelProperty(value="金融レポートデータ", notes="金融レポートデータ")
    public final String getFinancialRptData() {
        return financialRptData;
    }

    /**
     * Gets the storeid.
     *
     * @return storeid
     */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
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
    @ApiModelProperty(value="店舗名", notes="店舗名")
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

    /**
     * Get Html Format of financial data.
     *
     * @return String Html format of financial data
     */
    public final String toHTML() {
        String reportData = this.getFinancialRptData();
        if (reportData == null) {
            return "";
        }
        Document doc = Jsoup.parse(reportData);
        Element temp = doc.getElementById("deviceNo");
        temp.text(this.printerID);
        temp = doc.getElementById("businessDate");
        temp.text(this.businessDate.replaceAll("-", "/"));
        temp = doc.getElementById("grossSalesRevenue");
        temp.text(ReceiptFormatter.getNumberWithComma(this.grossSalesRevenue
                .longValue()));
        temp = doc.getElementById("discount");
        temp.text("-"
                + ReceiptFormatter.getNumberWithComma(this.discountSale
                        .longValue()));
        temp = doc.getElementById("return");
        temp.text("-"
                + ReceiptFormatter.getNumberWithComma(this.returnSale
                        .longValue()));
        temp = doc.getElementById("void");
        temp.text("-"
                + ReceiptFormatter.getNumberWithComma(this.voidSale
                        .longValue()));
        temp = doc.getElementById("netRevenue");
        temp.text(ReceiptFormatter.getNumberWithComma(this.getNetRevenue()
                .longValue()));
        temp = doc.getElementById("cash");
        temp.text(ReceiptFormatter.getNumberWithComma(this.cash.longValue()));
        temp = doc.getElementById("creditCard");
        temp.text(ReceiptFormatter.getNumberWithComma(this.creditCard
                .longValue()));
        temp = doc.getElementById("total");
        temp.text(ReceiptFormatter.getNumberWithComma(this.getTotalSale()
                .longValue()));
        return doc.getElementsByTag("table").toString();
    }

}
