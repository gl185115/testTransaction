package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PickListItem")
@ApiModel(value="PickListItem")
public class PickListItem {

    /**
     * a private property that holds the value of item name.
     */
    @XmlElement(name = "ItemName")
    private String itemName;

    /**
     * a private property that holds the value of mdinterna/ sku/ plu.
     */
    @XmlElement(name = "MdInternal")
    private String mdInternal;

    /**
     * a private property that holds the value of display order
     */
    @XmlElement(name = "DisplayOrder")
    private int displayOrder;

    /**
     * a private property that holds the value of item type.
     */
    @XmlElement(name = "ItemType")
    private int itemType;

    /**
     * @return the itemName
     */
    @ApiModelProperty(value="商品名称", notes="商品名称")
    public String getItemName() {
        return itemName;
    }

    /**
     * @param itemName the itemName to set
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    /**
     * @return the mdInternal
     */
    @ApiModelProperty(value="商品コード", notes="商品コード")
    public String getMdInternal() {
        return mdInternal;
    }

    /**
     * @param mdInternal the mdInternal to set
     */
    public void setMdInternal(String mdInternal) {
        this.mdInternal = mdInternal;
    }

    /**
     * @return the displayOrder
     */
    @ApiModelProperty(value="表示順", notes="表示順")
    public int getDisplayOrder() {
        return displayOrder;
    }

    /**
     * @param displayOrder the displayOrder to set
     */
    public void setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
    }

    /**
     * @return the itemType
     */
    @ApiModelProperty(value="分類コード", notes="分類コード")
    public int getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "ItemPickList [itemName=" + itemName + ", mdInternal=" + mdInternal + ", displayOrder=" + displayOrder
                + ", itemType=" + itemType + "]";
    }


}
