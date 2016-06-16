/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* BusteredTransactionList
*
* Model Class representing the QueueBuster Transaction List
*
* Campos, Carlos
*/
package ncr.res.mobilepos.queuebuster.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that represents the List of transaction for QueueBuster.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BusteredTransactionList")
@ApiModel(value="BusteredTransactionList")
public class BusteredTransactionList extends ResultBase {
	/**
	 * Default constructor.
	 */
	public BusteredTransactionList() {
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
	public BusteredTransactionList(final int resultCode,
			final int extendedResultCode, final Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
    /**
     * The Transaction List needs to be buster.
     */
    @XmlElement(name = "Transaction")
    private List<BusteredTransaction> busteredTransactionList;

    /**
     * Get the Transaction List needs to be bust.
     * @return The Transaction List to be buster.
     */
    @ApiModelProperty(value="業務リスト", notes="業務リスト")
    public final List<BusteredTransaction> getBusteredTransactionList() {
        return busteredTransactionList;
    }

    /**
     * Set the Transaction List needs to be bust.
     * @param busteredTransactionListToSet The Transaction List to bust.
     */
    public final void setBusteredTransactionList(
            final List<BusteredTransaction> busteredTransactionListToSet) {
        this.busteredTransactionList = busteredTransactionListToSet;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String dlmtr = "; ";
        str.append(super.toString()).append(dlmtr);

        if (null != this.busteredTransactionList) {
            str.append("Bustered Transaction List Count: ")
               .append(busteredTransactionList.size());
        }
        return "";
    }
}
