package ncr.res.mobilepos.forwarditemlist.model;

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

@XmlRootElement(name = "ForwardvoidList")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardvoidList")
public class ForwardvoidList extends ResultBase {

	@XmlElementWrapper(name = "ForwardvoidListInfo")
    @XmlElementRef()
    private List<ForwardvoidListInfo> ForwardvoidListInfo;

    @XmlElement(name = "Count")
    private Integer Count;

    @ApiModelProperty(value="�ďo����f�[�^�̃��X�g���", notes="�ďo����f�[�^�̃��X�g���")
    public List<ForwardvoidListInfo> getForwardvoidListInfo() {
		return ForwardvoidListInfo;
	}

	public void setForwardvoidListInfo(List<ForwardvoidListInfo> forwardvoidListInfo) {
		ForwardvoidListInfo = forwardvoidListInfo;
	}

    @ApiModelProperty(value="�ďo����f�[�^�̃J�E���g", notes="�ďo����f�[�^�̃J�E���g")
	public Integer getCount() {
		return Count;
	}

	public void setCount(Integer count) {
		Count = count;
	}
}
