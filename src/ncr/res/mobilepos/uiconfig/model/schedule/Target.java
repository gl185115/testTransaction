package ncr.res.mobilepos.uiconfig.model.schedule;


import ncr.res.mobilepos.uiconfig.model.store.CSVStore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "target")
public class Target {
    @XmlElement(name = "store")
    private String store;
    @XmlElement(name = "workstation")
    private String workstation;

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

    // Works for 'all', for workstationID and storeID
    private static final String ALLOW_ALL = "All";

    /**
     * Convert to string.
     *
     * @return String
     */
    @Override
    public final String toString() {
        ObjectWriter mapper = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }

    /**
     * Checks if given workstationID is included by the task.
     *
     * @param givenWorkstationID
     * @return
     */
    public boolean hasWorkstationID(String givenWorkstationID) {
        // 1st validation, checks if task.workstationID is All.
        if (ALLOW_ALL.equalsIgnoreCase(workstation)) {
            return true;
        }
        // 2nd validation, checks if givenWorkstationID matches with target workstationID.
        return givenWorkstationID.equalsIgnoreCase(workstation);
    }

    /**
     * Checks if given storeID is included by the task.
     *
     * @param givenStoreID
     * @param csvStores
     * @return
     */
    public boolean hasStoreID(String givenStoreID, List<CSVStore> csvStores) {
        // 1st validation, checks if task.storeID is All. if true it skips following validations.
        if (ALLOW_ALL.equalsIgnoreCase(store)) {
            return true;
        }
        // 2nd validation, checks if given storeID is in csvStores.
        boolean foundStore = false;
        for (CSVStore store : csvStores) {
            if (store.hasStoreID(givenStoreID)) {
                foundStore = true;
                break;
            }
        }
        if (!foundStore) {
            return false;
        }
        // 3rd validation, compares IDs between given storeID and target storeID.
        return givenStoreID.equalsIgnoreCase(store);
    }

}