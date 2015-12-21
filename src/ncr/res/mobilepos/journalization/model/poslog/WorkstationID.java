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
import javax.xml.bind.annotation.XmlValue;

/**
 * WorkstationID Model Object.
 *
 * <P>A WorkstationID Node in POSLog XML.
 *
 * <P>The WorkstationID node is under Transaction Node.
 * And mainly holds the information of the WorkstationID's TerminalType
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "WorkstationID")
public class WorkstationID {
    /**
     *
     */
    @XmlValue
    private String value;
    
    /**
     * The private member variable that holds
     * WorkstationID node specific terminalType.
     */
    @XmlAttribute(name = "TerminalType")
    private String terminalType;

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
     * @return the terminalType
     */
    public final String getTerminalType() {
        return terminalType;
    }

    /**
     * @param terminalType the terminalType to set
     */
    public final void setTerminalType(String terminalType) {
        this.terminalType = terminalType;
    }
}
