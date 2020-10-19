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
@ApiModel(value="ForwardCheckerList")
public class ForwardCheckerList extends ResultBase {

    @XmlElementWrapper(name = "ForwardCheckerListInfo")
    @XmlElementRef()
    private List<ForwardCheckerListInfo> ForwardCheckerListInfo;

    @XmlElement(name = "Count")
    private Integer Count;

    @ApiModelProperty(value="�����X�g���", notes="�����X�g���")
    public List<ForwardCheckerListInfo> getForwardCheckerListInfo() {
		return ForwardCheckerListInfo;
	}

	public void setForwardCheckerListInfo(List<ForwardCheckerListInfo> forwardCheckerListInfo) {
		ForwardCheckerListInfo = forwardCheckerListInfo;
	}

    @ApiModelProperty(value="���i�o�^�@�̑䐔", notes="���i�o�^�@�̑䐔")
	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}
}
