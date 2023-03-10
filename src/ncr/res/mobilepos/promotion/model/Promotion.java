package ncr.res.mobilepos.promotion.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.CouponInfo;
import ncr.res.mobilepos.pricing.model.QrCodeInfo;

/**
 * The Promotion Object.
 * The Promotion object holds the Mix and Match Discount(s) for a given
 * transaction.
 * @author cc185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Promotion")
@ApiModel(value="Promotion")
public class Promotion extends ResultBase {
    /** The List of Discount(s) in a promotion. */
    @XmlElement(name = "Discount")
    private List<Discount> discounts;

    /**
     * The Revoke in a Promotion.
     */
    @XmlElement(name = "Revoke")
    private Revoke revoke;

    @XmlElement(name = "Map")
    private Map<String,Map<String,Object>> map;
    
    @XmlElement(name = "PmMap")
    private  Map<String, PmDiscountInfo> pmMap;

    @XmlElement(name = "QrCodeInfoList")
    private List<QrCodeInfo> qrCodeInfoList;

    @XmlElement(name = "CouponInfoList")
    private List<CouponInfo> couponInfoList;

    @XmlElement(name = "PromotionMessageList")
    private List<PromotionMsgInfo> promotionMsgInfoList;
    
    @XmlElement(name = "PmDeleteList")
    private Set<String> pmDeleteList;

    public Set<String> getPmDeleteList() {
		return pmDeleteList;
	}
	public void setPmDeleteList(Set<String> pmDeleteList) {
		this.pmDeleteList = pmDeleteList;
	}

    @ApiModelProperty(value="コード情報リスト", notes="コード情報リスト")
    public List<QrCodeInfo> getQrCodeInfoMap() {
        return qrCodeInfoList;
    }
    public void setQrCodeInfoList(List<QrCodeInfo> qrCodeInfoList) {
        this.qrCodeInfoList = qrCodeInfoList;
    }

    @ApiModelProperty(value="クーポン情報リスト", notes="クーポン情報リスト")
    public List<CouponInfo> getCouponInfoList() {
        return couponInfoList;
    }
    public void setCouponInfoList(List<CouponInfo> couponInfoList) {
        this.couponInfoList = couponInfoList;
    }
    @ApiModelProperty(value="販促メッセージ情報リスト", notes="販促メッセージ情報リスト")
    public List<PromotionMsgInfo> getPromotionMsgInfoMap() {
        return promotionMsgInfoList;
    }
    public void setPromotionMsgInfoList(List<PromotionMsgInfo> promotionMsgInfoList) {
        this.promotionMsgInfoList = promotionMsgInfoList;
    }
    public Map<String, Map<String, Object>> getMap() {
        return map;
    }
    public void setMap(Map<String, Map<String, Object>> map) {
        this.map = map;
    }
    /**
     * @return the discounts
     */
    @ApiModelProperty(value="割引", notes="割引")
    public final List<Discount> getDiscounts() {
        return discounts;
    }
    /**
     * @param discountsToSet the discounts to set
     */
    public final void setDiscounts(final List<Discount> discountsToSet) {
        this.discounts = discountsToSet;
    }
    /**
     * Revoke setter.
     * @param revokeToSet    The Revoke to set.
     */

    public final void setRevoke(final Revoke revokeToSet) {
        this.revoke = revokeToSet;
    }
    /**
     * @return The Revoke
     */
    @ApiModelProperty(value="設定をキャンセル", notes="設定をキャンセル")
    public final Revoke getRevoke() {
        return revoke;
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

    public Map<String, PmDiscountInfo> getPmMap() {
		return pmMap;
	}
	public void setPmMap(Map<String, PmDiscountInfo> pmMap) {
		this.pmMap = pmMap;
	}
}
