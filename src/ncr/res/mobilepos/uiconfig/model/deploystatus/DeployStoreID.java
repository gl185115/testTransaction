package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeployStoreID")
public class DeployStoreID {

    @XmlElement(name = "storeID")
    private List<String> storeID;

    public List<String> getStoreID() {
        return storeID;
    }

    public void setStoreID(List<String> storeID) {
        this.storeID = storeID;
    }
}
