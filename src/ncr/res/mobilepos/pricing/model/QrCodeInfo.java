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

}
