package ncr.res.mobilepos.servertable.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ServerTable")
@ApiModel(value="ServerTable")
public class ServerTable {
	/**
	 * AP Server Name.
	 */
    @XmlElement(name = "Name")
    private String name;
    /**
     * Web application (resTransaction or WOApi)
     */
    @XmlElement(name = "Type")
    private String type;
    /**
     * Tomcat Url.
     */
    @XmlElement(name = "Url")
    private String url;
    /**
     * IIS Url.
     */
    @XmlElement(name = "IISUrl")
    private String iisUrl;
    
    @ApiModelProperty(value="Name", notes="Unique name")
    public final String getName() {
    	return name;
    }
    public final void setName(String name) {
    	this.name = name;
    }
    @ApiModelProperty(value="WebApp Type", notes="resTransaction or WOApi")
    public final String getType() {
    	return type;
    }
    public final void setType(String type) {
    	this.type = type;
    }
    @ApiModelProperty(value="Tomcat Url", notes="connection url to resTransaction")
    public final String getUrl() {
    	return this.url;
    }
    public final void setUrl(String url) {
    	this.url = url;
    }
    @ApiModelProperty(value="IIS Url", notes="connection url to WOApi")
    public String getIisUrl() {
		return iisUrl;
	}
	public void setIisUrl(String iisUrl) {
		this.iisUrl = iisUrl;
	}	
}
