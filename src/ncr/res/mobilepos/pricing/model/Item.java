package ncr.res.mobilepos.pricing.model;
/**
 * ���藚��
 * �o�[�W����      ������t        �S���Җ�      ������e
 * 1.01            2014.11.19      LIQIAN        ���i���擾
 */


import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Item Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Item")
@ApiModel(value="Item")
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
    private String discountType;
    
    @XmlElement(name = "SubNum1")
    private int subNum1 = 0;

    @ApiModelProperty(value="���劄���^�C�v", notes="���劄���^�C�v")
    public int getDptDiscountType() {
        return dptDiscountType;
    }

    public void setDptDiscountType(int dptDiscountType) {
        this.dptDiscountType = dptDiscountType;
    }

    @ApiModelProperty(value="���b�g��", notes="���b�g��")
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
    
    @ApiModelProperty(value="���i�敪", notes="���i�敪")
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
    
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
    
    @ApiModelProperty(value="�u�����������l�����z", notes="�u�����������l�����z")
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
    
    @XmlElement(name ="cCode")
    private String cCode;

    @XmlElement(name ="barCodePrice")
    private double barCodePrice;
    
    @ApiModelProperty(value="�J���[����(�J�i)", notes="�J���[����(�J�i)")
    public String getColorkananame() {
        return colorkananame;
    }

    public void setColorkananame(String colorkananame) {
        this.colorkananame = colorkananame;
    }

    @ApiModelProperty(value="�T�C�Y����(�J�i)", notes="�T�C�Y����(�J�i)")
    public String getSizeKanaName() {
        return sizeKanaName;
    }

    public void setSizeKanaName(String sizeKanaName) {
        this.sizeKanaName = sizeKanaName;
    }

    @XmlElement(name ="QrCodeList")
    private List<QrCodeInfo> qrCodeList;
    
    @ApiModelProperty(value="QR�R�[�h�Ǘ��}�X�^", notes="QR�R�[�h�Ǘ��}�X�^")
    public List<QrCodeInfo> getQrCodeList() {
        return qrCodeList;
    }

    public void setQrCodeList(List<QrCodeInfo> qrCodeList) {
        this.qrCodeList = qrCodeList;
    }

    @ApiModelProperty(value="QR���R�[�h", notes="QR���R�[�h")
    public String getQrPromotionId() {
        return qrPromotionId;
    }

    public void setQrPromotionId(String qrPromotionId) {
        this.qrPromotionId = qrPromotionId;
    }

    @ApiModelProperty(value="QR��於��", notes="QR��於��")
    public String getQrPromotionName() {
        return qrPromotionName;
    }

    public void setQrPromotionName(String qrPromotionName) {
        this.qrPromotionName = qrPromotionName;
    }

    @ApiModelProperty(value="QR�������z", notes="QR�������z")
    public Double getQrMinimumPrice() {
        return qrMinimumPrice;
    }

    public void setQrMinimumPrice(Double qrMinimumPrice) {
        this.qrMinimumPrice = qrMinimumPrice;
    }

    @ApiModelProperty(value="QR�o�͊�l", notes="QR�o�͊�l")
    public String getQrOutputTargetValue() {
        return qrOutputTargetValue;
    }

    public void setQrOutputTargetValue(String qrOutputTargetValue) {
        this.qrOutputTargetValue = qrOutputTargetValue;
    }

    @ApiModelProperty(value="QR bmp�t�@�C����", notes="QR bmp�t�@�C����")
    public String getQrBmpFileName() {
        return qrBmpFileName;
    }

    public void setQrBmpFileName(String qrBmpFileName) {
        this.qrBmpFileName = qrBmpFileName;
    }

    @ApiModelProperty(value="QR bmp�t�@�C���t���O", notes="QR bmp�t�@�C���t���O")
    public String getQrBmpFileFlag() {
        return qrBmpFileFlag;
    }

    public void setQrBmpFileFlag(String qrBmpFileFlag) {
        this.qrBmpFileFlag = qrBmpFileFlag;
    }

    @ApiModelProperty(value="QR bmp�t�@�C��������", notes="QR bmp�t�@�C��������")
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
    
    @ApiModelProperty(value="�O���[�v�R�[�h", notes="�O���[�v�R�[�h")
    public String getGroupID() {
        return groupID;
    }

    public void setGroupID(String groupID) {
        this.groupID = groupID;
    }

    @XmlElement(name = "dptSubCode1")
    private String dptSubCode1;
    
    @XmlElement(name = "dptSubNum1")
    private String dptSubNum1;
    
    @XmlElement(name = "dptSubNum2")
    private String dptSubNum2;
    
    @XmlElement(name = "dptSubNum3")
    private String dptSubNum3;
    
    @XmlElement(name = "dptSubNum4")
    private String dptSubNum4;
    
    @ApiModelProperty(value="���{��啪�ޖ���", notes="���{��啪�ޖ���")
    public String getDptNameLocal() {
        return dptNameLocal;
    }

    public void setDptNameLocal(String dptNameLocal) {
        this.dptNameLocal = dptNameLocal;
    }

    @ApiModelProperty(value="���{��N���X����", notes="���{��N���X����")
    public String getClassNameLocal() {
        return classNameLocal;
    }

    public void setClassNameLocal(String classNameLocal) {
        this.classNameLocal = classNameLocal;
    }

    @ApiModelProperty(value="�O���[�v����", notes="�O���[�v����")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @ApiModelProperty(value="����", notes="����")
    public String getNameText() {
        return nameText;
    }

    public void setNameText(String nameText) {
        this.nameText = nameText;
    }

    @ApiModelProperty(value="����i�ԋ敪", notes="����i�ԋ敪")
    public String getDptSubCode1() {
        return dptSubCode1;
    }

    public void setDptSubCode1(String dptSubCode1) {
        this.dptSubCode1 = dptSubCode1;
    }
    
    @ApiModelProperty(value="�d�q�}�l�[���p�ۃt���O", notes="�d�q�}�l�[���p�ۃt���O")
    public String getDptSubNum1() {
        return dptSubNum1;
    }

    public void setDptSubNum1(String dptSubNum1) {
        this.dptSubNum1 = dptSubNum1;
    }
    
    @ApiModelProperty(value="KPC�t�^�Ώۃt���O", notes="KPC�t�^�Ώۃt���O")
    public String getDptSubNum2() {
        return dptSubNum2;
    }

    public void setDptSubNum2(String dptSubNum2) {
        this.dptSubNum2 = dptSubNum2;
    }
    
    @ApiModelProperty(value="KPC���p�Ώۃt���O", notes="KPC���p�Ώۃt���O")
    public String getDptSubNum3() {
        return dptSubNum3;
    }

    public void setDptSubNum3(String dptSubNum3) {
        this.dptSubNum3 = dptSubNum3;
    }
    
    @ApiModelProperty(value="KPC�݌v�w�����z�A�g�Ώۃt���O", notes="KPC�݌v�w�����z�A�g�Ώۃt���O")
    public String getDptSubNum4() {
        return dptSubNum4;
    }

    public void setDptSubNum4(String dptSubNum4) {
        this.dptSubNum4 = dptSubNum4;
    }

    @ApiModelProperty(value="JAN�R�[�h���i", notes="JAN�R�[�h���i")
    public double getPluPrice() {
        return pluPrice;
    }

    public void setPluPrice(double pluPrice) {
        this.pluPrice = pluPrice;
    }

    @ApiModelProperty(value="���P��", notes="���P��")
    public double getCostPrice1() {
        return costPrice1;
    }

    public void setCostPrice1(double costPrice1) {
        this.costPrice1 = costPrice1;
    }

    @ApiModelProperty(value="�����P��", notes="�����P��")
    public double getMakerPrice() {
        return makerPrice;
    }

    public void setMakerPrice(double makerPrice) {
        this.makerPrice = makerPrice;
    }

    @ApiModelProperty(value="�d����R�[�h", notes="�d����R�[�h")
    public String getConn1() {
        return conn1;
    }

    public void setConn1(String conn1) {
        this.conn1 = conn1;
    }

    @ApiModelProperty(value="�T�u�d����R�[�h", notes="�T�u�d����R�[�h")
    public String getConn2() {
        return conn2;
    }

    public void setConn2(String conn2) {
        this.conn2 = conn2;
    }

    @XmlElement(name = "MdVender")
    private String mdVender = "";
    
    @ApiModelProperty(value="���[�J�[�i��", notes="���[�J�[�i��")
    public String getMdVender() {
        return mdVender;
    }

    public void setMdVender(String mdVender) {
        this.mdVender = mdVender;
    }

    @ApiModelProperty(value="�����P��", notes="�����P��")
    public double getOldPrice() {
        return oldPrice;
    }

    public void setOldPrice(double oldPrice) {
        this.oldPrice = oldPrice;
    }

    @ApiModelProperty(value="�̔����i", notes="�̔����i")
    public String getSalesPriceFrom() {
        return salesPriceFrom;
    }

    public void setSalesPriceFrom(String salesPriceFrom) {
        this.salesPriceFrom = salesPriceFrom;
    }

    @ApiModelProperty(value="�p�����[�^�[���l", notes="�p�����[�^�[���l")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @ApiModelProperty(value="������NO", notes="������NO")
    public String getCouponNo() {
        return couponNo;
    }

    @ApiModelProperty(value="�v���~�A�����i����", notes="�v���~�A�����i����")
    public String getPremiumItemName() {
        return premiumItemName;
    }

    public void setPremiumItemName(String premiumItemName) {
        this.premiumItemName = premiumItemName;
    }

    @ApiModelProperty(value="�Ώۋ��z", notes="�Ώۋ��z")
    public double getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(double targetPrice) {
        this.targetPrice = targetPrice;
    }

    @ApiModelProperty(value="���i����", notes="���i����")
    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    @ApiModelProperty(value="�C�x���g����", notes="�C�x���g����")
    public String getEvenetName() {
        return evenetName;
    }

    public void setEvenetName(String evenetName) {
        this.evenetName = evenetName;
    }

    @ApiModelProperty(value="���V�[�g����", notes="���V�[�g����")
    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    @ApiModelProperty(value="�P�ʋ��z(�ō�)", notes="�P�ʋ��z(�ō�)")
    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    @ApiModelProperty(value="���s����", notes="���s����")
    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    @ApiModelProperty(value="���s���@", notes="���s���@")
    public String getIssueType() {
        return issueType;
    }

    public void setIssueType(String issueType) {
        this.issueType = issueType;
    }

    @ApiModelProperty(value="������", notes="������")
    public double getDiacountRate() {
        return diacountRate;
    }

    public void setDiacountRate(double diacountRate) {
        this.diacountRate = diacountRate;
    }

    @ApiModelProperty(value="�����l�����z", notes="�����l�����z")
    public int getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(int discountAmt) {
        this.discountAmt = discountAmt;
    }

    @ApiModelProperty(value="�����敪", notes="�����敪")
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
    
    @ApiModelProperty(value="�K�萔��1", notes="�K�萔��1")
    public int getRuleQuantity1() {
        return ruleQuantity1;
    }

    public void setRuleQuantity1(int ruleQuantity1) {
        this.ruleQuantity1 = ruleQuantity1;
    }

    @ApiModelProperty(value="�K�萔��2", notes="�K�萔��2")
    public int getRuleQuantity2() {
        return ruleQuantity2;
    }

    public void setRuleQuantity2(int ruleQuantity2) {
        this.ruleQuantity2 = ruleQuantity2;
    }

    @ApiModelProperty(value="�K�萔��3", notes="�K�萔��3")
    public int getRuleQuantity3() {
        return ruleQuantity3;
    }

    public void setRuleQuantity3(int ruleQuantity3) {
        this.ruleQuantity3 = ruleQuantity3;
    }

    @ApiModelProperty(value="�������i1", notes="�������i1")
    public double getConditionPrice1() {
        return ConditionPrice1;
    }

    public void setConditionPrice1(double conditionPrice1) {
        ConditionPrice1 = conditionPrice1;
    }

    @ApiModelProperty(value="�������i2", notes="�������i2")
    public double getConditionPrice2() {
        return ConditionPrice2;
    }

    public void setConditionPrice2(double conditionPrice2) {
        ConditionPrice2 = conditionPrice2;
    }

    @ApiModelProperty(value="�������i3", notes="�������i3")
    public double getConditionPrice3() {
        return ConditionPrice3;
    }

    public void setConditionPrice3(double conditionPrice3) {
        ConditionPrice3 = conditionPrice3;
    }

    @ApiModelProperty(value="�����㉿�i1", notes="�����㉿�i1")
    public Double getDecisionPrice1() {
        return DecisionPrice1;
    }

    public void setDecisionPrice1(Double decisionPrice1) {
        DecisionPrice1 = decisionPrice1;
    }

    @ApiModelProperty(value="�����㉿�i2", notes="�����㉿�i2")
    public Double getDecisionPrice2() {
        return DecisionPrice2;
    }

    public void setDecisionPrice2(Double decisionPrice2) {
        DecisionPrice2 = decisionPrice2;
    }

    @ApiModelProperty(value="�����㉿�i3", notes="�����㉿�i3")
    public Double getDecisionPrice3() {
        return DecisionPrice3;
    }

    public void setDecisionPrice3(Double decisionPrice3) {
        DecisionPrice3 = decisionPrice3;
    }

    @ApiModelProperty(value="���ω��i1", notes="���ω��i1")
    public double getAveragePrice1() {
        return AveragePrice1;
    }

    public void setAveragePrice1(double averagePrice1) {
        AveragePrice1 = averagePrice1;
    }

    @ApiModelProperty(value="���ω��i2", notes="���ω��i2")
    public double getAveragePrice2() {
        return AveragePrice2;
    }

    public void setAveragePrice2(double averagePrice2) {
        AveragePrice2 = averagePrice2;
    }

    @ApiModelProperty(value="���ω��i3", notes="���ω��i3")
    public double getAveragePrice3() {
        return AveragePrice3;
    }

    public void setAveragePrice3(double averagePrice3) {
        AveragePrice3 = averagePrice3;
    }

    @XmlElement(name = "AveragePrice3")
    private double AveragePrice3;
 
    @ApiModelProperty(value="���Еi��", notes="���Еi��")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @ApiModelProperty(value="���T�C�YNo", notes="���T�C�YNo")
    public String getMd02() {
        return md02;
    }

    public void setMd02(String md02) {
        this.md02 = md02;
    }

    @ApiModelProperty(value="�T�C�Y�p�^�[���R�[�h�P", notes="�T�C�Y�p�^�[���R�[�h�P")
    public String getMd03() {
        return md03;
    }

    public void setMd03(String md03) {
        this.md03 = md03;
    }

    @ApiModelProperty(value="�T�C�Y�p�^�[���R�[�h�Q", notes="�T�C�Y�p�^�[���R�[�h�Q")
    public String getMd04() {
        return md04;
    }

    public void setMd04(String md04) {
        this.md04 = md04;
    }

    @ApiModelProperty(value="�T�C�Y�R�[�h", notes="�T�C�Y�R�[�h")
    public String getMd05() {
        return md05;
    }

    public void setMd05(String md05) {
        this.md05 = md05;
    }

    @ApiModelProperty(value="�V�[�Y��", notes="�V�[�Y��")
    public String getMd06() {
        return md06;
    }

    public void setMd06(String md06) {
        this.md06 = md06;
    }

    @ApiModelProperty(value="�u�����h�R�[�h", notes="�u�����h�R�[�h")
    public String getMd07() {
        return md07;
    }

    public void setMd07(String md07) {
        this.md07 = md07;
    }

    @ApiModelProperty(value="�����Еi��", notes="�����Еi��")
    public String getMd08() {
        return md08;
    }

    public void setMd08(String md08) {
        this.md08 = md08;
    }

    @ApiModelProperty(value="��\���Еi��", notes="��\���Еi��")
    public String getMd09() {
        return md09;
    }

    public void setMd09(String md09) {
        this.md09 = md09;
    }

    @ApiModelProperty(value="�O���[�v�R�[�h", notes="�O���[�v�R�[�h")
    public String getMd10() {
        return md10;
    }

    public void setMd10(String md10) {
        this.md10 = md10;
    }

    @ApiModelProperty(value="�����Ώۃt���O", notes="�����Ώۃt���O")
    public String getMd11() {
        return md11;
    }

    public void setMd11(String md11) {
        this.md11 = md11;
    }

    @ApiModelProperty(value="�|�C���g�Ώۃt���O", notes="�|�C���g�Ώۃt���O")
    public String getMd12() {
        return md12;
    }

    public void setMd12(String md12) {
        this.md12 = md12;
    }

    @ApiModelProperty(value="�e�C�N�A�E�g�t���O", notes="�e�C�N�A�E�g�t���O")
    public String getMd13() {
        return md13;
    }

    public void setMd13(String md13) {
        this.md13 = md13;
    }

    @ApiModelProperty(value="�ۏ؏��t���O", notes="�ۏ؏��t���O")
    public String getMd14() {
        return md14;
    }

    public void setMd14(String md14) {
        this.md14 = md14;
    }

    @ApiModelProperty(value="��[�����Ώۏ��i", notes="��[�����Ώۏ��i")
    public String getMd15() {
        return md15;
    }

    public void setMd15(String md15) {
        this.md15 = md15;
    }

    @ApiModelProperty(value="�{�{�����t���O", notes="�{�{�����t���O")
    public String getMd16() {
        return md16;
    }

    public void setMd16(String md16) {
        this.md16 = md16;
    }

    @ApiModelProperty(value="���{�ꏤ�i��", notes="���{�ꏤ�i��")
    public String getMdNameLocal() {
        return mdNameLocal;
    }

    public void setMdNameLocal(String mdNameLocal) {
        this.mdNameLocal = mdNameLocal;
    }

    @ApiModelProperty(value="���p�J�i���i��", notes="���p�J�i���i��")
    public String getMdKanaName() {
        return mdKanaName;
    }

    public void setMdKanaName(String mdKanaName) {
        this.mdKanaName = mdKanaName;
    }

    @ApiModelProperty(value="������P��", notes="������P��")
    public double getSalesPrice2() {
        return salesPrice2;
    }

    public void setSalesPrice2(double salesPrice2) {
        this.salesPrice2 = salesPrice2;
    }

    @ApiModelProperty(value="�x���敪", notes="�x���敪")
    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    @ApiModelProperty(value="�i�ԋ敪", notes="�i�ԋ敪")
    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    @ApiModelProperty(value="�d���敪", notes="�d���敪")
    public String getSubCode2() {
        return subCode2;
    }

    public void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    @ApiModelProperty(value="���B�敪", notes="���B�敪")
    public String getSubCode3() {
        return subCode3;
    }

    public void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    @ApiModelProperty(value="�{�{���b�g��", notes="�{�{���b�g��")
    public int getSubNum2() {
        return subNum2;
    }

    public void setSubNum2(int subNum2) {
        this.subNum2 = subNum2;
    }

    @ApiModelProperty(value="���R�[�h", notes="���R�[�h")
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }
    
    @ApiModelProperty(value="���No", notes="���No")
    public String getPromotionNo() {
        return promotionNo;
    }

    public void setPromotionNo(String promotionNo) {
        this.promotionNo = promotionNo;
    }

    @ApiModelProperty(value="�v���~�A�����iNO", notes="�v���~�A�����iNO")
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
    /* 1.02 2015.04.02 ���i���擾 ADD START*/
    @XmlElement(name = "EventId")
    private String  EventId="";

    @XmlElement(name = "EventName")
    private String EventName = "";
    
    @XmlElement(name = "eventSalesPrice")
    private double eventSalesPrice ;
    
    /* 1.02 2015.04.02 ���i���擾 ADD END*/
    /* 1.01 2014.11.19 ���i���擾 ADD START*/
    @XmlElement(name = "EmpPrice1")
    private double empPrice1;

    @XmlElement(name = "Md01")
    private String md01 = "";
    
    @XmlElement(name = "PSType")
    private String pSType = "";
    
    @XmlElement(name = "OrgSalesPrice1")
    private double orgSalesPrice1;
    /* 1.01 2014.11.19 ���i���擾 ADD END*/

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
        /* 1.01 2014.11.19 ���i���擾 ADD START*/
        this.md01 =  item.getMd01();
        this.empPrice1 = item.getEmpPrice1();
        this.pSType = item.getPSType();
        this.orgSalesPrice1 = item.getOrgSalesPrice1();
        this.promotionNo = item.getPromotionNo();
        this.premiumItemNo = item.getPremiumItemNo();
        /* 1.01 2014.11.19 ���i���擾 ADD END*/
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
        /* 1.01 2014.11.19 ���i���擾 ADD START*/
            final String line, final String classValue,final double empPrice1,
            final String md01,final String pSType,final double orgSalesPrice1,final String mixMatchCode) {
        /* 1.01 2014.11.19 ���i���擾 ADD END*/
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
        /* 1.01 2014.11.19 ���i���擾 ADD START*/
        setEmpPrice1(empPrice1);
        setMd01(md01);
        setPSType(pSType);
        setOrgSalesPrice1(orgSalesPrice1);
        /* 1.01 2014.11.19 ���i���擾 ADD END*/
    }
    
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
    public final String getCompanyId() {
        return this.companyId;
    }

    public final void setCompanyId(final String companyId) {
        this.companyId = companyId;
    }

    @ApiModelProperty(value="�v���W�F�N�g�R�[�h", notes="�v���W�F�N�g�R�[�h")
    public final String getItemId() {
        return this.itemId;
    }

    public final void setItemId(final String itemId) {
        this.itemId = itemId;
    }

    @ApiModelProperty(value="����", notes="����")
    public final String getDepartment() {
        return this.department;
    }

    public final void setDepartment(final String department) {
        this.department = department;
    }

    @ApiModelProperty(value="���l", notes="���l")
    public final Description getDescription() {
        return this.description;
    }

    public final void setDescription(final Description description) {
        this.description = description;
    }

    @ApiModelProperty(value="����̔��P��", notes="����̔��P��")
    public final double getRegularSalesUnitPrice() {
        return this.regularSalesUnitPrice;
    }

    public final void setRegularSalesUnitPrice(final double regularSalesUnitPrice) {
        this.regularSalesUnitPrice = regularSalesUnitPrice;
    }

    @ApiModelProperty(value="����", notes="����")
    public final double getDiscount() {
        return this.discount;
    }

    public final void setDiscount(final double discount) {
        this.discount = discount;
    }

    @ApiModelProperty(value="�����z", notes="�����z")
    public final double getDiscountAmount() {
        return this.discountAmount;
    }

    public final void setDiscountAmount(final double discountAmount) {
        this.discountAmount = discountAmount;
    }

    @ApiModelProperty(value="���݂̔̔��P�ʉ��i", notes="���݂̔̔��P�ʉ��i")
    public final double getActualSalesUnitPrice() {
        return this.actualSalesUnitPrice;
    }

    public final void setActualSalesUnitPrice(final double actualSalesUnitPrice) {
        this.actualSalesUnitPrice = actualSalesUnitPrice;
    }

    @ApiModelProperty(value="�\���������", notes="�\���������")
    public final boolean getDiscountable() {
        return this.discountable;
    }

    public final void setDiscountable(final boolean discountable) {
        this.discountable = discountable;
    }

    @ApiModelProperty(value="����ŗ�", notes="����ŗ�")
    public final int getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(final int taxRate) {
        this.taxRate = taxRate;
    }

    @ApiModelProperty(value="�ېŋ敪", notes="�ېŋ敪")
    public final int getTaxType() {
        return this.taxType;
    }

    public final void setTaxType(final int taxType) {
        this.taxType = taxType;
    }

    @ApiModelProperty(value="�l�����t���O", notes="�l�����t���O")
    public final String getDiscountType() {
        return this.discountType;
    }

    public final void setDiscountType(final String discountType) {
        this.discountType = discountType;

        // set discountable
        this.discountable = ("0".equals(this.discountType));
    }

    @ApiModelProperty(value="��̔�", notes="��̔�")
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

    @ApiModelProperty(value="�i��R�[�h(�����ރR�[�h)", notes="�i��R�[�h(�����ރR�[�h)")
    public final String getLine() {
        return this.line;
    }

    public final void setLine(final String line) {
        this.line = line;
    }

    @ApiModelProperty(value="���ڗ�", notes="���ڗ�")
    public final String getItemClass() {
        return this.itemClass;
    }

    public final void setItemClass(final String itemClass) {
        this.itemClass = itemClass;
    }

    @ApiModelProperty(value="�N����t���O", notes="�N����t���O")
    public final int getAgeRestrictedFlag() {
        return this.ageRestrictedFlag;
    }

    public final void setAgeRestrictedFlag(final int ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }

    @ApiModelProperty(value="�N�[�|�����t���O", notes="�N�[�|�����t���O")
    public final String getCouponFlag() {
        return couponFlag;
    }

    public final void setCouponFlag(final String couponFlag) {
        this.couponFlag = couponFlag;
    }

    @ApiModelProperty(value="�����t���O", notes="�����t���O")
    public final int getDiscountFlag() {
        return discountFlag;
    }

    public final void setDiscountFlag(final int discountFlag) {
        this.discountFlag = discountFlag;
    }

    @ApiModelProperty(value="�}�X�g�o�C�t���O", notes="�}�X�g�o�C�t���O")
    public final int getMustBuyFlag() {
        return this.mustBuyFlag;
    }

    public final void setMustBuyFlag(final int mustBuyFlag) {
        this.mustBuyFlag = mustBuyFlag;
    }

    @ApiModelProperty(value="�����̃R�[�h", notes="�����̃R�[�h")
    public final String getMixMatchCode() {
        return this.mixMatchCode;
    }

    public final void setMixMatchCode(final String mixMatchCode) {
        this.mixMatchCode = mixMatchCode;
    }

    @ApiModelProperty(value="�����X�̃R�[�h", notes="�����X�̃R�[�h")
    public final String getRetailStoreId() {
        return retailStoreId;
    }

    public final void setRetailStoreId(final String retailStoreId) {
        this.retailStoreId = retailStoreId;
    }

    @ApiModelProperty(value="�ŏI�X�V�v���O�����R�[�h", notes="�ŏI�X�V�v���O�����R�[�h")
    public final String getUpdAppId() {
        return updAppId;
    }

    public final void setUpdAppId(String updAppId) {
        this.updAppId = updAppId;
    }

    @ApiModelProperty(value="�ŏI�X�V���[�U�[�R�[�h", notes="�ŏI�X�V���[�U�[�R�[�h")
    public final String getUpdOpeCode() {
        return updOpeCode;
    }

    public final void setUpdOpeCode(String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }
    /* 1.01 2014.11.19 ���i���擾 ADD START*/
    @ApiModelProperty(value="�Ј��w�����P���P", notes="�Ј��w�����P���P")
   public final double getEmpPrice1() {
       return this.empPrice1;
   }

   public final void setEmpPrice1(final double empPrice1) {
       this.empPrice1 = empPrice1;
   }

   @ApiModelProperty(value="�J���[�R�[�h", notes="�J���[�R�[�h")
   public final String getMd01(){
       return this.md01;
   }

   public final void setMd01(final String md01){
       this.md01 = md01;
   }
   
   @ApiModelProperty(value="�Z�|��/�v���p�|�敪(P:�v���p�|  S:�Z�|��)", notes="�Z�|��/�v���p�|�敪(P:�v���p�|  S:�Z�|��)")
   public final String getPSType(){
       return this.pSType;
   }
   
   public final void setPSType(final String pSType){
       this.pSType = pSType;
   }
   
   @ApiModelProperty(value="��{�i�v���p�[�j���", notes="��{�i�v���p�[�j���")
   public final double getOrgSalesPrice1(){
       return this.orgSalesPrice1;
   }
   
   public final void setOrgSalesPrice1(final double orgSalesPrice1){
       this.orgSalesPrice1 = orgSalesPrice1;
   } 
   /* 1.01 2014.11.19 ���i���擾 ADD END*/

   @ApiModelProperty(value="�p���t���O", notes="�p���t���O")
    public String getInheritFlag() {
            return inheritFlag;
        }

    public void setInheritFlag(String inheritFlag) {
        this.inheritFlag = inheritFlag;
    }
    
    @ApiModelProperty(value="�C�x���g�R�[�h", notes="�C�x���g�R�[�h")
    public String  getEventId() {
        return EventId;
    }

    public void setEventId(String  eventId) {
        EventId = eventId;
    }

    @ApiModelProperty(value="�C�x���g����", notes="�C�x���g����")
    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    @ApiModelProperty(value="�C�x���g�̔����i", notes="�C�x���g�̔����i")
    public double getEventSalesPrice() {
        return eventSalesPrice;
    }

    public void setEventSalesPrice(double eventSalesPrice) {
        this.eventSalesPrice = eventSalesPrice;
    }
    
    @ApiModelProperty(value="�v���~�A���E���X�g", notes="�v���~�A���E���X�g")
    public List<PremiumInfo> getPremiumList() {
        return premiumList;
    }

    public void setPremiumList(List<PremiumInfo> premiumList) {
        this.premiumList = premiumList;
    }

    @ApiModelProperty(value="�u�����h����", notes="�u�����h����")
    public final String getBrandName() {
		return brandName;
	}

	public final void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	
    @ApiModelProperty(value="C�R�[�h", notes="C�R�[�h")
    public final String getCategoryCode() {
        return cCode;
    }

    public final void setCategoryCode(String cCode) {
        this.cCode = cCode;
    }
    
    @ApiModelProperty(value="�A�x�����i", notes="�A�x�����i")
    public final double getLabelPrice() {
        return barCodePrice;
    }

    public final void setLabelPrice(double barCodePrice) {
        this.barCodePrice = barCodePrice;
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
          /* 1.01 2014.11.19 ���i���擾 ADD START*/
         .append("EmpPrice1: ").append(empPrice1).append(clrf)
         .append("Md01:").append(md01).append(clrf)
         .append("PSType").append(pSType).append(clrf)
         .append("OrgSalesPrice1").append(orgSalesPrice1).append(clrf)
         /* 1.01 2014.11.19 ���i���擾 ADD END*/
         .append("Class: ").append(itemClass);
      return str.toString();
    }

}
