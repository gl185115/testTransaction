package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "QrCodeInfo")
@ApiModel(value="QrCodeInfo")
public class QrCodeInfo {

    @XmlElement(name = "promotionId")
    private String promotionId;
    
    @XmlElement(name = "promotionName")
    private String promotionName;
    
    @XmlElement(name = "minimumPrice")
    private Double minimumPrice;
    
    @XmlElement(name = "outputTargetValue")
    private String outputTargetValue;
    
    @XmlElement(name = "bmpFileName")
    private String bmpFileName;
    
    @XmlElement(name = "bmpFileFlag")
    private String bmpFileFlag;
    
    @XmlElement(name = "bmpFileCount")
    private String bmpFileCount;
    
    @XmlElement(name = "outputType")
    private String outputType;
    
    @XmlElement(name = "displayOrder")
    private String displayOrder;

    @XmlElement(name = "promotionType")
    private String promotionType;
    
    @XmlElement(name = "dpt")
    private String dpt;
    
    @XmlElement(name = "line")
    private String line;
    
    @XmlElement(name = "class")
    private String classCode;
    
    @XmlElement(name = "sku")
    private String sku;
    
    @XmlElement(name = "connCode")
    private String connCode;
    
    @XmlElement(name = "brandId")
    private String brandId;
    
    @XmlElement(name = "memberRank")
    private String memberRank;
    
    @XmlElement(name = "memberTargetType")
    private String memberTargetType;
    
    @XmlElement(name = "sexType")
    private String sexType;
    
    @XmlElement(name = "birthMonth")
    private String birthMonth;
    
    @XmlElement(name = "quantity")
    private int quantity;
    
    @XmlElement(name = "targetValue")
    private String targetValue;
    
    @XmlElement(name = "customerId")
    private String customerId;
    
    @XmlElement(name = "fileExist")
    private int fileExist;
    
    @ApiModelProperty(value="出力タイプ", notes="出力タイプ")
    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    @ApiModelProperty(value="企画コード", notes="企画コード")
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    @ApiModelProperty(value="企画名称", notes="企画名称")
    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @ApiModelProperty(value="下限金額", notes="下限金額")
    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    @ApiModelProperty(value="出力基準値", notes="出力基準値")
    public String getOutputTargetValue() {
        return outputTargetValue;
    }

    public void setOutputTargetValue(String outputTargetValue) {
        this.outputTargetValue = outputTargetValue;
    }

    @ApiModelProperty(value="bmpファイル名", notes="bmpファイル名")
    public String getBmpFileName() {
        return bmpFileName;
    }

    public void setBmpFileName(String bmpFileName) {
        this.bmpFileName = bmpFileName;
    }

    @ApiModelProperty(value="bmpファイルフラグ", notes="bmpファイルフラグ")
    public String getBmpFileFlag() {
        return bmpFileFlag;
    }

    public void setBmpFileFlag(String bmpFileFlag) {
        this.bmpFileFlag = bmpFileFlag;
    }

    @ApiModelProperty(value="bmpファイル分割数", notes="bmpファイル分割数")
    public String getBmpFileCount() {
        return bmpFileCount;
    }

    public void setBmpFileCount(String bmpFileCount) {
        this.bmpFileCount = bmpFileCount;
    }
    
    @ApiModelProperty(value="出力順", notes="出力順")
    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    @ApiModelProperty(value="登録種別", notes="登録種別")
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	@ApiModelProperty(value="部門コード", notes="部門コード")
	public String getDpt() {
		return dpt;
	}

	public void setDpt(String dpt) {
		this.dpt = dpt;
	}

	@ApiModelProperty(value="品種コード", notes="品種コード")
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	@ApiModelProperty(value="クラスコード", notes="クラスコード")
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@ApiModelProperty(value="商品コード", notes="商品コード")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@ApiModelProperty(value="仕入先コード", notes="仕入先コード")
	public String getConnCode() {
		return connCode;
	}

	public void setConnCode(String connCode) {
		this.connCode = connCode;
	}

	@ApiModelProperty(value="ブランドコード", notes="ブランドコード")
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	@ApiModelProperty(value="会員ランク", notes="会員ランク")
	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	@ApiModelProperty(value="会員番号指定フラグ", notes="会員番号指定フラグ")
	public String getMemberTargetType() {
		return memberTargetType;
	}

	public void setMemberTargetType(String memberTargetType) {
		this.memberTargetType = memberTargetType;
	}

	@ApiModelProperty(value="性別", notes="性別")
	public String getSexType() {
		return sexType;
	}

	public void setSexType(String sexType) {
		this.sexType = sexType;
	}

	@ApiModelProperty(value="誕生月", notes="誕生月")
	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	@ApiModelProperty(value="印字枚数", notes="印字枚数")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@ApiModelProperty(value="出力基準値", notes="出力基準値")
	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	@ApiModelProperty(value="会員番号", notes="会員番号")
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@ApiModelProperty(value="ファイル存在フラグ", notes="ファイル存在フラグ")
	public int getFileExist() {
		return fileExist;
	}

	public void setFileExist(int fileExist) {
		this.fileExist = fileExist;
	}
	
	public QrCodeInfo() {
	}

    //copy constructor
    public QrCodeInfo(final QrCodeInfo info) {
        this.promotionId = info.getPromotionId();
        this.promotionName = info.getPromotionName();
        this.minimumPrice = info.getMinimumPrice();
        this.outputTargetValue = info.getOutputTargetValue();
        this.bmpFileName = info.getBmpFileName();
        this.bmpFileFlag = info.getBmpFileFlag();
        this.bmpFileCount = info.getBmpFileCount();
        this.outputType = info.getOutputType();
        this.displayOrder = info.getDisplayOrder();
        this.promotionType = info.getPromotionType();
        this.dpt = info.getDpt();
        this.line = info.getLine();
        this.classCode = info.getClassCode();
        this.sku = info.getSku();
        this.connCode = info.getConnCode();
        this.brandId = info.getBrandId();
        this.memberRank = info.getMemberRank();
        this.memberTargetType = info.getMemberTargetType();
        this.sexType = info.getSexType();
        this.birthMonth = info.getBirthMonth();
        this.quantity = info.getQuantity();
        this.targetValue = info.getTargetValue();
        this.customerId = info.getCustomerId();
        this.fileExist = info.getFileExist();
    }	
}
