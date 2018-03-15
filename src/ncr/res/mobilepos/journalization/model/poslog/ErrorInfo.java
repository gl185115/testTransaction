package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PosLog Model Object.
 *
 * <P>A Error Info Node in POSLog XML.
 *
 * <P>The Error Info node mainly holds the Error Info of API.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ErrorInfo")
public class ErrorInfo {
	/**
     * The private member variable that will hold the API Name.
     */
    @XmlElement(name = "APIName")
    private String apiName;

    /**
     * The private member variable that will hold the Error Message.
     */
    @XmlElement(name = "ErrorMessage")
    private String errorMessage;

	/**
     * @return the apiName to set
     */
	public final String getAPIName() {
		return apiName;
	}
	/**
     * @param the apiName
     */
	public final void setAPIName(final String apiName) {
		this.apiName = apiName;
	}

    /**
     * @return the errorMessage to set
     */
    public final String getErrorMessage() {
        return errorMessage;
    }
    /**
     * @param the errorMessage
     */
    public final void setErrorMessage(final String errorMessage) {
        this.errorMessage = errorMessage;
    }
	
}