package ncr.res.mobilepos.queuesignature.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Model Class that represents Transaction.
 *
 */
@XmlRootElement(name = "Transaction")
@ApiModel(value="Transaction")
public class Transaction {
    /**
     * The Worksation ID.
     */
    private String workstationID;
    /**
     * The Sequence Number.
     */
    private String sequenceNumber;
    /**
     * The Total.
     */
    private double total;
    /**
     * Get the Total.
     * @return  The Total.
     */
    @XmlElement(name = "Total")
    @ApiModelProperty(value="���v", notes="���v")
    public final double getTotal() {
        return this.total;
    }
    /**
     * Set the Total.
     * @param totalToSet The Total.
     */
    public final void setTotal(final double totalToSet) {
        this.total = totalToSet;
    }
    /**
     * Get the Workstation ID.
     * @return  The Workstation ID.
     */
    @XmlElement(name = "WorkstationID")
    @ApiModelProperty(value="��Ƒ�R�[�h", notes="��Ƒ�R�[�h")
    public final String getWorkstationID() {
        return this.workstationID;
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
    @XmlElement(name = "SequenceNumber")
    @ApiModelProperty(value="�V���A���i���o�[", notes="�V���A���i���o�[")
    public final String getSequenceNumber() {
        return this.sequenceNumber;
    }
    /**
     * Set the Sequence Number.
     * @param sequenceNumberToSet The Sequence Number.
     */
    public final void setSequenceNumber(final String sequenceNumberToSet) {
        this.sequenceNumber = sequenceNumberToSet;
    }

    @Override
    public final String toString() {
        StringBuffer str = new StringBuffer();
        String dlmtr = "; ";
        str.append(super.toString()).append(dlmtr)
           .append("SequenceNumber: ").append(this.sequenceNumber).append(dlmtr)
           .append("Total: ").append(this.total).append(dlmtr)
           .append("WorkstationID: ").append(this.workstationID);
        return str.toString();
    }
}
