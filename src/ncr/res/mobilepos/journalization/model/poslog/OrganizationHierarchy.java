/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for OrganizationHierarchy
 *
 * Abenoja, Evan Joseph L.
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.Collections;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * OrganizationHierarchy Model Object.
 *
 * <P>A OrganizationHierarchy Node in POSLog XML.
 *
 * <P>The OrganizationHierarchy node is under Transaction Node.
 * And mainly holds the information of the Operating Company and its ID.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OrganizationHierarchy")
public class OrganizationHierarchy {
    /**
     *
     */
    @XmlValue
    private String value;
   
    /**
     * The private member variable that holds
     * OrganizationHierarchy node Level (Operating Company).
     */
    @XmlAttribute(name = "Level")
    private String level;

    /**
     * The private member variable that holds the ID of the Operating Company
     */
    @XmlAttribute(name = "ID")
    private String id;


    /**
     * The default constructor for OrganizationHierarchy class.
     */
    public OrganizationHierarchy() {
    }

    /**
     * Sets the level of the organization hierarchy.
     *
     * @param levelToSet    The new value for version in
     *                          organization hierarchy.
     */
    public final void setLevel(final String levelToSet) {
        this.level = levelToSet;
    }

    /**
     * Gets the level of the organization hierarchy.
     *
     * @return        The level of the organization hierarchy.
     */
    public final String getLevel() {
        return level;
    }

    /**
     * Sets the id of the organization hierarchy.
     *
     * @param idToSet   The new value for id in organization hierarchy
     */
    public final void setId(final String idToSet) {
        this.id = idToSet;
    }

    /**
     * Gets the id of the organization hierarchy.
     *
     * @return        The id of the organization hierarchy.
     */
    public final String getId() {
        return id;
    }

    /**
     * @return the value
     */
    public final String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public final void setValue(String value) {
        this.value = value;
    }

    /**
     * Override the toString() Method.
     * @return The String representation of OrganizationHierarchy.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Value : ").append(this.value).append(crlf)
        .append("Level : ").append(this.level).append(crlf)
        .append("ID : ").append(this.id).append(crlf);

        return str.toString();
    }
}
