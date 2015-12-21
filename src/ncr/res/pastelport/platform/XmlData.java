// Copyright(c) 2012 NCR/JAPAN
//
// $Log: XmlData.java,v $

//
//
// $Id: XmlData.java, Exp $
//
package ncr.res.pastelport.platform;

/**
 * NCR Payment from Client send XML tag keep value.
 *
 * @version $syx: 1.2 $ $Date: 2012/04/17 03:03:30 $
 */
public class XmlData {
    /** The Xml Value. */
    private String value;
    /** The flag if Encrypted or not. */
    private boolean enc;
    /** The flag if to Log or not. */
    private boolean log;

    /**
     * new a XmlData enc is false log is true.
     */
    public XmlData() {
        value = "";
        enc = false;
        log = true;
    }

    /**
     * @param valueToSet new a Xmldate with value.
     */
    public XmlData(final String valueToSet) {
        this();
        this.value = valueToSet;
    }

    /**
     * @return value The Xml Value.
     */
    public final String getValue() {
        return value;
    }

    /**
     * @return   The flag for Encrypted or not.
     */
    public final boolean isEncrypted() {
        return enc;
    }

    /**
     * @return True if can Log, else, false.
     */
    public final boolean canLogging() {
        return log;
    }

    /**
     * @param valueToSet The Value for the xml.
     */
    public final void setValue(final String valueToSet) {
        this.value = valueToSet;
    }

    /**
     * @param encToSet The value for if Encrypted Flag.
     */
    public final void setEncrypted(final boolean encToSet) {
        this.enc = encToSet;
    }

    /**
     * @param logToSet Set if to Log or not.
     */
    public final void setLogging(final boolean logToSet) {
        this.log = logToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(hashCode());
        sb.append(":value=").append(getValue());
        sb.append(" encrypt=").append(isEncrypted());
        sb.append(" logging=").append(canLogging());
        sb.append("]");
        return sb.toString();
    }

}
