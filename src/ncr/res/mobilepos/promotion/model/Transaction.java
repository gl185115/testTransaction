package ncr.res.mobilepos.promotion.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transaction Model Object.
 *
 * Encapsulates the Transaction Promotion information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Transaction")
public class Transaction {
    /** The Transaction Mode. 0=Normal, 1=Training.*/
    @XmlElement(name = "TransactionMode")
    private int transactionMode;
    /** The Operator ID. */
    @XmlElement(name = "OperatorID")
    private String operatorID;
    /** The Begin Date Time in yyyy-MM-dd'T'HH:mm:ss.SS format.*/
    @XmlElement(name = "BeginDateTime")
    private String beginDateTime;
    /** The Status. */
    @XmlElement(name = "Status")
    private String status;
    /** The End Date Time. */
    @XmlElement(name = "EndDateTime")
    private String endDateTime;
    /** The Promotion Sales. */
    @XmlElement(name = "Sales")
    private List<Sale> sales;
    /** The Promotion Sale. */
    @XmlElement(name = "Sale")
    private Sale sale;
    /** The Entry Flag of transaction. */
    @XmlElement(name = "EntryFlag")
    private String entryFlag;
    
    /** The Company ID. */
    @XmlElement(name = "CompanyId")
    private String companyId;

    /**
     * @return the  companyId
     */
    public final String getCompanyId() {
		return companyId;
	}
    /**
     * @param companyIdToSet the companyId to set
     */
	public final void setCompanyId(final String companyIdToSet) {
		this.companyId = companyIdToSet;
	}
	/**
     * @return the transactionMode
     */
    public final int getTransactionMode() {
        return transactionMode;
    }
    /**
     * @param transactionModeToSet the transactionMode to set
     */
    public final void setTransactionMode(final int transactionModeToSet) {
        this.transactionMode = transactionModeToSet;
    }
    /**
     * @return the operatorID
     */
    public final String getOperatorID() {
        return operatorID;
    }
    /**
     * @param operatorIDToSet the operatorID to set
     */
    public final void setOperatorID(final String operatorIDToSet) {
        this.operatorID = operatorIDToSet;
    }
    /**
     * @return the beginDateTime
     */
    public final String getBeginDateTime() {
        return beginDateTime;
    }
    /**
     * @param beginDateTimeToSet the beginDateTime to set
     */
    public final void setBeginDateTime(final String beginDateTimeToSet) {
        this.beginDateTime = beginDateTimeToSet;
    }
    /**
     * @return the status
     */
    public final String getStatus() {
        return status;
    }
    /**
     * @param statusToSet the status to set
     */
    public final void setStatus(final String statusToSet) {
        this.status = statusToSet;
    }
    /**
     * @return the endDateTime
     */
    public final String getEndDateTime() {
        return endDateTime;
    }
    /**
     * @param endDateTimeToSet the endDateTime to set
     */
    public final void setEndDateTime(final String endDateTimeToSet) {
        this.endDateTime = endDateTimeToSet;
    }
    /**
     * @return the sales
     */
    public final List<Sale> getSales() {
        return sales;
    }
    /**
     * @param salesToSet the sales to set
     */
    public final void setSales(final List<Sale> salesToSet) {
        this.sales = salesToSet;
    }
    /**
     * Sale setter.
     * @param aSale the sale to set
     */
    public final void setSale(final Sale aSale) {
        this.sale = aSale;
    }
    /**
     * Sale getter.
     * @return the sale
     */
    public final Sale getSale() {
        return sale;
    }
    /**
     * Entry Flag Setter.
     * @param entryFlagToSet Transaction's entry flag.
     */
    public final void setEntryFlag(final String entryFlagToSet) {
        this.entryFlag = entryFlagToSet;
    }
    /**
     * Entry Flag getter.
     * @return  The Entry Flag of transaction.
     */
    public final String getEntryFlag() {
        return entryFlag;
    }
}
