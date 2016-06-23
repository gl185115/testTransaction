package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * SubtotalDiscount info
 * Model for SubtotalDiscount information.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PointPosted")
@ApiModel(value="PointPosted")
public class PointPosted {

    /* the isPointPosted */
    @XmlElement(name = "PostPointed")
    private boolean PostPointed ;

    /* the MemberId */
    @XmlElement(name = "MemberId")
    private String MemberId ;

	/* the SalesAmt */
	@XmlElement(name = "SalesAmt")
	private String SalesAmt;

	/* the ForPointAmt */
	@XmlElement(name = "ForPointAmt")
	private String ForPointAmt;

	/* the AddPoint */
	@XmlElement(name = "AddPoint")
	private String AddPoint;

	/* the BeforePoint */
	@XmlElement(name = "BeforePoint")
	private String BeforePoint;

	/* the AfterPoint */
	@XmlElement(name = "AfterPoint")
	private String AfterPoint;

    public boolean isPostPointed() {
        return PostPointed;
    }

    public void setPostPointed(boolean postPointed) {
        PostPointed = postPointed;
    }
    @ApiModelProperty(value="会員コード", notes="会員コード")
    public String getMemberId() {
        return MemberId;
    }

    public void setMemberId(String memberId) {
        MemberId = memberId;
    }

    @ApiModelProperty(value="売価金額", notes="売価金額")
    public final String getSalesAmt() {
		return SalesAmt;
	}

	public final void setSalesAmt(String salesAmt) {
		SalesAmt = salesAmt;
	}
	 @ApiModelProperty(value="ポイントの数", notes="ポイントの数")
	public final String getForPointAmt() {
		return ForPointAmt;
	}

	public final void setForPointAmt(String forPointAmt) {
		ForPointAmt = forPointAmt;
	}
	 @ApiModelProperty(value="今回付与ポイント", notes="今回付与ポイント")
	public final String getAddPoint() {
		return AddPoint;
	}

	public final void setAddPoint(String addPoint) {
		AddPoint = addPoint;
	}
	 @ApiModelProperty(value="更新前累計ポイント", notes="更新前累計ポイント")
	public final String getBeforePoint() {
		return BeforePoint;
	}

	public final void setBeforePoint(String beforePoint) {
		BeforePoint = beforePoint;
	}
	 @ApiModelProperty(value="更新後累計ポイント", notes="更新後累計ポイント")
	public final String getAfterPoint() {
		return AfterPoint;
	}

	public final void setAfterPoint(String afterPoint) {
		AfterPoint = afterPoint;
	}

	@Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        String crlf= "\r\n";

        sb.append("PostPointed: ").append(String.valueOf(this.PostPointed));

        if(null != this.MemberId){
            sb.append(crlf).append("MemberId: ").append(this.MemberId.toString());
        }

        return sb.toString();
    }
}
