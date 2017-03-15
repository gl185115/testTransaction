package ncr.res.mobilepos.webserviceif.model;

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
@XmlRootElement(name = "JSONData")
@ApiModel(value="JSONData")
public class JSONData extends ResultBase{
	
	@XmlElement(name = "JSONObject")
	private String jsonObject = null;

	/**
	 * @return the jsonObject
	 */
	@ApiModelProperty( value="JsonëŒè€", notes="JsonëŒè€")
	public final String getJsonObject() {
		return jsonObject;
	}

	/**
	 * @param jsonObject the jsonObject to set
	 */
	public final void setJsonObject(String jsonObject) {
		this.jsonObject = jsonObject;
	}
	
}
