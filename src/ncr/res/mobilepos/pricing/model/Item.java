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

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.model.PMInfo;

/**
 * Item Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Item")
@ApiModel(value="Item")
public class Item extends ResultBase {

    // rounding options
    public static final int ROUND_UP = 1;
    public static final int ROUND_DOWN = 2;
    public static final int ROUND_OFF = 3;

    // tax options
    public static final String TAX_EXCLUDED = "0";
    public static final String TAX_INCLUDED = "1";
    public static final String TAX_FREE = "2";
    
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

    @XmlElement(name = "DefaultTaxRate")
    private DefaultTaxRate defaultTaxRate;

    @XmlElement(name = "ChangeableTaxRate")
    private ChangeableTaxRate changeableTaxRate;

    @XmlElement(name = "TaxSource")
    private String taxSource;

    @XmlElement(name = "TaxId")
    private Integer taxId;
    
    @XmlElement(name = "OriginalTaxId")
    private int originalTaxId;

    @XmlElement(name = "TaxRate")
    private int taxRate;

    @XmlElement(name = "TaxType")
    private String taxType;

    @XmlElement(name = "PluTaxType")
    private String pluTaxType;

    @XmlElement(name = "DptTaxType")
    private String dptTaxType;
    
    @XmlElement(name = "LineTaxType")
    private String lineTaxType;
    
    @XmlElement(name = "ClsTaxType")
    private String clsTaxType;
    
    @XmlElement(name = "DiscountType")
    private String discountType;
    
    @XmlElement(name = "SubNum1")
    private int subNum1 = 0;

    @ApiModelProperty(value="���劄���^�C�v", notes="���劄���^�C�v")
    public String getDptDiscountType() {
        return dptDiscountType;
    }

    public void setDptDiscountType(String dptDiscountType) {
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
    
    @XmlElement(name = "PromotionType")
    private String promotionType;
    
    @XmlElement(name = "MdType")
    private String mdType = "";
    
    @XmlElement(name = "HostFlag")
    private int hostFlag;
    
    @XmlElement(name = "SalesNameSource")
    private String salesNameSource;
    
    @ApiModelProperty(value="�z�X�g�o�^�t���O", notes="�z�X�g�o�^�t���O")
    public int getHostFlag() {
        return hostFlag;
    }

    public void setHostFlag(int hostFlag) {
        this.hostFlag = hostFlag;
    }
    
    @ApiModelProperty(value="���i���擾�t���O", notes="���i���擾�t���O")
    public String getSalesNameSource() {
        return salesNameSource;
    }

    public void setSalesNameSource(String salesNameSource) {
        this.salesNameSource = salesNameSource;
    }
    
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

    @XmlElement(name = "MdName")
    private String mdName = "";

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
    
    @XmlElement(name = "PmInfoList")
    private List<? extends PMInfo> pmInfoList;
    
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    @XmlElement(name = "PricePromList")
    private List<PricePromInfo> pricePromList;
    
    @XmlElement(name = "PriceUrgentInfo")
    private PriceUrgentInfo priceUrgentInfo;

    @XmlElement(name = "DptDiscountType")
    private String dptDiscountType;
    
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

    @XmlElement(name = "DptName")
    private String dptName;
    
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
    
    @XmlElement(name ="CategoryCode")
    private String categoryCode;

    @XmlElement(name ="LabelPrice")
    private Double labelPrice;
    
    @XmlElement(name = "MagazineCode")
    private String magazineCode;
    
    @XmlElement(name = "PublishingCode")
    private String publishingCode;
    
    @XmlElement(name = "TaxTypeSource")
    private String taxTypeSource;
    
    @XmlElement(name = "DiscountTypeSource")
    private String discountTypeSource;
    
    public String getTaxTypeSource() {
        return taxTypeSource;
    }
    public void setTaxTypeSource(String taxTypeSource) {
        this.taxTypeSource = taxTypeSource;
    }
    public String getDiscountTypeSource() {
        return discountTypeSource;
    }
    public void setDiscountTypeSource(String discountTypeSource) {
        this.discountTypeSource = discountTypeSource;
    }
    
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

	@ApiModelProperty(value="�{��啪�ޖ���", notes="�{��啪�ޖ���")
	public String getDptName() {
        return dptName;
    }

    public void setDptName(String dptName) {
        this.dptName = dptName;
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
    
    @ApiModelProperty(value="Point�t�^�Ώۃt���O", notes="Point�t�^�Ώۃt���O")
    public String getDptSubNum2() {
        return dptSubNum2;
    }

    public void setDptSubNum2(String dptSubNum2) {
        this.dptSubNum2 = dptSubNum2;
    }
    
    @ApiModelProperty(value="Point���p�Ώۃt���O", notes="Point���p�Ώۃt���O")
    public String getDptSubNum3() {
        return dptSubNum3;
    }

    public void setDptSubNum3(String dptSubNum3) {
        this.dptSubNum3 = dptSubNum3;
    }
    
    @ApiModelProperty(value="Point�݌v�w�����z�A�g�Ώۃt���O", notes="Point�݌v�w�����z�A�g�Ώۃt���O")
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

    @ApiModelProperty(value="����", notes="����")
    public List<PricePromInfo> getPricePromList() {
		return pricePromList;
	}

	public void setPricePromList(List<PricePromInfo> pricePromList) {
		this.pricePromList = pricePromList;
	}
	
	@ApiModelProperty(value="�ً}����", notes="�ً}����")
    public PriceUrgentInfo getPriceUrgentInfo() {
		return priceUrgentInfo;
	}

	public void setPriceUrgentInfo(PriceUrgentInfo priceUrgentInfo) {
		this.priceUrgentInfo = priceUrgentInfo;
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

    @ApiModelProperty(value="�{�ꏤ�i��", notes="�{�ꏤ�i��")
    public String getMdName() {
        return mdName;
    }

    public void setMdName(String mdName) {
        this.mdName = mdName;
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
    
    @ApiModelProperty(value="�ݒ�敪", notes="�ݒ�敪")
    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
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

    // FDMM add start by mt185204 2020-07-15
    @XmlElement(name = "PharmaceuticalFlag")
    private String PharmaceuticalFlag;

    @ApiModelProperty(value="�R�f�C���ܗL���i", notes="�R�f�C���ܗL���i")
    public String getPharmaceuticalFlag() {
        return PharmaceuticalFlag;
    }

    public void setPharmaceuticalFlag(String PharmaceuticalFlag) {
        this.PharmaceuticalFlag = PharmaceuticalFlag;
    }
    // FDMM add end by mt185204 2020-07-15

    // FDMM add start RESD-3589 2020-09-8
    @XmlElement(name = "RecallFlag")
    private String RecallFlag;

    @ApiModelProperty(value="����w��", notes="����w��")
    public String getRecallFlag() {
        return RecallFlag;
    }
    
    public void setRecallFlag(String RecallFlag) {
        this.RecallFlag = RecallFlag;
    }
    
    @XmlElement(name = "CallInReason")
    private String CallInReason;

    @ApiModelProperty(value="������R", notes="������R")
    public String getCallInReason() {
        return CallInReason;
    }

    public void setCallInReason(String CallInReason) {
        this.CallInReason = CallInReason;
    }
    // FDMM add end RESD-3589 2020-09-8

    // FDMM add start RESD-3824 2020-08-18
    @XmlElement(name = "AgeRestrictedFlag")
    private String ageRestrictedFlag;
    
    @XmlElement(name = "ClsAgeRestrictedFlag")
    private String ClsAgeRestrictedFlag;
    
    @XmlElement(name = "LineAgeRestrictedFlag")
    private String LineAgeRestrictedFlag;
    
    @XmlElement(name = "DptAgeRestrictedFlag")
    private String DptAgeRestrictedFlag;
    
    @XmlElement(name = "AgeSource")
    private String AgeSource;
    
    @ApiModelProperty(value="�N����t���O", notes="�N����t���O")
    public final String getAgeRestrictedFlag() {
        return this.ageRestrictedFlag;
    }

    public final void setAgeRestrictedFlag(final String ageRestrictedFlag) {
        this.ageRestrictedFlag = ageRestrictedFlag;
    }
    
    @ApiModelProperty(value="class�Ɛŋ敪", notes="class�Ɛŋ敪")
	public String getClsAgeRestrictedFlag() {
		return ClsAgeRestrictedFlag;
	}

	public void setClsAgeRestrictedFlag(String ClsAgeRestrictedFlag) {
		this.ClsAgeRestrictedFlag = ClsAgeRestrictedFlag;
	}
	@ApiModelProperty(value="line�Ɛŋ敪", notes="line�Ɛŋ敪")
	public String getLineAgeRestrictedFlag() {
		return LineAgeRestrictedFlag;
	}

	public void setLineAgeRestrictedFlag(String LineAgeRestrictedFlag) {
		this.LineAgeRestrictedFlag = LineAgeRestrictedFlag;
	}
	@ApiModelProperty(value="dpt�Ɛŋ敪", notes="dpt�Ɛŋ敪")
	public String getDptAgeRestrictedFlag() {
		return DptAgeRestrictedFlag;
	}

	public void setDptAgeRestrictedFlag(String DptAgeRestrictedFlag) {
		this.DptAgeRestrictedFlag = DptAgeRestrictedFlag;
	}
	
	@ApiModelProperty(value="�N����t���O���擾�����}�X�^�[�e�[�u���̎��ʎq", notes="�N����t���O���擾�����}�X�^�[�e�[�u���̎��ʎq")
	public String getAgeSource() {
		return AgeSource;
	}

	public void setAgeSource(String AgeSource) {
		this.AgeSource = AgeSource;
	}
    // FDMM add end RESD-3824 2020-08-18
    
	// FDMM add start RESD-3584 2020-08-19
	@XmlElement(name = "SelfFlag")
    private String SelfFlag;
	@XmlElement(name = "SelfMedicationMark")
    private String SelfMedicationMark;
	
	@ApiModelProperty(value="�Z���t���f�B�P�[�V�����Ő��t���O", notes="�Z���t���f�B�P�[�V�����Ő��t���O")
	public String getSelfFlag() {
		return SelfFlag;
	}

	public void setSelfFlag(String SelfFlag) {
		this.SelfFlag = SelfFlag;
	}
	@ApiModelProperty(value="�Z���t���f�B�P�[�V�����}�[�N", notes="�Z���t���f�B�P�[�V�����}�[�N")
	public String getSelfMedicationMark() {
		return SelfMedicationMark;
	}

	public void setSelfMedicationMark(String SelfMedicationMark) {
		this.SelfMedicationMark = SelfMedicationMark;
	}
	// FDMM add end RESD-3584 2020-08-19
	
	// FDMM add start RESD-3595 2020-09-18
	@XmlElement(name = "DrugActType")
	private String DrugActType;
	@XmlElement(name = "DrugType")
	private String DrugType;

	@ApiModelProperty(value="���i����敪", notes="���i����敪")
	public String getDrugActType() {
		return DrugActType;
	}

	public void setDrugActType(String DrugActType) {
		this.DrugActType = DrugActType;
	}
	@ApiModelProperty(value="���i����", notes="���i����")
	public String getDrugType() {
		return DrugType;
	}

	public void setDrugType(String DrugType) {
		this.DrugType = DrugType;
	}
	// FDMM add end RESD-3595 2020-09-18
	// FDMM add start RESD-3594 2020-09-22
	@XmlElement(name = "CountLimitFlag")
	private String CountLimitFlag;

	@XmlElement(name = "CountLimit")
	private String CountLimit;

	@ApiModelProperty(value="�̔�����������t���O", notes="�̔�����������t���O")
	public String getCountLimitFlag() {
		return CountLimitFlag;
	}

	public void setCountLimitFlag(String CountLimitFlag) {
		this.CountLimitFlag = CountLimitFlag;
	}
	
	@ApiModelProperty(value="�̔������", notes="�̔������")
	public String getCountLimit() {
		return CountLimit;
	}

	public void setCountLimit(String CountLimit) {
		this.CountLimit = CountLimit;
	}
	// FDMM add end RESD-3594 2020-09-22
	// FDMM add start RESD-3601 2020-09-28
	@XmlElement(name = "TransferWriteType")
	private String TransferWriteType;

	@XmlElement(name = "TransferActType")
	private String TransferActType;

	@ApiModelProperty(value="���n���L���敪", notes="���n���L���敪")
	public String getTransferWriteType() {
		return TransferWriteType;
	}

	public void setTransferWriteType(String TransferWriteType) {
		this.TransferWriteType = TransferWriteType;
	}
	
	@ApiModelProperty(value="���n���L������敪", notes="���n���L������敪")
	public String getTransferActType() {
		return TransferActType;
	}

	public void setTransferActType(String TransferActType) {
		this.TransferActType = TransferActType;
	}
	// FDMM add end RESD-3601 2020-09-28
	// FDMM add start RESD-3582 2020-09-30
	@XmlElement(name = "CertificatePrintFlag")
	private String CertificatePrintFlag;

	@ApiModelProperty(value="�������グ�ؖ����t���O", notes="�������グ�ؖ����t���O")
	public String getCertificatePrintFlag() {
		return CertificatePrintFlag;
	}

	public void setCertificatePrintFlag(String CertificatePrintFlag) {
		this.CertificatePrintFlag = CertificatePrintFlag;
	}
	// FDMM add end RESD-3582 2020-09-30
	// FDMM add start RESD-4596 2020-10-08
	@XmlElement(name = "SelfSaleRestrictedFlag")
	private String SelfSaleRestrictedFlag;

	@ApiModelProperty(value="�Z���t�̔��s���iFlag", notes="�Z���t�̔��s���iFlag")
	public String getSelfSaleRestrictedFlag() {
		return SelfSaleRestrictedFlag;
	}

	public void setSelfSaleRestrictedFlag(String selfSaleRestrictedFlag) {
		SelfSaleRestrictedFlag = selfSaleRestrictedFlag;
	}
	// FDMM add end RESD-4596 2020-10-08
	// FDMM add start RESD-3831 2020-10-12
	@XmlElement(name = "SaleRestrictedFlag")
	private String SaleRestrictedFlag;

	@ApiModelProperty(value="�̔��s���iFlag", notes="�̔��s���iFlag")
	public String getSaleRestrictedFlag() {
		return SaleRestrictedFlag;
	}

	public void setSaleRestrictedFlag(String saleRestrictedFlag) {
		SaleRestrictedFlag = saleRestrictedFlag;
	}
	// FDMM add end RESD-3831 2020-10-12
	 
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
		this.mdName = item.getMdName();
        this.mdKanaName = item.getMdKanaName();
        this.salesPrice2 =item.getSalesPrice2();
        this.paymentType = item.getPaymentType();
        this.subCode1 = item.getSubCode1();
        this.subCode2 = item.getSubCode2();
        this.subCode3 = item.getSubCode3();
        this.subNum2 = item.getSubNum2();
        //end
        // FDMM add start by mt185204 2020-07-15
        this.PharmaceuticalFlag = item.getPharmaceuticalFlag();
        // FDMM add end by mt185204 2020-07-15
        // FDMM add start RESD-3824 2020-08-18
        this.ageRestrictedFlag = item.getAgeRestrictedFlag();
        this.ClsAgeRestrictedFlag = item.getClsAgeRestrictedFlag();
        this.LineAgeRestrictedFlag = item.getLineAgeRestrictedFlag();
        this.DptAgeRestrictedFlag = item.getDptAgeRestrictedFlag();
        // FDMM add end RESD-3824 2020-08-18
        // FDMM add start RESD-3584 2020-08-19
        this.SelfFlag = item.getSelfFlag();
        this.SelfMedicationMark = item.getSelfMedicationMark();
        // FDMM add end RESD-3584 2020-08-19
        // FDMM add start RESD-3589 2020-09-8
        this.RecallFlag = item.getRecallFlag();
        this.CallInReason = item.getCallInReason();
        // FDMM add end RESD-3589 2020-09-8
        // FDMM add start RESD-3595 2020-09-18
        this.DrugActType = item.getDrugActType();
        this.DrugType = item.getDrugType();
        // FDMM add end RESD-3595 2020-09-18
        // FDMM add start RESD-3594 2020-09-22
        this.CountLimit = item.getCountLimit();
        this.CountLimitFlag = item.getCountLimitFlag();
        // FDMM add end RESD-3594 2020-09-22
        // FDMM add start RESD-3601 2020-09-28
        this.TransferWriteType = item.getTransferWriteType();
        this.TransferActType = item.getTransferActType();
        // FDMM add end RESD-3601 2020-09-28
        // FDMM add start RESD-3582 2020-09-30
        this.CertificatePrintFlag = item.getCertificatePrintFlag();
        // FDMM add end RESD-3582 2020-09-30
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

    @ApiModelProperty(value="�W���P��", notes="�W���P��")
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

    @ApiModelProperty(value="�������t���O", notes="�������t���O")
    public final boolean getDiscountable() {
        return this.discountable;
    }

    public final void setDiscountable(final boolean discountable) {
        this.discountable = discountable;
    }

    @ApiModelProperty(value="�f�t�H���g�Ŏg���ŗ�", notes="�f�t�H���g�Ŏg���ŗ�")
    public final DefaultTaxRate getDefaultTaxRate() {
        return this.defaultTaxRate;
    }

    public final void setDefaultTaxRate(final DefaultTaxRate defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }

    @ApiModelProperty(value="�ύX�\�Ȑŗ�", notes="�ύX�\�Ȑŗ�")
    public final ChangeableTaxRate getChangeableTaxRate() {
        return this.changeableTaxRate;
    }

    public final void setChangeableTaxRate(final ChangeableTaxRate changeableTaxRate) {
        this.changeableTaxRate = changeableTaxRate;
    }

    @ApiModelProperty(value="�ŗ��敪���擾�����}�X�^�[�e�[�u���̎��ʎq", notes="�ŗ��敪���擾�����}�X�^�[�e�[�u���̎��ʎq")
    public final String getTaxSource() {
        return this.taxSource;
    }

    public final void setTaxSource(final String taxSource) {
        this.taxSource = taxSource;
    }

    @ApiModelProperty(value="�ŗ��敪", notes="�ŗ��敪")
    public final Integer getTaxId() {
        return this.taxId;
    }

    public final void setTaxId(final Integer taxId) {
        this.taxId = taxId;
    }
    
    @ApiModelProperty(value="���ŗ��敪", notes="���ŗ��敪")
    public final int getOriginalTaxId() {
        return this.originalTaxId;
    }

    public final void setOriginalTaxId(final int originalTaxId) {
        this.originalTaxId = originalTaxId;
    }

    @ApiModelProperty(value="����ŗ�", notes="����ŗ�")
    public final int getTaxRate() {
        return taxRate;
    }

    public final void setTaxRate(final int taxRate) {
        this.taxRate = taxRate;
    }

    @ApiModelProperty(value="�ېŋ敪", notes="�ېŋ敪")
    public final String getTaxType() {
        return this.taxType;
    }

    public final void setTaxType(final String taxType) {
        this.taxType = taxType;
    }

    @ApiModelProperty(value="���i�ېŋ敪", notes="���i�ېŋ敪")
    public final String getPluTaxType() {
        return this.pluTaxType;
    }

    public final void setPluTaxType(final String pluTaxType) {
        this.pluTaxType = pluTaxType;
    }

    @ApiModelProperty(value="����ېŋ敪", notes="����ېŋ敪")
    public final String getDptTaxType() {
        return this.dptTaxType;
    }

    public final void setDptTaxType(final String dptTaxType) {
        this.dptTaxType = dptTaxType;
    }
    
    @ApiModelProperty(value="�N���X�ېŋ敪", notes="�N���X�ېŋ敪")
    public final String getClsTaxType() {
        return this.clsTaxType;
    }

    public final void setClsTaxType(final String clsTaxType) {
        this.clsTaxType = clsTaxType;
    }
    
    @ApiModelProperty(value="�����މېŋ敪", notes="�����މېŋ敪")
    public final String getLineTaxType() {
        return this.lineTaxType;
    }

    public final void setLineTaxType(final String lineTaxType) {
        this.lineTaxType = lineTaxType;
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
        return categoryCode;
    }

    public final void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }
    
    @ApiModelProperty(value="�A�x�����i", notes="�A�x�����i")
    public final Double getLabelPrice() {
        return labelPrice;
    }

    public final void setLabelPrice(Double labelPrice) {
        this.labelPrice = labelPrice;
    }

    @ApiModelProperty(value="�G���R�[�h", notes="�G���R�[�h")
    public final String getMagazineCode() {
        return magazineCode;
    }

    public final void setMagazineCode(String magazineCode) {
        this.magazineCode = magazineCode;
    }
    
    @ApiModelProperty(value="�o�ŎЃR�[�h", notes="�o�ŎЃR�[�h")
    public final String getPublishingCode() {
        return publishingCode;
    }

    public final void setPublishingCode(String publishingCode) {
        this.publishingCode = publishingCode;
    }

    @XmlElement(name = "PointAddFlag")
    private String pointAddFlag;

    @XmlElement(name = "PointUseFlag")
    private String pointUseFlag;

    @XmlElement(name = "TaxExemptFlag")
    private String taxExemptFlag;

    @XmlElement(name = "SaleSizeCode")
    private String saleSizeCode;

    @XmlElement(name = "SizePatternId")
    private String sizePatternId;

    @XmlElement(name ="BrandSaleName")
    private String brandSaleName;

    @ApiModelProperty(value="�|�C���g�t�^�Ώۃt���O", notes="�|�C���g�t�^�Ώۃt���O")
    public final String getPointAddFlag() {
        return pointAddFlag;
    }

    public final void setPointAddFlag(String pointAddFlag) {
        this.pointAddFlag = pointAddFlag;
    }

    @ApiModelProperty(value="�|�C���g���p�Ώۃt���O", notes="�|�C���g���p�Ώۃt���O")
    public final String getPointUseFlag() {
        return pointUseFlag;
    }

    public final void setPointUseFlag(String pointUseFlag) {
        this.pointUseFlag = pointUseFlag;
    }

    @ApiModelProperty(value="�ƐőΏۃt���O", notes="�ƐőΏۃt���O")
    public final String getTaxExemptFlag() {
        return taxExemptFlag;
    }

    public final void setTaxExemptFlag(String taxExemptFlag) {
        this.taxExemptFlag = taxExemptFlag;
    }

    @ApiModelProperty(value="�R�[�h�T�C�Y", notes="�R�[�h�T�C�Y")
    public final String getSaleSizeCode() {
        return saleSizeCode;
    }

    public final void setSaleSizeCode(final String saleSizeCode) {
        this.saleSizeCode = saleSizeCode;
    }

    @ApiModelProperty(value="�R�[�h�T�C�YID", notes="�R�[�h�T�C�YID")
    public final String getSizePatternId() {
        return sizePatternId;
    }

    public final void setSizePatternId(final String sizePatternId) {
        this.sizePatternId = sizePatternId;
    }

    @ApiModelProperty(value="�u�����h����", notes="�u�����h����")
    public final String getBrandSaleName() {
		return brandSaleName;
	}

	public final void setBrandSaleName(String brandSaleName) {
		this.brandSaleName = brandSaleName;
	}
	
    @XmlElement(name = "clsDiscountType")
    private String clsDiscountType;
	
    @XmlElement(name = "lineDiscountType")
    private String lineDiscountType;
    
    @ApiModelProperty(value="�����ފ����^�C�v", notes="�����ފ����^�C�v")
    public String getLineDiscountType() {
        return lineDiscountType;
    }

    public void setLineDiscountType(String lineDiscountType) {
        this.lineDiscountType = lineDiscountType;
    }
    
    @ApiModelProperty(value="�N���X�����^�C�v", notes="�N���X�����^�C�v")
    public String getClsDiscountType() {
        return clsDiscountType;
    }

    public void setClsDiscountType(String clsDiscountType) {
        this.clsDiscountType = clsDiscountType;
    }

    @XmlElement(name = "pluSubNum1")
    private String pluSubNum1 = "";

    @ApiModelProperty(value="���i�}�X�^�̐ŗ��敪", notes="���i�}�X�^�̐ŗ��敪")
    public String getPluSubNum1() {
        return pluSubNum1;
    }

    public void setPluSubNum1(String pluSubNum1) {
        this.pluSubNum1 = pluSubNum1;
    }

    @XmlElement(name = "lineInfoSubNum1")
    private String lineInfoSubNum1 = "";

    @ApiModelProperty(value="�����ރ}�X�^�̐ŗ��敪", notes="�����ރ}�X�^�̐ŗ��敪")
    public String getLineInfoSubNum1() {
        return lineInfoSubNum1;
    }

    public void setLineInfoSubNum1(String lineInfoSubNum1) {
        this.lineInfoSubNum1 = lineInfoSubNum1;
    }

    @XmlElement(name = "dptSubNum5")
    private String dptSubNum5 = "";

    @ApiModelProperty(value="�啪�ރ}�X�^�̐ŗ��敪", notes="�啪�ރ}�X�^�̐ŗ��敪")
    public String getDptSubNum5() {
        return dptSubNum5;
    }

    public void setDptSubNum5(String dptSubNum5) {
        this.dptSubNum5 = dptSubNum5;
    }

    @XmlElement(name = "classInfoSubNum1")
    private String classInfoSubNum1 = "";

    @ApiModelProperty(value="�����ރ}�X�^�̐ŗ��敪", notes="�����ރ}�X�^�̐ŗ��敪")
    public String getClassInfoSubNum1() {
        return classInfoSubNum1;
    }

    public void setClassInfoSubNum1(String classInfoSubNum1) {
        this.classInfoSubNum1 = classInfoSubNum1;
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

	public List<? extends PMInfo> getPmInfoList() {
		return pmInfoList;
	}

	public void setPmInfoList(List<? extends PMInfo> pmInfoList) {
		this.pmInfoList = pmInfoList;
	}

}
