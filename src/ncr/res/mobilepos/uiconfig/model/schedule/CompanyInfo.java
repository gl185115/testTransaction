package ncr.res.mobilepos.uiconfig.model.schedule;

import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlElement;

@XmlRootElement(name = "CompanyInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class CompanyInfo {
	
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
	private String SubCode1;
	
	@XmlElement(name = "SubCode2")
	private String SubCode2;
	
	@XmlElement(name = "SubCode3")
	private String SubCode3;
	
	@XmlElement(name = "SubCode4")
	private String SubCode4;
	
	@XmlElement(name = "SubCode5")
	private String SubCode5;
	
	@XmlElement(name = "SubNum1")
	private int SubNum1;
	
	@XmlElement(name = "SubNum2")
	private int SubNum2;
	
	@XmlElement(name = "SubNum3")
	private int SubNum3;
	
	@XmlElement(name = "SubNum4")
	private int SubNum4;
	
	@XmlElement(name = "SubNum5")
	private int SubNum5;

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
	public String getSubCode1() {
		return SubCode1;
	}

	/**
	 * @param subCode1 the subCode1 to set
	 */
	public void setSubCode1(String subCode1) {
		SubCode1 = subCode1;
	}

	/**
	 * @return the subCode2
	 */
	public String getSubCode2() {
		return SubCode2;
	}

	/**
	 * @param subCode2 the subCode2 to set
	 */
	public void setSubCode2(String subCode2) {
		SubCode2 = subCode2;
	}

	/**
	 * @return the subCode3
	 */
	public String getSubCode3() {
		return SubCode3;
	}

	/**
	 * @param subCode3 the subCode3 to set
	 */
	public void setSubCode3(String subCode3) {
		SubCode3 = subCode3;
	}

	/**
	 * @return the subCode4
	 */
	public String getSubCode4() {
		return SubCode4;
	}

	/**
	 * @param subCode4 the subCode4 to set
	 */
	public void setSubCode4(String subCode4) {
		SubCode4 = subCode4;
	}

	/**
	 * @return the subCode5
	 */
	public String getSubCode5() {
		return SubCode5;
	}

	/**
	 * @param subCode5 the subCode5 to set
	 */
	public void setSubCode5(String subCode5) {
		SubCode5 = subCode5;
	}
	
	
	

	/**
	 * @return the subNum1
	 */
	public int getSubNum1() {
		return SubNum1;
	}

	/**
	 * @param subNum1 the subNum1 to set
	 */
	public void setSubNum1(int subNum1) {
		SubNum1 = subNum1;
	}

	/**
	 * @return the subNum2
	 */
	public int getSubNum2() {
		return SubNum2;
	}

	/**
	 * @param subNum2 the subNum2 to set
	 */
	public void setSubNum2(int subNum2) {
		SubNum2 = subNum2;
	}

	/**
	 * @return the subNum3
	 */
	public int getSubNum3() {
		return SubNum3;
	}

	/**
	 * @param subNum3 the subNum3 to set
	 */
	public void setSubNum3(int subNum3) {
		SubNum3 = subNum3;
	}

	/**
	 * @return the subNum4
	 */
	public int getSubNum4() {
		return SubNum4;
	}

	/**
	 * @param subNum4 the subNum4 to set
	 */
	public void setSubNum4(int subNum4) {
		SubNum4 = subNum4;
	}

	/**
	 * @return the subNum5
	 */
	public int getSubNum5() {
		return SubNum5;
	}

	/**
	 * @param subNum5 the subNum5 to set
	 */
	public void setSubNum5(int subNum5) {
		SubNum5 = subNum5;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		
		if(null != this.CompanyId){
			sb.append("CompanyId: ").append(this.CompanyId.toString());
		}
		if(null != this.CompanyName){
			sb.append(crlf).append(",CompanyName: ").append(this.CompanyName.toString());
		}
		if(null != this.CompanyKanaName){
			sb.append(crlf).append(",CompanyKanaName: ").append(this.CompanyKanaName.toString());
		}
		if(null != this.CompanyShortName){
			sb.append(crlf).append(",CompanyShortName: ").append(this.CompanyShortName.toString());
		}
		if(null != this.CompanyShortKanaName){
			sb.append(crlf).append(",CompanyShortKanaName: ").append(this.CompanyShortKanaName.toString());
		}
		if("".equals(String.valueOf(this.DisplayOrder))){
			sb.append(crlf).append(",DisplayOrder: ").append(String.valueOf(this.DisplayOrder));
		}
		if(null != this.SubCode1){
			sb.append(crlf).append(",SubCode1: ").append(this.SubCode1.toString());
		}
		if(null != this.SubCode2){
			sb.append(crlf).append(",SubCode2: ").append(this.SubCode2.toString());
		}
		if(null != this.SubCode3){
			sb.append(crlf).append(",SubCode3: ").append(this.SubCode3.toString());
		}
		if(null != this.SubCode4){
			sb.append(crlf).append(",SubCode4: ").append(this.SubCode4.toString());
		}
		if(null != this.SubCode5){
			sb.append(crlf).append(",SubCode5: ").append(this.SubCode5.toString());
		}
		if("".equals(String.valueOf(this.SubNum1))){
			sb.append(crlf).append(",SubNum1: ").append(String.valueOf(this.SubNum1));
		}
		if("".equals(String.valueOf(this.SubNum2))){
			sb.append(crlf).append(",SubNum2: ").append(String.valueOf(this.SubNum2));
		}
		if("".equals(String.valueOf(this.SubNum3))){
			sb.append(crlf).append(",SubNum3: ").append(String.valueOf(this.SubNum3));
		}
		if("".equals(String.valueOf(this.SubNum4))){
			sb.append(crlf).append(",SubNum4: ").append(String.valueOf(this.SubNum4));
		}
		if("".equals(String.valueOf(this.SubNum5))){
			sb.append(crlf).append(",SubNum5: ").append(String.valueOf(this.SubNum5));
		}
		return sb.toString();
	}
}
