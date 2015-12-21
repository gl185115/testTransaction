package ncr.res.mobilepos.pricing.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PickList")
public class PickList extends ResultBase{

    /**
     * Array of itemTypes.
     */
    @XmlElement(name = "ItemType")
    private List<PickListItemType> items;
    
    /**
     * Getter for array of items.
     * @return array of items
     */
    public final List<PickListItemType> getItems() {
        return items;
    }

    /**
     * Setter for array of items.
     * @param itemsToSet    Array of items to set
     */
    public final void setItems(final List<PickListItemType> itemsToSet) {
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
