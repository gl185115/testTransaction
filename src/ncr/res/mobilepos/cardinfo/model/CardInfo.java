package ncr.res.mobilepos.cardinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CardInfo")
public class CardInfo extends ResultBase {
	@XmlElement(name = "CardClassInfo")
	private CardClassInfo cardClassInfo;
	@XmlElement(name = "CardClassInfoList")
	private List<CardClassInfo> cardClassInfoList;
	
	public final void setCardClassInfo(CardClassInfo cardClassInfo) {
		this.cardClassInfo = cardClassInfo;
	}
	
	public final CardClassInfo getCardClassInfo() {
		return cardClassInfo;
	}
	
	public final void setCardClassInfoList(List<CardClassInfo> cardClassInfoList) {
		this.cardClassInfoList = cardClassInfoList;
	}
	
	public final List<CardClassInfo> getCardClassInfoList() {
		return cardClassInfoList;
	}
	
	@Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("CardClassInfo: ").append(cardClassInfo).append("; ");
        sb.append("CardClassInfoList: ").append(cardClassInfoList).append("; ");
        return sb.toString();
    }
}
