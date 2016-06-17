package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GeStatusInfo")
@ApiModel(value="StatusInfo")
public class StatusInfo extends ResultBase {
    @XmlElement(name = "CompanyId")
    private String companyId;
    @XmlElement(name = "StoreId")
    private String storeId;
    @XmlElement(name = "MemberStatus")
    private String memberStatus;
    @XmlElement(name = "MemberStatusName")
    private String memberStatusName;
    @XmlElement(name = "RegisterFlag")
    private String registerFlag;
    @XmlElement(name = "PointAddFlag")
    private String pointAddFlag;
    @XmlElement(name = "PointUseFlag")
    private String pointUseFlag;
    @XmlElement(name = "SubCode1")
    private String subCode1;
    @XmlElement(name = "SubCode2")
    private String subCode2;
    @XmlElement(name = "SubCode3")
    private String subCode3;
    @XmlElement(name = "SubCode4")
    private String subCode4;
    @XmlElement(name = "SubCode5")
    private String subCode5;
    @XmlElement(name = "SubNum1")
    private String subNum1;
    @XmlElement(name = "SubNum2")
    private String subNum2;
    @XmlElement(name = "SubNum3")
    private String subNum3;
    @XmlElement(name = "SubNum4")
    private String subNum4;
    @XmlElement(name = "SubNum5")
    private String subNum5;

    /**
     * @return the companyId
     */
    @ApiModelProperty(value="âÔé–ÉRÅ[Éh", notes="âÔé–ÉRÅ[Éh")
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the storeId
     */
    @ApiModelProperty(value="ìXï‹ÉRÅ[Éh", notes="ìXï‹ÉRÅ[Éh")
    public String getStoreId() {
        return storeId;
    }

    /**
     * @param storeId the storeId to set
     */
    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * @return the memberStatus
     */
    @ApiModelProperty(value="âÔàıèÛë‘ÉRÅ[Éh", notes="âÔàıèÛë‘ÉRÅ[Éh")
    public String getMemberStatus() {
        return memberStatus;
    }

    /**
     * @param memberStatus the memberStatus to set
     */
    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    /**
     * @return the memberStatusName
     */
    @ApiModelProperty(value="âÔàıèÛë‘ñºèÃ", notes="âÔàıèÛë‘ñºèÃ")
    public String getMemberStatusName() {
        return memberStatusName;
    }

    /**
     * @param memberStatusName the memberStatusName to set
     */
    public void setMemberStatusName(String memberStatusName) {
        this.memberStatusName = memberStatusName;
    }

    /**
     * @return the registerFlag
     */
    @ApiModelProperty(value="âÔàıìoò^â¬î€", notes="âÔàıìoò^â¬î€")
    public String getRegisterFlag() {
        return registerFlag;
    }

    /**
     * @param registerFlag the registerFlag to set
     */
    public void setRegisterFlag(String registerFlag) {
        this.registerFlag = registerFlag;
    }

    /**
     * @return the pointAddFlag
     */
    @ApiModelProperty(value="É|ÉCÉìÉgïtó^â¬î€", notes="É|ÉCÉìÉgïtó^â¬î€")
    public String getPointAddFlag() {
        return pointAddFlag;
    }

    /**
     * @param pointAddFlag the pointAddFlag to set
     */
    public void setPointAddFlag(String pointAddFlag) {
        this.pointAddFlag = pointAddFlag;
    }

    /**
     * @return the pointUseFlag
     */
    @ApiModelProperty(value="É|ÉCÉìÉgà¯ìñâ¬î€", notes="É|ÉCÉìÉgà¯ìñâ¬î€")
    public String getPointUseFlag() {
        return pointUseFlag;
    }

    /**
     * @param pointUseFlag the pointUseFlag to set
     */
    public void setPointUseFlag(String pointUseFlag) {
        this.pointUseFlag = pointUseFlag;
    }

    /**
     * @return the subCode1
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubCode1() {
        return subCode1;
    }

    /**
     * @param subCode1 the subCode1 to set
     */
    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    /**
     * @return the subCode2
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubCode2() {
        return subCode2;
    }

    /**
     * @param subCode2 the subCode2 to set
     */
    public void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    /**
     * @return the subCode3
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubCode3() {
        return subCode3;
    }

    /**
     * @param subCode3 the subCode3 to set
     */
    public void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    /**
     * @return the subCode4
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubCode4() {
        return subCode4;
    }

    /**
     * @param subCode4 the subCode4 to set
     */
    public void setSubCode4(String subCode4) {
        this.subCode4 = subCode4;
    }

    /**
     * @return the subCode5
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubCode5() {
        return subCode5;
    }

    /**
     * @param subCode5 the subCode5 to set
     */
    public void setSubCode5(String subCode5) {
        this.subCode5 = subCode5;
    }

    /**
     * @return the subNum1
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubNum1() {
        return subNum1;
    }

    /**
     * @param subNum1 the subNum1 to set
     */
    public void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }

    /**
     * @return the subNum2
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubNum2() {
        return subNum2;
    }

    /**
     * @param subNum2 the subNum2 to set
     */
    public void setSubNum2(String subNum2) {
        this.subNum2 = subNum2;
    }

    /**
     * @return the subNum3
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubNum3() {
        return subNum3;
    }

    /**
     * @param subNum3 the subNum3 to set
     */
    public void setSubNum3(String subNum3) {
        this.subNum3 = subNum3;
    }

    /**
     * @return the subNum4
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubNum4() {
        return subNum4;
    }

    /**
     * @param subNum4 the subNum4 to set
     */
    public void setSubNum4(String subNum4) {
        this.subNum4 = subNum4;
    }

    /**
     * @return the subNum5
     */
    @ApiModelProperty(value="ó\ñÒ", notes="ó\ñÒ")
    public String getSubNum5() {
        return subNum5;
    }

    /**
     * @param subNum5 the subNum5 to set
     */
    public void setSubNum5(String subNum5) {
        this.subNum5 = subNum5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "StatusInfo [companyId=" + companyId + ",storeId=" + storeId + ",memberStatus=" + memberStatus
                + ",memberStatusName=" + memberStatusName + ",registerFlag=" + registerFlag + ",pointAddFlag="
                + pointAddFlag + ",pointUseFlag=" + pointUseFlag + ",subCode1=" + subCode1 + ",subCode2=" + subCode2
                + ",subCode3=" + subCode3 + ",subCode4=" + subCode4 + ",subCode5=" + subCode5 + ",subNum1=" + subNum1
                + ",subNum2=" + subNum2 + ",subNum3=" + subNum3 + ",subNum4=" + subNum4 + ",subNum5=" + subNum5 + "]";

    }

}
