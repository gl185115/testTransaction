package ncr.res.mobilepos.barcodeassignment.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Model for MultiForwardRecallCard barcode format from ItemCode.xml
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "multiForwardRecallCard")
@ApiModel(value="multiForwardRecallCard")
public class MultiForwardRecallCard {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> multiForwardRecallCards = new ArrayList<ItemSale>();

    /**
     * Gets the multiForwardRecallCards items list.
     *
     * @return multiForwardRecallCards
     */
    @ApiModelProperty(value="マルチフォワードリコールカードバーコードフォーマットリスト", notes="マルチフォワードリコールカードバーコードフォーマットリスト")
    public List<ItemSale> getMultiForwardRecallCards() {
        return multiForwardRecallCards;
    }

    /**
     * Sets the multiForwardRecallCards items list.
     *
     * @param multiForwardRecallCards
     */
    public void setMultiForwardRecallCards(List<ItemSale> multiForwardRecallCards) {
        this.multiForwardRecallCards = multiForwardRecallCards;
    }
}
