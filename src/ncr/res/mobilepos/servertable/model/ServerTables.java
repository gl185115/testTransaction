package ncr.res.mobilepos.servertable.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ServerTables")
@ApiModel(value="ServerTables")
public class ServerTables extends ResultBase {
	/**
	 * List of AP Servers
	 */
    @XmlElement(name = "Servers")
    private List<ServerTable> serverTables;
    /**
     * Get the list of AP Servers
     * @return
     */
    @ApiModelProperty(value="AP Servers", notes="Tomcat or IIS")
    public List<ServerTable> getServerTables() {
		return serverTables;
	}
    /**
     * Set the list of AP Servers
     * @param serverTables
     */
	public void setServerTables(List<ServerTable> serverTables) {
		this.serverTables = serverTables;
	}
}
