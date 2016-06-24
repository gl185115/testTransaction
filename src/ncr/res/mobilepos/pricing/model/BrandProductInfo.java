package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * BrandProduct Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BrandProduct")
@ApiModel(value="BrandProductInfo")
public class BrandProductInfo{
	
    @XmlElement(name = "Line")
    private String line;
    
	@XmlElement(name = "BrandName")
    private String brandName;

	@XmlElement(name = "ProductNum")
	private String productNum;
	
	@XmlElement(name = "MdShortName")
	private String mdShortName;
	
	@XmlElement(name = "SalesPrice")
	private double salesPrice;
	
	@XmlElement(name = "ActualSalesPrice")
	private double actualSalesPrice;
	
	@XmlElement(name = "TaxType")
    private int taxType;
	
	@XmlElement(name = "TaxRate")
	private int taxRate;
	
	@XmlElement(name = "DiscountType")
	private int discountType;
	
	@XmlElement(name = "DiscountFlag")
	private int discountFlag;
	
	@XmlElement(name = "DiscountRate")
	private double discountRate;
	
	@XmlElement(name = "DiscountAmt")
	private double discountAmt;
	
	@XmlElement(name = "RowCount")
	private int rowCount;
	
	
	/**
     * @return the line
     */
    @ApiModelProperty(value="品種コード(中分類コード)", notes="品種コード(中分類コード)")
    public final String getLine() {
        return line;
    }

    /**
     * @param line the line to set
     */
    public final void setLine(String line) {
        this.line = line;
    }

    /**
	 * @return the brandName
	 */
    @ApiModelProperty(value="ブランド名称", notes="ブランド名称")
	public final String getBrandName() {
		return brandName;
	}

	/**
	 * @param brandName the brandName to set
	 */
	public final void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	/**
	 * @return the productNum
	 */
    @ApiModelProperty(value="製品数量", notes="製品数量")
	public final String getProductNum() {
		return productNum;
	}

	/**
	 * @param productNum the productNum to set
	 */
	public final void setProductNum(String productNum) {
		this.productNum = productNum;
	}

	/**
	 * @return the mdShortName
	 */
    @ApiModelProperty(value="レシート用商品名", notes="レシート用商品名")
	public final String getMdShortName() {
		return mdShortName;
	}

	/**
	 * @param mdShortName the mdShortName to set
	 */
	public final void setMdShortName(String mdShortName) {
		this.mdShortName = mdShortName;
	}

	/**
	 * @return the salesPrice
	 */
    @ApiModelProperty(value="販売価格", notes="販売価格")
	public final double getSalesPrice() {
		return salesPrice;
	}

	/**
	 * @param salesPrice the salesPrice to set
	 */
	public final void setSalesPrice(double salesPrice) {
		this.salesPrice = salesPrice;
	}

	/**
	 * @return the taxType
	 */
    @ApiModelProperty(value="課税区分", notes="課税区分")
	public final int getTaxType() {
		return taxType;
	}

	/**
	 * @param taxType the taxType to set
	 */
	public final void setTaxType(int taxType) {
		this.taxType = taxType;
	}

	/**
	 * @return the taxRate
	 */
    @ApiModelProperty(value="消費税率", notes="消費税率")
	public final int getTaxRate() {
		return taxRate;
	}

	/**
	 * @param taxRate the taxRate to set
	 */
	public final void setTaxRate(int taxRate) {
		this.taxRate = taxRate;
	}

	/**
	 * @return the rowCount
	 */
    @ApiModelProperty(value="行数", notes="行数")
	public final int getRowCount() {
		return rowCount;
	}

	/**
	 * @param rowCount the rowCount to set
	 */
	public final void setRowCount(int rowCount) {
		this.rowCount = rowCount;
	}

	/**
     * @return the discountType
     */
    @ApiModelProperty(value="割引種別", notes="割引種別")
    public final int getDiscountType() {
        return discountType;
    }

    /**
     * @param discountType the discountType to set
     */
    public final void setDiscountType(int discountType) {
        this.discountType = discountType;
    }

    /**
     * @return the discountFlag
     */
    @ApiModelProperty(value="自動値引フラグ", notes="自動値引フラグ")
    public final int getDiscountFlag() {
        return discountFlag;
    }

    /**
     * @param discountFlag the discountFlag to set
     */
    public final void setDiscountFlag(int discountFlag) {
        this.discountFlag = discountFlag;
    }

    /**
     * @return the discountRate
     */
    @ApiModelProperty(value="自動値引率", notes="自動値引率")
    public final double getDiscountRate() {
        return discountRate;
    }

    /**
     * @param discountRate the discountRate to set
     */
    public final void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }

    /**
     * @return the discountAmt
     */
    @ApiModelProperty(value="自動値引金額", notes="自動値引金額")
    public final double getDiscountAmt() {
        return discountAmt;
    }

    /**
     * @param discountAmt the discountAmt to set
     */
    public final void setDiscountAmt(double discountAmt) {
        this.discountAmt = discountAmt;
    }

    /**
     * @return the actualSalesPrice
     */
    @ApiModelProperty(value="実際の販売価格", notes="実際の販売価格")
    public final double getActualSalesPrice() {
        return actualSalesPrice;
    }

    /**
     * @param actualSalesPrice the actualSalesPrice to set
     */
    public final void setActualSalesPrice(double actualSalesPrice) {
        this.actualSalesPrice = actualSalesPrice;
    }

    @Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String crlf = "\r\n";
		
		str.append("BrandName : ").append(this.brandName).append(crlf)
		   .append("ProductNum : ").append(this.productNum).append(crlf)
		   .append("MdShortName : ").append(this.mdShortName).append(crlf)
		   .append("SalesPrice : ").append(this.salesPrice).append(crlf)
		   .append("ActualSalesPrice : ").append(this.actualSalesPrice).append(crlf)
		   .append("TaxType : ").append(this.taxType).append(crlf)
		   .append("TaxRate : ").append(this.taxRate).append(crlf)
		   .append("DiscountType : ").append(this.discountType).append(crlf)
		   .append("DiscountFlag : ").append(this.discountFlag).append(crlf)
		   .append("DiscountRate : ").append(this.discountRate).append(crlf)
		   .append("DiscountAmt : ").append(this.discountAmt).append(crlf)
		   .append("RowCount : ").append(this.rowCount).append(crlf);
		   
		return str.toString();
	}
}
