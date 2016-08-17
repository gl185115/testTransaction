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

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Associate Model Object.
 *
 * <P>A Associate Node in POSLog XML.
 *
 * <P>The Associate node is under RetailTransaction Node.
 * And mainly holds the information of the Associate's transaction
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Associate")
public class Associate {
    /**
     * The private member variable that holds the AssociateID
     */
    @XmlElement(name = "AssociateID")
    private String associateID;
    
    /**
     * The private member variable that holds the AssociateName
     */
    @XmlElement(name = "AssociateName")
    private String associateName;
    
    /**
     * The private member variable that holds the AssociateKanaName
     */
    @XmlElement(name = "AssociateKanaName")
    private String associateKanaName;
    
    /**
     * @return the associateID
     */
    public final String getAssociateID() {
        return associateID;
    }

    /**
     * @param associateID the associateID to set
     */
    public final void setAssociateID(final String associateID) {
        this.associateID = associateID;
    }

    /**
     * @return the associateName
     */
    public final String getAssociateName() {
        return associateName;
    }

    /**
     * @param associateName the associateName to set
     */
    public final void setAssociateName(final String associateName) {
        this.associateName = associateName;
    }

    /**
     * @return the associateKanaName
     */
    public final String getAssociateKanaName() {
        return associateKanaName;
    }

    /**
     * @param associateKanaName the associateKanaName to set
     */
    public final void setAssociateKanaName(final String associateKanaName) {
        this.associateKanaName = associateKanaName;
    }
}
