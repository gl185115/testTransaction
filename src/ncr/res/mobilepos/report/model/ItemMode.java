package ncr.res.mobilepos.report.model;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01            2014.10.21    FENGSHA         レポート出力を対応
 */
@ApiModel(value="ItemMode")
public class ItemMode {

    /** Division設定 flag */
    private String isHeaderFlag;

    /** 時間帯、客数、点数、金額値設定flag */
    private String isBodyFlag;

    /** 客数、点数、金額合計値設定 flag */
    private String isFooterFlag;

    /** column name **/
    private String ItemName;

    /** 客数 **/
    private long GuestCnt;

    /** 点数 **/
    private long ItemCnt;

    /** 金額 **/
    private double SalesAmt;

    /** Division name **/
    private String divisionName;

    /** 客数合計 **/
    private long GuestCntSum;

    /** 点数合計 **/
    private long ItemCntSum;

    /** 金額合計 **/
    private double SalesAmtSum;

    /** column code **/
    private String ItemCode;


    /**
     * @return itemCode
     */
    @ApiModelProperty(value="商品コード", notes="商品コード")
    public String getItemCode() {
        return ItemCode;
    }
    /**
     * @param itemCode セットする itemCode
     */
    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }
    /**
     * @return itemName
     */
    @ApiModelProperty(value="商品名", notes="商品名")
    public String getItemName() {
        return ItemName;
    }
    /**
     * @param itemName セットする itemName
     */
    public void setItemName(String itemName) {
        ItemName = itemName;
    }
    /**
     * @return guestCnt
     */
    @ApiModelProperty(value="来店客数", notes="来店客数")
    public long getGuestCnt() {
        return GuestCnt;
    }
    /**
     * @param guestCnt セットする guestCnt
     */
    public void setGuestCnt(long guestCnt) {
        GuestCnt = guestCnt;
    }
    /**
     * @return itemCnt
     */
    @ApiModelProperty(value="数量", notes="数量")
    public long getItemCnt() {
        return ItemCnt;
    }
    /**
     * @param itemCnt セットする itemCnt
     */
    public void setItemCnt(long itemCnt) {
        ItemCnt = itemCnt;
    }
    /**
     * @return salesAmt
     */
    @ApiModelProperty(value="売上金額", notes="売上金額")
    public double getSalesAmt() {
        return SalesAmt;
    }
    /**
     * @param salesAmt セットする salesAmt
     */
    public void setSalesAmt(double salesAmt) {
        SalesAmt = salesAmt;
    }
    /**
     * @return isHeaderFlag
     */
    @ApiModelProperty(value="ヘッダフラグ", notes="ヘッダフラグ")
    public String getIsHeaderFlag() {
        return isHeaderFlag;
    }
    /**
     * @param isHeaderFlag セットする isHeaderFlag
     */
    public void setIsHeaderFlag(String isHeaderFlag) {
        this.isHeaderFlag = isHeaderFlag;
    }
    /**
     * @return guestCntSum
     */
    @ApiModelProperty(value="客数和", notes="客数和")
    public long getGuestCntSum() {
        return GuestCntSum;
    }
    /**
     * @param guestCntSum セットする guestCntSum
     */
    public void setGuestCntSum(long guestCntSum) {
        GuestCntSum = guestCntSum;
    }
    /**
     * @return itemCntSum
     */
    @ApiModelProperty(value="商品数和", notes="商品数和")
    public long getItemCntSum() {
        return ItemCntSum;
    }
    /**
     * @param itemCntSum セットする itemCntSum
     */
    public void setItemCntSum(long itemCntSum) {
        ItemCntSum = itemCntSum;
    }
    /**
     * @return salesAmtSum
     */
    @ApiModelProperty(value="売価金額和", notes="売価金額和")
    public double getSalesAmtSum() {
        return SalesAmtSum;
    }
    /**
     * @param salesAmtSum セットする salesAmtSum
     */
    public void setSalesAmtSum(double salesAmtSum) {
        SalesAmtSum = salesAmtSum;
    }
    /**
     * @return divisionName
     */
    @ApiModelProperty(value="部門名", notes="部門名")
    public String getDivisionName() {
        return divisionName;
    }
    /**
     * @param divisionName セットする divisionName
     */
    public void setDivisionName(String divisionName) {
        this.divisionName = divisionName;
    }
    /**
     * @return isFooterFlag
     */
    @ApiModelProperty(value="フッタフラグ", notes="フッタフラグ")
    public String getIsFooterFlag() {
        return isFooterFlag;
    }
    /**
     * @param isFooterFlag セットする isFooterFlag
     */
    public void setIsFooterFlag(String isFooterFlag) {
        this.isFooterFlag = isFooterFlag;
    }
    /**
     * @return isBodyFlag
     */
    @ApiModelProperty(value="ボディフラグ", notes="ボディフラグ")
    public String getIsBodyFlag() {
        return isBodyFlag;
    }
    /**
     * @param isBodyFlag セットする isBodyFlag
     */
    public void setIsBodyFlag(String isBodyFlag) {
        this.isBodyFlag = isBodyFlag;
    }
}
