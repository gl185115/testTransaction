package ncr.res.mobilepos.authentication.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * model for the activation key.
 *
 */
@XmlRootElement(name = "activationKey")
public class ActivationKey {
    /**
     * the activation key.
     */
    private String activationKey;
    /**
     * the signature id.
     */
    private String signTid;

    /**
     * constructor.
     */
    public ActivationKey() {

    }

    /**
     * constructor.
     * @param activationKeyToSet - the activation key
     * @param signTidToSet - the signature id
     */
    public ActivationKey(final String activationKeyToSet,
            final String signTidToSet) {
        this.activationKey = activationKeyToSet;
        this.signTid = signTidToSet;
    }

    /**
     * gets the sign activation key.
     * @return String - the activation key
     */
    @XmlElement(name = "signActivationKey")
    public final String getActivationKey() {
        return this.activationKey;
    }

    /**
     * Gets the signature tid.
     * @return String the signature tid
     */
    @XmlElement(name = "signTid")
    public final String getSignatureTid() {
        return this.signTid;
    }

    /**
     * Converts {@link ActivationKey} model to String.
     * @return String
     */
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("; ActivationKey: ")
            .append(this.getActivationKey());
        sb.append("; SignatureTid: ")
            .append(this.getSignatureTid());
        return sb.toString();
    }
}
