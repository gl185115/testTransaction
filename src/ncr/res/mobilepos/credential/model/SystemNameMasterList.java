package ncr.res.mobilepos.credential.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SystemNameMasterList")
@ApiModel(value="SystemNameMasterList")
public class SystemNameMasterList extends ResultBase{
	/** The system name master list. */
	@XmlElement(name = "systemNameMasterList")
	private List<NameMasterInfo> namemasterinfoList = null;

	/**
	 * Gets the system name list.
	 *
	 * @return the system name list
	 */
	@ApiModelProperty( value="システム名称リスト", notes="システム名称リスト")
	public List<NameMasterInfo> getNamemasterinfo() {
		return namemasterinfoList;
	}

	/**
	 * Sets the system name list.
	 *
	 * @param namemasterinfo
	 *            namemasterinfo
	 */
	public void setNamemasterinfo(List<NameMasterInfo> namemasterinfo) {
		this.namemasterinfoList = namemasterinfo;
	}

	@Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("systemNameMasterList: ").append(namemasterinfoList).append("; ");
        return sb.toString();
	}
}
