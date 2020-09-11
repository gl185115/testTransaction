package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PricePromInfo")
@ApiModel(value="PricePromInfo")
public class PricePromInfo {

	@XmlElement(name = "promotionNo")
    private String promotionNo;

	@XmlElement(name = "promotionName")
    private String promotionName;

	@XmlElement(name = "promotionType")
    private String promotionType;

	@XmlElement(name = "brandFlag")
    private int brandFlag;

	@XmlElement(name = "dpt")
    private String dpt;

	@XmlElement(name = "line")
    private String line;

	@XmlElement(name = "class")
    private String clas;

	@XmlElement(name = "sku")
    private String sku;

	@XmlElement(name = "discountClass")
    private String discountClass;

	@XmlElement(name = "diacountRate")
    private double diacountRate;

	@XmlElement(name = "discountAmt")
    private long discountAmt;

	@XmlElement(name = "brandId")
    private String brandId;

	@XmlElement(name = "SubNum1")
	private int SubNum1;
	
	@XmlElement(name = "SubCode2")
	private int subCode2;
	
	@XmlElement(name = "salesPrice")
    private long salesPrice;

	@ApiModelProperty(value="企画No", notes="企画No")
    public String getPromotionNo() {
        return promotionNo;
    }

    public void setPromotionNo(String promotionNo) {
        this.promotionNo = promotionNo;
    }

	@ApiModelProperty(value="企画名称", notes="企画名称")
    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }
	@ApiModelProperty(value="設定区分", notes="設定区分")
    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
    @ApiModelProperty(value="ブランド設定フラグ", notes="ブランド設定フラグ")
    public int getBrandFlag() {
        return brandFlag;
    }

    public void setBrandFlag(int brandFlag) {
        this.brandFlag = brandFlag;
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
    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

	@ApiModelProperty(value="商品コード", notes="商品コード")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

	@ApiModelProperty(value="割引区分", notes="割引区分")
    public String getDiscountClass() {
        return discountClass;
    }

    public void setDiscountClass(String discountClass) {
        this.discountClass = discountClass;
    }

	@ApiModelProperty(value="割引率", notes="割引率")
    public double getDiscountRate() {
        return diacountRate;
    }

    public void setDiscountRate(double diacountRate) {
        this.diacountRate = diacountRate;
    }

	@ApiModelProperty(value="値引額", notes="値引額")
    public long getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(long discountAmt) {
        this.discountAmt = discountAmt;
    }

    @ApiModelProperty(value="売単価", notes="売単価")
	public long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(long salesPrice) {
		this.salesPrice = salesPrice;
	}

	@ApiModelProperty(value="ブランドコード", notes="ブランドコード")
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

	public int getSubNum1() {
		return SubNum1;
	}

	public void setSubNum1(int subNum1) {
		SubNum1 = subNum1;
	}

	public int getSubCode2() {
		return subCode2;
	}

	public void setSubCode2(int subCode2) {
		this.subCode2 = subCode2;
	}
}

