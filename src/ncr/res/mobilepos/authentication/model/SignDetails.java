package ncr.res.mobilepos.authentication.model;
/**
 * Model that receives Sign Details information from AUT_DEVICES table.
 * @author CM185093
 *
 */
public class SignDetails {
    /**
     * the signature status.
     */
    private int signStatus = 0;
    /**
     * the signature tid.
     */
    private String signTid = "";
    /**
     * the signature activation key.
     */
    private String signActivationKey = "";

    /**
     * gets the signature tid.
     * @return String - the signature tid
     */
    public final String getSignTid() {
        return signTid;
    }
    /**
     * sets the signature tid.
     * @param signTidToSet - the signature tid to set
     */
    public final void setSignTid(final String signTidToSet) {
        this.signTid = signTidToSet;
    }
    /**
     * gets the activation key.
     * @return String - the activation key.
     */
    public final String getSignActivationKey() {
        return signActivationKey;
    }
    /**
     * sets the activation key.
     * @param signActivationKeyToSet - the activation key to set
     */
    public final void setSignActivationKey(
            final String signActivationKeyToSet) {
        this.signActivationKey = signActivationKeyToSet;
    }
    /**
     * gets the sign status.
     * @return int - the status of the signature
     */
    public final int getSignStatus() {
        return signStatus;
    }
    /**
     * sets the sign status.
     * @param signStatusToSet - the sign status to set
     */
    public final void setSignStatus(final int signStatusToSet) {
        this.signStatus = signStatusToSet;
    }
    /**
     * Converts SignDetails model to String.
     * @return String
     */
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("; SignActivationKey: ")
            .append(this.getSignActivationKey());
        sb.append("; SignTid: ")
            .append(this.getSignTid());
        sb.append("; SignStatus: ")
            .append(this.getSignStatus());
        return sb.toString();
    }
}
