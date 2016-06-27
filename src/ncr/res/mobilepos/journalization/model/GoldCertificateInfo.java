package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name="GoldCertificateInfo")
@ApiModel(value="GoldCertificateInfo")
public class GoldCertificateInfo{
	
	@XmlElement(name="CrCompCode")
    private String CrCompCode;
	
	@XmlElement(name="CrCompName")
    private String CrCompName;
	
	@XmlElement(name="CrCompKanaName")
    private String CrCompKanaName;
	
	@XmlElement(name="Subcode1")
    private String Subcode1;

	/**
	 * @return the crCompCode
	 */
	@ApiModelProperty(value="認証会社コード", notes="認証会社コード")
	public final String getCrCompCode() {
		return CrCompCode;
	}

	/**
	 * @param crCompCode the crCompCode to set
	 */
	public final void setCrCompCode(String crCompCode) {
		CrCompCode = crCompCode;
	}

	/**
	 * @return the crCompName
	 */
	@ApiModelProperty(value="認証会社名", notes="認証会社名")
	public final String getCrCompName() {
		return CrCompName;
	}

	/**
	 * @param crCompName the crCompName to set
	 */
	public final void setCrCompName(String crCompName) {
		CrCompName = crCompName;
	}

	/**
	 * @return the crCompKanaName
	 */
	@ApiModelProperty(value="認証会社氏名", notes="認証会社氏名")
	public final String getCrCompKanaName() {
		return CrCompKanaName;
	}

	/**
	 * @param crCompKanaName the crCompKanaName to set
	 */
	public final void setCrCompKanaName(String crCompKanaName) {
		CrCompKanaName = crCompKanaName;
	}

	/**
	 * @return the subcode1
	 */
	@ApiModelProperty(value="属性コード1", notes="属性コード1")
	public final String getSubcode1() {
		return Subcode1;
	}

	/**
	 * @param subcode1 the subcode1 to set
	 */
	public final void setSubcode1(String subcode1) {
		Subcode1 = subcode1;
	}
	
	@Override
	public String toString() {
		
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		
		if(null != this.CrCompCode){
			sb.append("CrCompCode: ").append(this.CrCompCode.toString());
		}
		
		if(null != this.CrCompName){
			sb.append(crlf).append("CrCompName: ").append(this.CrCompName.toString());
		}
		
		if(null != this.CrCompKanaName){
			sb.append(crlf).append("CrCompKanaName: ").append(this.CrCompKanaName.toString());
		}
		
		if(null != this.Subcode1){
			sb.append(crlf).append("Subcode1: ").append(this.Subcode1.toString());
		}
		
		return sb.toString();
	}

}
