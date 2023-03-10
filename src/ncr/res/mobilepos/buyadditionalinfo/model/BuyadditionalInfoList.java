package ncr.res.mobilepos.buyadditionalinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Buyadditional")
@ApiModel(value="BuyadditionalInfoList")
public class BuyadditionalInfoList extends ResultBase {
	@XmlElement(name = "BuyadditionalInfoList")
	private List<BuyadditionalInfo> buyadditionalInfoList;
	
	@ApiModelProperty(value="?w???⏕???񃊃X?g", notes="?w???⏕???񃊃X?g")
	public final List<BuyadditionalInfo> getBuyadditionalInfoList() {
		return buyadditionalInfoList;
	}

	public final void setBuyadditionalInfoList(List<BuyadditionalInfo> buyadditionalInfoList) {
		this.buyadditionalInfoList = buyadditionalInfoList;
	}

	@Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("BuyadditionalInfoList: ").append(buyadditionalInfoList).append("; ");
        return sb.toString();
    }
}
