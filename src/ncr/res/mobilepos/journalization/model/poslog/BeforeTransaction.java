/*
 * Copyright (c) 2011,2015 NCR/JAPAN Corporation SW-R&D
 *
 * Transaction
 *
 * Model Class for Transaction
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transaction Model Object.
 *
 * <P>A Before Transaction Node in POSLog XML.
 *
 * <P>The Before Transaction node is under POSLog Node.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BeforeTransaction")
public class BeforeTransaction {

    /**
     * The private member variable that holds the number of items
     * of the BeforeTransaction.
     */
    @XmlElement(name = "ItemCount")
    private int itemCount;

    /**
     * The private member variable that holds the line items
     * of the BeforeTransaction.
     */
    @XmlElement(name = "LineItem")
    private List<LineItem> lineItems;
    
    /**
     * The private member variable that holds the Total
     * information of the BeforeTransaction..
     */
    @XmlElement(name = "Total")
    private List<Total> total;

    /**
     * Sets the item count of a customer order transaction.
     *
     * @param The new value for item count.
     */
    public final void setItemCount(final int itemCount) {
        this.itemCount = itemCount;
    }

    /**
     * Gets the item count in a customer order transaction.
     *
     * @return The item count in customer order transaction.
     */
    public final int getItemCount() {
        return itemCount;
    }

    /**
     * Gets the LineItems in List.
     *
     * @return The lineItems of a customer order transaction.
     */
    public final List<LineItem> getLineItems() {
        if (lineItems == null) {
            return Collections.emptyList();
        }
        return lineItems;
    }

    /**
     * Sets the LineItems in List.
     *
     * @param The new value for LineItems in List.
     */
    public final void setLineItems(final List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }
    
    /**
     * The Getter method for Total.
     * @return  The Total.
     */
    public final List<Total> getTotal() {
        return total;
    }
    /**
     * The Setter method for Total.
     * @param The Total.
     */
    public final void setTotal(final List<Total> total) {
        this.total = total;
    }
}
