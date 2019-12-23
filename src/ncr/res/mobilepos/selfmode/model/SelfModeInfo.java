package ncr.res.mobilepos.selfmode.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

//import org.json.JSONObject;
//import atg.taglib.json.util.JSONObject;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SelfModeInfo")
@ApiModel(value="SelfModeInfo")
public class SelfModeInfo extends ResultBase{
	
	@XmlElement(name = "SelfModeInfo")
	private String selfModeInfo = null;

	/**
	 * @return the jsonObject
	 */
	@ApiModelProperty(value="セルフモード状態", notes="セルフモード状態")
	public final String getSelfModeInfo() {
		return selfModeInfo;
	}

	/**
	 * @param jsonObject the jsonObject to set
	 */
	public final void setSelfModeInfo(String selfModeInfo) {
		this.selfModeInfo = selfModeInfo;
	}
	
}
