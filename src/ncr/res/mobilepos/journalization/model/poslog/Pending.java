/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for RetailTransaction
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Pending Model Object.
 *
 * <P>A Pending Node in POSLog XML.
 *
 * <P>The Pending node is under RetailTransaction Node.
 * And mainly holds the information of the Pending
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Pending")
public class Pending {

    /**
     * The private member variable that holds the SlipNo
     * of the customer.
     */
    @XmlElement(name = "SlipNo")
    private String slipNo;

    /**
     * The private member variable that holds the delivery retail store Id
     * of the customer.
     */
    @XmlElement(name = "DeliveryRetailStoreId")
    private String deliveryRetailStoreId;

    /**
     * @return the slipNo
     */
    public String getSlipNo() {
        return slipNo;
    }

    /**
     * @param slipNo the slipNo to set
     */
    public void setSlipNo(final String slipNo) {
        this.slipNo = slipNo;
    }

    /**
     * @return the deliveryRetailStoreId
     */
    public String getDeliveryRetailStoreId() {
        return deliveryRetailStoreId;
    }

    /**
     * @param deliveryRetailStoreId the deliveryRetailStoreId to set
     */
    public void setDeliveryRetailStoreId(final String deliveryRetailStoreId) {
        this.deliveryRetailStoreId = deliveryRetailStoreId;
    }
}
