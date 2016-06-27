package ncr.res.mobilepos.report.model;
/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01  2014.11.29   FengSha   レポート出力を対応
 * 1.02  2014.12.30   Majinhui   会計レポート出力を対応
 */

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * The Class ReportItem.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReportItem")
@ApiModel(value="ReportItem")
public class ReportItem {

    /** The itemlabel. */
    private ItemLabel itemlabel = null;

    /** The number of customer. */
    private long customer = 0;

    /** The number of items. */
    private long items = 0;
    // 1.02 2014.12.30 Majinhui 会計レポート出力を対応 ADD START
    /** The number of item name. */
    private String itemName;
    // 1.02 2014.12.30 Majinhui 会計レポート出力を対応 ADD END
    /** The sales revenue. */
    private double salesRevenue = 0;

    /**
     * Instantiates a new report item.
     */
    public ReportItem() {
        this.itemlabel = new ItemLabel();
    }

    //1.01  2014.11.29   FengSha   レポート出力を対応　 START

   private String TimeZoneCode = null;

    @XmlElement(name = "TimeZoneCode")
    @ApiModelProperty(value="タイムゾーンコード", notes="タイムゾーンコード")
    public final String getTimeZoneCode() {
        return TimeZoneCode;
    }

    public final void setTimeZoneCode(final String timeZoneCode) {
        this.TimeZoneCode = timeZoneCode;
    }
    //1.01  2014.11.29   FengSha   レポート出力を対応　 END

    /**
     * Sets the customer.
     *
     * @param customerNum
     *            the new customer
     */
    public final void setCustomer(final long customerNum) {
        this.customer = customerNum;
    }

    /**
     * Sets the items.
     *
     * @param itemNum
     *            the new items
     */
    public final void setItems(final long itemNum) {
        this.items = itemNum;
    }

    // 1.02 2014.12.30 MAJINHUI 会計レポート出力を対応 ADD START
    /**
     * Gets the items.
     *
     * @return the items
     */
    @XmlElement(name = "ItemName")
    @ApiModelProperty(value="項目名", notes="項目名")
    public String getItemName() {
        return itemName;
    }

    /**
     * Sets the item name.
     *
     * @param item
     *            name
     */

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // 1.02 2014.12.30 MAJINHUI 会計レポート出力を対応 ADD END
    /**
     * Gets the sales revenue.
     *
     * @return the sales revenue
     */
    @XmlElement(name = "SalesRevenue")
    @ApiModelProperty(value="販売収益", notes="販売収益")
    public final double getSalesRevenue() {
        return this.salesRevenue;
    }

    /**
     * Gets the customer.
     *
     * @return the customer
     */
    @XmlElement(name = "Customers")
    @ApiModelProperty(value="顧客", notes="顧客")
    public final long getCustomer() {
        return this.customer;
    }

    /**
     * Gets the items.
     *
     * @return the items
     */
    @XmlElement(name = "Items")
    @ApiModelProperty(value="項目", notes="項目")
    public final long getItems() {
        return this.items;
    }

    /**
     * Sets the sales revenue.
     *
     * @param salesRevenueNum
     *            the new sales revenue
     */
    public final void setSalesRevenue(final double salesRevenueNum) {
        this.salesRevenue = salesRevenueNum;
    }

    /**
     * Sets the item label.
     *
     * @param itemLabel
     *            the new item label
     */
    public final void setItemLabel(final ItemLabel itemLabel) {
        this.itemlabel = itemLabel;
    }

    /**
     * Gets the item label.
     *
     * @return the item label
     */
    @XmlElement(name = "ItemLabel")
    @ApiModelProperty(value="項目ラベル", notes="項目ラベル")
    public final ItemLabel getItemLabel() {
        return this.itemlabel;
    }

}
