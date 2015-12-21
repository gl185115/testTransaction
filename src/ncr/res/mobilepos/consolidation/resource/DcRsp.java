/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * DcRsp
 *
 * DcRsp is a Model Class representing the response for Consolidation Requester
 *
 * Campos, Carlos  (cc185102)
 */
package ncr.res.mobilepos.consolidation.resource;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


/**
 * DcRsp is a Model Class representing the response for Consolidation Requester.
 *
 */
@XmlRootElement(name = "DcRsp")
public class DcRsp {

    /**
     * DCResponse status.
     */
    private int status;

    /**
     * Gets status.
     *
     * @return status.
     */
    @XmlElement(name = "Status")
    public final int getStatus() {
        return status;
    }

    /**
     * Sets status.
     *
     * @param statusToSet
     *            status to set.
     */
    public final void setStatus(final int statusToSet) {
        this.status = statusToSet;
    }

    @Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        str.append("{Status: ").append(this.getStatus()).append("}");
        return str.toString();
    }
}
