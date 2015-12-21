package ncr.res.mobilepos.cardinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
public class CardTypeInfoResponse extends ResultBase{

    @XmlElement(name = "CardTypeInfo")
    List<CardTypeInfo> cardTypeInfos;

    /**
     * @return the cardTypeInfos
     */
    public List<CardTypeInfo> getCardTypeInfos() {
        return cardTypeInfos;
    }

    /**
     * @param cardTypeInfos the cardTypeInfos to set
     */
    public void setCardTypeInfos(List<CardTypeInfo> cardTypeInfos) {
        this.cardTypeInfos = cardTypeInfos;
    }
    
    
}
