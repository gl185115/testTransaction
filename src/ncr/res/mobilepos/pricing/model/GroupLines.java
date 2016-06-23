package ncr.res.mobilepos.pricing.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GroupLines")
@ApiModel(value="GroupLines")
public class GroupLines extends ResultBase{

	@XmlElement(name = "GroupLineList")
	private List<GroupLineInfo> groupLineList;

	/**
	 * @return the groupLineList
	 */
	@ApiModelProperty(value="�O���[�v���X�g", notes="�O���[�v���X�g")
	public final List<GroupLineInfo> getGroupLineList() {
		return groupLineList;
	}

	/**
	 * @param groupLineList the groupLineList to set
	 */
	public final void setGroupLineList(List<GroupLineInfo> groupLineList) {
		this.groupLineList = groupLineList;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		String crlf = "\r\n";

		if(null != this.groupLineList){
			for(GroupLineInfo groupLineInfo : groupLineList){
				 str.append(crlf)
                 .append("GroupLineInfo : ").append(groupLineInfo.toString());
			}
		}
		return str.toString();
	}

}
