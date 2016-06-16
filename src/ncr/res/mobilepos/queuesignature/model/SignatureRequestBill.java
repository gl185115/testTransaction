package ncr.res.mobilepos.queuesignature.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
/**
 * The Model Class Representing the Signature Request Bill.
 *
 */
@XmlRootElement(name = "SignatureRequestBill")
@ApiModel(value="SignatureRequestBill")
public class SignatureRequestBill extends ResultBase {
    /**
     * the CA Information.
     */
    private CAInfo caInfo;
    /**
     * The Queue ID of the Signature Request Bill.
     */
    private String queueId;
    /**
     * The Retail Store ID.
     */
    private String retailStoreId;
    /**
     * The Workstation ID.
     */
    private String workstationId;
    /**
     * The Sequence Number.
     */
    private String sequenceNumber;
    /**
     * @param caInfoToSet   The CA Info.
     */
    public final void setCAInfo(final CAInfo caInfoToSet) {
        this.caInfo = caInfoToSet;
    }
    /**
     * Get the CA Info.
     * @return  The CA info.
     */
    @XmlElement(name = "CAInfo")
    @ApiModelProperty(value="CA情報", notes="CA情報")
    public final CAInfo getCAInfo() {
        return this.caInfo;
    }
    /**
     * Set the Queue ID.
     * @param queueIdToSet The Queue ID to set
     */
    public final void setQueue(final String queueIdToSet) {
        this.queueId = queueIdToSet;
    }
    /**
     * Get the Queue ID.
     * @return The Queue.
     */
    @XmlElement(name = "Queue")
    @ApiModelProperty(value="署名要請の列標識コード", notes="署名要請の列標識コード")
    public final String getQueue() {
        return this.queueId;
    }
    /**
     * Set the Retail Store ID.
     * @param retailStoreIdToSet    The Retail Store ID.
     */
    public final void setRetailStoreID(final String retailStoreIdToSet) {
        this.retailStoreId = retailStoreIdToSet;
    }
    /**
     * Get the Retail Store ID.
     * @return The Retail Store ID.
     */
    @XmlElement(name = "RetailStoreID ")
    @ApiModelProperty(value="小売店コード", notes="小売店コード")
    public final String getRetailStoreID() {
        return this.retailStoreId;
    }
    /**
     * Set the Workstation ID.
     * @param workstationIdToSet The Workstation ID.
     */
    public final void setWorkstationID(final String workstationIdToSet) {
        this.workstationId = workstationIdToSet;
    }
    /**
     * Get the Workstation ID.
     * @return The Workstation ID.
     */
    @XmlElement(name = "WorkstationID ")
    @ApiModelProperty(value="作業台コード", notes="作業台コード")
    public final String getWorkstationID() {
        return this.workstationId;
    }
    /**
     * Set the Sequence Number.
     * @param sequenceNumberToSet The Sequence Number.
     */
    public final void setSequenceNumber(final String sequenceNumberToSet) {
        this.sequenceNumber = sequenceNumberToSet;
    }
    /**
     * Get the Sequence Number.
     * @return Sequence Number
     */
    @XmlElement(name = "SequenceNumber")
    @ApiModelProperty(value="シリアルナンバー", notes="シリアルナンバー")
    public final String getSequenceNumber() {
        return this.sequenceNumber;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String dlmtr = "; ";
        str.append(super.toString()).append(dlmtr)
           .append("QueueID: ").append(this.queueId).append(dlmtr)
           .append("RetailStoreID: ").append(this.retailStoreId).append(dlmtr)
           .append("SequenceNumber: ").append(this.sequenceNumber).append(dlmtr)
           .append("WorkstationID: ").append(this.workstationId).append(dlmtr);

        if (null !=  this.caInfo) {
            str.append("CAInfo: {").append(this.queueId).append("}");
        }
        return str.toString();
    }
}

