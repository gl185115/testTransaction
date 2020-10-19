package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Request")
@XmlAccessorType(XmlAccessType.NONE)
public class PassportPaymentInfoRequest {
    
    @XmlElement(name = "authKey")
    private String authKey;
    
    @XmlElement(name = "memberID")
    private String memberID;

    @XmlElement(name = "strCd")
    private String strCd;
    
    @XmlElement(name = "settlementId")
    private String settlementId;
    
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

    /**
	 * @return the strCd
	 */
	public String getStrCd() {
		return strCd;
	}

	/**
	 * @param strCd the strCd to set
	 */
	public void setStrCd(String strCd) {
		this.strCd = strCd;
	}

	/**
	 * @return the settlementId
	 */
	public String getSettlementId() {
		return settlementId;
	}

	/**
	 * @param settlementId the settlementId to set
	 */
	public void setSettlementId(String settlementId) {
		this.settlementId = settlementId;
	}

	@Override
	public String toString() {
		return "PassportPaymentInfoRequest [authKey=" + authKey + ", memberID=" + memberID + ", strCd=" + strCd
				+ ", settlementId=" + settlementId + "]";
	}
}
