package ncr.res.mobilepos.queuesignature.model;

import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import ncr.res.mobilepos.model.ResultBase;
/**
 * The Model Class  containing the Signature Request List.
 *
 */
@XmlRootElement(name = "SignatureRequestList")
@XmlSeeAlso({ Transaction.class })
public class SignatureRequestList extends ResultBase {
    /**
     * The transaction List for Signature Request(s).
     */
    private List<Transaction> transactionList;
    /**
     * The Queue ID.
     */
    private String queueId;
    /**
     * Set the Transaction List.
     * @param result The transaction List for Signature Request(s)
     */
    public final void setTransactionList(
            final List<Transaction> result) {
        this.transactionList = result;
    }
    /**
     * Get the Transaction List for Signature Request(s).
     * @return The Transaction List for Signature Request(s).
     */
    @XmlElementWrapper(name = "TransactionList")
    @XmlElementRef()
    public final List<Transaction> getTransactionList() {
        return this.transactionList;
    }
    /**
     * Set the Queue Id.
     * @param queueIdToSet The Queue ID.
     */
    public final void setQueue(final String queueIdToSet) {
        this.queueId = queueIdToSet;
    }
    /**
     * Get the Queue ID.
     * @return  The Queue ID.
     */
    @XmlElement(name = "Queue")
    public final String getQueue() {
        return this.queueId;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String dlmtr = "; ";
        str.append(super.toString()).append(dlmtr)
           .append("QueueID: ").append(this.queueId).append(dlmtr);

        if (null != transactionList) {
            str.append("TransactionList: {");
            for (Transaction trans : transactionList) {
                str.append("Transaction: { ")
                   .append(trans.toString()).append("}")
                .append(dlmtr);
            }
            str.append("}").append(dlmtr);
        }
        return str.toString();
    }
}

