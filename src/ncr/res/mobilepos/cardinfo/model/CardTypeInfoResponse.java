package ncr.res.mobilepos.cardinfo.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="CardTypeInfoResponse")
public class CardTypeInfoResponse extends ResultBase{

    @XmlElement(name = "CardTypeInfo")
    List<CardTypeInfo> cardTypeInfos;

    /**
     * @return the cardTypeInfos
     */
    @ApiModelProperty(value="カードタイプ情報リスト", notes="カードタイプ情報リスト")
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
