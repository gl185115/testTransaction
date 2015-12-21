/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D

*
* JavaFramework
*
* A Model Class for JavaFramework
*
* del Rio, Rica Marie M.
*/

package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * JavaFramework Model class for application's software vesion.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "JavaFramework")
public class JavaFramework extends SoftwareVersion  {

    /**
     * Constructor for Java Framework version.
     */
    public JavaFramework() {    	
        this.setName("J2EE");
        this.setVersion(System.getProperty("java.version"));        
    }
}
