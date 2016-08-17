/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * BusinessUnit
 *
 * Model Class for BusinessUnit
 *
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * BusinessUnit Model Object.
 *
 * <P>A BusinessUnit Node in POSLog XML.
 *
 * <P>The BusinessUnit node is under RetailTransaction Node.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BusinessUnit")
public class BusinessUnit {
    
    /** The Name. */
    @XmlAttribute(name = "Name")
    private String name;
    
    /** The VoucherName. */
    @XmlAttribute(name = "VoucherName")
    private String voucherName;
    
    /** The Telephone. */
    @XmlElement(name = "Telephone")
    private Telephone telephone;
    
    /** The Address. */
    @XmlElement(name = "Address")
    private String address;
    
    /**
     * @return the name
     */
    public final String getName() {
        return name;
    }
    /**
     * @param name the name to set
     */
    public final void setName(String name) {
        this.name = name;
    }
    /**
     * @return the voucherName
     */
    public final String getVoucherName() {
        return voucherName;
    }
    /**
     * @param voucherName the voucherName to set
     */
    public final void setVoucherName(String voucherName) {
        this.voucherName = voucherName;
    }
    /**
     * @return the telephone
     */
    public final Telephone getTelephone() {
        return telephone;
    }
    /**
     * @param telephone the telephone to set
     */
    public final void setTelephone(Telephone telephone) {
        this.telephone = telephone;
    }
    /**
     * @return the address
     */
    public final String getAddress() {
        return address;
    }
    /**
     * @param address the address to set
     */
    public final void setAddress(String address) {
        this.address = address;
    }
}
