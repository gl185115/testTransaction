package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;
/**
 * PromotionResponse Model Object.
 *
 * Encapsulates the promotion responses.
 * Extends the {@link ResultBase} definition.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Transaction")
@ApiModel(value="PromotionResponse")
public class PromotionResponse extends ResultBase {
	/**
	 * default constructor.
	 */
	public PromotionResponse(){
		super();
	}
	/**
	 * Constructor.
	 * @param resultCode	The resulting error code.
	 * @param extendedResultCode	The extended error code.
	 * @param throwable	The exception.
	 */
	public PromotionResponse(final int resultCode,
			final int extendedResultCode, final Throwable throwable) {
		super(resultCode, extendedResultCode, throwable);
	}
    /**
     * The transaction member.
     */
    @XmlElement(name = "Transaction")
    private Transaction transaction;
    /**
     * The promotion member.
     */
    @XmlElement(name = "Promotion")
    private Promotion promotion;

    /**
     * Transaction setter.
     * @param tx the {@link Transaction} to set.
     */
    public final void setTransaction(
            final Transaction tx) {
        this.transaction = tx;
    }

    /**
     * Transaction getter.
     * @return {@link Transaction}
     */
    @ApiModelProperty(value="‹Æ–±‚ð“¾‚é", notes="‹Æ–±‚ð“¾‚é")
    public final Transaction getTransaction() {
        return transaction;
    }

    /**
     * Promotion setter.
     * @param promotionToSet The Promotion to set.
     */
    public final void setPromotion(final Promotion promotionToSet) {
        this.promotion = promotionToSet;
    }
    /**
     * Promotion getter.
     * @return  promotion.
     */
    @ApiModelProperty(value="Š„ˆøŠé‰æ", notes="Š„ˆøŠé‰æ")
    public final Promotion getPromotion() {
        return promotion;
    }

    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
