package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeployApplied")
public class DeployApplied {

    @XmlElement(name = "Store")
    private String store;

    @XmlElement(name = "Workstation")
    private String workstation;

    @XmlElement(name = "Filename")
    private String filename;

    @XmlElement(name = "Effective")
    private String effective;

    @XmlElement(name = "DeployStoreID")
    private DeployStoreID deployStoreID;

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getWorkstation() {
        return workstation;
    }

    public void setWorkstation(String workstation) {
        this.workstation = workstation;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getEffective() {
        return effective;
    }

    public void setEffective(String effective) {
        this.effective = effective;
    }

    public DeployStoreID getDeployStoreID() {
        return deployStoreID;
    }

    public void setDeployStoreID(DeployStoreID deployStoreID) {
        this.deployStoreID = deployStoreID;
    }
}
