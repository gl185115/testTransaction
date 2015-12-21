package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PosLog Model Object.
 *
 * <P>A API Error List Node in POSLog XML.
 *
 * <P>The API Error List node mainly holds the Error detail of API.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "APIErrorList")
public class APIErrorList {
	/**
     * The private member variable that will hold the Error Info.
     */
    @XmlElement(name = "ErrorInfo")
    private List<ErrorInfo> errorInfoList;
    
	/**
     * @return the errorInfoList to set
     */
	public final List<ErrorInfo> getErrorInfoList() {
		return errorInfoList;
	}
	
	/**
     * @param the errorInfoList
     */
	public final void setErrorInfoList(final List<ErrorInfo> errorInfoList) {
		this.errorInfoList = errorInfoList;
	}
	
}