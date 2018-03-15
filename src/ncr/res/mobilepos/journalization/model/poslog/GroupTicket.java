/*
 * Copyright (c) 2015 NCR/JAPAN Corporation SW-R&D
 *
 * Group Ticket
 *
 * Model Class for GroupTicket
 *
 *
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * GroupTicket Model Object.
 *
 * <P>A CreditDebit Node in POSLog XML.
 *
 * <P>The GroupTicket node is under TillSettle Node.
 *
 * @see           TillSettle
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GroupTicket")
public class GroupTicket {

    /***
     * registeredCount;
     */
    @XmlElement(name = "RegisteredCount")
    private String registeredCount;
    /***
     * registeredCountinStore;
     */
    @XmlElement(name = "RegisteredCountInStore")
    private String registeredCountinstore;
    /***
     * total
     */
    @XmlElement(name = "TotalCount")
    private String totalCount;

    /**
     * Default Constructor for GroupTicket.
     *
     * <P>Initializes member variable.
     */
    public GroupTicket() {
    }

    /**
     * Set registeredCount.
     * @param regCount
     */
    public final void setRegisteredCount(String regCount) {
    	this.registeredCount = regCount;
    }
    /***
     * Return registeredCount
     * @return
     */
    public final String getRegisteredCount() {
    	return this.registeredCount;
    }
    /**
     * Set registeredCountinStore.
     * @param regCount
     */
    public final void setRegisteredCountInStore(String regCount) {
    	this.registeredCountinstore = regCount;
    }
    /***
     * Return registeredCountinstore
     * @return
     */
    public final String getRegisteredCountInStore() {
    	return this.registeredCountinstore;
    }
    /**
     * set TotalCount
     */
    public final void setTotalCount(String regCount) {
    	this.totalCount = regCount;
    }
    /***
     * get TotalCount
     */
    public final String getTotalCount() {
    	return this.totalCount;
    }
}
