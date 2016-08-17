/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* TomCatServer
*
* A Model Class for TomCatServer
*
* del Rio, Rica Marie M.
*/

package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * TomCat Server Model class for application's software version.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TomCatServer")
public class TomCatServer extends SoftwareVersion  {
    /**
     * Constructor for TomCat Server version.
     */
    public TomCatServer() {
        this.setName("Tomcat");
        this.setVersion("7.0");
    }
}
