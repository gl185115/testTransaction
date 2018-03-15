/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Disposal
 *
 * Model Class for Disposal
 *
 * Carlos G. Campos
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Disposal Model Object.
 *
 * <P>A Disposal Node in POSLog XML.
 *
 * <P>The Disposal node is under Return Node.
 * And mainly holds the method of Disposal.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Disposal")
public class Disposal {
    /**
     * The private member variable that holds the method of the Disposal.
     */
    @XmlAttribute(name = "Method")
    private String method;

    /**
     * Gets the method in Disposal.
     *
     * @return        Returns the method in Disposal.
     */
    public final String getMethod() {
        return method;
    }

    /**
     * Sets the method in Disposal.
     *
     * @param methodToSet        The new value for the method in Disposal.
     */
    public final void setMethod(final String methodToSet) {
        this.method = methodToSet;
    }

    /**
     * Overrides the toString() Method.
     * @return The string representation of disposal.
     */
    public final String toString() {
        return "Method: " + this.method;
    }
}
