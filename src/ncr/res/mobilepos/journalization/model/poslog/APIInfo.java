package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PosLog Model Object.
 *
 * <P>A Report API Info Node in POSLog XML.
 *
 * <P>The Report API Info node mainly holds the Info of Report API.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "APIInfo")
public class APIInfo {
	/**
     * The private member variable that will hold the API Name.
     */
    @XmlElement(name = "APIName")
	private String apiName;
    
    /**
     * The private member variable that will hold the API Message.
     */
    @XmlElement(name = "APIMessage")
    private String apiMessage;

    /**
     * @return get the apiName
     */
	public String getApiName() {
		return apiName;
	}

	/**
     * @return the apiName to set
     */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/**
     * @param the APIMessage
     */
	public String getApiMessage() {
		return apiMessage;
	}

	/**
     * @return the APIMessage to set
     */
	public void setApiMessage(String apiMessage) {
		this.apiMessage = apiMessage;
	}
    
    
}
