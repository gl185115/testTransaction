package ncr.res.mobilepos.pricing.model;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DefaultTaxRate")
@ApiModel(value="DefaultTaxRate")
public class DefaultTaxRate {
    @XmlElement(name = "rate")
    private int rate;

    @XmlElement(name = "ReducedTaxRate")
    private String reducedTaxRate;

    @XmlElement(name = "ReceiptMark")
    private String receiptMark;

    @ApiModelProperty( value="デフォルト税率", notes="デフォルト税率")
    public final int getRate() {
		return rate;
	}

	public final void setRate(int rate) {
		this.rate = rate;
	}

	@ApiModelProperty( value="軽減税率フラグ", notes="軽減税率フラグ")
	public final String getReducedTaxRate() {
		return reducedTaxRate;
	}

	public final void setReducedTaxRate(String reducedTaxRate) {
		this.reducedTaxRate = reducedTaxRate;
	}

	@ApiModelProperty( value="レシートに印字するマーク", notes="レシートに印字するマーク")
	public final String getReceiptMark() {
		return receiptMark;
	}

	public final void setReceiptMark(String receiptMark) {
		this.receiptMark = receiptMark;
	}

	@Override
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String clrf = "; ";
        str.append("rate: ").append(rate).append(clrf)
        .append("ReducedTaxRate: ").append(reducedTaxRate).append(clrf)
    	.append("ReceiptMark: ").append(receiptMark).append(clrf);
        return str.toString();
    }

}
