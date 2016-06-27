package ncr.res.mobilepos.pricing.model;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PickListItemType")
@ApiModel(value="PickListItemType")
public class PickListItemType {

    /**
     * Array of itemTypes.
     */
    @XmlElement(name = "Items")
    private List<PickListItem> items;

    /**
     * Getter for array of items.
     * @return array of items
     */
    @ApiModelProperty(value="選択リストタイプ", notes="選択リストタイプ")
    public final List<PickListItem> getItems() {
        return items;
    }

    /**
     * Setter for array of items.
     * @param itemsToSet    Array of items to set
     */
    public final void setItems(final List<PickListItem> itemsToSet) {
        this.items = itemsToSet;
    }
    /**
     *
     * @param item
     */
    public void addItem(PickListItem item) {
        if(this.items == null) {
            this.items = new ArrayList<>();
        }
        this.items.add(item);
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
