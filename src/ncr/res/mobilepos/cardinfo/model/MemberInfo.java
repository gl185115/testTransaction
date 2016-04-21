package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GetMemberInfo")
public class MemberInfo extends ResultBase {
    @XmlElement(name = "CompanyId")
    private String companyId;
    @XmlElement(name = "StoreId")
    private String storeId;
    @XmlElement(name = "MemberId")
    private String memberId;
    @XmlElement(name = "CardCompanyId")
    private String cardCompanyId;
    @XmlElement(name = "MemberId1")
    private String memberId1;
    @XmlElement(name = "MemberId2")
    private String memberId2;
    @XmlElement(name = "CardType")
    private String cardType;
    @XmlElement(name = "MemberRank")
    private String memberRank;
    @XmlElement(name = "MemberStatus")
    private String memberStatus;
    @XmlElement(name = "MemberType")
    private String memberType;
    @XmlElement(name = "MemberTypeNext")
    private String memberTypeNext;
    @XmlElement(name = "MemberTypeUpdDate")
    private String memberTypeUpdDate;
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
    public final String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId
     *            the companyId to set
     */
    public final void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the storeId
     */
    public final String getStoreId() {
        return storeId;
    }

    /**
     * @param storeId
     *            the storeId to set
     */
    public final void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    /**
     * @return the memberId
     */
    public final String getMemberId() {
        return memberId;
    }

    /**
     * @param memberId
     *            the memberId to set
     */
    public final void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * @return the cardCompanyId
     */
    public final String getCardCompanyId() {
        return cardCompanyId;
    }

    /**
     * @param cardCompanyId
     *            the cardCompanyId to set
     */
    public final void setCardCompanyId(String cardCompanyId) {
        this.cardCompanyId = cardCompanyId;
    }

    /**
     * @return the memberId1
     */
    public final String getMemberId1() {
        return memberId1;
    }

    /**
     * @param memberId1
     *            the memberId1 to set
     */
    public final void setMemberId1(String memberId1) {
        this.memberId1 = memberId1;
    }

    /**
     * @return the memberId2
     */
    public final String getMemberId2() {
        return memberId2;
    }

    /**
     * @param memberId2
     *            the memberId2 to set
     */
    public final void setMemberId2(String memberId2) {
        this.memberId2 = memberId2;
    }

    /**
     * @return the cardType
     */
    public final String getCardType() {
        return cardType;
    }

    /**
     * @param cardType
     *            the cardType to set
     */
    public final void setCardType(String cardType) {
        this.cardType = cardType;
    }

    /**
     * @return the memberRank
     */
    public final String getMemberRank() {
        return memberRank;
    }

    /**
     * @param memberRank
     *            the memberRank to set
     */
    public final void setMemberRank(String memberRank) {
        this.memberRank = memberRank;
    }

    /**
     * @return the memberStatus
     */
    public final String getMemberStatus() {
        return memberStatus;
    }

    /**
     * @param memberStatus
     *            the memberStatus to set
     */
    public final void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    /**
     * @return the memberType
     */
    public final String getMemberType() {
        return memberType;
    }

    /**
     * @param memberType
     *            the memberType to set
     */
    public final void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    /**
     * @return the memberTypeNext
     */
    public final String getMemberTypeNext() {
        return memberTypeNext;
    }

    /**
     * @param memberTypeNext
     *            the memberTypeNext to set
     */
    public final void setMemberTypeNext(String memberTypeNext) {
        this.memberTypeNext = memberTypeNext;
    }

    /**
     * @return the memberTypeUpdDate
     */
    public final String getMemberTypeUpdDate() {
        return memberTypeUpdDate;
    }

    /**
     * @param memberTypeUpdDate
     *            the memberTypeUpdDate to set
     */
    public final void setMemberTypeUpdDate(String memberTypeUpdDate) {
        this.memberTypeUpdDate = memberTypeUpdDate;
    }

    /**
     * @return the subCode1
     */
    public final String getSubCode1() {
        return subCode1;
    }

    /**
     * @param subCode1
     *            the subCode1 to set
     */
    public final void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    /**
     * @return the subCode2
     */
    public final String getSubCode2() {
        return subCode2;
    }

    /**
     * @param subCode2
     *            the subCode2 to set
     */
    public final void setSubCode2(String subCode2) {
        this.subCode2 = subCode2;
    }

    /**
     * @return the subCode3
     */
    public final String getSubCode3() {
        return subCode3;
    }

    /**
     * @param subCode3
     *            the subCode3 to set
     */
    public final void setSubCode3(String subCode3) {
        this.subCode3 = subCode3;
    }

    /**
     * @return the subCode4
     */
    public final String getSubCode4() {
        return subCode4;
    }

    /**
     * @param subCode4
     *            the subCode4 to set
     */
    public final void setSubCode4(String subCode4) {
        this.subCode4 = subCode4;
    }

    /**
     * @return the subCode5
     */
    public final String getSubCode5() {
        return subCode5;
    }

    /**
     * @param subCode5
     *            the subCode5 to set
     */
    public final void setSubCode5(String subCode5) {
        this.subCode5 = subCode5;
    }

    /**
     * @return the subNum1
     */
    public final String getSubNum1() {
        return subNum1;
    }

    /**
     * @param subNum1
     *            the subNum1 to set
     */
    public final void setSubNum1(String subNum1) {
        this.subNum1 = subNum1;
    }

    /**
     * @return the subNum2
     */
    public final String getSubNum2() {
        return subNum2;
    }

    /**
     * @param subNum2
     *            the subNum2 to set
     */
    public final void setSubNum2(String subNum2) {
        this.subNum2 = subNum2;
    }

    /**
     * @return the subNum3
     */
    public final String getSubNum3() {
        return subNum3;
    }

    /**
     * @param subNum3
     *            the subNum3 to set
     */
    public final void setSubNum3(String subNum3) {
        this.subNum3 = subNum3;
    }

    /**
     * @return the subNum4
     */
    public final String getSubNum4() {
        return subNum4;
    }

    /**
     * @param subNum4
     *            the subNum4 to set
     */
    public final void setSubNum4(String subNum4) {
        this.subNum4 = subNum4;
    }

    /**
     * @return the subNum5
     */
    public final String getSubNum5() {
        return subNum5;
    }

    /**
     * @param subNum5
     *            the subNum5 to set
     */
    public final void setSubNum5(String subNum5) {
        this.subNum5 = subNum5;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "MemberInfo [companyId=" + companyId + ", storeId=" + storeId + ", memberId=" + memberId
                + ", cardCompanyId=" + cardCompanyId + ", memberId1=" + memberId1 + ", memberId2=" + memberId2
                + ", cardType=" + cardType + ", memberRank=" + memberRank + ", memberStatus=" + memberStatus
                + ", memberType=" + memberType + ", memberTypeNext=" + memberTypeNext + ", memberTypeUpdDate="
                + memberTypeUpdDate + ", subCode1=" + subCode1 + ", subCode2=" + subCode2 + ", subCode3=" + subCode3
                + ", subCode4=" + subCode4 + ", subCode5=" + subCode5 + ", subNum1=" + subNum1 + ", subNum2=" + subNum2
                + ", subNum3=" + subNum3 + ", subNum4=" + subNum4 + ", subNum5=" + subNum5 + "]";
    }

}
