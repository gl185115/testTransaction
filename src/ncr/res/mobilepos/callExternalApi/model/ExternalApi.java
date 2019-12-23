package ncr.res.mobilepos.callExternalApi.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AppServer")
@ApiModel(value="AppServer")
public class ExternalApi extends ResultBase {
	
	/**
	 * Result Data.
	 */
    @XmlElement(name = "ResultData")
    private String resultData;
    
    @ApiModelProperty(value="ResultData", notes="Result Data")
    public final String getResultData() {
    	return resultData;
    }
    public final void setResultData(String resultData) {
    	this.resultData = resultData;
    }
    
    /**
	 * Result Code.
    @XmlElement(name = "ResultCode")
    private String resultCode;
    
    @ApiModelProperty(value="ResultCode", notes="Result Code")
    public final String getResultCode() {
    	return resultCode;
    }
    public final void setResultCode(String resultCode) {
    	this.resultCode = resultCode;
    }
    */
}
