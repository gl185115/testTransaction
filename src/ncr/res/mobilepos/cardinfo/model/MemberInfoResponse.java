package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="MemberInfoResponse")
public class MemberInfoResponse extends ResultBase {

    @XmlElement(name = "MemberInfo")
    MemberInfo memberInfo;

    /**
     * @return the memberInfo
     */
    @ApiModelProperty(value="����⏕���", notes="����⏕���")
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
