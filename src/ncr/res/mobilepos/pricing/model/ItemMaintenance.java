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

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for Item Maintenance.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "maintenance")
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
