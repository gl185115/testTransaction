package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates ReasonDataList.
 * @author rd185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReasonDataList")
public class ReasonDataList extends ResultBase {
    /**
     * The TransactionReasonDataList.
     */
	@XmlElement(name = "TransactionReasonDataList")
    private TransactionReasonDataList txReasonDataList;
    /**
     * The ItemReasonDataList.
     */
	@XmlElement(name = "ItemReasonDataList")
    private ItemReasonDataList itemReasonDataList;
    /**
     * The OverrideReasonDataList.
     */
	@XmlElement(name = "OverrideReasonDataList")
    private OverrideReasonDataList overrideReasonDataList;
	/**
     * Getter for TransactionReasonDataList.
     * @return    TransactionReasonDataList
     */
    public final TransactionReasonDataList getTxReasonDataList() {
        return txReasonDataList;
    }
    /**
     * Setter for TransactionReasonDataList.
     * @param txReasonDataListToSet   TransactionReasonDataList
     */
    public final void setTxReasonDataList(
            final TransactionReasonDataList txReasonDataListToSet) {
        this.txReasonDataList = txReasonDataListToSet;
    }
    /**
     * Getter for ItemReasonDataList.
     * @return    ItemReasonDataList
     */
    public final ItemReasonDataList getItemReasonDataList() {
        return itemReasonDataList;
    }
    /**
     * Setter for ItemDiscountReasonList.
     * @param itemDiscountReasonListToSet    ItemDiscountReasonList
     */
    public final void setItemReasonDataList(
            final ItemReasonDataList itemReasonDataListToSet) {
        this.itemReasonDataList = itemReasonDataListToSet;
    }
    /**
     * Getter for List of OverrideReasonData.
     * @return	List of OverrideReasonData
     */
    public OverrideReasonDataList getOverrideReasonDataList() {
		return overrideReasonDataList;
	}
    /**
     * Setter for List of OverrideReasonData.
     * @param overrideReasonDataListToSet	List of OverrideReasonData.
     */
	public void setOverrideReasonDataList(
			OverrideReasonDataList overrideReasonDataListToSet) {
		this.overrideReasonDataList = overrideReasonDataListToSet;
	}
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("TransactionReasonDataList: "
                + txReasonDataList).append("; ");
        sb.append("ItemReasonDataList: "
                + itemReasonDataList).append("; ");
        sb.append("OverrideReasonDataList: "
        		+ overrideReasonDataList).append("; ");
        return sb.toString();
    }
}
