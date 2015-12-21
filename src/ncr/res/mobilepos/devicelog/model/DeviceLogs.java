package ncr.res.mobilepos.devicelog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model class that contains the a list of logs for a Device.
 * @author Developer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "udid", "count", "startIndex", "endIndex", "logs" })
@XmlRootElement(name = "DeviceLogs")
public class DeviceLogs extends ResultBase {
    /**
     * The Unique Device ID.
     */
    private String udid;
    /**
     * The Start Index of the array Log.
     */
    private int startIndex;
    /**
     * The End Index of the array Log.
     */
    private int endIndex;
    /**
     * The number of Logs.
     */
    private int count;
    /**
     * The Device Logs.
     */
    private DeviceLog[] logs;

    /**
     * Get the Unique Device ID.
     * @return  The Unique Device ID.
     */
    @XmlElement(name = "udid")
    public final String getUdid() {
        return udid;
    }

    /**
     * Set the Unique Device ID.
     * @param udidToSet The Unique Device ID.
     */
    public final void setUdid(final String udidToSet) {
        this.udid = udidToSet;
    }

    /**
     * Get the Start Index.
     * @return  The Start index.
     */
    @XmlElement(name = "startIndex")
    public final int getStartIndex() {
        return startIndex;
    }

    /**
     * Set the Start index.
     * @param startIndexToSet The Start Index.
     */
    public final void setStartIndex(final int startIndexToSet) {
        this.startIndex = startIndexToSet;
    }

    /**
     * Get the End index.
     * @return The End Index.
     */
    @XmlElement(name = "endIndex")
    public final int getEndIndex() {
        return endIndex;
    }

    /**
     * Set the end Index.
     * @param endIndexToSet The end Index.
     */
    public final void setEndIndex(final int endIndexToSet) {
        this.endIndex = endIndexToSet;
    }

    /**
     * Get the number of Logs.
     * @return The number of Logs.
     */
    @XmlElement(name = "count")
    public final int getCount() {
        return count;
    }

    /**
     * Set the numbe rof Logs.
     * @param countToSet The number of Logs.
     */
    public final void setCount(final int countToSet) {
        this.count = countToSet;
    }

    /**
     * The Device Logs.
     * @return  The Device Logs.
     */
    @XmlElement(name = "DeviceLog")
    @XmlElementWrapper(name = "logs")
    public final DeviceLog[] getLogs() {
        return logs.clone();
    }
    /**
     * Set the device Logs.
     * @param logsToSet The Device Logs.
     */
    public final void setLogs(final DeviceLog[] logsToSet) {
        this.logs = logsToSet.clone();
    }
}
