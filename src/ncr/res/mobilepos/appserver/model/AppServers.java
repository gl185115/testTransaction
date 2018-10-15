package ncr.res.mobilepos.appserver.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AppServers")
@ApiModel(value="AppServers")
public class AppServers extends ResultBase {
	/**
	 * List of AP Servers
	 */
    @XmlElement(name = "Servers")
    private List<AppServer> appServers;
    /**
     * Get the list of AP Servers
     * @return
     */
    @ApiModelProperty(value="App Servers", notes="Tomcat or IIS")
    public List<AppServer> getAppServers() {
		return appServers;
	}
    /**
     * Set the list of Application Servers
     * @param servers
     */
	public void setAppServers(List<AppServer> appServers) {
		this.appServers = appServers;
	}
}
