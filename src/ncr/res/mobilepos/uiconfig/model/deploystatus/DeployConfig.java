package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeployConfig")
public class DeployConfig {

    @XmlElement(name = "CompanyId")
    private String companyId;

    @XmlElement(name = "CompanyName")
    private String companyName;

    @XmlElement(name = "DeployEffective")
    private DeployEffective deployEffective;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public DeployEffective getDeployEffective() {
        return deployEffective;
    }

    public void setDeployEffective(DeployEffective deployEffective) {
        this.deployEffective = deployEffective;
    }
}
