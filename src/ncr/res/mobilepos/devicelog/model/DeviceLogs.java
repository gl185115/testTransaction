package ncr.res.mobilepos.devicelog.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model class that contains the a list of logs for a Device.
 * @author Developer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlType(propOrder = { "udid", "count", "startIndex", "endIndex", "logs" })
@XmlRootElement(name = "DeviceLogs")
@ApiModel(value="DeviceLogs")
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
    @ApiModelProperty( value="唯一装置コード", notes="唯一装置コード")
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
    @ApiModelProperty( value="スタート索引", notes="スタート索引")
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
    @ApiModelProperty( value="最終索引", notes="最終索引")
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
    @ApiModelProperty( value="返されるインデックス数", notes="返されるインデックス数")
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
    @ApiModelProperty( value="デバイス日志", notes="デバイス日志")
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
