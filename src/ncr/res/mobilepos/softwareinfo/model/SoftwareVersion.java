/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D

*
* SoftwareVersion
*
* A Model Class for SoftwareVersion
*
* del Rio, Rica Marie M.
*/

package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Sofware Version  Model class for application's software version.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SoftwareVersion")
public class SoftwareVersion extends ResultBase {

    /**
     * Name of the application software.
     */
    @XmlElement(name = "Name")
    private String name = "RES Transaction Service";

    /**
     * Version of the application software.
     */
    @XmlElement(name = "Version")
    private String version = "3.3.0";
    /**
     * Getter for name of the application software.
     * @return  Name of the application software
     */
    public final String getName() {
        return name;
    }
    /**
     * Getter for name of the application software.
     * @return Version of the application software
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Setter for name of the application software.
     * @param appName  Name of the application software
     */
    public final void setName(final String appName) {
        name = appName;
    }
    /**
     * Setter for version of the application software.
     * @param appVersion   Version of the application software
     */
    public final void setVersion(final String appVersion) {
        version = appVersion;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        String delimiter = "; ";
        sb.append("Name: ").append(this.getName()).append(delimiter);
        sb.append("Version: ").append(this.getVersion()).append(delimiter);
        sb.append(super.toString()).append(delimiter);
        return sb.toString();
    }
}
