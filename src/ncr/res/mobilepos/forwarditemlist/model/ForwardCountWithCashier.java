package ncr.res.mobilepos.forwarditemlist.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ForwardCountWithCashier")
@ApiModel(value="ForwardCountWithCashier")
public class ForwardCountWithCashier extends ResultBase {


	@XmlElement(name = "Count")
    private String Count;

	@ApiModelProperty( value="‘OJ•Û—¯Œ”", notes="‘OJ•Û—¯Œ”")
    public String getCount() {
		return Count;
	}

	public void setCount(String count) {
		Count = count;
	}
}
