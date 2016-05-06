package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Advertise")
public class EAdvertise {

    @XmlElement(name = "DeployApplied")
    private List<DeployApplied> deployAppliedList;

    public List<DeployApplied> getDeployAppliedList() {
        return deployAppliedList;
    }

    public void setDeployAppliedList(List<DeployApplied> deployAppliedList) {
        this.deployAppliedList = deployAppliedList;
    }
}
