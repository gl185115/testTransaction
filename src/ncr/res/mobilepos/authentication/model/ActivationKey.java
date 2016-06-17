package ncr.res.mobilepos.authentication.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * model for the activation key.
 *
 */
@XmlRootElement(name = "activationKey")
@ApiModel(value="ActivationKey")
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
    @ApiModelProperty(value="サインアクティベーションキー", notes="サインアクティベーションキー")
    public final String getActivationKey() {
        return this.activationKey;
    }

    /**
     * Gets the signature tid.
     * @return String the signature tid
     */
    @XmlElement(name = "signTid")
    @ApiModelProperty(value="シグネチャーTID", notes="シグネチャーTID")
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
