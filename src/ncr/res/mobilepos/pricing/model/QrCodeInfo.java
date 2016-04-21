package ncr.res.mobilepos.pricing.model;

public class QrCodeInfo {

    private String promotionId;
    private String promotionName;
    private Double minimumPrice;
    private String outputTargetValue;
    private String bmpFileName;
    private String bmpFileFlag;
    private String bmpFileCount;
    private String outputType;

    public String getOutputType() {
        return outputType;
    }

    public void setOutputType(String outputType) {
        this.outputType = outputType;
    }

    public String getPromotionId() {
        return promotionId;
    }

    public void setPromotionId(String promotionId) {
        this.promotionId = promotionId;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public Double getMinimumPrice() {
        return minimumPrice;
    }

    public void setMinimumPrice(Double minimumPrice) {
        this.minimumPrice = minimumPrice;
    }

    public String getOutputTargetValue() {
        return outputTargetValue;
    }

    public void setOutputTargetValue(String outputTargetValue) {
        this.outputTargetValue = outputTargetValue;
    }

    public String getBmpFileName() {
        return bmpFileName;
    }

    public void setBmpFileName(String bmpFileName) {
        this.bmpFileName = bmpFileName;
    }

    public String getBmpFileFlag() {
        return bmpFileFlag;
    }

    public void setBmpFileFlag(String bmpFileFlag) {
        this.bmpFileFlag = bmpFileFlag;
    }

    public String getBmpFileCount() {
        return bmpFileCount;
    }

    public void setBmpFileCount(String bmpFileCount) {
        this.bmpFileCount = bmpFileCount;
    }

}
