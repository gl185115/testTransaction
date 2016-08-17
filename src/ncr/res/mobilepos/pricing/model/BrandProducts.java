package ncr.res.mobilepos.pricing.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BrandProducts")
@ApiModel(value="BrandProducts")
public class BrandProducts extends ResultBase{

	@XmlElement(name = "BrandProductList")
	private List<BrandProductInfo> brandProductList;

	/**
	 * @return the brandProductList
	 */
    @ApiModelProperty(value="ブランド製品リスト", notes="ブランド製品リスト")
	public final List<BrandProductInfo> getBrandProductList() {
		return brandProductList;
	}

	/**
	 * @param brandProductList the brandProductList to set
	 */
	public final void setBrandProductList(List<BrandProductInfo> brandProductList) {
		this.brandProductList = brandProductList;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        
		if (null != this.brandProductList) {
			for (BrandProductInfo brandProductInfo : brandProductList) {
				str.append(crlf).append("BrandProductInfo : ").append(crlf)
						.append(brandProductInfo.toString());
			}
		}
        return str.toString();
	}
}
