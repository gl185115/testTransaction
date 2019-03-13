package ncr.res.mobilepos.systemconfiguration.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "SystemParameter")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="SystemParameter")
public class SystemParameter extends ResultBase{
	@XmlElement(name = "systemparameter")
	private List<SystemConfigInfo> systemparameter;

	public List<SystemConfigInfo> getSystemparameter() {
		return systemparameter;
	}

	public void setSystemparameter(List<SystemConfigInfo> systemparameter) {
		this.systemparameter = systemparameter;
	}
}
