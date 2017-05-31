package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Xeb Member Points Info Model Object.
 *
 * <P>
 * A MemberInfoDetail Node in POSLog XML.
 *
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "MemberInfoDetail")
public class MemberInfoDetail {
    /**
     * The private variable that will hold the MemberCode
     */
    @XmlElement(name = "MemberCode")
    private String memberCode;

    /**
     * The private variable that will hold the RankStart
     */
    @XmlElement(name = "RankStart")
    private String rankStart;

    /**
     * The private variable that will hold the RankEnd
     */
    @XmlElement(name = "RankEnd")
    private String rankEnd;

    /**
     * The private variable that will hold the Rank
     */
    @XmlElement(name = "Rank")
    private String rank;

    /**
     * The private variable that will hold the RankName
     */
    @XmlElement(name = "RankName")
    private String rankName;

    /**
     * The private variable that will hold the SalesAmountByRankStart
     */
    @XmlElement(name = "SalesAmountByRankStart")
    private String salesAmountByRankStart;

    /**
     * The private variable that will hold the SalesAmountByRankEnd
     */
    @XmlElement(name = "SalesAmountByRankEnd")
    private String salesAmountByRankEnd;

    /**
     * The private variable that will hold the SalesAmountByRank
     */
    @XmlElement(name = "SalesAmountByRank")
    private String salesAmountByRank;

    /**
     * The private variable that will hold the SalesAmountByRankExpiration
     */
    @XmlElement(name = "SalesAmountByRankExpiration")
    private String salesAmountByRankExpiration;

    /**
     * The private variable that will hold the RankApplyAmount
     */
    @XmlElement(name = "RankApplyAmount")
    private String rankApplyAmount;

    /**
     * The private variable that will hold the NextRankStart
     */
    @XmlElement(name = "NextRankStart")
    private String nextRankStart;

    /**
     * The private variable that will hold the NextRankEnd
     */
    @XmlElement(name = "NextRankEnd")
    private String nextRankEnd;

    /**
     * The private variable that will hold the NextRank
     */
    @XmlElement(name = "NextRank")
    private String nextRank;

    /**
     * The private variable that will hold the NextRankName
     */
    @XmlElement(name = "NextRankName")
    private String nextRankName;

    /**
     * 
     * @return MemberCode
     */
    public String getMemberCode() {
        return memberCode;
    }

    /**
     * 
     * @param MemberCode
     */
    public void setMemberCode(String memberCode) {
        this.memberCode = memberCode;
    }

    /**
     * 
     * @return RankStart
     */
    public String getRankStart() {
        return rankStart;
    }

    /**
     * 
     * @param RankStart
     */
    public void setRankStart(String rankStart) {
        this.rankStart = rankStart;
    }

    /**
     * 
     * @return RankEnd
     */
    public String getRankEnd() {
        return rankEnd;
    }

    /**
     * 
     * @param RankEnd
     */
    public void setRankEnd(String rankEnd) {
        this.rankEnd = rankEnd;
    }

    /**
     * 
     * @return Rank
     */
    public String getRank() {
        return rank;
    }

    /**
     * 
     * @param Rank
     */
    public void setRank(String rank) {
        this.rank = rank;
    }

    /**
     * 
     * @return RankName
     */
    public String getRankName() {
        return rankName;
    }

    /**
     * 
     * @param RankName
     */
    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    /**
     * 
     * @return SalesAmountByRankStart
     */
    public String getSalesAmountByRankStart() {
        return salesAmountByRankStart;
    }

    /**
     * 
     * @param SalesAmountByRankStart
     */
    public void setSalesAmountByRankStart(String salesAmountByRankStart) {
        this.salesAmountByRankStart = salesAmountByRankStart;
    }

    /**
     * 
     * @return SalesAmountByRankEnd
     */
    public String getSalesAmountByRankEnd() {
        return salesAmountByRankEnd;
    }

    /**
     * 
     * @param SalesAmountByRankEnd
     */
    public void setSalesAmountByRankEnd(String salesAmountByRankEnd) {
        this.salesAmountByRankEnd = salesAmountByRankEnd;
    }

    /**
     * 
     * @return SalesAmountByRank
     */
    public String getSalesAmountByRank() {
        return salesAmountByRank;
    }

    /**
     * 
     * @param SalesAmountByRank
     */
    public void setSalesAmountByRank(String salesAmountByRank) {
        this.salesAmountByRank = salesAmountByRank;
    }

    /**
     * 
     * @return SalesAmountByRankExpiration
     */
    public String getSalesAmountByRankExpiration() {
        return salesAmountByRankExpiration;
    }

    /**
     * 
     * @param SalesAmountByRankExpiration
     */
    public void setSalesAmountByRankExpiration(String salesAmountByRankExpiration) {
        this.salesAmountByRankExpiration = salesAmountByRankExpiration;
    }

    /**
     * 
     * @return RankApplyAmount
     */
    public String getRankApplyAmount() {
        return rankApplyAmount;
    }

    /**
     * 
     * @param RankApplyAmount
     */
    public void setRankApplyAmount(String rankApplyAmount) {
        this.rankApplyAmount = rankApplyAmount;
    }

    /**
     * 
     * @return NextRankStart
     */
    public String getNextRankStart() {
        return nextRankStart;
    }

    /**
     * 
     * @param NextRankStart
     */
    public void setNextRankStart(String nextRankStart) {
        this.nextRankStart = nextRankStart;
    }

    /**
     * 
     * @return NextRankEnd
     */
    public String getNextRankEnd() {
        return nextRankEnd;
    }

    /**
     * 
     * @param NextRankEnd
     */
    public void setNextRankEnd(String nextRankEnd) {
        this.nextRankEnd = nextRankEnd;
    }

    /**
     * 
     * @return NextRank
     */
    public String getNextRank() {
        return nextRank;
    }

    /**
     * 
     * @param NextRank
     */
    public void setNextRank(String nextRank) {
        this.nextRank = nextRank;
    }

    /**
     * 
     * @return NextRankName
     */
    public String getNextRankName() {
        return nextRankName;
    }

    /**
     * 
     * @param NextRankName
     */
    public void setNextRankName(String nextRankName) {
        this.nextRankName = nextRankName;
    }

    @Override
    public String toString() {
        return "KinopMemberInfo [MemberCode=" + memberCode + "RankStart=" + rankStart + "RankEnd=" + rankEnd + "Rank="
                + rank + "RankName=" + rankName + "SalesAmountByRankStart=" + salesAmountByRankStart
                + "SalesAmountByRankEnd=" + salesAmountByRankEnd + "SalesAmountByRank=" + salesAmountByRank
                + "SalesAmountByRankExpiration=" + salesAmountByRankExpiration + "RankApplyAmount=" + rankApplyAmount
                + "NextRankStart=" + nextRankStart + "NextRankEnd=" + nextRankEnd + "NextRank=" + nextRank
                + "NextRankName=" + nextRankName + "]";
    }

}
