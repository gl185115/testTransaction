package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
/***
 * The SummaryReceipt element. 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SummaryReceipt")
public class SummaryReceipt {
	/**
	 * The Original transaction.<p/>
	 * The transaction that was outputted in the summary receipt.
	 */
	@XmlElement(name = "TransactionLink")
	private TransactionLink transactionLink;
	/**
	 * Gets the original transaction.
	 * @return	the original transaction.
	 */
	public TransactionLink getTransactionLink() {
		return transactionLink;
	}
	/**
	 * Sets the original transaction.
	 * @param transactionLink	The original transaction.
	 */
	public void setTransactionLink(TransactionLink transactionLink) {
		this.transactionLink = transactionLink;
	}
}
