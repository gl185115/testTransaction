package ncr.res.mobilepos.pricing.model;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2014.11.19      LIQIAN        商品情報取得
 */


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Item Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Item")
public class Item {

    // rounding options
    public static final int ROUND_UP = 1;
    public static final int ROUND_DOWN = 2;
    public static final int ROUND_OFF = 3;

    // tax options
    public static final int TAX_EXCLUDED = 0;
    public static final int TAX_INCLUDED = 1;
    public static final int TAX_FREE = 2;
    
    @XmlElement(name = "ItemID")
    private String itemId;

    @XmlElement(name = "Department")
    private String department;

    @XmlElement(name = "Description")
    private Description description;

    @XmlElement(name = "RegularSalesUnitPrice")
    private double regularSalesUnitPrice;

    @XmlElement(name = "Discount")
    private double discount;

    @XmlElement(name = "DiscountFlag")
    private int discountFlag = -1;

    @XmlElement(name = "DiscountAmount")
    private double discountAmount;

    @XmlElement(name = "ActualSalesUnitPrice")
    private double actualSalesUnitPrice;

    @XmlElement(name = "Discountable")
    private boolean discountable;

    @XmlElement(name = "TaxRate")
    private int taxRate;

    @XmlElement(name = "TaxType")
    private int taxType;

    @XmlElement(name = "DiscountType")
    private int discountType = 0;
    
    @XmlElement(name = "SubNum1")
    private int subNum1 = 0;

    public int getDptDiscountType() {
        return dptDiscountType;
    }

    public void setDptDiscountType(int dptDiscountType) {
        this.dptDiscountType = dptDiscountType;
    }

    public int getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(int subNum1) {
        this.subNum1 = subNum1;
    }

    @XmlElement(name = "NonSales")
    private int nonSales;

    @XmlElement(name = "SubInt10")
    private int subInt10;

    @XmlElement(name = "Line")
    private String line;

    //---- start
    @XmlElement(name = "CompanyID")
    private String companyId;
    
    @XmlElement(name = "promotionNo")
    private String promotionNo;
    
    @XmlElement(name = "MdType")
    private String mdType = "";
    
    public String getMdType() {
        return mdType;
    }

    public void setMdType(String mdType) {
        this.mdType = mdType;
    }

    @XmlElement(name = "Sku")
    private String sku;
    @XmlElement(name = "Md02")
    private String md02 = "";
    
    @XmlElement(name = "Md03")
    private String md03 = "";
    
    @XmlElement(name = "Md04")
    private String md04 = "";
    
    @XmlElement(name = "Md05")
    private String md05 = "";
    
    @XmlElement(name = "Md06")
    private String md06 = "";
    
    @XmlElement(name = "Md07")
    private String md07 = "";
    
    @XmlElement(name = "Md08")
    private String md08 = "";
    
    @XmlElement(name = "Md09")
    private String md09 = "";
    
    @XmlElement(name = "Md10")
    private String md10 = "";
    
    @XmlElement(name = "Md11")
    private String md11 = "";
    
    @XmlElement(name = "Md12")
    private String md12 = "";
    
    @XmlElement(name = "Md13")
    private String md13 = "";
    
    @XmlElement(name = "Md14")
    private String md14 = "";
    
    @XmlElement(name = "Md15")
    private String md15 = "";
    
    @XmlElement(name = "Md16")
    private String md16 = "";
    
    @XmlElement(name = "MdNameLocal")
    private String mdNameLocal = "";
    
    @XmlElement(name = "MdKanaName")
    private String mdKanaName = "";
    
    @XmlElement(name = "SalesPrice2")
    private double salesPrice2;
    
    @XmlElement(name = "PaymentType")
    private int paymentType = 0;
    
    @XmlElement(name ="SubCode1")
    private String subCode1 = "";
    
    @XmlElement(name ="SubCode2")
    private String subCode2 = "";
    
    @XmlElement(name ="SubCode3")
    private String subCode3 = "";
    
    @XmlElement(name = "SubNum2")
    private int subNum2 = 0;
    
    @XmlElement(name = "PromotionId")
    private String promotionId;
    
    @XmlElement(name = "StoreId")
    private String storeId;
    
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @XmlElement(name = "DptDiscountType")
    private int dptDiscountType = 0;
    
    @XmlElement(name = "DiacountRate")
    private double diacountRate = 0;
    
    @XmlElement(name = "DiscountAmt")
    private int discountAmt = 0;
    
    @XmlElement(name = "ReplaceSupportDiscountAmt")
    private int replaceSupportdiscountAmt = 0;
   // ReplaceSupport
    
    public int getReplaceSupportdiscountAmt() {
        return replaceSupportdiscountAmt;
    }

    public void setReplaceSupportdiscountAmt(int replaceSupportdiscountAmt) {
        this.replaceSupportdiscountAmt = replaceSupportdiscountAmt;
    }

    @XmlElement(name = "DiscountClass")
    private int discountClass = 0;
    
    @XmlElement(name = "CouponNo")
    private String couponNo;
    
    @XmlElement(name = "EvenetName")
    private String evenetName;
    
    @XmlElement(name = "ReceiptName")
    private String receiptName;
    
    @XmlElement(name = "UnitPrice")
    private double unitPrice;
    
    @XmlElement(name = "IssueCount")
    private int issueCount;
    
    @XmlElement(name = "IssueType")
    private String issueType;
    
    @XmlElement(name = "PremiumList")
    private List<PremiumInfo> premiumList;
    
    @XmlElement(name = "PremiumItemName")
    private String premiumItemName;
    
    @XmlElement(name = "TargetPrice")
    private double targetPrice;
    
    @XmlElement(name = "TargetCount")
    private int targetCount;
    
    @XmlElement(name = "Note")
    private String note;
    
    @XmlElement(name = "SalesPriceFrom")
    private String salesPriceFrom;
    
    @XmlElement(name = "OldPrice")
    private double oldPrice;
    
    @XmlElement(name = "CostPrice1")
    private double costPrice1;
    
    @XmlElement(name = "MakerPrice")
    private double makerPrice;
    
    @XmlElement(name = "Conn1")
    private String conn1 = "";
    
    @XmlElement(name = "Conn2")
    private String conn2 = "";
    
    @XmlElement(name = "PluPrice")
    private double pluPrice;
    
    @XmlElement(name = "DptNameLocal")
    private String dptNameLocal;
    
    @XmlElement(name = "ClassNameLocal")
    private String classNameLocal;
    
    @XmlElement(name = "GroupName")
    private String groupName;
    
    @XmlElement(name = "GroupID")
    private String groupID;
    
    @XmlElement(name = "NameText")
    private String nameText;
    
    @XmlElement(name = "QrPromotionId")
    private String qrPromotionId;
    
    @XmlElement(name ="QrPromotionName")
    private String qrPromotionName;
    
    @XmlElement(name ="QrMinimumPrice")
    private Double qrMinimumPrice;
    
    @XmlElement(name ="QrOutputTargetValue")
    private String qrOutputTargetValue;
    
    @XmlElement(name ="QrBmpFileName")
    private String qrBmpFileName;
    
    @XmlElement(name ="Colorkananame")
    private String colorkananame;
    
    @XmlElement(name ="SizeKanaName")
    private String sizeKanaName;
    
    @XmlElement(name ="BrandName")
    private String brandName;
    
    public String getColorkananame() {
        return colorkananame;
    }

    public void setColorkananame(String colorkananame) {
        this.colorkananame = colorkananame;
    }

    public String getSizeKanaName() {
        return sizeKanaName;
    }

    public void setSizeKanaName(String sizeKanaName) {
        this.sizeKanaName = sizeKanaName;
    }

    @XmlElement(name ="QrCodeList")
    private List<QrCodeInfo> qrCodeList;
    
    public List<QrCodeInfo> getQrCodeList() {
        return qrCodeList;
    }

    public void setQrCodeList(List<QrCodeInfo> qrCodeList) {
        this.qrCodeList = qrCodeList;
    }

    public String getQrPromotionId() {
        return qrPromotionId;
    }

    public void setQrPromotionId(String qrPromotionId) {
        this.qrPromotionId = qrPromotionId;
    }

    public String getQrPromotionName() {
        return qrPromotionName;
    }

    public void setQrPromotionName(String qrPromotionName) {
        this.qrPromotionName = qrPromotionName;
    }

    public Double getQrMinimumPrice() {
        return qrMinimumPrice;
    }

    public void setQrMinimumPrice(Double qrMinimumPrice) {
        this.qrMinimumPrice = qrMinimumPrice;
    }

    public String getQrOutputTargetValue() {
        return qrOutputTargetValue;
    }

    public void setQrOutputTargetValue(String qrOutputTargetValue) {
        this.qrOutputTargetValue = qrOutputTargetValue;
    }

    public String getQrBmpFileName() {
        return qrBmpFileName;
    }

    public void setQrBmpFileName(String qrBmpFileName) {
        this.qrBmpFileName = qrBmpFileName;
    }

    public String getQrBmpFileFlag() {
        return qrBmpFileFlag;
    }

    public void setQrBmpFileFlag(String qrBmpFileFlag) {
        this.qrBmpFileFlag = qrBmpFileFlag;
    }

    public String getQrBmpFileCount() {
        return qrBmpFileCount;
    }

    public void setQrBmpFileCount(String qrBmpFileCount) {
        this.qrBmpFileCount = qrBmpFileCount;
    }

    @XmlElement(name ="QrBmpFileFlag")
    private String qrBmpFileFlag;
    
    @XmlElement(name ="QrBmpFileCount")
    private String qrBmpFileCount;
    
    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @XmlElement(name = "dptSubCode1")
    private String dptSubCode1;
    
    public String getDptNameLocal() {
        return dptNameLocal;
    }

    public void setDptNameLocal(String dptNameLocal) {
        this.dptNameLocal = dptNameLocal;
    }

    public String getClassNameLocal() {
        return classNameLocal;
    }

    public void setClassNameLocal(String classNameLocal) {
        this.classNameLocal = classNameLocal;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    public String getDptSubCode1() {
        return dptSubCode1;
    }

    public void setDptSubCode1(String dptSubCode1) {
        this.dptSubCode1 = dptSubCode1;
    }

    public double getPluPrice() {
        return pluPrice;
    }

    public void setPluPrice(double pluPrice) {
        this.pluPrice = pluPrice;
    }

    public double getCostPrice1() {
        return costPrice1;
    }

    public void setCostPrice1(double costPrice1) {
        this.costPrice1 = costPrice1;
    }

    public double getMakerPrice() {
        return makerPrice;
    }

    public void setMakerPrice(double makerPrice) {
        this.makerPrice = makerPrice;
    }

    public String getConn1() {
        return conn1;
    }

    public void setConn1(String conn1) {
        this.conn1 = conn1;
    }

    public String getConn2() {
        return conn2;
    }

    public void setConn2(String conn2) {
        this.conn2 = conn2;
    }

    @XmlElement(name = "MdVender")
    private String mdVender = "";
    
    

    
    public String getMdVender() {
        return mdVender;
    }

    public void setMdVender(String mdVender) {
        this.mdVender = mdVender;
    }

    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    public String getSalesPriceFrom() {
        return salesPriceFrom;
    }

    public void setSalesPriceFrom(String salesPriceFrom) {
        this.salesPriceFrom = salesPriceFrom;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getCouponNo() {
        return couponNo;
    }

    public String getPremiumItemName() {
        return premiumItemName;
    }

    public void setPremiumItemName(String premiumItemName) {
        this.premiumItemName = premiumItemName;
    }

    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    public String getEvenetName() {
        return evenetName;
    }

    public void setEvenetName(String evenetName) {
        this.evenetName = evenetName;
    }

    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    public double getDiacountRate() {
        return diacountRate;
    }

    public void setDiacountRate(double diacountRate) {
        this.diacountRate = diacountRate;
    }

    public int getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(int discountAmt) {
        this.discountAmt = discountAmt;
    }

    public int getDiscountClass() {
        return discountClass;
    }

    public void setDiscountClass(int discountClass) {
        this.discountClass = discountClass;
    }

    @XmlElement(name = "RuleQuantity1")
    private int ruleQuantity1;
    @XmlElement(name = "RuleQuantity2")
    private int ruleQuantity2;
    @XmlElement(name = "RuleQuantity3")
    private int ruleQuantity3;
    
    @XmlElement(name = "ConditionPrice1")
    private double ConditionPrice1;
    
    @XmlElement(name = "ConditionPrice2")
    private double ConditionPrice2;
    
    @XmlElement(name = "ConditionPrice3")
    private double ConditionPrice3;
    
    @XmlElement(name = "DecisionPrice1")
    private Double DecisionPrice1 = null;
    
    @XmlElement(name = "DecisionPrice2")
    private Double DecisionPrice2 = null;
    
    @XmlElement(name = "DecisionPrice3")
    private Double DecisionPrice3 = null;
    
    @XmlElement(name = "AveragePrice1")
    private double AveragePrice1;
    
    @XmlElement(name = "AveragePrice2")
    private double AveragePrice2;
    
    public int getRuleQuantity1() {
        return ruleQuantity1;
    }

    public void setRuleQuantity1(int ruleQuantity1) {
        this.ruleQuantity1 = ruleQuantity1;
    }

    public int getRuleQuantity2() {
        return ruleQuantity2;
    }

    public void setRuleQuantity2(int ruleQuantity2) {
        this.ruleQuantity2 = ruleQuantity2;
    }

    public int getRuleQuantity3() {
        return ruleQuantity3;
    }

    public void setRuleQuantity3(int ruleQuantity3) {
        this.ruleQuantity3 = ruleQuantity3;
    }

    public double getConditionPrice1() {
        return ConditionPrice1;
    }

    public void setConditionPrice1(double conditionPrice1) {
        ConditionPrice1 = conditionPrice1;
    }

    public double getConditionPrice2() {
        return ConditionPrice2;
    }

    public void setConditionPrice2(double conditionPrice2) {
        ConditionPrice2 = conditionPrice2;
    }

    public double getConditionPrice3() {
        return ConditionPrice3;
    }

    public void setConditionPrice3(double conditionPrice3) {
        ConditionPrice3 = conditionPrice3;
    }

    public Double getDecisionPrice1() {
        return DecisionPrice1;
    }

    public void setDecisionPrice1(Double decisionPrice1) {
        DecisionPrice1 = decisionPrice1;
    }

    public Double getDecisionPrice2() {
        return DecisionPrice2;
    }

    public void setDecisionPrice2(Double decisionPrice2) {
        DecisionPrice2 = decisionPrice2;
    }

    public Double getDecisionPrice3() {
        return DecisionPrice3;
    }

    public void setDecisionPrice3(Double decisionPrice3) {
        DecisionPrice3 = decisionPrice3;
    }

    public double getAveragePrice1() {
        return AveragePrice1;
    }

    public void setAveragePrice1(double averagePrice1) {
        AveragePrice1 = averagePrice1;
    }

    public double getAveragePrice2() {
        return AveragePrice2;
    }

    public void setAveragePrice2(double averagePrice2) {
        AveragePrice2 = averagePrice2;
    }

    public double getAveragePrice3() {
        return AveragePrice3;
    }

    public void setAveragePrice3(double averagePrice3) {
        AveragePrice3 = averagePrice3;
    }

    @XmlElement(name = "AveragePrice3")
    private double AveragePrice3;
 
    
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getMd02() {
        return md02;
    }

    public void setMd02(String md02) {
        this.md02 = md02;
    }

    public String getMd03() {
        return md03;
    }

    public void setMd03(String md03) {
        this.md03 = md03;
    }

    public String getMd04() {
        return md04;
    }

    public void setMd04(String md04) {
        this.md04 = md04;
    }

    public String getMd05() {
        return md05;
    }

    public void setMd05(String md05) {
        this.md05 = md05;
    }

    public String getMd06() {
        return md06;
    }

    public void setMd06(String md06) {
        this.md06 = md06;
    }

    public String getMd07() {
        return md07;
    }

    public void setMd07(String md07) {
        this.md07 = md07;
    }

    public String getMd08() {
        return md08;
    }

    public void setMd08(String md08) {
        this.md08 = md08;
    }

    public String getMd09() {
        return md09;
    }

    public void setMd09(String md09) {
        this.md09 = md09;
    }

    public String getMd10() {
        return md10;
    }

    public void setMd10(String md10) {
        this.md10 = md10;
    }

    public String getMd11() {
        return md11;
    }

    public void setMd11(String md11) {
        this.md11 = md11;
    }

    public String getMd12() {
        return md12;
    }

    public void setMd12(String md12) {
        this.md12 = md12;
    }

    public String getMd13() {
        return md13;
    }

    public void setMd13(String md13) {
        this.md13 = md13;
    }

    public String getMd14() {
        return md14;
    }

    public void setMd14(String md14) {
        this.md14 = md14;
    }

    public String getMd15() {
        return md15;
    }

    public void setMd15(String md15) {
        this.md15 = md15;
    }

    public String getMd16() {
        return md16;
    }

    public void setMd16(String md16) {
        this.md16 = md16;
    }

    public String getMdNameLocal() {
        return mdNameLocal;
    }

    public void setMdNameLocal(String mdNameLocal) {
        this.mdNameLocal = mdNameLocal;
    }

    public String getMdKanaName() {
        return mdKanaName;
    }

    public void setMdKanaName(String mdKanaName) {
        this.mdKanaName = mdKanaName;
    }

    public double getSalesPrice2() {
        return salesPrice2;
    }

    public void setSalesPrice2(double salesPrice2) {
        this.salesPrice2 = salesPrice2;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    public String getSubCode2() {
        return subCode2;
    }

    public void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    public String getSubCode3() {
        return subCode3;
    }

    public void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    public int getSubNum2() {
        return subNum2;
    }

    public void setSubNum2(int subNum2) {
        this.subNum2 = subNum2;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
    
    public String getPromotionNo() {
        return promotionNo;
    }

    public void setPromotionNo(String promotionNo) {
        this.promotionNo = promotionNo;
    }

    public String getPremiumItemNo() {
        return premiumItemNo;
    }

    public void setPremiumItemNo(String premiumItemNo) {
        this.premiumItemNo = premiumItemNo;
    }

    @XmlElement(name = "PremiumItemNo")
    private String premiumItemNo;
    // ----end

    @XmlElement(name = "Class")
    private String itemClass;

    @XmlElement(name = "AgeRestrictedFlag")
    private int ageRestrictedFlag = -1;

    @XmlElement(name = "CouponFlag")
    private String couponFlag;

    @XmlElement(name = "RetailStoreID")
    private String retailStoreId;
    /* 1.02 2015.04.02 商品情報取得 ADD START*/
    @XmlElement(name = "EventId")
    private String  EventId="";

    @XmlElement(name = "EventName")
    private String EventName = "";
    
    @XmlElement(name = "eventSalesPrice")
    private double eventSalesPrice ;
    
    /* 1.02 2015.04.02 商品情報取得 ADD END*/
    /* 1.01 2014.11.19 商品情報取得 ADD START*/
    @XmlElement(name = "EmpPrice1")
    private double empPrice1;

    @XmlElement(name = "Md01")
    private String md01 = "";
    
    @XmlElement(name = "PSType")
    private String pSType = "";
    
    @XmlElement(name = "OrgSalesPrice1")
    private double orgSalesPrice1;
    /* 1.01 2014.11.19 商品情報取得 ADD END*/

    @XmlElement(name = "InheritFlag")
    private String inheritFlag;

    private int mustBuyFlag;
    
    @XmlElement(name = "mixMatchCode")
    private String mixMatchCode;

    private String updAppId;

    private String updOpeCode;

    // constructors
    public Item() {
    }

    public Item(final String itemId) {
        this.itemId = itemId;
    }

    public Item(final Item item) {
        this.companyId = item.getCompanyId();
        this.itemId = item.getItemId();
        this.department = item.getDepartment();
        this.description = item.getDescription();
        this.regularSalesUnitPrice = item.getRegularSalesUnitPrice();
        this.discount = item.getDiscount();
        this.discountAmount = item.getDiscountAmount();
        this.actualSalesUnitPrice = item.getActualSalesUnitPrice();
        this.discountable = item.getDiscountable();
        this.taxRate = item.getTaxRate();
        this.taxType = item.getTaxType();
        this.discountType = item.getDiscountType();
        this.nonSales = item.getNonSales();
        this.subInt10 = item.getSubInt10();
        this.line = item.getLine();
        this.itemClass = item.getItemClass();
        this.ageRestrictedFlag = item.getAgeRestrictedFlag();
        this.couponFlag = item.getCouponFlag();
        this.discountFlag = item.getDiscountFlag();
        this.mustBuyFlag = item.getMustBuyFlag();
        this.mixMatchCode = item.getMixMatchCode();
        /* 1.01 2014.11.19 商品情報取得 ADD START*/
        this.md01 =  item.getMd01();
        this.empPrice1 = item.getEmpPrice1();
        this.pSType = item.getPSType();
        this.orgSalesPrice1 = item.getOrgSalesPrice1();
        this.promotionNo = item.getPromotionNo();
        this.premiumItemNo = item.getPremiumItemNo();
        /* 1.01 2014.11.19 商品情報取得 ADD END*/
        this.inheritFlag = item.getInheritFlag();
        // add 20150827
        this.mdType = item.getMdType();
        this.sku = item.getSku();
        this.md02 = item.getMd02();
        this.md03 = item.getMd03();
        this.md04 = item.getMd04();
        this.md05 = item.getMd05();
        this.md06 = item.getMd06();
        this.md07 = item.getMd07();
        this.md08 = item.getMd08();
        this.md09 = item.getMd09();
        this.md10 = item.getMd10();
        this.md11 = item.getMd11();
        this.md12 = item.getMd12();
        this.md13 = item.getMd13();
        this.md14 = item.getMd14();
        this.md15 = item.getMd15();
        this.md16 = item.getMd16();
        this.mdNameLocal = item.getMdNameLocal();
        this.mdKanaName = item.getMdKanaName();
        this.salesPrice2 =item.getSalesPrice2();
        this.paymentType = item.getPaymentType();
        this.subCode1 = item.getSubCode1();
        this.subCode2 = item.getSubCode2();
        this.subCode3 = item.getSubCode3();
        this.subNum2 = item.getSubNum2();
        //end
    }

    public Item(final String itemId, final Description description,
            final double regularSalesUnitPrice, final String department,
        /* 1.01 2014.11.19 商品情報取得 ADD START*/
            final String line, final String classValue,final double empPrice1,
            final String md01,final String pSType,final double orgSalesPrice1,final String mixMatchCode) {
        /* 1.01 2014.11.19 商品情報取得 ADD END*/
        setItemId(itemId);
        setDepartment(department);
        setDescription(description);
        setDiscount(0);
        setDiscountAmount(0);
        setActualSalesUnitPrice(regularSalesUnitPrice);
        setRegularSalesUnitPrice(regularSalesUnitPrice);
        setLine(line);
        setItemClass(classValue);
        setMixMatchCode(mixMatchCode);
        /* 1.01 2014.11.19 商品情報取得 ADD START*/
        setEmpPrice1(empPrice1);
        setMd01(md01);
        setPSType(pSType);
        setOrgSalesPrice1(orgSalesPrice1);
        /* 1.01 2014.11.19 商品情報取得 ADD END*/
    }
    
    public final String getCompanyId() {
        return this.companyId;
    }

    public final void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }

    public final String getItemId() {
        return this.itemId;
    }

    public final void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    public final String getDepartment() {
        return this.department;
    }

    public final void setDepartment(final String department) {
        this.department = department;
    }

    public final Description getDescription() {
        return this.description;
    }

    public final void setDescription(final Description description) {
        this.description = description;
    }

    public final double getRegularSalesUnitPrice() {
        return this.regularSalesUnitPrice;
    }

    public final void setRegularSalesUnitPrice(final double regularSalesUnitPrice) {
        this.regularSalesUnitPrice = regularSalesUnitPrice;
    }

    public final double getDiscount() {
        return this.discount;
    }

    public final void setDiscount(final double discount) {
        this.discount = discount;
    }

    public final double getDiscountAmount() {
        return this.discountAmount;
    }

    public final void setDiscountAmount(final double discountAmount) {
        this.discountAmount = discountAmount;
    }

    public final double getActualSalesUnitPrice() {
        return this.actualSalesUnitPrice;
    }

    public final void setActualSalesUnitPrice(final double actualSalesUnitPrice) {
        this.actualSalesUnitPrice = actualSalesUnitPrice;
    }

    public final boolean getDiscountable() {
        return this.discountable;
    }

    public final void setDiscountable(final boolean discountable) {
        this.discountable = discountable;
    }

    public final int getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(final int taxRate) {
        this.taxRate = taxRate;
    }

    public final int getTaxType() {
        return this.taxType;
    }

    public final void setTaxType(final int taxType) {
        this.taxType = taxType;
    }

    public final int getDiscountType() {
        return this.discountType;
    }

    public final void setDiscountType(final int discountType) {
        this.discountType = discountType;

        // set discountable
        this.discountable = (this.discountType == 0);
    }

    public final int getNonSales() {
        return this.nonSales;
    }

    public final void setNonSales(final int nonSales) {
        this.nonSales = nonSales;
    }

    public final int getSubInt10() {
        return subInt10;
    }

    public final void setSubInt10(final int subInt10) {
        this.subInt10 = subInt10;
    }

    public final String getLine() {
        return this.line;
    }

    public final void setLine(final String line) {
        this.line = line;
    }

    public final String getItemClass() {
        return this.itemClass;
    }

    public final void setItemClass(final String itemClass) {
        this.itemClass = itemClass;
    }

    public final int getAgeRestrictedFlag() {
        return this.ageRestrictedFlag;
    }

    public final void setAgeRestrictedFlag(final int ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }

    public final String getCouponFlag() {
        return couponFlag;
    }

    public final void setCouponFlag(final String couponFlag) {
        this.couponFlag = couponFlag;
    }

    public final int getDiscountFlag() {
        return discountFlag;
    }

    public final void setDiscountFlag(final int discountFlag) {
        this.discountFlag = discountFlag;
    }

    public final int getMustBuyFlag() {
        return this.mustBuyFlag;
    }

    public final void setMustBuyFlag(final int mustBuyFlag) {
        this.mustBuyFlag = mustBuyFlag;
    }

    public final String getMixMatchCode() {
        return this.mixMatchCode;
    }

    public final void setMixMatchCode(final String mixMatchCode) {
        this.mixMatchCode = mixMatchCode;
    }

    public final String getRetailStoreId() {
        return retailStoreId;
    }

    public final void setRetailStoreId(final String retailStoreId) {
        this.retailStoreId = retailStoreId;
    }

    public final String getUpdAppId() {
        return updAppId;
    }

    public final void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }

    public final String getUpdOpeCode() {
        return updOpeCode;
    }

    public final void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }
    /* 1.01 2014.11.19 商品情報取得 ADD START*/
   public final double getEmpPrice1() {
       return this.empPrice1;
   }

   public final void setEmpPrice1(final double empPrice1) {
       this.empPrice1 = empPrice1;
   }

   public final String getMd01(){
       return this.md01;
   }

   public final void setMd01(final String md01){
       this.md01 = md01;
   }
   
   public final String getPSType(){
       return this.pSType;
   }
   
   public final void setPSType(final String pSType){
       this.pSType = pSType;
   }
   
   public final double getOrgSalesPrice1(){
       return this.orgSalesPrice1;
   }
   
   public final void setOrgSalesPrice1(final double orgSalesPrice1){
       this.orgSalesPrice1 = orgSalesPrice1;
   } 
   /* 1.01 2014.11.19 商品情報取得 ADD END*/


    public String getInheritFlag() {
            return inheritFlag;
        }

    public void setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    public String  getEventId() {
        return EventId;
    }

    public void setEventId(String  eventId) {
        EventId = eventId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public double getEventSalesPrice() {
        return eventSalesPrice;
    }

    public void setEventSalesPrice(double eventSalesPrice) {
        this.eventSalesPrice = eventSalesPrice;
    }
    public List<PremiumInfo> getPremiumList() {
        return premiumList;
    }

    public void setPremiumList(List<PremiumInfo> premiumList) {
        this.premiumList = premiumList;
    }

    public final String getBrandName() {
		return brandName;
	}

	public final void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	@Override
    public final String toString() {
      StringBuilder str = new StringBuilder();
      String clrf = "; ";
      str.append("ItemID: ").append(itemId).append(clrf)
         .append("Description: ").append(description != null ?
                 description.toString() : "").append(clrf)
         .append("RegularSalesUnitPrice: ").append(regularSalesUnitPrice)
         .append(clrf)
         .append("ActualSalesPrice: ").append(actualSalesUnitPrice).append(clrf)
         .append("Department: ").append(department).append(clrf)
         .append("Discount: ").append(discount).append(clrf)
         .append("Discount Amount: ").append(discountAmount).append(clrf)
         .append("SubInt10: ").append(subInt10).append(clrf)
         .append("Line: ").append(line).append(clrf)
          /* 1.01 2014.11.19 商品情報取得 ADD START*/
         .append("EmpPrice1: ").append(empPrice1).append(clrf)
         .append("Md01:").append(md01).append(clrf)
         .append("PSType").append(pSType).append(clrf)
         .append("OrgSalesPrice1").append(orgSalesPrice1).append(clrf)
         /* 1.01 2014.11.19 商品情報取得 ADD END*/
         .append("Class: ").append(itemClass);
      return str.toString();
    }

}
