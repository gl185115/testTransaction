/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Item
 *
 * Model class for item object.
 *
 */
package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for Item Maintenance.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "maintenance")
@ApiModel(value="ItemMaintenance")
public class ItemMaintenance extends ResultBase {

    /**
     * The Retail Store ID.
     */
    @XmlElement(name = "RetailStoreID")
    private String storeid;

    /**
     * The WorkstationID.
     */
    @XmlElement(name = "WorkstationID")
    private String workstationid;

    /**
     * The Item information.
     */
    @XmlElement(name = "Item")
    private Item item;


    /**
     * The getters for Store ID.
     * @return The Store ID.
     */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public final String getStoreid() {
        return storeid;
    }

    /**
     * Setters for Store ID.
     * @param storeidtoset  The new Store ID.
     */
    public final void setStoreid(final String storeidtoset) {
        this.storeid = storeidtoset;
    }

    /**
     * Getters for Workstation ID.
     * @return The Workstation ID.
     */
    @ApiModelProperty(value="作業台コード", notes="作業台コード")
    public final String getWorkstationid() {
        return workstationid;
    }

    /**
     * Setters for WorkstationID.
     * @param workstationidtoset    The new Workstation ID.
     */
    public final void setWorkstationid(final String workstationidtoset) {
        this.workstationid = workstationidtoset;
    }

    /**
     * Setters for Item.
     * @param itemToSet The new Item value.
     */
    public final void setItem(final Item itemToSet) {
        this.item = itemToSet;
    }

    /**
     * Getters for item.
     * @return The Item.
     */
    @ApiModelProperty(value="商品", notes="商品")
    public final Item getItem() {
        return this.item;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("RetailStoreID: " + storeid).append("; ");
        sb.append("WorkstationID: " + workstationid).append("; ");
        sb.append("Item: " + item);
        return sb.toString();
    }


}
