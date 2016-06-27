package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.journalization.model.PointPosted;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AdditionalInformation")
@ApiModel(value="AdditionalInformation")
public class AdditionalInformation {

    @XmlElement(name = "Voided")
    private String voided;

    @XmlElement(name = "Returned")
    private String returned;

    @XmlElement(name = "Locked")
    private String locked;

	@XmlElement(name = "SummaryReceipt")
    private String summaryReceipt;

    @XmlElement(name = "receipt")
    private String[] receipt;

    @XmlElement(name = "receiptAttributes")
    private String[] receiptAttributes;

    @XmlElement(name = "TransactionType")
    private String transactionType;

    @XmlElement(name = "JournalLine")
    private long journalLine;

    @XmlElement(name = "Voidable")
    private String voidable;

    @XmlElement(name = "PoslogXML")
    private String poslogXML;

    @XmlElement(name="PostPointed")
    private PointPosted postPointed;

    @XmlElement(name="MemberId")
    private String memberId;

    @ApiModelProperty(value="取消", notes="取消")
	public final String getVoided() {
		return voided;
	}

	public final void setVoided(String voided) {
		this.voided = voided;
	}
	@ApiModelProperty(value="返品", notes="返品")
	public final String getReturned() {
		return returned;
	}

	public final void setReturned(String returned) {
		this.returned = returned;
	}
	@ApiModelProperty(value="ロック", notes="ロック")
	public String getLocked() {
		return locked;
	}

	public void setLocked(String locked) {
		this.locked = locked;
	}
	@ApiModelProperty(value="領収書の概要", notes="領収書の概要")
	public final String getSummaryReceipt() {
		return summaryReceipt;
	}

	public final void setSummaryReceipt(String summaryReceipt) {
		this.summaryReceipt = summaryReceipt;
	}
	@ApiModelProperty(value="領収書", notes="領収書")
	public final String[] getReceipt() {
		return receipt;
	}

	public final void setReceipt(String[] receipt) {
        this.receipt = new String[receipt.length];
        System.arraycopy(receipt, 0, this.receipt, 0, receipt.length);
	}
	@ApiModelProperty(value="領収書の属性", notes="領収書の属性")
	public final String[] getReceiptAttributes() {
		return receiptAttributes;
	}

	public final void setReceiptAttributes(String[] receipt) {
        this.receiptAttributes = new String[receipt.length];
        System.arraycopy(receipt, 0, this.receiptAttributes, 0, receipt.length);
	}
	@ApiModelProperty(value="取引のタイプ", notes="取引のタイプ")
	public final String getTransactionType() {
		return transactionType;
	}

	public final void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}
	@ApiModelProperty(value="連番", notes="連番")
    public final long getJournalLine() {
        return journalLine;
    }

    public final void setJournalLine(long line) {
        journalLine = line;
    }
    @ApiModelProperty(value="取消", notes="取消")
    public final String getVoidable() {
        return voidable;
    }

    public final void setVoidable(String s) {
        voidable = s;
    }
    @ApiModelProperty(value="PoslogのXML", notes="PoslogのXML")
    public String getPoslogXML() {
        return poslogXML;
    }

    public void setPoslogXML(String poslogXML) {
        this.poslogXML = poslogXML;
    }
    @ApiModelProperty(value="ポスト・ポイント", notes="ポスト・ポイント")
	public final PointPosted getPostPointed() {
		return postPointed;
	}

	public final void setPostPointed(PointPosted postPointed) {
		this.postPointed = postPointed;
	}
	@ApiModelProperty(value="会員コード", notes="会員コード")
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}
}
