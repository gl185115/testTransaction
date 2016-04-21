package ncr.res.mobilepos.uiconfig.model.schedule;

import ncr.res.mobilepos.uiconfig.model.store.CSVStore;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "config")
public class Config {
    @XmlElement(name = "resource")
    private String resource;
    @XmlElement(name = "task")
    private List<Task> task;

    public List<Task> getTask() {
        return task;
    }

    public void setTask(List<Task> task) {
        this.task = task;
    }

    public String getResource() {
        return resource;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

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
     * Filters children tasks and get valid tasks.
     *
     * @param givenStoreID
     * @param givenWorkstationID
     * @param csvStores
     * @return
     */
    public List<Task> getValidTasks(String givenStoreID, String givenWorkstationID, List<CSVStore> csvStores) {
        // Prepares TreeMap to sort tasks by effective date. Oldest comes first.
        List<Task> validTasks = new ArrayList<Task>();
        // Puts effective tasks into the map.
        for (Task aTask : task) {
            if (Config.isValidTask(givenStoreID, givenWorkstationID, csvStores, aTask)) {
                validTasks.add(aTask);
            }
        }
        // Sorts valid tasks by effective date.
        Collections.sort(validTasks, Task.TaskEffectiveDateComparator);
        return validTasks;
    }

    private static boolean isValidTask(String givenStoreID, String givenWorkstationID, List<CSVStore> csvStores, Task task) {
        return task.isEffective()
                && task.getTarget().hasStoreID(givenStoreID, csvStores)
                && task.getTarget().hasWorkstationID(givenWorkstationID);
    }
}
