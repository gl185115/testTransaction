package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
public class MemberInfoResponse extends ResultBase {

    @XmlElement(name = "MemberInfo")
    MemberInfo memberInfo;

    /**
     * @return the memberInfo
     */
    public MemberInfo getMemberInfo() {
        return memberInfo;
    }

    /**
     * @param memberInfo
     *            the memberInfo to set
     */
    public void setMemberInfo(MemberInfo memberInfo) {
        this.memberInfo = memberInfo;
    }

}
