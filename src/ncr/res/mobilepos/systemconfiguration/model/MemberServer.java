package ncr.res.mobilepos.systemconfiguration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MemberServer")
public class MemberServer {
    /**
     * create an empty instance.
     */
    public MemberServer() {
    }

    /**
     * The Server Uri.
     */
    @XmlElement(name = "ServerUri")
    private String serverUri;
    /**
     * The Corp Id.
     */
    @XmlElement(name = "CorpId")
    private String corpId;
    /**
     * @return the serverUri
     */
    public final String getServerUri() {
        return serverUri;
    }
    /**
     * @param serverUriToSet
     *  the serverUri to set
     */
    public final void setServerUri(
            final String serverUriToSet) {
        this.serverUri = serverUriToSet;
    }
    /**
     * @return the corpId
     */
    public final String getCorpId() {
        return corpId;
    }
    /**
     * @param corpIdToSet
     *  the corpId to set
     */
    public final void setCorpId(
            final String corpIdToSet) {
        this.corpId = corpIdToSet;
    }
}

