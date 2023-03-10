package ncr.res.mobilepos.journalization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
@XmlRootElement(name = "GoldCertificate")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="GoldCertificate")
public class GoldCertificate extends ResultBase {
	
    @XmlElement(name = "GoldCertificateInfo")
    private List<GoldCertificateInfo> goldCertificateInfo;

	/**
	 * @return the goldCertificateInfo
	 */
    @ApiModelProperty(value="金券情報", notes="金券情報")
	public final List<GoldCertificateInfo> getGoldCertificateInfo() {
		return goldCertificateInfo;
	}

	/**
	 * @param goldCertificateInfo the goldCertificateInfo to set
	 */
	public final void setGoldCertificateInfo(
			List<GoldCertificateInfo> goldCertificateInfo) {
		this.goldCertificateInfo = goldCertificateInfo;
	}

	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());
		
		if(null != this.goldCertificateInfo){
			sb.append(crlf).append("GoldCertificateInfo: ").append(this.goldCertificateInfo.toString());
		}
		
		return sb.toString();
	}
	
    
}
