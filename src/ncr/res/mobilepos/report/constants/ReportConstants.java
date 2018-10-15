/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ReportConstants
 *
 * Contains all constants used in Report Generation
 *
 * Meneses, Chris Niven
 */
package ncr.res.mobilepos.report.constants;
/**
 * 改定履歴
 * バージョン      改定日付      担当者名        改定内容
 * 1.01            2014.10.21    FENGSHA         レポート出力を対応
 * 1.02            2014.11.28    FENGSHA         売上表を対応
 * 1.03            2014.12.25    MAJINHUI        会計レポート出力を対応
 * 1.04            2015.1.30     MAJINHUI        点検・精算レポート出力変更を対応
 * 1.05            2015.2.13     MAJINHUI        レポート出力変更を対応
 */

/**
 * ReportsConstants is a class that enumerates all fixed values used in the
 * Report Generation Web Service.
 */
public final class ReportConstants {

	// 1.04 2015.1.30 MAJINHUI 点検・精算レポート出力変更を対応 add start
    public static final String CASH_TOTAL = "cashTotal";
    // cash in drawer 買物券
    public static final String CASH_COMMONVOUCHER = "91";
    // cash in drawer  ギフト券
    public static final String CASH_COMMONGIFT = "92";
    // cash in drawer  伊勢丹
    public static final String CASH_ISETANGIFT = "93";
    // cash in drawer  DC
    public static final String CASH_DC = "01";
    // cash in drawer  AMEX
    public static final String CASH_AMEX= "02";
    // cash in drawer  JCB
    public static final String CASH_JCB = "03";
    // cash in drawer  Diners
    public static final String CASH_DINERS = "04";
    // cash in drawer Iカード
    public static final String CASH_ICARD = "05";
    // cash in drawer  三井
    public static final String CASH_MITSUI = "06";
    // cash in drawer  軽井沢
    public static final String CASH_PRINCE = "08";
    // cash in drawer  Chel 1000
    public static final String CASH_CHEL1000 = "10";
    // cash in drawer  Chel 2000
    public static final String CASH_CHEL2000 = "09";
    // その他
    public static final String SONOTA = "Other";
    //実在高
    public static final String  CASH_CALCULATETOTAL = "CalculateTotal";
    //計算在高
    public static final String  CASH_REALTOTAL = "RealTotal";
    //差額
    public static final String  CASH_GAP = "Gap";
    // 1.04 2015.1.30 MAJINHUI 点検・精算レポート出力変更を対応 add end
    
    // 1.03 2014.12.25 majinhui 会計レポート出力を対応　add start
    // 売上
    public static final String ATYREPORT_SALES = "Sales";
    // 売上値引
    public static final String ATYREPORT_SALESDISCOUNT = "SalesDiscount";
    // 取消
    public static final String ATYREPORT_CANCEL = "Cancel";
    // 小計
    public static final String ATYREPORT_SALESSUBTOTAL = "SalesSubTotal";

    // 返品売上
    public static final String ATYREPORT_RETURNSALES = "ReturnSales";
    // 返品値引
    public static final String ATYREPORT_RETURNDISCOUNT = "ReturnDiscount";
    // 返品取消
    public static final String ATYREPORT_RETURNCANCEL = "ReturnCancel";
    // 小計
    public static final String ATYREPORT_RETURNSUBTOTAL = "ReturnSubTotal";

    // 純売上
    public static final String ATYREPORT_NETSALES = "NetSales";
    // 純売上(税抜)
    public static final String ATYREPORT_TAXNETSALES = "TaxNetSales";
    // 総売上
    public static final String ATYREPORT_TOTALSALES = "TotalSales";
    // 総売上(税抜)
    public static final String ATYREPORT_TAXTOTALSALES = "TaxTotalSales";

    // 客数
    public static final String ATYREPORT_CUSTOMERS = "Customers";

    // ＜税別内訳＞
    // 課税(税抜)
    public static final String ATYREPORT_TAXATION = "Taxation";
    // 税
    public static final String ATYREPORT_TAX = "Tax";
    // 非課税
    public static final String ATYREPORT_TAXEXEMPTION = "TaxExemption";
    // 計
    public static final String ATYREPORT_AMOUNTSUBTOAL = "AmountSubtoal";

    // ＜値引内訳＞
    // 明細割引
    public static final String ATYREPORT_ITEMDISCOUNTS = "ItemDiscounts";
    // 明細値引
    public static final String ATYREPORT_ITEMNEBIKI = "ItemNebiki";
    // イベント
    public static final String ATYREPORT_EVENTSNAME = "EventsName";
    // 社員販売
    public static final String ATYREPORT_EMPLOYEESALES = "EmployeeSales";
    // 小計割引
    public static final String ATYREPORT_SUBTOTALDISCOUNTS = "SubtotalDiscounts";
    // 小計値引
    public static final String ATYREPORT_SUBTOTANEBIKI = "SubtotaNebiki";

    // 計
    public static final String ATYREPORT_DICOUNTSSUBTOTAL = "DicountsSubtotal";

    // ＜ギフトカード販売>
    // 販売
    public static final String ATYREPORT_SELL = "Sell";
    // 販売取消
    public static final String ATYREPORT_SELLCANCEL = "SellCancel";

    // 計
    public static final String ATYREPORT_SELLSUBTOTAL = "SellSubtotal";

    // ＜前受金＞
    // 前受金
    public static final String ATYREPORT_ADVANCES = "Advances";
    // 前受金取消
    public static final String ATYREPORT_ADVANCESCANCEL = "AdvancesCancel";

    // 計
    public static final String ATYREPORT_ADVANCESSUBTOTAL = "AdvancesSubtotal";

    // ＜金種別内訳＞
    // 現金
    public static final String ATYREPORT_CASH = "Cash";
    // クレジット
    public static final String ATYREPORT_CREDIT = "Credit";
    // 銀聯
    public static final String ATYREPORT_UNIONPAY = "UnionPay";
    // ギフトカード
    public static final String ATYREPORT_GIFTCARD = "GiftCard";
    // 商品券
    public static final String ATYREPORT_GIFTCERTIFICATES = "GiftCertificates";
    // 振込入金
    public static final String ATYREPORT_TRANSFERPAYMENT = "TransferPayment";
    //1.05     2015.2.13     MAJINHUI        レポート出力変更を対応 add start
    // 前受金売上
    public static final String ATYREPORT_ADVANCESSALES="AdvancesSales";
    // 1.05    2015.2.13     MAJINHUI        レポート出力変更を対応 add end
    // 計
    public static final String ATYREPORT_GOLDSPECIESSUBTOTAL = "GoldSpeciesSubtotal";

    // ＜商品券内訳＞
    // 買物券
    public static final String ATYREPORT_SHOPPINGTICKET = "CommonVoucher";
    // ギフト券
    public static final String ATYREPORT_GIFTVOUCHER = "CommonGift";
    // 伊勢丹
    public static final String ATYREPORT_ISETAN = "IsetanGift";
    // DC
    public static final String ATYREPORT_DC = "DC";
    // AMEX
    public static final String ATYREPORT_AMEX = "AMEX";
    // JCB
    public static final String ATYREPORT_JCB = "JCB";
    // Diners
    public static final String ATYREPORT_DINERS = "Diners";
    //Iカード
    public static final String ATYREPORT_ICARD = "ICard";

    // 三井
    public static final String ATYREPORT_MITSUI = "Mitsui";
    // 軽井沢
    public static final String ATYREPORT_KARUIZAWA = "Prince";
    // Chel　1000
    public static final String ATYREPORT_CHEL1000= "Chel1000";
    // Chel 2000
    public static final String ATYREPORT_CHEL2000= "Chel2000";
    // その他
    public static final String ATYREPORT_SONOTA = "Sonota";
    // 計
    public static final String ATYREPORT_GIFTCERTIFICATESSUBTOTAL = "GiftCertificatesSubtotal";

    // 回収金
    public static final String ATYREPORT_COLLECTED = "Collected";
    //　払戻金
    public static final String ATYREPORT_MODOKINN = "Modokinn";
    
    // 中止回数
    public static final String ATYREPORT_DISCONTINUATION = "Discontinuation";
    // 両替回数
    public static final String ATYREPORT_EXCHANGE = "Exchange";

    // 印紙200
    public static final String ATYREPORT_STAMP200 = "Stamp200";
    // 印紙400
    public static final String ATYREPORT_STAMP400 = "Stamp400";
    // 印紙600
    public static final String ATYREPORT_STAMP600 = "Stamp600";
    // 印紙1,000
    public static final String ATYREPORT_STAMP1000 = "Stamp1000";
    // 印紙2,000
    public static final String ATYREPORT_STAMP2000 = "Stamp2000";
    // 印紙4,000
    public static final String ATYREPORT_STAMP4000 = "Stamp4000";

    /** String value that identifies Sales Accountancy Report Item */
    public static final String COLITEM = "AccountancyItem";

    // 1.03 2014.12.25 majinhui 会計レポート出力を対応　add end
    //1.03 2014.12.22 FENGSHA  売上表印刷を対応 START
    /**A block record delimiter */
    public static final String SALESREPORTPRINT_SPLITFORBLOCKSTR = "#";

    /**A line of record delimiter */
    public static final String SALESREPORTPRINT_SPLITSEMICOLONSTR = ";";

    /**A small record delimiter */
    public static final String SALESREPORTPRINT_SPLITCOMMASTR = ",";

    /**the result set flag */
    public static final String SALESREPORTPRINT_SETFLAGSTR = "True";

    /**the parameter dataStr correct length  */
    public static final int SALESREPORTPRINT_DATASTRISFOUR = 4;

    /**the parameter dataStr correct length  */
    public static final int SALESREPORTPRINT_DATASTRISTHREE = 3;

    /**the parameter totalQuantityStr correct length  */
    public static final int SALESREPORTPRINT_DATASTRISTWO = 2;
    //1.03 2014.12.22 FENGSHA  売上表印刷を対応 END

    /** Default Constructor. */
    private ReportConstants() {
    }
    /** String value that identifies Sales Report type. */
    public static final String TYPE_SALES = "Sales";

    /** String value that identifies Financial Report type. */
    public static final String TYPE_FINANCIAL = "Financial";

    /** String value that identifies Drawer Financial Report type. */
    public static final String TYPE_DRAWERFINANCIAL = "FinancialByDrawer";

    // 1.01 2014.10.31 FENGSHA  レポート出力を対応 START
    /** String value that identifies Sales Accountancy Report type */
    public static final String SALESTYPE_ACCOUNTANCY = "Accountancy";
    /** String value that identifies Sales Accountancy Report Item name */
    public static final String COLITEMNAME = "AccountancyItemName";
    /** String value that identifies Sales Accountancy Report Account */
    public static final String COLACCOUNT = "AccountancyAccount";
    /** String value that identifies Sales Accountancy Report Amount */
    public static final String COLAMOUNT = "AccountancyAmount";
//    /** String value that identifies Sales Accountancy Report QuFenID */
//    public static final String COLQFID = "AccountancyQFid";
    /** String value that identifies Sales Accountancy Report SalesOrPay */
    public static final String SALESORPAY="SalesOrPay";
    // 1.01 2014.10.21 FENGSHA  レポート出力を対応　 END
    /** String value that identifies Sales Hourly Report type. */
    public static final String SALESTYPE_HOURLY = "Hourly";

    /** String value that identifies Sales Deparmtent Report type. */
    public static final String SALESTYPE_DPT = "Department";

    /** String value that identifies Sales SalesClerk Report type. */
    public static final String SALESTYPE_SALESCLERK = "SalesClerk";

    /** String value that identifies Store type*/
    public static final String ALL_STORE = "0000";

    /**String value that identifies all store*/
    public static final String ALL_STORENAME = "全店";

    /** String value that identifies Store type*/
    public static final String FLAGSHIP_STORE = "0001";

    /**String value that identifies flagship store*/
    public static final String FLAGSHIP_STORENAME = "旗艦店";

    /** String value that identifies Store type*/
    public static final String OUTLET_STORE = "0002";

    /**String value that identifies outlet store*/
    public static final String OUTLET_STORENAME = "催事・アウトレット";

    /**String value that identifies all department name*/
    public static final String ALL_DEPARTMENTNAME = "全部門";

    //1.02 2014.11.28 FENGSHA 売上表を対応 ADD START
    /** String value that identifies SalesMan report type */
    public static final String SALESTYPE_SALESMAN = "SalesMan";

    /** String value that identifies Sales Group report type */
    public static final String SALESTYPE_GROUP = "group";

    /** String value that identifies Clerk Product report type*/
    public static final String SALESTYPE_CLERKPRODUCT = "ClerkProduct";

    /** String value that identifies Product */
    public static final String ROW_PRODUCT = "MdNameLocal";
    
    /** String value that identifies Product Code */
    public static final String ROW_PRODUCTCODE = "MdInternal";
    
    /** String value that identifies Product item */
    public static final String ROW_ITEM = "SalesItemCnt";

    /** String value that identifies Product sales*/
    public static final String ROW_SALES = "SalesSalesAmt";

      /** String value that identifies Sales Store Report type. */
    public static final String SALESTYPE_STORE = "Store";

    /** Row name of Store. */
    public static final String ROW_STORE = "StoreId";

    /** Row name of Store Name. */
    public static final String ROW_STORENAME = "StoreName";

    /** String value that identifies Sales Item Report type. */
    public static final String SALESTYPE_ITEM = "Item";

    /** Row name of Item. */
    public static final String ROW_ITEMCODE = "Class";

    /** Row name of Item Name. */
    public static final String ROW_ITEMNAME = "ClassNameLocal";

    /** String value that identifies Sales Department and Hourly Report type. */
    public static final String SALESTYPE_DPT_HOURLY = "Department_Hourly";

    /** String value that identifies Sales TargetMarket Report type. */
    public static final String SALESTYPE_TGT_MARKET = "TargetMarket";

    /** Row name of guest code. */
    public static final String ROW_GUESTCODE = "GuestCode";

    /** Row name of guest zone name. */
    public static final String ROW_GUESTZONENAME = "GuestZoneName";

    //1.02 2014.11.28 FENGSHA 売上表を対応 ADD END
    
    /** String value that identifies Sales Detail Report type. */
    public static final String SALESTYPE_DETAIL = "Detail";

    /** Status Flag for Success Report Generation. */
    public static final int STATUS_SUCCESS = 0;

    /** Status Flag for Fail Report Generation. */
    public static final int STATUS_FAIL = 1;

    /** Status Flag when invalid reportParam XML is passed. */
    public static final int STATUS_XMLINVALID = 2;

    /** Status Flag when invalid Report Type is passed. */
    public static final int STATUS_TYPEINVALID = 3;

    /** Status Flag when invalid Sales Report Type is passed. */
    public static final int STATUS_SALESTYPEINVALID = 4;

    /** Wild card character for SQL statements. */
    public static final String SQL_WILDCARD = "*";

    /** Column Header for DPT number. */
    public static final String COLHEADER_NUMBER = "#";

    /** Column Header for Name. */
    public static final String COLHEADER_NAME = "Name";

    /** Column Header for Customers. */
    public static final String COLHEADER_CUSTOMERS = "Customers";

    /** Column Header for Items. */
    public static final String COLHEADER_ITEMS = "Items";

    /** Column Header for Amount. */
    public static final String COLHEADER_AMOUNT = "Amount";

    /** Column Header for Amount. */
    public static final String COLHEADER_TIMEZONE = "Timezone";

    /** Row name of DPT. */
    public static final String ROW_DPT = "Dpt";

    /** Row name of Sales Item Count. */
    public static final String ROW_SALESITEMCNT = "SalesItemcnt";

    /** Row name of Sales Sales Amount. */
    public static final String ROW_SALESSALESAMOUNT = "SalesSalesAmt";

    /** Row name of Guest Count. */
    public static final String ROW_SALESGUESTCNT = "SalesGuestCnt";

    /** Row name of Void Guest Count. */
    public static final String ROW_VOIDGUESTCNT = "VoidGuestCnt";

    /** Row name of Sales Item Count. */
    public static final String ROW_ITEMCNT = "ItemCnt";

    /** Row name of Sales Sales Amount. */
    public static final String ROW_SALESAMOUNT = "SalesAmt";

    /** Row name of Guest Count. */
    public static final String ROW_GUESTCNT = "GuestCnt";

    /** Row name of time zone code. */
    public static final String ROW_TIMEZONECODE = "timezonecode";

    /** Row name of Guest Count. */
    public static final String ROW_DPTNAME = "DptName";

    /** Row name of Sales Clerk Number. */
    public static final String ROW_OPERATORNUMBER = "OperatorCode";

    /** Row name of Sales Clerk Name. */
    public static final String ROW_OPERATORNAME = "OperatorName";

    //1.02 2014.11.28 FENGSHA 売上表を対応 ADD START
    /** Row name of Sales Person Id. */
    public static final String ROW_SALESPERSONID = "SalesPersonId";

    /** Row name of Sales Group ID */
    public static final String GROUPID = "groupId";

    /** Row name of Sales Group Name */
    public static final String GROUPNAME = "groupName";
    //1.02 2014.11.28 FENGSHA 売上表を対応 ADD END


    /** Report Header name for device number. */
    public static final String REPORTHEADER_DEVNUMBER = "Device No";

    /** Report Header name for operator number. */
    public static final String REPORTHEADER_OPNUMBER = "Operator No";

    /** Report Header name for business date. */
    public static final String REPORTHEADER_BUSINESSDATE = "Business Date";

    /** Report Header name for store number. */
    public static final String REPORTHEADER_STORENUMBER = "Store No";

    /** Report Header name for Department. */
    public static final String REPORTHEADER_DEPARTMENT = "Department";

    /** Report Header Left Position. */
    public static final int REPORTHEADERPOS_LEFT = 1;

    /** Report Header Right Position. */
    public static final int REPORTHEADERPOS_RIGHT = 2;

    /** Message when invalid business date. */
    public static final String INVALID_BUSINESSDATE = "INVBIZDAY";

    /**
     * Characters added to retrieved timeZone to properly represent Time Zone.
     */
    public static final String TIMEZONE_OCLOCK = ":00";

    /**
     * SQL Satement added when operator number is not null in Detail report
     * query string.
     */
    public static final String SQL_OPNUMEQ = " AND act.Operatorcode = ?";

    /**
     * SQL Statement adden when device number is not null in Detail report query
     * string.
     */
    public static final String SQL_DEVNUMEQ = " AND act.deviceno = ?";

    /** The sql selectdetailreport. */
    public static final String SQL_SELECTDETAILREPORT = "select-detailreport";

    /** The parameter 0. */
    public static final int PARAM0 = 0;

    /** The parameter 1. */
    public static final int PARAM1 = 1;

    /** The parameter 2. */
    public static final int PARAM2 = 2;

    /** The parameter 3. */
    public static final int PARAM3 = 3;

    /** The parameter 4. */
    public static final int PARAM4 = 4;

    /** The parameter 5. */
    public static final int PARAM5 = 5;


    /** The parameter 10. */
    public static final int PARAM10 = 10;

    /** The parameter 12. */
    public static final int PARAM12 = 12;

    /** The parameter 13. */
    public static final int PARAM13 = 13;

    /** The parameter 14. */
    public static final int PARAM14 = 14;

    /** The parameter 16. */
    public static final int PARAM16 = 16;

    /** The parameter 40. */
    public static final int PARAM40 = 40;

    /** The parameter 0x0A. */
    public static final int PARAM0A = 0x0A;

    /** The x coordinate. */
    public static final double X_COORDINATE = 5.5;

    /** The y coordinate. */
    public static final double Y_COORDINATE = 55;

    /** The width. */
    public static final double DEFAULT_WIDTH = 164;

    /** The height. */
    public static final double DEFAULT_HEIGHT = 1390;

    /** The twelve hour. */
    public static final int TWELVE_HOUR = 12;

    /** The last digit in a minute hh:mm. */
    public static final int MINUTE_SECOND_INDEX = 4;

    /** The time separator index hh:mm. */
    public static final int HOUR_SEPARATOR_INDEX = 2;
}
