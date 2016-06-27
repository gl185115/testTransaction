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

    @XmlElement(name = "PromotionId")
    private String promotionId;
    
    @XmlElement(name = "PromotionName")
    private String promotionName;
    
    @XmlElement(name = "MinimumPrice")
    private Double minimumPrice;
    
    @XmlElement(name = "OutputTargetValue")
    private String outputTargetValue;
    
    @XmlElement(name = "BMPFileName")
    private String bmpFileName;
    
    @XmlElement(name = "BMPFileFlag")
    private String bmpFileFlag;
    
    @XmlElement(name = "BMPFileCount")
    private String bmpFileCount;
    
    @XmlElement(name = "OutputType")
    private String outputType;

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

}
