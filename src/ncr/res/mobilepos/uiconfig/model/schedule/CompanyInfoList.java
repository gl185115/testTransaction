package ncr.res.mobilepos.uiconfig.model.schedule;

import ncr.res.mobilepos.model.ResultBase;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CompanyInfoList")
public class CompanyInfoList extends ResultBase{
	
    @XmlElement(name = "CompanyInfoList")
    private List<CompanyInfo> companyInfoList;
    
    /**
	 * @return the companyInfo
	 */
	public List<CompanyInfo> getCompanyInfo() {
        return companyInfoList;
	}

	/**
	 * @param companyInfoList the companyInfo to set
	 */
	public final void setCompanyInfo(final List<CompanyInfo> companyInfoList) {
		this.companyInfoList = companyInfoList;
	}

	public final String toString() {
		StringBuilder ret = new StringBuilder();
		String crlf = "\r\n";
		if (null != this.companyInfoList) {
			for (CompanyInfo companyInfo : companyInfoList) {
				ret.append(crlf).append("CompanyInfo : ").append(companyInfo.toString());
			}
		}
		return ret.toString();
	}
	
}