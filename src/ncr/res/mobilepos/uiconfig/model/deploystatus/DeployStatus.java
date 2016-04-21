package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeployStatus")
public class DeployStatus {

    @XmlElement(name = "DeployConfig")
    private List<DeployConfig> deployConfig;

    public List<DeployConfig> getDeployConfig() {
        return deployConfig;
    }

    public void setDeployConfig(List<DeployConfig> deployConfig) {
        this.deployConfig = deployConfig;
    }
}
