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
 * ���藚��
 * �o�[�W����      ������t      �S���Җ�        ������e
 * 1.01            2014.10.21    FENGSHA         ���|�[�g�o�͂�Ή�
 * 1.02            2014.11.28    FENGSHA         ����\��Ή�
 * 1.03            2014.12.25    MAJINHUI        ��v���|�[�g�o�͂�Ή�
 * 1.04            2015.1.30     MAJINHUI        �_���E���Z���|�[�g�o�͕ύX��Ή�
 * 1.05            2015.2.13     MAJINHUI        ���|�[�g�o�͕ύX��Ή�
 */

/**
 * ReportsConstants is a class that enumerates all fixed values used in the
 * Report Generation Web Service.
 */
public final class ReportConstants {

	// 1.04 2015.1.30 MAJINHUI �_���E���Z���|�[�g�o�͕ύX��Ή� add start
    public static final String CASH_TOTAL = "cashTotal";
    // cash in drawer ������
    public static final String CASH_COMMONVOUCHER = "91";
    // cash in drawer  �M�t�g��
    public static final String CASH_COMMONGIFT = "92";
    // cash in drawer  �ɐ��O
    public static final String CASH_ISETANGIFT = "93";
    // cash in drawer  DC
    public static final String CASH_DC = "01";
    // cash in drawer  AMEX
    public static final String CASH_AMEX= "02";
    // cash in drawer  JCB
    public static final String CASH_JCB = "03";
    // cash in drawer  Diners
    public static final String CASH_DINERS = "04";
    // cash in drawer I�J�[�h
    public static final String CASH_ICARD = "05";
    // cash in drawer  �O��
    public static final String CASH_MITSUI = "06";
    // cash in drawer  �y���
    public static final String CASH_PRINCE = "08";
    // cash in drawer  Chel 1000
    public static final String CASH_CHEL1000 = "10";
    // cash in drawer  Chel 2000
    public static final String CASH_CHEL2000 = "09";
    // ���̑�
    public static final String SONOTA = "Other";
    //���ݍ�
    public static final String  CASH_CALCULATETOTAL = "CalculateTotal";
    //�v�Z�ݍ�
    public static final String  CASH_REALTOTAL = "RealTotal";
    //���z
    public static final String  CASH_GAP = "Gap";
    // 1.04 2015.1.30 MAJINHUI �_���E���Z���|�[�g�o�͕ύX��Ή� add end
    
    // 1.03 2014.12.25 majinhui ��v���|�[�g�o�͂�Ή��@add start
    // ����
    public static final String ATYREPORT_SALES = "Sales";
    // ����l��
    public static final String ATYREPORT_SALESDISCOUNT = "SalesDiscount";
    // ���
    public static final String ATYREPORT_CANCEL = "Cancel";
    // ���v
    public static final String ATYREPORT_SALESSUBTOTAL = "SalesSubTotal";

    // �ԕi����
    public static final String ATYREPORT_RETURNSALES = "ReturnSales";
    // �ԕi�l��
    public static final String ATYREPORT_RETURNDISCOUNT = "ReturnDiscount";
    // �ԕi���
    public static final String ATYREPORT_RETURNCANCEL = "ReturnCancel";
    // ���v
    public static final String ATYREPORT_RETURNSUBTOTAL = "ReturnSubTotal";

    // ������
    public static final String ATYREPORT_NETSALES = "NetSales";
    // ������(�Ŕ�)
    public static final String ATYREPORT_TAXNETSALES = "TaxNetSales";
    // ������
    public static final String ATYREPORT_TOTALSALES = "TotalSales";
    // ������(�Ŕ�)
    public static final String ATYREPORT_TAXTOTALSALES = "TaxTotalSales";

    // �q��
    public static final String ATYREPORT_CUSTOMERS = "Customers";

    // ���ŕʓ���
    // �ې�(�Ŕ�)
    public static final String ATYREPORT_TAXATION = "Taxation";
    // ��
    public static final String ATYREPORT_TAX = "Tax";
    // ��ې�
    public static final String ATYREPORT_TAXEXEMPTION = "TaxExemption";
    // �v
    public static final String ATYREPORT_AMOUNTSUBTOAL = "AmountSubtoal";

    // ���l������
    // ���׊���
    public static final String ATYREPORT_ITEMDISCOUNTS = "ItemDiscounts";
    // ���גl��
    public static final String ATYREPORT_ITEMNEBIKI = "ItemNebiki";
    // �C�x���g
    public static final String ATYREPORT_EVENTSNAME = "EventsName";
    // �Ј��̔�
    public static final String ATYREPORT_EMPLOYEESALES = "EmployeeSales";
    // ���v����
    public static final String ATYREPORT_SUBTOTALDISCOUNTS = "SubtotalDiscounts";
    // ���v�l��
    public static final String ATYREPORT_SUBTOTANEBIKI = "SubtotaNebiki";

    // �v
    public static final String ATYREPORT_DICOUNTSSUBTOTAL = "DicountsSubtotal";

    // ���M�t�g�J�[�h�̔�>
    // �̔�
    public static final String ATYREPORT_SELL = "Sell";
    // �̔����
    public static final String ATYREPORT_SELLCANCEL = "SellCancel";

    // �v
    public static final String ATYREPORT_SELLSUBTOTAL = "SellSubtotal";

    // ���O�����
    // �O���
    public static final String ATYREPORT_ADVANCES = "Advances";
    // �O������
    public static final String ATYREPORT_ADVANCESCANCEL = "AdvancesCancel";

    // �v
    public static final String ATYREPORT_ADVANCESSUBTOTAL = "AdvancesSubtotal";

    // ������ʓ���
    // ����
    public static final String ATYREPORT_CASH = "Cash";
    // �N���W�b�g
    public static final String ATYREPORT_CREDIT = "Credit";
    // ���
    public static final String ATYREPORT_UNIONPAY = "UnionPay";
    // �M�t�g�J�[�h
    public static final String ATYREPORT_GIFTCARD = "GiftCard";
    // ���i��
    public static final String ATYREPORT_GIFTCERTIFICATES = "GiftCertificates";
    // �U������
    public static final String ATYREPORT_TRANSFERPAYMENT = "TransferPayment";
    //1.05     2015.2.13     MAJINHUI        ���|�[�g�o�͕ύX��Ή� add start
    // �O�������
    public static final String ATYREPORT_ADVANCESSALES="AdvancesSales";
    // 1.05    2015.2.13     MAJINHUI        ���|�[�g�o�͕ύX��Ή� add end
    // �v
    public static final String ATYREPORT_GOLDSPECIESSUBTOTAL = "GoldSpeciesSubtotal";

    // �����i������
    // ������
    public static final String ATYREPORT_SHOPPINGTICKET = "CommonVoucher";
    // �M�t�g��
    public static final String ATYREPORT_GIFTVOUCHER = "CommonGift";
    // �ɐ��O
    public static final String ATYREPORT_ISETAN = "IsetanGift";
    // DC
    public static final String ATYREPORT_DC = "DC";
    // AMEX
    public static final String ATYREPORT_AMEX = "AMEX";
    // JCB
    public static final String ATYREPORT_JCB = "JCB";
    // Diners
    public static final String ATYREPORT_DINERS = "Diners";
    //I�J�[�h
    public static final String ATYREPORT_ICARD = "ICard";

    // �O��
    public static final String ATYREPORT_MITSUI = "Mitsui";
    // �y���
    public static final String ATYREPORT_KARUIZAWA = "Prince";
    // Chel�@1000
    public static final String ATYREPORT_CHEL1000= "Chel1000";
    // Chel 2000
    public static final String ATYREPORT_CHEL2000= "Chel2000";
    // ���̑�
    public static final String ATYREPORT_SONOTA = "Sonota";
    // �v
    public static final String ATYREPORT_GIFTCERTIFICATESSUBTOTAL = "GiftCertificatesSubtotal";

    // �����
    public static final String ATYREPORT_COLLECTED = "Collected";
    //�@���ߋ�
    public static final String ATYREPORT_MODOKINN = "Modokinn";
    
    // ���~��
    public static final String ATYREPORT_DISCONTINUATION = "Discontinuation";
    // ���։�
    public static final String ATYREPORT_EXCHANGE = "Exchange";

    // ��200
    public static final String ATYREPORT_STAMP200 = "Stamp200";
    // ��400
    public static final String ATYREPORT_STAMP400 = "Stamp400";
    // ��600
    public static final String ATYREPORT_STAMP600 = "Stamp600";
    // ��1,000
    public static final String ATYREPORT_STAMP1000 = "Stamp1000";
    // ��2,000
    public static final String ATYREPORT_STAMP2000 = "Stamp2000";
    // ��4,000
    public static final String ATYREPORT_STAMP4000 = "Stamp4000";

    /** String value that identifies Sales Accountancy Report Item */
    public static final String COLITEM = "AccountancyItem";

    // 1.03 2014.12.25 majinhui ��v���|�[�g�o�͂�Ή��@add end
    //1.03 2014.12.22 FENGSHA  ����\�����Ή� START
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
    //1.03 2014.12.22 FENGSHA  ����\�����Ή� END

    /** Default Constructor. */
    private ReportConstants() {
    }
    /** String value that identifies Sales Report type. */
    public static final String TYPE_SALES = "Sales";

    /** String value that identifies Financial Report type. */
    public static final String TYPE_FINANCIAL = "Financial";

    /** String value that identifies Drawer Financial Report type. */
    public static final String TYPE_DRAWERFINANCIAL = "FinancialByDrawer";

    // 1.01 2014.10.31 FENGSHA  ���|�[�g�o�͂�Ή� START
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
    // 1.01 2014.10.21 FENGSHA  ���|�[�g�o�͂�Ή��@ END
    /** String value that identifies Sales Hourly Report type. */
    public static final String SALESTYPE_HOURLY = "Hourly";

    /** String value that identifies Sales Deparmtent Report type. */
    public static final String SALESTYPE_DPT = "Department";

    /** String value that identifies Sales SalesClerk Report type. */
    public static final String SALESTYPE_SALESCLERK = "SalesClerk";

    /** String value that identifies Store type*/
    public static final String ALL_STORE = "0000";

    /**String value that identifies all store*/
    public static final String ALL_STORENAME = "�S�X";

    /** String value that identifies Store type*/
    public static final String FLAGSHIP_STORE = "0001";

    /**String value that identifies flagship store*/
    public static final String FLAGSHIP_STORENAME = "���͓X";

    /** String value that identifies Store type*/
    public static final String OUTLET_STORE = "0002";

    /**String value that identifies outlet store*/
    public static final String OUTLET_STORENAME = "�Î��E�A�E�g���b�g";

    /**String value that identifies all department name*/
    public static final String ALL_DEPARTMENTNAME = "�S����";

    //1.02 2014.11.28 FENGSHA ����\��Ή� ADD START
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

    //1.02 2014.11.28 FENGSHA ����\��Ή� ADD END
    
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

    //1.02 2014.11.28 FENGSHA ����\��Ή� ADD START
    /** Row name of Sales Person Id. */
    public static final String ROW_SALESPERSONID = "SalesPersonId";

    /** Row name of Sales Group ID */
    public static final String GROUPID = "groupId";

    /** Row name of Sales Group Name */
    public static final String GROUPNAME = "groupName";
    //1.02 2014.11.28 FENGSHA ����\��Ή� ADD END


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
