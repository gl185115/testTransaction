package ncr.res.mobilepos.queuebuster.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that represents a resumed transaction.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ResumedTransaction")
public class ResumedTransaction extends ResultBase {
	/**
	 * Default constructor.
	 */
	public ResumedTransaction() {
		super();
	}

	/**
	 * Constructor.
	 * 
	 * @param resultCode
	 *            The resulting error code.
	 * @param extendedResultCode
	 *            The extended error code.
	 * @param throwable
	 *            The exception.
	 */
	public ResumedTransaction(final int resultCode,
			final int extendedResultCode, final Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
    /**
     * The Retail Store ID.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;
    /**
     * The Queue from POS.
     */
    @XmlElement(name = "Queue")
    private String queue;
    /**
     * The Workstation ID.
     */
    @XmlElement(name = "WorkstationID")
    private String workstationID;
    /**
     * The Sequence Number.
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNumber;
    /**
     * The POSLog xml.
     */
    @XmlElement(name = "POSLog")
    private String poslog;

    /**
     * Get the Retail Store ID.
     * @return  The Retail Store ID.
     */
    public final String getRetailStoreID() {
        return retailStoreID;
    }
    /**
     * Set the Retail Store ID.
     * @param retailStoreIDToSet The Retail Store ID.
     */
    public final void setRetailStoreID(final String retailStoreIDToSet) {
        this.retailStoreID = retailStoreIDToSet;
    }
    /**
     * Get the Queue of POS.
     * @return The Queue of POS.
     */
    public final String getQueue() {
        return queue;
    }
    /**
     * Set the Queue from POS.
     * @param queueToSet The Queue from POS.
     */
    public final void setQueue(final String queueToSet) {
        this.queue = queueToSet;
    }
    /**
     * Get the Workstation ID.
     * @return The Workstation ID.
     */
    public final String getWorkstationID() {
        return workstationID;
    }
    /**
     * Set the Workstation ID.
     * @param workstationIDToSet The Workstation ID.
     */
    public final void setWorkstationID(final String workstationIDToSet) {
        this.workstationID = workstationIDToSet;
    }
    /**
     * Get the Sequence Number.
     * @return The Sequence Number.
     */
    public final String getSequenceNumber() {
        return sequenceNumber;
    }
    /**
     * Set the Sequence Number.
     * @param sequenceNumberToSet The Sequence Number to set.
     */
    public final void setSequenceNumber(final String sequenceNumberToSet) {
        this.sequenceNumber = sequenceNumberToSet;
    }
    /**
     * Get the POSLog xml.
     * @return  The POSLog Xml.
     */
    public final String getPoslog() {
        return poslog;
    }
    /**
     * Set the POSLog xml.
     * @param poslogToSet The POSLog xml.
     */
    public final void setPoslog(final String poslogToSet) {
        this.poslog = poslogToSet;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String dlmtr = "; ";
        str.append(super.toString()).append(dlmtr)
           .append("Queue").append(this.queue).append(dlmtr)
           .append("RetailStoreID").append(this.retailStoreID).append(dlmtr)
           .append("SequenceNumber").append(this.sequenceNumber).append(dlmtr)
           .append("WorkstationID").append(this.workstationID);
        return str.toString();
    }
}
