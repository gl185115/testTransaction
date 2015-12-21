package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TotalAmount")
public class TotalAmount extends ResultBase {
	
	@XmlElement(name = "Cash")
    private String cash;
	
	@XmlElement(name = "Credit")
	private String credit;
	
	@XmlElement(name = "GiftCard")
	private String giftCard;
	
	/**
	 * @return the cash
	 */
	public final String getCash() {
		return cash;
	}

	/**
	 * @param cash the cash to set
	 */
	public final void setCash(final String cash) {
		this.cash = cash;
	}

	/**
	 * @return the credit
	 */
	public final String getCredit() {
		return credit;
	}

	/**
	 * @param credit the credit to set
	 */
	public final void setCredit(final String credit) {
		this.credit = credit;
	}

	/**
	 * @return the giftCard
	 */
	public final String getGiftCard() {
		return giftCard;
	}

	/**
	 * @param giftCard the giftCard to set
	 */
	public final void setGiftCard(final String giftCard) {
		this.giftCard = giftCard;
	}

	@Override
	public String toString() {
		return "TotalAmount [cash=" + cash + ", credit=" + credit
				+ ", giftCard=" + giftCard + "]";
	}
	
}
