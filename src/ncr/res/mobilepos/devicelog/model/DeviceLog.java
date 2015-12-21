package ncr.res.mobilepos.devicelog.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Clas that represents a log from the device.
 * @author PB185094
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceLog")
public class DeviceLog extends ResultBase {
    /**
     * The Log's Row ID.
     */
    private String rowId;
    /**
     * The Unique Device ID where the Log belong.
     */
    private String udid;
    /**
     * The Log date.
     */
    private Date logDate;
    /**
     * The Date and Time when the Log was uploaded.
     */
    private Date uploadTime;
    /**
     * The Date and Time when the Log was last updated.
     */
    private Date lastUpdated;

    /**
     * Get the Row ID.
     * @return  The Row ID.
     */
    @XmlElement(name = "rowId")
    public final String getRowId() {
        return rowId;
    }

    /**
     * Set the RowID.
     * @param rowIdToSet The new Row ID.
     */
    public final void setRowId(final String rowIdToSet) {
        this.rowId = rowIdToSet;
    }

    /**
     * Get the Unique Device ID for the Log.
     * @return The Unique Device ID.
     */
    @XmlElement(name = "udid")
    public final String getUdid() {
        return udid;
    }

    /**
     * Set the Unique Device ID for the Log.
     * @param udidToSet The Unique Device ID.
     */
    public final void setUdid(final String udidToSet) {
        this.udid = udidToSet;
    }

    /**
     * Get the Log Date.
     * @return  The Log Date.
     */
    @XmlElement(name = "logDate")
    public final Date getLogDate() {
        return (Date) logDate.clone();
    }

    /**
     * Set the Log date.
     * @param logDateToSet  The Log Date.
     */
    public final void setLogDate(final Date logDateToSet) {
        this.logDate = (Date) logDateToSet.clone();
    }

    /**
     * Get the upload time.
     * @return  The date and time for Upload.
     */
    @XmlElement(name = "uploadTime")
    public final Date getUploadTime() {
        return (Date) uploadTime.clone();
    }

    /**
     * Set the time to upload the Log.
     * @param uploadTimeToSet   The date for the upload time.
     */
    public final void setUploadTime(final Date uploadTimeToSet) {
        this.uploadTime = (Date) uploadTimeToSet.clone();
    }

    /**
     * Get the Last update.
     * @return The last update date.
     */
    @XmlElement(name = "lastUpdated")
    public final Date getLastUpdated() {
        return (Date) lastUpdated.clone();
    }

    /**
     * Set the Log's Last update.
     * @param lastUpdatedToSet The Date of the Last update.
     */
    public final void setLastUpdated(final Date lastUpdatedToSet) {
        this.lastUpdated = (Date) lastUpdatedToSet.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("; rowId: ").append(this.getRowId());
        sb.append("; udid: ").append(this.getUdid());
        sb.append("; logDate: ").append(this.getLogDate());
        sb.append("; uploadTime: ").append(this.getUploadTime());
        sb.append("; lastUpdated: ").append(this.getLastUpdated());

        return super.toString() + sb.toString();
    }

}
