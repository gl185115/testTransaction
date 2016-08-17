package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * QRPromotionInfo Model Object.
 *
 * <P>An QRPromotionInfo Node in POSLog XML.
 *
 * @author CC185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "QRPromotionInfo")
public class QRPromotionInfo {
    /**
     * QRPromotion
     */
    @XmlElement(name="QRPromotion")
    private List<QRPromotion> qrPromotion;
    
    /**
     * @return the qrPromotion
     */
    public final List<QRPromotion> getQRPromotion() {
        return qrPromotion;
    }
    /**
     * @param qrPromotion the qrPromotion to set
     */
    public final void setQRPromotion(final List<QRPromotion> qrPromotion) {
        this.qrPromotion = qrPromotion;
    }
}
