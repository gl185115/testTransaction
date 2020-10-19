package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class AccountInfoRequest {
    
    @XmlElement(name = "authKey")
    private String authKey;
    
    @XmlElement(name = "memberID")
    private String memberID;

    /**
     * @return the authKey
     */
    public final String getAuthKey() {
        return authKey;
    }

    /**
     * @param authKey the authKey to set
     */
    public final void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    /**
     * @return the memberID
     */
    public final String getMemberID() {
        return memberID;
    }

    /**
     * @param memberID the memberID to set
     */
    public final void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    @Override
    public String toString() {
        return "AccountInfoRequest [" + (authKey != null ? "authKey=" + authKey + ", " : "")
                + (memberID != null ? "memberID=" + memberID : "") + "]";
    }
}
