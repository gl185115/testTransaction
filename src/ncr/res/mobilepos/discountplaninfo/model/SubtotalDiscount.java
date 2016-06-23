package ncr.res.mobilepos.discountplaninfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;
/**
 * @author
 *
 * Sales person Class is a Model representation of the SubtotalDiscount List.
 */
@XmlRootElement(name = "SubtotalDiscount")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="SubtotalDiscount")
public class SubtotalDiscount extends ResultBase {

	/*This list is used to save SubtotalDiscount information*/
    @XmlElement(name = "SubtotalDiscountInfo")
    private SubtotalDiscountInfo[] SubtotalDiscount;

    /**
	 * Gets the sales person list.
	 *
	 * @return the sales person list
	 */
    @ApiModelProperty( value="小計割引メッセージ", notes="小計割引メッセージ")
    public final SubtotalDiscountInfo[] getSubtotalDiscount() {
        return SubtotalDiscount;
    }

    /**
	 * Sets the SubtotalDisExcept information of the list.
	 *
	 * @param SubtotalDiscount information ToSet
	 *            the new SubtotalDisExcept
	 */
    public final void setSubtotalDiscount(final SubtotalDiscountInfo[] subtotalDiscount) {
    	SubtotalDiscount = subtotalDiscount;
    }

    @Override
    public final String toString() {

		StringBuilder sb = new StringBuilder();
		String crlf = "\r\n";
		sb.append(super.toString());

		if( null != this.SubtotalDiscount){
			sb.append(crlf).append("SubtotalDiscount: ").append(this.SubtotalDiscount.toString());
		}

		return sb.toString();
    }
}
