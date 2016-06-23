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
@XmlRootElement(name = "SubtotalDiscountInfo")
@ApiModel(value="SubtotalDiscountInfo")
public class SubtotalDiscountInfo {

	/* the DiscountReason */
    @XmlElement(name = "DiscountReason")
    private String DiscountReason ;

    /* the DiscountReasonName */
    @XmlElement(name = "DiscountReasonName")
    private String DiscountReasonName ;

    /**
	 * Gets the discountReason.
	 *
	 * @return the discountReason
	 */
    @ApiModelProperty(value="割引の理由", notes="割引の理由")
    public String getDiscountReason() {
        return DiscountReason;
    }

    /**
	 * Sets the discountReasonName of the column.
	 *
	 * @param discountReasonToSet
	 *            the new discountReason
	 */
    public void setDiscountReason(String discountReason) {
    	DiscountReason = discountReason;
    }

    /**
   	 * Gets the discountReasonName.
   	 *
   	 * @return the discountReasonName
   	 */
    @ApiModelProperty(value="割引の理由の名", notes="割引の理由の名")
    public String getDiscountReasonName() {
        return DiscountReasonName;
    }

    /**
   	 * Sets the discountReasonName of the column.
   	 *
   	 * @param discountReasonNameToSet
   	 *            the new discountReasonName
   	 */
    public void setDiscountReasonName(String discountReasonName) {
    	DiscountReasonName = discountReasonName;
    }

	@Override
	public final String toString() {
		StringBuilder sb = new StringBuilder();
		String crlf= "\r\n";

		if(null != this.DiscountReason){
			sb.append("DiscountReason: ").append(this.DiscountReason.toString());
		}

		if(null != this.DiscountReasonName){
			sb.append(crlf).append("DiscountReasonName: ").append(this.DiscountReasonName.toString());
		}

		return sb.toString();
	}
}
