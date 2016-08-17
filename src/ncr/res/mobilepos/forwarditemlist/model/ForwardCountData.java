/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ForwardCountData
 *
 * Model Class
 */
package ncr.res.mobilepos.forwarditemlist.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ForwardCountData Model object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemForward")
public class ForwardCountData {

    /**
     * The status of get count response.
     */
    @XmlElement(name = "status")
    private String status;

    /**
     * The count of Forward Item.
     */
    @XmlElement(name = "ForwardCount")
    private String forwardCount;

    /**
     * The Default Constructor.
     */
    public ForwardCountData() {
        this.status = "0";
        this.forwardCount = "0";
    }

    /**
     * @return status
     */
    public final String getStatus() {
        return status;
    }

    /**
     * @param statusToSet  status
     */
    public final void setStatus(final String statusToSet) {
        this.status = statusToSet;
    }

    /**
     * @return forwardCount
     */
    public final String getForwardCount() {
        return forwardCount;
    }

    /**
     * @param forwardCountToSet  forwardCount
     */
    public final void setForwardCount(final String forwardCountToSet) {
        this.forwardCount = forwardCountToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Status: ").append(status).append("; ");
        sb.append("Forward Count: ").append(forwardCount).append("; ");
        return sb.toString();
    }
}
