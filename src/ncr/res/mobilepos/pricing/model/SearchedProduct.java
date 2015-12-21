package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for a searched product.
 *<br>
 * Members:<br>
 * ResultCode   : Result code of the operation.<br>
 * Item       : {@link Item} object.<br>
 *                Item as search result for a specific PLU.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchedProduct")
public class SearchedProduct extends ResultBase {
    /**
     * The Item.
     */
    @XmlElement(name = "Item")
    private Item item;

    /**
     * Getter for item.
     * @return item
     */
    public final Item getItem() {
        return item;
    }

    /**
     * Setter for item.
     * @param itemToSet    Item to set.
     */
    public final void setItem(final Item itemToSet) {
        this.item = itemToSet;
    }

    @Override
    public final String toString() {
        if (null == item) {
            return "Item: NULL";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Item: " + item.toString());
        return sb.toString();
    }

}
