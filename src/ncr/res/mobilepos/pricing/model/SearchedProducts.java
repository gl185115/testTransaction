package ncr.res.mobilepos.pricing.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for the searched products.
 *<br>
 * Members:<br>
 * ResultCode   : Result code of the operation.<br>
 * Item[]       : Array of {@link Item} object.<br>
 *                But this only contains a single<br>
 *                Item as search result for a specific PLU.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SearchedProducts")
@ApiModel(value="SearchedProducts")
public class SearchedProducts extends ResultBase {
    /**
     * Array ot items.
     */
    @XmlElement(name = "Item")
    private List<Item> items;

    /**
     * Getter for array of items.
     * @return array of items
     */
    @ApiModelProperty(value="アイテムリスト", notes="アイテムリスト")
    public final List<Item> getItems() {
        return items;
    }

    /**
     * Setter for array of items.
     * @param itemsToSet    Array of items to set
     */
    public final void setItems(final List<Item> itemsToSet) {
        this.items = itemsToSet;
    }

    @Override
    public final String toString() {
        int noOfItems = 0;
        if (null != items) {
            noOfItems = items.size();
        }

        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Items count: " + noOfItems);
        return sb.toString();
    }

}
