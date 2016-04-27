package ncr.res.mobilepos.uiconfig.model.schedule;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "CompanyInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class CompanyInfo extends ResultBase {
	
	@XmlElement(name = "CompanyId")
	private String CompanyId;
	
	@XmlElement(name = "CompanyName")
	private String CompanyName;
	
	@XmlElement(name = "CompanyKanaName")
	private String CompanyKanaName;
	
	@XmlElement(name = "CompanyShortName")
	private String CompanyShortName;
	
	@XmlElement(name = "CompanyShortKanaName")
	private String CompanyShortKanaName;
	
	@XmlElement(name = "DisplayOrder")
	private int DisplayOrder;
	
	@XmlElement(name = "SubCode1")
	private int SubCode1;
	
	@XmlElement(name = "SubCode2")
	private int SubCode2;
	
	@XmlElement(name = "SubCode3")
	private int SubCode3;
	
	@XmlElement(name = "SubCode4")
	private int SubCode4;
	
	@XmlElement(name = "SubCode5")
	private int SubCode5;
	
	@XmlElement(name = "DeleteFlag")
	private int DeleteFlag;
	
	@XmlElement(name = "DelAppId")
	private String DelAppId;

	/**
	 * @return the companyId
	 */
	public String getCompanyId() {
		return CompanyId;
	}

	/**
	 * @param companyId the companyId to set
	 */
	public void setCompanyId(String companyId) {
		CompanyId = companyId;
	}

	/**
	 * @return the companyName
	 */
	public String getCompanyName() {
		return CompanyName;
	}

	/**
	 * @param companyName the companyName to set
	 */
	public void setCompanyName(String companyName) {
		CompanyName = companyName;
	}

	/**
	 * @return the companyKanaName
	 */
	public String getCompanyKanaName() {
		return CompanyKanaName;
	}

	/**
	 * @param companyKanaName the companyKanaName to set
	 */
	public void setCompanyKanaName(String companyKanaName) {
		CompanyKanaName = companyKanaName;
	}

	/**
	 * @return the companyShortName
	 */
	public String getCompanyShortName() {
		return CompanyShortName;
	}

	/**
	 * @param companyShortName the companyShortName to set
	 */
	public void setCompanyShortName(String companyShortName) {
		CompanyShortName = companyShortName;
	}

	/**
	 * @return the companyShortKanaName
	 */
	public String getCompanyShortKanaName() {
		return CompanyShortKanaName;
	}

	/**
	 * @param companyShortKanaName the companyShortKanaName to set
	 */
	public void setCompanyShortKanaName(String companyShortKanaName) {
		CompanyShortKanaName = companyShortKanaName;
	}

	/**
	 * @return the displayOrder
	 */
	public int getDisplayOrder() {
		return DisplayOrder;
	}

	/**
	 * @param displayOrder the displayOrder to set
	 */
	public void setDisplayOrder(int displayOrder) {
		DisplayOrder = displayOrder;
	}

	/**
	 * @return the subCode1
	 */
	public int getSubCode1() {
		return SubCode1;
	}

	/**
	 * @param subCode1 the subCode1 to set
	 */
	public void setSubCode1(int subCode1) {
		SubCode1 = subCode1;
	}

	/**
	 * @return the subCode2
	 */
	public int getSubCode2() {
		return SubCode2;
	}

	/**
	 * @param subCode2 the subCode2 to set
	 */
	public void setSubCode2(int subCode2) {
		SubCode2 = subCode2;
	}

	/**
	 * @return the subCode3
	 */
	public int getSubCode3() {
		return SubCode3;
	}

	/**
	 * @param subCode3 the subCode3 to set
	 */
	public void setSubCode3(int subCode3) {
		SubCode3 = subCode3;
	}

	/**
	 * @return the subCode4
	 */
	public int getSubCode4() {
		return SubCode4;
	}

	/**
	 * @param subCode4 the subCode4 to set
	 */
	public void setSubCode4(int subCode4) {
		SubCode4 = subCode4;
	}

	/**
	 * @return the subCode5
	 */
	public int getSubCode5() {
		return SubCode5;
	}

	/**
	 * @param subCode5 the subCode5 to set
	 */
	public void setSubCode5(int subCode5) {
		SubCode5 = subCode5;
	}

	/**
	 * @return the deleteFlag
	 */
	public int getDeleteFlag() {
		return DeleteFlag;
	}

	/**
	 * @param deleteFlag the deleteFlag to set
	 */
	public void setDeleteFlag(int deleteFlag) {
		DeleteFlag = deleteFlag;
	}

	/**
	 * @return the delAppId
	 */
	public String getDelAppId() {
		return DelAppId;
	}

	/**
	 * @param delAppId the delAppId to set
	 */
	public void setDelAppId(String delAppId) {
		DelAppId = delAppId;
	}
	
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		if(null != this.CompanyId){
			sb.append("{CompanyId: ").append(this.CompanyId.toString());
		}
		if(null != this.CompanyName){
			sb.append(",CompanyName: ").append(this.CompanyName.toString());
		}
		if(null != this.CompanyKanaName){
			sb.append(",CompanyKanaName: ").append(this.CompanyKanaName.toString());
		}
		if(null != this.CompanyShortName){
			sb.append(",CompanyShortName: ").append(this.CompanyShortName.toString());
		}
		if(null != this.CompanyShortKanaName){
			sb.append(",CompanyShortKanaName: ").append(this.CompanyShortKanaName.toString());
		}
		/*if("".equals(String.valueOf(this.SubCode5))){
			sb.append(",SubCode5: ").append(String.valueOf(this.SubCode5));
		}*/
		sb.append("}");
		
		return sb.toString();
	}
}
