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
    
    @ApiModelProperty(value="�o�̓^�C�v", notes="�o�̓^�C�v")
    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    @ApiModelProperty(value="���R�[�h", notes="���R�[�h")
    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    @ApiModelProperty(value="��於��", notes="��於��")
    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @ApiModelProperty(value="�������z", notes="�������z")
    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    @ApiModelProperty(value="�o�͊�l", notes="�o�͊�l")
    public String getOutputTargetValue() {
        return outputTargetValue;
    }

    public void setOutputTargetValue(String outputTargetValue) {
        this.outputTargetValue = outputTargetValue;
    }

    @ApiModelProperty(value="bmp�t�@�C����", notes="bmp�t�@�C����")
    public String getBmpFileName() {
        return bmpFileName;
    }

    public void setBmpFileName(String bmpFileName) {
        this.bmpFileName = bmpFileName;
    }

    @ApiModelProperty(value="bmp�t�@�C���t���O", notes="bmp�t�@�C���t���O")
    public String getBmpFileFlag() {
        return bmpFileFlag;
    }

    public void setBmpFileFlag(String bmpFileFlag) {
        this.bmpFileFlag = bmpFileFlag;
    }

    @ApiModelProperty(value="bmp�t�@�C��������", notes="bmp�t�@�C��������")
    public String getBmpFileCount() {
        return bmpFileCount;
    }

    public void setBmpFileCount(String bmpFileCount) {
        this.bmpFileCount = bmpFileCount;
    }
    
    @ApiModelProperty(value="�o�͏�", notes="�o�͏�")
    public String getDisplayOrder() {
        return displayOrder;
    }

    public void setDisplayOrder(String displayOrder) {
        this.displayOrder = displayOrder;
    }

    @ApiModelProperty(value="�o�^���", notes="�o�^���")
	public String getPromotionType() {
		return promotionType;
	}

	public void setPromotionType(String promotionType) {
		this.promotionType = promotionType;
	}

	@ApiModelProperty(value="����R�[�h", notes="����R�[�h")
	public String getDpt() {
		return dpt;
	}

	public void setDpt(String dpt) {
		this.dpt = dpt;
	}

	@ApiModelProperty(value="�i��R�[�h", notes="�i��R�[�h")
	public String getLine() {
		return line;
	}

	public void setLine(String line) {
		this.line = line;
	}

	@ApiModelProperty(value="�N���X�R�[�h", notes="�N���X�R�[�h")
	public String getClassCode() {
		return classCode;
	}

	public void setClassCode(String classCode) {
		this.classCode = classCode;
	}

	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	@ApiModelProperty(value="�d����R�[�h", notes="�d����R�[�h")
	public String getConnCode() {
		return connCode;
	}

	public void setConnCode(String connCode) {
		this.connCode = connCode;
	}

	@ApiModelProperty(value="�u�����h�R�[�h", notes="�u�����h�R�[�h")
	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}

	@ApiModelProperty(value="��������N", notes="��������N")
	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
	}

	@ApiModelProperty(value="����ԍ��w��t���O", notes="����ԍ��w��t���O")
	public String getMemberTargetType() {
		return memberTargetType;
	}

	public void setMemberTargetType(String memberTargetType) {
		this.memberTargetType = memberTargetType;
	}

	@ApiModelProperty(value="����", notes="����")
	public String getSexType() {
		return sexType;
	}

	public void setSexType(String sexType) {
		this.sexType = sexType;
	}

	@ApiModelProperty(value="�a����", notes="�a����")
	public String getBirthMonth() {
		return birthMonth;
	}

	public void setBirthMonth(String birthMonth) {
		this.birthMonth = birthMonth;
	}

	@ApiModelProperty(value="�󎚖���", notes="�󎚖���")
	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@ApiModelProperty(value="�o�͊�l", notes="�o�͊�l")
	public String getTargetValue() {
		return targetValue;
	}

	public void setTargetValue(String targetValue) {
		this.targetValue = targetValue;
	}

	@ApiModelProperty(value="����ԍ�", notes="����ԍ�")
	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	
	@ApiModelProperty(value="�t�@�C�����݃t���O", notes="�t�@�C�����݃t���O")
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
