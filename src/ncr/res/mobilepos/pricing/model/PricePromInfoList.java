package ncr.res.mobilepos.pricing.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Promotion Object.
 * The Promotion object holds the Mix and Match Discount(s) for a given
 * transaction.
 * @author cc185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PricePromInfoList")
@ApiModel(value="PricePromInfoList")
public class PricePromInfoList extends ResultBase {

    @XmlElement(name = "PricePromInfoList")
    private List<PricePromInfo> pricePromInfoList;

	public List<PricePromInfo> getPricePromInfoList() {
		return pricePromInfoList;
	}

	public void setPricePromInfoList(List<PricePromInfo> pricePromInfoList) {
		this.pricePromInfoList = pricePromInfoList;
	}

	@Override
	public String toString() {
		return "PricePromInfoList [pricePromInfoList=" + pricePromInfoList + "]";
	}
}
