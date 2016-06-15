package ncr.res.mobilepos.nationalityinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "NationalityInfoList")
@ApiModel(value="NationalityInfoList")
public class NationalityInfoList extends ResultBase {
	@XmlElement(name = "NationalityInfoList")
	private List<NationalityInfo> nationalityInfoList;
   
	@ApiModelProperty( value="çëê–èÓïÒ", notes="çëê–èÓïÒ")
	public final List<NationalityInfo> getNationalityInfoList() {
		return nationalityInfoList;
	}

	public final void setNationalityInfoList(List<NationalityInfo> nationalityInfoList) {
		this.nationalityInfoList = nationalityInfoList;
	}

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.toString()).append("; ");
		sb.append("NationalityInfoList: ").append(nationalityInfoList).append("; ");
		return sb.toString();
	}
}
