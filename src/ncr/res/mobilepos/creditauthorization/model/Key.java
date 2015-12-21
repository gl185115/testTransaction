package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Class Key.
 */
@XmlRootElement(name = "Key")
public class Key extends ResultBase {

    /** The key. */
    private String key;

    /** The key64. */
    private String key64;

    /** The old key. */
    private String oldKey;

    /** The old key64. */
    private String oldKey64;

    /** The generated date. */
    private String genDate;

    /**
     * Gets the gen date.
     *
     * @return the gen date
     */
    @XmlElement(name = "genDate")
    public final String getGenDate() {
        return this.genDate;
    }

    /**
     * Sets the gen date.
     *
     * @param generatedDate
     *            the new gen date
     */
    public final void setGenDate(final String generatedDate) {
        this.genDate = generatedDate;
    }

    /**
     * Gets the key.
     *
     * @return the key
     */
    @XmlElement(name = "key")
    public final String getKey() {
        return this.key;
    }

    /**
     * Sets the key.
     *
     * @param keyToSet
     *            the new key
     */
    public final void setKey(final String keyToSet) {
        this.key = keyToSet;
    }

    /**
     * Gets the key64.
     *
     * @return the key64
     */
    @XmlElement(name = "key64")
    public final String getKey64() {
        return this.key64;
    }

    /**
     * Sets the key64.
     *
     * @param key64ToSet
     *            the new key64
     */
    public final void setKey64(final String key64ToSet) {
        this.key64 = key64ToSet;
    }

    /**
     * Gets the old key.
     *
     * @return the old key
     */
    @XmlElement(name = "oldKey")
    public final String getOldKey() {
        return this.oldKey;
    }

    /**
     * Sets the old key.
     *
     * @param keyOld
     *            the new old key
     */
    public final void setOldKey(final String keyOld) {
        this.oldKey = keyOld;
    }

    /**
     * Gets the old key64.
     *
     * @return the old key64
     */
    @XmlElement(name = "oldKey64")
    public final String getOldKey64() {
        return this.oldKey64;
    }

    /**
     * Sets the old key64.
     *
     * @param key64Old
     *            the new old key64
     */
    public final void setOldKey64(final String key64Old) {
        this.oldKey64 = key64Old;
    }
}
