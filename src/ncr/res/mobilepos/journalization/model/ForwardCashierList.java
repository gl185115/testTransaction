package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "ForwardCashierList")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardCashierList")
public class ForwardCashierList extends ResultBase {

    @XmlElementWrapper(name = "ForwardCashierListInfo")
    @XmlElementRef()
    private List<ForwardCashierListInfo> ForwardCashierListInfo;

    @XmlElement(name = "Count")
    private Integer Count;

    @ApiModelProperty(value="順リスト情報", notes="順リスト情報")
    public List<ForwardCashierListInfo> getForwardCashierListInfo() {
		return ForwardCashierListInfo;
	}

	public void setForwardCashierListInfo(List<ForwardCashierListInfo> forwardCashierListInfo) {
		ForwardCashierListInfo = forwardCashierListInfo;
	}

    @ApiModelProperty(value="精算機の台数", notes="精算機の台数")
	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}
}
