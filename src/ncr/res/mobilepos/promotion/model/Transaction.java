package ncr.res.mobilepos.promotion.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Transaction Model Object.
 *
 * Encapsulates the Transaction Promotion information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Transaction")
@ApiModel(value="Transaction")
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
    @ApiModelProperty(value="会社コード", notes="会社コード")
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
    @ApiModelProperty(value="業務モード", notes="業務モード")
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
    @ApiModelProperty(value="オペレーターコード", notes="オペレーターコード")
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
    @ApiModelProperty(value="日付を開始", notes="日付を開始")
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
    @ApiModelProperty(value="身元", notes="身元")
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
    @ApiModelProperty(value="瞭期日", notes="瞭期日")
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
    @ApiModelProperty(value="販売リスト", notes="販売リスト")
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
    @ApiModelProperty(value="販売商品", notes="販売商品")
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
    @ApiModelProperty(value="入口マーク", notes="入口マーク")
    public final String getEntryFlag() {
        return entryFlag;
    }
}
