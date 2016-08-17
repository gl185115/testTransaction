/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D

*
* WebApiSw
*
* A Model Class for WebApiSw
*
* del Rio, Rica Marie M.
* jd185128
*/


package ncr.res.mobilepos.softwareinfo.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * WebApiSw Model class for application's software version.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "WebApiSw")
public class WebApiSw extends SoftwareVersion {

    /** The wss software name default value. */
    private static String wssSoftwareName = "WebStoreServer";

    /** The wss software version default value. */
    private static String wssSoftwareVersion = "NA";

    /** The wss key name in the MANIFEST file. */
    private static String wssKeyName = "Implementation-Title";

    /** The wss key version name in the MANIFEST file. */
    private static String wssKeyVersion = "Implementation-Build";

    /**
     * Constructor for WebApiSw version.
     */
    public WebApiSw() {
        this.setName(wssSoftwareName);
        this.setVersion(wssSoftwareVersion);
    }

    /**
     * Instantiates a new webapisw.
     *
     * @param name the wss name
     * @param version the wss version
     */
    public WebApiSw(final String name, final String version) {
        super.setName(name);
        super.setVersion(version);
    }

    /** The wss information file. */
    private InputStream wssInfoFile = null;

    /**
     * Sets the inputstream of wss information file.
     *
     * @param input the new input stream
     */
    public final void setInputStream(final InputStream input) {
        if (null == wssInfoFile) {
            this.wssInfoFile = input;
        }
    }

    /**
     * Gets the input stream.
     *
     * @return the input stream
     */
    public final InputStream getInputStream() {
        return this.wssInfoFile;
    }

    /** The properties. */
    private Properties prop = new Properties();

    /**
     * Gets the properties.
     *
     * @return the properties
     */
    public final Properties getProp() {
        return this.prop;
    }

    /**
     * Sets the properties.
     *
     * @param props the new properties
     */
    public final void setProp(final Properties props) {
        this.prop = props;
    }

    /**
     * Load to properties from file.
     *
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public final void loadProperties() throws IOException {
        this.prop.load(this.getInputStream());

        if (prop.containsKey(wssKeyVersion)) {
            this.setVersion(prop.getProperty(wssKeyVersion));
        }
        if (prop.containsKey(wssKeyName)) {
            this.setName(prop.getProperty(wssKeyName));
        }
    }
}
