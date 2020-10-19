package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "plu_info")
@ApiModel(value="plu_info")
public class UrgentChangeItemInfo extends ResultBase {

    /**
     * 商品名.
     */
    @XmlElement(name = "mdname")
    private String mdNameLocal;

    /**
     * 現在売価.
     */
    @XmlElement(name = "current_salesprice")
    private Double urgentPrice;
    
    /**
     * 標準売価.
     */
    @XmlElement(name = "salesprice")
    private Double salesPrice1;
    
    /**
     * 部門.
     */
    @XmlElement(name = "dpt")
    private String dptCode;
    
    /**
     * 中分類.
     */
    @XmlElement(name = "class")
    private String classCode;
    
    /**
     * 小分類.
     */
    @XmlElement(name = "category")
    private String categoryCode;
    
    @ApiModelProperty(value="商品名", notes="商品名")
    public final String getmdName() {
        return mdNameLocal;
    }

    public final void setmdName(final String mdNameToSet) {
        this.mdNameLocal = mdNameToSet;
    }

    @ApiModelProperty(value="現在売価", notes="現在売価")
    public final Double getUrgentPrice(){
        return this.urgentPrice;
    }
    
    public final void setUrgentPrice(final Double UrgentPriceToSet){
        this.urgentPrice = UrgentPriceToSet;
    }
    
    @ApiModelProperty(value="標準売価", notes="標準売価")
    public final Double getSalesPrice1(){
        return this.salesPrice1;
    }
    
    public final void setSalesPrice1(final Double SalesPrice1ToSet){
        this.salesPrice1 = SalesPrice1ToSet;
    } 
    
    @ApiModelProperty(value="部門", notes="部門")
    public final String getDptCode() {
        return this.dptCode;
    }

    public final void setDptCode(final String dptCodeToSet) {
        this.dptCode = dptCodeToSet;
    }
    
    @ApiModelProperty(value="中分類", notes="中分類")
    public final String getClassCode() {
        return this.classCode;
    }

    public final void setClassCode(final String classCodeToSet) {
        this.classCode = classCodeToSet;
    }
    
    @ApiModelProperty(value="小分類", notes="小分類")
    public final String getCategoryCode() {
        return this.categoryCode;
    }

    public final void setCategoryCode(final String categoryCodeToSet) {
        this.categoryCode = categoryCodeToSet;
    }
    
    @Override
    public final String toString() {
		StringBuilder str = new StringBuilder();
		String crlf = "\r\n";
		
		str.append("mdNameLocal : ").append(this.mdNameLocal).append(crlf)
		   .append("urgentPrice : ").append(this.urgentPrice).append(crlf)
		   .append("salesPrice1 : ").append(this.salesPrice1).append(crlf)
		   .append("dptCode : ").append(this.dptCode).append(crlf)
		   .append("classCode : ").append(this.classCode).append(crlf)
		   .append("categoryCode : ").append(this.categoryCode).append(crlf);
		   
		return str.toString();
    }

}
