/*
* Copyright (c) 2011-2012,2015 NCR/JAPAN Corporation SW-R&D

*
* Operating System Info
*
* A Model Class for OperatingSystemInfo
*
*/

package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * OperatingSystemInfo Model class for application's software vesion.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OperatingSystemInfo")
public class OperatingSystemInfo extends SoftwareVersion  {

    /**
     * Constructor for Operating System version.
     */
    public OperatingSystemInfo() {    	
        this.setName("OperatingSystem");
        this.setVersion(System.getProperty("os.name")
                        + "(" + System.getProperty("os.arch")
                        + ")" + System.getProperty("os.version"));
    }
}
