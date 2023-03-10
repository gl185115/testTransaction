package ncr.res.mobilepos.uiconfig.model.schedule;

import ncr.res.mobilepos.uiconfig.model.store.CSVStore;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.text.ParseException;
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
     * @throws ParseException 
     */
    public List<Task> getValidTasks(String givenStoreID, String givenWorkstationID, List<CSVStore> csvStores, String thisBusinessDay) throws ParseException {
        // Prepares TreeMap to sort tasks by effective date. Oldest comes first.
        List<Task> validTasks = new ArrayList<Task>();
        // Puts effective tasks into the map.
        for (Task aTask : task) {
            if (Config.isValidTask(givenStoreID, givenWorkstationID, csvStores, aTask, thisBusinessDay)) {
                validTasks.add(aTask);
            }
        }
        // Sorts valid tasks by effective date.
        Collections.sort(validTasks, Task.TaskEffectiveDateComparator);
        return validTasks;
    }

    private static boolean isValidTask(String givenStoreID, String givenWorkstationID, List<CSVStore> csvStores, Task task, String thisBusinessDay) throws ParseException {
        return task.isEffective(thisBusinessDay)
                && task.getTarget().hasStoreID(givenStoreID, csvStores)
                && task.getTarget().hasWorkstationID(givenWorkstationID);
    }
    
    /**
     * Filters children tasks and get valid tasks.
     *
     * @param givenStoreID
     * @param givenWorkstationID
     * @param dbStores
     * @return
     * @throws ParseException 
     */
    public List<Task> getValidTasksByDB(String givenStoreID, String givenWorkstationID, List<StoreEntry> dbStores, String thisBusinessDay) throws ParseException {
        // Prepares TreeMap to sort tasks by effective date. Oldest comes first.
        List<Task> validTasks = new ArrayList<Task>();
        // Puts effective tasks into the map.
        for (Task aTask : task) {
            if (Config.isValidTaskByDB(givenStoreID, givenWorkstationID, dbStores, aTask, thisBusinessDay)) {
                validTasks.add(aTask);
            }
        }
        // Sorts valid tasks by effective date.
        Collections.sort(validTasks, Task.TaskEffectiveDateComparator);
        return validTasks;
    }

    private static boolean isValidTaskByDB(String givenStoreID, String givenWorkstationID, List<StoreEntry> dbStores, Task task, String thisBusinessDay) throws ParseException {
        return task.isEffective(thisBusinessDay)
                && task.getTarget().hasStoreIDByDB(givenStoreID, dbStores)
                && task.getTarget().hasWorkstationID(givenWorkstationID);
    }
}
